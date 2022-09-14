# Photo-Picker

[![](https://jitpack.io/v/R-Gang/Photo-Picker.svg)](https://jitpack.io/#R-Gang/Photo-Picker)

Android 图片选择、系统拍照、系统裁剪、九宫格图片控件

![photo_single1.jpg](https://github.com/R-Gang/Photo-Picker/blob/master/images/photo_single1.jpg)
![photo_choose.jpg](https://github.com/R-Gang/Photo-Picker/blob/master/images/photo_choose.jpg)

## 使用

### 初始化实用工具

```
    initPhotoPicker(this)
    
    initLogger(ToolsConfig.isShowLog)
    LayoutManager.instance?.init(this)
```

### 打开图片选择

```
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
        toPickerTakePhoto(this@MainActivity,
            isCallResult = true) { requestCode: Int, perms: String ->
            val photoPickerIntent: Intent = IntentBuilder(this)
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
```

## Reference

[🏃BGAPhotoPicker-Android🏃](https://github.com/bingoogolapple/BGAPhotoPicker-Android)
