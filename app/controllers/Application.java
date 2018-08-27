package controllers;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import dao.ProjectRepository;
import models.Project;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;
import views.html.*;

import javax.inject.Inject;

public class Application extends Controller {
	@Inject
	FormFactory formFactory;
	@Inject
	ProjectRepository projDao;

	/**
	 * 首页面
	 * @return
	 */
    public Result index() {
        return ok(index.render("我的第一个index页面内容"));
    }
    /**
     * 表单提交页面
     * @return
     */
    public Result form() {
    	return ok(views.html.form.render());
    }
    /**
     * 提交表单的动作
     * @return
     */
    public Result postForm() {
    	DynamicForm in   = formFactory.form().bindFromRequest();
	    String system    = in.get("system");
	    String email=in.get("email");
	    String questionFeedback=in.get("questionDescription");
	    String suggest=in.get("suggest");
	    String result="提交成功，提交问题所属系统是"+system+"您的邮箱是"+email;
	    return ok(result);
    }
    /**
     * 我的项目
     * @return
     */
    public CompletionStage<Result> myProject() {
        return projDao.findAll().thenApplyAsync(stream -> {
            return ok(myProject.render(stream.collect(Collectors.toList())));
        });
    }

	/**
	 * 登录
	 * @return
	 */
	public Result login() {
		return ok(views.html.login.render());
	}
	public Result loginSubmit() {
		return ok(views.html.login.render());
	}
}
