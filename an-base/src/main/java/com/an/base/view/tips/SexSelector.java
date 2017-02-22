package com.an.base.view.tips;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.an.base.R;
import com.an.base.utils.DScreenUtil;
import com.an.base.view.widget.PickerSimpleView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by qydq on 2015/11/27. 後期會提供 同公用的SimpleSelector給大家使用 孫順濤
 */
public class SexSelector {

    private List<String> datas = new ArrayList<>();

    public interface ResultHandler {
        void handle(String sex);
    }

    private ResultHandler handler;
    private Context context;

    private Dialog seletorDialog;
    private PickerSimpleView content;


    private String workStart_str;
    private TextView tv_cancle;
    private TextView tv_select;
    private TextView tv_title;

    public SexSelector(Context context, ResultHandler resultHandler, List<String> datas) {
        this.context = context;
        this.handler = resultHandler;
        this.datas = datas;
        initDialog();
        initView();
    }

    public void show() {
        initTimer();
        addListener();
        seletorDialog.show();
    }

    private void initDialog() {
        if (seletorDialog == null) {
            seletorDialog = new Dialog(context, R.style.time_dialog);
            seletorDialog.setCancelable(false);
            seletorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            seletorDialog.setContentView(R.layout.base_pickerview_sex_selector);
            Window window = seletorDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
            int width = DScreenUtil.getInstance(context).getScreenWidth();
            lp.width = width;
            window.setAttributes(lp);
        }
    }

    private void initView() {
        content = (PickerSimpleView) seletorDialog.findViewById(R.id.year_pv);
        tv_cancle = (TextView) seletorDialog.findViewById(R.id.tv_cancle);
        tv_select = (TextView) seletorDialog.findViewById(R.id.tv_select);
        tv_title = (TextView) seletorDialog.findViewById(R.id.tv_title);

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seletorDialog.dismiss();
            }
        });
        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.handle(txt);
                seletorDialog.dismiss();
                txt = "男";//初始化
            }
        });

    }

    private void initTimer() {
        initArrayList();
        loadComponent();
    }

    private void initArrayList() {
        if (datas == null) datas = new ArrayList<>();
        datas.clear();
    }

    private String txt = "男";

    private void addListener() {
        content.setOnSelectListener(new PickerSimpleView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                txt = text;
            }
        });
    }

    private void loadComponent() {
        datas = new ArrayList<>();
        datas.add("男");
        datas.add("女");
        datas.add("保密");
        content.setData(datas);
        content.setSelected(0);
        excuteScroll();
    }

    private void excuteScroll() {
        content.setCanScroll(datas.size() > 1);
    }

    public void setTitle(String str) {
        tv_title.setText(str);
    }
}
