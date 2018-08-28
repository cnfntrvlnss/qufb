package dao;

import models.QuestionFeedback;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;
import java.util.List;

public interface QuestionFeedbackRepository {

    public CompletionStage<List<QuestionFeedback>> findAll();

}
