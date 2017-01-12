package com.qyddai.an_aw_base.model.entity;


import com.qyddai.an_aw_base.model.adapter.ExpandableRecyclerAdapter;

import static com.qyddai.an_aw_base.model.adapter.CommentExpandAdapter.TYPE_PERSON;
import static com.qyddai.an_aw_base.model.adapter.ExpandableRecyclerAdapter.TYPE_HEADER;

/**
 * Created by Lzx on 2016/9/30.
 */

public class CommentItem extends ExpandableRecyclerAdapter.ListItem {

    public String Text;

    public CommentItem(String group) {
        super(TYPE_HEADER);
        Text = group;
    }

    public CommentItem(String first, String last) {
        super(TYPE_PERSON);
        Text = first + " " + last;
    }
}
