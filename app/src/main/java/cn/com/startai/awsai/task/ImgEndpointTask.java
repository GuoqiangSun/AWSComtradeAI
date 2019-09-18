package cn.com.startai.awsai.task;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;

/**
 * author Guoqiang_Sun
 * date 2019/9/3
 * desc
 */
public class ImgEndpointTask extends AsyncTask<Bitmap, Void, String> {

    @Override
    protected String doInBackground(Bitmap... bitmaps) {
        byte[] compress = toCSV(compress(bitmaps[0]));
        return XgbootEndpointTask.digtalReccognitionEndpoint(compress);
    }

    public Bitmap getNewBitmap(Bitmap bitmap, int newWidth, int newHeight) {
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

    private byte[] compress(Bitmap bitmap) {
        System.out.println("-------------compress----------------");

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        System.out.println("width:" + width + " height:" + height);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        int compressSize = 784;

        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        System.out.println(" length:" + baos.toByteArray().length);

        while (baos.toByteArray().length != compressSize) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            System.out.println("options:" + options + " length:" + baos.toByteArray().length);
            if (baos.toByteArray().length > compressSize) {
                options -= 1;
                if (options < 0) {
                    break;
                }
            } else {
                options += 1;
                if (options > 100) {
                    break;
                }
            }
        }

        byte[] bytes = baos.toByteArray();

        width = bitmap.getWidth();
        height = bitmap.getHeight();
        System.out.println("width:" + width + " height:" + height + " length:" + bytes.length);
//        width:456 height:956 length:153117
//        435936
//        153117
        return bytes;
    }

    private byte[] toCSV(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            if (i != bytes.length - 1) {
                sb.append((bytes[i] & 0xFF)).append(",");
            } else {
                sb.append((bytes[i] & 0xFF));
            }
        }
        return sb.toString().getBytes();
    }

}
