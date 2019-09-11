package cn.com.startai.awsai.comtrade;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.com.startai.awsai.comtrade.cfg.ComtradeConfig;
import cn.com.startai.awsai.comtrade.dat.ComtradeChannelData;
import cn.com.startai.awsai.comtrade.dat.ComtradeDatBinary;
import cn.com.startai.awsai.comtrade.exception.ComtradeNullException;
import cn.com.startai.awsai.comtrade.exception.WrongFormatException;
import cn.com.startai.awsai.comtrade.utils.ComtradeInfo;

/**
 * author Guoqiang_Sun
 * date 2019/9/6
 * desc
 */
public class DatWorker {

    public DatWorker() {
    }

    /**
     * 读取二进制通道数据
     *
     * @param config  配置属性
     * @param datPath dat文件路径
     * @return ComtradeChannelData
     * @throws IOException e
     */
    public ComtradeChannelData readBinaryChannelDat(ComtradeConfig config, File datPath) throws IOException {
        if (config == null) {
            throw new ComtradeNullException("ComtradeConfig must not be null");
        }
        if (!config.ft.equalsIgnoreCase(ComtradeInfo.BINARY)) {
            throw new WrongFormatException("config.ft must be " + ComtradeInfo.BINARY);
        }
        return readBinaryChannelDat(config, new BufferedInputStream(new FileInputStream(datPath)));
    }

    /**
     * 读取二进制通道数据
     *
     * @param config 配置属性
     * @param bisDat dat输入流
     * @return ComtradeChannelData
     * @throws IOException e
     */
    public ComtradeChannelData readBinaryChannelDat(ComtradeConfig config,
                                                    BufferedInputStream bisDat) throws IOException {
        if (config == null) {
            throw new ComtradeNullException("ComtradeConfig must not be null");
        }
        if (!config.ft.equalsIgnoreCase(ComtradeInfo.BINARY)) {
            throw new WrongFormatException("config.ft must be " + ComtradeInfo.BINARY);
        }
        ComtradeChannelData mChannelData;

        try {

            int count = config.mChannelType.analog_channel_A_count
                    + config.mChannelType.state_channel_D_count;
            mChannelData = new ComtradeChannelData(config.mSampRateInfo.endsamp, count);

            ComtradeDatBinary binary = new ComtradeDatBinary(count);

            while (binary.read(bisDat) != -1) {
                mChannelData.setTimestamp(binary.timestamp);
                short[] channelValue = binary.channelValue;
                for (int i = 0; i < count; i++) {
                    mChannelData.setValue(i, channelValue[i]);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (bisDat != null) {
                try {
                    bisDat.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return mChannelData;
    }


}
