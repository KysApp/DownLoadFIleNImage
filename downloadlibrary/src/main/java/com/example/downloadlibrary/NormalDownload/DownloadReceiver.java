package com.example.downloadlibrary.NormalDownload;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;

/**
 * Created by lhl on 2016/9/5.
 */
@SuppressLint("ParcelCreator")
public class DownloadReceiver extends ResultReceiver {
    private Handler handler;
    private boolean downloading = true;

    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */
    public DownloadReceiver(Handler handler) {
        super(handler);
        this.handler = handler;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        if (resultCode == DownloadService.UPDATE_PROGRESS) {
            int progress = resultData.getInt("progress");
            handler.sendEmptyMessageDelayed(progress, 100);
            if (progress == 100) {
                downloading = false;
            }
        }
    }

    public boolean isDownloading() {
        return downloading;
    }
}
