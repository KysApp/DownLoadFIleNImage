package com.example.download.how2use.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.download.R;
import com.example.downloadlibrary.DownloadManager.Download_API9_Upper;
import com.example.downloadlibrary.FileOperation;
import com.example.downloadlibrary.ImageCompression;
import com.example.downloadlibrary.NormalDownload.DownloadReceiver;
import com.example.downloadlibrary.NormalDownload.DownloadService;

import java.io.File;


public class DownloadActivity extends AppCompatActivity {
    private String type = "";
    //显示控件
    private ProgressBar pb_progressbar;
    private TextView tv_download_percentage;
    private ImageView iv_img;
    private ImageView iv_comp_img;
    private Button bt_operation;
    private TextView tv_img_size;
    private TextView tv_comp_img_size;
    //下载id
    private long downloadId = 0;
    private Download_API9_Upper downloadApi9Upper;
    private final String url = "http://m.koznak.tv/download/apk/kys/koznak_kys.apk";
    private final String imgUrl = "http://www.bazirim.tv/data/upload/mobile/special/s0/s0_05263329430906085.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_down_load_app);

        initView();

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        dispachWay();
    }

    /**
     * 初始化进度UI
     */
    private void initView() {
        iv_img = (ImageView)findViewById(R.id.iv_img);
        iv_comp_img = (ImageView)findViewById(R.id.iv_comp_img);
        bt_operation = (Button)findViewById(R.id.bt_operation);
        bt_operation.setOnClickListener(new Listeners());
        tv_img_size = (TextView)findViewById(R.id.tv_img_size);
        tv_comp_img_size = (TextView)findViewById(R.id.tv_comp_img_size);
        pb_progressbar = (ProgressBar) findViewById(R.id.pb_progressbar);
        tv_download_percentage = (TextView) findViewById(R.id.tv_process_percentage);
        //文件操作
        View view = LayoutInflater.from(this).inflate(R.layout.ui_file_operation_test,null);
        view.findViewById(R.id.bt_fileIsExist).setOnClickListener(new Listeners());
        view.findViewById(R.id.bt_fileDelete).setOnClickListener(new Listeners());
        view.findViewById(R.id.bt_fileMove).setOnClickListener(new Listeners());
        view.findViewById(R.id.bt_fileSize).setOnClickListener(new Listeners());
        view.findViewById(R.id.bt_installApp).setOnClickListener(new Listeners());
    }

    private void dispachWay() {
        switch (type) {
            case "下载":
                bt_operation.setText("打开app");
                download();
                break;
            case "下载(支持API9以上)":
                bt_operation.setText("打开app");
                download_API9Upper();
                break;
            case "下载图片":
                bt_operation.setText("图片压缩");
                downloadImage();
                break;
            case "文件操作":
                FileOperation fo = new FileOperation(DownloadActivity.this);
                fo.setFileDir(getExternalFilesDir("")+"");
                fo.setFileName("download.apk");
                break;
        }
    }

    /**
     * 通用下载
     */
    private void download() {
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra("url", url);
        intent.putExtra("receiver", new DownloadReceiver(new UIHandler()));
        startService(intent);
    }

    /**
     * api 9 以上的下载请用该函数
     */
    private void download_API9Upper() {
        downloadApi9Upper = new Download_API9_Upper(DownloadActivity.this);
        try {
            /*通知栏不显示任何提示
            该设置和MediaScanner都需要在api13以上才能设置*/
            downloadApi9Upper.setDownloadingNotifyWay(Download_API9_Upper.VISIBILITY_HIDDEN);
            //设置下载地址，并开始下载
            downloadApi9Upper.download(url);
            //获取本次下载id
            downloadId = downloadApi9Upper.getDownloadId();
            //通过id获得下载进度，并实时更新UI
            downloadApi9Upper.getDownloadPercentage(downloadId,new UIHandler());
        } catch (Exception e) {
            Log.e("DownloadActvity",e.toString());
        }
    }

    /**
     * 图片下载及压缩
     */
    private void downloadImage(){
        downloadApi9Upper = new Download_API9_Upper(DownloadActivity.this);
        try {
            /*通知栏不显示任何提示
            该设置和MediaScanner都需要在api13以上才能设置*/
            downloadApi9Upper.setDownloadingNotifyWay(Download_API9_Upper.VISIBILITY_HIDDEN);
            //设置文件名称，默认为download.apk
            downloadApi9Upper.setFileName("download.jpg");
            //设置下载地址，并开始下载
            downloadApi9Upper.download(imgUrl);
            //获取本次下载id
            downloadId = downloadApi9Upper.getDownloadId();
            //通过id获得下载进度，并实时更新UI
            downloadApi9Upper.getDownloadPercentage(downloadId,new UIHandler());
        } catch (Exception e) {
            Log.e("DownloadActvity",e.toString());
        }
    }

    /**
     * 进度更新类,请将需要进度更新的UI写在handler中
     */
    class UIHandler extends Handler {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    String percentageString = String.format(getResources().getString(R.string.download_percentage), msg.what);
                    tv_download_percentage.setText(percentageString);
                    pb_progressbar.setProgress(msg.what);
                    //若为图片，则显示图片和图片大小
                    if(msg.what == 100) {

                        if (type.equals(getResources().getString(R.string.downloadImage))) {
                            File file = new File(getExternalFilesDir(""), "download.jpg");
                            iv_img.setImageURI(Uri.fromFile(file));
                            iv_img.setVisibility(View.VISIBLE);
                            bt_operation.setVisibility(View.VISIBLE);
                            tv_img_size.setVisibility(View.VISIBLE);
                            tv_img_size.setText(FileOperation.getFileSize(file) / 1024 + " KB");
                        }
                    }
                }
            });
        }
    }
    class Listeners implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_operation:
                    ImageCompression imageCompression = new ImageCompression(DownloadActivity.this);
                    imageCompression.compress(getExternalFilesDir("")+"/download.jpg");
                    if(imageCompression.isCompressed()){
                        File file = new File(getExternalFilesDir("").getAbsolutePath()+"/download_compression.jpg");
                        iv_comp_img.setVisibility(View.VISIBLE);
                        iv_comp_img.setImageURI(Uri.fromFile(file));
                        tv_comp_img_size.setVisibility(View.VISIBLE);
                        tv_comp_img_size.setText(FileOperation.getFileSize(file)/1024+" KB");
                    }
                    break;
                case R.id.bt_fileIsExist:
                    FileOperation.fileIsExists();
                    break;
                case R.id.bt_fileDelete:
                    FileOperation.fileDelete();
                    break;
                case R.id.bt_fileMove:
                    FileOperation.moveFile(Environment.getExternalStorageDirectory()+"");
                    break;
                case R.id.bt_fileSize:
                    FileOperation.getFileSize();
                    break;
                case R.id.bt_installApp:
                    FileOperation.openAPK();
                    break;
            }
        }
    }
}
