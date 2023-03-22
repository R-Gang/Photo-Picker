package com.gang.photo.kotlin.picker.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.gang.imageloader.kotlin.coil.loadImage
import com.gang.photo.kotlin.R
import com.gang.photo.kotlin.picker.model.PhotoFolderModel
import com.gang.recycler.kotlin.interfaces.ViewOnItemClick
import com.gang.recycler.kotlin.recycleradapter.RecyclerAdapter
import com.gang.recycler.kotlin.recycleradapter.RecyclerViewHolder
import com.gang.tools.kotlin.dimension.screenWidth
import com.gang.tools.kotlin.utils.getDrawable

/**
 * @CreateDate:     2022/9/6 17:48
 * @Author:         haoruigang
 * @ClassName:      FolderAdapter
 * @Description:    类作用描述
 */
class FolderAdapter(datas: ArrayList<*>, `object`: Any, onItemClick1: ViewOnItemClick) :
    RecyclerAdapter(datas, `object`, onItemClick1) {

    private var mImageSize: Int = screenWidth / 10


    override val layoutResId: Int
        get() = R.layout.pp_item_photo_folder

    override fun convert(holder: RecyclerViewHolder, position: Int, context: Context) {
        val model = datas[position] as PhotoFolderModel
        holder.getView2<TextView>(R.id.tv_item_photo_folder_name).text = model.name
        holder.getView2<TextView>(R.id.tv_item_photo_folder_count).text = model.count.toString()
        holder.getView2<ImageView>(R.id.iv_item_photo_folder_photo).loadImage(
            data = model.coverPath,
            width = mImageSize,
            height = mImageSize,
            defaultImg = getDrawable(R.mipmap.pp_ic_holder_light),
        )
    }

}