package com.qyddai.an_aw_base.view.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.daimajia.easing.BaseEasingMethod;
import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;
import com.qyddai.an_aw_base.R;
import com.qyddai.an_aw_base.view.DrawView;
import com.qyddai.an_aw_base.view.EasingAdapter;


public class MyActivity extends Activity {

    private ListView mEasingList;
    private EasingAdapter mAdapter;
    private View mTarget;

    private DrawView mHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        mEasingList = (ListView) findViewById(R.id.easing_list);
        mAdapter = new EasingAdapter(this);
        mEasingList.setAdapter(mAdapter);
        mTarget = findViewById(R.id.target);
        mHistory = (DrawView) findViewById(R.id.history);
        mEasingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mHistory.clear();
                Skill s = (Skill) view.getTag();
                AnimatorSet set = new AnimatorSet();
                mTarget.setTranslationX(0);
                mTarget.setTranslationY(0);
                set.playTogether(
                        Glider.glide(s, 1200, ObjectAnimator.ofFloat(mTarget, "translationY", 0, dipToPixels(MyActivity.this, -(160 - 3))), new BaseEasingMethod.EasingListener() {
                            @Override
                            public void on(float time, float value, float start, float end, float duration) {
                                mHistory.drawPoint(time, duration, value - dipToPixels(MyActivity.this, 60));
                            }
                        })
                );
                set.setDuration(1200);
                set.start();
            }
        });

    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
}
