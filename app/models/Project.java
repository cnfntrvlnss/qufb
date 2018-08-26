package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 项目
 * 2018-8-15 16:29:27
 * @author li.xin01
 *
 */
@Entity
public class Project {
	private Integer projectId;//项目id
	private String projectName;//项目名称
	private String projectCode;//项目编号
	private Integer departmentId;//部门id
	private String projectManager;//项目经理
	private Integer projectType;//项目类型
	private Integer projectLevel;//项目级别
	private String productManager;//产品经理
	private Date startTimePlan;//计划开始时间
	private Date endTimePlan;//计划结束时间
	private String projectDescription;//项目描述
	private Integer projectState;//项目状态
	private String currentDescription;//当前项目进展描述
	private String delayDescription;//项目延期描述
	private String customerName;//客户名称
	private String developmentSite;//开发地点
	private String createPerson;//创建人
	private Date createTime;//创建时间
	private String modifier;//修改者
	private Date modifyTime;//修改时间
	private String deviationRate;//偏差率

	@Id
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getProjectManager() {
		return projectManager;
	}
	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}

	public Integer getProjectType() {
		return projectType;
	}
	public void setProjectType(Integer projectType) {
		this.projectType = projectType;
	}

	public Integer getProjectLevel() {
		return projectLevel;
	}
	public void setProjectLevel(Integer projectLevel) {
		this.projectLevel = projectLevel;
	}

	public String getProductManager() {
		return productManager;
	}
	public void setProductManager(String productManager) {
		this.productManager = productManager;
	}

	public Date getStartTimePlan() {
		return startTimePlan;
	}
	public void setStartTimePlan(Date startTimePlan) {
		this.startTimePlan = startTimePlan;
	}

	public Date getEndTimePlan() {
		return endTimePlan;
	}
	public void setEndTimePlan(Date endTimePlan) {
		this.endTimePlan = endTimePlan;
	}

	public String getProjectDescription() {
		return projectDescription;
	}
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public Integer getProjectState() {
		return projectState;
	}
	public void setProjectState(Integer projectState) {
		this.projectState = projectState;
	}

	public String getCurrentDescription() {
		return currentDescription;
	}
	public void setCurrentDescription(String currentDescription) {
		this.currentDescription = currentDescription;
	}

	public String getDelayDescription() {
		return delayDescription;
	}
	public void setDelayDescription(String delayDescription) {
		this.delayDescription = delayDescription;
	}

	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDevelopmentSite() {
		return developmentSite;
	}
	public void setDevelopmentSite(String developmentSite) {
		this.developmentSite = developmentSite;
	}

	public String getCreatePerson() {
		return createPerson;
	}
	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getDeviationRate() {
		return deviationRate;
	}
	public void setDeviationRate(String deviationRate) {
		this.deviationRate = deviationRate;
	}
	
}
