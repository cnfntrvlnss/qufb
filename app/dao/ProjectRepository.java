package dao;

import com.google.inject.ImplementedBy;
import models.Project;
import dao.impl.JPAProjectRepository;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@ImplementedBy(JPAProjectRepository.class)
public interface ProjectRepository {

    public CompletionStage<Stream<Project>> findAll();
    public CompletionStage<Project> findByName(String projectName);
}
