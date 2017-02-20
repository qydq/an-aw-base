package com.qyddai.an_aw_base.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.an.base.view.recyclerview.ListBaseAdapter;
import com.an.base.view.recyclerview.SuperViewHolder;
import com.an.base.view.recyclerview.interfaces.OnItemClickListener;
import com.an.base.view.recyclerview.interfaces.OnItemLongClickListener;
import com.an.base.view.recyclerview.recyclerview.LRecyclerView;
import com.an.base.view.recyclerview.recyclerview.LRecyclerViewAdapter;
import com.qyddai.an_aw_base.R;
import com.qyddai.an_aw_base.model.entity.ItemModel;
import com.qyddai.an_aw_base.utils.SampleFooter;
import com.qyddai.an_aw_base.utils.SampleHeader;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 局部刷新
 */
public class PartialRefreshActivity extends AppCompatActivity {

    private LRecyclerView mRecyclerView = null;

    private DataAdapter mDataAdapter = null;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sst_activity_recyclerview_endless);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (LRecyclerView) findViewById(R.id.list);

        //init data
        ArrayList<ItemModel> dataList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ItemModel itemModel = new ItemModel();
            itemModel.title = "item" + i;
            itemModel.imgUrl = "http://b.bst.126.net/common/face60.png";
            dataList.add(itemModel);
        }

        mDataAdapter = new DataAdapter(this);
        mDataAdapter.setDataList(dataList);

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //add a HeaderView
        View header = LayoutInflater.from(this).inflate(R.layout.base_recyclerview_header, (ViewGroup) findViewById(android.R.id.content), false);

        mLRecyclerViewAdapter.addHeaderView(header);
        mLRecyclerViewAdapter.addHeaderView(new SampleHeader(this));

        //add a FooterView
        mLRecyclerViewAdapter.addFooterView(new SampleFooter(this));

        //禁止下拉刷新功能
        mRecyclerView.setPullRefreshEnabled(false);

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ItemModel item = mDataAdapter.getDataList().get(position);
                Toast.makeText(getApplicationContext(), "onItemLongClick - " + item.title, Toast.LENGTH_SHORT).show();
            }

        });

        mLRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                ItemModel item = mDataAdapter.getDataList().get(position);
                Toast.makeText(getApplicationContext(), "onItemLongClick - " + item.title, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private class DataAdapter extends ListBaseAdapter<ItemModel> {

        public DataAdapter(Context context) {
            super(context);
        }


        @Override
        public int getLayoutId() {
            return R.layout.item_recyclerview_pictext;
        }

        @Override
        public void onBindItemHolder(SuperViewHolder holder, int position) {
            bind(holder, position);
        }

        @Override
        public void onBindItemHolder(SuperViewHolder holder, int position, List<Object> payloads) {
            super.onBindItemHolder(holder, position, payloads);

            //注意：payloads的size总是1
            String payload = (String) payloads.get(0);
//            TLog.error("payload = " + payload);

            TextView textView = holder.getView(R.id.info_text);
            //需要更新的控件
            ItemModel itemModel = mDataList.get(position);
            textView.setText(itemModel.title);
        }

        private void bind(SuperViewHolder holder, int position) {
            ItemModel itemModel = mDataList.get(position);

            TextView textView = holder.getView(R.id.info_text);
            ImageView avatarImage = holder.getView(R.id.avatar_image);

            textView.setText(itemModel.title);
            x.image().bind(avatarImage, itemModel.imgUrl);
//            ImageLoaderUtil imageLoaderUtil = new ImageLoaderUtil();
//            ImageLoader imageLoader = new ImageLoader.Builder()
//                    .imgView(avatarImage)
//                    .url(itemModel.imgUrl)
//                    //.strategy(ImageLoaderUtil.LOAD_STRATEGY_ONLY_WIFI) 可以不写
//                    .build();
//            //imageLoaderUtil.setLoadImgStrategy(new GlideImageLoaderStrategy()); //这里可以更改图片加载框架
//            imageLoaderUtil.loadImage(mContext, imageLoader);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recyclerview_partial_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_partial_refresh) {

            int position = 1;//指定列表中的第2个item

            ItemModel itemModel = mDataAdapter.getDataList().get(position);
            itemModel.id = 100;
            itemModel.title = "refresh item " + itemModel.id;
            mDataAdapter.getDataList().set(position, itemModel);

            //RecyclerView局部刷新
            // notifyItemChanged(int position, Object payload) 其中的payload相当于一个标记，类型不限
            mLRecyclerViewAdapter.notifyItemChanged(mLRecyclerViewAdapter.getAdapterPosition(false, position), "jdsjlzx");

        }
        return true;
    }

}