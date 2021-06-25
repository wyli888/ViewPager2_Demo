# ViewPager2_Demo

### 典型的 UI 框架 我们在开发中也经常使用

程序截图如下:

 <img src="https://ae03.alicdn.com/kf/U3292cb58c99d44a3bcf092f9d0fba361h.png" width = "300" height = "600" alt="图片名称" align=center />

之前项目中 也用过这种UI页面 但是写的时候总是会出现内存泄漏 所以就研究了一下 viewPager2 

#[https://square.github.io/leakcanary/getting_started/](内存泄漏检测工具)

使用非常简单 

只需要 在build.gradle 中 添加这么一行就可以了 只会在debug模式下检测 不会影响到 正式环境

```
dependencies {
  // debugImplementation because LeakCanary should only run in debug builds.
  debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.7'
}
```