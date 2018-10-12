package dao.impl;

import com.google.inject.Singleton;
import dao.ConfigInfoRepository;
import dao.QuestionFeedbackRepository;
import models.ConfigInfo;
import models.ConfigInfoPK;
import models.QuestionFeedback;
import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.JPAApi;
import play.libs.Json;

import javax.persistence.EntityManager;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Singleton
public class JPAConfigInfoRepository implements ConfigInfoRepository {
    private static Logger.ALogger logger = Logger.of(JPAConfigInfoRepository.class);

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAConfigInfoRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext){
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<List<ConfigInfo>> findAll(ConfigInfo configInfo) {
        return supplyAsync(()-> {
            return  jpaApi.withTransaction(questionList->{
                return questionList.createQuery("select p from configInfo p where p.configId="+configInfo.getConfigId(), ConfigInfo.class)
                        .getResultList();
            });
        });
    }

    @Override
    public CompletionStage<List<ConfigInfo>> fetchAll(){
        return supplyAsync(() -> jpaApi.withTransaction(em -> {
            return em.createQuery("select c from ConfigInfo c", ConfigInfo.class)
            .getResultList();
        }));
    }

    @Override
    public CompletionStage<Void> addOrUpdate(ConfigInfo configInfo){
        return supplyAsync(()-> jpaApi.withTransaction((EntityManager em)-> {
            ConfigInfoPK pk = configInfo.getConfigId();
            Integer subId = pk.getSubId();
            pk.setSubId(null);
            List<ConfigInfo> configs = em.createNamedQuery("ConfigInfo.findConfig", ConfigInfo.class)
                    .setParameter("configId", pk.getConfigId())
                    .getResultList();
            pk.setSubId(subId);
            logger.debug("zsrdebug: {}", Json.prettyPrint(Json.toJson(configs)));
            if(configs.size() > 0){
                 if(!configs.get(0).getConfigName().equals(configInfo.getConfigName())){
                     //数据不一致，不能更新或添加
                     throw new RuntimeException("cannot use incoming data to update db, " +
                             "as configName is different with that of same configId in DB. configId:" + pk.getConfigId());
                 }
                 for(ConfigInfo config: configs){
                     if(config.getConfigId().getSubId() == pk.getSubId()){
                         config.setSubName(configInfo.getSubName());
                         return null;
                     }
                 }
            }
            em.persist(configInfo);
            return null;
        }));
    }

    @Override
    public CompletionStage<Void> deleteConfigs(List<ConfigInfoPK> pks){
        return supplyAsync(()-> jpaApi.withTransaction((EntityManager em) -> {
            for(ConfigInfoPK pk: pks){
                ConfigInfo config = em.find(ConfigInfo.class, pk);
                if(config != null) {
                    em.remove(config);
                }
            }
            return null;
        }));
    }
}
