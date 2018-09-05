package controllers;

import javax.inject.Inject;
import models.QuestionFeedback;
import dao.QuestionFeedbackRepository;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.myQuestion;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletionStage;
import play.mvc.BodyParser;
import java.util.concurrent.Executor;
import play.mvc.Http.RequestBody;
import  play.api.mvc.RequestHeader;
import play.libs.streams.Accumulator;
import static play.libs.Json.toJson;

import play.libs.streams.Accumulator;
import play.libs.F;
//import akka.util;
import com.fasterxml.jackson.databind.JsonNode;

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
	@BodyParser.Of(BodyParser.Json.class)
	public CompletionStage<Result> addQuestion() {
		RequestBody body = request().body();

		QuestionFeedback questionFeedback =new QuestionFeedback();
		questionFeedback.setQuestionTitle(body.asJson().get("questionTitle").toString());
		questionFeedback.setFeedbackTime(new Date());
		return questRepo.save(questionFeedback).thenApplyAsync(p -> {
			return redirect(routes.QuestionFeedbackController.myQuestionSubmit());
		}, ec.current());
	}
	/*public static  class QuestionBodyParser implements  BodyParser<QuestionFeedback> {
		public BodyParser.Json jsonParser;
		public Executor executor;
		@Inject
		public QuestionBodyParser(BodyParser.Json jsonParser, Executor executor) {
			this.jsonParser = jsonParser;
			this.executor = executor;
		}
	}*/

	/*public Accumulator<ByteString, F.Either<Result, QuestionFeedback>> apply(RequestHeader request) {
		Accumulator<ByteString, F.Either<Result, JsonNode>> jsonAccumulator = jsonParser.apply(request);
		return jsonAccumulator.map(resultOrJson -> {
			if (resultOrJson.left.isPresent()) {
				return F.Either.Left(resultOrJson.left.get());
			} else {
				JsonNode json = resultOrJson.right.get();
				try {
					User user = play.libs.Json.fromJson(json, User.class);
					return F.Either.Right(user);
				} catch (Exception e) {
					return F.Either.Left(Results.badRequest(
							"Unable to read User from json: " + e.getMessage()));
				}
			}
		}, executor);
	}*/

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

	public CompletionStage<Void> updateQuestionInfo() {
		QuestionFeedback questionFeedback =new QuestionFeedback();
		questRepo.updateNotNull(questionFeedback).thenApplyAsync(() -> {
			return ok();
		}, ec.current());
	}
}
