package com.library.kotlin.photo.picker.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import androidx.core.content.FileProvider
import com.gang.tools.kotlin.utils.LogUtils
import com.library.kotlin.photo.mPhotoContext
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * @CreateDate:     2022/9/7 17:40
 * @Author:         haoruigang
 * @ClassName:      PhotoHelper
 * @Description:    类作用描述
 */
class PhotoHelper(private val mCameraFileDir: File?, private val mCropFileDir: File?) {

    var cameraFilePath: String? = null
        private set
    var cropFilePath: String? = null
        private set

    constructor(mCameraFileDir: File?) : this(mCameraFileDir, null)

    /**
     * 创建用于保存拍照生成的图片文件
     *
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun createCameraFile(): File {
        val captureFile = File.createTempFile(
            "Capture_" + PHOTO_NAME_POSTFIX_SDF.format(Date()),
            ".jpg",
            mCameraFileDir)
        cameraFilePath = captureFile.absolutePath
        return captureFile
    }

    /**
     * 创建用于保存裁剪生成的图片文件
     *
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun createCropFile(): File {
        val cropFile = File.createTempFile(
            "Crop_" + PHOTO_NAME_POSTFIX_SDF.format(Date()),
            ".jpg",
            mCropFileDir)
        cropFilePath = cropFile.absolutePath
        return cropFile
    }

    /**
     * 获取从系统相册选图片意图
     *
     * @return
     */
    val chooseSystemGalleryIntent: Intent
        get() {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
            return intent
        }

    /**
     * 获取拍照意图
     *
     * @return
     * @throws IOException
     */
    @get:Throws(IOException::class)
    val takePhotoIntent: Intent
        get() {
            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                takePhotoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, createFileUri(createCameraFile()))
            return takePhotoIntent
        }

    /**
     * 刷新图库
     */
    fun refreshGallery() {
        if (!TextUtils.isEmpty(cameraFilePath)) {
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            mediaScanIntent.data = createFileUri(File(cameraFilePath.toString()))
            mPhotoContext?.sendBroadcast(mediaScanIntent)
            cameraFilePath = null
        }
    }

    /**
     * 删除拍摄的照片
     */
    fun deleteCameraFile() {
        deleteFile(cameraFilePath)
        cameraFilePath = null
    }

    /**
     * 删除裁剪的照片
     */
    fun deleteCropFile() {
        deleteFile(cropFilePath)
        cropFilePath = null
    }

    private fun deleteFile(filePath: String?) {
        if (!TextUtils.isEmpty(filePath)) {
            val photoFile = File(filePath)
            photoFile.deleteOnExit()
        }
    }


    /**
     * 获取裁剪图片的 intent
     * @param inputFilePath 要裁剪的图片
     * @param flag 默认true:相册中选取裁剪，false:相机拍照后裁剪
     * @param isCrop 显示的VIEW是否可裁剪
     * @param isCircle 是否圆形裁剪
     * @param isScale 是否缩放
     * @param returnData
     * @return
     */
    @SuppressLint("QueryPermissionsNeeded")
    @Throws(IOException::class)
    fun getCropIntent(
        inputFilePath: String?,
        flag: Boolean = true,
        isCrop: Boolean = true,
        isCircle: Boolean = false,
        isScale: Boolean = false,
        returnData: Boolean = false,
        noFaceDetection: Boolean = true,
        /*width: Int,
        height: Int,*/
        aspectX: Int = 1,
        aspectY: Int = 1,
    ): Intent {
        //要裁剪的图片Uri
        val fileUri: Uri? = createFileUri(File(inputFilePath))
        LogUtils.d(TAG, "裁剪 的 fileUri=$fileUri")
        //裁剪后输出的file
        val cropUri = if (flag) {
            //相册选图片裁剪：注意到此处使用的file:// uri类型.
            Uri.fromFile(createCropFile())
        } else {
            //相机拍完后裁剪
            createFileUri(createCropFile())
        }
        LogUtils.d(TAG, "裁剪 输出 cropUri=$cropUri")

        val intent = Intent("com.android.camera.action.CROP")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        intent.setDataAndType(fileUri, "image/*") //要裁剪的Uri
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", isCrop)
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", aspectX)
        intent.putExtra("aspectY", aspectY)
        // outputX outputY 是裁剪图片宽高 android 11以上需要注调
        /*intent.putExtra("outputX", width)
        intent.putExtra("outputY", height)*/
        intent.putExtra("circleCrop", isCircle) // 圆形裁剪
        intent.putExtra("scale", isScale)  // 缩放
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri) // 裁剪输出的Uri
        intent.putExtra("return-data", returnData) //是否把剪切后的图片通过data返回
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()) // 图片格式
        intent.putExtra("noFaceDetection", noFaceDetection) // 取消人脸识别

        mPhotoContext?.apply {
            //重要！！！添加权限，不然裁剪完后报 “保存时发生错误，保存失败” （我的小米10.0系统是这样）
            val resInfoList: List<ResolveInfo> =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            for (resolveInfo in resInfoList) {
                //我用的小米手机 packageName 得到的是：com.miui.gallery
                val packageName = resolveInfo.activityInfo.packageName
                grantUriPermission(packageName,
                    cropUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                //注意不是 getPackageName()！！ getPackageName()得到的是app的包名
                //grantUriPermission(getPackageName(), cropUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            intent.resolveActivity(packageManager)
        }
        return intent
    }

    companion object {
        private const val STATE_CAMERA_FILE_PATH = "STATE_CAMERA_FILE_PATH"
        private const val STATE_CROP_FILE_PATH = "STATE_CROP_FILE_PATH"
        private val PHOTO_NAME_POSTFIX_SDF =
            SimpleDateFormat("yyyy-MM-dd_HH-mm_ss", Locale.getDefault())

        /**
         * 根据文件创建 Uri
         *
         * @param file
         * @return
         */
        fun createFileUri(file: File?): Uri? {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                mPhotoContext?.apply {
                    val authority: String =
                        applicationInfo?.packageName + ".gang_photo_picker.file_provider"
                    return file?.let { FileProvider.getUriForFile(this, authority, it) }
                }
            }
            return Uri.fromFile(file)
        }

        /**
         * 从 Uri 中获取文件路劲
         *
         * @param uri
         * @return
         */
        fun getFilePathFromUri(uri: Uri?): String? {
            if (uri == null) {
                return null
            }
            val scheme = uri.scheme
            var filePath: String? = null
            if (TextUtils.isEmpty(scheme) || TextUtils.equals(ContentResolver.SCHEME_FILE,
                    scheme)
            ) {
                filePath = uri.path
            } else if (TextUtils.equals(ContentResolver.SCHEME_CONTENT, scheme)) {
                val filePathColumn = arrayOf(MediaStore.MediaColumns.DATA)
                val cursor = mPhotoContext?.contentResolver?.query(uri,
                    filePathColumn,
                    null,
                    null,
                    null)
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        if (columnIndex > -1) {
                            filePath = cursor.getString(columnIndex)
                        }
                    }
                    cursor.close()
                }
            }
            return filePath
        }

        fun onRestoreInstanceState(photoHelper: PhotoHelper?, savedInstanceState: Bundle?) {
            if (photoHelper != null && savedInstanceState != null) {
                photoHelper.cameraFilePath = savedInstanceState.getString(STATE_CAMERA_FILE_PATH)
                photoHelper.cropFilePath = savedInstanceState.getString(STATE_CROP_FILE_PATH)
            }
        }

        fun onSaveInstanceState(photoHelper: PhotoHelper?, savedInstanceState: Bundle?) {
            if (photoHelper != null && savedInstanceState != null) {
                savedInstanceState.putString(STATE_CAMERA_FILE_PATH, photoHelper.cameraFilePath)
                savedInstanceState.putString(STATE_CROP_FILE_PATH, photoHelper.cropFilePath)
            }
        }
    }

    /**
     * @param cameraFileDir 拍照后图片保存的目录
     */
    init {
        if (mCameraFileDir != null && !mCameraFileDir.exists()) {
            mCameraFileDir.mkdirs() // 拍照路径
        }
        if (mCropFileDir != null && !mCropFileDir.exists()) {
            mCropFileDir.mkdirs() // 截图路径
        }
    }
}