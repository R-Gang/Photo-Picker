package com.library.kotlin.photo.picker.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.gang.imageloader.kotlin.coil.loadImage
import com.gang.recycler.kotlin.interfaces.ViewOnItemClick
import com.gang.recycler.kotlin.recycleradapter.RecyclerViewHolder
import com.gang.tools.kotlin.dimension.screenWidth
import com.gang.tools.kotlin.utils.getColor
import com.library.kotlin.photo.R
import com.library.kotlin.photo.picker.model.PhotoFolderModel

/**
 * @CreateDate: 2022/9/5 16:25
 * @Author: haoruigang
 * @ClassName: PhotoPickerAdapter
 * @Description: 类作用描述
 */
class PhotoPickerAdapter(
    datas: ArrayList<String>,
    `object`: Any,
    onItemClick1: ViewOnItemClick,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var datas = datas
    var onItemClick: ViewOnItemClick? = onItemClick1
    var mContext: Context = `object` as Context

    private var posCamear: Int = 0
    private var mSelectedPhotos = ArrayList<String>()
    private val mPhotoSize: Int = screenWidth / 6
    private var mTakePhotoEnabled = false

    override fun getItemViewType(position: Int): Int {
        return if (mTakePhotoEnabled && position == 0) {
            R.layout.pp_item_photo_camera
        } else {
            R.layout.pp_item_photo_picker
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == R.layout.pp_item_photo_camera) {
            val res: Int = R.layout.pp_item_photo_camera
            val view = LayoutInflater.from(viewGroup.context).inflate(res, viewGroup, false)
            return PhotoViewHolder(view, onItemClick)
        } else {
            val res: Int = R.layout.pp_item_photo_picker
            val view = LayoutInflater.from(viewGroup.context).inflate(res, viewGroup, false)
            return RecyclerViewHolder(view, onItemClick, null)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        @SuppressLint("RecyclerView") position: Int,
    ) {
        posCamear = position
        val model = datas[position]
        if (holder is RecyclerViewHolder) {
            if (getItemViewType(position) == R.layout.pp_item_photo_picker) {
                val pickerPhoto = holder.getView2<ImageView>(R.id.iv_item_photo_picker_photo)
                pickerPhoto.loadImage(
                    data = model, defaultImgRes = R.mipmap.pp_ic_holder_light, width = mPhotoSize,
                )
                if (mSelectedPhotos.contains(model)) {
                    getColor(R.color.color_4d000000)?.let {
                        pickerPhoto.setColorFilter(it)
                    }
                } else {
                    pickerPhoto.colorFilter = null
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (datas.isEmpty()) 0 else datas.size
    }

    var selectedPhotos: ArrayList<String>
        get() = mSelectedPhotos
        set(selectedPhotos) {
            mSelectedPhotos.clear()
            mSelectedPhotos.addAll(selectedPhotos)
            notifyDataSetChanged()
        }
    val selectedCount: Int
        get() = mSelectedPhotos.size

    fun setPhotoFolderModel(photoFolderModel: PhotoFolderModel) {
        mTakePhotoEnabled = photoFolderModel.isTakePhotoEnabled
        update(photoFolderModel.photos)
    }

    fun update(pdata: ArrayList<String>?) {
        this.datas.clear()
        this.datas.addAll(pdata as ArrayList<String>)
        notifyDataSetChanged()
    }

}