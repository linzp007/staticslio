package com.excelliance.staticslio.beans;

import com.excelliance.staticslio.database.DataBaseHelper;
import com.excelliance.staticslio.utiltool.Machine;

import android.content.ContentValues;

/**
 * 控制信息Bean
 * 
 * @author liuyuli
 *
 * 2016年7月19日下午9:35:58 
 */
public class CtrlBean {
	/** 功能点ID */
	private int mFunID;
	/** 开关开始时间 */
	private long mStartTime;
	/** 开关有效时间戳 */
	private long mValidTime;
	/** 上传间隔时间 */
	private long mIntervalTime;

	private int mNetWork;

	/** 开关控制批次 */
	private String mBn;
	/** 开关更新时间 */
	private String mUpdateTime;

	private int mPriority = 0;
	public CtrlBean(long validTime, long intervalTime, String bn,String updateTime, int funID, long startTime, int network, int priority) {
		this.mValidTime = validTime;
		this.mStartTime = startTime;
		this.mIntervalTime = intervalTime;
		this.mBn = bn;
		this.mUpdateTime = updateTime;
		this.mFunID = funID;
		this.mNetWork = network;
		this.mPriority = priority;
	}

	public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.TABLE_CTRLINFO_COLOUM_BN, mBn);
        values.put(DataBaseHelper.TABLE_CTRLINFO_COLOUM_VALIDTIME, mValidTime);
        values.put(DataBaseHelper.TABLE_CTRLINFO_COLOUM_FUNID, mFunID);
        values.put(DataBaseHelper.TABLE_CTRLINFO_COLOUM_INTERVALTIME, mIntervalTime);
        values.put(DataBaseHelper.TABLE_CTRLINFO_COLOUM_STARTIME, mStartTime);
        values.put(DataBaseHelper.TABLE_CTRLINFO_COLOUM_UPDATETIME, mUpdateTime);
        values.put(DataBaseHelper.TABLE_CTRLINFO_COLOUM_UPDATETIME, mUpdateTime);
        values.put(DataBaseHelper.TABLE_CTRLINFO_COLOUM_NETWORK, mNetWork);
        values.put(DataBaseHelper.TABLE_CTRLINFO_COLOUM_PRIORITY, mPriority);

		return values;
	}

	public long getStartTime() {
		return mStartTime;
	}

	public void setStartTime(long startTime) {
		this.mStartTime = startTime;
	}

	public long getValidTime() {
		return mValidTime;
	}

	public void setValidTime(long duration) {
		this.mValidTime = duration;
	}

	public long getIntervalTime() {
		return mIntervalTime;
	}

	public void setIntervalTime(long intervalTime) {
		this.mIntervalTime = intervalTime;
	}

	public String getBn() {
		return mBn;
	}

	public void setBn(String bn) {
		this.mBn = bn;
	}

	public int getFunID() {
		return mFunID;
	}

	public void setFunID(int funID) {
		this.mFunID = funID;
	}

	public String getUpdateTime() {
		return mUpdateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.mUpdateTime = updateTime;
	}

	public int getNetwork() {
		return mNetWork;
	}

	public void setNetwork(int network) {
		this.mNetWork = network;
	}

	public int getPriority() {
		return mPriority;
	}

	public void setPriority(int mPriority) {
		this.mPriority = mPriority;
	}

    @Override
    public String toString() {
        return "CtrlBean [mFunID=" + mFunID + ", mStartTime=" + mStartTime + ", mValidTime=" + mValidTime + ", mIntervalTime=" + mIntervalTime + ", mNetWork=" + mNetWork + ", mBn=" + mBn + ", mUpdateTime=" + mUpdateTime + ", mPriority=" + mPriority + "]";
    }
	
}
