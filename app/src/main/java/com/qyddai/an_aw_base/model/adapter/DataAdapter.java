package com.qyddai.an_aw_base.model.adapter;

import android.content.Context;
import android.widget.TextView;

import com.qyddai.an_aw_base.R;
import com.qyddai.an_aw_base.model.ListBaseAdapter;
import com.qyddai.an_aw_base.model.SuperViewHolder;
import com.qyddai.an_aw_base.model.entity.ItemModel;


/**
 * Created by Lzx on 2016/12/30.
 */

public class DataAdapter extends ListBaseAdapter<ItemModel> {

    public DataAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.sample_item_text;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ItemModel item = mDataList.get(position);

        TextView titleText = holder.getView(R.id.info_text);
        titleText.setText(item.title);
    }
}
