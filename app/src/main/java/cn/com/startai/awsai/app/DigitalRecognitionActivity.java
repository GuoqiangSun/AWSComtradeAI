package cn.com.startai.awsai.app;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.com.startai.awsai.R;
import cn.com.startai.awsai.task.XgbootEndpointTask;
import cn.com.startai.awsai.utils.NumberData;
import cn.com.startai.awsai.view.GrayView;
import cn.com.swain.baselib.log.Tlog;

/**
 * author Guoqiang_Sun
 * date 2019/8/23
 * desc
 */
public class DigitalRecognitionActivity extends AppCompatActivity {

    private String TAG = "DigitalRecognitionActivity";
    private GrayView mGrayView;
    private TextView mResultEndpointTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digtal_rec);
        Tlog.v(TAG, "DigitalRecognitionActivity onCreate");
        mGrayView = findViewById(R.id.grayview);
        mResultEndpointTxt = findViewById(R.id.resultEndpointTxt);
        NumberData.getInstance().openExample(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NumberData.getInstance().clear();
    }

    public void endpoint(View view) {
        String[] cities = NumberData.getInstance().getCities();
        if (cities == null) {
            Toast.makeText(getApplicationContext(), "wait a minute,is loading data", Toast.LENGTH_SHORT).show();
            return;
        }
        if (endpoint) {
            Toast.makeText(getApplicationContext(),
                    "digtalReccognitionEndpoint is running ,please retry",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("数字文件总个数:" + String.valueOf(cities.length));
        //    设置一个下拉的列表选择项
        builder.setItems(cities, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String[]> strings = NumberData.getInstance().getStrings();
                if (strings == null || strings.size() <= which) {
                    Toast.makeText(getApplicationContext(), "Please select the smaller index", Toast.LENGTH_SHORT).show();
                    return;
                }
                mGrayView.setPoint(strings.get(which));
                List<String> lines = NumberData.getInstance().getLines();
                if (lines == null || lines.size() <= which) {
                    Toast.makeText(getApplicationContext(), "csv data error...", Toast.LENGTH_SHORT).show();
                    return;
                }
                endpoint(lines.get(which));
            }
        });
        builder.show();
    }

    volatile boolean endpoint = false;

    @SuppressLint("StaticFieldLeak")
    private void endpoint(final String s) {

        new XgbootEndpointTask() {

            long start;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                endpoint = true;
                mResultEndpointTxt.setText("N/A 推理中...");
                start = System.currentTimeMillis();
            }

            @Override
            protected void onPostExecute(String aVoid) {
                super.onPostExecute(aVoid);

                if (null != mResultEndpointTxt) {
                    if (aVoid != null) {
                        double ut = (System.currentTimeMillis() - start) / 1000D;
                        mResultEndpointTxt.setText(aVoid + " (耗时" + ut + "s)");
                    } else {
                        mResultEndpointTxt.setText("FAIL");
                    }
                }
                endpoint = false;

            }
        }.execute(s.getBytes());
    }

}
