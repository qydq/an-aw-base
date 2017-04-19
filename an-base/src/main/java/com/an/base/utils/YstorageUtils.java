package com.an.base.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**********************************************************
 * @文件名称：DUtilsSdcard
 * @文件作者：staryumou@163.com
 * @创建时间：2016/9/21
 * @文件描述：如果涉及Storage代表sd卡，外置和内置，不是内存，如果是root则是根目录。**获得系统目录/system
 * @修改历史：2017/3/21
 **********************************************************/
public enum YstorageUtils {
    INSTANCE;

    /***
     * @return boolean getEnvironmentPath
     * @ 获取手机上需要保存的根路径String，优先考虑外置SD卡。
     */
    public String getskRootPath() {
        String skRootPath = "";
        //**如果存在sd卡返回sd卡路径，如果没有则返回根路径（
        if (checkExistRom()) {
            skRootPath = Environment.getExternalStorageDirectory().getPath();//获取根目录
        } else {
            skRootPath = Environment.getRootDirectory().getPath();
        }
        return skRootPath;
    }

    /***
     * @return boolean getEnvironmentPath
     * @ 获取手机上需要保存的路径String，使用该方法首先要判空
     */
    public String getskStorageDirectory() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /***
     * @return boolean getEnvironmentPath
     * @ 获取手机上需要保存的路径String，该方法不必判空。
     */
    public String getskRootDirectory() {
        return Environment.getRootDirectory().getPath();
    }

    /*
    * 获取根目录/cache目录
    * */
    public String getDownloadCacheDirectory() {
        return Environment.getDownloadCacheDirectory().getPath();
    }
/*
* -----------------分割线-------------
* */

    /***
     * @return boolean getEnvironmentPath
     * @ 获取手机上需要保存的根路径file，优先考虑外置SD卡。(file会一定存在)
     */
    public File getskRootFile() {
        File skRootFile = null;
        //**如果存在sd卡返回sd卡路径，如果没有则返回根路径
        if (checkExistRom()) {
            skRootFile = Environment.getExternalStorageDirectory();//获取根目录
        } else {
            skRootFile = Environment.getRootDirectory();
        }
        return skRootFile;
    }

    /***
     * @return boolean getEnvironmentPath
     * @ 获取手机上绝对路径需要保存的路径file，(改方法需要判空。)
     */
    public File getskStorageDirectoryFile() {
        return Environment.getExternalStorageDirectory();
    }

    /***
     * @return boolean getEnvironmentPath
     * @ 获取手机上绝对路径需要保存的路径file，(该方法一定不会为空的。)
     */
    public File getskRootDirectoryFile() {
        return Environment.getRootDirectory();
    }

    /***
     * @return boolean getskCacheFile
     * @ 获取手机上缓存的路径file，(该方法一定不会为空的。)
     * android的存储建议放在该目录下面，该目录的下面不需要权限。
     */
    public File getskCacheFile(Context context) {
        return context.getCacheDir();
    }

    /*
* -----------------分割线-------------
* */

    /***
     * @return boolean
     * @判断同名方法内置SDcard是否存在（目的是为了兼容以前的版本）
     */
    public static boolean checkExistRom() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /***
     * @return boolean
     * @判断同名方法内置SDcard是否存在
     */
    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /***
     * @return boolean
     * @ 获取外置SDcard的路径(mount linux的角度去获取，和上面方法有区别)
     * 得到mount外置sd卡的路径（简单参数）
     */
    public String getMountSimpleSdcardPath() {
        StringBuilder sdCardStringBuilder = new StringBuilder();
        List<String> sdCardPathList = new ArrayList<String>();
        String sdcardpath = null;
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("extSdCard")) {
                    String[] arr = line.split(" ");
                    String path = arr[1];
                    File file = new File(path);
                    if (file.isDirectory()) {
                        sdCardPathList.add(path);
                    }
                }
            }
            isr.close();
        } catch (Exception e) {
        }
        if (sdCardPathList != null) {
            for (String path : sdCardPathList) {
                sdCardStringBuilder.append(path);
            }
            sdcardpath = sdCardStringBuilder.toString();
        }
        return sdcardpath;
    }

    /*
    * android系统可通过Environment.getExternalStorageDirectory()获取存储卡的路径，
    * 但是现在有很多手机内置有一个存储空间，同时还支持外置sd卡插入，
    * 这样通过Environment.getExternalStorageDirectory()方法获取到的就是内置存储卡的位置，
    * 需要获取外置存储卡的路径就比较麻烦，这里借鉴网上的代码，稍作修改，
    * 在已有的手机上做了测试，效果还可以，当然也许还有其他的一些奇葩机型没有覆盖到。
    *
    * 得到mount外置sd卡的路径（复杂参数）
    * */
    public String getMountComplexSdcardPath() {
        String sdcard_path = null;
        String sd_default = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        if (sd_default.endsWith("/")) {
            sd_default = sd_default.substring(0, sd_default.length() - 1);
        }
        // 得到路径
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line.contains("secure"))
                    continue;
                if (line.contains("asec"))
                    continue;
                if (line.contains("fat") && line.contains("/mnt/")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        if (sd_default.trim().equals(columns[1].trim())) {
                            continue;
                        }
                        sdcard_path = columns[1];
                    }
                } else if (line.contains("fuse") && line.contains("/mnt/")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        if (sd_default.trim().equals(columns[1].trim())) {
                            continue;
                        }
                        sdcard_path = columns[1];
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sdcard_path;
    }
    /*
* -----------------分割线-------------
* */

    /***
     * @return String 总容量and剩余容量
     * @ 计算SD卡剩余容量和总容量(调用该方法需要判空。)
     */
    public String calculateStorage() {
        String txt = "";
        //判断是否有插入存储卡
        if (checkExistRom()) {
            File path = Environment.getExternalStorageDirectory();
            //取得sdcard文件路径
            StatFs statfs = new StatFs(path.getPath());
            //获取block的SIZE
            long blocSize = statfs.getBlockSize();
            //获取BLOCK数量
            long totalBlocks = statfs.getBlockCount();
            //己使用的Block的数量
            long availaBlock = statfs.getAvailableBlocks();
            String[] total = filesize(totalBlocks * blocSize);
            String[] availale = filesize(availaBlock * blocSize);
            //显示SD卡的容量信息
            txt = total[0] + total[1] + "and" + availale[0] + availale[1];
        }
        return txt;
    }

    /***
     * @return String 总容量and剩余容量
     * @ 计算内存总容量(调用该方法不需要判空。)
     */
    public String calculateRoot() {
        String txt = "";
        //判断是否有插入存储卡,这里严谨些还是判断sk
        if (!checkExistRom()) {
            File path = getskRootFile();
            //取得sdcard文件路径
            StatFs statfs = new StatFs(path.getPath());
            //获取block的SIZE
            long blocSize = statfs.getBlockSize();
            //获取BLOCK数量
            long totalBlocks = statfs.getBlockCount();
            //己使用的Block的数量
            long availaBlock = statfs.getAvailableBlocks();
            String[] total = filesize(totalBlocks * blocSize);
            String[] availale = filesize(availaBlock * blocSize);
            //显示SD卡的容量信息
            txt = total[0] + total[1] + "and" + availale[0] + availale[1];
        }
        return txt;
    }
    /*
* -----------------分割线-------------
* */

    //返回数组，下标1代表大小，下标2代表单位 KB/MB
    private String[] filesize(long size) {
        String str = "";
        if (size >= 1024) {
            str = "KB";
            size /= 1024;
            if (size >= 1024) {
                str = "MB";
                size /= 1024;
            }
        }
        DecimalFormat formatter = new DecimalFormat();
        formatter.setGroupingSize(3);
        String result[] = new String[2];
        result[0] = formatter.format(size);
        result[1] = str;
        return result;
    }
}
