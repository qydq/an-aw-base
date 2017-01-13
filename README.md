# an-aw-base

* 项目的主要功能是作为an框架的基类（基础的an-base）的仓库；你可以借助github，jitpack ，bintrary快速集成。

* 框架来源于本人an-maven-base框架，an-aw-base框架去除了an-maven-base框架不常用的功能，更轻量提升了性能。

本项目地址：https://github.com/qydq/an-aw-base

**使用介绍：如下简介或知乎，或个人网易博客。**

>知乎主页：https://www.zhihu.com/people/qydq

>博客主页：https://bgwan.blog.163.com


**如有使用问题请发送电子邮件。**

>邮件地址： staryumou@163.com  /  qyddai@gmail.com


**特别说明**

	
>创建时间 <------2016年09月17日------->
	
>2016年09月17日;最近修改时间：2017年01月13日。

########
	
**目录**
	
1.前言（包含该项目主要使用方法，功能的简单概述;运行配置,可选）。

2.实现效果（如果没有可以省略，建议包含）。

3.使用方法（代码）。

4.重要知识点（总结，思考）。

5.联系作者。


# [![](https://jitpack.io/v/qydq/an-aw-base.svg)](https://jitpack.io/#qydq/an-aw-base)       <a href='https://bintray.com/qydq/an/an-aw-base?source=watch' alt='Get automatic notifications about new "an-aw-base" versions'><img src='https://www.bintray.com/docs/images/bintray_badge_color.png'></a>

#######

# 1. 前言

**使用Gradle构建时添加以下依赖即可** *（注：也可以使用之前的低稳定版本）*

加入依赖之前Step 1（在你的根build.gradle文件中增加如下代码。）

```groovy
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

加入以后Step 2:

`compile 'com.github.qydq:an-aw-base:0.1.1'`

或者加入：

`compile 'cn.android.sunst:an-base:1.0.1'`

使用整套an框架则加入另一个依赖，具体请参考url = https://github.com/qydq/an


**使用建议**

>使用建议继承SuperActivity，SuperFragment，BasePresenter，BaseView。

>使用建议在AndroidManifest.xml中加入android:theme="@style/AnAppTheme"/||/NoBarTheme(覆盖写法)"`。

>使用建议在编译的build的时候注意an-aw-base需要在API=19或以上版本编译。

```groovy
aN框架支持android 4.4+使用，兼容性测试通过，华为Android4.4 ，小米Android5.1，三星Android5.1。
```
##samples apk demo下载参考

[点我下载](https://github.com/qydq/an-aw-base/raw/master/app/app-release.apk)

**Tips**
---

+ an框架提供了SuperActivity,SuperFragment,BasePresenter,BaseView等基类符合Google Material Design。

+ an框架提供了网络请求基于xutils模块的封装，http实现XHttps。

```groovy
   i.   HTTP实现利用了XHttps提供了post,get,upLoadFile,downLoadFile...`

   ii.  集成以上则可以使用注解功能，可以参考xUtils3开源项目。`

   iii. 提供便捷XCallBack ,XParseResponse ,XProgressCallBack操作。`
```
+ an框架包含了许多实用的工具类，像MD5加密，数据校验，夜间模式切换等。

+ an框架提供基本weight,如WSlidingDeleteListView,WToggleButton、提供了recyclerview,refreshlayout,swipelayout系列。

+ an框架加入了两种夜间模式的功能。

+ an框架提供了快速监听网络变化的方法。

+ an框架后期功能正在完善中。

---------

# 2. 实现效果

+ **项目部分效果截图。如图**

 <img src="https://github.com/qydq/an-aw-base/blob/master/screen/demo_main.png" width=280 height=480 />   <img src="https://github.com/qydq/an-aw-base/blob/master/screen/demo_recyclerview.png" width=280 height=480 />  <img src="https://github.com/qydq/an-aw-base/blob/master/screen/demo_littlertrick.png" width=280 height=480 />

+ **an框架提供通用的标题栏头集成（实际使用时背景颜色CommMainBg）。如图**

（1）普通状态栏参考(anPb)<br>

 ![](https://github.com/qydq/an-aw-base/blob/master/screen/base_headview_standard.png)
 
 `新增，android:transitionName="anllbacktrans"，android:transitionName="anllrighttrans"`

（2）复杂状态栏参考(anPb)<br>

 ![](https://github.com/qydq/an-aw-base/blob/master/screen/base_headview_standard_complex.png)

 `新增，android:transitionName="anllbacktrans"，android:transitionName="anllrighttrans"， android:transitionName="anllrrighttrans"`
 
备注：新增（不影响使用）transition支持MD动画（[Transition用法请点击-一片枫叶_刘超](http://blog.csdn.net/qq_23547831/article/details/51821159)）

---------

# 3. 使用方法（代码）

[简单帮助文档-aN框架API指导参考](https://github.com/qydq/an-aw-base/blob/master/an-base/ApiGuide.md)

[利用aN框架快速开发android应用程序基本使用方法](https://zhuanlan.zhihu.com/p/24273705?refer=sunst)

[如何使用An框架提供的WToggleButton实现一个漂亮的切换按钮？](https://zhuanlan.zhihu.com/p/24275861?refer=sunst)

[利用an框架提供的WSlidingDeleteListView仿QQ滑动删除。](https://zhuanlan.zhihu.com/p/24408002?refer=sunst)

[利用an框架提供的recyclerview系列实现下拉刷新、分页加载数据](https://github.com/qydq/an-aw-base/blob/master/app/README.md)

--------

+ **An框架命名规范**

命名规范是一个项目中非常重要的部分，任何事情不规范不仅别人看不懂你，自己都不了解自己。<br>
[An框架命名规范参考](https://zhuanlan.zhihu.com/p/24155927)
```groovy
		/*
		* 类的声明次序依次是：TAG类--View控件--静态成员变量--普通类成员变量
		* public 在最前方的位置。
		* private 在public的位置后面。
		* */
```

+ **An框架状态栏**

[利用aN框架快速开发android应用程序基本使用方法](https://zhuanlan.zhihu.com/p/24273705?refer=sunst)

<font color=#0099ff size=12 face="黑体">备注：aN框架提供通用状态栏可修改改base_drawable_backarrow，base_drawable_backarrow_click图片；文字可修改CommColorGray，CommColorWhite；标题颜色CommTxtMainColor可修改，标题大一字体大小CommDimenBarTitleMax，*修改的时候直接替换即可*。</font>

（1）普通状态栏参考<br>

<font>Tips: 左右文本大小CommDimenBarTitleSmall可修改;</font>

		<include
        android:id="@+id/includeHead"
        layout="@layout/base_headview_standard"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
		
（2）复杂状态栏参考<br>

<font color=#0099ff size=12 face="黑体">Tips：aN框架提供复杂状态栏可修改从左到右图片base_drawable_art，base_drawable_art_click，base_drawable_share，base_drawable_share_click。文本大小，CommDimenTxtSSmall，可修改。;</font>

		<include
        android:id="@+id/includeHead"
        layout="@layout/base_headview_standard_complex"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
		
+ **An通用属性值部分参考**

（1）Style通用参考

AnTvContentStyle 标准文本内容风格。

AnTvTitleStyle 标准文本标题风格。

NoBarTheme 无标题栏。提供（[覆盖an框架默认的AnAppTheme透明效果的作用](https://github.com/qydq/an-aw-base/issues/6)）。

AnAppTheme an框架默认的风格。

### 使用方法

+ **An框架夜间模式**

（1）简单夜间模式使用<br>

<font color=#0099ff size=12 face="黑体">备注：aN框架提供了数据保存Sp，直接sp.edit即可得到Editor对象；如editor = sp.edit();</font>

	//用来保存皮肤切换模式的sp
	if (sp.getBoolean("isNight", false)) {
        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(mContext,R.drawable.yy_drawable_bgday_shape));
        tvChangModel.setText("现在是白天，点击切换晚上");
        editor.putBoolean("isNight", false);
        } else {
        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(mContext,R.drawable.yy_drawable_bgnigt_shape));
        tvChangModel.setText("现在是晚上，点击切换白天");
        editor.putBoolean("isNight", true);
        }
（2）仿知乎简书夜间模式切换实现套路<br>

[Android 利用an框架快速实现夜间模式的两种套路](https://zhuanlan.zhihu.com/p/22520818?refer=qyddai)

[Android 利用an框架快速实现夜间模式的两种套路-加强版](https://zhuanlan.zhihu.com/p/24269723)

+ **An框架提供的WToggleButton的使用参考** *（源码中配合夜间模式使用）*

[如何使用An框架提供的WToggleButton实现一个漂亮的切换按钮？](https://zhuanlan.zhihu.com/p/24275861?refer=sunst)

<font color=#0099ff size=12 face="黑体">备注：Default Size:width=50dp,height=30dp.；如editor = sp.edit();</font>

		xmlns:toggle="http://schemas.android.com/apk/res-auto"

		<com.an.base.view.widget.WToggleButton
            android:layout_width="60dp"
            android:layout_height="30dp"
            android:layout_marginTop="@dimen/CommDimenMargin"
            toggle:tbBorderWidth="2dp"
            toggle:tbOffBorderColor="#000"
            toggle:tbOffColor="#ddd"
            toggle:tbOnColor="#f00"
            toggle:tbSpotColor="#00f" />
			
		private WToggleButton toggleBtn;
		
		    //切换开关
		toggleBtn.toggle();
		//切换无动画
		toggleBtn.toggle(false);
		//开关切换事件
		toggleBtn.setOnToggleChanged(new OnToggleChanged(){
        @Override
        public void onToggle(boolean on) {
        }
		});

		toggleBtn.setToggleOn();
		toggleBtn.setToggleOff();
		//无动画切换
		toggleBtn.setToggleOn(false);
		toggleBtn.setToggleOff(false);

		//禁用动画
		toggleBtn.setAnimate(false);
			

+ **网络状态监听**

[网络状态监听-MainActivity.java-onNetChange](https://raw.githubusercontent.com/qydq/an-aw-base/master/app/src/main/java/com/qyddai/an_aw_base/MainActivity.java)

+ **DUtilsDialog使用**

[Android 利用an框架快速加入Dialog对话框示例代码](https://zhuanlan.zhihu.com/p/24146818)

//android:background="@color/CommColorDialog"<br>
[An框架示例-MainActivity.java对应普通dialog](https://raw.githubusercontent.com/qydq/an-aw-base/master/app/src/main/java/com/qyddai/an_aw_base/MainActivity.java)

//android:background="@drawable/yy_shape_dialog_progress"<br>
[An框架示例-TestActivity.java对应ProgressDialog](https://github.com/qydq/an-aw-base/blob/master/app/src/main/java/com/qyddai/an_aw_base/TestActivity.java)

+ **XHttps网络请求使用方法**

[Android利用aN框架快速实现网络请求(含上传下载文件）高级-加强版](https://zhuanlan.zhihu.com/p/24132012)

[aN框架示例-XHttpsActivity.java（含注解，ContentVIew，）](https://raw.githubusercontent.com/qydq/an-aw-base/master/app/src/main/java/com/qyddai/an_aw_base/XHttpsActivity.java)

+ **选择器**
	
|选择器名称|背景说明|边框|圆角|
|:---------|:-------|:---|---:|
|drawable_selector_md|CommColorRipple<br>CommColorTransparent<br>CommColorTransparentClicked|无|无|
|drawable_selector_md_oppose|CommColorOpposeRipple<br>CommColorOpposeClicked<br>CommColorOppose|无|无|
|drawable_selector_md_shape|CommColorRipple<br>CommColorTransparent<br>CommColorTransparentClicked|CommColorBorder<br>CommColorBorderClicked|2dp|
|drawable_selector_md_shape_oppose|CommColorOppose<br>CommColorOpposeClicked<br>CommColorOpposeRipple|CommColorBorder<br>CommColorBorderClicked|2dp|
|drawable_selector_txt|CommColorGray<br>CommColorWhite|无|无|
|drawable_selector_txt_oppose|CommColorWhite<br>CommColorGray|无|无|
|drawable_selector_ll_shape|CommEndColor<br>CommStartColor<br>CommMainBgClicked<br>CommMainBg|CommColorLineClicked<br>CommColorLine|2dp|

---------

# 4. 重要知识点。

+ **[如何使用MVP架构搭建Android应用程序-(demo+解析)-极简版](https://zhuanlan.zhihu.com/p/24452403)**

+ **Android MVP架构示例项目解析-加强版**

+ **[列表控件借鉴使用swipelayout系列](https://github.com/qydq/an-aw-base/blob/master/an-base/README.md)**
		
---------

# 5. 联系作者。

Athor IP：sunshuntao（qydq）（莳萝花）。

Email：qyddai@gmail.com。

欢迎大家使用aN快速开发框架。
If it doesn't work, please send me a email, qyddai@gmail.com
An框架可能存在一些缺陷，有问题欢迎大家反馈，收到邮件我会第一次时间回复处理。

Or

Import the library, then add it to your /settings.gradle and /app/build.gradle, if you don't know how to do it, you can read my blog for help.
http://drakeet.me/android-studio


# Thanks

1.[xUtils3-- wyouflf](https://github.com/wyouflf/xUtils3)

2.[严振杰CSDN](http://my.csdn.net/yanzhenjie1003)

3.[一片枫叶_刘超CSDN](http://blog.csdn.net/qq_23547831/article/details/46423599)

4.[BGARefreshLayout-Android-refreshlayout系列](https://github.com/bingoogolapple/BGARefreshLayout-Android/wiki/Advanced)


#打赏

觉得本框架对你有帮助，不妨打赏赞助我一下，让我有动力走的更远。

![](http://img0.ph.126.net/1gDvw3-W3Gw5X0voBIu5zA==/6632106804188340891.png)

<img src="http://img0.ph.126.net/1gDvw3-W3Gw5X0voBIu5zA==/6632106804188340891.png" width="100" />

---------

#### 后记：


## an-base > module /build.gradle

		apply plugin: 'com.android.library'
		//配置jitpack
		apply plugin: 'com.github.dcendents.android-maven'
		group = 'com.github.qydq'
		dependencies {
		compile fileTree(dir: 'libs', include: ['*.jar'])
		androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
        exclude group: 'com.android.support', module: 'support-annotations'
		})
		compile 'com.android.support:appcompat-v7:24.2.0'
		testCompile 'junit:junit:4.12'
		compile 'org.xutils:xutils:3.3.36'
		compile 'com.google.code.gson:gson:2.7'
		compile 'com.android.support:design:24.2.0'<!--android md新特性的引用-->
		compile 'com.android.support:recyclerview-v7:24.2.0'
		//apply from: 'https://raw.githubusercontent.com/blundell/release-android-library/master/android-release-aar.gradle'
		
## an-aw-base > project /build.gradle

		buildscript {
		repositories {
			jcenter()
		}
		dependencies {
			classpath 'com.android.tools.build:gradle:2.2.0-rc2'
			// jitpack发布github
			classpath 'com.github.dcendents:android-maven-gradle-plugin:1.5'
			// NOTE: Do not place your application dependencies here; they belong
			// in the individual module build.gradle files
		}
		}·
		
## 权限相关。an-mave-base /AndroidManifest.xml	

    <!-- 网络相关权限 -->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <!-- WIFI状态监听相关权限-->
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <!-- 往SDCard写入数据权限 -->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <!-- 往SDCard读取数据权限 -->
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <!-- 在SDCard 的挂载权限 -->
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
		
		
## 编译及相关SDK配置。

		android {
			compileSdkVersion 24
			buildToolsVersion "24.0.2"

			defaultConfig {
			minSdkVersion 19
			targetSdkVersion 24
			versionCode 1
			versionName "1.0"

			testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"

		}
		
		说明这里面：在调用的Activity时应该super.onCreate(savedInstanceState);，还有setBackUp的属性，建议不要在低版本用高版本的内容。
		
		
## 具体功能的使用可以参考release的日志文件，或者在以后会在个人知乎做一些讲解。


[An框架注解参考](https://raw.githubusercontent.com/qydq/an-aw-base/master/app/src/main/java/com/qyddai/an_aw_base/AnotationActivity.java)

		//以下为headview_standard.xml
		@ViewInject(R.id.anLlBack)
		private LinearLayout anLlBack;
		@ViewInject(R.id.anTvBack)
		private TextView anTvBack;
		@ViewInject(R.id.anPb)
		private ProgressBar anPb;
		@ViewInject(R.id.anTvTitle)
		private TextView anTvTitle;

		@ViewInject(R.id.anLlRight)
		private LinearLayout anLlRight;
		@ViewInject(R.id.anTvRight)
		private TextView anTvRight;
		@ViewInject(R.id.anIvRight)
		private ImageView anIvRight;

		//分割线0000---complex
		@ViewInject(R.id.anLlRRight)
		private LinearLayout anLlRRight;
		@ViewInject(R.id.anTvRRight)
		private TextView anTvRRight;
		@ViewInject(R.id.anIvRRight)
		private ImageView anIvRRight;
		
		//FINDVIEWBY ID
		anLlBack = (LinearLayout) findViewById(R.id.anLlBack);
        anTvBack = (TextView) findViewById(R.id.anTvBack);
        anPb = (ProgressBar) findViewById(R.id.anPb);
        anTvTitle = (TextView) findViewById(R.id.anTvTitle);

        anLlRight = (LinearLayout) findViewById(R.id.anLlRight);
        anTvRight = (TextView) findViewById(R.id.anTvRight);
        anIvRight = (ImageView) findViewById(R.id.anIvRight);

        anLlRRight = (LinearLayout) findViewById(R.id.anLlRRight);
        anTvRRight = (TextView) findViewById(R.id.anTvRRight);
        anIvRRight = (ImageView) findViewById(R.id.anIvRRight);

## 2016/12/27

`compile 'com.github.qydq:an-aw-base:0.1.4'`

an-aw-base更新日志，会在这里面做一次纪录，当前an-aw-base

SlideBackActiivity,修改为SwipeBackActivity

SlideCloseActivity,修改为SwipeCloseActivity

添加SwipeFinishActivity（通用。）
WIoScrollView,

WSlideFrameLayout,修改为WSwipeCloseFrameLayout

添加WSwipeFinishFrameLayout

yy_drawable_slidingclose_shadow修改为yy_drawable_swipe_shadow.xml

修改为SwipeCloseActivity继承BaseActivity，修复bug.可用其它属性。但设置间隔。

添加一个base_swipe_finish Layout布局。

完成SwipeCloseActivity，SwipeFinishActivity使用参考例子，详细见app，src目录。

SwipeBackActivity //为单独使用。不能使用an框架其它功能。

SwipeCloseActivity //集成baseActivity可以使用an框架所有功能。（内部可修改为SwipeFinishActivity）

SwipeFinishActivity //集成baseActivity，可以使用an框架所有功能。（通用，推荐使用。）

## 2017/01/06

`compile 'com.github.qydq:an-aw-base:0.1.5'`

（1）完善

解决了WIoScrollView和ListView嵌套时滑动冲突的解决办法，[详见issue](https://github.com/qydq/an-aw-base/issues)。

完善WIosCrollView中setListViewHeightBasedOnChildren方法防止listView只显示部分，[详见API指导参考](https://github.com/qydq/an-aw-base/blob/master/an-base/ApiGuide.md)

完善WIosCrollView使用参考，见api，sst_coordinnatorlayout_wtiocrollview.xml … 

完善DataService中增加了验证字符串是否合法islegalInput，[详见API指导参考](https://github.com/qydq/an-aw-base/blob/master/an-base/ApiGuide.md)

更新了ApiGuide中指导和README。

新增布局文件加入了layout-v21，支持android高版本head切换的效果,[详见API指导参考](https://github.com/qydq/an-aw-base/blob/master/an-base/ApiGuide.md)

完善了DUtilsBitmap工具类，旨在解决android手机OOM的处理方法，[详见API指导参考](https://github.com/qydq/an-aw-base/blob/master/an-base/ApiGuide.md)

（2）增加

新增CaptureHelper拍照辅助工具类，向下支持华为，小米低版本兼容性问题[samples参考SlidingFinishDetailActivity](https://raw.githubusercontent.com/qydq/an-aw-base/master/app/src/main/java/com/qyddai/an_aw_base/SlidingFinishDetailActivity.java)。

ListView ,Recycleview的待更新状态，md的新特性即将在下一个版本体现出来。（代码已上传）


`compile 'com.github.qydq:an-aw-base:0.1.5'`应该与maven-jcenter对应起来了。


## 2017/01/12

`compile 'com.github.qydq:an-aw-base:0.2'`

（1）完善

去除框架无用的代码，本着解耦的原则减少代码体积。

完善an框架sample。

完善swipelayou系列。

（2）增加

新增2+1（上个版本的swipelayout包系列）=3种类别的recyclerview；

支持下拉刷新，自动加载更多，动画特性的列表刷新控件布局，带你实现完美动画特效，仿IOS的侧滑删除（上个版本也有）。

sample主要参考，refreshlayout,swipelayout,recyclerview包下面。

tips:针对recyclerview添加了如下辅助，ListBaseAdapter,MultiAdapter,MultiItemEntity,SuperViewHolder。

具体的使用会在后面更新两篇博客。