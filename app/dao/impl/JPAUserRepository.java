package dao.impl;

import models.Section;
import models.Unit;
import models.User;
import play.Logger;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class JPAUserRepository {
    private final Logger.ALogger logger = Logger.of(JPAQuestionFeedbackRepository.class);

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAUserRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext){
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }


    //包装
    private CompletionStage<Void> wrap(Function<EntityManager, Void> fn){
        return supplyAsync(() -> jpaApi.withTransaction(fn), executionContext);
    }



    public CompletionStage<User> findById(String id){
        return supplyAsync(() -> jpaApi.withTransaction(em ->{
            User u = em.find(User.class, id);
            return u;
        }), executionContext);
    }

    public CompletionStage<List<Unit>> findAllUnitBySection(String sectionName){
        return supplyAsync(()->jpaApi.withTransaction((EntityManager em)->{
            return em.createNamedQuery("Unit.findAllBySection").setParameter("sectionName", sectionName)
                    .getResultList();
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

    //添加处及部门，若部门不存在就添加部门，在判定部门下的处存在与否，若不存在处就添加处。
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

    public CompletionStage<Void> deleteAllUsers(){
        return wrap(em ->{
            em.createQuery("delete from User").executeUpdate();
            return null;
        });
    }


}
