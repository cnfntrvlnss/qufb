package controllers;

import java.util.List;

import models.Project;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
	/**
	 * 首页面
	 * @return
	 */
    public static Result index() {
        return ok(index.render("我的第一个index页面内容"));
    }
    /**
     * 表单提交页面
     * @return
     */
    public static Result form() {
    	return ok(views.html.form.render());
    }
    /**
     * 提交表单的动作
     * @return
     */
    public static Result postForm() {
    	DynamicForm in   = Form.form().bindFromRequest();
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
    public static Result myProject() {
    	List<Project> projectList=Project.findAll();
    	//return ok(views.html.myQuestion.render());
    	//return ok("获取的项目长度为"+projectList.size());
		//System.err.print(projectList.size());
    	return ok(myProject.render(projectList));
    }

	/**
	 * 登录
	 * @return
	 */
	public static Result login() {
		return ok(views.html.login.render());
	}
	public static Result loginSubmit() {
		return ok(views.html.login.render());
	}
}
