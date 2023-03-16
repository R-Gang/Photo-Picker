package com.library.kotlin.photo.picker.other

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import com.library.kotlin.photo.activity.PhotoPickerActivity
import com.library.kotlin.photo.mPhotoContext
import com.library.kotlin.photo.picker.utils.PhotoHelper
import java.io.File


const val EXTRA_CAMERA_FILE_DIR = "EXTRA_CAMERA_FILE_DIR"
const val EXTRA_SELECTED_PHOTOS = "EXTRA_SELECTED_PHOTOS"
const val EXTRA_MAX_CHOOSE_COUNT = "EXTRA_MAX_CHOOSE_COUNT"
const val EXTRA_PAUSE_ON_SCROLL = "EXTRA_PAUSE_ON_SCROLL"
const val STATE_SELECTED_PHOTOS = "STATE_SELECTED_PHOTOS"
const val EXTRA_CURRENT_POSITION = "EXTRA_CURRENT_POSITION"
const val EXTRA_CROP_FILE_DIR = "EXTRA_CROP_FILE_DIR"

const val EXTRA_CROP_WIDTH = "EXTRA_CROP_WIDTH"
const val EXTRA_CROP_HEIGHT = "EXTRA_CROP_HEIGHT"
const val EXTRA_CROP_ASPECTX = "EXTRA_CROP_ASPECTX"
const val EXTRA_CROP_ASPECTY = "EXTRA_CROP_ASPECTY"

const val EXTRA_IS_FROM_TAKE_PHOTO = "EXTRA_IS_FROM_TAKE_PHOTO"
const val EXTRA_IS_CIRCLE_CROP_TAKE_PHOTO = "EXTRA_IS_CIRCLE_CROP_TAKE_PHOTO"


/**
 * 拍照的请求码
 */
const val REQUEST_CODE_TAKE_PHOTO = 1

/**
 * 裁剪的请求码
 */
const val REQUEST_CODE_CROP = 3

/**
 * 预览照片的请求码
 */
const val RC_PREVIEW = 2
const val SPAN_COUNT = 4


/**
 * 获取已选择的图片集合
 *
 * @param intent
 * @return
 */
fun getSelectedPhotos(intent: Intent): ArrayList<String> {
    return intent.getStringArrayListExtra(EXTRA_SELECTED_PHOTOS) as ArrayList<String>
}

/**
 * 获取已选择的图片集合
 *
 * @param Bundle
 * @return
 */
fun getSelectedPhotos(savedState: Bundle): ArrayList<String> {
    return savedState.getStringArrayList(STATE_SELECTED_PHOTOS) as ArrayList<String>
}

/**
 * 当前已选中的图片路径集合，可以传 null
 */
fun putSelectedPhotos(outState: Bundle, selectedPhotos: ArrayList<String>?) {
    outState.putStringArrayList(STATE_SELECTED_PHOTOS, selectedPhotos)
}

/**
 * 是否是拍照预览
 *
 * @param intent
 * @return
 */
fun getIsFromTakePhoto(intent: Intent): Boolean {
    return intent.getBooleanExtra(
        EXTRA_IS_FROM_TAKE_PHOTO,
        false
    )
}


class IntentBuilder(context: Context?) {
    val mIntent: Intent

    /**
     * 拍照后图片保存的目录。如果传 null 表示没有拍照功能，如果不为 null 则具有拍照功能，
     */
    fun cameraFileDir(cameraFileDir: File?): IntentBuilder {
        mIntent.putExtra(EXTRA_CAMERA_FILE_DIR, cameraFileDir)
        return this
    }

    /**
     * 图片选择张数的最大值
     *
     * @param maxChooseCount
     * @return
     */
    fun maxChooseCount(maxChooseCount: Int): IntentBuilder {
        mIntent.putExtra(EXTRA_MAX_CHOOSE_COUNT, maxChooseCount)
        return this
    }

    /**
     * 当前已选中的图片路径集合，可以传 null
     */
    fun selectedPhotos(selectedPhotos: ArrayList<String?>?): IntentBuilder {
        mIntent.putStringArrayListExtra(EXTRA_SELECTED_PHOTOS, selectedPhotos)
        return this
    }

    /**
     * 滚动列表时是否暂停加载图片，默认为 false
     */
    fun pauseOnScroll(pauseOnScroll: Boolean): IntentBuilder {
        mIntent.putExtra(EXTRA_PAUSE_ON_SCROLL, pauseOnScroll)
        return this
    }

    /**
     * 当前预览图片的索引
     */
    fun currentPosition(currentPosition: Int): IntentBuilder {
        mIntent.putExtra(
            EXTRA_CURRENT_POSITION,
            currentPosition
        )
        return this
    }

    /**
     * 裁剪的宽
     */
    fun cropWidth(cropWidth: Int): IntentBuilder {
        mIntent.putExtra(
            EXTRA_CROP_WIDTH,
            cropWidth
        )
        return this
    }

    /**
     * 裁剪的高
     */
    fun cropHeight(cropHeight: Int): IntentBuilder {
        mIntent.putExtra(
            EXTRA_CROP_HEIGHT,
            cropHeight
        )
        return this
    }

    /**
     * 裁剪的宽比
     */
    fun cropAspectX(cropWidth: Int): IntentBuilder {
        mIntent.putExtra(
            EXTRA_CROP_ASPECTX,
            cropWidth
        )
        return this
    }

    /**
     * 裁剪的高比
     */
    fun cropAspectY(cropHeight: Int): IntentBuilder {
        mIntent.putExtra(
            EXTRA_CROP_ASPECTY,
            cropHeight
        )
        return this
    }

    /**
     * 是否是拍完照后跳转过来
     */
    fun isFromTakePhoto(isFromTakePhoto: Boolean): IntentBuilder {
        mIntent.putExtra(
            EXTRA_IS_FROM_TAKE_PHOTO,
            isFromTakePhoto
        )
        return this
    }

    /**
     * 是否是圆形裁剪
     */
    fun isCircleCrop(isCircleCrop: Boolean): IntentBuilder {
        mIntent.putExtra(
            EXTRA_IS_CIRCLE_CROP_TAKE_PHOTO,
            isCircleCrop
        )
        return this
    }

    /**
     * 裁剪后图片保存的目录。如果传 null 表示没有裁剪功能，如果不为 null 则具有裁剪功能，
     */
    fun cropFileDir(cropFileDir: File?): IntentBuilder {
        mIntent.putExtra(EXTRA_CROP_FILE_DIR, cropFileDir)
        return this
    }

    fun build(): Intent {
        return mIntent
    }

    init {
        mIntent = Intent(context, PhotoPickerActivity::class.java)
    }
}

// 拍照后照片的存放目录，如果不传递该参数的话就没有拍照功能。
fun takePhotoDir(): File {
    return File(
        Environment.getExternalStorageDirectory(),
        "GANGPhotoPicker"
    ) // /storage/emulated/0/GANGPhotoPicker 目录
}

// 裁剪后照片的存放目录，如果不传递该参数的话就没有裁剪功能。
fun cropPhotoDir(): File? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        //虽然getExternalStoragePublicDirectory方法被淘汰了，但是不影响使用
        return Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES) // /storage/emulated/0/Pictures 目录
    } else {
        return mPhotoContext?.externalCacheDir // /storage/emulated/0/Android/data/cache 目录
    }
}

fun MPhotoHelper(camera: File = takePhotoDir(), crop: File? = cropPhotoDir()): PhotoHelper {
    return PhotoHelper(camera, crop)
}