package dao.impl;

import com.google.inject.Singleton;
import dao.ConfigInfoRepository;
import dao.QuestionFeedbackRepository;
import models.ConfigInfo;
import models.QuestionFeedback;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Singleton
public class JPAConfigInfoRepository implements ConfigInfoRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAConfigInfoRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext){
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<List<ConfigInfo>> findAll(ConfigInfo configInfo) {
        return supplyAsync(()->{
            return  jpaApi.withTransaction(questionList->{
                return questionList.createQuery("select p from configInfo p where p.configId="+configInfo.getConfigId(), ConfigInfo.class).getResultList();
            });
        });
    }
}
