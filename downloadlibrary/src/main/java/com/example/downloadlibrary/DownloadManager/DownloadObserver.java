package com.example.downloadlibrary.DownloadManager;

import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;

/**
 * Created by lhl on 2016/9/2.
 */
public class DownloadObserver extends ContentObserver {
    private DownloadManager mDownloadManager;
    private DownloadManager.Query query;
    private Cursor cursor;
    private int progress;
    private Handler handler;
    private boolean downloading = true;
    private Context context;
    /**
     * 建立一个内容监听
     *
     * @param handler 用于onChange中的进度改变之后，向handler中传递进度，若没有handler，可设置为null
     * @param context 上下文
     * @param downloadId 下载任务的id
     */
    public DownloadObserver(Handler handler, Context context, long downloadId) {
        super(handler);
        this.handler = handler;
        this.context = context;
        mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        query = new DownloadManager.Query().setFilterById(downloadId);
    }

    /**
     * /data/data/com.android.providers.download/database/database.db 每次变化均会触发onChange
     *
     * @param selfChange 当自动触发onChange的时候返回true.
     */
    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        if (downloading) {
            cursor = mDownloadManager.query(query);
            cursor.moveToFirst();
            int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            progress = (bytes_downloaded * 100) / bytes_total;
            handler.sendEmptyMessageDelayed(progress, 100);
            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                downloading = false;
            }
            cursor.close();
        }
    }

    /**
     * @return 该任务是否正在下载
     */
    public boolean isDownloading(){
        return downloading;
    }
}