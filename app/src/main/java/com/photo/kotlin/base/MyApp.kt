package com.photo.kotlin.base

import com.gang.library.BaseApp
import com.gang.recycler.kotlin.manager.LayoutManager
import com.gang.tools.kotlin.ToolsConfig
import com.gang.photo.kotlin.initPhotoPicker

/**
 * @ProjectName: Photo-Picker
 * @Package: com.gang.photo.kotlin.base
 * @ClassName: MyApp
 * @Description: java类作用描述
 * @Author: haoruigang
 * @CreateDate: 2022/3/7 16:30
 */
class MyApp : BaseApp() {
    override fun onCreate() {
        super.onCreate()

        ToolsConfig.isShowLog = true

        initPhotoPicker(this)
        initLogger(ToolsConfig.isShowLog)
        LayoutManager.instance?.init(this)
    }
}