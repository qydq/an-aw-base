package com.qyddai.an_aw_base.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.qyddai.an_aw_base.OnItemClickListener;
import com.qyddai.an_aw_base.R;
import com.qyddai.an_aw_base.adapter.MainItemAdapter;

import java.util.Arrays;
import java.util.List;

//如果添加注解，则夜间模式用不了，只能findViewById实现。
public class SwipeRecyclerActivity extends AppCompatActivity implements OnItemClickListener {

    private RecyclerView recyclerView;
    private List<String> titles;
    private List<String> descriptions;
    private MainItemAdapter mMainItemAdapter;

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, AllMenuActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, ViewTypeMenuActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, ViewPagerMenuActivity.class));
                break;
            case 3:
                startActivity(new Intent(this, RefreshLoadMoreActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, ListDragMenuActivity.class));
                break;
            case 5:
                startActivity(new Intent(this, GridDragMenuActivity.class));
                break;
            case 6:
                startActivity(new Intent(this, ListDragSwipeActivity.class));
                break;
            case 7:
                startActivity(new Intent(this, DragSwipeFlagsActivity.class));
                break;
            case 8:
                startActivity(new Intent(this, DefineActivity.class));
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ListViewDecoration());

        titles = Arrays.asList(getResources().getStringArray(R.array.main_item1));
        descriptions = Arrays.asList(getResources().getStringArray(R.array.main_item_des1));
        mMainItemAdapter = new MainItemAdapter(titles, descriptions);
        mMainItemAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mMainItemAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_open_rv_menu) {
            //如果点击右边，则打开第一个滑动+
//            mSwipeMenuRecyclerView.openRightMenu(0);
        }
        return true;
    }

}
