package com.qyddai.an_aw_base.view.activity;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.an.base.view.SuperActivity;
import com.an.base.view.refreshlayout.BGAMoocStyleRefreshViewHolder;
import com.an.base.view.refreshlayout.BGARefreshLayout;
import com.qyddai.an_aw_base.R;
import com.qyddai.an_aw_base.view.Engine;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/7/21 下午11:42
 * 描述:
 */
public class RefreshWebViewActivity extends SuperActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private static final String TAG = RefreshWebViewActivity.class.getSimpleName();
    private BGARefreshLayout mRefreshLayout;
    private WebView mContentWv;
    private Engine mEngine;

    @Override
    public void initView() {
        setContentView(R.layout.sst_activity_refreshlayout_webview);
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_webview_refresh);
        mContentWv = (WebView) findViewById(R.id.wv_webview_content);
        mRefreshLayout.setDelegate(this);
        mContentWv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mRefreshLayout.endRefreshing();
            }
        });
        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(mContext, false);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.bga_refresh_moooc);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.colorPrimary);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);
//        mRefreshLayout.setCustomHeaderView(DataEngine.getCustomHeaderView(mApp), true);
        mContentWv.getSettings().setJavaScriptEnabled(true);
        mContentWv.loadUrl("https://github.com/qydq");
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mContentWv.reload();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        Log.i(TAG, "加载更多");
        return false;
    }
}