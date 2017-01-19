package com.qyddai.an_aw_base.view.activity;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.an.base.AnApplication;
import com.an.base.view.SuperActivity;
import com.an.base.view.refreshlayout.BGAMoocStyleRefreshViewHolder;
import com.an.base.view.refreshlayout.BGARefreshLayout;
import com.qyddai.an_aw_base.R;
import com.qyddai.an_aw_base.model.adapter.NormalAdapterViewAdapter;
import com.qyddai.an_aw_base.model.entity.RefreshModel;
import com.qyddai.an_aw_base.utils.ThreadUtil;
import com.qyddai.an_aw_base.view.Engine;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildLongClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by qydda on 2017/1/18.
 */

public class RefreshGridViewActivity extends SuperActivity implements BGARefreshLayout.BGARefreshLayoutDelegate, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, BGAOnItemChildClickListener, BGAOnItemChildLongClickListener, View.OnClickListener {
    @ViewInject(R.id.anLlBack)
    private LinearLayout anLlBack;

    private BGARefreshLayout mRefreshLayout;
    private GridView mDataGv;
    private NormalAdapterViewAdapter mAdapter;
    private int mNewPageNumber = 0;
    private int mMorePageNumber = 0;

    private boolean mIsNetworkEnabled = false;

    private Engine mEngine;

    @Override
    public void initView() {
        setContentView(R.layout.sst_activity_refreshlayout_gridview);
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_gridview_refresh);
        mDataGv = (GridView) findViewById(R.id.lv_gridview_data);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshScaleDelegate(new BGARefreshLayout.BGARefreshScaleDelegate() {
            @Override
            public void onRefreshScaleChanged(float scale, int moveYDistance) {
                Log.i(TAG, "scale:" + scale + " moveYDistance:" + moveYDistance);
            }
        });

        mDataGv.setOnItemClickListener(this);
        mDataGv.setOnItemLongClickListener(this);
        mDataGv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.i(TAG, "滚动状态变化");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.i(TAG, "正在滚动");
            }
        });

        mAdapter = new NormalAdapterViewAdapter(AnApplication.getInstance());
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemChildLongClickListener(this);
//        anLlBack = (LinearLayout) findViewById(R.id.anLlBack);
        anLlBack.setOnClickListener(this);

        findViewById(R.id.beginRefreshing).setOnClickListener(this);
        findViewById(R.id.beginLoadingMore).setOnClickListener(this);

        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(AnApplication.getInstance(), true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.bga_refresh_moooc);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.ColorRed);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        mDataGv.setAdapter(mAdapter);
        mEngine = new Retrofit.Builder()
                .baseUrl("http://7xk9dj.com1.z0.glb.clouddn.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Engine.class);
        onLazyLoadOnce();
    }

    protected void onLazyLoadOnce() {
        mNewPageNumber = 0;
        mMorePageNumber = 0;
        mEngine.loadInitDatas().enqueue(new Callback<List<RefreshModel>>() {
            @Override
            public void onResponse(Call<List<RefreshModel>> call, Response<List<RefreshModel>> response) {
                mAdapter.setData(response.body());
            }

            @Override
            public void onFailure(Call<List<RefreshModel>> call, Throwable t) {
            }
        });
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(final BGARefreshLayout refreshLayout) {
        if (mIsNetworkEnabled) {
            // 如果网络可用，则加载网络数据

            mNewPageNumber++;
            if (mNewPageNumber > 4) {
                mRefreshLayout.endRefreshing();
                showToast("没有最新数据了");
                return;
            }
            mEngine.loadNewData(mNewPageNumber).enqueue(new Callback<List<RefreshModel>>() {
                @Override
                public void onResponse(Call<List<RefreshModel>> call, final Response<List<RefreshModel>> response) {
                    // 测试数据放在七牛云上的比较快，这里加载完数据后模拟延时查看动画效果
                    ThreadUtil.runInUIThread(new Runnable() {
                        @Override
                        public void run() {
                            mRefreshLayout.endRefreshing();
                            mAdapter.addNewData(response.body());
                        }
                    }, 3000);
                }

                @Override
                public void onFailure(Call<List<RefreshModel>> call, Throwable t) {
                    mRefreshLayout.endRefreshing();
                }
            });
        } else {
            // 网络不可用，结束下拉刷新
            showToast("网络不可用");
            mRefreshLayout.endRefreshing();
        }
        // 模拟网络可用不可用
        mIsNetworkEnabled = !mIsNetworkEnabled;
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        Log.i(TAG, "开始加载更多");

        if (mIsNetworkEnabled) {
            // 如果网络可用，则异步加载网络数据，并返回true，显示正在加载更多
            mMorePageNumber++;
            if (mMorePageNumber > 4) {
                mRefreshLayout.endLoadingMore();
                showToast("没有更多数据了");
                return false;
            }
            mEngine.loadMoreData(mMorePageNumber).enqueue(new Callback<List<RefreshModel>>() {
                @Override
                public void onResponse(Call<List<RefreshModel>> call, final Response<List<RefreshModel>> response) {
                    // 测试数据放在七牛云上的比较快，这里加载完数据后模拟延时查看动画效果
                    ThreadUtil.runInUIThread(new Runnable() {
                        @Override
                        public void run() {
                            mRefreshLayout.endLoadingMore();
                            mAdapter.addMoreData(response.body());
                        }
                    }, 3000);
                }

                @Override
                public void onFailure(Call<List<RefreshModel>> call, Throwable t) {
                    mRefreshLayout.endLoadingMore();
                }
            });
            // 模拟网络可用不可用
            mIsNetworkEnabled = !mIsNetworkEnabled;
            return true;
        } else {
            // 模拟网络可用不可用
            mIsNetworkEnabled = !mIsNetworkEnabled;

            // 网络不可用，返回false，不显示正在加载更多
            showToast("网络不可用");
            return false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showToast("点击了条目 " + mAdapter.getItem(position).title);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showToast("长按了" + mAdapter.getItem(position).title);
        return true;
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        if (childView.getId() == R.id.tv_item_normal_delete) {
            mAdapter.removeItem(position);
        }
    }

    @Override
    public boolean onItemChildLongClick(ViewGroup parent, View childView, int position) {
        if (childView.getId() == R.id.tv_item_normal_delete) {
            showToast("长按了删除 " + mAdapter.getItem(position).title);
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.beginRefreshing) {
            mRefreshLayout.beginRefreshing();
        } else if (v.getId() == R.id.beginLoadingMore) {
            mRefreshLayout.beginLoadingMore();
        } else if (v.getId() == R.id.anLlBack) {
            finish();
        }
    }

}
