package com.excelliance.test;

import java.util.HashMap;
import java.util.Map;

import com.excelliance.multiaccount.R;
import com.excelliance.staticslio.OnInsertDBListener;
import com.excelliance.staticslio.StatisticsManager;
import com.excelliance.staticslio.beans.OptionBean;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
//CHECKSTYLE:OFF
//该类调试时使用，无需加入jar包

public class MainActivity extends Activity {
    protected static final String SENDER_OBJ_SEPARATE = "||";
    protected static final String END_OBJ_SEPAPATE = "||null||null||";
    protected static final String TAG = "lyl";
    protected final static int funid = 483;
    protected final static int TONG_45 = 45;
    int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn201 = (Button) findViewById(R.id.button201);
        Button btn202 = (Button) findViewById(R.id.button202);
        Button btn203 = (Button) findViewById(R.id.button203);
        Button btn211 = (Button) findViewById(R.id.button211);
        Button btn212 = (Button) findViewById(R.id.button212);
        Button btn213 = (Button) findViewById(R.id.button213);
        btn201.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 日志序列||产品ID||AndroidID||日志打印时间||OS||ROM||机型||语言地区||渠道||版本号||版本名||产品客户端类型||手机号码||手机运营商||是否付费用户||是否GO桌面用户||goid||是否已root||是否新用户||来源产品包名||来源产品id||正版标识||CPU架构类型||手机分辨率||产品包名||GADID||用户手机语言||默认桌面包名||是否后台运行||用户随机数||屏幕像素密度(ppi)||CPU型号||CPU频率||CPU核数||RAM容量(MB)||ROM容量(MB)||SD卡容量(MB)
//                (String productID, String channel, boolean isPay, String key, boolean isNew)
                //需要上传这些字段          产品ID(必传项),渠道号,是否是付费用户,
                StatisticsManager.getInstance(getApplicationContext()).upLoadBasicInfoStaticData("105", "200", false, "A", true);

            }
        });

        btn202.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 此协议会每隔8小时上传一次

                // 拼装格式： 功能点ID||统计对象||操作代码||操作结果||入口||tab分类||位置||关联对象||备注
                StatisticsManager.getInstance(getApplicationContext()).uploadStaticData(101, 460, "460||com.facebook.io||app_a000||1||1||1||1||null||null");
                // 拼装格式： 功能点ID||统计对象||操作代码||操作结果||入口||tab分类||位置||关联对象||备注
                StatisticsManager.getInstance(getApplicationContext()).uploadStaticData(101, 490, "490|| ||guide_page||1||1||a|| || || ");
                // 拼装格式： 功能点ID||布局信息||类型||位置||备注
                StatisticsManager.getInstance(getApplicationContext()).uploadStaticData(102, 461, "461||1||3||||");

            }
        });

        btn203.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 此协议会即时上传

                // 拼装格式： 功能点ID||统计对象||操作代码||操作结果||入口||tab分类||位置||关联对象||备注
                StatisticsManager.getInstance(getApplicationContext()).uploadStaticData(59, 524, "524||com.excelliance.multiaccount.product1||j005||1||||||af36fg5364ad63431sds354684643654315||null||");
                // 拼装格式： 功能点ID||操作代码||操作结果||备注
                StatisticsManager.getInstance(getApplicationContext()).uploadStaticData(45, 483, "483||k001||1||");

            }
        });

        btn211.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 可选选项：<br>
                // com.excelliance.staticslio.beans.OptionBean#OPTION_INDEX_IMMEDIATELY_CARE_SWITCH
                // 为是否立即上传的标识符(在开关限制下立即上传)，传入true or false;<br>
                // com.excelliance.staticslio.beans.OptionBean#OPTION_INDEX_IMMEDIATELY_ANYWAY
                // 为是否立即上传的标识符(不管开关，直接、立即上传)，传入true or false;<br>
                // com.excelliance.staticslio.beans.OptionBean#OPTION_INDEX_POSITIONOPTION_INDEX_POSITION
                // 为位置信息；类型为字符串，如"105.23,155.88"<br>
                // com.excelliance.staticslio.beans.OptionBean#OPTION_INDEX_ABTESTOPTION_INDEX_ABTEST
                // 为ABTest值；类型为字符串,如"A"
                // 立即上传，不管有没有该功能点id的开关，都会立即，全量上传
                StatisticsManager.getInstance(getApplicationContext()).uploadStaticDataForOptions(101, 201, "201||统计对象||launch_de_po||1||入口||tab分类||位置||关联对象||备注||", null, new OptionBean(OptionBean.OPTION_INDEX_IMMEDIATELY_ANYWAY, true));

                // 立即上传，但必须该功能点id的开关是打开的，才会立即，全量上传
                StatisticsManager.getInstance(getApplicationContext()).uploadStaticDataForOptions(101, 201, "201||统计对象||launch_de_po||1||入口||tab分类||位置||关联对象||备注||", null, new OptionBean(OptionBean.OPTION_INDEX_IMMEDIATELY_CARE_SWITCH, true));

                // 传入gps参数，用字符串传入位置信息
                StatisticsManager.getInstance(getApplicationContext()).uploadStaticDataForOptions(101, 201, "201||统计对象||launch_de_po||1||入口||tab分类||位置||关联对象||备注||", null, new OptionBean(OptionBean.OPTION_INDEX_POSITION, "101.95,106.88"));

                // 传入abtest，用字符串传入a or b
                StatisticsManager.getInstance(getApplicationContext()).uploadStaticDataForOptions(101, 201, "201||统计对象||launch_de_po||1||入口||tab分类||位置||关联对象||备注||", null, new OptionBean(OptionBean.OPTION_INDEX_ABTEST, "A"));

                // 传入多个参数
                StatisticsManager.getInstance(getApplicationContext()).uploadStaticDataForOptions(101, 201, "201||统计对象||launch_de_po||1||入口||tab分类||位置||关联对象||备注||", null, new OptionBean(OptionBean.OPTION_INDEX_ABTEST, "A"), new OptionBean(OptionBean.OPTION_INDEX_IMMEDIATELY_CARE_SWITCH, true), new OptionBean(OptionBean.OPTION_INDEX_POSITION, "101.95,106.88"));

                // 巧妙使用db插入监听
                StatisticsManager.getInstance(getApplicationContext()).uploadStaticData(101, 201, "201||统计对象||launch_de_po||1||入口||tab分类||位置||关联对象||备注||", new OnInsertDBListener() {

                    @Override
                    public void onInsertToDBFinish() {
                        // 插入db后要干的事情，比如统计完这条数据马上结束进程。
                    }

                    @Override
                    public void onBeforeInsertToDB() {
                        // 插入db前要干的事情
                    }
                });
            }
        });

        btn212.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 手动查询数据库里存在的还没有来得及上传的数据（因为有的数据可能累计到8小时才上传一次）
                StatisticsManager.getInstance(getApplicationContext()).uploadAllData();
            }
        });

        btn213.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出程序时候要destroy统计sdk
        StatisticsManager.getInstance(getApplicationContext()).destory();
    }
    
    
    
    
}
