package dao;

import com.google.inject.ImplementedBy;
import dao.impl.JPAConfigInfoRepository;
import models.ConfigInfo;
import java.util.List;
import java.util.concurrent.CompletionStage;

@ImplementedBy(JPAConfigInfoRepository.class)
public interface ConfigInfoRepository {
    /**
     * 获取所有的config集合
     * lixin
     * 2018-8-28 16:34:56
     * @return
     */
    CompletionStage<List<ConfigInfo>> findAll(ConfigInfo configInfo);

}
