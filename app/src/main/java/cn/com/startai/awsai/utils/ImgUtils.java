package cn.com.startai.awsai.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * author Guoqiang_Sun
 * date 2019/9/3
 * desc
 */
public class ImgUtils {

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        // 获得图片的宽高.
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放比例.
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片.
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap compress(Resources res, int id, float ww, float hh) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, id, newOpts);//此时返回bm为空
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inJustDecodeBounds = false;
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        return  BitmapFactory.decodeResource(res, id, newOpts);
    }

    /**
     * 将彩色图转换为灰度图
     *
     * @param img 位图
     * @return 返回转换好的位图
     */
    public static Bitmap convertGreyImg(Bitmap img) {
        int width = img.getWidth();         //获取位图的宽
        int height = img.getHeight();       //获取位图的高

        System.out.println("convertGreyImg width:" + width + " height:" + height);

        int[] pixels = new int[width * height]; //通过位图的大小创建像素点数组

        img.getPixels(pixels, 0, width, 0, 0, width, height);

        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
//            System.out.println("height:" + i);
            System.out.println();
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
                if (grey == -1) {
                    System.out.print(0);
                } else {
                    System.out.print(1);
                }
            }

        }

        System.out.println("convertGreyImg finish");

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }

}
