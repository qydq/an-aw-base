an-aw-base version 0.3 更新说明（孙顺涛，qydq，晴雨 ，莳萝花 ，qyddai，2017年4月7日 17:57:43）

对0.3以前也一并做一个简单说明。（后续会继续更新samples给大家参考）

这次版本0.3是主要更新是为了an-aw-base能够适应android6.0以上的系统，手机权限问题。

D系列替换为Y系列。

1.widget说明：

(点击图片查看更直接)(使用说明参考相关文档，an框架使用说明（连接）)

“luue”系列和“y”系列的命名，组合起来的寓意代表lu + yue（拼音“露月”）an-aw-base从构建工程的那天灵感都来源于她，至少她也算是你真正的初恋啊。

就算现在改框架也需要好多好多时间的，而且这些东西都是需要灵感，所以还是选择维护这个架构吧（可以修改任何人都会介意）。
如果你（huaqiangu)看到不要怪师傅，我先认识她。我知道你觉得师傅对你来说是一个很重要的人，很多时候我喜欢直接，爱一个人就勇敢，虽然没有教到你什么，但你永远都是我的小骨。

我知道你不会看到。


（1）(luue系列特殊控件类)

LuueCircleImageView--更名为--LuueCircleIv

LuueDragImageView--更名为--LuueDragIv

WRectangleView--更名为--LuueRectangleIv

WRoundImageView--更名为--LuueRoundIv

LuueScaleImageView--更名为--LuueScaleIv

LuueSmoothImageView--更名为--LuueSmoothIv

WToggleButton--更名为--LuueToggleBb

新增--LuueTouchIb

LuueVerticalViewPager--更名为--LuueVerticalVp

LuueViewPager--更名为--LuueVp

WZoomImageView--更名为--LuueZoomIv


PowerImageView从view/ytips中移动到view/widget并更名为--LuuePowerIv

（2）(y系列特殊布局类)

WResizeRelativeLayout--更名为--YresizeRelativeLayout

新增--YshrinkFrameLayout

WShrinkScrollView--更名为--YshrinkScrollView

WSlidingDeleteListView--更名为--YslidingDeleteListView

WSlidingLayout--更名为--YslidingLayout

WSwipeCloseFrameLayout--更名为--YswipeCloseFrameLayout

WSwipeFinishFrameLayout--更名为--YswipeFinishFrameLayout


2.工具类说明：

（1）修改CaptureHelper 拍照帮助类

兼容android4.4到android7.0权限。

（2）utils/ytips增加了PermissionUtils工具类（这是本次更新的重点）

（3）utils/ytips增加FastBlurUtil 采用的是StackBlur模糊算法 毛玻璃效果。

（4）utils/ytips更新FileUtils操作sd卡权限，是否兼容android60+待测试。

（5）SizeUtil 增加更多方法 ，如dip/dp ，getStatusBarHeight具体参考该类。

（6）为了适配权限的问题，在res/下面新增an-aw-base 文件提供资源base_fileprovider_tackphoto，名字为name="an_ytips"
provider参考，使用的时候，用下面com.an.base.fileprovider.tackphoto包名即可
<provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="com.an.base.fileprovider.tackphoto"
            android:exported="false"
            android:grantUriPermissions="true">

（7）修改CaptureHelper帮助类，并且兼容之前的版本。


3.其它说明过的内容。

（1）网络请求从model/utils包 --改为到model/ytips中

（2）DBitmapUtils替换为YbitmapUtils
DDialogUtils替换为YdialogUtils
DScrenUtils替换为YscreenUtils
DStorageUtils替换为YstorageUtils
DUitlsUi更名为YuiUtils
SizeUitl更名为SizeUtils
FastBlurUtil更名为FastBlurUtils
