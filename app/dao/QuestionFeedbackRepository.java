package dao;

import models.QuestionFeedback;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

public interface QuestionFeedbackRepository {

    public CompletionStage<Stream<QuestionFeedback>> findAll();

}
