package dao;

import com.google.inject.ImplementedBy;
import models.QuestionFeedback;
import dao.impl.JPAQuestionFeedbackRepository;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;
import java.util.List;

@ImplementedBy(JPAQuestionFeedbackRepository.class)
public interface QuestionFeedbackRepository {
    /**
     * 获取所有的问题列表
     * @return
     */
     CompletionStage<List<QuestionFeedback>> findAll();

    /**
     * 通过id获取一个反馈问题信息
     * @param id
     * @return
     */
    CompletionStage<QuestionFeedback> findById(Integer id);

    /**
     * 保存一个问题
     * @param fb
     * @return
     */
     CompletionStage<Void> save(QuestionFeedback fb);

    /**
     * 更新一个问题
     * @param fb
     * @return
     */
     CompletionStage<Void> update(QuestionFeedback fb);

    /**
     * 判断条件是否为空的更新方法
     * @param fb
     * @return
     */
     CompletionStage<Void> updateNotNull(QuestionFeedback fb);

}
