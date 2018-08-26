package dao;

import models.QuestionFeedback;

import java.util.List;

public class QuestionFeedbackDao {

   public  List<QuestionFeedback> getQuestionFeedbackList(){
      return QuestionFeedback.findAll();
   }
}
