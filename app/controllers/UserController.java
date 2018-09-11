package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.fasterxml.jackson.databind.JsonNode;
import dao.impl.JPAUserRepository;
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

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class UserController extends Controller {
    private final Logger.ALogger logger = Logger.of(UserController.class);

    @Inject
    FormFactory formFactory;
    @Inject
    private HttpExecutionContext ec;
    @Inject
    JPAUserRepository userRepo;

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
                if(!user.getPassword().equals(password)){
                    return forbidden("password is not matched!");
                }
            }
            session().put("username", name);
            // session().put("email", user.getEmail());
            return ok(views.html.myQuestionSubmit.render());
        }, ec.current());
    }

    public Result logout(){
        session().clear();
        return ok("bye");
    }

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
         users = users.stream().map((User u1) ->{
            Unit u = u1.getUnit();
            if(unitMap.containsKey(u.getName())){
                u1.setUnit(unitMap.get(u.getName()));
            }
            return u1;
        }).collect(Collectors.toList());
        //添加用户列表
        //logger.debug("in testAddUser, before: {}", Json.toJson(users));
        userRepo.addUsers(users).toCompletableFuture().join();
        //logger.debug("in testAddUser, after: {}", Json.toJson(users));

        return ok();
    }

    public Result deleteAllUsers(){
        String userId = session().get("userId");
        if(userId == null) userId = "";
        //logger.debug("delete all users for test! ");
        userRepo.deleteAllUsers().toCompletableFuture().join();

        return ok(userId);
    }

    @Restrict(@Group("ADMIN"))
    public Result testSecure(){
        String userId = session().get("username");
        return ok("pass " + userId);
    }

}
