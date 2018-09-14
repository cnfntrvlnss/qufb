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
        ADMIN, STAFF, MANAGER;
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

    public CompletionStage<Void> addUsers(List<User> users) {
        return wrap(em -> {
            for(User u: users){
                em.persist(u);
            }
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

    /**
     * 先添加section, 再添加section下的units, 最后添加unit下的staffs
     * section， unit已经存在了，就不在添加; user先删除旧的，再添加新的.
     * @param section
     * @return
     */
    @Override
    public CompletionStage<Void> updateSectionInner(Section section){
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
                        em.createNamedQuery("Unit.updateManager").setParameter("id", u.getId())
                                .setParameter("userId", manager.getUserId()).executeUpdate();
                    }
                }else{
                    u.setId(us.get(0).getId());
                }
                Unit u1 = em.merge(u);
                //user中的Unit必须要被managed，才能保持user.
                for(User user: users) {
                    User user1 = em.find(User.class, user.getUserId());
                    if(user1 != null){
                        em.remove(user1);
                    }
                    user.setUnit(u1);
                    em.persist(user);
                }
            }

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
