package cn.com.startai.awsai.task;

import android.os.AsyncTask;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sagemakerruntime.AmazonSageMakerRuntimeClient;
import com.amazonaws.services.sagemakerruntime.model.InvokeEndpointRequest;
import com.amazonaws.services.sagemakerruntime.model.InvokeEndpointResult;

import java.nio.ByteBuffer;

import cn.com.swain.baselib.log.Tlog;

/**
 * author Guoqiang_Sun
 * date 2019/9/3
 * desc 数字识别
 */
public class XgbootEndpointTask extends AsyncTask<byte[], Void, String> {
    public static final String TAG = "XgbootEndpointTask";

    @Override
    protected String doInBackground(byte[]... voids) {
        Tlog.v(TAG, " invokeEndpoint ");
        return digtalReccognitionEndpoint(voids[0]);
    }

    public static String digtalReccognitionEndpoint(byte[] buf) {
        final String AccessKeyId = "AKIAJ4XWTQ2P3AZHIY6A";
        final String SecretKey = "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";
        AWSCredentials awsCredentials = new BasicAWSCredentials(AccessKeyId, SecretKey);
        AmazonSageMakerRuntimeClient client = new AmazonSageMakerRuntimeClient(awsCredentials);
        InvokeEndpointRequest invokeEndpointRequest = new InvokeEndpointRequest();
        invokeEndpointRequest.setEndpointName("xgboost-2019-08-29-08-20-52-772");
        invokeEndpointRequest.setContentType("text/csv");
        ByteBuffer wrap = ByteBuffer.wrap(buf);
        invokeEndpointRequest.setBody(wrap);
        try {
            InvokeEndpointResult invokeEndpointResult =
                    client.invokeEndpoint(invokeEndpointRequest);
            Tlog.v(TAG, " invokeEndpointResult:" + invokeEndpointResult);
            byte[] array = invokeEndpointResult.getBody().array();
            String body = new String(array);
            for (byte b : array) {
                Tlog.v(TAG, " array:" + b + " 0x" + Integer.toHexString(b & 0xFF));
            }
            Tlog.v(TAG, " body:" + body);
            return body;
        } catch (Exception e) {
            Tlog.e(TAG, " invokeEndpoint Exception", e);
            return e.getMessage();
        }
    }

}
