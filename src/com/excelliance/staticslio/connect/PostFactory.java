package com.excelliance.staticslio.connect;


import android.content.Context;

/**
 * @author liuyuli
 *
 * 2016年7月6日下午2:24:39 
 */
public class PostFactory {

	public static BaseConnectHandle produceHandle(Context context) {
		BaseConnectHandle handle = null;
		handle = new BasicConnHandle(context);
		return handle;
	}
}
