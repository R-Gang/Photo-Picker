# Photo-Picker

[![](https://jitpack.io/v/R-Gang/Photo-Picker.svg)](https://jitpack.io/#R-Gang/Photo-Picker)

Android 图片选择、九宫格图片控件

![photo_single1.jpg](https://github.com/R-Gang/Photo-Picker/blob/master/images/photo_single1.jpg)
![photo_choose.jpg](https://github.com/R-Gang/Photo-Picker/blob/master/images/photo_choose.jpg)

## 使用

### 初始化实用工具

```
    initPhotoPicker(this)
```

### 打开图片选择

```
    fun toPickerTakePhoto(context: Context, maxChooseCoun: Int = 1) {
        (context as BasePermissionActivity).requestPermission(Config.REQUEST_CAMERA,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
            context.getString(R.string.string_permission_camear),
            object : PermissionCallBackM {
                override fun onPermissionGrantedM(
                    requestCode: Int,
                    vararg perms: String?,
                ) {
                    LogUtils.e(
                        "onPermissionGrantedM", Thread.currentThread().name,
                    )
                    val photoPickerIntent: Intent = IntentBuilder(context)
                        // 拍照后照片的存放目录，如果不传递该参数的话则不开启图库里的拍照功能。
                        .cameraFileDir(takePhotoDir())
                        .maxChooseCount(maxChooseCoun) // 图片选择张数的最大值
                        .build()
                    context.startActivity(photoPickerIntent)
    
                }
    
                override fun onPermissionDeniedM(
                    requestCode: Int,
                    vararg perms: String?,
                ) {
                    LogUtils.e(context.toString(), "TODO: INTERNET Denied")
                }
            })
```

## Reference

[🏃BGAPhotoPicker-Android🏃](https://github.com/bingoogolapple/BGAPhotoPicker-Android)
