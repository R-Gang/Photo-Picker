package com.gang.photo.kotlin.picker.pw;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.PopupWindow;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

/**
 * @CreateDate: 2022/9/6 09:58
 * @Author: haoruigang
 * @ClassName: BasePopupWindow
 * @Description: 类作用描述
 */
public abstract class BasePopupWindow extends PopupWindow implements View.OnClickListener {
    protected Activity mActivity;
    protected View mWindowRootView;
    protected View mAnchorView;

    public BasePopupWindow(Activity activity, @LayoutRes int layoutId, View anchorView, int width, int height) {
        super(View.inflate(activity, layoutId, null), width, height, true);
        init(activity, anchorView);

        initView();
        setListener();
        processLogic();
    }

    private void init(Activity activity, View anchorView) {
        getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        // 如果想让在点击别的地方的时候 关闭掉弹出窗体 一定要记得给mPopupWindow设置一个背景资源
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mAnchorView = anchorView;
        mActivity = activity;
        mWindowRootView = activity.getWindow().peekDecorView();
    }

    protected abstract void initView();

    protected abstract void setListener();

    protected abstract void processLogic();

    public abstract void show();

    @Override
    public void onClick(View view) {
    }

    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT findViewById(@IdRes int id) {
        return getContentView().findViewById(id);
    }
}