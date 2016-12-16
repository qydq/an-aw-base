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
	
>2016年09月17日;最近修改时间：2016年12月16日。

########
	
**目录**
	
1.前言（包含该项目主要使用方法，功能的简单概述;运行配置,可选）。

2.实现效果（如果没有可以省略，建议包含）。

3.思路或使用（代码）。

+ *实现思路*

+ *使用方法*

4.重要知识点（总结，思考）。

5.内容参考（尊重原创）。

6.联系作者。


# [![](https://jitpack.io/v/qydq/an-aw-base.svg)](https://jitpack.io/#qydq/an-aw-base)   ![](https://github.com/qydq/an-aw-base/blob/master/an-base/src/main/res/drawable/base_drawable_art_click.png)   <a href='https://bintray.com/qydq/an/an-aw-base?source=watch' alt='Get automatic notifications about new "an-aw-base" versions'><img src='https://www.bintray.com/docs/images/bintray_badge_color.png'></a>  ![](https://github.com/qydq/an-aw-base/blob/master/screen/base_wtogglebutton.gif)

#######

# 1. 前言

**使用Gradle构建时添加以下依赖即可** *（注：也可以使用之前的低稳定版本）*

`compile 'com.github.qydq:an-aw-base:0.1.1'`

或者加入：

`compile 'cn.android.sunst:an-base:1.0.1'`

如要使用整套an框架则加入另一个依赖，具体请参考url = https://github.com/qydq/an

`compile 'com.github.qydq:an:0.0.1'`

或者加入：

`compile 'cn.android.sunst:an:0.0.1'`

**使用建议**

>使用建议继承SuperActivity，SuperFragment，BasePresenter，BaseView。

>使用建议在AndroidManifest.xml中加入android:theme="@style/Theme.AppCompat.Light.NoActionBar"`。

>使用建议在编译的build的时候注意an-aw-base需要在API=19或以上版本编译。

**Tips**
---

+ an框架提供了SuperActivity,SuperFragment,BasePresenter,BaseView等基类符合Google Material Design。

+ an框架提供了网络请求基于xutils模块的封装，http实现XHttps。

`*i.   HTTP实现利用了XHttps提供了post,get,upLoadFile,downLoadFile...*`

`*ii.  集成以上则可以使用注解功能，可以参考xUtils3开源项目。*`

`*iii. 提供便捷XCallBack ,XParseResponse ,XProgressCallBack操作。*`

+ an框架包含了许多实用的工具类，像MD5加密，数据校验，夜间模式切换等。

+ an框架提供了一些weight,如WSlidingDeleteListView,WToggleButton等。

+ an框架加入了两种夜间模式的功能。

+ an框架提供了快速监听网络变化的方法。

+ an框架后期功能正在完善中。

---------

# 2. 实现效果

+ **an框架提供通用的标题栏头集成（实际使用时背景颜色CommMainBg）。如图**

（1）普通状态栏参考<br>

 ![](https://github.com/qydq/an-aw-base/blob/master/screen/base_headview_standard.png)

（2）复杂状态栏参考<br>

 ![](https://github.com/qydq/an-aw-base/blob/master/screen/base_headview_standard_complex.png)


---------

# 3. 思路或使用（代码）

[Api Guide --aN框架API指导参考](https://github.com/qydq/an-aw-base/blob/master/an-base/ApiGuide.md)

[利用aN框架快速开发android应用程序基本使用方法](https://zhuanlan.zhihu.com/p/24273705?refer=sunst)

[如何使用An框架提供的WToggleButton实现一个漂亮的切换按钮？](https://zhuanlan.zhihu.com/p/24275861?refer=sunst)

[利用an框架提供的WSlidingDeleteListView仿QQ滑动删除。](https://zhuanlan.zhihu.com/p/24408002?refer=sunst)
		
### 实现思路

+ **An框架命名规范**

命名规范是一个项目中非常重要的部分，任何事情不规范不仅别人看不懂你，自己都不了解自己。<br>
[An框架命名规范参考](https://zhuanlan.zhihu.com/p/24155927)

	
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

（1）Style<br>

AnTvContentStyle 标准文本内容风格。

AnTvTitleStyle 标准文本标题风格。



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

+ **如何使用MVP架构搭建Android应用程序-(demo+解析)-极简版**

+ **Android MVP架构示例项目解析-加强版**
		
---------

# 5. 内容参考（尊重原创）。

个人原创

---------

# 6. 联系作者。

Athor IP：sunshuntao（qydq）（莳萝花）。

Email：qyddai@gmail.com。

欢迎大家使用aN快速开发框架。
If it doesn't work, please send me a email, qyddai@gmail.com
An框架可能存在一些缺陷，有问题欢迎大家反馈，收到邮件我会第一次时间回复处理。

Or

Import the library, then add it to your /settings.gradle and /app/build.gradle, if you don't know how to do it, you can read my blog for help.
http://drakeet.me/android-studio


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
		<!-- 往SDCard写入数据权限 -->
		<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
		<!-- WIFI状态监听相关权限-->
		<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
		<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
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

