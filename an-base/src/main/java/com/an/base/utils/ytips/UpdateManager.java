package com.an.base.utils.ytips;

/**********************************************************
 * @文件名称：UpdateManger
 * @文件作者：staryumou@163.com
 * @创建时间：2016/8/18
 * @文件描述：提供一个软件更新的接口，这里需要服务器判断
 * @修改历史：2016/8/18
 **********************************************************/

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.an.base.R;
import com.an.base.model.ytips.XHttps;
import com.an.base.model.ytips.XProgressCallBack;
import com.an.base.utils.DataService;

import org.xutils.http.RequestParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateManager {

    private Context mContext;

    //提示语
    private String updateMsg = "有最新的软件包哦，亲快下载吧~";

    //返回的安装包url
    private String apkUrl;

    private String apkName;


    private Dialog noticeDialog;

    private Dialog downloadDialog;
    /* 下载包安装路径 */
    private String savePath = "/sdcard/an/apk/";//默认保存在an/apk框架中sdcard写法不严谨

    private String saveFileName;//操作的saveFileName

    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;


    private static final int DOWN_UPDATE = 1;

    private static final int DOWN_OVER = 2;

    private int progress;

    private Thread downLoadThread;

    private boolean interceptFlag = false;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
                default:
                    break;
            }
        }
    };

    public UpdateManager(Context context, String apkUrl, String apkName) {
        this.mContext = context;
        this.apkUrl = apkUrl;
        this.apkName = apkName;
        saveFileName = savePath + apkName + "_" + DataService.INSTANCE.getShotDateTime() + ".apk";//这里用时间区分。
    }

    /*savePath 参考 = DUtilsStorage.INSTANCE.getskRootPath()
    * savePath = "/sdcard/an/apk/apkname_time.apk
    * 备注后面不用加 “/”
    * */
    public UpdateManager(Context context, String savePath, String apkUrl, String apkName) {
        this.mContext = context;
        this.apkUrl = apkUrl;
        this.savePath = savePath;
        saveFileName = savePath + File.separator + apkName + "_" + DataService.INSTANCE.getShotDateTime() + ".apk";
    }

    //外部接口让主Activity调用
    public void checkUpdateInfo() {
        showNoticeDialog();
    }


    private void showNoticeDialog() {
        Builder builder = new Builder(mContext);
        builder.setTitle("软件版本更新");
        builder.setMessage(updateMsg);
        builder.setPositiveButton("下载", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
            }
        });
        builder.setNegativeButton("以后再说", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }

    private void showDownloadDialog() {
        Builder builder = new Builder(mContext);
        builder.setTitle("软件版本更新");

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.base_progress_update_standard, null);
        mProgress = (ProgressBar) v.findViewById(R.id.anProgressUpdateBar);

        builder.setView(v);
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
            }
        });
        downloadDialog = builder.create();
        downloadDialog.show();

        downloadApk();
    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    //更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        //下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);//点击取消就停止下载.

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    /**
     * 下载apk
     *
     * @param
     */

    private void downloadApk() {
        RequestParams params = new RequestParams(apkUrl);
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(saveFileName);
        XHttps.downLoadFile(apkUrl, saveFileName, new XProgressCallBack<File>() {

            @Override
            public void onSuccess(File result) {
                if (downloadDialog != null && downloadDialog.isShowing()) {
                    downloadDialog.dismiss();
                }
                installApk();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                super.onLoading(total, current, isDownloading);
                mProgress.setMax((int) total);
                progress = (int) current;
                mHandler.sendEmptyMessage(DOWN_UPDATE);
            }
        });

//        downLoadThread = new Thread(mdownApkRunnable);
//        downLoadThread.start();
    }

    /**
     * 安装apk
     *
     * @param
     */

    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.setDataAndType(Uri.fromFile(new File(saveFileName)), "application/vnd.android.package-archive");
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }
}

