package cn.com.startai.awsai.app;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import cn.com.startai.awsai.R;
import cn.com.startai.awsai.task.ImgEndpointTask;
import cn.com.startai.awsai.utils.ImgUtils;

/**
 * author Guoqiang_Sun
 * date 2019/9/3
 * desc
 */
public class ImgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        ImageView img = findViewById(R.id.img);
        ImageView imgo = findViewById(R.id.img_0);

        Bitmap compress = ImgUtils.compress(getResources(), R.mipmap.one, 28, 28);
        imgo.setImageBitmap(compress);

        Bitmap bitmapG = ImgUtils.convertGreyImg(compress);
        img.setImageBitmap(bitmapG);

//        e();
    }

    @SuppressLint("StaticFieldLeak")
    private void e() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.seven);
        new ImgEndpointTask() {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        }.execute(bitmap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
