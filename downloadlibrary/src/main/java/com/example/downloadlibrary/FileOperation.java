package com.example.downloadlibrary;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 使用该方法之前，请进行权限判断
 *
 * Created by lhl on 2016/9/2.
 */
public class FileOperation {
    private static Context context;
    /*文件保存的目录及文件名称*/
    private static String fileDir = "";
    private static String fileName = "";

    public FileOperation(Context context) {
        this.context = context;
        fileDir = context.getExternalFilesDir("") + "/";
        fileName = "download.apk";
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }



    /**
     * 文件是否存在
     *
     * @return 文件是否存在
     * @throws NullPointerException if {@code fileName == null}.
     */
    public static boolean fileIsExists() {
        return fileIsExists(new File(fileDir, fileName));
    }

    /**
     * 文件是否存在
     *
     * @param file 需要检查的文件
     * @return 存在为true，反之为false
     * @throws NullPointerException if {@code fileName == null}.
     */
    public static boolean fileIsExists(File file) {
        if (!file.exists()) {
            Log.e("FileOperation", "source file not found");
            return false;
        }
        return true;
    }

    /**
     * 删除文件
     *
     * @return 文件是否被删除
     * @throws NullPointerException if {@code fileName == null}.
     */
    public static boolean fileDelete() {
        return fileDelete(new File(fileDir, fileName));
    }

    /**
     * 删除文件
     *
     * @param file 需要删除的文件
     * @return 文件是否被删除
     */
    public static boolean fileDelete(File file) {
        if (fileIsExists()) {
            file.delete();
            return true;
        } else {
            Log.e("FileOperation", "the file is not exist");
            return false;
        }
    }

    /**
     * 移动文件
     *
     * @param destDirName 目的目录完整路径
     * @return 文件移动成功返回true，否则返回false
     * @throws NullPointerException if {@code fileName == null}.
     */
    public static boolean moveFile(String destDirName) {
        return moveFile(destDirName, new File(fileDir, fileName));
    }

    /**
     * 移动文件
     *
     * @param destDirName 移动到的目录地址
     * @param srcFile     需要移动的文件
     * @return 是否被移动
     */
    public static boolean moveFile(String destDirName, File srcFile) {
        if (!srcFile.exists() || !srcFile.isFile()) {
            Log.e("FileOperation", "source file not found");
            return false;
        }
        File destDir = new File(destDirName);
        if (!destDir.exists())
            destDir.mkdirs();
        if (!destDir.exists()) {
            Log.e("FileOperation", "no permission for write or read");
            return false;
        }
        return srcFile.renameTo(new File(destDirName + File.separator + srcFile.getName()));
    }

    /**
     * 获取文件大小
     *
     * @param file 需要获取大小的文件
     * @return 文件大小，单位B
     */
    public static long getFileSize(File file) {
        long size = 0;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            size = fis.available();
        } catch (FileNotFoundException e) {
            Log.e("FileOperation", "file is not found");
            return size;
        } catch (IOException e) {
            Log.e("FileOperation", "file is not available");
            return size;
        }
        return size;
    }

    /**
     * 查看文件大小
     *
     * @return 文件大小 单位B
     * @throws NullPointerException if {@code fileName == null}.
     */
    public static long getFileSize() {
        return getFileSize(new File(fileDir, fileName));
    }

    /**
     * 若为apk，可以调用该函数安装app
     */
    public static boolean openAPK() {
        return openAPK(fileDir, fileName);
    }

    public static boolean openAPK(String fileDir, String fileName) {
        if (!fileIsExists()) {
            Log.e("FileOperation", "the APK doesn't exist");
            return false;
        }
        String extension = fileName.substring(fileName.length() - 3, fileName.length());
        if (!extension.equals("apk")) {
            Log.e("FileOperation", "the file is not a app installation package");
            return false;
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(fileDir, fileName));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        Log.e("FileOperation", "install app");
        context.startActivity(intent);
        return true;
    }
}
