package cn.com.startai.awsai.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.math.BigDecimal;

import cn.com.swain.baselib.alg.MathUtils;
import cn.com.swain.baselib.alg.PointS;
import cn.com.swain.baselib.display.ScreenUtils;
import cn.com.swain.baselib.log.Tlog;

/**
 * author Guoqiang_Sun
 * date 2019/8/30
 * desc
 */
public class GrayView extends View {
    public GrayView(Context context) {
        super(context);
        init();
    }

    public GrayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GrayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GrayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private String TAG = "GrayView";
    private final int WIDTH_COUNT = 28;
    private final int HEIGHT_COUNT = 28;
    private final int MAX_COUNT = WIDTH_COUNT * HEIGHT_COUNT;


    private Paint blackPaint = new Paint();
    private Paint whitePaint = new Paint();

    private void init() {
        blackPaint.setAntiAlias(true);
        blackPaint.setColor(Color.BLACK);

        whitePaint.setAntiAlias(true);
        whitePaint.setColor(Color.WHITE);

        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setXfermode(porterDuffXfermodeClear);
    }

    final int[] loc = new int[4];
    RectF rect = new RectF();

    @Override
    protected void onSizeChanged(int mWidth, int mHeight, int oldw, int oldh) {
        super.onSizeChanged(mWidth, mHeight, oldw, oldh);
        Tlog.v(TAG, " onSizeChanged() w:" + mWidth + " h:" + mHeight + " oldw:" + oldw + " oldh:" + oldh);
        ScreenUtils.getLocationInWindow(this, loc);
        Tlog.v(TAG, " onSizeChanged() x:" + loc[0] + " y:" + loc[1] + " w:" + loc[2] + " h:" + loc[3]);

        int width = mWidth;
        int height = mHeight;

        if (height > width) {
            height = width;
        } else {
            width = height;
        }

        float wr = width / (float) WIDTH_COUNT;
        float hr = height / (float) HEIGHT_COUNT;
//        float sqrt = (float) Math.sqrt(wr * wr + hr * hr);
        float sqrt = (wr + hr) / 2;
        blackPaint.setStrokeWidth(sqrt);
        whitePaint.setStrokeWidth(sqrt);

        for (int h = 0; h < HEIGHT_COUNT; h++) {
            for (int w = 0; w < WIDTH_COUNT; w++) {
                xy[(h * WIDTH_COUNT + w) * 2] = wr * w;
                xy[(h * WIDTH_COUNT + w) * 2 + 1] = hr * h;
            }
        }


        rect.left = 0;
        rect.top = 0;
        rect.bottom = height;
        rect.right = width;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Tlog.v(TAG, " onMeasure() ");
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        Tlog.v(TAG, "get width:" + width + " height:" + height);


    }

    private final float[] xy = new float[MAX_COUNT * 2];
    private final float[] grayP = new float[MAX_COUNT];

    private float getDoubleNumber(String str) {
        float number = 0;
        try {
            BigDecimal bd = new BigDecimal(str);
            number = (float) Double.parseDouble(bd.toPlainString());
        } catch (Exception e) {
        }
        return number;
    }

    public void setPoint(String[] strings) {
        System.out.println();
        int length = strings.length;
        System.out.println("GrayView setPoint:: length:" + length);
        for (int i = 0; i < length; i++) {
            String str = strings[i];
            if (null != str && !str.equals("")) {
                grayP[i] = getDoubleNumber(str);

                // print
                if (grayP[i] == 0) {
                    System.out.print("0,");
                } else {
                    System.out.print("1,");
                }
                if ((i + 1) % 28 == 0) {
                    System.out.println();
                }
            }
        }
        postInvalidate();
    }

    private final PorterDuffXfermode porterDuffXfermodeClear = new PorterDuffXfermode(PorterDuff.Mode.SRC);
    private Paint mPaint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        Tlog.v(TAG, " onDraw() ");

        canvas.drawRect(rect, mPaint);

        for (int i = 0; i < MAX_COUNT; i++) {
            Paint p;

            if (grayP[i] == 0) {
                continue;
            } else {
                p = whitePaint;
                p.setAlpha((int) (255 * grayP[i]));
            }
            canvas.drawPoint(xy[i * 2], xy[i * 2 + 1], p);
        }

        Tlog.v(TAG, "end onDraw() ");
    }

}
