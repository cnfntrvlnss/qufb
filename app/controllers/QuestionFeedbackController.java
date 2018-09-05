package controllers;

import akka.util.ByteString;
import com.fasterxml.jackson.databind.JsonNode;
import dao.QuestionFeedbackRepository;
import models.QuestionFeedback;
import play.data.FormFactory;
import play.libs.F;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.streams.Accumulator;
import play.mvc.*;
import views.html.myQuestion;

import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import static play.libs.Json.toJson;

//import akka.util;

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
        QuestionFeedback quest = Json.fromJson(request().body().asJson(), QuestionFeedback.class);
        quest.setFeedbackTime(new Date());
		return questRepo.save(quest).thenApplyAsync(p -> {
			return ok();
		}, ec.current());
	}

	public static  class QuestionBodyParser<Q> implements  BodyParser<Q> {
		public BodyParser.Json jsonParser;
		public Executor executor;
		Class<Q> clz;
		@Inject
		public QuestionBodyParser(BodyParser.Json jsonParser, Executor executor) {
			this.jsonParser = jsonParser;
			this.executor = executor;
            clz = (Class<Q>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }

		@Override
		public Accumulator<ByteString, F.Either<Result, Q>> apply(Http.RequestHeader request) {
			Accumulator<ByteString, F.Either<Result, JsonNode>> jsonAccumulator = jsonParser.apply(request);
			return jsonAccumulator.map(resultOrJson -> {
				if (resultOrJson.left.isPresent()) {
					return F.Either.Left(resultOrJson.left.get());
				} else {
					JsonNode json = resultOrJson.right.get();
					try {
						Q qust = play.libs.Json.fromJson(json, clz);
						return F.Either.Right(qust);
					} catch (Exception e) {
						return F.Either.Left(Results.badRequest(
								"Unable to read User from json: " + e.getMessage()));
					}
				}
			}, executor);
		}

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
