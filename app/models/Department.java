package models;

import javax.persistence.*;

/**
部门
 lixin
 2018-8-23 17:02:43
 *
 */
@Entity
@NamedQueries({
		@NamedQuery(name = "Department.findByNameAndParent",
				query = "select d from Department d where d.name = :name and d.parent = :parent"),
		@NamedQuery(name="Department.findByNameAndParentId",
				query = "select d from Department d left join d.parent p where d.name = :name and p.id = :parentId")
})
public class Department {
	private Integer id;//部门id
	private String name;//部门名称
	private String type;//部门类型
	//private Integer parentDepartmentId;//父级部门id
	private Department parent;
	private Integer orderCode;//排序号
	private String memo;//备注

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer departmentId) {
		this.id = departmentId;
	}

	public String getName() {
		return name;
	}
	public void setName(String departmentName) {
		this.name = departmentName;
	}

	public String getType() {
		return type;
	}
	public void setType(String departmentType) {
		this.type = departmentType;
	}

	@ManyToOne(cascade=CascadeType.MERGE)
	public Department getParent() {
		return parent;
	}

	public void setParent(Department parent) {
		this.parent = parent;
	}

	//	public Integer getParentDepartmentId() {
//		return parentDepartmentId;
//	}
//	public void setParentDepartmentId(Integer parentDepartmentId) {
//		this.parentDepartmentId = parentDepartmentId;
//	}

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