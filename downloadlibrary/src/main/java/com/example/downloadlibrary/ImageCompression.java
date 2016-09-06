package com.example.downloadlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lhl on 2016/8/31.
 */
public class ImageCompression {
    private Context context;
    //获取屏幕宽高
    private static int defaultDisplayH;
    private static int defaultDisplayW;
    //是否成功被压缩并保存
    private boolean isCompressed = true;
    //文件最大大小
    private int maxSize = 45;
    //另存为的文件名称
    private String fileName = "download_compression.jpg";
    //另存为的文件
    private File file;

    public ImageCompression(Context context) {
        this.context = context;
        init();
    }

    public ImageCompression(Context context, int maxSize) {
        this.context = context;
        this.maxSize = maxSize;
        init();
    }

    private void init() {
        defaultDisplayH = getScreenHeight(context);
        defaultDisplayW = getScreenWidth(context);
        String outputFileDir = context.getExternalFilesDir("") + "";
        String fileName = "download_compression.jpg";
        if (!outputFileDir.equals("")) {
            file = new File(outputFileDir, fileName);
        }
    }

    public boolean isCompressed() {
        return isCompressed;
    }

    public int getMaxSize() {
        return maxSize;
    }

    /**
     * 屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        return width;
    }

    /**
     * 屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        return height;
    }

    /**
     * @param srcPath
     */
    public void compress(String srcPath) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bitmap;
        opts.inJustDecodeBounds = false;
        int w = opts.outWidth;
        int h = opts.outHeight;
        int size = 0;
        if (w <= defaultDisplayW && h <= defaultDisplayH) {
            size = 1;
        } else {
            double scale = w >= h ? w / defaultDisplayW : h / defaultDisplayH;
            double log = Math.log(scale) / Math.log(2);
            double logCeil = Math.ceil(log);
            size = (int) Math.pow(2, logCeil);
        }
        opts.inSampleSize = size;
        bitmap = BitmapFactory.decodeFile(srcPath, opts);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        System.out.println(baos.toByteArray().length);
        while (baos.toByteArray().length > maxSize * 1024) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 10;
            System.out.println(baos.toByteArray().length);
        }
        try {
            baos.writeTo(new FileOutputStream(file));
        } catch (Exception e) {
            isCompressed = false;
            e.printStackTrace();
        } finally {
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                isCompressed = false;
                e.printStackTrace();
            }
        }
    }
}
