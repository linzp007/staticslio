package com.excelliance.staticslio;

import com.excelliance.staticslio.database.DataBaseHelper;
import com.excelliance.staticslio.utiltool.UtilTool;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * @author liuyuli
 *
 * 2016年7月19日下午12:42:38 
 */
public class StaticDataContentProvider extends ContentProvider {
	private static final long MIN_GET_TIME = 100;
	private volatile long mLastGetTime = 0;
	private volatile String mCurrenceProgress = null;
	
	private static final String AUTHORITY = "com.excelliance.lyl.staticsprovider";

	private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	private static final String DATA_PATH = "data_info";
	private static final String CTRLINFO_PATH = "controler_info";

	private static final int CODE_DATA = 1;
	private static final int INFO_DATA = 2;

	private byte[] mMutex = new byte[0];
	private DataBaseHelper mHelper;
	public static boolean isNew = true;
	
	private static String sAuthority = AUTHORITY;
	public static Uri sNewUrl;
	public static Uri sCtrlInfoUrl;
	
	public static void init(String authority) {
	    sAuthority = authority;
		sUriMatcher.addURI(sAuthority, DATA_PATH, CODE_DATA);
		sUriMatcher.addURI(sAuthority, CTRLINFO_PATH, INFO_DATA);
		
		sNewUrl = new Uri.Builder()
		.scheme(ContentResolver.SCHEME_CONTENT)
		.authority(sAuthority).appendPath(DATA_PATH).build();
		sCtrlInfoUrl = new Uri.Builder()
		.scheme(ContentResolver.SCHEME_CONTENT)
		.authority(sAuthority).appendPath(CTRLINFO_PATH).build();
		
	}

	@Override
	public boolean onCreate() {
		mHelper = new DataBaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		String table = null;
		synchronized (mMutex) {
			if (StatisticsManager.sCurrentProcessName != null && !StatisticsManager.sCurrentProcessName
					.equals(StatisticsManager.sMainProcessName)) {
				return cursor;
			}
            Log.d(StatisticsManager.TAG, "Query time diff:" + (System.currentTimeMillis() - mLastGetTime));
			//两个进程同时获取数据，做延时操作
			//mCurrenceProgress != null && !mCurrenceProgress.equals(StatisticsManager.sCurrentProcessName) by.lyl    
            if ((mLastGetTime != 0 && System.currentTimeMillis() - mLastGetTime < MIN_GET_TIME)/* && (mCurrenceProgress != null && !mCurrenceProgress.equals(StatisticsManager.sCurrentProcessName))*/) {
                return cursor;
            }
			switch (sUriMatcher.match(uri)) {
			case CODE_DATA:
				table = DataBaseHelper.TABLE_STATISTICS_NEW;
				break;
			case INFO_DATA:
				table = DataBaseHelper.TABLE_CTRLINFO;
				break;
			}
			if (table != null) {
				try {
					cursor = mHelper.query(table, projection, selection,
							selectionArgs, sortOrder);
					if (cursor != null && cursor.getCount() > 0) {
						mLastGetTime = System.currentTimeMillis();
						mCurrenceProgress=StatisticsManager.sCurrentProcessName;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return cursor;
		}
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		String table = null;
		switch (sUriMatcher.match(uri)) {
		case CODE_DATA:
			table = DataBaseHelper.TABLE_STATISTICS_NEW;
			break;
		case INFO_DATA:
			table = DataBaseHelper.TABLE_CTRLINFO;
			break;
		}
		if (table != null) {
			try {
				long count = mHelper.insert(table, values);
				if (count > 0) {
					return uri;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		String table = null;
		switch (sUriMatcher.match(uri)) {
		case CODE_DATA:
			table = DataBaseHelper.TABLE_STATISTICS_NEW;
			break;
		case INFO_DATA:
			table = DataBaseHelper.TABLE_CTRLINFO;
			break;
		}
		if (table != null) {
			try {
				return mHelper.delete(table, selection, selectionArgs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		String table = null;
		switch (sUriMatcher.match(uri)) {
		case CODE_DATA:
			table = DataBaseHelper.TABLE_STATISTICS_NEW;
			break;
		case INFO_DATA:
			table = DataBaseHelper.TABLE_CTRLINFO;
			// break;
		}
		if (table != null) {
			try {
				return mHelper.update(table, values, selection, selectionArgs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

}
