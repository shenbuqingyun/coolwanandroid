所有网络数据来源：鸿洋大神玩Android网站https://www.wanandroid.com/blog/show/2
-------
实现方式：非RxJava实现，用MVC+OKGO实现，主要用于测试网络数据请求展示刷新和一些第三方库的练习。
-------
#### 效果如下：<br><br>
![img](https://github.com/shenbuqingyun/coolwanandroid/blob/master/GIF.gif)<br>  
#### 涉及的第三方库：感谢开源<br>
    // ui<br>
        implementation 'com.youth.banner:banner:1.4.10' // 首页横幅<br>
        implementation 'com.scwang.smartrefresh:SmartRefreshLayout:1.0.5.1' // 下拉刷新 上拉加载<br>
        implementation 'com.scwang.smartrefresh:SmartRefreshHeader:1.0.3'<br>
        implementation 'com.ashokvarma.android:bottom-navigation-bar:2.1.0' // 底部导航栏<br>
    // 动画<br>
        implementation 'com.github.ibrahimsn98:android-particles:1.6' // 炫酷的粒子背景动画<br>
        implementation 'com.airbnb.android:lottie:2.2.5' // lottie动画库<br>
    // util<br>
        implementation 'com.blankj:utilcode:1.16.3' // 强大的Utils<br>
        implementation 'com.just.agentweb:agentweb:4.0.2' // 展示网页内容<br>
        implementation 'com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.40' // 强大的RecyclerView适配器<br>
        implementation 'com.lzy.net:okgo:3.0.4' // 网络请求<br>
        implementation 'com.alibaba:fastjson:1.1.67.android' // 解析json数据<br>
        implementation 'com.github.bumptech.glide:glide:4.8.0' // 加载图片<br>
        implementation 'com.jakewharton:butterknife:8.4.0' // 注入框架<br>
        annotationProcessor 'com.jakewharton:butterknife-compiler:8.4.0'<br>

