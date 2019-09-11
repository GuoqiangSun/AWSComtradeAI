package cn.com.startai.awsai.app;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;

import java.util.List;

import cn.com.startai.awsai.R;
import cn.com.startai.awsai.lambda.MyInterface;
import cn.com.startai.awsai.lambda.RequestClass;
import cn.com.startai.awsai.lambda.ResponseClass;
import cn.com.startai.awsai.utils.NumberData;
import cn.com.startai.awsai.view.GrayView;
import cn.com.swain.baselib.log.Tlog;

/**
 * author Guoqiang_Sun
 * date 2019/8/23
 * desc
 */
public class LambdaActivity extends AppCompatActivity {

    private String TAG = "DigitalRecognitionActivity";
    private MyInterface myInterface;
    private GrayView mGrayView;
    private TextView mResultTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lambda);
        Tlog.v(TAG, "LambdaActivity onCreate");
        mGrayView = findViewById(R.id.grayview);
        mResultTxt = findViewById(R.id.resultTxt);
        NumberData.getInstance().openExample(getApplicationContext());
        aws();
    }


    private void aws() {

//        // Create an instance of CognitoCachingCredentialsProvider
//        CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
//                this.getApplicationContext(), "us-east-1:30777930-4fa8-4b11-ad81-e85ac0107e3a", Regions.US_WEST_2);
//        // Create LambdaInvokerFactory, to be used to instantiate the Lambda proxy.
//        LambdaInvokerFactory factory = new LambdaInvokerFactory(this.getApplicationContext(),
//                Regions.US_WEST_2, cognitoProvider);


//                "arn:aws:lambda:us-east-1:365907165264:function:aIfunctionTest",

        CognitoCachingCredentialsProvider credentialsProvider;

        String identityPoolId = "us-east-1:939660f1-3966-41d0-b2f2-c584676673eb"; // 身份池 ID;

        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                identityPoolId,
                Regions.US_EAST_1 // 区域
        );

//        String accountId = "365907165264";
//        String authRoleArn = "arn:aws:iam::365907165264:role/lambda-android-role";
//        String unauthRoleArn = "arn:aws:iam::365907165264:role/lambda-android-role";
//
//        credentialsProvider = new CognitoCachingCredentialsProvider(getApplicationContext(),
//                accountId, identityPoolId,
//                unauthRoleArn, authRoleArn,
//                Regions.US_EAST_1);


        LambdaInvokerFactory factory = new LambdaInvokerFactory(this.getApplicationContext(),
                Regions.US_EAST_1, credentialsProvider);


// Create the Lambda proxy object with a default Json data binder.
// You can provide your own data binder by implementing
// LambdaDataBinder.

        myInterface = factory.build(MyInterface.class);

    }

    @SuppressLint("StaticFieldLeak")
    public void AndroidBackendLambdaFunction(View view) {
        RequestClass request = new RequestClass("Android", "Lambda");
        AndroidBackendLambdaFunction(request);
    }

    @SuppressLint("StaticFieldLeak")
    public void aIfunctionTest(View view) {
        RequestClass request = new RequestClass("John", "Doe");
        new AsyncTask<RequestClass, Void, ResponseClass>() {
            @Override
            protected ResponseClass doInBackground(RequestClass... params) {
                // invoke "echo" method. In case it fails, it will throw a
                // LambdaFunctionException.
                try {
                    return myInterface.aIfunctionTest(params[0]);
                } catch (Exception lfe) {
                    Tlog.e(TAG, "Failed to invoke aIfunctionTest", lfe);
                    return new ResponseClass(lfe.getMessage());
                }
            }

            @Override
            protected void onPostExecute(ResponseClass result) {
                Tlog.v(TAG, " onPostExecute " + String.valueOf(result));

                // Do a toast
                Toast.makeText(LambdaActivity.this,
                        result == null ? " onPostExecute fail " : result.getGreetings(),
                        Toast.LENGTH_LONG).show();
            }
        }.execute(request);
    }

    @SuppressLint("StaticFieldLeak")
    public void sageMaker(View view) {
        new AsyncTask<Void, Void, ResponseClass>() {

            @Override
            protected ResponseClass doInBackground(Void... voids) {
                try {
                    return myInterface.sageMaker();
                } catch (Exception lfe) {
                    Tlog.e(TAG, "Failed to invoke aIfunctionTest", lfe);
                    return new ResponseClass(lfe.getMessage());
                }
            }

            @Override
            protected void onPostExecute(ResponseClass result) {
                Tlog.v(TAG, " onPostExecute " + String.valueOf(result));

                // Do a toast
                Toast.makeText(LambdaActivity.this,
                        result == null ? " onPostExecute fail " : result.getGreetings(),
                        Toast.LENGTH_LONG).show();
            }
        }.execute();
    }
    public void selectCSV(View view) {
        String[] cities = NumberData.getInstance().getCities();
        if (cities == null) {
            Toast.makeText(getApplicationContext(), "wait a minute,is loading data", Toast.LENGTH_SHORT).show();
            return;
        }
        if (AndroidBackendLambdaFunctionIsExe) {
            Toast.makeText(getApplicationContext(),
                    "AndroidBackendLambdaFunction is running ,please retry",
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
                AndroidBackendLambdaFunction(new RequestClass(lines.get(which), "2"));
            }
        });
        builder.show();
    }

    volatile  boolean AndroidBackendLambdaFunctionIsExe = false;

    @SuppressLint("StaticFieldLeak")
    private void AndroidBackendLambdaFunction(RequestClass request) {
        // The Lambda function invocation results in a network call.
// Make sure it is not called from the main thread.
        if (AndroidBackendLambdaFunctionIsExe) {
            Toast.makeText(getApplicationContext(),
                    "AndroidBackendLambdaFunction is running ,please retry",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        new AsyncTask<RequestClass, Void, ResponseClass>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mResultTxt.setText("N/A 推理中...");
                AndroidBackendLambdaFunctionIsExe = true;
            }

            @Override
            protected ResponseClass doInBackground(RequestClass... params) {
                // invoke "echo" method. In case it fails, it will throw a
                // LambdaFunctionException.
                try {
                    return myInterface.AndroidBackendLambdaFunction(params[0]);
                } catch (Exception lfe) {
                    Tlog.e(TAG, "Failed to invoke AndroidBackendLambdaFunction", lfe);
                    return new ResponseClass(lfe.getMessage());
                }
            }

            @Override
            protected void onPostExecute(ResponseClass result) {
                Tlog.v(TAG, " onPostExecute " + String.valueOf(result));
                if (null != mResultTxt) {
                    if (result != null) {
                        mResultTxt.setText(result.getGreetings());
                    } else {
                        mResultTxt.setText("FAIL");
                    }
                }
                AndroidBackendLambdaFunctionIsExe = false;
            }
        }.execute(request);
    }

}
