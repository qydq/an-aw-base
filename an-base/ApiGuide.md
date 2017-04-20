# Api Guide --aN框架API指导参考 

aN框架API指导参考-Api Guide  最近修改日期 2017年4月20日09:45:51

##  aN框架工具类参考。

类 - 方法 - 说明 - 参数 - 返回值 

### [!FastBlurUtils] StackBlur模糊算法，能得到非常良好的毛玻璃效果。

|方法名称|方法说明|参数|返回|
|:---------|:---|:---|---:|
|doBlur|进行StackBlur模糊算法|Bitmap sentBitmap, int radius, boolean canReuseInBitmap|Bitmap|

使用参考：

Bitmap bitmap = FastBlurUtil.doBlur(sBitmap, 2, false);

### [!FileUtils] 文件操作。

|方法名称|方法说明|参数|返回|
|:---------|:---|:---|---:|
|copyAssetsToSD|复制assets目录下的文件到sd卡<br>父类可实现接口FileOperateCallback，监听是否复制成功|final String srcPath, final String sdPath|FileUtils|
|getTotalCacheSize|得到总缓存大小|Context context|String|
|clearAllCache|得到总缓存大小|Context context|String|
|getFolderSize|得到文件目录下的大小|File file|long|
|getFormatSize|格式化单位|double size|String|
|cleanInternalCache|清除本应用内部缓存(/data/data/com.xxx.xxx/cache)|Context context|void|
|cleanDatabases|清除本应用所有数据库(/data/data/com.xxx.xxx/databases) |Context context|void|
|cleanSharedPreference|清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) |Context context|void|
|cleanDatabaseByName|按名字清除本应用数据库 |Context context, String dbName|void|
|cleanFiles|清除/data/data/com.xxx.xxx/files下的内容 |Context context|void|
|cleanExternalCache|清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)|Context context|void|


部分使用参考：
```groovy
String size = FileUtils.getTotalCacheSize(mContext);
```

```groovy
FileUtils.copyAssetsToSD(filePath,sdPath);
主类实现FileOperateCallback接口，并监听
void onSuccess();
void onFailed(String error);
```

### [!PermissionUtils] 为android6.0以上系统提供的权限检测工具类。

|方法名称|方法说明|参数|返回|
|:---------|:---|:---|---:|
|requestPermission|请求权限|nt requestCode, PermissionGrant permissionGrant|void|
|requestMultiPermissions|一次申请多个权限|PermissionGrant grant|void|
|requestPermissionsResult|Need consistent with requestPermission|int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults, PermissionGrant permissionGrant|void|
|getNoGrantedPermission|return no granted and shouldShowRequestPermissionRationale permissions|Activity activity, boolean isShouldRationale|ArrayList<String>|
|lacksPermissions|判断权限集合|String... permissions|boolean|
|lacksPermission|判断是否缺少权限|String permission|boolean|
|requestPermissions|请求权限|String[] PERMISSIONS, int requestCode, String permission|void|

使用参考：

PermissionUtils.requestPermissions(PERMISSIONS, 2, permission);

### [!DUtilsBitmap](https://github.com/qydq/an-aw-base/blob/master/an-base/src/main/java/com/an/base/utils/DUtilsBitmap.java) 操作图片类的工具 ，（枚举单例模式）

|方法名称|方法说明|参数|返回|
|:---------|:---|:---|---:|
|saveAvatarFile|保存文件|Context context, Bitmap bm, String dir, String fileName|无|
|saveBitmap|保存文件|Bitmap bm, String dir, String picName|无|
|getImage|通过路径获取图片|String path|byte|
|readInputStream|得到流数据|InputStream inputStream|byte|
|ReadBitmapById|读取本地资源的图片|Context context, int resId|Bitmap|
|ReadBitmapById|根据资源文件获取Bitmap|Context context, int drawableId,int screenWidth, int screenHight|Bitmap|
|getBitmap|等比例压缩图片|Bitmap bitmap, int screenWidth, int screenHight|Bitmap|
|drawableToBitmap|转化成bitmap对象|Drawable drawable|Bitmap|
|bitmapToDrawable|转化成drawable对象|Bitmap bitmape|Bitmap|
|createRoundedCornerBitmap|得到圆角图片bitmap对象|Bitmap bitmap, float roundPx|Bitmap|
|createReflectionImageWithOrigin|获得带倒影的图片方法|Bitmap bitmap|Bitmap|
|createWatermarkBitmap|图片水印的生成方法|Bitmap src, Bitmap watermark|Bitmap|
|zoomBitmap|放大缩小图片|Bitmap bitmap, int w, int h|Bitmap|
|zoomBitmap|设置收缩的图片，scale为收缩比例<br>为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存<br>这里缩小了1/2,但图片过大时仍然会出现加载不了,但系统中一个BITMAP最大是在10M左右,<br>我们可以根据BITMAP的大小根据当前的比例缩小,即如果当前是15M,那如果定缩小后是6M,那么SCALE= 15/6|Bitmap photo, int SCALE|Bitmap|
|decodeBitmapFromResource|为了避免OOM异常，最好在解析每张图片的时候都先检查一下图片的大小|Resources res, int resId,
                                           int reqWidth, int reqHeight|Bitmap|
|decodeBitmapFromPath|为了避免OOM异常，最好在解析每张图片的时候都先检查一下图片的大小,path为绝对路径|String path,
                                       int reqWidth, int reqHeight|Bitmap|

备注：Bitmap largeIcon = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_launcher)).getBitmap();

//过时的解决方法。
Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_launcher);

BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;

Bitmap largeIcon = bitmapDrawable.getBitmap();

		//设置通知栏Notification
		public Notification setNotification(String showTitle, String showInfo, Intent intent, boolean isCancel) {
			nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());//v7就ok
		//        builder = new Notification.Builder(getApplicationContext());//v4就ok
				int smallIconId = R.drawable.base_drawable_circle_click;
		//        Bitmap largeIcon = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_launcher)).getBitmap();//过时的解决方法。
				Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_launcher);
				BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
				Bitmap largeIcon = bitmapDrawable.getBitmap();
				builder.setLargeIcon(largeIcon)
						.setSmallIcon(smallIconId)
						.setContentTitle(showTitle)
						.setContentText(showInfo)
						.setTicker(showTitle)
						.setAutoCancel(isCancel)
						.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, intent, 0));
				Notification n = builder.build();
				nm.notify(NOTIFICATION_START, n);
				return n;
			}
			
### [!DataService](https://github.com/qydq/an-aw-base/blob/master/an-base/src/main/java/com/an/base/utils/DataService.java) 数据操作类工具 ，（枚举单例模式）

|方法名称|方法说明|参数|返回|
|:---------|:---|:---|---:|
|checkIp|md5验证ip地址是否合法|String ip|boolean|
|checkMobiles|验证是否为电话号码|String mobiles|boolean|
|md5|md5加密字符串|String text|String|
|byteArrayToHex|Convert hex string to byte[]|String hexString|byte[]|
|byteArrayToHex|下面这个函数用于将字节数组换成成16进制的字符串|byte[] byteArray|String|
|getCurrentTimeByDate|得到当前时间|无|String|
|getCurrentTimeByCalendar|获取今天时间|无|Date|
|getShotDateTime|获取短时间|无|String|
|getLongDateTime|获取长时间|无|String|
|removeItemByName|通过字段来移除某个list中的数据|List<String> lists, String name|List<String>|
|removeItemByPosition|移除某个位置后的lists|List<String> lists, int position|List<String>|
|islegalInput|//检查字符串是否合法，合法返回true，不合法返回false|String txt|boolean|

### [!NetBroadcastReceiverUtils](https://github.com/qydq/an-aw-base/blob/master/an-base/src/main/java/com/an/base/utils/NetBroadcastReceiverUtils.java) 网络操作类

|方法名称|方法说明|参数|返回|
|:---------|:---|:---|---:|
|getNetworkState|得到网络的连接状态，状态见上面的状态码。为NetBroadcastReceiverUtils提供服务|Context context|int|
|isWifiConnected|检查是否是WIFI|Context context|boolean|
|isMobileConnected|检查是否是移动网络|Context context|boolean|
|isConnectedToInternet|检查是否有网络连接|Context context|boolean|

### [!DUtilsUi](https://github.com/qydq/an-aw-base/blob/master/an-base/src/main/java/com/an/base/utils/DUtilsUi.java) UI操作的工具类普通单列模式

|方法名称|方法说明|参数|返回|
|:---------|:---|:---|---:|
|playHeartbeatAnimation|按钮模拟心脏跳动|final View view|void|

### [!DUtilsStorage](https://github.com/qydq/an-aw-base/blob/master/an-base/src/main/java/com/an/base/utils/DUtilsStorage.java) 网络操作类（枚举单例模式）

|方法名称|方法说明|参数|返回|
|:---------|:---|:---|---:|
|checkExistRom|判断内置SDcard是否存在 rom代表内置, 可不判断，因为内置都会有。|void|boolean|
|getRomPath|获取内置SDcard的路径|void|String|
|getSdcardPath|获取外置SDcard的路径|void|String|
|calculate|计算SD卡剩余容量和总容量|void|String 总容量and剩余容量|

##  aN框架Widget

WResizeRelativeLayout ，监听键盘是否弹出关闭，然后做相关操作。
WResizeRelativeLayout，提供的接口--
		public interface KeyBordStateListener {
			void onStateChange(int state);
		}

        
		@Override
			public void onStateChange(int state) {
				switch (state) {
					case ResizeRelativeLayout.HIDE:
		//                card.setVisibility(View.VISIBLE);
		//                cardSmall.setVisibility(View.INVISIBLE);
						break;
					case ResizeRelativeLayout.SHOW:
		//                card.setVisibility(View.GONE);
		//                cardSmall.setVisibility(View.VISIBLE);
						break;
			}
		}
		
完整布局代码参考。
		<?xml version="1.0" encoding="utf-8"?>
		<com.an.base.view.widget.WResizeRelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
		xmlns:app="http://schemas.android.com/apk/res-auto"
		android:id="@+id/login_root"
		android:layout_width="match_parent"
		android:layout_height="match_parent"
		android:background="@drawable/shape_login_bg_start">

		<android.support.v7.widget.CardView
        android:id="@+id/card_small"
        android:layout_width="40dp"
        android:layout_height="40dp"
        android:layout_alignParentRight="true"
        android:layout_alignParentTop="true"
        android:layout_margin="20dp"
        android:visibility="invisible"
        app:cardBackgroundColor="@color/transparent"
        app:cardCornerRadius="2dp"
        app:cardElevation="2dp">

        <com.an.base.view.widget.WRoundedImageView
            android:id="@+id/profile_small"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@drawable/login_btn_photo_l"
            android:scaleType="fitCenter"
            android:src="@drawable/photo_l"
            app:riv_corner_radius="2dp" />
		</android.support.v7.widget.CardView>

		<LinearLayout
        android:layout_width="315dp"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:layout_marginLeft="20dp"
        android:layout_marginRight="20dp"
        android:orientation="vertical">

        <android.support.v7.widget.CardView
            android:id="@+id/card"
            android:layout_width="98dp"
            android:layout_height="98dp"
            android:layout_gravity="center_horizontal"
            android:layout_marginBottom="@dimen/login_input_height"
            app:cardBackgroundColor="@color/transparent"
            app:cardCornerRadius="2dp"
            app:cardElevation="2dp">

            <com.makeramen.roundedimageview.RoundedImageView
                android:id="@+id/profile"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:background="@drawable/login_btn_photo_l"
                android:scaleType="fitCenter"
                android:src="@drawable/photo_l"
                app:riv_corner_radius="2dp" />
        </android.support.v7.widget.CardView>

        <EditText
            android:id="@+id/et_login_username"
            android:layout_width="match_parent"
            android:layout_height="@dimen/login_input_height"
            android:layout_marginLeft="@dimen/login_margin_left"
            android:background="@drawable/shape_solid_transparent"
            android:drawableLeft="@drawable/login_icon_company"
            android:drawablePadding="@dimen/login_padding"
            android:hint="@string/login_input_hint_username"
            android:inputType="textPersonName"
            android:singleLine="true"
            android:textColor="@color/ColorPrimary"
            android:textColorHint="@color/text_hint_color"
            android:textCursorDrawable="@drawable/common_input_cursor"
            android:textSize="@dimen/login_input_text_size" />

        <TextView
            android:id="@+id/line1"
            style="@style/common_login_horizontalLine_matchParent_normal" />

        <EditText
            android:id="@+id/et_login_employ_name"
            android:layout_width="match_parent"
            android:layout_height="@dimen/login_input_height"
            android:layout_marginLeft="@dimen/login_margin_left"
            android:background="@drawable/shape_solid_transparent"
            android:drawableLeft="@drawable/login_icon_man"
            android:drawablePadding="@dimen/login_padding"
            android:hint="@string/login_input_hint_employ"
            android:inputType="textPersonName"
            android:singleLine="true"
            android:textColor="@color/ColorPrimary"
            android:textColorHint="@color/text_hint_color"
            android:textCursorDrawable="@drawable/common_input_cursor"
            android:textSize="@dimen/login_input_text_size" />

        <TextView
            android:id="@+id/line2"
            style="@style/common_login_horizontalLine_matchParent_normal" />


        <EditText
            android:id="@+id/et_login_pwd"
            android:layout_width="match_parent"
            android:layout_height="@dimen/login_input_height"
            android:layout_marginLeft="@dimen/login_margin_left"
            android:background="@drawable/shape_solid_transparent"
            android:drawableLeft="@drawable/login_icon_lock"
            android:drawablePadding="@dimen/login_padding"
            android:hint="@string/login_input_hint_pwd"
            android:inputType="textPassword"
            android:singleLine="true"
            android:textColor="@color/ColorPrimary"
            android:textColorHint="@color/text_hint_color"
            android:textCursorDrawable="@drawable/common_input_cursor"
            android:textSize="@dimen/login_input_text_size" />

        <TextView
            android:id="@+id/line3"
            style="@style/common_login_horizontalLine_matchParent_normal" />

        <CheckBox
            android:id="@+id/remember_pwd"
            style="@style/RememberPasswordCheckBox"
            android:layout_marginLeft="@dimen/login_margin_left"
            android:text="@string/login_remember_pwd" />

        <Button
            android:id="@+id/btn_login"
            android:layout_width="match_parent"
            android:layout_height="46dp"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="32dp"
            android:background="@drawable/common_green_btn_selector"
            android:focusable="true"
            android:text="@string/login_button_text"
            android:textColor="@color/text_button_login"
            android:textSize="@dimen/login_btn_text_size" />
    </LinearLayout>

</com.qianmi.shine.widget.ResizeRelativeLayout>

WRoundImageView 圆形ImageView，可设置最多两个宽度不同且颜色不同的圆形边框。


		WRoundImageView.setImageBitmap(bitmap);
		WRoundImageView.setImageResource(R.drawable.default_avatar);
		
	/**
     * 获取裁剪后的圆形图片
     * @param radius
     * 半径
     */
    public Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
	
WIoScrollView 可以仿IOS實現下拉彈性佈局的效果。
使用經典參考：

[sst_coordinatorlayout_wtiocrollview.xml](https://github.com/qydq/an-aw-base/tree/master/app/src/main/res/layout/sst_coordinatorlayout_wtiocrollview.xml) 

WSlidingDeleteListView 滑动删除的ListView
使用参考：
https://zhuanlan.zhihu.com/p/24408002

WToggleButton aN提供的公共开关参考。
https://zhuanlan.zhihu.com/p/24275861?refer=sunst

### [!CaptureHelper](https://github.com/qydq/an-aw-base/blob/master/an-base/src/main/java/com/an/base/utils/CaptureHelper.java) 拍照辅助类

使用方法 CaptureHelper helper = new CaptureHelper(skRoot,mcontext);

|方法名称|方法说明|参数|返回|
|:---------|:---|:---|---:|
|capture|拍照，以时间命名，可以调用getPhoto得到时间命名的file照片|无|mActivity.startActivityForResult(captureIntent, RESULT_CAPTURE_CODE);|
|capture|拍照，以时间命名，可以调用getPhoto得到时间命名的file照片|String saveName, boolean isNoFaceDetection|mActivity.startActivityForResult(captureIntent, RESULT_CAPTURE_CODE);|
|photo|调用系统相册，并返回RESULT_PHOTO_CODE|无|无|
|startPhotoZoom|原始uri。一般未data.getData()  裁剪图片方法实现,系统会自动根据android版本调用合适的方法。|Uri uri, int cropWidthSize, int cropHeightSize|mActivity.startActivityForResult(intent, RESULT_PHOTO_CROP_CODE);|
|startPhotoZoom|原始uri。一般未data.getData()  ,裁剪并保存名为cropName的jpg照片,裁剪图片方法实现,系统会自动根据android版本调用合适的方法。|Uri uri, String cropName,int cropWidthSize, int cropHeightSize|mActivity.startActivityForResult(intent, RESULT_PHOTO_CROP_CODE);|
|startCaptureZoom|调用系统相机后可以调用该方法为照片裁剪|String originName, String cropName|void|
|saveBitmap|该方法单独使用，保存名为saveName的照片到内存卡,使用方法如下|Bitmap mBitmap, String saveName|String|
|getPath|以下是关键，原本uri返回的是file:///...来着的，android4.4返回的是content:///..|final Context context, final Uri uri|String|

使用参考

		//选取照片裁剪后返回。可以在这里设置图片显示到控件中。
                case RESULT_PHOTO_CROP_CODE:
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            Bitmap bitmap = extras.getParcelable("data");
                            Bitmap roundBitmap = DUtilsBitmap.INSTANCE.createReflectionImageWithOrigin(bitmap);
                            iv.setImageBitmap(roundBitmap);
                            //保存
                            captureHelper.saveBitmap(bitmap, IMAGE_FILE_NAME);
                        }
                    }
                    break;