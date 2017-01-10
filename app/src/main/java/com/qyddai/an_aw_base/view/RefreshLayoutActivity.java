package com.qyddai.an_aw_base.view;

import android.os.Handler;

import com.an.base.view.SuperActivity;
import com.an.base.view.refreshlayout.BGARefreshLayout;
import com.an.base.view.refreshlayout.BGARefreshViewHolder;
import com.an.base.view.refreshlayout.BGAStickinessRefreshViewHolder;
import com.qyddai.an_aw_base.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/7/10 14:11
 * 描述:
 */
@ContentView(R.layout.sst_activity_refresh_layout)
public class RefreshLayoutActivity extends SuperActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @ViewInject(R.id.rl_modulename_refresh)
    private BGARefreshLayout mRefreshLayout;

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
        },5000);

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}