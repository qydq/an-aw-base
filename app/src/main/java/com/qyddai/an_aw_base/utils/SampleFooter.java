package com.qyddai.an_aw_base.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.qyddai.an_aw_base.R;


/**
 * RecyclerView的FooterView，简单的展示一个TextView
 */
public class SampleFooter extends RelativeLayout {

    public SampleFooter(Context context) {
        super(context);
        init(context);
    }

    public SampleFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SampleFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {

        inflate(context, R.layout.base_recyclerview_footer, this);
    }
}