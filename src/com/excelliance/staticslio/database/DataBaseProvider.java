package com.excelliance.staticslio.database;

//CHECKSTYLE:OFF
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.excelliance.staticslio.ExecuterAsyncTask;
import com.excelliance.staticslio.StaticDataContentProvider;
import com.excelliance.staticslio.ExecuterAsyncTask.AsyncCallBack;
import com.excelliance.staticslio.beans.CtrlBean;
import com.excelliance.staticslio.beans.PostBean;
import com.excelliance.staticslio.utiltool.UtilTool;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * 
 * 
 * 类描述:DB封装工具
 *
 * @author liuyuli
 *
 *         2016年7月5日下午4:42:30
 */
public class DataBaseProvider {

    private Context mContext;
    private DataBaseHelper mHelp;
    private boolean mCanNotFindUrl = false;
    private ExecutorService mSingleExecutor;

    public DataBaseProvider(Context context) {
        mContext = context;
        mSingleExecutor = Executors.newSingleThreadExecutor();
    }

    /**
     * 插入数据到数据库
     * 
     * @param bean
     * @param callBack
     */
//    public void insertPostDataAsync(final PostBean bean, AsyncCallBack callBack) {
//        ExecuterAsyncTask task = new ExecuterAsyncTask();
//        task.addCallBack(callBack);
//        task.addTask(new Runnable() {
//            @Override
//            public void run() {
//                ContentResolver resolver = mContext.getContentResolver();
//                Uri ret = null;
//                try {
//                    ret = resolver.insert(StaticDataContentProvider.sNewUrl, bean.getContentValues());
//                    if (ret != null) {
//                        bean.setFromDB(true);
//                    }
//                    if (UtilTool.isEnableLog()) {
//                        UtilTool.logStatic("Insert static Data to DB:" + bean.getContentValues().get(DataBaseHelper.TABLE_STATISTICS_COLOUM_DATA));
//                    }
//                } catch (Exception e) {
//                    mCanNotFindUrl = true;
//                }
//            }
//        });
//        try {
//            if (!mSingleExecutor.isShutdown()) {
//                mSingleExecutor.execute(task);
//            }
//        } catch (Exception e) {
//            UtilTool.printException(e);
//        }
//    }
    
    /**
     * 插入数据到数据库
     * 
     * 非异步
     * 
     * @param bean
     */
    public void insertPostData(PostBean bean) {
        ContentResolver resolver = this.mContext.getContentResolver();
        Uri ret = null;
        try {
            ret = resolver.insert(StaticDataContentProvider.sNewUrl, bean.getContentValues());
            if (ret != null) {
                bean.setFromDB(true);
            }
            if (UtilTool.isEnableLog()) {
                UtilTool.logStatic("Insert static Data to DB:" + bean.getContentValues().get(DataBaseHelper.TABLE_STATISTICS_COLOUM_DATA));
            }
        } catch (Exception e) {
            this.mCanNotFindUrl = true;
        }
    }
    
    public void insertCtrlInfo(Map<String, CtrlBean> ctrlBeanMap) {
        for (String key : ctrlBeanMap.keySet()) {
            CtrlBean bean = (CtrlBean) ctrlBeanMap.get(key);
            ContentResolver resolver = this.mContext.getContentResolver();
            try {
                resolver.insert(StaticDataContentProvider.sCtrlInfoUrl, bean.getContentValues());
            } catch (Exception e) {
                this.mCanNotFindUrl = true;
            }
        }
    }
//    public void insertCtrlInfoAsync(final Map<String, CtrlBean> ctrlBeanMap, AsyncCallBack callBack) {
//        ExecuterAsyncTask task = new ExecuterAsyncTask();
//        task.addCallBack(callBack);
//        task.addTask(new Runnable() {
//            @Override
//            public void run() {
//                for (String key : ctrlBeanMap.keySet()) {
//                    CtrlBean bean = ctrlBeanMap.get(key);
//                    ContentResolver resolver = mContext.getContentResolver();
//                    try {
//                        resolver.insert(StaticDataContentProvider.sCtrlInfoUrl, bean.getContentValues());
//                    } catch (Exception e) {
//                        mCanNotFindUrl = true;
//                    }
//                }
//            }
//        });
//        try {
//            if (!mSingleExecutor.isShutdown()) {
//                mSingleExecutor.execute(task);
//            }
//        } catch (Exception e) {
//            UtilTool.printException(e);
//        }
//
//    }

    
    public Map<String, CtrlBean> queryCtrlInfo() {
        Map<String, CtrlBean> ctrlMap = new HashMap<String, CtrlBean>();
        Cursor cursor = null;
        try {
            ContentResolver resolver = mContext.getContentResolver();
            cursor = resolver.query(StaticDataContentProvider.sCtrlInfoUrl, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()) {
                    CtrlBean bean = new CtrlBean(cursor.getLong(cursor.getColumnIndex(DataBaseHelper.TABLE_CTRLINFO_COLOUM_VALIDTIME)), cursor.getLong(cursor.getColumnIndex(DataBaseHelper.TABLE_CTRLINFO_COLOUM_INTERVALTIME)), cursor.getString(cursor.getColumnIndex(DataBaseHelper.TABLE_CTRLINFO_COLOUM_BN)),
                            cursor.getString(cursor.getColumnIndex(DataBaseHelper.TABLE_CTRLINFO_COLOUM_UPDATETIME)), cursor.getInt(cursor.getColumnIndex(DataBaseHelper.TABLE_CTRLINFO_COLOUM_FUNID)), cursor.getLong(cursor.getColumnIndex(DataBaseHelper.TABLE_CTRLINFO_COLOUM_STARTIME)), cursor.getInt(cursor.getColumnIndex(DataBaseHelper.TABLE_CTRLINFO_COLOUM_NETWORK)),
                            cursor.getInt(cursor.getColumnIndex(DataBaseHelper.TABLE_CTRLINFO_COLOUM_PRIORITY)));
                    ctrlMap.put(String.valueOf(bean.getFunID()), bean);
                }
            }
        } catch (Exception e) {
            UtilTool.printException(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return ctrlMap;
    }

    public LinkedList<PostBean> queryPostDatas(HashSet<String> funidList) {
        Cursor cursor = null;
        LinkedList<PostBean> list = new LinkedList<PostBean>();
        StringBuffer where = null;
        if (funidList != null && funidList.size() > 0) {
            where = new StringBuffer("funid IN (");
            for (String funid : funidList) {
                where.append(funid + ",");
            }
            where.deleteCharAt(where.length() - 1);
            where.append(")");
        }

        if (where == null) {
            return list;
        }
        try {
            ContentResolver resolver = mContext.getContentResolver();
            cursor = resolver.query(StaticDataContentProvider.sNewUrl, null, where.toString(), null, DataBaseHelper.TABLE_STATISTICS_COLOUM_ID + " DESC limit " + QUERYLIMIT);
            if (cursor != null) {
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()) {
                    PostBean bean = new PostBean();
                    bean.parse(cursor);
                    list.add(bean);
                }
                if (UtilTool.isEnableLog()) {
                    UtilTool.logStatic("Query post data:" + where.toString() + ",data count:" + cursor.getCount());
                }
            } else if (mCanNotFindUrl && cursor == null) {
                throw new IllegalArgumentException("Unknown URL" + StaticDataContentProvider.sNewUrl);
            }
        } catch (Exception e) {
            UtilTool.printException(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    private static final int QUERYLIMIT = 300;

    /**
     * 查询数据库中未上传但是急需上传的数据（isold = 1）
     * 
     * @return
     */
    public LinkedList<PostBean> queryOldData() {
        Cursor cursor = null;
        LinkedList<PostBean> list = new LinkedList<PostBean>();
        try {
            ContentResolver resolver = mContext.getContentResolver();
            cursor = resolver.query(StaticDataContentProvider.sNewUrl, null, "isold=1", null, DataBaseHelper.TABLE_STATISTICS_COLOUM_ID + " DESC limit " + QUERYLIMIT);
            if (cursor != null) {
                if (UtilTool.isEnableLog()) {
                    UtilTool.logStatic("Query all old data, data count:" + cursor.getCount());
                }
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()) {
                    PostBean bean = new PostBean();
                    bean.parse(cursor);
                    list.add(bean);
                }
            } else if (mCanNotFindUrl && cursor == null) {
                throw new IllegalArgumentException("Unknown URL" + StaticDataContentProvider.sNewUrl);
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    public LinkedList<PostBean> queryAllData() {
        Cursor cursor = null;
        LinkedList<PostBean> list = new LinkedList<PostBean>();
        try {
            ContentResolver resolver = mContext.getContentResolver();
            cursor = resolver.query(StaticDataContentProvider.sNewUrl, null, null, null, DataBaseHelper.TABLE_STATISTICS_COLOUM_ID + " DESC");
            if (cursor != null) {
                if (UtilTool.isEnableLog()) {
                    UtilTool.logStatic("Query all data in db, data count:" + cursor.getCount());
                }
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()) {
                    PostBean bean = new PostBean();
                    bean.parse(cursor);
                    list.add(bean);
                }
            } else if (mCanNotFindUrl && cursor == null) {
                throw new IllegalArgumentException("Unknown URL" + StaticDataContentProvider.sNewUrl);
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    public int queryDataCount() {
        Cursor cursor = null;
        int count = 0;
        try {
            ContentResolver resolver = mContext.getContentResolver();
            cursor = resolver.query(StaticDataContentProvider.sNewUrl, null, null, null, null);
            if (cursor != null) {
                count = cursor.getCount();
            } else if (mCanNotFindUrl && cursor == null) {
                throw new IllegalArgumentException("Unknown URL" + StaticDataContentProvider.sNewUrl);
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    public LinkedList<PostBean> queryPostDatas(String funid) {
        Cursor cursor = null;
        LinkedList<PostBean> list = null;

        try {
            ContentResolver resolver = mContext.getContentResolver();
            cursor = resolver.query(StaticDataContentProvider.sNewUrl, null, "funid IN (" + funid + ")", null, DataBaseHelper.TABLE_STATISTICS_COLOUM_ID + " DESC limit " + QUERYLIMIT);

            if (cursor != null) {
                if (UtilTool.isEnableLog()) {
                    UtilTool.logStatic("Query Post Data In funid:" + funid + " and data Count:" + cursor.getCount());
                }
                list = new LinkedList<PostBean>();
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()) {
                    PostBean bean = new PostBean();
                    bean.parse(cursor);
                    list.add(bean);
                }

            } else if (mCanNotFindUrl && cursor == null) {
                throw new IllegalArgumentException("Unknown URL" + StaticDataContentProvider.sNewUrl);
            }
        } catch (Exception e) {
            UtilTool.printException(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return list;
    }

    public void deletePushData(PostBean bean) {
        // TODO Auto-generated method stub
        StringBuilder builder = new StringBuilder();
        PostBean tmp = bean;
        builder.append("(");
        int beanCount = 0;
        while (tmp != null) {
            beanCount++;
            builder.append("'");
            builder.append(tmp.mId);
            builder.append("'");
            if (tmp.mNext != null) {
                builder.append(",");
            }
            tmp = tmp.mNext;
        }
        builder.append(")");
        String where = null;
        if (beanCount > 1) {
            where = "funid=" + bean.mFunId + " and " + DataBaseHelper.TABLE_STATISTICS_COLOUM_ID + " IN " + builder.toString();
        } else {
            where = "funid=" + bean.mFunId + " and " + DataBaseHelper.TABLE_STATISTICS_COLOUM_ID + "='" + bean.mId + "'";
        }
        try {
            ContentResolver resolver = mContext.getContentResolver();
            int count = resolver.delete(StaticDataContentProvider.sNewUrl, where, null);
            if (UtilTool.isEnableLog()) {
                UtilTool.log(null, "deletePushData from db count:" + count + ",where:" + where);
            }
        } catch (Exception e) {
            UtilTool.printException(e);
        }
    }

    private synchronized void closeDB() {
        if (mHelp != null) {
            mHelp.close();
        }
    }

    public void destory() {
        try {
            mSingleExecutor.shutdown();
            closeDB();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            UtilTool.printException(e);
        }
    }

    /**
     * 未上传的数据设置为旧数据
     * 
     * @param bean
     * 
     *            取出所有bean的id，根据id查找，将isold的值设为true
     */
    public void setDataOld(PostBean bean) {
        PostBean tmp = bean.mNext;
        StringBuffer buffer = new StringBuffer();
        buffer.append("'" + bean.mId + "',");
        while (tmp != null) {
            buffer.append("'" + tmp.mId + "',");
            tmp = tmp.mNext;
        }
        String where = "";
        buffer.deleteCharAt(buffer.length() - 1);
        where = buffer.toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.TABLE_STATISTICS_COLOUM_ISOLD, true);
        try {
            ContentResolver resolver = mContext.getContentResolver();
            int count = resolver.update(StaticDataContentProvider.sNewUrl, contentValues, "id IN (" + where + ")", null);
            if (UtilTool.isEnableLog()) {
                UtilTool.log(null, "setDataOld in db count:" + count);
            }
        } catch (Exception e) {
            UtilTool.printException(e);
        }
    }

    /**
     * 将所有现有数据库中的数据的isold字段设置为true
     */
    public int setAllDataOld() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.TABLE_STATISTICS_COLOUM_ISOLD, true);
        int count = 0;
        try {
            ContentResolver resolver = mContext.getContentResolver();
            count = resolver.update(StaticDataContentProvider.sNewUrl, contentValues, "isold=0", null);
            if (UtilTool.isEnableLog()) {
                UtilTool.logStatic("Set Data new to old,success count:" + count);
            }
        } catch (Exception e) {
            UtilTool.printException(e);
        }
        return count;
    }

    public int deleteOldCtrlInfo() {
        int count = 0;
        try {
            ContentResolver resolver = mContext.getContentResolver();
            count = resolver.delete(StaticDataContentProvider.sCtrlInfoUrl, null, null);
            if (UtilTool.isEnableLog()) {
                UtilTool.logStatic("Delete old ctrlInfo from db, ctrlInfo count:" + count);
            }
        } catch (Exception e) {
            UtilTool.printException(e);
            // try {
            // count = getDataHelper().delete(DataBaseHelper.TABLE_CTRLINFO,
            // null, null);
            // } catch (Exception e1) {
            // // TODO Auto-generated catch block
            // UtilTool.printException(e1);
            // }
        }
        return count;
    }

    public PostBean queryPostData(String stringExtra) {
        Cursor cursor = null;
        PostBean bean = null;

        try {
            ContentResolver resolver = mContext.getContentResolver();

            cursor = resolver.query(StaticDataContentProvider.sNewUrl, null, "id IN ('" + stringExtra + "')", null, null);

            if (cursor != null && cursor.getCount() > 0) {
                if (UtilTool.isEnableLog()) {
                    UtilTool.logStatic("Query Post Data In id:" + stringExtra + " and data Count:" + cursor.getCount());
                }
                cursor.moveToPosition(0);

                bean = new PostBean();
                bean.parse(cursor);
                if (UtilTool.isEnableLog()) {
                    UtilTool.logStatic("beanData:" + bean.mData);
                }
            } else if (mCanNotFindUrl && cursor == null) {
                throw new IllegalArgumentException("Unknown URL" + StaticDataContentProvider.sNewUrl);
            }
        } catch (Exception e) {
            UtilTool.printException(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return bean;
    }

  /*  public void deleteOldData(LinkedList<PostBean> bean) {
        StringBuffer where = new StringBuffer("id IN (");
        for (PostBean postBean : bean) {
            where.append("" + postBean.mId + ",");
        }
        where.deleteCharAt(where.length() - 1);
        where.append(")");
        try {
            ContentResolver resolver = mContext.getContentResolver();
            int count = resolver.delete(StaticDataContentProvider.sUrl, where.toString(), null);
            if (UtilTool.isEnableLog()) {
                UtilTool.log(null, "Delete old data from db and where: " + where.toString() + " and count:" + count);
            }
        } catch (Exception e) {
            UtilTool.printException(e);
        }
    }*/
    
    
    /**
     * 查询老版本（Version < 1.10）SDK数据库中的数据
     * 
     * @return
     */
   /* public LinkedList<PostBean> queryOldSDKVersionData() {
        Cursor cursor = null;
        LinkedList<PostBean> list = null;
        try {
            ContentResolver resolver = mContext.getContentResolver();
            cursor = resolver.query(StaticDataContentProvider.sUrl, null, null, null, null);
            if (cursor != null) {
                list = new LinkedList<PostBean>();
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()) {
                    PostBean bean = new PostBean();
                    bean.parse(cursor);
                    list.add(bean);
                }

            } else if (mCanNotFindUrl && cursor == null) {
                throw new IllegalArgumentException("Unknown URL" + StaticDataContentProvider.sUrl);
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }
*/
}
