package controllers;

import play.Logger;
import  dao.DepartmentRepository;
import java.util.HashMap;
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


public class DepartmentController extends Controller {
	private final Logger.ALogger logger = Logger.of(DepartmentController.class);


	@Inject
	DepartmentRepository departmentRepository;
	@Inject
	private HttpExecutionContext ec;
	@Inject
	private  FormFactory formFactory;

	public Result myDepartment() {
		return ok(views.html.myDepartment.render());
	}




	/**
	 * 获取所有的一级部门
	 * lixin
	 * 2018-9-11 10:51:55
	 * @return
	 */
	public  CompletionStage<Result> getFirstDepartment(){
		return departmentRepository.getFirstDepartment().thenApplyAsync(departmentList -> {
			return ok(toJson(departmentList));
		}, ec.current());
		//return departmentRepository.getFirstDepartment();
	}

	/**
	 * 获取二级部门
	 * lixin
	 * 2018-9-11 10:53:28
	 * @param parentDepartmentId
	 * @return
	 */
	public  CompletionStage<Result> getSecondDepartment(Integer parentDepartmentId){
		return departmentRepository.getSecondDepartment(parentDepartmentId).thenApplyAsync(departmentList -> {
			return ok(toJson(departmentList));
		}, ec.current());
	}

	/**
	 * 通过一个部门id，获取该部门下的所有的用户
	 * lixin
	 * 2018-9-11 15:26:03
	 * @param departmentId
	 * @return
	 */
	public  CompletionStage<Result> getUserList(Integer departmentId){
		return departmentRepository.getUserList(departmentId).thenApplyAsync(userList -> {
			return ok(toJson(userList));
		}, ec.current());
	}
}
