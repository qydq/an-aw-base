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


import com.an.base.utils.NetBroadcastReceiverUtils;
import com.an.base.view.recyclerview.ExpandableRecyclerAdapter;
import com.an.base.view.recyclerview.interfaces.OnNetWorkErrorListener;
import com.an.base.view.recyclerview.interfaces.OnRefreshListener;
import com.an.base.view.recyclerview.recyclerview.LRecyclerView;
import com.an.base.view.recyclerview.recyclerview.LRecyclerViewAdapter;
import com.an.base.view.recyclerview.recyclerview.ProgressStyle;
import com.qyddai.an_aw_base.R;
import com.qyddai.an_aw_base.model.adapter.CommentExpandAdapter;
import com.qyddai.an_aw_base.model.entity.CommentItem;
import com.qyddai.an_aw_base.model.entity.ItemModel;
import com.qyddai.an_aw_base.utils.SampleHeader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 带HeaderView的分页加载LinearLayout RecyclerView
 */
public class ExpandableRecyclerViewOneActivity extends AppCompatActivity {
    private static final String TAG = "lzx";

    /**
     * 服务器端一共多少条数据
     */
    private static final int TOTAL_COUNTER = 24;

    /**
     * 每一页展示多少条数据
     */
    private static final int REQUEST_COUNT = 10;

    /**
     * 已经获取到多少条数据了
     */
    private static int mCurrentCounter = 0;

    private LRecyclerView mRecyclerView = null;

    private CommentExpandAdapter mDataAdapter = null;

    private PreviewHandler mHandler = new PreviewHandler(this);
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private boolean isRefresh = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sst_activity_recyclerview_endless);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (LRecyclerView) findViewById(R.id.list);


        mDataAdapter = new CommentExpandAdapter(this, mRecyclerView);
        mDataAdapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.mipmap.base_refresh_head_arrow);

        mLRecyclerViewAdapter.addHeaderView(new SampleHeader(this));

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentCounter = 0;
                isRefresh = true;
                requestData();
            }
        });

        mRecyclerView.refresh();

        //不要在调用下面代码
        /*mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //ItemModel item = mDataAdapter.getDataList().get(position);
                //AppToast.showShortText(ExpandableRecyclerViewOneActivity.this, item.title);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //ItemModel item = mDataAdapter.getDataList().get(position);
                //AppToast.showShortText(ExpandableRecyclerViewOneActivity.this, "onItemLongClick - " + item.title);
            }
        });*/

    }

    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<CommentItem> list) {

        mDataAdapter.setItems(list);
        mCurrentCounter += list.size();
    }

    private class PreviewHandler extends Handler {

        private WeakReference<ExpandableRecyclerViewOneActivity> ref;

        PreviewHandler(ExpandableRecyclerViewOneActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final ExpandableRecyclerViewOneActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {

                case -1:
                    if (activity.isRefresh) {
                        //activity.mDataAdapter.clear();
                        mCurrentCounter = 0;
                    }

                    int currentSize = activity.mDataAdapter.getItemCount();

                    //模拟组装10个数据
                    ArrayList<ItemModel> newList = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        if (newList.size() + currentSize >= TOTAL_COUNTER) {
                            break;
                        }

                        ItemModel item = new ItemModel();
                        item.id = currentSize + i;
                        item.title = "item" + (item.id);

                        newList.add(item);
                    }


                    activity.addItems(activity.mDataAdapter.getSampleItems());

                    activity.mRecyclerView.refreshComplete(10);
                    activity.notifyDataSetChanged();
                    break;
                case -2:
                    activity.notifyDataSetChanged();
                    break;
                case -3:

                    activity.mRecyclerView.refreshComplete(10);
                    activity.notifyDataSetChanged();
                    activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            requestData();
                        }
                    });
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
                if (NetBroadcastReceiverUtils.isConnectedToInternet(ExpandableRecyclerViewOneActivity.this)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
        this.getMenuInflater().inflate(R.menu.menu_recyclerview_main_expand, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_expand_all:
                mDataAdapter.expandAll();
                return true;
            case R.id.action_collapse_all:
                mDataAdapter.collapseAll();
                return true;
            default:
                return true;
        }


    }

}