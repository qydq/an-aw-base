package com.qyddai.an_aw_base.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.an.base.utils.NetBroadcastReceiverUtils;
import com.an.base.view.recyclerview.ItemDecoration.DividerDecoration;
import com.an.base.view.recyclerview.interfaces.OnItemClickListener;
import com.an.base.view.recyclerview.interfaces.OnItemLongClickListener;
import com.an.base.view.recyclerview.interfaces.OnLoadMoreListener;
import com.an.base.view.recyclerview.interfaces.OnNetWorkErrorListener;
import com.an.base.view.recyclerview.interfaces.OnRefreshListener;
import com.an.base.view.recyclerview.recyclerview.LRecyclerView;
import com.an.base.view.recyclerview.recyclerview.LRecyclerViewAdapter;
import com.an.base.view.recyclerview.recyclerview.ProgressStyle;
import com.qyddai.an_aw_base.R;
import com.qyddai.an_aw_base.model.adapter.MultipleItemAdapter;
import com.qyddai.an_aw_base.model.entity.MultipleItem;
import com.qyddai.an_aw_base.utils.SampleHeader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 带HeaderView的分页加载LinearLayout RecyclerView
 */
public class MulItemLinearLayoutActivity extends AppCompatActivity {
    private static final String TAG = "lzx";

    /**
     * 服务器端一共多少条数据
     */
    private static final int TOTAL_COUNTER = 64;

    /**
     * 每一页展示多少条数据
     */
    private static final int REQUEST_COUNT = 10;

    /**
     * 已经获取到多少条数据了
     */
    private static int mCurrentCounter = 0;

    private LRecyclerView mRecyclerView = null;

    private MultipleItemAdapter mMultipleItemAdapter = null;

    private PreviewHandler mHandler = new PreviewHandler(this);
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sst_activity_recyclerview_endless);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (LRecyclerView) findViewById(R.id.list);

        mMultipleItemAdapter = new MultipleItemAdapter(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mMultipleItemAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.split)
                .build();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(divider);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.mipmap.base_refresh_head_arrow);

        mLRecyclerViewAdapter.addHeaderView(new SampleHeader(this));

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMultipleItemAdapter.clear();
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                requestData();
            }
        });

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mCurrentCounter < TOTAL_COUNTER) {
                    // loading more
                    requestData();
                } else {
                    //the end
                    mRecyclerView.setNoMore(true);
                }
            }
        });

        mRecyclerView.setLScrollListener(new LRecyclerView.LScrollListener() {

            @Override
            public void onScrollUp() {
            }

            @Override
            public void onScrollDown() {
            }


            @Override
            public void onScrolled(int distanceX, int distanceY) {
            }

            @Override
            public void onScrollStateChanged(int state) {

            }

        });


        mRecyclerView.setRefreshing(true);

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MultipleItem item = mMultipleItemAdapter.getDataList().get(position);
                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        mLRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                MultipleItem item = mMultipleItemAdapter.getDataList().get(position);
                Toast.makeText(getApplicationContext(), "onItemLongClick - " + item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<MultipleItem> list) {

        mMultipleItemAdapter.addAll(list);
        mCurrentCounter += list.size();

    }

    private class PreviewHandler extends Handler {

        private WeakReference<MulItemLinearLayoutActivity> ref;

        PreviewHandler(MulItemLinearLayoutActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final MulItemLinearLayoutActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {

                case -1:
                    if (activity.mRecyclerView.isPulldownToRefresh()) {
                        activity.mMultipleItemAdapter.clear();
                        mCurrentCounter = 0;
                    }

                    int currentSize = activity.mMultipleItemAdapter.getItemCount();

                    //模拟组装10个数据
                    ArrayList<MultipleItem> newList = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        if (newList.size() + currentSize >= TOTAL_COUNTER) {
                            break;
                        }

                        MultipleItem item;
                        if (i % 2 == 0) {
                            item = new MultipleItem(MultipleItem.IMG);
                        } else {
                            item = new MultipleItem(MultipleItem.TEXT);
                        }
                        item.setTitle("item" + (currentSize + i));

                        newList.add(item);
                    }

                    activity.addItems(newList);

                    if (activity.mRecyclerView.isPulldownToRefresh()) {
                        activity.mRecyclerView.refreshComplete();
                        activity.notifyDataSetChanged();
                    } else {
                        activity.mRecyclerView.loadMoreComplete();
                    }
                    break;
                case -2:
                    activity.notifyDataSetChanged();
                    break;
                case -3:
                    if (activity.mRecyclerView.isPulldownToRefresh()) {
                        activity.mRecyclerView.refreshComplete();
                        activity.notifyDataSetChanged();
                    } else {
                        activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                requestData();
                            }
                        });
                    }
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 模拟请求网络
     */
    private void requestData() {
        Log.d(TAG, "requestData");
        new Thread() {

            @Override
            public void run() {
                super.run();

                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //模拟一下网络请求失败的情况
                if (NetBroadcastReceiverUtils.isConnectedToInternet(MulItemLinearLayoutActivity.this)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recyclerview_main_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_refresh) {
            mRecyclerView.forceToRefresh();
        }
        return true;
    }
}