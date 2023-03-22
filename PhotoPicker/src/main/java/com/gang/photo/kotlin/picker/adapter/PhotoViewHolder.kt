package com.gang.photo.kotlin.picker.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.gang.photo.kotlin.R
import com.gang.recycler.kotlin.interfaces.ViewOnItemClick


class PhotoViewHolder(
    var view: View,
    onItemClick: ViewOnItemClick?,
) : RecyclerView.ViewHolder(view), View.OnClickListener {

    var onItemClick: ViewOnItemClick? = onItemClick
    private var photoCamera: ImageView

    override fun onClick(v: View) {
        onItemClick?.setOnItemClickListener(v, position)
    }

    fun <T : View> getView2(id: Int): T {
        return view.findViewById(id) as T
    }

    init {
        view.setOnClickListener(this)
    }

    init {
        photoCamera = getView2(R.id.iv_item_photo_camera_camera)
        photoCamera.setOnClickListener(this)
    }
}