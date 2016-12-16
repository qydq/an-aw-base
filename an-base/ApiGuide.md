## Api Guide --aN框架API指导参考 

类 - 方法 - 说明 - 参数 - 返回值 

### DUtilsBitmap 操作图片类的工具 ，枚举单例模式

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
|:---------|:---|:---|---:|

备注：Bitmap largeIcon = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_launcher)).getBitmap();//过时的解决方法。
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
			
### DataService 数据操作类工具 ，枚举单例模式