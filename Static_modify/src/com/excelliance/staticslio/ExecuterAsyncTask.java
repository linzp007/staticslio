package com.excelliance.staticslio;

/**
 * 
 * <br>
 * 类描述:DB异步TASK <br>
 * 功能详细描述:
 * 
 * @author liuyuli
 *
 *         2016年7月5日下午5:52:28
 */
public class ExecuterAsyncTask implements Runnable {

    private Runnable mRunnable;
    private AsyncCallBack mCallBack;

    @Override
    public void run() {
        // TODO Auto-generated method stub
        if (mRunnable != null) {
            mRunnable.run();
        }
        if (mCallBack != null) {
            mCallBack.onFinish();
        }
    }

    public void addTask(Runnable runnable) {
        mRunnable = runnable;
    }

    /**
     * 增加任务完成后的回调
     * 
     * @param callBack
     */
    public void addCallBack(AsyncCallBack callBack) {
        mCallBack = callBack;
    }

    /**
     * 
     * <br>
     * 类描述:回调 <br>
     * 功能详细描述:
     * 
     * @author liuyuli
     *
     *         2016年7月5日下午5:53:55
     */
    public interface AsyncCallBack {
        public void onFinish();
    }
}
