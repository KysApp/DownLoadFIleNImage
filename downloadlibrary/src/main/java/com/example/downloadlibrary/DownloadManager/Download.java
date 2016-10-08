package com.example.downloadlibrary.DownloadManager;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.example.downloadlibrary.FileOperation;

import java.util.List;

/**
 * attention:
 * 1.该下载方法只有在API 9以上的系统才可以执行
 * 2.只有在API 13以上的系统下，才可以控制是否被系统扫描到和通知方式，
 * 3.本方法可以多任务下载
 * 4.若要显示下载进度，请在开启每个下载之后获取一次下载id，并进行存储，以便写进度的时候获取对应下载的已下载百分比
 * 5.默认存储地址为
 * Created by lhl on 2016/8/31.
 */
public class Download {

    private static Context context;
    /*是否在通知栏上面显示进度*/
    private boolean showDownloading = true;
    /*有进度通知，没有完成通知*/
    public static final int VISIBILITY_VISIBLE = 0;
    /* 有进度通知，有完成通知*/
    public static final int VISIBILITY_VISIBLE_NOTIFY_COMPLETED = 1;
    /**
     * 没有进度通知，没有完成通知
     * 需要系统权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION.
     */
    public static final int VISIBILITY_HIDDEN = 2;
    /* 没有进度通知，有完成通知*/
    public static final int VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION = 3;
    /**
     * can take any of the following values: {@link #VISIBILITY_HIDDEN}
     * {@link #VISIBILITY_VISIBLE_NOTIFY_COMPLETED}, {@link #VISIBILITY_VISIBLE},
     * {@link #VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION}
     */
    private int DownloadingNotifyWay = DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED;
    /*正在下载通知上的标题*/
    private String DownloadingTitle = "title";
    /*正在下载通知上的描述*/
    private String DownloadingDescription = "description";
    /*是否可以被MediaScanner扫描到，默认是不可以被扫描到*/
    private boolean allowScanningByMediaScanner = false;
    /*每个下载的id*/
    private long downloadId = 0;
    /*文件名称*/
    private String fileName = "download.apk";

    public Download(Context context) {
        this.context = context;
    }

    /**
     * 检查DownloadManager是否可用
     *
     * @return 返回值为true的时候可以进行下载
     */
    public static boolean isDownloadManagerAvailable() {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                Log.e("DownloadManager", "can't use this download method on the device");
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setClassName("com.android.providers.downloads.ui", "com.android.providers.downloads.ui.DownloadList");
            List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        } catch (Exception e) {
            Log.e("DownloadManager", "can't use this download method on the device");
            return false;
        }
    }

    /**
     * 传入url即可下载
     *
     * @param url 下载地址
     * @return this
     * @throws Exception 有错误参数时，将抛出异常
     */
    public Download download(String url) throws Exception {
        if (!isDownloadManagerAvailable())
            return this;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription(DownloadingDescription);
        request.setTitle(DownloadingTitle);
        // 要保证被扫描和通知是否隐藏的设置被执行，需要用android3.2以上的版本编译
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //允许被系统扫描到
            if (allowScanningByMediaScanner)
                request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadingNotifyWay);
        } else {
            Log.e("Download_API9_Upper", "this file can't be scanned by media scanner on this device");
            Log.e("Download_API9_Upper", "this file has notification only if this file is downloading on this device");
        }
        //设置下载的地址
        FileOperation fo = new FileOperation(context);
        fo.setFileDir(context.getExternalFilesDir("") + "/");
        fo.setFileName(fileName);
        if (fo.fileIsExists())
            fo.fileDelete();
        request.setDestinationInExternalFilesDir(context, "", fileName);
        // 获取下载服务
        DownloadManager mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        //将下载加入下载队列
        downloadId = mDownloadManager.enqueue(request);
        return this;
    }

    /**
     *  取消一个准备进行的下载，中止一个正在进行的下载，或者删除一个已经完成的下载。
     *
     * @param downloadId 需要取消的下载id
     * @return 取消或删除的下载个数
     */
    public int downloadCancel(long... downloadId) {
        DownloadManager mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        return mDownloadManager.remove(downloadId);
    }

    /**
     * {@link Download#getDownloadPercentage(long, Handler handler)}
     *
     * @return 获取下载id
     */
    public long getDownloadId() {
        return downloadId;
    }

    /**
     * 获取下载进度
     *
     * @param downloadId 需要获得进度的下载id
     * @param handler    activity中更新进度条的UIHandler
     */
    public void getDownloadPercentage(long downloadId, Handler handler) {
        if (!isDownloadManagerAvailable())
            return;
        context.getContentResolver().registerContentObserver(Uri.parse("content://downloads/"), true, new DownloadObserver(handler, context, downloadId));
    }

    public String getFileName() {
        return fileName;
    }

    public Download setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    /*获取文件路径*/
    public String getSavePath() {
        return context.getFilesDir() + "/" + fileName;
    }

    public boolean isAllowScanningByMediaScanner() {
        return allowScanningByMediaScanner;
    }

    public Download setAllowScanningByMediaScanner(boolean allowScanningByMediaScanner) {
        this.allowScanningByMediaScanner = allowScanningByMediaScanner;
        return this;
    }

    public String getDownloadingDescription() {
        return DownloadingDescription;
    }

    public Download setDownloadingDescription(String downloadingDescription) {
        DownloadingDescription = downloadingDescription;
        return this;
    }

    public int getDownloadingNotifyWay() {
        return DownloadingNotifyWay;
    }

    public Download setDownloadingNotifyWay(int downloadingNotifyWay) {
        DownloadingNotifyWay = downloadingNotifyWay;
        return this;
    }

    public String getDownloadingTitle() {
        return DownloadingTitle;
    }

    public Download setDownloadingTitle(String downloadingTitle) {
        DownloadingTitle = downloadingTitle;
        return this;
    }

    public boolean isShowDownloading() {
        return showDownloading;
    }

    public Download setShowDownloading(boolean showDownloading) {
        this.showDownloading = showDownloading;
        return this;
    }


}
