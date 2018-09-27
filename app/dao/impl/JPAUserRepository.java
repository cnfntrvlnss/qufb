package dao.impl;

import dao.UserRepository;
import models.MyRole;
import models.Section;
import models.Unit;
import models.User;
import play.Logger;
import play.db.jpa.JPAApi;
import util.PeekingIterator;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class JPAUserRepository implements UserRepository {
    private final Logger.ALogger logger = Logger.of(JPAQuestionFeedbackRepository.class);

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAUserRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext){
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;

        populateRoles();
    }

    enum RoLe {
        ADMIN, STAFF, MANAGER, VP;
    }

    //把RoLe中的元素同步到数据库中
    private void populateRoles() {
        findAllRoles().thenAccept(rs -> {
            List<String> roles = Arrays.asList(RoLe.values()).stream().map(r -> r.toString()).sorted().collect(Collectors.toList());
            List<String> myroles = rs.stream().map(r-> r.getId()).sorted().collect(Collectors.toList());
            PeekingIterator<String> it0 = new PeekingIterator(roles.iterator());
            PeekingIterator<String> it = new PeekingIterator(myroles.iterator());
            while(it0.hasNext() && it.hasNext()){
                int ret = it.peek().compareTo(it0.peek());
                if(ret > 0){
                    MyRole r = new MyRole();
                    r.setId(it0.next());
                    _addRole(r);
                }else if(ret < 0){
                    _deleteRole(it.next());
                }else{
                    it.next();
                    it0.next();
                }
            }
            while(it0.hasNext()){
                MyRole r = new MyRole();
                String id = it0.next();
                r.setId(id);
                _addRole(r);
            }
            while(it.hasNext()){
                _deleteRole(it.next());
            }
        }).toCompletableFuture().join();
    }

    //包装
    private CompletionStage<Void> wrap(Function<EntityManager, Void> fn){
        return supplyAsync(() -> jpaApi.withTransaction(fn), executionContext);
    }

    @Override
    public CompletionStage<User> findById(String id){
        return supplyAsync(() -> jpaApi.withTransaction(em ->{
            User u = em.find(User.class, id);
            return u;
        }), executionContext);
    }


    @Override
    public CompletionStage<List<Section>> findSections(){
        return supplyAsync(() -> jpaApi.withTransaction((EntityManager em) -> {
            return em.createNamedQuery("Section.findAll", Section.class).getResultList();
        }));
    }

    @Override
    public CompletionStage<List<Unit>> findUnitsBySection(Integer sectionId){
        return supplyAsync(() -> jpaApi.withTransaction(em ->{
            return em.createNamedQuery("Unit.findBySection", Unit.class).setParameter("sectionId", sectionId)
                    .getResultList();
        }));
    }

    @Override
    public CompletionStage<List<Unit>> findUnitsBySectionName(String sectionName) {
        return supplyAsync(()->jpaApi.withTransaction((EntityManager em)->{
            return em.createNamedQuery("Unit.findAllBySectionName").setParameter("sectionName", sectionName)
                    .getResultList();
        }));
    }

    @Override
    public CompletionStage<List<User>> findUsersByUnit(Integer unitId){
        return supplyAsync(()-> jpaApi.withTransaction((EntityManager em) -> {
                Unit u = em.find(Unit.class, unitId);
                if(u == null) return new ArrayList<>();
                logger.debug("in findUsersByUnit, staffs: {}", u.getStaffs().size());
                return u.getStaffs();
            }
         ));
    }

    @Override
    public CompletionStage<List<User>> findUsersByUnitName(String sectionName, String unitName) {
        return supplyAsync(() -> jpaApi.withTransaction((EntityManager em)->{
            List<Unit> units = em.createNamedQuery("Unit.findByName", Unit.class).setParameter("name", unitName)
                    .setParameter("sectionName", sectionName)
                    .getResultList();
            if(units.size() == 0){
                return new ArrayList<User>();
            }else{
                return units.get(0).getStaffs();
            }
        }));
    }

    public CompletionStage<List<User>> findUsersBySection(Integer sectionId) {
        return supplyAsync(() -> jpaApi.withTransaction((EntityManager em) -> {
            List<Unit> us = em.createNamedQuery("Unit.findBySection", Unit.class).setParameter("sectionId", sectionId)
                    .getResultList();
            List<User> users = new ArrayList<>();
            for(Unit u: us){
                users.addAll(u.getStaffs());
            }
            return users;
        }));
    }

    @Override
    public CompletionStage<List<User>> findUsersBySectionName(String sectionName) {
        return supplyAsync(() -> jpaApi.withTransaction((EntityManager em) -> {
            List<Unit> units = em.createNamedQuery("Unit.findAllBySectionName").setParameter("sectionName", sectionName)
                    .getResultList();
            List<User> users = new ArrayList<User>();
            for(Unit u: units){
                List<Unit> curUnits = em.createNamedQuery("Unit.findByName", Unit.class).setParameter("name", u.getName())
                        .setParameter("sectionName", sectionName)
                        .getResultList();
                if(curUnits.size() > 0){
                    users.addAll(u.getStaffs());
                }
            }
            return users;
        }));
    }

    @Override
    public CompletionStage<Optional<Section>> findSectionData(String sectionName){
        return supplyAsync(() -> jpaApi.withTransaction((EntityManager em) -> {
            List<Section> sections = em.createNamedQuery("Section.findByName", Section.class)
                    .setParameter("name", sectionName).getResultList();
            if(sections.size() == 0) return Optional.empty();
            else{
                Section sec = sections.get(0);
                for(Unit u: sec.getUnits()){
                    u.getStaffs().size();
                }
                return Optional.of(sec);
            }
        }));
    }

    private void _addUnitsIfNone(EntityManager em, List<Unit> units){
        for(Unit u: units) {
            User mana = u.getManager();
            u.setManager(null);
            List<Section> secs = em.createNamedQuery("Section.findByName", Section.class)
                    .setParameter("name", u.getSection().getName())
                    .getResultList();
            if(secs.isEmpty()){
                em.persist(u.getSection());
            }else{
                u.setSection(secs.get(0));
            }
            List<Unit> us = em.createNamedQuery("Unit.findByName", Unit.class)
                    .setParameter("name", u.getName())
                    .setParameter("parentName", u.getSection().getName())
                    .getResultList();
            if(us.isEmpty()){
                em.persist(u);
                em.createNamedQuery("Unit.updateManager").setParameter("id", u.getId())
                        .setParameter("userId", mana.getUserId()).executeUpdate();

            }else{
                u.setId(us.get(0).getId());
            }

        }
    }

    public CompletionStage<Void> addSection(Section section){
        return wrap(em -> {
            em.persist(section);
            return null;
        });
    }

    //添加处及部门，若部门不存在就添加部门，在判定部门下的处存在与否，若不存在处就添加处。
    @Override
    public CompletionStage<Void> addUnitsIfNone(List<Unit> units){
        return wrap(em->{
            _addUnitsIfNone(em, units);
            return null;
        });
    }

    @Override
    public CompletionStage<Void> deleteUnitsById(List<Integer> unitIds){
        return wrap(em -> {
            for(Integer id: unitIds){
                Unit u = em.find(Unit.class, id);
                for(User user: u.getStaffs()){
                    em.remove(user);
                }
                em.remove(u);
            }
            return null;
        });
    }

    @Override
    public CompletionStage<Void> deleteDeptById(Integer deptId) {
        return wrap( em -> {
            Section section = em.find(Section.class, deptId);
            for(Unit unit: section.getUnits()){
                for(User user: unit.getStaffs()){
                    em.remove(user);
                }
                em.remove(unit);
            }
            em.remove(section);
            return null;
        });
    }

    @Override
    public CompletionStage<Void> readdUsers(List<User> users){
        return wrap(em -> {
            for(User u: users){
                User u1 = em.find(User.class, u.getUserId());
                if(u1 != null){
                    em.remove(u1);
                }
                em.persist(u);
            }
            return null;
        });
    }

    @Override
    public CompletionStage<Void> updateUser(User user){
        return wrap(em -> {
            User user1 = em.find(User.class, user.getUserId());
            user1.setRoles(user.getRoles());
            return null;
        });
    }

    /**
     * 先添加section, 再添加section下的units, 最后添加unit下的staffs
     * section， unit已经存在了，就不在添加; user要已经存在，就删除旧的.
     *
     * @param section
     * @return
     */
    @Override
    public CompletionStage<Void> addSectionRecur(Section section) {
        return wrap(em -> {
            List<Section> sections = em.createNamedQuery("Section.findByName", Section.class)
                    .setParameter("name", section.getName())
                    .getResultList();
            List<Unit> units = section.getUnits();
            if(sections.size() == 0){
                em.persist(section);
            }else{
                section.setId(sections.get(0).getId());
            }

            List<Unit> units1 = new ArrayList<>();
            for(Unit u: units) {
                List<Unit> us = em.createNamedQuery("Unit.findByName", Unit.class)
                        .setParameter("name", u.getName())
                        .setParameter("parentName", u.getSection().getName())
                        .getResultList();
                List<User> users = u.getStaffs();
                u.setStaffs(null);// 后添加staffs，就需要在更新unit时设置为null.
                User manager = u.getManager();
                u.setManager(null);//后添加Manager，就需要在更新unit时设置为null.
                if(us.isEmpty()){
                    em.persist(u);
                    if(manager != null){
                        u.setManager(em.find(User.class, manager.getUserId()));
                    }
                }else{
                    u = us.get(0);
                }

                //user中的Unit必须要被managed，才能保存到数据库.
                for(User user: users) {
                    User user1 = em.find(User.class, user.getUserId());
                    if(user1 != null){
                        em.remove(user1);
                    }
                    user.setUnit(u);
                    em.persist(user);
                }
                //恢复unit数据原样，保证函数返回后数据的完整性。
                //返回的实体的各字段要来自数据库，因为这是在事务里面。
                u.setStaffs(users);
                units1.add(u);
            }
            section.setUnits(units1);

            return null;
        });
    }

    // 参数section中所有的实体要存在，不然会报错，更新失败。
    //支持的功能： 只是更新部分，字段，更新部门经理，处经理，更新用户权限。
    @Override
    public CompletionStage<Void> updateSectionRecur(Section section) {
        return wrap(em -> {
            List<Section> sections = em.createNamedQuery("Section.findByName", Section.class)
                    .setParameter("name", section.getName())
                    .getResultList();
            List<Unit> units = section.getUnits();
            if(sections.size() == 0){
                throw new RuntimeException("section " + section.getName() + " does not exist, in updateSectionRecur.");
            }else{
                section.setId(sections.get(0).getId());
            }

            List<Unit> units1 = new ArrayList<>();
            for(Unit u: units) {
                List<Unit> us = em.createNamedQuery("Unit.findByName", Unit.class)
                        .setParameter("name", u.getName())
                        .setParameter("parentName", u.getSection().getName())
                        .getResultList();
                List<User> users = u.getStaffs();
                u.setStaffs(null);// 后添加staffs，就需要在更新unit时设置为null.
                User manager = u.getManager();
                u.setManager(null);//后添加Manager，就需要在更新unit时设置为null.
                if(us.isEmpty()){
                    throw new RuntimeException("unit " + section.getName() + "---" + u.getName() +  " does not exist, " +
                            "in updateSectionRecur.");
                }else{
                    u = us.get(0);
                    u.setManager(em.find(User.class, manager.getUserId()));
                }
                units1.add(u);

                List<User> users1 = new ArrayList<>();
                for(User user: users) {
                    User user1 = em.find(User.class, user.getUserId());
                    if(user1 == null){
                        throw new RuntimeException("user " + user.getUserId() + " does not exist, in updateSectionRecur.");
                    }
                    user1.setRoles(user.getRoles());
                }
                //恢复unit数据原样，保证函数返回后数据的完整性
                u.setStaffs(users1);

            }
            section.setUnits(units1);

            return null;
        });
    }

    @Override
    public CompletionStage<Void> deleteAllUsers(){
        return wrap(em ->{
            em.createQuery("delete from User").executeUpdate();
            return null;
        });
    }

    @Override
    public CompletionStage<Void> deleteUsersById(List<String> userIds){
        return wrap(em -> {
            for(String id: userIds){
                User user = em.find(User.class, id);
                if(user != null){
                    em.remove(user);
                }
            }
            return null;
        });
    }

    @Override
    public CompletionStage<List<MyRole>> findAllRoles(){
        return supplyAsync(() -> jpaApi.withTransaction( (EntityManager em) -> {
            return em.createQuery("select r from MyRole r").getResultList();
        }), executionContext);
    }

    private Void _addRole(MyRole role){
        return jpaApi.withTransaction(em ->{
            em.persist(role);
            return null;
        });
    }

    public CompletionStage<Void> addRole(MyRole role){
        return supplyAsync(()-> _addRole(role));
    }

    private Void _deleteRole(String id){
        return jpaApi.withTransaction(em -> {
            MyRole r = em.find(MyRole.class, id);
            if(r != null){
                em.remove(r);
            }
            return null;
        });
    }

    public CompletionStage<Void> deleteRole(String id){
        return  supplyAsync(()-> _deleteRole(id));
    }
}
