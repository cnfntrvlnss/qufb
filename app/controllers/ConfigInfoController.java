package controllers;

import dao.ConfigInfoRepository;
import models.ConfigInfo;
import models.ConfigInfoPK;
import models.viewModels.ConfigSetting;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.myConfig;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;


public class ConfigInfoController extends Controller {

	@Inject
	ConfigInfoRepository configRepo;
	@Inject
	HttpExecutionContext ec;


	/**
	 * 获取具体的configList
	 * @return
	 */
    public  CompletionStage<Result> myConfig() {
    	ConfigInfo configInfo=new ConfigInfo();
    	ConfigInfoPK configPK=new ConfigInfoPK();
		configPK.setConfigId(ConfigSetting.QUESTION_STATE.getValue());
		configInfo.setConfigId(configPK);
    	CompletionStage<List<ConfigInfo>> configList= configRepo.findAll(configInfo);

		//lambda表达式实现方法
		return configList.thenApplyAsync(list -> {
			return ok(myConfig.render(list));
		}, ec.current());
    }

}
