package com.excelliance.staticslio.beans;

/**
 * Option选项的bean，为upload提供选项
 *
 * @author liuyuli
 *
 * 2016年7月13日下午6:29:25 
 */

public class OptionBean {  
	/**
	 *为是否立即上传的标识符(在开关限制下立即上传)，传入true or false;
	 */
	public static final int OPTION_INDEX_IMMEDIATELY_CARE_SWITCH = 0;
	/**
	 * 为位置信息；类型为字符串，如"105.23,155.88"
	 */
	public static final int OPTION_INDEX_POSITION = 1;
	/**
	 * 为ABTest值；类型为字符串,如"A"
	 */
	public static final int OPTION_INDEX_ABTEST = 2;
	/**
	 * 为是否立即上传的标识符(不管开关，直接、立即上传)，传入true or false;
	 */
	public static final int OPTION_INDEX_IMMEDIATELY_ANYWAY = 3;
	private int mOptionID = -1;
	private Object mOptionContent = null;

	public OptionBean(int optionID, Object optionContent) {
		this.mOptionID = optionID;
		this.mOptionContent = optionContent;
		checkOptionContentType();
	}

	public int getOptionID() {
		return mOptionID;
	}

	public Object getOptionContent() {
		return mOptionContent;
	}

	private void checkOptionContentType() {
		if ((mOptionID == OPTION_INDEX_IMMEDIATELY_ANYWAY || mOptionID == OPTION_INDEX_IMMEDIATELY_CARE_SWITCH)
				&& !(mOptionContent instanceof Boolean)) {
			throw new IllegalArgumentException("Immediately argument must be 'true' or 'false'");
		} else if ((mOptionID == OPTION_INDEX_ABTEST || mOptionID == OPTION_INDEX_POSITION)
				&& !(mOptionContent instanceof String)) {
			throw new IllegalArgumentException("Position or ABTest argument type must be String");
		}
	}
}
