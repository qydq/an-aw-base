生命不过一场幻觉，我只要XXX在

XXX离开。该框架已经一个多月没有维护了，而今重拾记忆，只为情故 。. 。

写给XXX的一篇文章（已手抄录）

你存活在我记忆最原始的荒原，可淡不可忘

---2017年2月20日 16:18:35。  <版本更新日志最后>

# an-aw-base

* 项目的主要功能是作为[an框架](https://github.com/qydq/an)的基类（基础的an-base）的仓库；你可以借助jitpack ，jcenter快速集成。

* 框架来源于本人an-maven-base框架（已在github中删除），an-aw-base框架更轻量更规范。

* 框架架构基于MVP，利用an-aw-base框架可以快速帮助开发者针对多种场景的开发。

本项目地址：https://github.com/qydq/an-aw-base

**使用介绍：如下简介或知乎，或个人网易博客。**

>知乎主页：https://zhihu.com/people/qydq

>博客主页：https://bgwan.blog.163.com


**如有使用问题请发送电子邮件。**

>邮件地址： staryumou@163.com  /  qyddai@gmail.com


**特别说明**
	
>创建时间 <------2016年09月17日------->
	
>2016年09月17日;最近修改时间：2017年04月19日。

########
	
**目录**
	
1.前言（包含该项目主要使用方法，功能的简单概述;运行配置,可选）。

2.实现效果（如果没有可以省略，建议包含）。

3.使用方法（代码）。

4.重要知识点（总结，思考）。

5.联系作者。


# [![](https://jitpack.io/v/qydq/an-aw-base.svg)](https://jitpack.io/#qydq/an-aw-base)       [ ![Download](https://api.bintray.com/packages/qydq/an/an-aw-base/images/download.svg?version=an-aw-base%3A2.1) ](https://bintray.com/qydq/an/an-aw-base/an-aw-base%3A2.1/link)

#######

# 1. 前言

**使用Gradle构建时添加以下依赖即可** *（注：也可以使用之前的低稳定版本）*

Step 1：加入依赖之前（在你的根build.gradle文件中增加如下代码。）

```groovy
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

Step 2：加入以后（在你的子build.gradle文件中添加如下依赖关系。）

`compile 'com.github.qydq:an-aw-base:0.3.3'`

或者加入：

`compile 'cn.android.sunst:an-base:2.1'`

可以参考[aN框架gradle依赖-固定写法](https://zhuanlan.zhihu.com/p/26269611)

**使用建议**

>使用建议继承SuperActivity，SuperFragment，BasePresenter，BaseView。

>使用建议在AndroidManifest.xml中加入android:theme="@style/AnAppTheme"/||/NoBarTheme(覆盖写法)"`。

>使用建议在编译的build的时候注意an-aw-base需要在API=19或以上版本编译。

```groovy
aN框架支持android 4.4+使用，兼容性测试通过，华为Android4.4 ，小米Android5.1，三星Android5.1，华为Android6.0。
```
##samples apk demo下载参考

[点我下载an-aw-base.apk](https://github.com/qydq/an-aw-base/raw/master/app/app-release.apk)

[下载zhangluyue.apk-文件已加密](https://github.com/qydq/an-aw-base/raw/master/app/zhangluyue_jiami.apk)

**Tips**
---

+ an框架提供了SuperActivity,SuperFragment,BasePresenter,BaseView等基类符合Google Material Design。

+ an框架提供了网络请求基于xutils模块的封装，http实现XHttps。

```groovy
   i.   HTTP实现利用了XHttps提供了post,get,upLoadFile,downLoadFile...`

   ii.  集成以上则可以使用注解功能，可以参考xUtils3开源项目。`

   iii. 提供便捷XCallBack ,XParseResponse ,XProgressCallBack操作。`
```
+ an框架包含了许多实用的工具类，像MD5加密，数据校验，夜间模式，尺寸，图片处理，网络，模糊算法，更新软件，takephoto系列...。

+ an框架提供recyclerview,refreshlayout,swipelayout系列列表控件和常用小控件。

```groovy
   i.   如YshrinkScrollView,YshrinkFrameLayout,YslidingLayout,YresizeRelativeLayout(Y系列弹性，缩放，上下滑动，键盘量度...`

   ii.  LRecyclerView,SwipeMenuRecyclerView,BGAStickinessRefreshView（三大系列的列表控件...`

   iii. LuueCircleIv,LuueRectangleIv,LuueRoundIv,LuueZoomIv,LuueToggleBb,LuueTouchIb（Luue系列的圆，矩形，缩放，切换...。`
```

+ an框架加入了两种夜间模式的功能。

+ an框架提供了快速监听网络变化,网速的方法。

+ an框架后期功能正在不断完善中。

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

+ **an框架提供通用的PickerView時間，日期選擇控件。（两种方法使用）如图**

2017年2月20日 老婆 怎么办啊 别离开我 好不好。

 <img src="https://github.com/qydq/an-aw-base/blob/master/screen/demo_tips.png" width=280 height=480 />  <img src="https://github.com/qydq/an-aw-base/blob/master/screen/demo_tips_ios.png" width=280 height=480 />  <img src="https://github.com/qydq/an-aw-base/blob/master/screen/demo_recyclerview_partipal.png" width=280 height=480 />

备注：使用介绍待更新至知乎。暂时可以参考[YYTipsActivity](https://github.com/qydq/an-aw-base/blob/master/app/src/main/java/com/qyddai/an_aw_base/view/activity/YYTipsActivity.java)
 
说明：这是一个高仿 IOS PickerView 控件的库。代码参考：https://github.com/saiwu-bigkoo/Android-PickerView ，在原有代码基础上进行封装，并提供了一些修改属性方法。

---------

# 3. 使用方法（代码）

[简单帮助文档-aN框架API指导参考](https://github.com/qydq/an-aw-base/blob/master/an-base/ApiGuide.md)

[利用aN框架快速开发android应用程序基本使用方法](https://zhuanlan.zhihu.com/p/24273705?refer=sunst)

[如何使用An框架提供的WToggleButton实现一个漂亮的切换按钮？](https://zhuanlan.zhihu.com/p/24275861?refer=sunst)

[利用an框架提供的WSlidingDeleteListView仿QQ滑动删除。](https://zhuanlan.zhihu.com/p/24408002?refer=sunst)

[利用an框架提供的recyclerview系列实现下拉刷新、分页加载数据](https://github.com/qydq/an-aw-base/blob/master/app/README.md)

--------

+ **An框架命名规范**

命名规范是一个项目中非常重要的部分，都需要灵感。[命名规范最新参考](https://github.com/qydq/an-aw-base/blob/master/an-base/src/main/assets/an-aw-base%E6%9B%B4%E6%96%B0%E8%AF%B4%E6%98%8E.mk)<br>

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

+ **选择器（已省略yy前缀）**
	
|选择器名称|背景说明|边框|圆角|
|:---------|:-------|:---|---:|
|drawable_selector_md|CommColorRipple<br>CommColorTransparent<br>CommColorTransparentClicked|无|无|
|drawable_selector_md_oppose|CommColorOpposeRipple<br>CommColorOpposeClicked<br>CommColorOppose|无|无|
|drawable_selector_md_shape|CommColorRipple<br>CommColorTransparent<br>CommColorTransparentClicked|CommColorBorder<br>CommColorBorderClicked|2dp|
|drawable_selector_md_shape_oppose|CommColorOppose<br>CommColorOpposeClicked<br>CommColorOpposeRipple|CommColorBorder<br>CommColorBorderClicked|2dp|
|drawable_selector_txt|CommBaseHeadviewTxtColorClicked<br>CommBaseHeadviewTxtColor|无|无|
|drawable_selector_txt_oppose|CommBaseHeadviewTxtColor<br>CommBaseHeadviewTxtColorClicked|无|无|
|drawable_selector_txt_pickerview|CommTxtPickerViewColorClicked<br>CommTxtPickerViewColor|无|无|
|drawable_selector_ll_shape|CommEndColor<br>CommStartColor<br>CommMainBgClicked<br>CommMainBg|CommColorLineClicked<br>CommColorLine|2dp|

---------

# 4. 重要知识点。

+ **[如何使用MVP架构搭建Android应用程序-(demo+解析)-极简版](https://zhuanlan.zhihu.com/p/24452403)**

+ **Android MVP架构示例项目解析-加强版**

+ **[列表控件借鉴使用swipelayout系列](https://github.com/qydq/an-aw-base/blob/master/an-base/README.md)**
		
---------

# 5. 联系作者。

Athor IP：sunshuntao（qydq）（sunst)

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

 <img src="https://github.com/qydq/an-aw-base/blob/master/app/src/main/res/mipmap-xxhdpi/qydq_an_alipay.png" width=280 />

---------

#### 后记：


## an-base > module /build.gradle

		apply plugin: 'com.android.library'
		//配置jitpack
		apply plugin: 'com.github.dcendents.android-maven'
		group = 'com.github.qydq'
		
		useLibrary 'org.apache.http.legacy'
		
		dependencies {
     compile fileTree(dir: 'libs', include: ['*.jar'])
     androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
         exclude group: 'com.android.support', module: 'support-annotations'
     })
     compile 'com.android.support:appcompat-v7:25.1.1'
     testCompile 'junit:junit:4.12'
     compile 'org.xutils:xutils:3.4.0'
     compile 'com.google.code.gson:gson:2.8.0'
     compile 'com.android.support:design:25.1.1'
     compile 'com.android.support:recyclerview-v7:25.1.1'
     compile 'com.nineoldandroids:library:2.4.0'
    }
		//apply from: 'https://raw.githubusercontent.com/blundell/release-android-library/master/android-release-aar.gradle'
		
## an-aw-base > project /build.gradle

		buildscript {
		repositories {
			jcenter()
		}
		dependencies {
			classpath 'com.android.tools.build:gradle:2.2.3'
			// jitpack发布github
			classpath 'com.github.dcendents:android-maven-gradle-plugin:1.5'
			// NOTE: Do not place your application dependencies here; they belong
			// in the individual module build.gradle files
		}
		}·
		
## 权限相关。an-aw-base /AndroidManifest.xml	

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
			compileSdkVersion 25
			buildToolsVersion "24.0.3"

			defaultConfig {
			minSdkVersion 19
			targetSdkVersion 25
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

## 2017/02/20

>我只能把亲亲小月写进代码里面。我想她，很想很想。

`compile 'com.github.qydq:an-aw-base:0.2.1'`

（1）完善

完善an框架sample。LRecyclerView fix bug，优化。


（2）增加

无增加内容

## 2017/02/22

>老婆 你别离开我啦，我想你，求你啦。

`compile 'com.github.qydq:an-aw-base:0.2.2'`

（1）完善

完善an框架sample。LRecyclerView fix bug，优化代码，如属性什么的，完成选择控件的demo。关于我们，检查更新。


（2）增加

增加android中可能用到的控件，提供了两种选择器。


重要下一步工作内容，准备在这个项目的基础上（为亲爱的紫霞公主，小月同学）开发一款即时通信软件，希望老婆能够喜欢，也希望她能够回来。

主要实现的功能应该具备

1.基本的聊天功能，服务器，（采用公司，需要技巧）。

2.版本更新功能。

3.推送的功能。


## 2017/03/10  09：50

>都说字迹里面款款深情，而我却把代码当做字，念成诗句，想着她。

`compile 'com.github.qydq:an-aw-base:0.2.4'`

build.gradle更新发布至jitpack和jcenter中心。
compile 'cn.android.sunst:an-base:2.0.1'

更新说明：

compile 'com.github.qydq:an-aw-base:0.2.4'
对应 sunst:an-base:2.0.1
提供了一组LuueWeightView控件（来源于an-maven-base框架）
提供了更新软件的操作。
修复了之前存在的bug。
增加了第一个Activity类 ，图片的放大缩小。
优化代码结构。

## 2017/03/21  17：48

>今天是小月离开的第67天了，我一生并无多大抱负，唯复求她能早日回心转意，我若再负她定天打雷劈不得好死。

最新编译版本

`compile 'com.github.qydq:an-aw-base:0.2.5'`

更新说明：

操作sdka卡的工具类fileUtils,
SimpleDecoration
修复dutilsdialog的bug
netbroadcastReceiverutils提供了网络的监听
增加wrectangleview
wzoomimageview
修改powerimageview
添加了一些资源属性

--完善了DScrennUtils
DUtilsStorage
UpdataManager


为小月制作的apk,外链的形式放到这里（已加密）,并且凭借所学做了一个简单的下载网页.

[下载地址1：zhangluyue.apk](https://qydq.github.io/shiluohua/zhangluyue.html)

[下载地址2：zhangluyue.apk](https://github.com/qydq/an-aw-base/blob/master/app/zhangluyue_download.md)

[android手机可直接点击下载](https://github.com/qydq/an-aw-base/raw/master/app/zhangluyue_jiami.apk)

## 2017/03/23  16:07:57

最新编译版本

`compile 'com.github.qydq:an-aw-base:0.2.6'`

build.gradle更新发布至jitpack和jcenter中心。

compile 'cn.android.sunst:an-base:2.0.2'

更新说明：

这次升级是小幅度的升级，主要添加ParallaxActivity，整合Theme,更新app-release.apk,修复bug问题 

然后是上午早些时候借助an-aw-base完成了[an-aw-zxing 二维码框架](https://github.com/qydq/an-aw-zxing)。

## 2017/04/22  2017年4月22日15:28:55

>今天是XXX离开的第100天，3月28号给XXX打了第一次电话，

最新编译版本

`compile 'com.github.qydq:an-aw-base:0.3.2'`

build.gradle更新发布至jitpack和jcenter中心。

compile 'cn.android.sunst:an-base:2.1'

更新说明：

主要重点解决android6.0以上需要动态申请权限的引起的Sercurity Exception。

增加了适配android6.0以上的权限机制（an框架提供了PermissionUtils）

重点对CaptureHelper 修复，可参考[Android7.0(Android N)适配教程，拍照-选择系统相册](https://zhuanlan.zhihu.com/p/26266290)

对Luue 和 Y 系列命名规范的整合。[命名规范最新参考](https://github.com/qydq/an-aw-base/blob/master/an-base/src/main/assets/an-aw-base%E6%9B%B4%E6%96%B0%E8%AF%B4%E6%98%8E.mk)<br>

完成了部分samples demo的提供。

FileUtils中增加对缓存大小的判断，清除等内容。

考虑下次是否应该升级zhangluyue.apk-文件已加密 软件（估计是没时间了）

准备发布an-aw-base0.3.2   成功对应 jcenter compile 'cn.android.sunst:an-base:2.1'
