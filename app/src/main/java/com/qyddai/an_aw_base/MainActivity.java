package com.qyddai.an_aw_base;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.qyddai.an_aw_base.model.adapter.MainItemAdapter;
import com.qyddai.an_aw_base.utils.ListViewDecoration;
import com.qyddai.an_aw_base.view.LRecyclerViewActivity;
import com.qyddai.an_aw_base.view.LittleTrickActivity;
import com.qyddai.an_aw_base.view.RefreshLayoutActivity;
import com.qyddai.an_aw_base.view.SwipeRecyclerActivity;
import com.qyddai.an_aw_base.view.activity.YYTipsActivity;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    private RecyclerView recyclerView;
    private List<String> titles;
    private List<String> descriptions;
    private MainItemAdapter mMainItemAdapter;

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, LittleTrickActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, SwipeRecyclerActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, RefreshLayoutActivity.class));
                break;
            case 3:
                startActivity(new Intent(this, LRecyclerViewActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, YYTipsActivity.class));
                break;
            case 5:
//                startActivity(new Intent(this, GridDragMenuActivity.class));
                break;
            case 6:
//                startActivity(new Intent(this, ListDragSwipeActivity.class));
                break;
            case 7:
//                startActivity(new Intent(this, DragSwipeFlagsActivity.class));
                break;
            case 8:
//                startActivity(new Intent(this, DefineActivity.class));
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ListViewDecoration());

        titles = Arrays.asList(getResources().getStringArray(R.array.main_item));
        descriptions = Arrays.asList(getResources().getStringArray(R.array.main_item_des));
        mMainItemAdapter = new MainItemAdapter(titles, descriptions);
        mMainItemAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mMainItemAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_recyclerview_setion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (item.getItemId() == R.id.action_about) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/qydq/an-aw-base/blob/master/README.md"));
            this.startActivity(intent);
            return true;
        }
        return true;
    }

}
