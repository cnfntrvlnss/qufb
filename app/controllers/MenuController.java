package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import dao.MenuRepository;
import models.Menu;
import play.Logger;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
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
	private FormFactory formFactory;

	/**
	 * 获取问题列表
	 *
	 * @return
	 */

	@BodyParser.Of(BodyParser.Json.class)
	public CompletionStage<Result> listMenu() {
		Menu menu = Json.fromJson(request().body().asJson(), Menu.class);
		CompletionStage<List<Menu>> menuList = menuRepository.listMenu(menu);
		return menuList.thenApplyAsync(menus -> {
			for(Menu menuTemp : menus){
				menuTemp.setSubMenuJson(listSubMenu(menuTemp.getMenuId()));
			}
		return ok(toJson(menus));
		}, ec.current());
	}
	public CompletionStage<Result> listSubMenu(Integer parentMenuId) {
		return menuRepository.listSubMenu(parentMenuId).thenApplyAsync(subMenuList ->
				ok(toJson(subMenuList))
		);
	}
}