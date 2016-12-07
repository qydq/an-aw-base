package com.qyddai.an_aw_base;

import android.util.Log;
import android.widget.Button;

import com.an.base.model.entity.ResponseBaseModel;
import com.an.base.model.utils.XCallBack;
import com.an.base.model.utils.XHttps;
import com.an.base.model.utils.XProgressCallBack;
import com.an.base.view.SuperActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by stary on 2016/12/2.
 * 莳萝花，晴雨荡气，sunshuntao，qydq
 * Contact : qyddai@gmail.com
 * 说明：XhttpsActivity请求实例
 * 最后修改：on 2016/12/2.
 */
@ContentView(R.layout.activity_main)
public class XHttpsActivity extends SuperActivity {
    @ViewInject(R.id.tvChangModel)
    private Button tvChangModel;
    @ViewInject(R.id.btnDialog)
    private Button btnDialog;

    @Override
    public void initView() {
        getxml();
    }

    //得到xml的数据这里url为气象台的数据
    private void getxml() {
        String url = "http://flash.weather.com.cn/wmaps/xml/china.xml";
        XHttps.get(url, null, new XCallBack<String>() {

            @Override
            public void onSuccess(String xmlString) {
                super.onSuccess(xmlString);
                try {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xmlPullParser = factory.newPullParser();
                    xmlPullParser.setInput(new StringReader(xmlString));
                    int eventType = xmlPullParser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                String nodeName = xmlPullParser.getName();
                                if ("city".equals(nodeName)) {
                                    String pName = xmlPullParser.getAttributeValue(0);
                                    Log.e("TAG", "city is " + pName);
                                    showToast("city is:" + pName + "weather is");
                                }
                                break;
                        }
                        eventType = xmlPullParser.next();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }

        });
    }

    //下载带进度条的文件
    private void downloadprogressfile() {
        //文件下载地址
        String url = "";
        //文件保存在本地的路径
        String filepath = "";
        XHttps.downLoadFile(url, filepath, new XProgressCallBack<File>() {

            @Override
            public void onSuccess(File result) {
                super.onSuccess(result);
                showToast("result:" + result.toString());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);

            }

        });
    }
//an下载文件
    private void downloadfile() {
//文件下载地址
        String url = "";
        //文件保存在本地的路径
        String filepath = "";
        XHttps.downLoadFile(url, filepath, new XCallBack<File>() {
            @Override
            public void onSuccess(File result) {
                super.onSuccess(result);
                showToast("result:" + result.toString());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);

            }

        });
    }

    /**
     * 上传文件(支持多文件上传)
     */
    private void uploadfile() {
//图片上传地址
        String url = "";
        Map<String, Object> map = new HashMap<>();
        //传入自己的相应参数
        //map.put(key, value);
        //map.put(key, value);
        XHttps.upLoadFile(url, map, new XCallBack<String>() {

            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                showToast("result:" + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }

        });
    }

    //an框架get方法
    private void get() {
        String url = "http://api.k780.com:88/?app=idcard.get";
        Map<String, String> map = new HashMap<>();
        map.put("appkey", "10003");
        map.put("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4");
        map.put("format", "json");
        map.put("idcard", "110101199001011114");
        XHttps.get(url, map, new XCallBack<ResponseBaseModel>() {

            @Override
            public void onSuccess(ResponseBaseModel result) {
                super.onSuccess(result);
                Log.e("result", result.toString());
                showToast("result:" + result.getBody());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }

        });
    }

    //an框架post方法
    private void post() {
        String url = "http://api.k780.com:88/?app=idcard.get";
        Map<String, Object> map = new HashMap<>();
        map.put("appkey", "10003");
        map.put("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4");
        map.put("format", "json");
        map.put("idcard", "110101199001011114");
        XHttps.post(url, map, new XCallBack<ResponseBaseModel>() {

            @Override
            public void onSuccess(ResponseBaseModel result) {
                super.onSuccess(result);
                Log.e("result", result.toString());
                showToast("result,信息:" + result.getBody());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);

            }
        });
    }
}
