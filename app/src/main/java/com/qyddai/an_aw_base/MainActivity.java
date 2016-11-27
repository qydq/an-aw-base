package com.qyddai.an_aw_base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.an.base.utils.DUtilsDialog;
import com.an.base.view.SuperActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends SuperActivity {
    @ViewInject(R.id.tvChangModel)
    private Button tvChangModel;
    @ViewInject(R.id.btnDialog)
    private Button btnDialog;
    private SharedPreferences.Editor editor;
    private Dialog dialog;

    @Override
    public void initView() {
        editor = sp.edit();
        tvChangModel.setText("现在是白天，点击切换");
        tvChangModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.getBoolean("isNight", false)) {
                    getWindow().getDecorView().setBackground(ContextCompat.getDrawable(mContext, R.drawable.yy_drawable_bgday_shape));
                    tvChangModel.setText("现在是白天，点击切换晚上");
                    editor.putBoolean("isNight", false);
                } else {
                    getWindow().getDecorView().setBackground(ContextCompat.getDrawable(mContext, R.drawable.yy_drawable_bgnigt_shape));
                    tvChangModel.setText("现在是晚上，点击切换白天");
                    editor.putBoolean("isNight", true);
                }
                editor.commit();
            }
        });
        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = DUtilsDialog.INSTANCE.
                        createDialog(mContext,"加载中```",true);
                dialog.show();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                },1000);
            }
        });
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dialog.dismiss();
            Intent intent = new Intent(MainActivity.this,TestActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public void onNetChange(int netModile) {
        super.onNetChange(netModile);
        if (netModile == 1) {
            showToast("连接无线网络");
        } else if (netModile == 0) {
            showToast("连接移动网络");
        } else if (netModile == -1) {
            showToast("没有连接网络");
        }
    }
}
