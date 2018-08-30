package controllers;

import javax.inject.Inject;

import models.QuestionFeedback;
import dao.QuestionFeedbackRepository;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.myQuestion;

import java.util.List;
import java.util.concurrent.CompletionStage;



public class QuestionFeedbackController extends Controller {

	@Inject
	QuestionFeedbackRepository questRepo;
	@Inject
	private HttpExecutionContext ec;
	@Inject
	private  FormFactory formFactory;


	/**
     * 我的问题列表
	 * lixin
	 * 2018-8-24 10:50:25
     * @return
     */
    public  CompletionStage<Result> myQuestion() {
    	CompletionStage<List<QuestionFeedback>> questionList= questRepo.findAll();
    	//匿名内部类的实现方法
		/*questionList.thenApplyAsync(new Function<List<QuestionFeedback>, Result>() {
			@Override
			public Result apply(List<QuestionFeedback> questionFeedbackStream) {
				return ok(myQuestion.render(questionFeedbackStream));
			}
		}, ec.current());*/
		//lambda表达式实现方法
		return questionList.thenApplyAsync(list -> ok(myQuestion.render(list)), ec.current());
    }

	/**
	 * 提交问题页面
	 * @return
	 */
	public Result myQuestionSubmit() {
		return ok(views.html.myQuestionSubmit.render());
	}

	/**
	 * 保存一个问题
	 * @return
	 */
	public CompletionStage<Result> addQuestion() {
		QuestionFeedback questionFeedback = formFactory.form(QuestionFeedback.class).bindFromRequest().get();
		return questRepo.save(questionFeedback).thenApplyAsync(p -> {
			return redirect(routes.QuestionFeedbackController.myQuestionSubmit());
		}, ec.current());
	}
}
