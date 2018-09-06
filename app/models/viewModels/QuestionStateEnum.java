package models.viewModels;

/**
 * 问题状态枚举类
 * lixin
 * 2018-9-05 16:42:03
 */
public enum QuestionStateEnum
{
	/** 
	 问题提交
	*/
	FEEDBACKER(1),

	/** 
	 BUG负责人处理
	*/
	BUG_HEADER(2),

	/** 
	 接口人处理
	 
	*/
	TRANSFER(3),
	/**
	 方案责任人处理
	 */
	DEVELOPER(4),

	/**
	 方案审核人处理
	 */
	SCHEME_AUDITOR(5),

	/**
	 审核人处理

	 */
	AUDITOR(6),
	/**
	 验证人处理

	 */
	VERIFY(7),
	/**
	问题处理完毕
	 */
	DONE(8);
	private int intValue;
	private static java.util.HashMap<Integer, QuestionStateEnum> mappings;

	private synchronized static java.util.HashMap<Integer, QuestionStateEnum> getMappings() {
		if (mappings == null) {
			mappings = new java.util.HashMap<Integer, QuestionStateEnum>();
		}
		return mappings;
	}

	private QuestionStateEnum(int value) {
		intValue = value;
		QuestionStateEnum.getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static QuestionStateEnum forValue(int value) {
		return getMappings().get(value);
	}
}