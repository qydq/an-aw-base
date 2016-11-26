package com.an.base.view;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by stary on 2016/8/18.
 * 文件名称：SuperFragment
 * 文件描述：
 * 作者：staryumou@163.com
 * 修改时间：2016/8/18
 */

public abstract class SuperFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void showToast(final String msg) {
        this.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    public void reload() {
    }

    protected void runOnUiThread(Runnable r) {
        this.getActivity().runOnUiThread(r);
    }
}
