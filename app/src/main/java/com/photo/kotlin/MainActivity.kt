package com.photo.kotlin

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore.Images
import com.gang.library.base.BaseVBActivity
import com.gang.photo.kotlin.permiss.RC_CHOOSE_PHOTO
import com.gang.photo.kotlin.permiss.toPickerTakePhoto
import com.gang.photo.kotlin.picker.other.IntentBuilder
import com.gang.photo.kotlin.picker.other.cropPhotoDir
import com.gang.photo.kotlin.picker.other.takePhotoDir
import com.gang.photo.kotlin.picker.utils.PhotoHelper.Companion.getAppAllFilesPerIntent
import com.gang.tools.kotlin.utils.LogUtils
import com.gang.tools.kotlin.utils.vClick
import com.gang.tools.kotlin.view.marginTopWithStatusBar
import com.photo.kotlin.databinding.ActivityMainBinding

class MainActivity : BaseVBActivity<ActivityMainBinding>() {

    val TAG = "MainActivity"

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.apply {
            ivAvatar.marginTopWithStatusBar()
        }
    }

    override fun initData() {

    }

    override fun onClick() {
        super.onClick()
        mBinding?.apply {
            selectPhoto.vClick {
                //判断是否需要所有文件权限  perform action when allow permission success
                if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager())) {
                    //表明已经有这个权限了
                    pickerTakePhoto()
                } else {
                    // 获取权限 ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                    startActivity(mContext.getAppAllFilesPerIntent)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_CHOOSE_PHOTO) {
                val uri = data?.data
                LogUtils.d(TAG, "选择图片路径$uri")
                val bitmap = Images.Media.getBitmap(contentResolver, uri)
                mBinding?.ivAvatar?.setImageBitmap(bitmap)
            }
        }
    }

    fun pickerTakePhoto(
        maxChooseCoun: Int = 1,
        isForResult: Boolean = true,
    ) {
        //toPickerTakePhoto(this@MainActivity)
        toPickerTakePhoto(
            isCallResult = true
        ) { permission: Array<out String> ->
            val photoPickerIntent: Intent =
                IntentBuilder(this).cameraFileDir(takePhotoDir()) // 是否开启拍照
                    .maxChooseCount(maxChooseCoun) // 图片选择张数的最大值
                    .cropFileDir(cropPhotoDir()) // 是否开启裁剪
                    .build()
            if (isForResult) {
                startActivityForResult(photoPickerIntent, RC_CHOOSE_PHOTO)
            } else {
                startActivity(photoPickerIntent)
            }
        }
    }
}