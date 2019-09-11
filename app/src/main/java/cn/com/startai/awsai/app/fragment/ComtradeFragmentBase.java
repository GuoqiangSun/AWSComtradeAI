package cn.com.startai.awsai.app.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import cn.com.startai.awsai.R;
import cn.com.startai.awsai.app.ComtradeActivity;
import cn.com.startai.awsai.comtrade.CfgData;
import cn.com.startai.awsai.comtrade.cfg.ComtradeConfig;
import cn.com.startai.awsai.comtrade.dat.ComtradeChannelData;
import cn.com.startai.awsai.comtrade.utils.ComtradeUtils;
import cn.com.startai.awsai.typeface.TypefaceUtils;
import cn.com.swain.baselib.display.ScreenUtils;
import cn.com.swain.baselib.log.Tlog;

/**
 * author Guoqiang_Sun
 * date 2019/9/10
 * desc
 */
public class ComtradeFragmentBase extends BaseFragment {

    private ListView lstv;
    protected LineAdapter lineAdapter;

    private Context context;
    public static final String TAG = ComtradeActivity.TAG;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private Handler UIHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIHandler = new Handler(Looper.getMainLooper());
    }

    protected float density;

    protected String getTitle() {
        return "";
    }

    protected final List<LineData> lines = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_comtrade_one, container, false);

        TextView mTitleTxt = v.findViewById(R.id.title);
        mTitleTxt.setText(getTitle());

        lstv = v.findViewById(R.id.lstv);
        lineAdapter = new LineAdapter(context, lines);
        lstv.setAdapter(lineAdapter);
        density = ScreenUtils.getDensity(context);

        return v;
    }

    private CfgData mLastCfgData;

    private Runnable notifyDataSetChanged = new Runnable() {
        @Override
        public void run() {
            if (lineAdapter != null) {
                lineAdapter.notifyDataSetChanged();
            }
        }
    };

    @SuppressLint("StaticFieldLeak")
    @Override
    public void runview(CfgData lastCfgData) {
        if (mLastCfgData != null && mLastCfgData == lastCfgData) {
            return;
        }
        runview(lastCfgData.getChannelData(), lastCfgData.getConfig());
        mLastCfgData = lastCfgData;
        if (UIHandler != null) {
            UIHandler.post(notifyDataSetChanged);
        }
    }

    protected void runview(ComtradeChannelData channelData, ComtradeConfig config) {
        lines.clear();
    }

    public void clear() {
        lines.clear();
        if (UIHandler != null) {
            UIHandler.post(notifyDataSetChanged);
        }
    }

    protected LineDataSet getData(ComtradeChannelData mChannelData, ComtradeConfig mConfig, int j, short[] shorts) {

        int length = shorts.length;

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            float ration = mConfig.mAnalogChannels[j].ration(shorts[i]);
            values.add(new Entry(i, ration));
//            values.add(new Entry(i, shorts[i]));
        }

        Tlog.v(TAG, "--- min" + mConfig.mAnalogChannels[j].ration(mChannelData.getMin(j))
                + " max:" + mConfig.mAnalogChannels[j].ration(mChannelData.getMax(j)));

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, mConfig.mAnalogChannels[j].ch_id);
        set1.setColor(ComtradeUtils.PL.of(mConfig.mAnalogChannels[j].ch_id).color);
        set1.setFillAlpha(255);
        set1.setLineWidth(2f);
        set1.setDrawValues(true);
        set1.setDrawCircles(false);
//        set1.setMode(LineDataSet.Mode.LINEAR);
//        set1.setDrawFilled(false);
        return set1;
    }

    protected LineDataSet getData(String id, int color, float[] shorts) {

        int length = shorts.length;

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            values.add(new Entry(i, shorts[i]));
//            values.add(new Entry(i, shorts[i]));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, id);
        set1.setColor(color);
        set1.setFillAlpha(255);
        set1.setLineWidth(2f);
        set1.setDrawValues(true);
        set1.setDrawCircles(false);
//        set1.setMode(LineDataSet.Mode.LINEAR);
//        set1.setDrawFilled(false);
        return set1;
    }

    protected void init(LineChart chart) {
        chart.setDrawGridBackground(false);

        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);


        Legend l = chart.getLegend();
        l.setTypeface(TypefaceUtils.tflight);
        l.setEnabled(true);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(TypefaceUtils.tflight);
//        leftAxis.setAxisMaximum(1.2f);
//        leftAxis.setAxisMinimum(-1.2f);
        leftAxis.setDrawGridLines(false);

        chart.getAxisRight().setEnabled(false);


        XAxis xAxis = chart.getXAxis();
//        xAxis.setEnabled(false);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(false);

        // don't forget to refresh the drawing
//        chart.invalidate();
    }

    protected void scaleView(LineChart chart) {
    }

    public class LineAdapter extends ArrayAdapter<LineData> {

        LineAdapter(Context context) {
            super(context, 0);
        }

        LineAdapter(Context context, List<LineData> lst) {
            super(context, 0, lst);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {

            LineData data = getItem(position);

            ViewHolder holder;

            if (convertView == null) {

                holder = new ViewHolder();

                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.list_item_linechart, null);
                holder.chart = convertView.findViewById(R.id.chart);
                scaleView(holder.chart);

//                // create marker to display box when values are selected
//                MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
//                // Set the marker to the chart
//                mv.setChartView(holder.chart);
//                holder.chart.setMarker(mv);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            init(holder.chart);
            holder.chart.setData(data);

            holder.chart.animateX(750);

            return convertView;
        }

        private class ViewHolder {
            LineChart chart;
        }
    }
}
