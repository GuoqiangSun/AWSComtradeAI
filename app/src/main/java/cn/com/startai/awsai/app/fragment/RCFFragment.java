package cn.com.startai.awsai.app.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.startai.awsai.R;
import cn.com.startai.awsai.app.ComtradeActivity;
import cn.com.startai.awsai.task.RCFEndpointTask;
import cn.com.startai.awsai.typeface.TypefaceUtils;
import cn.com.swain.baselib.display.ScreenUtils;
import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.comtrade.CfgData;
import cn.com.swain.comtrade.ComtradeWorker;
import cn.com.swain.comtrade.cfg.AnalogChannel;
import cn.com.swain.comtrade.cfg.ComtradeConfig;
import cn.com.swain.comtrade.dat.ComtradeChannelData;
import cn.com.swain.comtrade.utils.ComtradeUtils;

/**
 * author Guoqiang_Sun
 * date 2019/9/10
 * desc
 */
public class RCFFragment extends BaseFragment {

    private ListView lstv;
    protected LineAdapter lineAdapter;

    private Context context;
    public static final String TAG = ComtradeActivity.TAG;

    public static BaseFragment newInstance() {
        return new RCFFragment();
    }

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
        return "RCF";
    }

    protected final List<String> lines = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_comtrade_one, container, false);

        TextView mTitleTxt = v.findViewById(R.id.title);
        mTitleTxt.setText(getTitle());

        lstv = v.findViewById(R.id.lstv);
        lineAdapter = new LineAdapter(context, lines);
        lstv.setAdapter(lineAdapter);
        AdapterView.OnItemClickListener listener = getItemClick();
        if (listener != null) {
            lstv.setOnItemClickListener(listener);
        }
        density = ScreenUtils.getDensity(context);

        return v;
    }

    protected AdapterView.OnItemClickListener getItemClick() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                invokeEndpoint(mLastCfgData, position);
            }
        };
    }


    @SuppressLint("StaticFieldLeak")
    private void invokeEndpoint(CfgData mCfgData, int position) {
        Tlog.v(TAG, " invokeEndpoint:: " + position);
        if (mCfgData == null) {
            return;
        }
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                ByteArrayOutputStream byteArrayOutputStream = null;
                try {
                    ComtradeConfig config = mCfgData.getConfig();
                    ComtradeChannelData channelData = mCfgData.getChannelData();
                    ComtradeWorker worker = new ComtradeWorker();
                    Tlog.v(TAG, " analogDatRationToCsv... ");
                    byteArrayOutputStream = worker.analogDatRationToCsv(config, channelData, position);
                } catch (IOException e) {
                    e.printStackTrace();
                    Tlog.e(TAG, "analogDatRationToCsv::", e);
                }

                if (byteArrayOutputStream == null) {
                    return null;
                }

                try {
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    Tlog.v(TAG, new String(bytes));
                    String s = RCFEndpointTask.RCFEndpoint(bytes);
                    Tlog.v(TAG, "RCFEndpoint::" + s);
                } catch (Exception e) {
                    e.printStackTrace();
                    Tlog.e(TAG, "RCFEndpoint::", e);
                }

                return null;
            }
        }.execute();
    }

    protected CfgData mLastCfgData;

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
        AnalogChannel[] mAnalogChannels = config.mAnalogChannels;
        int length = mAnalogChannels.length;
        for (int i = 0; i < length; i++) {
            AnalogChannel mAnalogChannel = mAnalogChannels[i];
            lines.add(mAnalogChannel.An + "_" + mAnalogChannel.ch_id + "_" + mAnalogChannel.ph);
        }
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


    public class LineAdapter extends ArrayAdapter<String> {

        LineAdapter(Context context, List<String> lst) {
            super(context, 0, lst);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {

            String data = getItem(position);

            ViewHolder holder;

            if (convertView == null) {

                holder = new ViewHolder();

                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.recy_item_txt, null);
                holder.chart = (TextView) convertView;

//                // create marker to display box when values are selected
//                MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
//                // Set the marker to the chart
//                mv.setChartView(holder.chart);
//                holder.chart.setMarker(mv);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.chart.setText(data);

            return convertView;
        }

        private class ViewHolder {
            TextView chart;
        }
    }
}
