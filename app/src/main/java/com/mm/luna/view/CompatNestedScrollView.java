package com.mm.luna.view;

import android.content.Context;
import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.mm.luna.MyApplication;
import com.mm.luna.util.StatusBarUtils;
import com.scwang.smartrefresh.layout.util.DensityUtil;


/**
 * 自定义NestedScrollView，主要实现根据滑动距离控制绑定View的alpha值
 */

public class CompatNestedScrollView extends NestedScrollView {
    private View headView;
    private View bindView;

    public CompatNestedScrollView(Context context) {
        super(context);
    }

    public CompatNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CompatNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {
            throw new IllegalArgumentException("only can 1 child in this view");
        } else {
            if (getChildAt(0) instanceof ViewGroup) {
                ViewGroup childViewGroup = (ViewGroup) getChildAt(0);
                if (childViewGroup != null) {
                    headView = childViewGroup.getChildAt(0);
                }
            } else {
                throw new IllegalArgumentException("child must be instanceof ViewGroup");
            }
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float alpha = 1.f;
        if (headView != null && bindView != null) {
            //如果上滑超过toolbar高度，开启伴随动画
            //Logger.e("t = " + t);
            //Logger.e("headView.getHeight = " + headView.getHeight());
            float slideValue = t - (DensityUtil.dp2px(56) + StatusBarUtils.getStatusBarHeight(MyApplication.getAppContext()));

            if (slideValue < 0)
                slideValue = 0;

            float fraction = slideValue / (headView.getHeight() / 2.f);
            if (fraction > 1) {
                fraction = 1;
            }

            alpha *= fraction;
            bindView.setAlpha(alpha);
        }
    }

    /**
     * 绑定要变化Alpha的view
     *
     * @param view 要变化Alpha的view
     */
    public void bindAlphaView(View view) {
        bindView = view;
    }
}
