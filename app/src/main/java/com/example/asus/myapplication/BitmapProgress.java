package com.example.asus.myapplication;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitmapProgress {

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    public class DownloadWebPicture {
        private Bitmap bmp;

        public Bitmap getImg(){
            return bmp;
        }

        public void handleWebPic(final String url, final Handler handler){
            new Thread(new Runnable(){
                @Override
                public void run() {
                    bmp = getUrlPic(url);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }).start();
        }
        public synchronized Bitmap getUrlPic(String url) {

            Bitmap webImg = null;

            try {
                URL imgUrl = new URL(url);
                HttpURLConnection httpURLConnection
                        = (HttpURLConnection) imgUrl.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                int length = (int) httpURLConnection.getContentLength();
                int tmpLength = 512;
                int readLen = 0,desPos = 0;
                byte[] img = new byte[length];
                byte[] tmp = new byte[tmpLength];
                if (length != -1) {
                    while ((readLen = inputStream.read(tmp)) > 0) {
                        System.arraycopy(tmp, 0, img, desPos, readLen);
                        desPos += readLen;
                    }
                    webImg = BitmapFactory.decodeByteArray(img, 0, img.length);
                    if(desPos != length){
                        throw new IOException("Only read" + desPos +"bytes");
                    }
                }
                httpURLConnection.disconnect();
            }
            catch (IOException e) {
                Log.e("IOException", e.toString());
            }
            return webImg;
        }
    }

}
