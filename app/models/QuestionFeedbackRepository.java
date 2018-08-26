package models;

import javax.annotation.processing.Completion;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

public interface QuestionFeedbackRepository {

    public CompletionStage<Stream<QuestionFeedback>> findAll();

}
