package com.excelliance.test;

import com.excelliance.staticslio.StatisticsManager;

import android.app.Application;

public class StatisticApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		// 1.主进程名（注意：并非主包名，如果手动改过进程名，此时的进程名就不是主包名！如果传错了，就会上传不成功！）
		// 2.渠道号，如果没有可传""，不要传null
		// 3.IMEI,如果没有可传""，不要传null
		// 4.ContentProvider对应的Authority，不要传null
		StatisticsManager.initBasicInfo("com.excelliance.multiaccount","com.excelliance.multiaccount.staticsdkprovider",200+"");
		
		//设置是否显示日至
		StatisticsManager.getInstance(this).enableLog(true);
		
		//设置debug模式
//		StatisticsManager.getInstance(this).setDebugMode();
	}
}
