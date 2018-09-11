package models.viewModels;

/**
 * 部门类型枚举类
 * lixin
 * 2018-9-11 13:53:50
 */
public enum DepartmentTypeEnum
{
	/**
	 顶级部门
	 */
	TOP_DEPARMENT(1),
	/**
	一级部门
	*/
	FIRST_DEPARTMENT(2),

	/**
	 二级部门
	*/
	SECOND_DEPARTMENT(3);
	private int intValue;
	private static java.util.HashMap<Integer, DepartmentTypeEnum> mappings;

	private synchronized static java.util.HashMap<Integer, DepartmentTypeEnum> getMappings() {
		if (mappings == null) {
			mappings = new java.util.HashMap<Integer, DepartmentTypeEnum>();
		}
		return mappings;
	}

	private DepartmentTypeEnum(int value) {
		intValue = value;
		DepartmentTypeEnum.getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static DepartmentTypeEnum forValue(int value) {
		return getMappings().get(value);
	}
}