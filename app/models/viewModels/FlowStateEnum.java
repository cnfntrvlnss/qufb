package models.viewModels;

/**
 * 流程状态枚举类
 * lixin
 * 2018-9-6 10:55:01
 */
public enum FlowStateEnum
{
	/** 
	 保存
	*/
	DRAFT(1),

	/** 
	 提交
	*/
	SUBMIT(2),

	/**
	 * 分析
	 
	*/
	ANALYSE(3),
	/**
	 审核
	 */
	REVIEW(4),

	/**
	 验证
	 */
	VERIFY(5),

	/**
	 关闭

	 */
	CLOSED(6),
	/**
	 WORK_AS_DESIGN

	 */
	WORK_AS_DESIGN(7),
	/**
	 无法解决

	 */
	无法解决(8),
	/**
	 挂起

	 */
	挂起(9);
	private int intValue;
	private static java.util.HashMap<Integer, FlowStateEnum> mappings;

	private synchronized static java.util.HashMap<Integer, FlowStateEnum> getMappings() {
		if (mappings == null) {
			mappings = new java.util.HashMap<Integer, FlowStateEnum>();
		}
		return mappings;
	}

	private FlowStateEnum(int value) {
		intValue = value;
		FlowStateEnum.getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static FlowStateEnum forValue(int value) {
		return getMappings().get(value);
	}
}