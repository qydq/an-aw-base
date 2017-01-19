package com.qyddai.an_aw_base.view.activity;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.an.base.view.SuperActivity;
import com.an.base.view.refreshlayout.BGANormalRefreshViewHolder;
import com.an.base.view.refreshlayout.BGARefreshLayout;
import com.qyddai.an_aw_base.R;
import com.qyddai.an_aw_base.model.adapter.StaggeredRecyclerViewAdapter;
import com.qyddai.an_aw_base.model.entity.StaggeredModel;
import com.qyddai.an_aw_base.utils.ThreadUtil;
import com.qyddai.an_aw_base.view.DataEngine;
import com.qyddai.an_aw_base.view.Engine;

import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemLongClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/22 10:06
 * 描述:
 */
public class RefreshStaggeredRecyclerViewActivity extends SuperActivity implements BGARefreshLayout.BGARefreshLayoutDelegate, BGAOnRVItemClickListener, BGAOnRVItemLongClickListener {
    private static final String TAG = RefreshStaggeredRecyclerViewActivity.class.getSimpleName();
    private StaggeredRecyclerViewAdapter mAdapter;
    private BGARefreshLayout mRefreshLayout;
    private RecyclerView mDataRv;
    private int mNewPageNumber = 0;
    private int mMorePageNumber = 0;
    private Engine mEngine;

    @Override
    public void initView() {
        setContentView(R.layout.sst_activity_refreshlayout_recyclerview);
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_recyclerview_refresh);
        mDataRv = (RecyclerView) findViewById(R.id.rv_recyclerview_data);

        mRefreshLayout.setDelegate(this);

        mAdapter = new StaggeredRecyclerViewAdapter(mDataRv);
        mAdapter.setOnRVItemClickListener(this);
        mAdapter.setOnRVItemLongClickListener(this);

        mRefreshLayout.setCustomHeaderView(DataEngine.getCustomHeaderView(mContext), true);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, true));

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mDataRv.setLayoutManager(layoutManager);

        mDataRv.setAdapter(mAdapter);

        mEngine = new Retrofit.Builder()
                .baseUrl("http://7xk9dj.com1.z0.glb.clouddn.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Engine.class);
        onLazyLoadOnce();
    }

    protected void onLazyLoadOnce() {
        mNewPageNumber = 0;
        mMorePageNumber = 0;
        mEngine.loadDefaultStaggeredData().enqueue(new Callback<List<StaggeredModel>>() {
            @Override
            public void onResponse(Call<List<StaggeredModel>> call, Response<List<StaggeredModel>> response) {
                mAdapter.setData(response.body());
            }

            @Override
            public void onFailure(Call<List<StaggeredModel>> call, Throwable t) {
            }
        });
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mNewPageNumber++;
        if (mNewPageNumber > 4) {
            mRefreshLayout.endRefreshing();
            showToast("没有最新数据了");
            return;
        }
        mEngine.loadNewStaggeredData(mNewPageNumber).enqueue(new Callback<List<StaggeredModel>>() {
            @Override
            public void onResponse(Call<List<StaggeredModel>> call, final Response<List<StaggeredModel>> response) {
                ThreadUtil.runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.endRefreshing();
                        mAdapter.addNewData(response.body());
                        mDataRv.smoothScrollToPosition(0);
                    }
                }, 3000);
            }

            @Override
            public void onFailure(Call<List<StaggeredModel>> call, Throwable t) {
                mRefreshLayout.endRefreshing();
            }
        });
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        mMorePageNumber++;
        if (mMorePageNumber > 5) {
            mRefreshLayout.endLoadingMore();
            showToast("没有更多数据了");
            return false;
        }
        mEngine.loadMoreStaggeredData(mMorePageNumber).enqueue(new Callback<List<StaggeredModel>>() {
            @Override
            public void onResponse(Call<List<StaggeredModel>> call, final Response<List<StaggeredModel>> response) {
                ThreadUtil.runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.endLoadingMore();
                        mAdapter.addMoreData(response.body());
                    }
                }, 3000);
            }

            @Override
            public void onFailure(Call<List<StaggeredModel>> call, Throwable t) {
                mRefreshLayout.endLoadingMore();
            }
        });
        return true;
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        showToast("点击了条目 " + mAdapter.getItem(position).desc);
    }

    @Override
    public boolean onRVItemLongClick(ViewGroup parent, View itemView, int position) {
        showToast("长按了条目 " + mAdapter.getItem(position).desc);
        return true;
    }
}