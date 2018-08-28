package dao.impl;

import com.google.inject.Singleton;
import models.Project;
import dao.ProjectRepository;
import org.springframework.stereotype.Repository;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Singleton
public class JPAProjectRepository implements ProjectRepository {
    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAProjectRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext){
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Stream<Project>> findAll() {
        return supplyAsync(()->{
            return jpaApi.withTransaction(entityManager -> {
                return entityManager.createQuery("select p from Project p", Project.class)
                .getResultList().stream();
            });
        }, executionContext);
    }

    @Override
    public CompletionStage<Project> findByName(String projectName) {
        return supplyAsync(()->{
            return jpaApi.withTransaction(entityManager -> {
                return entityManager.createQuery("select p from Project p", Project.class)
                        .getSingleResult();
            });
        }, executionContext);
    }
}
