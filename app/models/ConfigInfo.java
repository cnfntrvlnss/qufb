package models;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.*;


@Entity
@NamedQueries(
        @NamedQuery(name = "ConfigInfo.findConfig",
        query = "select c from ConfigInfo c where c.configId.configId = :configId")
)
public class ConfigInfo {

    public ConfigInfoPK configId;//复合主键
    public String configName;//配置id
    public  String subName;//配置名称
    @EmbeddedId
    public ConfigInfoPK getConfigId() {
        return configId;
    }

    public void setConfigId(ConfigInfoPK configId) {
        this.configId = configId;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }
}
