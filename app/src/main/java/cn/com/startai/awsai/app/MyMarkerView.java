
package cn.com.startai.awsai.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import cn.com.startai.awsai.R;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ViewConstructor")
public class MyMarkerView extends MarkerView {

    private final TextView tvContent;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = findViewById(R.id.tvContent);
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText(Utils.formatNumber(e.getX(), 0, true)
                + "," +
                Utils.formatNumber(e.getY(), 0, true));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
