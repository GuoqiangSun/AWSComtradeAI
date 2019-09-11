package cn.com.startai.awsai.app.fragment;

import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import cn.com.startai.awsai.comtrade.cfg.ComtradeConfig;
import cn.com.startai.awsai.comtrade.dat.ComtradeChannelData;
import cn.com.startai.awsai.comtrade.utils.ComtradeUtils;
import cn.com.startai.awsai.typeface.TypefaceUtils;
import cn.com.swain.baselib.display.ScreenUtils;

/**
 * author Guoqiang_Sun
 * date 2019/9/10
 * desc
 */
public class ComtradeFragment2 extends ComtradeFragmentBase {

    public static BaseFragment newInstance() {
        return new ComtradeFragment2();
    }

    @Override
    protected String getTitle() {
        return "U,A";
    }

    @Override
    protected void scaleView(LineChart chart) {
        super.scaleView(chart);
        chart.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        ScreenUtils.dip2px(300, density)));
    }

    @Override
    protected void runview(ComtradeChannelData mChannelData, ComtradeConfig mConfig) {
        super.runview(mChannelData, mConfig);
        short[][] values = mChannelData.getValues();

        ArrayList<ILineDataSet> setsu = new ArrayList<>();
        ArrayList<ILineDataSet> setsa = new ArrayList<>();
        ArrayList<ILineDataSet> setso = new ArrayList<>();

        for (int i = 0; i < mConfig.mChannelType.analog_channel_A_count; i++) {
            // create a data object with the data sets

            short[] value = values[i];
            LineDataSet data = getData(mChannelData, mConfig, i, value);

            ComtradeUtils.UI of = ComtradeUtils.UI.of(mConfig.mAnalogChannels[i].ch_id);
            switch (of) {
                case U:
                    setsu.add(data);
                    break;
                case I:
                    setsa.add(data);
                    break;
                default:
                    setso.add(data);
                    break;
            }
        }
        if (setsu.size() > 0) {
            LineData lineDatau = new LineData(setsu);
            lineDatau.setValueTypeface(TypefaceUtils.tfregular);
            lines.add(lineDatau);
        }
        if (setsa.size() > 0) {
            LineData lineDataa = new LineData(setsa);
            lineDataa.setValueTypeface(TypefaceUtils.tfregular);
            lines.add(lineDataa);
        }
        if (setso.size() > 0) {
            LineData lineDatao = new LineData(setso);
            lineDatao.setValueTypeface(TypefaceUtils.tfregular);
            lines.add(lineDatao);
        }
    }

}
