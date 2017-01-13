package com.qyddai.an_aw_base.view.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.an.base.view.recyclerview.recyclerview.LRecyclerViewAdapter;
import com.qyddai.an_aw_base.R;
import com.qyddai.an_aw_base.model.adapter.InlineStickyTestAdapter;
import com.qyddai.an_aw_base.utils.StickyHeaderDecoration;


public class InlineStickyHeaderFragment
        extends BaseDecorationFragment {

    private StickyHeaderDecoration decor;

    @Override
    protected void setAdapterAndDecor(RecyclerView list) {
        final InlineStickyTestAdapter adapter = new InlineStickyTestAdapter(this.getActivity());
        decor = new StickyHeaderDecoration(adapter, true);
        setHasOptionsMenu(true);

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        list.setAdapter(mLRecyclerViewAdapter);
        list.addItemDecoration(decor, 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_clear_cache) {
            decor.clearHeaderCache();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
