package com.qyddai.an_aw_base.model.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.qyddai.an_aw_base.R;
import com.qyddai.an_aw_base.model.SuperViewHolder;
import com.qyddai.an_aw_base.model.entity.MultipleItem;


/**
 * Created by Lzx on 2016/12/30.
 */

public class MultipleItemAdapter extends MultiAdapter<MultipleItem> {

    public MultipleItemAdapter(Context context) {
        super(context);
        addItemType(MultipleItem.TEXT, R.layout.item_recyclerview_text);
        addItemType(MultipleItem.IMG, R.layout.item_recyclerview_pictext);
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        MultipleItem item = getDataList().get(position);
        switch (item.getItemType()) {
            case MultipleItem.TEXT:
                bindTextItem(holder, item);
                break;
            case MultipleItem.IMG:
                bindPicItem(holder, item);
                break;
            default:
                break;
        }

    }

    private void bindTextItem(SuperViewHolder holder, MultipleItem item) {
        TextView textView = holder.getView(R.id.info_text);
        textView.setText(item.getTitle());
    }

    private void bindPicItem(SuperViewHolder holder, MultipleItem item) {
        TextView textView = holder.getView(R.id.info_text);
        ImageView avatarImage = holder.getView(R.id.avatar_image);

        textView.setText(item.getTitle());
        avatarImage.setImageResource(R.mipmap.yy);
    }


}
