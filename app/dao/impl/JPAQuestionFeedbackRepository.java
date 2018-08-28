package dao.impl;

import com.google.inject.Singleton;
import dao.QuestionFeedbackRepository;
import models.QuestionFeedback;
import play.db.jpa.JPAApi;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@Singleton
public class JPAQuestionFeedbackRepository implements QuestionFeedbackRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAQuestionFeedbackRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext){
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<List<QuestionFeedback>> findAll() {
        return supplyAsync(()->{
            return  jpaApi.withTransaction(questionList->{
                return questionList.createQuery("select p from QuestionFeedback p", QuestionFeedback.class).getResultList();
            });
        });
    }
}
