package models;

import javax.persistence.Embeddable;
import java.io.Serializable;
@Embeddable
public class ConfigInfoPK implements Serializable {
    private Integer configId;
    private Integer subId;

    public Integer getConfigId() {
        return configId;
    }

    public void setConfigId(Integer configId) {
        this.configId = configId;
    }

    public Integer getSubId() {
        return subId;
    }

    public void setSubId(Integer subId) {
        this.subId = subId;
    }
}
