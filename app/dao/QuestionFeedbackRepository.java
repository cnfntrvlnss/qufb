package dao;

import com.google.inject.ImplementedBy;
import models.QuestionFeedback;
import dao.impl.JPAQuestionFeedbackRepository;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;
import java.util.List;

@ImplementedBy(JPAQuestionFeedbackRepository.class)
public interface QuestionFeedbackRepository {

    public CompletionStage<List<QuestionFeedback>> findAll();

    CompletionStage<QuestionFeedback> findById(Integer id);

    public CompletionStage<Void> save(QuestionFeedback fb);
    public CompletionStage<Void> update(QuestionFeedback fb);
    public CompletionStage<Void> updateNotNull(QuestionFeedback fb);

}
