package cn.com.startai.awsai.comtrade;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import cn.com.startai.awsai.comtrade.cfg.AnalogChannel;
import cn.com.startai.awsai.comtrade.cfg.ChannelType;
import cn.com.startai.awsai.comtrade.cfg.ComtradeConfig;
import cn.com.startai.awsai.comtrade.cfg.SampRateInfo;
import cn.com.startai.awsai.comtrade.cfg.StateChannel;
import cn.com.startai.awsai.comtrade.cfg.StationInfo;
import cn.com.startai.awsai.comtrade.cfg.TimeDate;
import cn.com.startai.awsai.comtrade.exception.WrongFormatException;
import cn.com.startai.awsai.comtrade.utils.ComtradeInfo;
import cn.com.startai.awsai.comtrade.utils.ComtradeUtils;

/**
 * author Guoqiang_Sun
 * date 2019/9/6
 * desc
 */
public class CfgWorker {

    public CfgWorker() {
    }

    /**
     * 写入配置文件
     */
    public void write(ComtradeConfig mConfig, File path) throws IOException {
        if (path == null || !path.exists()) {
            throw new FileNotFoundException();
        }
        if (!path.getPath().endsWith(ComtradeInfo.CFG_SUFFIX)
                && !path.getPath().endsWith(ComtradeInfo.CFG_SUFFIX_UPPER)) {
            throw new WrongFormatException("path suffix must endsWith " + ComtradeInfo.CFG_SUFFIX);
        }
        write(mConfig, new BufferedWriter(new FileWriter(path)));
    }

    /**
     * 写入配置
     *
     * @param mConfig 配置属性
     * @param bw      BufferedWriter
     * @throws IOException e
     */
    public void write(ComtradeConfig mConfig, BufferedWriter bw) throws IOException {
        try {
            StationInfo mStationInfo = mConfig.mStationInfo;
            if (mStationInfo != null) {
                mStationInfo.write(bw);
                bw.newLine();
            }

            ChannelType mChannelType = mConfig.mChannelType;
            if (mChannelType != null) {
                mChannelType.write(bw);
                bw.newLine();
            }

            AnalogChannel[] mAnalogChannels = mConfig.mAnalogChannels;
            if (mAnalogChannels != null && mAnalogChannels.length > 0) {
                for (int i = 0; i < mAnalogChannels.length; i++) {
                    AnalogChannel mAnalogChannel = mAnalogChannels[i];
                    mAnalogChannel.write(bw);
                    bw.newLine();
                }
            }

            StateChannel[] mStateChannels = mConfig.mStateChannels;
            if (mStateChannels != null && mStateChannels.length > 0) {
                for (int i = 0; i < mStateChannels.length; i++) {
                    StateChannel mStateChannel = mStateChannels[i];
                    mStateChannel.write(bw);
                    bw.newLine();
                }
            }

            bw.write(String.valueOf(mConfig.IF));
            bw.newLine();

            SampRateInfo mSampRateInfo = mConfig.mSampRateInfo;
            if (mSampRateInfo != null) {
                mSampRateInfo.write(bw);
                bw.newLine();
            }

            TimeDate mTimeDates = mConfig.mTimeDates;
            if (mTimeDates != null) {
                mTimeDates.write(bw);
                bw.newLine();
            }

            bw.write(ComtradeUtils.valueOf(mConfig.ft));
            bw.newLine();

            bw.write(String.valueOf(mConfig.timemult));

            bw.flush();

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 读取配置文件
     *
     * @param path 配置文件路径
     * @return 配置文件结构
     * @throws IOException e
     */
    public ComtradeConfig read(File path) throws IOException {
        if (path == null || !path.exists()) {
            throw new FileNotFoundException();
        }
        if (!path.getPath().endsWith(ComtradeInfo.CFG_SUFFIX)
                && !path.getPath().endsWith(ComtradeInfo.CFG_SUFFIX_UPPER)) {
            throw new WrongFormatException("path suffix must endsWith " + ComtradeInfo.CFG_SUFFIX);
        }
        return read(new BufferedReader(new FileReader(path)));
    }

    /**
     * 读取配置文件
     *
     * @param br BufferedReader
     * @return ComtradeConfig
     * @throws IOException e
     */
    public ComtradeConfig read(BufferedReader br) throws IOException {
        ComtradeConfig config;
        try {
            config = new ComtradeConfig();

            String readLine;
            int analogChannelPoint = 0;
            int stateChannelPoint = 0;
            boolean IFReaded = false; // 已经读过IF了

            while ((readLine = br.readLine()) != null) {
                if (config.mStationInfo == null) {// StationInfo
                    config.mStationInfo = new StationInfo(readLine);
                } else if (config.mChannelType == null) { // ChannelType
                    config.mChannelType = new ChannelType(readLine);
                } else {
                    int analog_channel_a_count = config.mChannelType.analog_channel_A_count;
                    int state_channel_d_count = config.mChannelType.state_channel_D_count;

                    if (analogChannelPoint < analog_channel_a_count) { // AnalogChannel
                        if (analog_channel_a_count != 0 && config.mAnalogChannels == null) {
                            config.mAnalogChannels = new AnalogChannel[analog_channel_a_count];
                        }
                        config.mAnalogChannels[analogChannelPoint] = new AnalogChannel(readLine);
                        analogChannelPoint++;
                    } else if (stateChannelPoint < state_channel_d_count) { // StateChannel

                        if (state_channel_d_count != 0 && config.mStateChannels == null) {
                            config.mStateChannels = new StateChannel[state_channel_d_count];
                        }

                        config.mStateChannels[stateChannelPoint] = new StateChannel(readLine);
                        stateChannelPoint++;
                    } else {
                        if (!IFReaded) {  //IF
                            if (!readLine.equalsIgnoreCase("")) {
                                config.IF = Float.parseFloat(readLine);
                            }
                            IFReaded = true;
                        } else {
                            if (config.mSampRateInfo == null) {
                                String readLine1 = br.readLine();
                                config.mSampRateInfo = new SampRateInfo(readLine, readLine1);
                            } else {
                                if (config.mTimeDates == null) {
                                    String readLine1 = br.readLine();
                                    config.mTimeDates = new TimeDate(readLine, readLine1);
                                } else {
                                    config.ft = readLine;
                                    String s = br.readLine();
                                    if (s != null && !s.equalsIgnoreCase("")) {
                                        config.timemult = Float.parseFloat(s);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return config;
    }

}
