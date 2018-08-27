package controllers;

import javax.inject.Inject;

import models.QuestionFeedback;
import dao.QuestionFeedbackRepository;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;


public class QuestionFeedbackController extends Controller {

	@Inject
	QuestionFeedbackRepository questRepo;
	@Inject
	private HttpExecutionContext ec;

	/**
     * 我的问题列表
	 * lixin
	 * 2018-8-24 10:50:25
     * @return
     */
    public  CompletionStage<Result> myQuestion() {
    	CompletionStage<Stream<QuestionFeedback>> questionList= questRepo.findAll();
		return questionList.thenApplyAsync(list -> {
			return ok(myQuestion.render(list));
		}, ec);
    }

}
