package cn.com.startai.awsai.comtrade.dat;

import java.util.Arrays;

/**
 * author Guoqiang_Sun
 * date 2019/9/6
 * desc
 */
public class ComtradeChannelData {

    /**
     * @param count   每个通道的个数
     * @param channel 所有通道 包含模拟通道和状态通道
     */
    public ComtradeChannelData(int count, int channel) {
        this.count = count;
        this.channel = channel;
        this.timestamp = new int[count];
        this.values = new short[channel][count];
        this.mValueIndexS = new int[channel];
        this.mins = new short[channel];
        this.maxs = new short[channel];
        Arrays.fill(mins, Short.MAX_VALUE);
        Arrays.fill(maxs, Short.MIN_VALUE);
    }

    public void reset() {
        Arrays.fill(timestamp, 0);
        short a = 0;
        for (int i = 0; i < values.length; i++) {
            Arrays.fill(values[i], a);
        }
        Arrays.fill(mValueIndexS, 0);
        Arrays.fill(mins, Short.MAX_VALUE);
        Arrays.fill(maxs, Short.MIN_VALUE);
    }

    private int count;
    private int channel;
    private int[] timestamp;
    private short[][] values;
    private short[] mins;
    private short[] maxs;

    public int getCount(){
        return count;
    }

    public int getChannel(){
        return channel;
    }

    public short[] getMins() {
        return mins;
    }

    public short[] getMaxs() {
        return maxs;
    }

    public short getMin(int channel) {
        return mins[channel];
    }

    public short getMax(int channel) {
        return maxs[channel];
    }

    /**
     * 毫秒时间戳（去除年月日的时间）
     */
    public int[] getTs() {
        return timestamp;
    }

    /**
     * 所有渠道数据
     * 一维表示渠道下标
     * 二维表示渠道数据
     */
    public short[][] getValues() {
        return values;
    }

    /**
     * 获取渠道数据
     *
     * @param channel 渠道id
     * @return 数据
     */
    public short[] getChannleValue(int channel) {
        return values[channel];
    }

    private int[] mValueIndexS;

    /**
     * 设置渠道
     *
     * @param channel 渠道下标
     * @param value   渠道值
     */
    public void setValue(int channel, short value) {
        setValue(channel, mValueIndexS[channel], value);
        mValueIndexS[channel]++;
    }

    public void setValue(int channel, int index, short value) {
        if (value < mins[channel]) {
            mins[channel] = value;
        } else if (value > maxs[channel]) {
            maxs[channel] = value;
        }
        values[channel][index] = value;
    }

    private int mTsIndex = 0;

    public void setTimestamp(int value) {
        setTimestamp(mTsIndex, value);
        mTsIndex++;
    }

    public void setTimestamp(int index, int value) {
        timestamp[index] = value;
    }

    @Override
    public String toString() {
        return "ComtradeChannelData{" +
                "count=" + count +
                ", channel=" + channel +
                ", mValueIndexS=" + Arrays.toString(mValueIndexS) +
                ", mTsIndex=" + mTsIndex +
                ", \ntimestamp=" + tsToStr() +
                ", \nvalues=\n" + valuesToStr() +
                '}';
    }

    private String tsToStr() {
        return timestamp[0] + "," + timestamp[1] + "," + timestamp[2]
                + "......" + timestamp[count - 3] + "," + timestamp[count - 2] + "," + timestamp[count - 1];
    }

    private String valuesToStr() {

        StringBuffer sb = new StringBuffer(128 * channel);


        for (int i = 0; i < channel; i++) {

            sb.append("[").append(i).append("]=");
            sb.append(values[i][0]).append(",").append(values[i][1]).append(",").append(values[i][2]);
            sb.append("......");
            sb.append(values[i][count - 3]).append(",").append(values[i][count - 2])
                    .append(",").append(values[i][count - 1]).append(".\n");

        }

        return sb.toString();

    }
}
