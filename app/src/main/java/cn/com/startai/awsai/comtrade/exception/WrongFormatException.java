package cn.com.startai.awsai.comtrade.exception;

import java.io.IOException;

/**
 * author Guoqiang_Sun
 * date 2019/9/6
 * desc
 */
public class WrongFormatException extends IOException {

    public WrongFormatException() {
        super();
    }

    public WrongFormatException(String s) {
        super(s);
    }

    public WrongFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongFormatException(Throwable cause) {
        super(cause);
    }

}
