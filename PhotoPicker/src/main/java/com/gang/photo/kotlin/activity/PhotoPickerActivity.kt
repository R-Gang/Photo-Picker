package com.gang.photo.kotlin.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import com.gang.library.base.BaseVBActivity
import com.gang.photo.kotlin.dismissLoadingDialog
import com.gang.photo.kotlin.R
import com.gang.photo.kotlin.databinding.ActivityPhotoPickerBinding
import com.gang.photo.kotlin.picker.adapter.PhotoPickerAdapter
import com.gang.photo.kotlin.picker.model.PhotoFolderModel
import com.gang.photo.kotlin.picker.other.*
import com.gang.photo.kotlin.picker.pw.PhotoFolderPw
import com.gang.photo.kotlin.picker.utils.PhotoHelper
import com.gang.photo.kotlin.showLoadingDialog
import com.gang.recycler.kotlin.interfaces.ViewOnItemClick
import com.gang.recycler.kotlin.itemdecoration.SpaceItemDecoration
import com.gang.recycler.kotlin.manager.LayoutManager
import com.gang.tools.kotlin.utils.LogUtils
import com.gang.tools.kotlin.utils.toastCustom
import com.gang.tools.kotlin.view.marginTopWithStatusBar
import java.io.File
import java.util.*

/**
 * @CreateDate: 16/6/24 下午2:55
 * @ClassName: PhotoPickerActivity
 * @Description: 图片选择界面
 * @Author: haoruigang
 */
open class PhotoPickerActivity : BaseVBActivity<ActivityPhotoPickerBinding>(),
    PhotoAsyncTask.Callback<ArrayList<PhotoFolderModel>>, ViewOnItemClick, View.OnClickListener {

    var TAG = "PhotoPickerActivity"

    var mPhotoHelper: PhotoHelper? = null
    var mCurrentPhotoFolderModel: PhotoFolderModel? = null
    var mPhotoFolderPw: PhotoFolderPw? = null

    /**
     * 是否可以拍照
     */
    var mTakePhotoEnabled = false

    /**
     * 是否拍照裁剪
     */
    var mCropPhotoEnabled = false

    /**
     * 最多选择多少张图片，默认等于1，为单选
     */
    var mMaxChooseCount = 1

    /**
     * 裁剪的宽高
     */
    var cropWidth: Int = 300
    var cropHeight: Int = 300

    /**
     * 裁剪的宽高比
     */
    var cropAspectX: Int = 1
    var cropAspectY: Int = 1

    /**
     * 圆形裁剪
     */
    var isCircleCrop: Boolean = false

    /**
     * 图片目录数据集合
     */
    var mPhotoFolderModels: ArrayList<PhotoFolderModel?>? = arrayListOf()
    private var mPicAdapter: PhotoPickerAdapter? = null

    var mLoadPhotoTask: LoadPhotoTask? = null

    override fun initView(savedInstanceState: Bundle?) {
        dark()
        mBinding?.apply {
            mTitleBar.apply {
                setLeftIvRes(R.mipmap.pp_icon_left_arrow)
                backLFinish(this@PhotoPickerActivity)
                setBgColor(com.gang.tools.R.color.white)
            }
            flTitleBar.marginTopWithStatusBar()
            mTitleTv.setOnClickListener(this@PhotoPickerActivity)
            mArrowIv.setOnClickListener(this@PhotoPickerActivity)
            mTitleTv.setText(R.string.string_pp_all_image)
            if (mCurrentPhotoFolderModel != null) {
                mTitleTv.text = mCurrentPhotoFolderModel?.name
            }

        }
    }

    override fun initData() {
        isCircleCrop = intent.getBooleanExtra(EXTRA_IS_CIRCLE_CROP_TAKE_PHOTO, false)
        cropWidth = intent.getIntExtra(EXTRA_CROP_WIDTH, 300)
        cropHeight = intent.getIntExtra(EXTRA_CROP_HEIGHT, 300)
        cropAspectX = intent.getIntExtra(EXTRA_CROP_ASPECTX, 1)
        cropAspectY = intent.getIntExtra(EXTRA_CROP_ASPECTY, 1)

        processLogic()
    }

    protected fun processLogic() {
        // 获取拍照图片保存目录
        val cameraFileDir = intent.getSerializableExtra(EXTRA_CAMERA_FILE_DIR) as File?
        // 获取裁剪图片保存目录
        val cropFileDir = intent.getSerializableExtra(EXTRA_CROP_FILE_DIR) as File?
        if (cameraFileDir != null) {
            mTakePhotoEnabled = true
            if (cropFileDir != null) {
                // 拍照后是否裁剪
                mCropPhotoEnabled = true
                mPhotoHelper = MPhotoHelper(cameraFileDir, cropFileDir)
            } else {
                mPhotoHelper = MPhotoHelper(cameraFileDir)
            }
        }
        // 获取图片选择的最大张数
        mMaxChooseCount = intent.getIntExtra(EXTRA_MAX_CHOOSE_COUNT, 1)
        if (mMaxChooseCount < 1) {
            mMaxChooseCount = 1
        }

        mPicAdapter = PhotoPickerAdapter(
            arrayListOf(),
            this, this@PhotoPickerActivity
        )
        mBinding?.mContentRv?.apply {
            LayoutManager.instance?.initRecyclerGrid(this, SPAN_COUNT)
            val itemDecoration = SpaceItemDecoration(2, SpaceItemDecoration.GRIDLAYOUT)
            addItemDecoration(itemDecoration)
            adapter = mPicAdapter
            if (intent.getBooleanExtra(EXTRA_PAUSE_ON_SCROLL, false)) {
                addOnScrollListener(
                    RVOnScrollListener(
                        this@PhotoPickerActivity
                    )
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        showLoadingDialog(this)
        mLoadPhotoTask = LoadPhotoTask(this, this, mTakePhotoEnabled).perform()
    }

    private fun showPhotoFolderPw() {
        mBinding?.apply {
            mArrowIv.apply {
                if (mPhotoFolderPw == null) {
                    mPhotoFolderPw = PhotoFolderPw(this@PhotoPickerActivity,
                        mTitleBar.getBgView(), object : PhotoFolderPw.Delegate {
                            override fun onSelectedFolder(position: Int) {
                                reloadPhotos(position)
                            }

                            override fun executeDismissAnim() {
                                ViewCompat.animate(this@apply)
                                    .setDuration(PhotoFolderPw.ANIM_DURATION.toLong())
                                    .rotation(0f)
                                    .start()
                            }
                        })
                }
                mPhotoFolderPw?.setData(mPhotoFolderModels)
                mPhotoFolderPw?.show()
                ViewCompat.animate(this).setDuration(PhotoFolderPw.ANIM_DURATION.toLong())
                    .rotation(90f).start()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
                /* 图片预览
                val photos = ArrayList(listOf(mPhotoHelper?.cameraFilePath))
                val photoPickerPreview = IntentBuilder(this@PhotoPrePickerActivity)
                    .isFromTakePhoto(true)
                    .maxChooseCount(1)
                    .selectedPhotos(photos)
                    .currentPosition(0)
                    .build()
                startActivityForResult(photoPickerPreview, RC_PREVIEW)
            } else if (resultCode == RESULT_CANCELED && requestCode == RC_PREVIEW) {
                data?.apply {
                    if (getIsFromTakePhoto(data)) {
                        // 从拍照预览界面返回，删除之前拍的照片
                        mPhotoHelper?.deleteCameraFile()
                    } else {
                        mPicAdapter?.selectedPhotos = getSelectedPhotos(data)
                    }
                }*/
                val cameraPath = mPhotoHelper?.cameraFilePath
                LogUtils.d(TAG, "拍照图片路径$cameraPath")
                cropPhoto(cameraPath, false)
            } else if (requestCode == REQUEST_CODE_CROP) {
                data?.apply {
                    val cropPath = mPhotoHelper?.cropFilePath
                    LogUtils.d(TAG, "裁剪图片路径$cropPath")
                    // File(cropPath)
                    returnSelectedPhotos(arrayListOf(cropPath))
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        PhotoHelper.onSaveInstanceState(mPhotoHelper, outState)
        putSelectedPhotos(outState, mPicAdapter?.selectedPhotos)
    }

    override fun onRestoreInstanceState(savedState: Bundle) {
        super.onRestoreInstanceState(savedState)
        PhotoHelper.onRestoreInstanceState(mPhotoHelper, savedState)
        mPicAdapter?.selectedPhotos = getSelectedPhotos(savedState)
    }

    override fun setOnItemClickListener(view: View?, position: Int) {
        if (view?.id == R.id.iv_item_photo_camera_camera) {
            // TODO 拍照
            takePhoto()
        } else {
            // TODO 选中照片
            /**
             * 返回已选中的图片集合
             *
             * @param selectedPhotos
             *//*
            val selectedPhoto = mPicAdapter?.selectedPhotos?.get(position)*/
            val selectedPhoto = mCurrentPhotoFolderModel?.photos?.get(position)
            LogUtils.d(TAG, "选中图片路径$selectedPhoto")
            if (mCropPhotoEnabled) {
                cropPhoto(selectedPhoto, true)
            } else {
                returnSelectedPhotos(arrayListOf(selectedPhoto))
            }
        }
    }

    /**
     * 返回已选中的图片集合
     *
     * @param selectedPhotos
     */
    fun returnSelectedPhotos(selectedPhotos: ArrayList<String?>) {
        val intent = Intent()
        intent.putStringArrayListExtra(EXTRA_SELECTED_PHOTOS, selectedPhotos)
        intent.data =
            if (selectedPhotos.size > 0) Uri.fromFile(selectedPhotos[0]?.let { File(it) }) else null
        setResult(RESULT_OK, intent)
        finish()
    }

    /**
     * 拍照
     */
    private fun takePhoto() {
        try {
            startActivityForResult(mPhotoHelper?.takePhotoIntent, REQUEST_CODE_TAKE_PHOTO)
        } catch (e: Exception) {
            toastCustom(getString(R.string.string_pp_not_support_take_photo))
        }
    }

    /**
     * 裁剪
     */
    private fun cropPhoto(selectedPhoto: String?, flag: Boolean) {
        try {
            startActivityForResult(
                mPhotoHelper?.getCropIntent(
                    inputFilePath = selectedPhoto,
                    flag = flag,
                    isCircle = isCircleCrop,
                    width = cropWidth,
                    height = cropHeight,
                    aspectX = cropAspectX,
                    aspectY = cropAspectY,
                ),
                REQUEST_CODE_CROP
            )
        } catch (e: Exception) {
            mPhotoHelper?.deleteCameraFile()
            mPhotoHelper?.deleteCropFile()
            toastCustom(getString(R.string.string_pp_not_support_crop))
            e.printStackTrace()
        }
    }

    private fun reloadPhotos(position: Int) {
        mPhotoFolderModels?.apply {
            if (position < size) {
                mCurrentPhotoFolderModel = this[position]
                if (mBinding?.mTitleTv != null) {
                    mBinding?.mTitleTv?.text = mCurrentPhotoFolderModel?.name
                }
                mCurrentPhotoFolderModel?.apply {
                    mPicAdapter?.setPhotoFolderModel(this)
                }
            }
        }
    }

    override fun onPostExecute(photoFolderModels: ArrayList<PhotoFolderModel>?) {
        dismissLoadingDialog()
        mLoadPhotoTask = null
        mPhotoFolderModels?.apply {
            if (photoFolderModels != null) {
                clear()
                addAll(photoFolderModels)
                mCurrentPhotoFolderModel = this[0]
                this[0]?.let { mPicAdapter?.setPhotoFolderModel(it) }
            }
        }
    }

    override fun onTaskCancelled() {
        dismissLoadingDialog()
        mLoadPhotoTask = null
    }

    private fun cancelLoadPhotoTask() {
        if (mLoadPhotoTask != null) {
            mLoadPhotoTask?.cancelTask()
            mLoadPhotoTask = null
        }
    }

    override fun onDestroy() {
        dismissLoadingDialog()
        cancelLoadPhotoTask()
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        mBinding?.apply {
            when (v) {
                mTitleTv, mArrowIv -> {
                    mPhotoFolderModels?.apply {
                        if (size > 0) {
                            showPhotoFolderPw()
                        }
                    }
                }
            }
        }
    }

}