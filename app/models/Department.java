package models;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
部门
 lixin
 2018-8-23 17:02:43
 *
 */
@Entity
public class Department {
	private Integer departmentId;//部门id
	private String departmentName;//部门名称
	private Integer departmentType;//部门类型
	private Integer parentDepartmentId;//父级部门id
	private Integer orderCode;//排序号
	private String memo;//备注

	@Id
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Integer getDepartmentType() {
		return departmentType;
	}
	public void setDepartmentType(Integer departmentType) {
		this.departmentType = departmentType;
	}

	public Integer getParentDepartmentId() {
		return parentDepartmentId;
	}
	public void setParentDepartmentId(Integer parentDepartmentId) {
		this.parentDepartmentId = parentDepartmentId;
	}

	public Integer getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}

	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

}