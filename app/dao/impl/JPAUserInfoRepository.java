package dao.impl;

import play.Logger;
import play.db.jpa.JPAApi;

import javax.inject.Inject;

public class JPAUserInfoRepository {
    private final Logger.ALogger logger = Logger.of(JPAQuestionFeedbackRepository.class);

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAUserInfoRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext){
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }


}
