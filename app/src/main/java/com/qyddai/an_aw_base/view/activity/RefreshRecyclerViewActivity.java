package com.qyddai.an_aw_base.view.activity;

import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.an.base.AnApplication;
import com.an.base.view.SuperActivity;
import com.an.base.view.refreshlayout.BGARefreshLayout;
import com.an.base.view.refreshlayout.BGAStickinessRefreshViewHolder;
import com.qyddai.an_aw_base.R;
import com.qyddai.an_aw_base.model.adapter.NormalRecyclerViewAdapter;
import com.qyddai.an_aw_base.model.entity.RefreshModel;
import com.qyddai.an_aw_base.utils.ListViewDecoration;
import com.qyddai.an_aw_base.utils.ThreadUtil;
import com.qyddai.an_aw_base.view.Engine;

import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildLongClickListener;
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
public class RefreshRecyclerViewActivity extends SuperActivity implements BGARefreshLayout.BGARefreshLayoutDelegate, BGAOnRVItemClickListener, BGAOnRVItemLongClickListener, BGAOnItemChildClickListener, BGAOnItemChildLongClickListener {
    private static final String TAG = RefreshRecyclerViewActivity.class.getSimpleName();
    private NormalRecyclerViewAdapter mAdapter;
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

        mAdapter = new NormalRecyclerViewAdapter(mDataRv);
        mAdapter.setOnRVItemClickListener(this);
        mAdapter.setOnRVItemLongClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemChildLongClickListener(this);

        // 使用addOnScrollListener，而不是setOnScrollListener();
        mDataRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                Log.i(TAG, "测试自定义onScrollStateChanged被调用");
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.i(TAG, "测试自定义onScrolled被调用");
            }
        });
        //        mRefreshLayout.setCustomHeaderView(DataEngine.getCustomHeaderView(mApp), true);

        View headerView = View.inflate(AnApplication.getInstance(), R.layout.yy_view_simple_header, null);

        // 测试自定义header中控件的点击事件
        headerView.findViewById(R.id.btn_custom_header2_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("点击了测试按钮");
            }
        });
        // 模拟网络数据加载，测试动态改变自定义header的高度
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.tv_custom_header2_title)).setText(R.string.test_custom_header_title);
                ((TextView) findViewById(R.id.tv_custom_header2_desc)).setText(R.string.test_custom_header_desc);
            }
        }, 2000);
        mEngine = new Retrofit.Builder()
                .baseUrl("http://7xk9dj.com1.z0.glb.clouddn.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Engine.class);
//        mRefreshLayout.setCustomHeaderView(headerView, true);
        mAdapter.addHeaderView(headerView);

        BGAStickinessRefreshViewHolder stickinessRefreshViewHolder = new BGAStickinessRefreshViewHolder(AnApplication.getInstance(), true);
        stickinessRefreshViewHolder.setStickinessColor(R.color.colorPrimary);
        stickinessRefreshViewHolder.setRotateImage(R.mipmap.bga_refresh_stickiness);
        mRefreshLayout.setRefreshViewHolder(stickinessRefreshViewHolder);

        mDataRv.addItemDecoration(new ListViewDecoration());

        mDataRv.setLayoutManager(new GridLayoutManager(AnApplication.getInstance(), 2, GridLayoutManager.VERTICAL, false));
//        mDataRv.setLayoutManager(new LinearLayoutManager(mApp, LinearLayoutManager.VERTICAL, false));

        mDataRv.setAdapter(mAdapter.getHeaderAndFooterAdapter());
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

//        showLoadingDialog();
        mEngine.loadNewData(mNewPageNumber).enqueue(new Callback<List<RefreshModel>>() {
            @Override
            public void onResponse(Call<List<RefreshModel>> call, final Response<List<RefreshModel>> response) {
                ThreadUtil.runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.endRefreshing();
//                        dismissLoadingDialog();
                        mAdapter.addNewData(response.body());
                        mDataRv.smoothScrollToPosition(0);
                    }
                }, 3000);
            }

            @Override
            public void onFailure(Call<List<RefreshModel>> call, Throwable t) {
                mRefreshLayout.endRefreshing();
//                dismissLoadingDialog();
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

//        showLoadingDialog();
        mEngine.loadMoreData(mMorePageNumber).enqueue(new Callback<List<RefreshModel>>() {
            @Override
            public void onResponse(Call<List<RefreshModel>> call, final Response<List<RefreshModel>> response) {
                ThreadUtil.runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.endLoadingMore();
//                        dismissLoadingDialog();
                        mAdapter.addMoreData(response.body());
                    }
                }, 3000);
            }

            @Override
            public void onFailure(Call<List<RefreshModel>> call, Throwable t) {
                mRefreshLayout.endLoadingMore();
//                dismissLoadingDialog();
            }
        });

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
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        showToast("点击了条目 " + mAdapter.getItem(position).title);
    }

    @Override
    public boolean onRVItemLongClick(ViewGroup parent, View itemView, int position) {
        showToast("长按了条目 " + mAdapter.getItem(position).title);
        return true;
    }
}