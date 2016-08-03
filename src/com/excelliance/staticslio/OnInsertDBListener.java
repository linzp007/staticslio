package com.excelliance.staticslio;
/**
 * 
 * 存db监听
 *
 * @author liuyuli
 *
 * 2016年7月13日下午6:33:06 
 */
public interface OnInsertDBListener {
	void onBeforeInsertToDB();
	void onInsertToDBFinish();
}
