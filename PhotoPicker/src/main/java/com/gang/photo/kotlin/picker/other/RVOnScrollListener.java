package com.gang.photo.kotlin.picker.other;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @CreateDate: 2022/9/6 10:22
 * @Author: haoruigang
 * @ClassName: RVOnScrollListener
 * @Description: 类作用描述
 */
public class RVOnScrollListener extends RecyclerView.OnScrollListener {
    private Activity mActivity;

    public RVOnScrollListener(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            //BGAImage.resume(mActivity);
        } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            //BGAImage.pause(mActivity);
        }
    }
}
