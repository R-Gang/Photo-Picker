package com.gang.photo.kotlin

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDialog
import com.gang.imageloader.initImage
import com.gang.tools.kotlin.utils.initToolsUtils

/**
 * 初始化
 */
fun initPhotoPicker(context: Context) {
    mPhotoContext = context
    initImage.initLoadImage(mPhotoContext as Application) // 初始化图片框架
    initToolsUtils(mPhotoContext as Application) // 初始化常用工具框架
}

/**
 * 获取mContext
 */
var mPhotoContext: Context? = null


var mLoadingDialog: AppCompatDialog? = null
fun showLoadingDialog(activity: Activity) {
    mLoadingDialog = AppCompatDialog(activity)
    mLoadingDialog?.setContentView(R.layout.bga_pp_dialog_loading)
    mLoadingDialog?.setCancelable(false)
    mLoadingDialog?.show()
}

fun dismissLoadingDialog() {
    if (mLoadingDialog != null && mLoadingDialog?.isShowing == true) {
        mLoadingDialog?.dismiss()
    }
}