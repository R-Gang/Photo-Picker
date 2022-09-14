package com.library.kotlin.photo.picker.pw

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gang.recycler.kotlin.interfaces.ViewOnItemClick
import com.gang.tools.kotlin.dimension.screenHeight
import com.library.kotlin.photo.R
import com.library.kotlin.photo.picker.adapter.FolderAdapter
import com.library.kotlin.photo.picker.model.PhotoFolderModel

/**
 * @CreateDate: 2022/9/6 09:58
 * @Author: haoruigang
 * @ClassName: BGAPhotoFolderPw
 * @Description: 选择图片目录的PopupWindow
 */
class PhotoFolderPw(activity: Activity?, anchorView: View?, private val mDelegate: Delegate?) :
    BasePopupWindow(activity,
        R.layout.pp_pw_photo_folder,
        anchorView,
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.MATCH_PARENT), ViewOnItemClick {

    private var mRootLl: LinearLayout? = null
    private var mContentRv: RecyclerView? = null
    private var mFolderAdapter: FolderAdapter? = null
    var currentPosition = 0
        private set

    override fun initView() {
        mRootLl = findViewById(R.id.ll_photo_folder_root)
        mContentRv = findViewById(R.id.rv_photo_folder_content)
    }

    override fun setListener() {
        mRootLl?.setOnClickListener(this)
        mFolderAdapter = FolderAdapter(arrayListOf<PhotoFolderModel>(), mActivity, this)
    }

    override fun processLogic() {
        animationStyle = android.R.style.Animation
        setBackgroundDrawable(ColorDrawable(-0x70000000))
        mContentRv?.layoutManager = LinearLayoutManager(mActivity)
        mContentRv?.adapter = mFolderAdapter
    }

    /**
     * 设置目录数据集合
     *
     * @param data
     */
    fun setData(data: ArrayList<PhotoFolderModel?>?) {
        mFolderAdapter?.update(data)
    }

    override fun show() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val location = IntArray(2)
            mAnchorView.getLocationInWindow(location)
            val offsetY = location[1] + mAnchorView.height
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                height = screenHeight - offsetY
            }
            showAtLocation(mAnchorView, Gravity.NO_GRAVITY, 0, offsetY)
        } else {
            showAsDropDown(mAnchorView)
        }
        mContentRv?.let {
            ViewCompat.animate(it).translationY(-mWindowRootView.height.toFloat())
                .setDuration(0).start()
            ViewCompat.animate(it).translationY(0f).setDuration(ANIM_DURATION.toLong())
                .start()
        }
        mRootLl?.let {
            ViewCompat.animate(it).alpha(0f).setDuration(0).start()
            ViewCompat.animate(it).alpha(1f).setDuration(ANIM_DURATION.toLong()).start()
        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.ll_photo_folder_root) {
            dismiss()
        }
    }

    override fun dismiss() {
        mContentRv?.let {
            ViewCompat.animate(it).translationY(-mWindowRootView.height.toFloat())
                .setDuration(
                    ANIM_DURATION.toLong()).start()
        }
        mRootLl?.let {
            ViewCompat.animate(it).alpha(1f).setDuration(0).start()
            ViewCompat.animate(it).alpha(0f).setDuration(ANIM_DURATION.toLong()).start()
        }
        mDelegate?.executeDismissAnim()
        mContentRv?.postDelayed({ super@PhotoFolderPw.dismiss() }, ANIM_DURATION.toLong())
    }

    interface Delegate {
        fun onSelectedFolder(position: Int)
        fun executeDismissAnim()
    }

    companion object {
        const val ANIM_DURATION = 300
    }

    override fun setOnItemClickListener(view: View?, position: Int) {
        if (mDelegate != null && currentPosition != position) {
            mDelegate.onSelectedFolder(position)
        }
        currentPosition = position
        dismiss()
    }
}