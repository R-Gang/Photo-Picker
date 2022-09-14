package com.library.kotlin.photo.picker.model;

import java.util.ArrayList;

/**
 * @CreateDate: 2022/9/6 09:58
 * @Author: haoruigang
 * @ClassName: BGAPhotoFolderModel
 * @Description: 类作用描述
 */
public class PhotoFolderModel {
    public String name;
    public String coverPath;
    private ArrayList<String> mPhotos = new ArrayList<>();
    private boolean mTakePhotoEnabled;

    public PhotoFolderModel(boolean takePhotoEnabled) {
        mTakePhotoEnabled = takePhotoEnabled;
        if (takePhotoEnabled) {
            // 拍照
            mPhotos.add("");
        }
    }

    public PhotoFolderModel(String name, String coverPath) {
        this.name = name;
        this.coverPath = coverPath;
    }

    public boolean isTakePhotoEnabled() {
        return mTakePhotoEnabled;
    }

    public void addLastPhoto(String photoPath) {
        mPhotos.add(photoPath);
    }

    public ArrayList<String> getPhotos() {
        return mPhotos;
    }

    public int getCount() {
        return mTakePhotoEnabled ? mPhotos.size() - 1 : mPhotos.size();
    }
}