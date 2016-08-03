package com.excelliance.staticslio.connect;

//CHECKSTYLE:OFF
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

import com.excelliance.staticslio.StatisticsManager;
import com.excelliance.staticslio.beans.PostBean;
import com.excelliance.staticslio.utiltool.UtilTool;

import android.content.Context;

/**
 * 网络处理类 检查数据及网络是否可以
 * 
 * @author liuyuli
 *
 *         2016年7月9日下午6:58:06
 */
public abstract class BaseConnectHandle {
    private static final int HTTP_REQUEST_TIMEOUT = 30 * 1000;
    public static final int RET_ERRO_NONE = 0;
    public static final int RET_ERRO_EXCEPTION = 1;
    public static final int RET_ERRO_MALFORMEDURLEXCEPTION = 2;

    protected HttpURLConnection mUrlConn;
    protected Context mContext;
    public static final String JSON_REPONSE_RESULT_OK = "OK";
    public static final String STATISTICS_DATA_CODE = "UTF-8";

    private static final String POST_DATA_URL = "http://mto.multiopen.cn/functiondata.php";
    public static final String UPLOAD_DEBUG_URL = "http://mto.multiopen.cn/userdatabk.php";
    public static final String BASIC_POST_DATA_URL = "http://mto.multiopen.cn/userdata.php";

    public void postData(PostBean bean) {
        if (BaseConnectHandle.RET_ERRO_MALFORMEDURLEXCEPTION == prepareConnect(bean.mFunId, bean.mData)) {
            // 错误的url直接忽略该条数据
            return;
        }
        try {
            onPost(bean);
        } catch (Throwable t) {
            bean.mState = PostBean.STATE_POSTFAILED;
        }
    }

    public boolean postData(int funId, String buffer) {
        if (BaseConnectHandle.RET_ERRO_MALFORMEDURLEXCEPTION == prepareConnect(funId, buffer)) {
            return false;
        }
        try {
            return onPost(buffer);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return false;
    }

    /**
     *上传的数据每条用"\r\n" 分隔开
     * 
     * @param bean
     * @return
     * @throws Throwable
     */
    public StringBuilder buildData(PostBean bean) throws Throwable {
        StringBuilder builder = new StringBuilder(bean.mData);
        PostBean tmp = bean.mNext;
        while (tmp != null && tmp.mData != null) {
            builder.append("\r\n");
            builder.append(tmp.mData);
            tmp = tmp.mNext;
        }
        return builder;
    }

    /**
     * <br>
     * 功能简述:真正上传数据接口 <br>
     * 功能详细描述: <br>
     * 注意: com.gau.go.gostaticsdk.connect.BasicConnHandle
     * 
     * @param bean
     */
    public abstract void onPost(PostBean bean) throws Throwable;

    public abstract boolean onPost(String buffer) throws Throwable;

    public BaseConnectHandle(Context context) {
        mContext = context;
    }

    /**
     * 检查数据是否正确
     * 
     * @param funid
     * @param urlString
     * @return
     */
    public int prepareConnect(int funid, String urlString) {
        int ret = RET_ERRO_NONE;
        try {
            URL url = null;
            if (StatisticsManager.getInstance(mContext).getDebugMode()) {
                if (UtilTool.isEnableLog()) {
                    UtilTool.logStatic("测试协议：" + url);
                }
                // 基础协议的url
                url = new URL(BaseConnectHandle.UPLOAD_DEBUG_URL);
                if (funid == StatisticsManager.BASIC_FUN_ID) {
                } else {
                    url = new URL(BaseConnectHandle.UPLOAD_DEBUG_URL);
                }
            } else {
                if (funid == StatisticsManager.BASIC_FUN_ID) {
                    url = new URL(BASIC_POST_DATA_URL);
                    if (UtilTool.isEnableLog()) {
                        UtilTool.logStatic("基础协议：" + url);
                    }
                } else {
                    url = new URL(POST_DATA_URL);
                    if (UtilTool.isEnableLog()) {
                        UtilTool.logStatic("非基础协议：" + url);
                    }
                }
                
            }
            Proxy proxy = null;
            HttpURLConnection urlConn;
            if (UtilTool.isCWWAPConnect(mContext) && UtilTool.getNetWorkType(mContext) != UtilTool.NETTYPE_UNICOM) {
                try {
                    if (UtilTool.getNetWorkType(mContext) == UtilTool.NETTYPE_TELECOM) {
                        String proxyHost = android.net.Proxy.getDefaultHost();
                        int port = android.net.Proxy.getDefaultPort();
                        proxy = new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(proxyHost, port));
                    } else {
                        // 联通的3gwap经测试不需设置代理
                        String host = UtilTool.getProxyHost(mContext);
                        int port = UtilTool.getProxyPort(mContext);
                        proxy = new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(host, port));
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            if (proxy != null) {
                urlConn = (HttpURLConnection) url.openConnection(proxy);
            } else {
                urlConn = (HttpURLConnection) url.openConnection();
            }
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setRequestMethod("POST");
            urlConn.setUseCaches(false);
            urlConn.setInstanceFollowRedirects(true);
            urlConn.setConnectTimeout(HTTP_REQUEST_TIMEOUT);
            urlConn.setReadTimeout(HTTP_REQUEST_TIMEOUT);

            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            mUrlConn = urlConn;
        } catch (MalformedURLException e) {
            // TODO: handle exception
            ret = RET_ERRO_MALFORMEDURLEXCEPTION;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ret = RET_ERRO_EXCEPTION;
        }

        return ret;
    }

    // public int prepareConnect(String urlString, int funid) {
    // int ret = RET_ERRO_NONE;
    // try {
    // URL url = null;
    // if (urlString != null && !urlString.trim().equals("null")
    // && !urlString.trim().equals("")) {
    // url = new URL(urlString);
    // } else if (StatisticsManager.getInstance(mContext).getDebugMode()) {
    // if (funid == StatisticsManager.BASIC_FUN_ID) {
    // url = new URL(StatisticsManager.TEST_UPLOADURL
    // + StatisticsManager.BASIC_CONTROL);
    // } else {
    // url = new URL(StatisticsManager.TEST_UPLOADURL);
    // }
    // } else {
    // if (funid == StatisticsManager.BASIC_FUN_ID) {
    // url = new URL(StatisticsManager.UPLOAD_URL
    // + StatisticsManager.BASIC_CONTROL);
    // } else {
    // url = new URL(StatisticsManager.UPLOAD_URL);
    // }
    // }
    // Proxy proxy = null;
    // HttpURLConnection urlConn;
    // if (UtilTool.isCWWAPConnect(mContext)
    // && UtilTool.getNetWorkType(mContext) != UtilTool.NETTYPE_UNICOM) {
    // try {
    // if (UtilTool.getNetWorkType(mContext) == UtilTool.NETTYPE_TELECOM) {
    // String proxyHost = android.net.Proxy.getDefaultHost();
    // int port = android.net.Proxy.getDefaultPort();
    // proxy = new Proxy(java.net.Proxy.Type.HTTP,
    // new InetSocketAddress(proxyHost, port));
    // } else {
    // // 联通的3gwap经测试不需设置代理
    // String host = UtilTool.getProxyHost(mContext);
    // int port = UtilTool.getProxyPort(mContext);
    // proxy = new Proxy(java.net.Proxy.Type.HTTP,
    // new InetSocketAddress(host, port));
    // }
    // } catch (Exception e) {
    // // TODO: handle exception
    // }
    // }
    // if (proxy != null) {
    // urlConn = (HttpURLConnection) url.openConnection(proxy);
    // } else {
    // urlConn = (HttpURLConnection) url.openConnection();
    // }
    // urlConn.setDoOutput(true);
    // urlConn.setDoInput(true);
    // urlConn.setRequestMethod("POST");
    // urlConn.setUseCaches(false);
    // urlConn.setInstanceFollowRedirects(true);
    // urlConn.setConnectTimeout(HTTP_REQUEST_TIMEOUT);
    // urlConn.setReadTimeout(HTTP_REQUEST_TIMEOUT);
    //
    // urlConn.setRequestProperty("Content-Type",
    // "application/x-www-form-urlencoded");
    // mUrlConn = urlConn;
    // } catch (MalformedURLException e) {
    // // TODO: handle exception
    // ret = RET_ERRO_MALFORMEDURLEXCEPTION;
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // ret = RET_ERRO_EXCEPTION;
    // }
    //
    // return ret;
    // }
}
