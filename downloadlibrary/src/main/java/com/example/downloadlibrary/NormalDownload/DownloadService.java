package com.example.downloadlibrary.NormalDownload;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by lhl on 2016/9/5.
 */
public class DownloadService extends IntentService {
    public static final int UPDATE_PROGRESS = 8344;
    //是否进行了下载操作
    private boolean download = true;
    //是否需要进度条
    private boolean needProgressBar = true;

    public boolean isDownload() {
        return download;
    }

    public boolean isNeedProgressBar() {
        return needProgressBar;
    }

    public void setNeedProgressBar(boolean needProgressBar) {
        this.needProgressBar = needProgressBar;
    }

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String urlToDownload = intent.getStringExtra("url");
        try {
            URL url = new URL(urlToDownload);
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();
            // download the file
            InputStream input = new BufferedInputStream(connection.getInputStream());
            FileOutputStream output = new FileOutputStream(getExternalFilesDir("").getAbsolutePath() + "/download.apk");
            if (needProgressBar) {
                ResultReceiver receiver = intent.getParcelableExtra("receiver");
                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    Bundle resultData = new Bundle();
                    resultData.putInt("progress", (int) (total * 100 / fileLength));
                    receiver.send(UPDATE_PROGRESS, resultData);
                    output.write(data, 0, count);
                }
                Bundle resultData = new Bundle();
                resultData.putInt("progress", 100);
                receiver.send(UPDATE_PROGRESS, resultData);
            }
            output.flush();
            output.close();
            input.close();
        } catch (IOException e) {
            download = false;
            e.printStackTrace();
        }

    }

}

