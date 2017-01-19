package com.qyddai.an_aw_base.view.activity;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.an.base.view.SuperActivity;
import com.an.base.view.refreshlayout.BGARefreshLayout;
import com.an.base.view.refreshlayout.BGAStickinessRefreshViewHolder;
import com.qyddai.an_aw_base.R;
import com.qyddai.an_aw_base.view.DataEngine;
import com.qyddai.an_aw_base.view.Engine;


/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午1:22
 * 描述:
 */
public class RefreshNormalViewActivity extends SuperActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private static final String TAG = RefreshNormalViewActivity.class.getSimpleName();
    private BGARefreshLayout mRefreshLayout;
    private TextView mClickableLabelTv;

    private Engine mEngine;

    @Override
    public void initView() {
        setContentView(R.layout.sst_activity_refreshlayout_normalview);
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_normalview_refresh);
        mClickableLabelTv = (TextView) findViewById(R.id.tv_normalview_clickablelabel);
        mRefreshLayout.setDelegate(this);

        mClickableLabelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("点击了测试文本");
            }
        });

        BGAStickinessRefreshViewHolder stickinessRefreshViewHolder = new BGAStickinessRefreshViewHolder(mContext, true);
        stickinessRefreshViewHolder.setStickinessColor(R.color.colorPrimary);
        stickinessRefreshViewHolder.setRotateImage(R.mipmap.bga_refresh_stickiness);
        mRefreshLayout.setRefreshViewHolder(stickinessRefreshViewHolder);

        mRefreshLayout.setCustomHeaderView(DataEngine.getCustomHeaderView(mContext), false);
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mRefreshLayout.endRefreshing();
                mClickableLabelTv.setText("加载最新数据完成");
            }
        }.execute();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mRefreshLayout.endLoadingMore();
                Log.i(TAG, "上拉加载更多完成");
            }
        }.execute();
        return true;
    }
}