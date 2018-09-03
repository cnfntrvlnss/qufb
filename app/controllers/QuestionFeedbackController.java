package controllers;

import javax.inject.Inject;
import models.QuestionFeedback;
import dao.QuestionFeedbackRepository;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.myQuestion;
import com.google.gson.GsonBuilder;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletionStage;
import static play.libs.Json.toJson;


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
		questionFeedback.setFeedbackTime(new Date());
		return questRepo.save(questionFeedback).thenApplyAsync(p -> {
			return redirect(routes.QuestionFeedbackController.myQuestionSubmit());
		}, ec.current());
	}

	/**
	 * 获取问题列表
	 * @return
	 */
	public CompletionStage<Result> listQuestion() {
		return questRepo.findAll().thenApplyAsync(questionList -> {
			return ok(toJson(questionList));
		}, ec.current());
	}
	/**
	 * 问题处理页面
	 * lixin
	 * 2018-9-3 13:53:32
	 * @return
	 */
	public Result myQuestionDeal(Integer questionId) {
		return ok(views.html.myQuestionDeal.render(questionId));
	}
	/**
	 * 通过问题id获取一个问题信息
	 * lixin
	 * 2018-9-3 15:39:15
	 * @param questionId：问题id，数据库主键
	 * @return
	 */
	public CompletionStage<Result> getQuestionInfo(Integer questionId) {
		CompletionStage<QuestionFeedback> questionFeedback=questRepo.findById(questionId);
		return questionFeedback.thenApplyAsync(questionInfo -> {
			return ok(toJson(questionInfo));
		}, ec.current());
	}
}
