package models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@Entity
@Table(name="CodeSerial")
public class CodeSerial implements Serializable {
	public CodeSerial() {
	}

	/**
	 * CodeType 。
	 */
	private String codeType;//主键	//流水号对应配置id
	private Integer serial;//序列号
	@Id
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public Integer getSerial() {
		return serial;
	}
	public void setSerial(Integer serial) {
		this.serial = serial;
	}

}
