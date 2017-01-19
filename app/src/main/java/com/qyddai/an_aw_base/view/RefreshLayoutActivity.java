package com.qyddai.an_aw_base.view;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.an.base.view.SuperActivity;
import com.an.base.view.refreshlayout.BGARefreshLayout;
import com.an.base.view.refreshlayout.BGAStickinessRefreshViewHolder;
import com.qyddai.an_aw_base.R;
import com.qyddai.an_aw_base.view.activity.RefreshGridViewActivity;
import com.qyddai.an_aw_base.view.activity.RefreshListViewActivity;
import com.qyddai.an_aw_base.view.activity.RefreshNormalViewActivity;
import com.qyddai.an_aw_base.view.activity.RefreshRecyclerViewActivity;
import com.qyddai.an_aw_base.view.activity.RefreshScrollViewActivity;
import com.qyddai.an_aw_base.view.activity.RefreshStaggeredRecyclerViewActivity;
import com.qyddai.an_aw_base.view.activity.RefreshSwipeListViewActivity;
import com.qyddai.an_aw_base.view.activity.RefreshSwipeRecyclerViewActivity;
import com.qyddai.an_aw_base.view.activity.RefreshWebViewActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 作者:孙顺涛 邮件:qyddai@gmail.com
 * 描述: * 创建时间:15/7/10 14:11修改王皓大神
 */
@ContentView(R.layout.sst_activity_refreshlayout)
public class RefreshLayoutActivity extends SuperActivity implements BGARefreshLayout.BGARefreshLayoutDelegate, View.OnClickListener {
    @ViewInject(R.id.rl_modulename_refresh)
    private BGARefreshLayout mRefreshLayout;

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;
    private Button btn9;
    private Button btn10;

    @Override
    public void initView() {
        // 为BGARefreshLayout 设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
//        BGARefreshViewHolder refreshViewHolder = new BGAStickinessRefreshViewHolder(this, true);
        final BGAStickinessRefreshViewHolder refreshViewHolder = new BGAStickinessRefreshViewHolder(this, true);
        refreshViewHolder.setRotateImage(R.drawable.base_refresh_loding);
        refreshViewHolder.setStickinessColor(R.color.CommMainBgClicked);
        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
// 为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
        // 设置正在加载更多时不显示加载更多控件
        // mRefreshLayout.setIsShowLoadingMoreView(false);
        // 设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText("loadingMoreText");
        // 设置整个加载更多控件的背景颜色资源 id
//        refreshViewHolder.setLoadMoreBackgroundColorRes(loadMoreBackgroundColorRes);
        // 设置整个加载更多控件的背景 drawable 资源 id
//        refreshViewHolder.setLoadMoreBackgroundDrawableRes(loadMoreBackgroundDrawableRes);
        // 设置下拉刷新控件的背景颜色资源 id
//        refreshViewHolder.setRefreshViewBackgroundColorRes(refreshViewBackgroundColorRes);
        // 设置下拉刷新控件的背景 drawable 资源 id
//        refreshViewHolder.setRefreshViewBackgroundDrawableRes(refreshViewBackgroundDrawableRes);
        // 设置自定义头部视图（也可以不用设置）
        // 参数1：自定义头部视图（例如广告位）， 参数2：上拉加载更多是否可用
//        mRefreshLayout.setCustomHeaderView(mBanner, false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                mRefreshLayout.endLoadingMore();
                refreshViewHolder.onEndRefreshing();
            }
        }, 5000);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        btn8 = (Button) findViewById(R.id.btn8);
        btn9 = (Button) findViewById(R.id.btn9);
        btn10 = (Button) findViewById(R.id.btn10);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn10.setOnClickListener(this);

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                startActivity(new Intent(RefreshLayoutActivity.this, RefreshGridViewActivity.class));
                break;
            case R.id.btn2:
                startActivity(new Intent(RefreshLayoutActivity.this, RefreshListViewActivity.class));
                break;
            case R.id.btn3:
                startActivity(new Intent(RefreshLayoutActivity.this, RefreshRecyclerViewActivity.class));
                break;
            case R.id.btn4:
                startActivity(new Intent(RefreshLayoutActivity.this, RefreshSwipeListViewActivity.class));
                break;
            case R.id.btn5:
                startActivity(new Intent(RefreshLayoutActivity.this, RefreshSwipeRecyclerViewActivity.class));
                break;
            case R.id.btn6:
                startActivity(new Intent(RefreshLayoutActivity.this, RefreshStaggeredRecyclerViewActivity.class));
                break;
            case R.id.btn7:
                startActivity(new Intent(RefreshLayoutActivity.this, RefreshScrollViewActivity.class));
                break;
            case R.id.btn8:
                startActivity(new Intent(RefreshLayoutActivity.this, RefreshNormalViewActivity.class));
                break;
            case R.id.btn9:
                startActivity(new Intent(RefreshLayoutActivity.this, RefreshWebViewActivity.class));
                break;
            case R.id.btn10:
                break;
        }

    }
}