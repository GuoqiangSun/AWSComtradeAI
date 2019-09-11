package cn.com.startai.awsai.comtrade;

import cn.com.startai.awsai.comtrade.cfg.ComtradeConfig;
import cn.com.startai.awsai.comtrade.dat.ComtradeChannelData;

/**
 * author Guoqiang_Sun
 * date 2019/9/10
 * desc
 */
public class CfgData {

    private final ComtradeConfig config;
    private final ComtradeChannelData mChannelData;

    public CfgData(ComtradeConfig config, ComtradeChannelData mChannelData) {
        this.config = config;
        this.mChannelData = mChannelData;
    }

    public ComtradeConfig getConfig() {
        return config;
    }

    public ComtradeChannelData getChannelData() {
        return mChannelData;
    }
}
