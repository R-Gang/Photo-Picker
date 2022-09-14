import org.gradle.api.artifacts.dsl.RepositoryHandler
import java.net.URI

/**
 * 作者: hrg
 * 创建时间: 2022-04-07 11:11:48
 * 描述: 新框架依赖项，统一管理，同时也方便其他组件引入
 */

/**
 * App
 */
object App {
    const val applicationId = "com.gang.kotlin.photo"
}

/**
 * Android
 */
object Android {
    const val kotlin = "1.6.10"
    const val gradle = "7.0.3"
    const val compileSdkVersion = 32
    const val minSdkVersion = 26
    const val targetSdkVersion = 32
    const val versionCode = 1
    const val versionName = "1.0"

}

/**
 * 系统库依赖
 * */
object Support {

    const val junit = "junit:junit:4.13.2"
    const val junit_ext = "androidx.test.ext:junit:1.1.2"
    const val espresso_core = "androidx.test.espresso:espresso-core:3.3.0"

    const val documentfile = "androidx.documentfile:documentfile:1.0.1"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val appcompat = "androidx.appcompat:appcompat:1.5.0"
    const val recyclerview = "androidx.recyclerview:recyclerview:1.2.1@aar" // recyclerview

    const val kotlin_stdlib_jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Android.kotlin}"

    const val build_gradle = "com.android.tools.build:gradle:${Android.gradle}"
    const val kotlin_gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Android.kotlin}"
}

/**
 * 第三方库依赖
 * */
object Dependencies {

    const val androidx_multidex = "androidx.multidex:multidex:2.0.1" // Dex处理

    const val AndroidCommon =
        "com.github.R-Gang:Android-Common:v0.1.6-beta.13.0@aar" // 常用类(以上为基类关联依赖)
    const val RecyclerCommon = "com.github.R-Gang:Recycler-Common:1.0.0" // 视图列表扩展
    const val OkhttpUtils = "com.github.R-Gang:Okhttp-Utils:v1.0.0-beta.6" // 网络请求框架
    const val toolsUtils = "com.github.R-Gang:Tools-Utils:v1.0.1-beta.1" // 实用工具类
    const val coil = "io.coil-kt:coil:2.2.0" // Coil图片加载框架
    const val Imageloader = "com.github.R-Gang:Image-loader:v1.0.0-beta.0" // Coil扩展工具

    const val logger = "com.orhanobut:logger:2.2.0" // 日志工具类 logger

    val addRepos: (handler: RepositoryHandler) -> Unit = {
        it.google()
        it.jcenter()
        it.mavenCentral()
        it.maven { url = URI("https://maven.aliyun.com/repository/gradle-plugin") }
        it.maven { url = URI("https://maven.aliyun.com/repository/public") }
        it.maven { url = URI("https://maven.aliyun.com/repository/central") }
        it.maven { url = URI("https://maven.aliyun.com/repository/google") }
        it.maven { url = URI("https://maven.aliyun.com/repository/jcenter") }
        it.maven { url = URI("https://jitpack.io") }
        it.maven { url = URI("https://dl.google.com/dl/android/maven2/") }
        it.maven { url = URI("https://maven.youzanyun.com/repository/maven-releases") }
        it.maven { url = URI("https://maven.google.com") }
        it.maven { url = URI("https://dl.bintray.com/thelasterstar/maven/") }
        it.maven { url = URI("https://dl.bintray.com/kotlin/kotlin-eap") }
    }
}
