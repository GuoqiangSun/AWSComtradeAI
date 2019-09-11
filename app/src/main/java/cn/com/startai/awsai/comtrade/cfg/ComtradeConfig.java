package cn.com.startai.awsai.comtrade.cfg;

import java.io.File;
import java.util.Arrays;

/**
 * author Guoqiang_Sun
 * date 2019/9/6
 * desc
 * <p>
 * station_name，rec_dev_id，rev_year<CR/LF>
 * TT，##A，##D<CR.LF>
 * An，ch_id，ph，ccbm，uu，a，b，skew，min，max，primary，secondary，ps<CR/LF>
 * An，ch_id，ph，ccbm，uu，a，b，skew，min，max，primary，secondary，ps<CR/LF>
 * An，ch_id，ph，ccbm，uu，a，b，skew，min，max，primary，secondary，ps<CR/LF>
 * An，ch_id，ph，ccbm，uu，a，b，skew，min，max，primary，secondary，ps<CR/LF>
 * Dn，ch_id，ph，ccbm，y<CR/LF>
 * Dn，ch_id，ph，ccbm，y<CR/LF>
 * if<CR/LF>
 * nrates<CR/LF>
 * samp，endsamp<CR/LF>
 * samp，endsamp<CR/LF>
 * dd/mm/yyyy，hh:mm:ss.ssssss<CR/LF>
 * dd/mm/yyyy，hh:mm:ss.ssssss<CR/LF>
 * if<CR/LF>
 * timemult<CR/LF>
 */
public class ComtradeConfig {


    public ComtradeConfig() {
    }


    /**
     * 站名、特征和修改年份
     */
    public StationInfo mStationInfo;

    /**
     * 通道的数量和类型
     */
    public ChannelType mChannelType;

    /**
     * 模拟通道信息  如果模拟通道计数=O，便没有模拟通道信息行
     */
    public AnalogChannel[] mAnalogChannels;

    /**
     * 状态（数字）通道信息
     */
    public StateChannel[] mStateChannels;

    /**
     * 线路频率
     * 正常线路频率（Hz）（比如 50，60，33，333）。
     * 非必需，实数，最小长度=零字符，最大长度=32 字符，可以使用标准浮点表示法[4]。
     */
    public float IF;

    /**
     * 采样率信息
     */
    public SampRateInfo mSampRateInfo;

    /**
     * 用于数据文件中第一个数据值的时间
     */
    public TimeDate mTimeDates;

    /**
     * 数据文件类型
     * 判别数据文件类型是 ASCII 还是二进制文件。
     * 数据文件类型。必需，字母，无情况敏感性，最小长度=5 字符，最大长度=6 字符。
     * 仅允许的文字=ASCII 或 ascii，BINARY 或 binary。
     */
    public String ft;

    /**
     * 时间标记乘数系数
     * 数据文件中时差（Timestamp）区的相乘系数。
     * 必需，实数，数字，最小长度=1 字符，最大长度=32 字符，可以使用标准浮点标记法。
     */
    public float timemult;
    //     该区被用作数据文件中的时间标记（timestamp）的乘数系数，
    //     使长时间的记录可以存储在 COMTRADE 格式中。
    //     时间标记的基本单位是微秒。
    //     从数据文件中第一次数据采样至该数据文件中任意时间标记区所标志的采样所经过的时间
    //     是该数据采样的时间标记与配置文件中的时间乘数的乘积（timestamp * timemult）。


    @Override
    public String toString() {
        return "ComtradeConfig{" +
                "\n mStationInfo=" + mStationInfo +
                ",\n mChannelType=" + mChannelType +
                ",\n mAnalogChannels=" + toString(mAnalogChannels) +
                ",\n mStateChannels=" + toString(mStateChannels) +
                ",\n IF=" + IF +
                ",\n mSampRateInfo=" + mSampRateInfo +
                ",\n mTimeDates=" + mTimeDates +
                ",\n ft='" + ft + '\'' +
                ",\n timemult=" + timemult +
                "\n}";
    }

    private static String toString(Object[] a) {
        if (a == null)
            return "null";

        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append("[\n");
        for (int i = 0; ; i++) {
            b.append("  ");
            b.append(String.valueOf(a[i]));
            if (i == iMax)
                return b.append("\n ]").toString();
            b.append(",\n");
        }
    }
}
