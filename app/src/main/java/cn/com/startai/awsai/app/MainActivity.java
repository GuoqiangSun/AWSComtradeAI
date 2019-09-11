package cn.com.startai.awsai.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import cn.com.startai.awsai.R;
import cn.com.startai.awsai.typeface.TypefaceUtils;
import cn.com.swain.baselib.permission.PermissionGroup;
import cn.com.swain.baselib.permission.PermissionSingleton;

/**
 * author Guoqiang_Sun
 * date 2019/9/9
 * desc
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TypefaceUtils.load(getApplicationContext());
        TextView mTestTxt = findViewById(R.id.testtxt);
        TextView mAlgTxt = findViewById(R.id.algtxt);
        mTestTxt.setTypeface(TypefaceUtils.tflight);
        mTestTxt.setText("测试");
        mAlgTxt.setTypeface(TypefaceUtils.tfregular);
        mAlgTxt.setText("算法");
        PermissionSingleton.getInstance().requestPermission(this, PermissionGroup.STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionSingleton.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TypefaceUtils.clear();
    }

    public void skipLambda(View view) {
        startActivity(new Intent(this, LambdaActivity.class));
    }

    public void selectpic(View view) {
        startActivity(new Intent(this, ImgActivity.class));
    }

    public void digitalrec(View view) {
        startActivity(new Intent(this, DigitalRecognitionActivity.class));
    }

    public void comtrade(View view) {
        startActivity(new Intent(this, ComtradeActivity.class));
    }
}
