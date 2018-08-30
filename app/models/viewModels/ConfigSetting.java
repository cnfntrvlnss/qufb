package models.viewModels;

/**
 * 枚举类
 * lixin
 * 2018-8-28 16:42:03
 */
public enum ConfigSetting
{
	/** 
	 问题状态
	*/
	QUESTION_STATE(1),

	/** 
	 流程状态
	*/
	FLOW_STATE(2),

	/** 
	 有效标识
	 
	*/
	ValigFlag(3);
	private int intValue;
	private static java.util.HashMap<Integer, ConfigSetting> mappings;

	private synchronized static java.util.HashMap<Integer, ConfigSetting> getMappings() {
		if (mappings == null) {
			mappings = new java.util.HashMap<Integer, ConfigSetting>();
		}
		return mappings;
	}

	private ConfigSetting(int value) {
		intValue = value;
		ConfigSetting.getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static ConfigSetting forValue(int value) {
		return getMappings().get(value);
	}
}