package dao;

import models.Project;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

public interface ProjectRepository {

    public CompletionStage<Stream<Project>> findAll();
    public CompletionStage<Project> findByName(String projectName);
}
