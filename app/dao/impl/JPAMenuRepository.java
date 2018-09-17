package dao.impl;

import dao.MenuRepository;
import models.Menu;
import models.QuestionFeedback;
import play.Logger;
import play.db.jpa.JPAApi;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class JPAMenuRepository implements MenuRepository {
    private final Logger.ALogger logger = Logger.of(JPAQuestionFeedbackRepository.class);

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAMenuRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext){
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<List<Menu>> listMenu() {
        return supplyAsync(()-> jpaApi.withTransaction(menuList-> menuList.createQuery("select p from Menu p ", Menu.class).getResultList()));
    }
}
