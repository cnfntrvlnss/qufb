package controllers;

import akka.util.ByteString;
import com.fasterxml.jackson.databind.JsonNode;
import dao.QuestionFeedbackRepository;
import models.QuestionFeedback;
import  models.viewModels.SubmitTypeEnum;
import  models.viewModels.QuestionStateEnum;
import  models.viewModels.FlowStateEnum;
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
        QuestionFeedback questionFeedback = Json.fromJson(request().body().asJson(), QuestionFeedback.class);
		questionFeedback.setFeedbackTime(new Date());
		//用户的提交类型，用来判断问题状态和流程状态的变换
		int submitType=questionFeedback.getSubmitType();
		if(submitType == SubmitTypeEnum.QUESTION_SAVE.getValue()){//第一次保存
			questionFeedback.setQuestionState(QuestionStateEnum.FEEDBACKER.getValue());//设置问题状态为1
			questionFeedback.setFlowState(FlowStateEnum.DRAFT.getValue());//设置流程状态为1
		}else if(submitType == SubmitTypeEnum.QUESTION_SUBMIT.getValue()){//第一次提交
			questionFeedback.setQuestionState(QuestionStateEnum.BUG_HEADER.getValue());//设置问题状态为2
			questionFeedback.setFlowState(FlowStateEnum.SUBMIT.getValue());//设置流程状态为2
		}
		return questRepo.save(questionFeedback).thenApplyAsync(p -> { return  ok();	}, ec.current());
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

	/**
	 * 更新一个问题信息
	 * lixin
	 * 2018-9-5 11:32:13
	 * @return
	 */
	public CompletionStage<Result> updateQuestionInfo() {
		QuestionFeedback questionFeedback = Json.fromJson(request().body().asJson(), QuestionFeedback.class);
		//用户的提交类型，用来判断问题状态和流程状态的变换
		int submitType=questionFeedback.getSubmitType();
		if(submitType==SubmitTypeEnum.QUESTION_SUBMIT.getValue()){//问题提交者提交问题后，问题状态和流程状态分别变为2 2
			questionFeedback.setQuestionState(QuestionStateEnum.BUG_HEADER.getValue());
			questionFeedback.setFlowState(FlowStateEnum.SUBMIT.getValue());
		}else if(submitType==SubmitTypeEnum.BUG_HEADER_REJECT.getValue()){//bug负责人驳回问题状态和流程状态分别变为 11
			questionFeedback.setQuestionState(QuestionStateEnum.FEEDBACKER.getValue());
			questionFeedback.setFlowState(FlowStateEnum.DRAFT.getValue());
		}else if(submitType==SubmitTypeEnum.BUG_HEADER_SUBMIT.getValue()){//bug负责人提交 问题状态和流程状态分别变为3 2
			questionFeedback.setQuestionState(QuestionStateEnum.TRANSFER.getValue());
			questionFeedback.setFlowState(FlowStateEnum.SUBMIT.getValue());
		}else if(submitType==SubmitTypeEnum.TRANSFER_REJECT.getValue()){//接口人驳回 问题状态和流程状态分别变为2 2
			questionFeedback.setQuestionState(QuestionStateEnum.BUG_HEADER.getValue());
			questionFeedback.setFlowState(FlowStateEnum.SUBMIT.getValue());
		}else if(submitType==SubmitTypeEnum.TRANSFER_SUBMIT.getValue()){//接口人提交 问题状态和流程状态分别变为4 3
			questionFeedback.setQuestionState(QuestionStateEnum.DEVELOPER.getValue());
			questionFeedback.setFlowState(FlowStateEnum.ANALYSE.getValue());
		}else if(submitType==SubmitTypeEnum.DEVELOPER_REJECT.getValue()){//方案责任人驳回 问题状态和流程状态分别变为3 2
			questionFeedback.setQuestionState(QuestionStateEnum.TRANSFER.getValue());
			questionFeedback.setFlowState(FlowStateEnum.SUBMIT.getValue());
		}else if(submitType==SubmitTypeEnum.DEVELOPER_SUBMIT.getValue()){//方案责任人提交 问题状态和流程状态分别变为5 4
			questionFeedback.setQuestionState(QuestionStateEnum.SCHEME_AUDITOR.getValue());
			questionFeedback.setFlowState(FlowStateEnum.REVIEW.getValue());
		}else if(submitType==SubmitTypeEnum.SCHEMER_AUDIT_REJECT.getValue()){//方案审核人驳回 问题状态和流程状态分别变为4 3
			questionFeedback.setQuestionState(QuestionStateEnum.DEVELOPER.getValue());
			questionFeedback.setFlowState(FlowStateEnum.ANALYSE.getValue());
		}else if(submitType==SubmitTypeEnum.SCHEMER_AUDIT_SUBMIT.getValue()){//方案审核人提交 问题状态和流程状态分别变为6 5
			questionFeedback.setQuestionState(QuestionStateEnum.AUDITOR.getValue());
			questionFeedback.setFlowState(FlowStateEnum.VERIFY.getValue());
		}else if(submitType==SubmitTypeEnum.RESULT_AUDIT_REJECT.getValue()){//结果审核人驳回 问题状态和流程状态分别变为4 3
			questionFeedback.setQuestionState(QuestionStateEnum.DEVELOPER.getValue());
			questionFeedback.setFlowState(FlowStateEnum.ANALYSE.getValue());
		}else if(submitType==SubmitTypeEnum.RESULT_AUDIT_SUBMIT.getValue()){//结果审核人提交 问题状态和流程状态分别变为7 5
			questionFeedback.setQuestionState(QuestionStateEnum.VERIFY.getValue());
			questionFeedback.setFlowState(FlowStateEnum.VERIFY.getValue());
		}else if(submitType==SubmitTypeEnum.VARIFY_REJECT.getValue()){//验证人员驳回 问题状态和流程状态分别变为4 3
			questionFeedback.setQuestionState(QuestionStateEnum.DEVELOPER.getValue());
			questionFeedback.setFlowState(FlowStateEnum.ANALYSE.getValue());
		}else if(submitType==SubmitTypeEnum.VERIFY_CLOSE.getValue()){//验证人员关闭问题 问题状态和流程状态分别变为8 6
			questionFeedback.setQuestionState(QuestionStateEnum.DONE.getValue());
			questionFeedback.setFlowState(FlowStateEnum.CLOSED.getValue());
		}
		return questRepo.updateNotNull(questionFeedback).thenApplyAsync((v) -> ok());
	}
}
