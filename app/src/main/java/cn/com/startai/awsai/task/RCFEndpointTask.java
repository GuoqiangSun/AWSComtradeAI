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
public class RCFEndpointTask extends AsyncTask<byte[], Void, String> {
    public static final String TAG = "RCFEndpointTask";

    @Override
    protected String doInBackground(byte[]... voids) {
        Tlog.v(TAG, " invokeEndpoint ");
        try {
            return RCFEndpoint(voids[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static String RCFEndpoint(byte[] buf) throws Exception {
        final String AccessKeyId = "AKIAJ4XWTQ2P3AZHIY6A";
        final String SecretKey = "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";
        AWSCredentials awsCredentials = new BasicAWSCredentials(AccessKeyId, SecretKey);
        AmazonSageMakerRuntimeClient client = new AmazonSageMakerRuntimeClient(awsCredentials);
        InvokeEndpointRequest invokeEndpointRequest = new InvokeEndpointRequest();
        invokeEndpointRequest.setEndpointName("randomcutforest-2019-09-06-02-17-21-451");
        invokeEndpointRequest.setContentType("text/csv");
        invokeEndpointRequest.setAccept("application/json");
        invokeEndpointRequest.setBody(ByteBuffer.wrap(buf));
        try {
            InvokeEndpointResult invokeEndpointResult =
                    client.invokeEndpoint(invokeEndpointRequest);
            Tlog.v(TAG, " invokeEndpointResult:" + invokeEndpointResult);
            byte[] array = invokeEndpointResult.getBody().array();
            String body = new String(array);
            Tlog.v(TAG, " body:" + body);
            return body;
        } catch (Exception e) {
            Tlog.e(TAG, " invokeEndpoint Exception", e);
            throw e;
        }
    }

}
