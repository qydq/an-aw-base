package com.qyddai.an_aw_base.view.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.an.base.AnApplication;
import com.an.base.view.SuperActivity;
import com.an.base.view.refreshlayout.BGAMeiTuanRefreshViewHolder;
import com.an.base.view.refreshlayout.BGARefreshLayout;
import com.qyddai.an_aw_base.R;
import com.qyddai.an_aw_base.model.adapter.SwipeAdapterViewAdapter;
import com.qyddai.an_aw_base.model.entity.RefreshModel;
import com.qyddai.an_aw_base.utils.ThreadUtil;
import com.qyddai.an_aw_base.view.DataEngine;
import com.qyddai.an_aw_base.view.Engine;

import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildLongClickListener;
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
public class RefreshSwipeListViewActivity extends SuperActivity implements BGARefreshLayout.BGARefreshLayoutDelegate, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, BGAOnItemChildClickListener, BGAOnItemChildLongClickListener {
    private static final String TAG = RefreshSwipeListViewActivity.class.getSimpleName();
    private BGARefreshLayout mRefreshLayout;
    private ListView mDataLv;
    private SwipeAdapterViewAdapter mAdapter;
    private int mNewPageNumber = 0;
    private int mMorePageNumber = 0;

    private Engine mEngine;

    @Override
    public void initView() {
        setContentView(R.layout.sst_activity_refreshlayout_listview);
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_listview_refresh);
        mDataLv = (ListView) findViewById(R.id.lv_listview_data);

        mRefreshLayout.setDelegate(this);

        mDataLv.setOnItemClickListener(this);
        mDataLv.setOnItemLongClickListener(this);
        mDataLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL == scrollState) {
                    mAdapter.closeOpenedSwipeItemLayoutWithAnim();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        mAdapter = new SwipeAdapterViewAdapter(AnApplication.getInstance());
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemChildLongClickListener(this);

        BGAMeiTuanRefreshViewHolder meiTuanRefreshViewHolder = new BGAMeiTuanRefreshViewHolder(AnApplication.getInstance(), true);
        meiTuanRefreshViewHolder.setPullDownImageResource(R.mipmap.bga_refresh_mt_pull_down);
        meiTuanRefreshViewHolder.setChangeToReleaseRefreshAnimResId(R.drawable.bga_refresh_mt_change_to_release_refresh);
        meiTuanRefreshViewHolder.setRefreshingAnimResId(R.drawable.bga_refresh_mt_refreshing);

        mRefreshLayout.setRefreshViewHolder(meiTuanRefreshViewHolder);
        mRefreshLayout.setCustomHeaderView(DataEngine.getCustomHeaderView(AnApplication.getInstance()), true);

        mDataLv.setAdapter(mAdapter);

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
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mNewPageNumber++;
        if (mNewPageNumber > 4) {
            mRefreshLayout.endRefreshing();
            showToast("没有最新数据了");
            return;
        }
        mEngine.loadNewData(mNewPageNumber).enqueue(new Callback<List<RefreshModel>>() {
            @Override
            public void onResponse(Call<List<RefreshModel>> call, final Response<List<RefreshModel>> response) {
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
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        mMorePageNumber++;
        if (mMorePageNumber > 4) {
            mRefreshLayout.endLoadingMore();
            showToast("没有更多数据了");
            return false;
        }
        mEngine.loadMoreData(mMorePageNumber).enqueue(new Callback<List<RefreshModel>>() {
            @Override
            public void onResponse(Call<List<RefreshModel>> call, final Response<List<RefreshModel>> response) {
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
        return true;
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
        if (childView.getId() == R.id.tv_item_swipe_delete) {
            // 作为ListView的item使用时，如果删除了某一个item，请先关闭已经打开的item，否则其他item会显示不正常（RecyclerView不会有这个问题）
            mAdapter.closeOpenedSwipeItemLayout();
            mAdapter.removeItem(position);
        }
    }

    @Override
    public boolean onItemChildLongClick(ViewGroup parent, View childView, int position) {
        if (childView.getId() == R.id.tv_item_swipe_delete) {
            showToast("长按了删除 " + mAdapter.getItem(position).title);
            return true;
        }
        return false;
    }
}