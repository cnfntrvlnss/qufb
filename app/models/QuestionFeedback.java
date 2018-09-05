package models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

/**
 * 问题反馈实体
 *
 * lixin
 * 2018-8-23 16:12:14
 */
@Entity
public class QuestionFeedback {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	public Integer questionId;//问题反馈系统id
	public String questionCode;//问题编号
	public String questionTitle;//问题标题

    public String feedbacker;//反馈人
	public String feedbackerId;//反馈人id
    public Date feedbackTime;//反馈时间
    public String feedbackSuggestion;//用户修改意见

	public String bugHeader;//ָBUG负责人
	public String bugHeaderId;//ָBUG负责人id
	public String auditSuggestion;//审核意见
	public Date auditTime;//审核时间

	public String transferName;//接口人姓名
	public String transferId;//接口人id
	public String transferSuggestion;//接口人意见
	public Date transferTime;//接口人处理时间

	public String  developerName;//方案责任人姓名
	public String  developerId;//方案责任人id
	public String solution;//解决方案
	public Date soluteTime;//解决时间

	public String  schemeAuditName;//方案审核人姓名
	public String  schemeAuditId;//方案审核人id
	public String schemeAuditSuggestion;//方案审核意见
	public Date schemeAuditTime;//方案审核时间

	public String  resultAuditName;//结果审核人姓名
	public String  resultAuditId;//结果审核人id
	public String resultAuditSuggestion;//结果审核意见
	public Date resultAuditTime;//结果审核时间

	public String  verifyName;//验证人姓名
	public String  verifyId;//验证人id
	public String verifySuggestion;//验证意见
	public Date verifyTime;//验证时间

    public Date estimateFinishTime;//预计完成时间
	public Date latestUpdateTime;//最近更新时间

	public Integer projectId;//问题所属项目id
	public String projectName;//问题所属项目名称
	public String questionDescription;//问题描述
	public Integer questionState;//问题状态״̬
	public Integer questionType;//问题类型
	public Integer questionLevel;//问题等级

	public Integer flowId;//流程id


	public Integer getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}
	@Column(unique=true, nullable=false)
	public String getQuestionCode() {
		return questionCode;
	}
	public void setQuestionCode(String questionCode) {
		this.questionCode = questionCode;
	}

	public String getQuestionTitle() {
		return questionTitle;
	}
	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	public String getFeedbacker() {
		return feedbacker;
	}
	public void setFeedbacker(String feedbacker) {
		this.feedbacker = feedbacker;
	}

	public String getFeedbackerId() {
		return feedbackerId;
	}
	public void setFeedbackerId(String feedbackerId) {
		this.feedbackerId = feedbackerId;
	}

	public Date getFeedbackTime() {
		return feedbackTime;
	}
	public void setFeedbackTime(Date feedbackTime) {
		this.feedbackTime = feedbackTime;
	}

	public String getFeedbackSuggestion() {
		return feedbackSuggestion;
	}

	public void setFeedbackSuggestion(String feedbackSuggestion) {
		this.feedbackSuggestion = feedbackSuggestion;
	}

	public String getBugHeader() {
		return bugHeader;
	}

	public void setBugHeader(String bugHeader) {
		this.bugHeader = bugHeader;
	}

	public String getBugHeaderId() {
		return bugHeaderId;
	}

	public void setBugHeaderId(String bugHeaderId) {
		this.bugHeaderId = bugHeaderId;
	}

	public String getAuditSuggestion() {
		return auditSuggestion;
	}

	public void setAuditSuggestion(String auditSuggestion) {
		this.auditSuggestion = auditSuggestion;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getTransferName() {
		return transferName;
	}

	public void setTransferName(String transferName) {
		this.transferName = transferName;
	}

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public String getTransferSuggestion() {
		return transferSuggestion;
	}

	public void setTransferSuggestion(String transferSuggestion) {
		this.transferSuggestion = transferSuggestion;
	}

	public Date getTransferTime() {
		return transferTime;
	}

	public void setTransferTime(Date transferTime) {
		this.transferTime = transferTime;
	}

	public String getDeveloperName() {
		return developerName;
	}

	public void setDeveloperName(String developerName) {
		this.developerName = developerName;
	}

	public String getDeveloperId() {
		return developerId;
	}

	public void setDeveloperId(String developerId) {
		this.developerId = developerId;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public Date getSoluteTime() {
		return soluteTime;
	}

	public void setSoluteTime(Date soluteTime) {
		this.soluteTime = soluteTime;
	}

	public String getSchemeAuditName() {
		return schemeAuditName;
	}

	public void setSchemeAuditName(String schemeAuditName) {
		this.schemeAuditName = schemeAuditName;
	}

	public String getSchemeAuditId() {
		return schemeAuditId;
	}

	public void setSchemeAuditId(String schemeAuditId) {
		this.schemeAuditId = schemeAuditId;
	}

	public String getSchemeAuditSuggestion() {
		return schemeAuditSuggestion;
	}

	public void setSchemeAuditSuggestion(String schemeAuditSuggestion) {
		this.schemeAuditSuggestion = schemeAuditSuggestion;
	}

	public Date getSchemeAuditTime() {
		return schemeAuditTime;
	}

	public void setSchemeAuditTime(Date schemeAuditTime) {
		this.schemeAuditTime = schemeAuditTime;
	}

	public String getResultAuditName() {
		return resultAuditName;
	}

	public void setResultAuditName(String resultAuditName) {
		this.resultAuditName = resultAuditName;
	}

	public String getResultAuditId() {
		return resultAuditId;
	}

	public void setResultAuditId(String resultAuditId) {
		this.resultAuditId = resultAuditId;
	}

	public String getResultAuditSuggestion() {
		return resultAuditSuggestion;
	}

	public void setResultAuditSuggestion(String resultAuditSuggestion) {
		this.resultAuditSuggestion = resultAuditSuggestion;
	}

	public Date getResultAuditTime() {
		return resultAuditTime;
	}

	public void setResultAuditTime(Date resultAuditTime) {
		this.resultAuditTime = resultAuditTime;
	}

	public String getVerifyName() {
		return verifyName;
	}

	public void setVerifyName(String verifyName) {
		this.verifyName = verifyName;
	}

	public String getVerifyId() {
		return verifyId;
	}

	public void setVerifyId(String verifyId) {
		this.verifyId = verifyId;
	}

	public String getVerifySuggestion() {
		return verifySuggestion;
	}

	public void setVerifySuggestion(String verifySuggestion) {
		this.verifySuggestion = verifySuggestion;
	}

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public Date getEstimateFinishTime() {
		return estimateFinishTime;
	}

	public void setEstimateFinishTime(Date estimateFinishTime) {
		this.estimateFinishTime = estimateFinishTime;
	}

	public Date getLatestUpdateTime() {
		return latestUpdateTime;
	}

	public void setLatestUpdateTime(Date latestUpdateTime) {
		this.latestUpdateTime = latestUpdateTime;
	}

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

	public String getQuestionDescription() {
		return questionDescription;
	}

	public void setQuestionDescription(String questionDescription) {
		this.questionDescription = questionDescription;
	}

	public Integer getQuestionState() {
		return questionState;
	}

	public void setQuestionState(Integer questionState) {
		this.questionState = questionState;
	}

	public Integer getQuestionType() {
		return questionType;
	}

	public void setQuestionType(Integer questionType) {
		this.questionType = questionType;
	}

	public Integer getQuestionLevel() {
		return questionLevel;
	}

	public void setQuestionLevel(Integer questionLevel) {
		this.questionLevel = questionLevel;
	}

	public Integer getFlowId() {
		return flowId;
	}

	public void setFlowId(Integer flowId) {
		this.flowId = flowId;
	}
}
