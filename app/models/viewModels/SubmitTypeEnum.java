package models.viewModels;

/**
 * 提交类型枚举类
 * lixin
 * 2018-9-6 10:37:04
 */
public enum SubmitTypeEnum
{
	/**
	 问题保存
	 */
	QUESTION_SAVE(0),
	/**
	 问题提交
	*/
	QUESTION_SUBMIT(1),

	/**
	 BUG负责人驳回
	*/
	BUG_HEADER_REJECT(2),

	/** 
	 BUG负责人提交
	*/
	BUG_HEADER_SUBMIT(3),

	/**
	 问题接口人驳回
	 */
	TRANSFER_REJECT(4),

	/**
	 问题接口人提交
	 */
	TRANSFER_SUBMIT(5),

	/**
	 方案责任人驳回
	 */
	DEVELOPER_REJECT(6),

	/**
	 方案责任人提交
	 */
	DEVELOPER_SUBMIT(7),

	/**
	 方案审核人驳回
	 */
	SCHEMER_AUDIT_REJECT(8),

	/**
	 方案审核人提交
	 */
	SCHEMER_AUDIT_SUBMIT(9),

	/**
	 结果审核人驳回
	 */
	RESULT_AUDIT_REJECT(10),

	/**
	 结果审核人提交
	 */
	RESULT_AUDIT_SUBMIT(11),

	/**
	 * 验证人员驳回
	 */
	VARIFY_REJECT(12),

	/**
	 验证人员关闭
	 */
	VERIFY_CLOSE(13);
	private int intValue;
	private static java.util.HashMap<Integer, SubmitTypeEnum> mappings;

	private synchronized static java.util.HashMap<Integer, SubmitTypeEnum> getMappings() {
		if (mappings == null) {
			mappings = new java.util.HashMap<Integer, SubmitTypeEnum>();
		}
		return mappings;
	}

	private SubmitTypeEnum(int value) {
		intValue = value;
		SubmitTypeEnum.getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static SubmitTypeEnum forValue(int value) {
		return getMappings().get(value);
	}
}