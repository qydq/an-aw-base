package com.an.base.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.an.base.R;

/**********************************************************
 * @文件名称：DUtilsDialog
 * @文件作者：staryumou@163.com
 * @创建时间：2016/9/23
 * @文件描述：自定义的an框架dialog createdialog hui返回一个Dialog对象,单例枚举参考
 * @修改历史：2016/9/23
 **********************************************************/
public enum DUtilsDialog {
    INSTANCE;

    private AnimationDrawable animationDrawable;
    private Dialog dUtilsDialog;

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @param msg     创建一个对话框，msg是提示语，isCancle是否可以取消对话框
     * @return Dialog
     */
    public Dialog createDialog(Context context, String msg, boolean isCancle) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.base_standard_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.anLlHeadView);// 加载布局
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.anDialogIv);
        TextView tipTextView = (TextView) v.findViewById(R.id.anDialogTv);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.base_anim_dialog_loading);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息
        Dialog loadingDialog = new Dialog(context, R.style.AnDialog);
        loadingDialog.setCancelable(isCancle);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return loadingDialog;
    }

    /**
     * @param context
     * @param thems    一般传入${R.style.AnProgressDialog}
     * @param tips     对话框需要的内容提示
     * @param isCancle 是否可以取消
     * @return
     */
    public ProgressDialog showProgressDialog(Context context, int thems, String tips, boolean isCancle) {
        ProgressDialog progressDialog = new ProgressDialog(context, thems);
        progressDialog.setCancelable(isCancle);
        progressDialog.setCanceledOnTouchOutside(isCancle);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.base_standard_dialog_progress, null);
        TextView anProgressTv = (TextView) v.findViewById(R.id.anProgressTv);
        anProgressTv.setText(tips);
        progressDialog.show();
        progressDialog.setContentView(R.layout.base_standard_dialog_progress);
        Window window = progressDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        return progressDialog;
    }

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @param isCancle 创建一个对话框，msg是提示语，isCancle是否可以取消对话框
     * @return Dialog ?android:attr/progressBarStyleLarge,AnSimpleDialog
     */
    public Dialog createSimpleDialog(Context context, boolean isCancle, String tips) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.base_standard_simple_dialog, null);// 得到加载view
        ImageView imageView = (ImageView) layout.findViewById(R.id.anDialogIv);
        TextView tipTextView = (TextView) layout.findViewById(R.id.anDialogTv);// 提示文字
        if (TextUtils.isEmpty(tips)) {
            tipTextView.setText(context.getString(R.string.listview_loading));
        } else {
            tipTextView.setText(tips);
        }
        dUtilsDialog = new Dialog(context, R.style.AnSimpleDialog);
        dUtilsDialog.setCancelable(isCancle);
        dUtilsDialog.setContentView(layout);
        dUtilsDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        animationDrawable = (AnimationDrawable) imageView.getBackground();
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }
        return dUtilsDialog;
    }

    public void dismissSimpleDialog() {
        // TODO Auto-generated method stub
        if (animationDrawable != null && animationDrawable.isRunning())
            animationDrawable.stop();
        if (dUtilsDialog != null)
            dUtilsDialog.dismiss();
    }

}
