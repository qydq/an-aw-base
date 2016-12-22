package com.qyddai.an_aw_base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.an.base.contract.TaskDayNightContract;
import com.an.base.presenter.TaskDayNightPresenter;
import com.an.base.utils.DUtilsDialog;
import com.an.base.utils.DUtilsUi;
import com.an.base.utils.DataService;
import com.an.base.utils.NetBroadcastReceiverUtils;
import com.an.base.view.DUtilsActivity;
import com.an.base.view.SuperActivity;
import com.an.base.view.widget.WToggleButton;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

//如果添加注解，则夜间模式用不了，只能findViewById实现。
@ContentView(R.layout.activity_main)
public class MainActivity extends SuperActivity implements TaskDayNightContract.View {
    @ViewInject(R.id.tvChangModel)
    private Button tvChangModel;
    @ViewInject(R.id.btnDialog)
    private Button btnDialog;

    /*以上注解模块如不适用夜间模式可使用，本人已经反复验证过了，
    具体等待反馈给wyouflf,你可以点击dialog的按钮看看是否崩溃*/
    private SharedPreferences.Editor editor;
    private Dialog dialog;

    private WToggleButton toggleButton;
    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout;
    private TextView textView;

    private List<LinearLayout> mLayoutList;
    private List<RelativeLayout> mRelativeList;
    private List<Button> mButtonList;
    private List<TextView> mTextViewList;

    private TaskDayNightContract.Presenter presenter;

    //base_headview_standard_complex的引用
    private LinearLayout anLlRight;
    private LinearLayout anLlRRight;
    private TextView anTvRight;

    @Override
    public void initView() {
        editor = sp.edit();
        presenter = new TaskDayNightPresenter(sp, this);

        _initTheme();
        setContentView(R.layout.activity_main);

        toggleButton = (WToggleButton) findViewById(R.id.toggleBtn);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
        linearLayout = (LinearLayout) findViewById(R.id.anLlLayout);
        anLlRRight = (LinearLayout) findViewById(R.id.anLlRRight);
        anLlRight = (LinearLayout) findViewById(R.id.anLlRight);
        tvChangModel = (Button) findViewById(R.id.tvChangModel);
        btnDialog = (Button) findViewById(R.id.btnDialog);
        textView = (TextView) findViewById(R.id.textView);
        anTvRight = (TextView) findViewById(R.id.anTvRight);

        mLayoutList = new ArrayList<>();
        mTextViewList = new ArrayList<>();
        mRelativeList = new ArrayList<>();
        mButtonList = new ArrayList<>();
        mLayoutList.add(linearLayout);
        mRelativeList.add(relativeLayout);
        mButtonList.add(btnDialog);
        mTextViewList.add(textView);

        //该夜间模式
        tvChangModel.setText(DataService.INSTANCE.checkIp("") + "现在是白天，点击切换getNetWrokState:" + NetBroadcastReceiverUtils.getNetworkState(mContext) + "\n--isMobile:" + NetBroadcastReceiverUtils.isMobileConnected(mContext) + "--isWifi:" + NetBroadcastReceiverUtils.isWifiConnected(mContext) + "--isNetworkAvailable:" + NetBroadcastReceiverUtils.isConnectedToInternet(mContext));
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
        //设置初始化打开关闭的开关。
        if (presenter.isDay()) {
            toggleButton.setToggleOn();
        } else {
            toggleButton.setToggleOff();
        }
        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = DUtilsDialog.INSTANCE.
                        createDialog(mContext, "加载中```", true);
                dialog.show();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                }, 1000);
            }
        });
        //开关切换事件
        toggleButton.setOnToggleChanged(new WToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                presenter.start();
                showToast("夜间模式" + on);
            }
        });
    }

    @Override
    public void _initTheme() {
        if (presenter.isDay()) {
            setTheme(R.style.DayTheme);
        } else {
            setTheme(R.style.NightTheme);
        }
    }

    /**
     * 使用知乎的实现套路来切换夜间主题
     */
    @Override
    public void changeThemeByZhiHu() {
        if (presenter.isDay()) {
            initNightModel();
        } else {
            initDayModel();
        }
    }

    /**
     * 展示一个切换动画
     */
    @Override
    public void showAnimation() {
        final View decorView = getWindow().getDecorView();
        Bitmap cacheBitmap = getCacheBitmapFromView(decorView);
        if (decorView instanceof ViewGroup && cacheBitmap != null) {
            final View view = new View(this);
            view.setBackgroundDrawable(new BitmapDrawable(getResources(), cacheBitmap));
            ViewGroup.LayoutParams layoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) decorView).addView(view, layoutParam);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            objectAnimator.setDuration(300);
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ((ViewGroup) decorView).removeView(view);
                }
            });
            objectAnimator.start();
        }
    }

    /**
     * 切换主题设置白天模式
     */
    @Override
    public void initDayModel() {
        presenter.setDayModel();
        setTheme(R.style.DayTheme);
    }

    /**
     * 切换主题设置夜晚模式
     */
    @Override
    public void initNightModel() {
        presenter.setNightModel();
        setTheme(R.style.NightTheme);
    }

    /**
     * 刷新UI界面
     */
    @Override
    public void refreshUI() {
        TypedValue background = new TypedValue();//背景色
        TypedValue textColor = new TypedValue();//字体颜色
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.anBackground, background, true);
        theme.resolveAttribute(R.attr.anTextColor, textColor, true);

        relativeLayout.setBackgroundResource(background.resourceId);
        linearLayout.setBackgroundResource(background.resourceId);
        for (RelativeLayout layout : mRelativeList) {
            layout.setBackgroundResource(background.resourceId);
        }
        for (LinearLayout layout : mLayoutList) {
            layout.setBackgroundResource(background.resourceId);
        }
        for (Button button : mButtonList) {
            button.setBackgroundResource(background.resourceId);
        }
        for (TextView textView : mTextViewList) {
            textView.setBackgroundResource(background.resourceId);
        }

        Resources resources = getResources();
        for (TextView textView : mTextViewList) {
            textView.setTextColor(resources.getColor(textColor.resourceId));
        }
        for (Button button : mButtonList) {
            button.setTextColor(resources.getColor(textColor.resourceId));
        }
        refreshStatusBar();
    }

    /**
     * 刷新 StatusBar
     */

    private void refreshStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            getWindow().setStatusBarColor(getResources().getColor(typedValue.resourceId));
        }
    }


    /**
     * 获取一个 View 的缓存视图
     *
     * @param view
     * @return
     */

    private Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dialog.dismiss();
            Intent intent = new Intent(MainActivity.this, TestActivity.class);
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

    @Override
    public void setPresenter(TaskDayNightContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }
}
