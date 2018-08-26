package services;

import models.QuestionFeedback;

import java.util.List;

/**
 * 问题反馈接口
 * lixin
 * 2018-8-24 10:56:35
 */

public interface QuestionFeedbackService {
    List<QuestionFeedback> getQuestionFeedbackList();
}
