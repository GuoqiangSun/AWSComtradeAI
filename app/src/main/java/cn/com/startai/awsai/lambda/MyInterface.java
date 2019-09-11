package cn.com.startai.awsai.lambda;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

/**
 * author Guoqiang_Sun
 * date 2019/8/23
 * desc
 */
public interface MyInterface {

    /**
     * Invoke the Lambda function "AndroidBackendLambdaFunction".
     * The function name is the method name.
     */
    @LambdaFunction
    ResponseClass AndroidBackendLambdaFunction(RequestClass request);

    @LambdaFunction
    ResponseClass aIfunctionTest(RequestClass request);

    @LambdaFunction
    ResponseClass sageMaker();

}
