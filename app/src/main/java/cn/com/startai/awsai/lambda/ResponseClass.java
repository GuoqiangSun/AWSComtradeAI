package cn.com.startai.awsai.lambda;

/**
 * author Guoqiang_Sun
 * date 2019/8/23
 * desc
 */
public class ResponseClass {
    String greetings;

    public String getGreetings() {
        return greetings;
    }

    public void setGreetings(String greetings) {
        this.greetings = greetings;
    }

    public ResponseClass(String greetings) {
        this.greetings = greetings;
    }

    public ResponseClass() {
    }

    @Override
    public String toString() {
        return " [greetings:"+greetings+"]";
    }
}
