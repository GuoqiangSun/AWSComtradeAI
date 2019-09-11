package cn.com.startai.awsai.app.fragment;

import android.graphics.Color;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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
public class ComtradeFragment3 extends ComtradeFragmentBase {

    public static BaseFragment newInstance() {
        return new ComtradeFragment3();
    }

    @Override
    protected String getTitle() {
        return "(调试)U+,A+,U*A";
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

        int count = mChannelData.getCount();

        float[] sumu = new float[count];
        float[] suma = new float[count];

        float[] sumuo = new float[count];
        float[] sumao = new float[count];

        ComtradeUtils.PL[] pls = new ComtradeUtils.PL[mConfig.mChannelType.analog_channel_A_count];
        ComtradeUtils.UI[] uis = new ComtradeUtils.UI[mConfig.mChannelType.analog_channel_A_count];

        for (int j = 0; j < mConfig.mChannelType.analog_channel_A_count; j++) {
            pls[j] = ComtradeUtils.PL.of(mConfig.mAnalogChannels[j].ch_id);
            uis[j] = ComtradeUtils.UI.of(mConfig.mAnalogChannels[j].ch_id);
        }

        boolean u0 = false;
        boolean i0 = false;

        for (int i = 0; i < count; i++) {

            for (int j = 0; j < mConfig.mChannelType.analog_channel_A_count; j++) {
                short[] value = values[j];

                ComtradeUtils.PL pl = pls[j];
                ComtradeUtils.UI ui = uis[j];
                switch (pl) {
                    case N: // 零线
                        switch (ui) {
                            case U:
                                sumuo[i] += value[i];
                                u0 = true;
                                break;
                            case I:
                                sumao[i] += value[i];
                                i0 = true;
                                break;
                            default:
                                break;
                        }
                        break;
                    case A://相线
                    case B:
                    case C:
                        switch (ui) {
                            case U:
                                sumu[i] += value[i];
                                break;
                            case I:
                                suma[i] += value[i];
                                break;
                            default:

                                break;
                        }
                        break;
                    default:

                        break;
                }
            }
        }

        LineDataSet u = getData("U+", Color.RED, sumu);
        LineData lineDatau = new LineData(u);
        lineDatau.setValueTypeface(TypefaceUtils.tfregular);
        lines.add(lineDatau);

        LineDataSet a = getData("A+", Color.GREEN, suma);
        LineData lineDataa = new LineData(a);
        lineDataa.setValueTypeface(TypefaceUtils.tfregular);
        lines.add(lineDataa);

        float[] times = new float[count];
        for (int i = 0; i < count; i++) {
            times[i] = sumu[i] * suma[i];
        }
        LineDataSet t = getData("U*A", Color.GREEN, times);
        LineData lineDataTimes = new LineData(t);
        lineDataTimes.setValueTypeface(TypefaceUtils.tfregular);
        lines.add(lineDataTimes);

        if (u0) {
            LineDataSet uo = getData("UO", Color.BLACK, sumuo);
            LineData lineDatauo = new LineData(uo);
            lineDatauo.setValueTypeface(TypefaceUtils.tfregular);
            lines.add(lineDatauo);
        }

        if (i0) {
            LineDataSet ao = getData("AO", Color.BLUE, sumao);
            LineData lineDataao = new LineData(ao);
            lineDataao.setValueTypeface(TypefaceUtils.tfregular);
            lines.add(lineDataao);
        }
    }

}
