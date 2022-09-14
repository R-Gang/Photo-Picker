package com.gang.photo.base

import androidx.appcompat.app.AppCompatDialog
import com.gang.library.BaseApp
import com.gang.library.common.user.Config
import com.gang.recycler.kotlin.manager.LayoutManager
import com.gang.tools.kotlin.ToolsConfig
import com.gang.tools.kotlin.utils.initToolsUtils
import com.library.kotlin.photo.initPhotoPicker

/**
 * @ProjectName: Easy-Popup
 * @Package: com.simple.kotlin
 * @ClassName: MyApp
 * @Description: java类作用描述
 * @Author: haoruigang
 * @CreateDate: 2022/3/7 16:30
 */
class MyApp : BaseApp() {
    override fun onCreate() {
        super.onCreate()

        ToolsConfig.isShowLog = true

        initLogger(ToolsConfig.isShowLog)
        initPhotoPicker(this)
        LayoutManager.instance?.init(this)
    }
}