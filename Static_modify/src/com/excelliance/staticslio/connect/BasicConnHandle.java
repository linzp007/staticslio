package com.excelliance.staticslio.connect;

//CHECKSTYLE:OFF
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.excelliance.staticslio.StatisticsManager;
import com.excelliance.staticslio.beans.PostBean;
import com.excelliance.staticslio.utiltool.CryptTool;
import com.excelliance.staticslio.utiltool.UtilTool;

import android.content.Context;
import android.util.Log;

/**
 * 数据上传
 * 
 * @author liuyuli
 *
 * 2016年7月9日下午7:31:24 
 */
public class BasicConnHandle extends BaseConnectHandle {
	public static final String STATISTICS_DATA_ENCRYPT_KEY = "lylfdl2016722";

	public BasicConnHandle(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	/* 
	 * 上传数据的具体实现
	 * 
	 */
	@Override
	public void onPost(PostBean bean) throws Throwable {
		// TODO Auto-generated method stub
		bean.mState = PostBean.STATE_POSTING;
		String statisticsData = null;
		StringBuilder buffer = null;
        if (bean != null) {
            buffer = buildData(bean);
        }
		if (buffer != null) {
			statisticsData = buffer.toString();
			if (UtilTool.isEnableLog()) {
                UtilTool.logStatic("原始数据：：：：：：："+statisticsData);
            }
			try {
//			    bean.mDataOption=0;
				if ((bean.mDataOption & PostBean.DATAHANDLECODE_ZIP) != 0
						&& (bean.mDataOption & PostBean.DATAHANDLECODE_ENCODE) != 0) {
					// 压缩
					statisticsData = UtilTool.gzip(statisticsData.getBytes());
					// 加密
					statisticsData = URLEncoder.encode(statisticsData, BaseConnectHandle.STATISTICS_DATA_CODE);
					statisticsData = CryptTool.encrypt(statisticsData, STATISTICS_DATA_ENCRYPT_KEY);
				} else if ((bean.mDataOption & PostBean.DATAHANDLECODE_ZIP) != 0) {
					// 压缩
					statisticsData = UtilTool.gzip(statisticsData.getBytes());
				} else if ((bean.mDataOption & PostBean.DATAHANDLECODE_ENCODE) != 0) {
					// 加密
					statisticsData = URLEncoder.encode(statisticsData, BaseConnectHandle.STATISTICS_DATA_CODE);
					statisticsData = CryptTool.encrypt(statisticsData, STATISTICS_DATA_ENCRYPT_KEY);
				}
				if (statisticsData != null) {
					DataOutputStream out = new DataOutputStream(mUrlConn.getOutputStream());
					if (UtilTool.isEnableLog()) {
		                UtilTool.logStatic("处理过的数据：：：：：：："+statisticsData);
		            }
					out.writeBytes(statisticsData);
					out.flush();
					out.close();
				}
				if (mUrlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					if (bean.mFunId == StatisticsManager.BASIC_FUN_ID ) {
                        if (UtilTool.isEnableLog()) {
                            UtilTool.logStatic("Basic fun upload success");
                        }
						bean.mState = PostBean.STATE_POSTED;
						mUrlConn.disconnect();
					} else {
						BufferedReader reader = null;
						try {
							reader = new BufferedReader(new InputStreamReader(mUrlConn.getInputStream()), 5);
							String jsonString;
							jsonString = reader.readLine();
							if (jsonString.trim().equalsIgnoreCase(JSON_REPONSE_RESULT_OK)) {
								if (UtilTool.isEnableLog()) {
									UtilTool.logStatic("Upload static data Ok:" + jsonString
											+ ", current url:" + mUrlConn.getURL().toString());
								}
								bean.mState = PostBean.STATE_POSTED;
							} else {
								bean.mState = PostBean.STATE_POSTFAILED;
							}
						} catch (Exception e) {
							// TODO: handle exception
						} finally {
							if (reader != null) {
								reader.close();
							}
							mUrlConn.disconnect();
						}
					}
					return;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			bean.mState = PostBean.STATE_POSTFAILED;
		}
	}

	@Override
	public boolean onPost(String statisticsData) throws Throwable {
		if (statisticsData != null) {
			try {
				if (statisticsData != null) {
					DataOutputStream out = new DataOutputStream(mUrlConn.getOutputStream());
					out.writeBytes(statisticsData);
					out.flush();
					out.close();
				}
				if (mUrlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					return true;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				mUrlConn.disconnect();
			}
		}
		return false;
	}
}
