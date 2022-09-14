package com.library.kotlin.photo.picker

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import com.gang.library.common.ext.permissions.BasePermissionActivity
import com.gang.library.common.ext.permissions.PermissionCallBackM
import com.gang.library.common.user.Config
import com.gang.tools.kotlin.utils.LogUtils
import com.library.kotlin.photo.R
import com.library.kotlin.photo.picker.other.IntentBuilder
import com.library.kotlin.photo.picker.other.cropPhotoDir
import com.library.kotlin.photo.picker.other.takePhotoDir


/**
 * @CreateDate:     2022/7/19 15:34
 * @ClassName:      permission
 * @Description:    申请权限
 * @Author:         haoruigang
 */

const val RC_CHOOSE_PHOTO = 1
const val RC_MANAGE_ALL_FILES = 2
const val RC_DATA_FOR_DIR = 3

// 如果 Activity继承PhotoPickerActivity 直接跳转
fun Activity.toPickerTakePhoto(
    context: Context,
    maxChooseCoun: Int = 1,
    isForResult: Boolean = false,
    isCallResult: Boolean = false,
    action: (requestCode: Int, perms: String) -> Unit,
) {
    (this as BasePermissionActivity).requestPermission(Config.REQUEST_CAMERA,
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA),
        context.getString(R.string.string_permission_camear),
        object : PermissionCallBackM {
            override fun onPermissionGrantedM(
                requestCode: Int,
                vararg perms: String?,
            ) {
                LogUtils.e(
                    "onPermissionGrantedM", Thread.currentThread().name,
                )
                if (isCallResult) {
                    action.invoke(requestCode, perms.toString())
                } else {
                    val photoPickerIntent: Intent = IntentBuilder(this@toPickerTakePhoto)
                        .cameraFileDir(takePhotoDir()) // 是否开启拍照
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

            override fun onPermissionDeniedM(
                requestCode: Int,
                vararg perms: String?,
            ) {
                LogUtils.e(context.toString(), "TODO: INTERNET Denied")
            }
        })
}

fun Context.toPickerTakePhoto(
    context: Context,
    maxChooseCoun: Int = 1,
    isForResult: Boolean = false,
    isCallResult: Boolean = false,
    action: (requestCode: Int, perms: String) -> Unit,
) {
    (this as BasePermissionActivity).requestPermission(Config.REQUEST_CAMERA,
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA),
        context.getString(R.string.string_permission_camear),
        object : PermissionCallBackM {
            override fun onPermissionGrantedM(
                requestCode: Int,
                vararg perms: String?,
            ) {
                LogUtils.e(
                    "onPermissionGrantedM", Thread.currentThread().name,
                )
                if (isCallResult) {
                    action.invoke(requestCode, perms.toString())
                } else {
                    val photoPickerIntent: Intent = IntentBuilder(this@toPickerTakePhoto)
                        .cameraFileDir(takePhotoDir()) // 是否开启拍照
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

            override fun onPermissionDeniedM(
                requestCode: Int,
                vararg perms: String?,
            ) {
                LogUtils.e(context.toString(), "TODO: INTERNET Denied")
            }
        })
}