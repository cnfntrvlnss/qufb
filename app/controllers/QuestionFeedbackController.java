package controllers;

import javax.inject.Inject;

import models.QuestionFeedback;
import models.QuestionFeedbackRepository;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;


public class QuestionFeedbackController extends Controller {

	@Inject
	QuestionFeedbackRepository questRepo;

	/**
     * 我的问题列表
	 * lixin
	 * 2018-8-24 10:50:25
     * @return
     */
    public  Result myQuestion() {
    	CompletionStage<Stream<QuestionFeedback>> questionList= questRepo.findAll();

    	return ok(myQuestion.render(questionList));
    }

}
