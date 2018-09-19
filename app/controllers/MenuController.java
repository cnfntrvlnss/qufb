package controllers;

import dao.MenuRepository;
import dao.QuestionFeedbackRepository;
import dao.UserRepository;
import models.Menu;
import models.QuestionFeedback;
import models.viewModels.FlowStateEnum;
import models.viewModels.QuestionStateEnum;
import models.viewModels.SubmitTypeEnum;
import play.Logger;
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


public class MenuController extends Controller {
	private final Logger.ALogger logger = Logger.of(MenuController.class);

	@Inject
	MenuRepository menuRepository;

	@Inject
	private HttpExecutionContext ec;
	@Inject
	private  FormFactory formFactory;

	/**
	 * 获取问题列表
	 * @return
	 */

	@BodyParser.Of(BodyParser.Json.class)
	public CompletionStage<Result> listMenu() {
		Menu menu = Json.fromJson(request().body().asJson(), Menu.class);
		return menuRepository.listMenu(menu).thenApplyAsync(menuList -> ok(toJson(menuList)), ec.current());
	}


}
