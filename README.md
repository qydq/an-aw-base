# an-aw-base

* 项目的主要功能是作为an框架的基类（基础的an-base）的仓库；你可以借助github，jitpack ，bintrary快速集成。

* 框架来源于本人an-maven-base框架，an-aw-base框架去除了an-maven-base框架不常用的功能，更轻量提升了性能。

*本项目地址：https://github.com/qydq/an-aw-base*

**使用介绍：如下简介或知乎，或个人网易博客。**

>个人知乎主页：https://www.zhihu.com/people/qydq

>个人博客主页：https://bgwan.blog.163.com


**如有使用问题请发送电子邮件。**

>邮件地址： staryumou@163.com  /  qyddai@gmail.com


**特别说明**

	
>创建时间 <------2016年09月17日------->
	
>2016年09月17日;最近修改时间：2016年09月19日。

########
	
**目录**
	
1. 前言（包含该项目主要实现的功能的简短说明，运行配置；可选）。

2. 实现效果（如果没有可以省略，建议包含）。

3. 思路或使用（代码）。

+ *实现思路*

+ *使用方法*

4。重要知识点（总结，思考）。

5。内容参考（尊重原创）。

6。联系作者。


# [![](https://jitpack.io/v/qydq/an-maven-base.svg)](https://jitpack.io/#qydq/an-maven-base)

#######

# 1. 前言

**使用Gradle构建时添加以下依赖即可** *（注：也可以使用之前的低稳定版本）*

`compile 'com.github.qydq:an-maven-base:0.1.3'`

或者加入：

`compile 'cn.android.sunst:an-base:0.0.2'`

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

+ an框架提供了SuperActivity,SuperFragment,BasePresenter,BaseView等基类符合Google material design。

+ an框架提供了网络请求基于xutils模块的封装，http实现XHttps。

*i.   HTTP实现利用了XHttps提供了post,get,upLoadFile,downLoadFile...*

*ii.  集成以上则可以使用注解功能，可以参考xUtils3开源项目。*

*iii. 提供便捷XCallBack ,XParseResponse ,XProgressCallBack操作。*

+ an框架包含了许多实用的工具类，像MD5加密，数据校验，夜间模式切换等。

+ an框架简化了Toast使用，直接showToast。

+ an框架加入了两种夜间模式的功能。

+ an框架提供了快速监听网络变化的方法。

+ an框架后期功能正在完善中。

#######

# 2. 实现效果
---
an框架，0.1.0版本新增通用的标题栏目方便以后的集成（实际使用的时候无背景颜色）。如图

| ![](https://github.com/qydq/an-maven-base/blob/master/screenpic/base_headview_standard_complex.png)| 


---

# 3. 思路或使用（代码）
		
### 实现思路

### 使用方法

+ **选择器**
	
|选择器名称|背景说明|边框|圆角|
|:---------|:-------|:---|---:|
|drawable_selector_md|CommColorTransparent<br>CommColorTransparentClicked<br>CommColorDayBackground|无|无|
|drawable_selector_md_shape|CommColorTransparent<br>CommColorTransparentClicked<br>CommColorDayBackground|CommColorBorder<br>CommColorBorderClicked|2dp|
# 4。重要知识点。
		

# 5。内容参考（尊重原创）。

个人原创


# 6。联系作者。

Athor IP：sunshuntao（qydq）（莳萝花）。

Email：qyddai@gmail.com。

知乎项目地址：https://zhuanlan.zhihu.com/qyddai


欢迎大家使用aN快速开发框架。
If it doesn't work, please send me a email, qyddai@gmail.com
An框架也可能存在一些笑笑的缺陷，有问题欢迎大家反馈，收到邮件我会第一次时间回复处理。

Or

Import the library, then add it to your /settings.gradle and /app/build.gradle, if you don't know how to do it, you can read my blog for help.
http://drakeet.me/android-studio


#### 后记：


## an-base > module /build.gradle

		apply plugin: 'com.android.library'
		////配置jitpack
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
		compile 'com.android.support:design:24.2.0'···
	
## an-maven-base > project /build.gradle

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
		
## AndroidManifest.xml

引用的地方应该使用这种theme
		<application
        android:allowBackup="true"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/Theme.AppCompat.Light.NoActionBar"
        android:windowSoftInputMode="stateAlwaysHidden">
		</application>
		
		
## 编译及相关SDK配置。

		android {
			compileSdkVersion 24
			buildToolsVersion "24.0.2"

			defaultConfig {
			minSdkVersion 19（现在改为21）
			targetSdkVersion 24
			versionCode 1
			versionName "1.0"

			testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"

		}
		
		说明这里面：在调用的Activity时应该super.onCreate(savedInstanceState);，还有setBackUp的属性，建议不要在低版本用高版本的内容。
		
		
## 具体功能的使用可以参考release的日志文件，或者在以后会在个人知乎做一些讲解。

