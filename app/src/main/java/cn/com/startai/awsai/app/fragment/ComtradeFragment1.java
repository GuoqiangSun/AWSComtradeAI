package cn.com.startai.awsai.app.fragment;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import cn.com.swain.comtrade.cfg.ComtradeConfig;
import cn.com.swain.comtrade.dat.ComtradeChannelData;
import cn.com.startai.awsai.typeface.TypefaceUtils;

/**
 * author Guoqiang_Sun
 * date 2019/9/10
 * desc
 */
public class ComtradeFragment1 extends ComtradeFragmentBase {

    public static BaseFragment newInstance() {
        return new ComtradeFragment1();
    }

    @Override
    protected String getTitle() {
        return "单相数据";
    }

    @Override
    protected void runview(ComtradeChannelData mChannelData, ComtradeConfig mConfig) {
        super.runview(mChannelData, mConfig);
        short[][] values = mChannelData.getValues();
        for (int i = 0; i < mConfig.mChannelType.analog_channel_A_count; i++) {
            short[] value = values[i];
            LineDataSet data = getData(mChannelData, mConfig, i, value);
            LineData lineData = new LineData(data);
            lineData.setValueTypeface(TypefaceUtils.tfregular);
            lines.add(lineData);
        }
    }


}
