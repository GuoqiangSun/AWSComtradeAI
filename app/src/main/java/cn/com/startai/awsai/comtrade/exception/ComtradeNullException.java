package cn.com.startai.awsai.comtrade.exception;

import java.io.IOException;

/**
 * author Guoqiang_Sun
 * date 2019/9/9
 * desc
 */
public class ComtradeNullException extends IOException {

    public ComtradeNullException() {
        super();
    }

    public ComtradeNullException(String s) {
        super(s);
    }

    public ComtradeNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComtradeNullException(Throwable cause) {
        super(cause);
    }
}
