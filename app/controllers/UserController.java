package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.UserRepository;
import models.MyRole;
import models.Section;
import models.Unit;
import models.User;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import util.HashFunctions;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class UserController extends Controller {
    private final Logger.ALogger logger = Logger.of(UserController.class);

    @Inject
    FormFactory formFactory;
    @Inject
    private HttpExecutionContext ec;
    @Inject
    UserRepository userRepo;

    private String createHashed(String user, String pwd){
        return HashFunctions.createHash(user + pwd);
    }

    public CompletionStage<Result> login(){
        DynamicForm form = formFactory.form().bindFromRequest();
        String name = form.get("name");
        String password = form.get("password");
        logger.debug("in login, name: {}; password: {}", name, password);

        return userRepo.findById(name).thenApplyAsync(user -> {
            if(user == null){
                return forbidden("user is not found!");
            }
            if(user.getPassword() != null){
                String hashed = createHashed(user.getUserId(), user.getPassword());
                if(!hashed.equals(password)){
                    return forbidden("password is not matched!");
                }
            }
            session().put("username", name);
            // session().put("email", user.getEmail());
            return ok(views.html.myQuestionSubmit.render(name));
        }, ec.current());
    }

    public Result logout(){
        session().clear();
        return ok("bye");
    }

    /*****
     * 当前的导入逻辑是, 提交json中前一部分是部门处信息，后一部分是用户列表信息；
     * 部门处信息已经存在就添加不进去了， 用户信息已经存在就先删除旧的再添加新的。
     * @return
     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result addUsers(){
        JsonNode body = request().body().asJson();
        JsonNode unitPart = body.get("units");
        List<Unit> units = new ArrayList<Unit>();
        for(int i=0; i< unitPart.size(); i++){
            units.add(Json.fromJson(unitPart.get(i), Unit.class));
        }
        //先添加部门，处信息
        userRepo.addUnitsIfNone(units).toCompletableFuture().join();

        Map<String, Unit> unitMap = units.stream().collect(HashMap::new, (r, t)->{
            r.put(t.getSection().getName() + "---" + t.getName(), t);
        }, HashMap::putAll);

        JsonNode userPart = body.get("users");
        List<User> users = new ArrayList<User>();
        for(JsonNode n: userPart){
            users.add(Json.fromJson(n, User.class));
        }
         users = users.stream().map((User u1) -> {
             if(u1.getPassword() != null){
                 u1.setPassword(createHashed(u1.getUserId(), u1.getPassword()));
             }
            Unit u = u1.getUnit();
            if(unitMap.containsKey(u.getName())){
                u1.setUnit(unitMap.get(u.getName()));
            }
            return u1;
        }).collect(Collectors.toList());
        //添加用户列表
        //logger.debug("in testAddUser, before: {}", Json.toJson(users));
        userRepo.readdUsers(users).toCompletableFuture().join();
        //logger.debug("in testAddUser, after: {}", Json.toJson(users));

        return ok();
    }

    private void modifyUserNode(ObjectNode userNode) {
        if(userNode.get("userId") == null) {
            String email = userNode.get("email").asText();
            String userId = email.substring(0, email.indexOf("@"));
            userNode.put("userId", userId);
        }
        if(userNode.get("roles") != null){
            JsonNode rolesNode = userNode.get("roles");
            if(rolesNode.isArray()){
                //修改角色结构
                if(rolesNode != null){
                    ArrayNode nroles = Json.newArray();
                    for(int j=0; j< rolesNode.size(); j++){
                        ObjectNode nrole = Json.newObject();
                        nrole.set("id", rolesNode.get(j));
                        nroles.add(nrole);
                    }
                    userNode.set("roles", nroles);
                }
            }
        }
    }

    private void modifyDepartmentNode(JsonNode body){
        ObjectNode bodyNode = (ObjectNode) body;
        ArrayNode unitsNode = bodyNode.withArray("units");

        for(int i=0; i< unitsNode.size(); i++) {
            JsonNode staffsNode = unitsNode.get(i).get("staffs");
            if(staffsNode == null) continue;
            for(int j=0; j< staffsNode.size(); j++){
                modifyUserNode((ObjectNode)staffsNode.get(j));
            }
        }

        if(body.get("staffs") != null || body.get("manager") != null){
            //创建经理处，适应本数据库的设计
            ObjectNode objNode = Json.newObject();
            objNode.put("name", "经理处");
            if(body.get("staffs") != null){
                ArrayNode usersNode = (ArrayNode) body.get("staffs");
                for(int i=0; i< usersNode.size(); i++){
                    modifyUserNode((ObjectNode)usersNode.get(i));
                }
                objNode.set("staffs", body.get("staffs"));
                bodyNode.remove("staffs");
            }
            if(body.get("manager") != null){
                objNode.set("manager", body.get("manager"));
                bodyNode.remove("manager");
            }
            unitsNode.insert(0, objNode);
        }
    }

    private Section parseDepartmentJson(JsonNode deptNode){
        Section section = Json.fromJson(deptNode, Section.class);
        JsonNode unitsNode = deptNode.get("units");
        List<Unit> units = new ArrayList<>();
        for(int i=0; i< unitsNode.size(); i++){
            Unit unit = Json.fromJson(unitsNode.get(i), Unit.class);
            unit.setSection(section);
            units.add(unit);
            JsonNode staffsNode = unitsNode.get(i).get("staffs");
            List<User> users = new ArrayList<>();
            if(staffsNode != null){
                for(int j=0; j< staffsNode.size(); j++) {
                    User user = Json.fromJson(staffsNode.get(j), User.class);
                    user.setUnit(unit);
                    users.add(user);
                }
            }
            unit.setStaffs(users);
            JsonNode managerNode = unitsNode.get(i).get("manager");
            if(managerNode != null){
                unit.setManager(new User(){
                    {
                        setUserId(managerNode.asText());
                    }
                });
            }
        }
        section.setUnits(units);
        return section;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> addUsersN(){

        JsonNode body = request().body().asJson();
        CompletionStage stags = CompletableFuture.completedFuture(null);
        if(body.get("departments") == null){
            modifyDepartmentNode(body);
            logger.debug("in addUsersN, after modification, body: {}", Json.prettyPrint(body));
            Section dept = parseDepartmentJson(body);
            stags = stags.thenComposeAsync(v -> userRepo.updateSectionRecur(dept));
        } else{
            for(int i=0; i <body.get("departments").size(); i++){
                JsonNode deptNode = body.get("departments").get(i);
                modifyDepartmentNode(deptNode);
                logger.debug("in addUsersN, after modification, body: {}", Json.prettyPrint(deptNode));
                Section dept = parseDepartmentJson(deptNode);
                stags = stags.thenComposeAsync(v -> userRepo.updateSectionRecur(dept));
            }
        }
        return stags.thenApplyAsync(v -> ok(), ec.current());
    }

    //生成的Json格式与导入的是一致的
    private JsonNode convertSectionToJson(Section section){
        ObjectNode secNode = (ObjectNode) Json.toJson(section);
        if(secNode.get("units") == null){
            List<Unit> units = section.getUnits();
            ArrayNode unitsNode = Json.newArray();

            for(Unit u: units){
                ObjectNode unitNode = (ObjectNode) Json.toJson(u);
                if(u.getManager() != null){
                    unitNode.put("manager", u.getManager().getUserId());
                }
                unitNode.remove("section");
                ArrayNode staffsNode = Json.newArray();
                List<User> staffs = u.getStaffs();
                for(User user: staffs){
                    ObjectNode staffNode = (ObjectNode) Json.toJson(user);
                    staffNode.remove("unit");
                    ArrayNode rolesNode = (ArrayNode) staffNode.get("roles");
                    ArrayNode simpleNode = Json.newArray();
                    for(int i=0; i< rolesNode.size(); i++){
                        simpleNode.add(rolesNode.get(i).get("id"));
                    }
                    staffNode.set("roles", simpleNode);
                    staffsNode.add(staffNode);
                }

                unitNode.set("staffs", staffsNode);
                if(unitNode.get("name").asText().equals("经理处")){
                    if(unitNode.get("manager") != null){
                        secNode.set("manager", unitNode.get("manager"));
                    }
                    if(unitNode.get("staffs") != null){
                        secNode.set("staffs", unitNode.get("staffs"));
                    }
                }else{
                    unitsNode.add(unitNode);
                }
            }
            secNode.set("units", unitsNode);
        }

        return secNode;
    }

    public CompletionStage<Result> fetchDepartmentData(String sectionName){
        return userRepo.findSectionData(sectionName).thenApplyAsync(section -> {
            //生成Json文档返回
            if(!section.isPresent()) return noContent();
            ArrayNode secsNode = Json.newArray();
            secsNode.add(convertSectionToJson(section.get()));
            ObjectNode bodyNode = Json.newObject();
            bodyNode.set("departments", secsNode);
            return ok(bodyNode);
        });
    }

    @BodyParser.Of(BodyParser.MultipartFormData.class)
    public CompletionStage<Result> addUser(){
        Map<String, String[]> m = request().body().asMultipartFormData().asFormUrlEncoded();
        Section section = new Section();
        section.setName(m.get("sectionName")[0]);
        Unit unit = new Unit();
        if(m.get("unitName")[0].equals("无")){
            unit.setName("经理处");
        }else{
            unit.setName(m.get("unitName")[0]);
        }
        User user = new User();
        user.setUserId(m.get("userId")[0]);
        user.setName(m.get("name")[0]);
        user.setNumber(m.get("number")[0]);
        user.setEmail(m.get("email")[0]);
        user.setRoles(new ArrayList<MyRole>());
        String[] roles = m.get("roles");
        for(int i=0; i< roles.length; i++){
            MyRole role = new MyRole();
            role.setId(roles[i]);
            user.getRoles().add(role);
        }

        section.setUnits(new ArrayList<>());
        section.getUnits().add(unit);
        unit.setSection(section);
        unit.setStaffs(new ArrayList<>());
        unit.getStaffs().add(user);
        user.setUnit(unit);
        return userRepo.updateSectionRecur(section).thenApplyAsync(v ->  ok(convertSectionToJson(section)), ec.current());
    }

    public CompletionStage<Result> deleteAllUsers(){
        return userRepo.deleteAllUsers().thenApplyAsync(v -> {
            return ok();
        }, ec.current());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> deleteUsers() {
        JsonNode userIdsNode = request().body().asJson();
        List<String> userIds = Json.fromJson(userIdsNode, new ArrayList<String>().getClass());
        //logger.debug("!!!!!!!!!!!!!! in deleteUsers, {}", Json.prettyPrint(Json.toJson(userIds)));

        return userRepo.deleteUsersById(userIds).thenApplyAsync(v -> {
            return ok();
        }, ec.current());
    }

    @BodyParser.Of(BodyParser.MultipartFormData.class)
    public CompletionStage<Result> deleteUnits(){
        List<String> unitIdsStr = Arrays.asList(request().body().asMultipartFormData().asFormUrlEncoded().get("unitId"));
        List<Integer> unitIds = unitIdsStr.stream().map(Integer::valueOf).collect(Collectors.toList());
        return userRepo.deleteUnitsById(unitIds).thenApplyAsync(v -> ok(Json.toJson(unitIds)));
    }

    @Restrict(@Group("ADMIN"))
    public Result testSecure(){
        String userId = session().get("username");
        return ok("pass " + userId);
    }

}
