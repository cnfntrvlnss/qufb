package controllers;

import play.Logger;

import dao.QuestionFeedbackRepository;
import models.QuestionFeedback;
import models.viewModels.FlowStateEnum;
import models.viewModels.QuestionStateEnum;
import models.viewModels.SubmitTypeEnum;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.myQuestion;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static play.libs.Json.toJson;


public class QuestionFeedbackController extends Controller {
	private final Logger.ALogger logger = Logger.of(QuestionFeedbackController.class);

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
		String userName=session().get("userName");
        QuestionFeedback questionFeedback = Json.fromJson(request().body().asJson(), QuestionFeedback.class);
		questionFeedback.setFeedbackTime(new Date());//反馈时间
		questionFeedback.setFeedbacker(userName);
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
		String userName=session().get("username");//当前登录用户名
		//操作标志码1：第一个环节2、第二个环节 3、第三个环节 4、第四个环节 5、第五个环节 6、第六个环节 7、第七个环节 8、第八个环节

		//判断当前问题状态，获取需要处理问题的人，与当前登录用户进行对比，若是同一个人，允许操作。否则只展示信息，隐藏按钮的操作。
		CompletionStage<QuestionFeedback> questionFeedback=questRepo.findById(questionId);

		return questionFeedback.thenApplyAsync(questionInfo -> {
			logger.debug("{},{},{},{}", questionInfo.getQuestionState(),QuestionStateEnum.DEVELOPER.getValue(), questionInfo.getDeveloperName(),userName);
			if(questionInfo.getQuestionState() == QuestionStateEnum.FEEDBACKER.getValue() && questionInfo.getFeedbacker().equals(userName)){//第一个节点的人，需要对比问题提交人
				questionInfo.setOperateFlag(1);
			}else if(questionInfo.getQuestionState() == QuestionStateEnum.BUG_HEADER.getValue() && questionInfo.getBugHeader().equals(userName)){//第二个节点的人，需要对比bug负责人
				questionInfo.setOperateFlag(2);
			}else if(questionInfo.getQuestionState() == QuestionStateEnum.TRANSFER.getValue() && questionInfo.getTransferName().equals(userName)){//第二个节点的人，需要对比bug负责人
				questionInfo.setOperateFlag(3);
			}else if(questionInfo.getQuestionState() == QuestionStateEnum.DEVELOPER.getValue() && questionInfo.getDeveloperName().equals(userName)){//第二个节点的人，需要对比bug负责人
				questionInfo.setOperateFlag(4);
			}
			else if(questionInfo.getQuestionState() == QuestionStateEnum.SCHEME_AUDITOR.getValue() && questionInfo.getSchemeAuditName().equals(userName)){//第二个节点的人，需要对比bug负责人
				questionInfo.setOperateFlag(5);
			}else if(questionInfo.getQuestionState() == QuestionStateEnum.AUDITOR.getValue() && questionInfo.getResultAuditName().equals(userName)){//第二个节点的人，需要对比bug负责人
				questionInfo.setOperateFlag(6);
			}else if(questionInfo.getQuestionState() == QuestionStateEnum.VERIFY.getValue() && questionInfo.getVerifyName().equals(userName)){//第二个节点的人，需要对比bug负责人
				questionInfo.setOperateFlag(7);
			}else{
				questionInfo.setOperateFlag(8);
			}
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

	/**
	 * 获取权限过滤后的问题列表
	 * lixin
	 * 2018-9-7 08:31:25
	 * @return
	 */
	public CompletionStage<Result> listMyQuestion() {
		String userName = session().get("username");
		return questRepo.findAll(userName).thenApplyAsync(questionList -> {
			return ok(toJson(questionList));
		}, ec.current());
	}
}
