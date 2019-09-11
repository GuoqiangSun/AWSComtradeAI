package cn.com.startai.awsai.typeface;

import android.content.Context;
import android.graphics.Typeface;

/**
 * author Guoqiang_Sun
 * date 2019/9/10
 * desc
 */
public class TypefaceUtils {

    public static Typeface tfregular;
    public static Typeface tflight;

    public static void load(Context context) {
        tfregular = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
        tflight = Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");
    }

    public static void clear() {
        tfregular = null;
        tflight = null;
    }

}
