package com.an.base.utils;

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
 * @文件描述：null
 * @修改历史：2016/9/21
 **********************************************************/
public enum DUtilsStorage {
    INSTANCE;

    /***
     * @return boolean
     * @判断内置SDcard是否存在 rom代表内置, 可不判断，因为内置都会有。
     */
    public static boolean checkExistRom() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /***
     * @return boolean
     * @ 获取内置SDcard的路径
     */
    public String getRomPath() {
        String romPath = null;
        if (checkExistRom()) {
            romPath = Environment.getExternalStorageDirectory().getPath();//获取根目录
        }
        return romPath;
    }

    /***
     * @return boolean
     * @ 获取外置SDcard的路径
     */
    public String getSDCardPath() {
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


    /***
     * @return String 总容量and剩余容量
     * @ 计算SD卡剩余容量和总容量
     */
    public String OnCalculator() {
        String txt = null;
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
