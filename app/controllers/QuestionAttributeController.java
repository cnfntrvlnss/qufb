package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import dao.ConfigInfoRepository;
import models.ConfigInfo;
import models.ConfigInfoPK;
import play.Logger;

import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;


public class QuestionAttributeController extends Controller {
    private final Logger.ALogger logger = Logger.of(QuestionAttributeController.class);

    @Inject
    ConfigInfoRepository configRepo;
    @Inject
    private HttpExecutionContext ec;

    public CompletionStage<Result> fetchAll(){

        return configRepo.fetchAll().thenApplyAsync(list -> {
            return ok(Json.toJson(list));
        }, ec.current());
    }

    @BodyParser.Of(BodyParser.MultipartFormData.class)
    public CompletionStage<Result> addOrUpdate(){
        Map<String, String[]> m = request().body().asMultipartFormData().asFormUrlEncoded();
        ConfigInfo config = new ConfigInfo();
        config.setConfigId(new ConfigInfoPK());
        config.getConfigId().setConfigId(Integer.valueOf(m.get("itemId")[0]));
        config.getConfigId().setSubId(Integer.valueOf(m.get("subItemId")[0]));
        config.setConfigName(m.get("itemName")[0]);
        config.setSubName(m.get("subItemName")[0]);
        return configRepo.addOrUpdate(config).thenApplyAsync(v -> {
            return ok();
        }, ec.current());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> deleteSome(){
        JsonNode data = request().body().asJson();
        List<ConfigInfoPK> pks = new ArrayList<>();
        for(int idx =0; idx<data.size(); idx++){
            pks.add(Json.fromJson(data.get(idx), ConfigInfoPK.class));
        }
        return configRepo.deleteConfigs(pks).thenApplyAsync(v->ok());
    }

}