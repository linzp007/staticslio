package com.excelliance.staticslio;

import java.util.List;

import com.excelliance.staticslio.beans.PostBean;

import android.util.Log;


/**
 * @author liuyuli
 *
 * 2016年7月5日下午2:53:20 
 */
public class PostQueue {

	// private LinkedList<PostBean> mPostList;
	private PostBean mFirst;
	private PostBean mLast;

	private static final int MAX_POST_BEANS = 30;
	int i=0;
	public void push(PostBean bean) {
		if (bean != null) {
			synchronized (this) {
				PostBean tmpBean = bean;
				if (mFirst == null) {
					mFirst = bean;
				}
				if (mLast == null) {
					mLast = mFirst;
				}
				if (mLast != bean) {
					mLast.mNext = bean;
				}
				while (tmpBean.mNext != null) { // 插入的是一个队列
					tmpBean = tmpBean.mNext;
				}
				mLast = tmpBean;
				mLast.mNext = null; // 保证不是个环
			}
		}
	}

	
	/**
	 * <br>
	 * 功能简述:获取相同属性的统计数据，批量上传 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @return
	 */
	public PostBean pop() {
		synchronized (this) {
			if (mFirst != null) {
				PostBean head = mFirst;
				PostBean tmp = head;
				PostBean next = head.mNext;
				PostBean tmpLast = null;
				mFirst = null;
				int count = 1;
				while (next != null && count < MAX_POST_BEANS) {
				    // 找到相同url的post数据
                    if ((tmp.mFunId == next.mFunId) && tmp.mDataOption == next.mDataOption) {
                        tmp.mNext = next;
                        tmp = next;
                        count++;
					} else {
						if (mFirst == null) {
							mFirst = next;
						} else {
							if (tmpLast == null) {
								mFirst.mNext = next;
							} else {
								tmpLast.mNext = next;
							}
							tmpLast = next;
						}
					}
					next = next.mNext;
				}
				if (mFirst == null) {
					mFirst = next;
				} else if (mFirst != null && tmpLast == null) { // 剩余队列只有mFirst一个元素
					mFirst.mNext = next;
				}
				if (next == null || tmp == mLast) { // 遍历到最后一个
					mLast = tmpLast;
				} else if (tmpLast != null) { // 未遍历到最后，队列已满，把两段接起来
					tmpLast.mNext = next;
				}
				if (mLast != null) {
					mLast.mNext = null;
				}
				tmp.mNext = null;
				return head;
			}
		}
		return null;

	}

	public void push(List<PostBean> beans) {
		synchronized (this) {
			if (beans != null && !beans.isEmpty()) {
				for (PostBean bean : beans) {
					if (!isDataExist(bean)) {
						push(bean);
					}
				}
			}
		}
	}

	private boolean isDataExist(PostBean bean) {
		if (bean == null) {
			return true;
		}
		PostBean tmp = mFirst;
		while (tmp != null) {
			if (tmp.mId == bean.mId) {
				return true;
			} else {
				tmp = tmp.mNext;
			}

		}
		return false;
	}
//————————————————————————————————————————————————————————————废 除————————————————————————————————————————————————————————————
	public void clear() {
		// mPostList.clear();
	}
	// public PostBean pop() {
    // synchronized (this) {
    // if (mFirst != null) {
    // PostBean bean = mFirst;
    // mFirst = mFirst.mNext;
    // bean.mNext = null;
    // return bean;
    // }
    // }
    // return null;
    //
    // }
}
