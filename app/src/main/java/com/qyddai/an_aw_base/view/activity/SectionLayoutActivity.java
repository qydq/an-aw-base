package com.qyddai.an_aw_base.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.qyddai.an_aw_base.R;
import com.qyddai.an_aw_base.view.fragment.DoubleHeaderFragment;
import com.qyddai.an_aw_base.view.fragment.InlineStickyHeaderFragment;
import com.qyddai.an_aw_base.view.fragment.StickyHeaderFragment;


public class SectionLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_pager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        HeaderPagerAdapter adapter = new HeaderPagerAdapter(this.getSupportFragmentManager());

        ViewPager pager = (ViewPager) this.findViewById(R.id.pager);
        pager.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_recyclerview_setion, menu);
        return true;
    }

    class HeaderPagerAdapter extends FragmentPagerAdapter {

        public HeaderPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new StickyHeaderFragment();

                case 1:
                    return new InlineStickyHeaderFragment();

                case 2:
                    return new DoubleHeaderFragment();

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Sticky Header";

                case 1:
                    return "Sticky Header - Inline";

                case 2:
                    return "Double Header";

                default:
                    return null;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (item.getItemId() == R.id.action_about) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/qydq/an-aw-base"));
            this.startActivity(intent);
            return true;
        }
        return true;
    }
}
