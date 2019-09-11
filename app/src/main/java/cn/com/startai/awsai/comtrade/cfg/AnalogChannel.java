package cn.com.startai.awsai.comtrade.cfg;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

import cn.com.startai.awsai.comtrade.exception.ComtradeNullException;
import cn.com.startai.awsai.comtrade.exception.WrongFormatException;
import cn.com.startai.awsai.comtrade.utils.ComtradeInfo;
import cn.com.startai.awsai.comtrade.utils.ComtradeUtils;

/**
 * author Guoqiang_Sun
 * date 2019/9/6
 * desc 模拟通道信息  如果模拟通道计数=O，便没有模拟通道信息行
 */
public class AnalogChannel {

    public AnalogChannel() {

    }


    public AnalogChannel(String line) throws WrongFormatException, ComtradeNullException {
        String[] split = ComtradeUtils.split(line);
        ComtradeUtils.checkLength(split, 13);
        An = Integer.parseInt(split[0]);
        ch_id = split[1];
        ph = split[2];
        ccbm = split[3];
        uu = split[4];
        a = Float.parseFloat(split[5]);
        b = Float.parseFloat(split[6]);
        skew = split[7];
        min = Integer.parseInt(split[8]);
        max = Integer.parseInt(split[9]);
        primary = Float.parseFloat(split[10]);
        secondary = Float.parseFloat(split[11]);
        ps = split[12];
    }

    public void write(BufferedWriter mWriter) throws IOException {
        mWriter.write(String.valueOf(An));
        mWriter.write(ComtradeInfo.CFG_LINE_SPLIT);
        mWriter.write(ComtradeUtils.valueOf(ch_id));
        mWriter.write(ComtradeInfo.CFG_LINE_SPLIT);
        mWriter.write(ComtradeUtils.valueOf(ph));
        mWriter.write(ComtradeInfo.CFG_LINE_SPLIT);
        mWriter.write(ComtradeUtils.valueOf(ccbm));
        mWriter.write(ComtradeInfo.CFG_LINE_SPLIT);
        mWriter.write(ComtradeUtils.valueOf(uu));
        mWriter.write(ComtradeInfo.CFG_LINE_SPLIT);
        mWriter.write(String.valueOf(a));
        mWriter.write(ComtradeInfo.CFG_LINE_SPLIT);
        mWriter.write(String.valueOf(b));
        mWriter.write(ComtradeInfo.CFG_LINE_SPLIT);
        mWriter.write(String.valueOf(skew));
        mWriter.write(ComtradeInfo.CFG_LINE_SPLIT);
        mWriter.write(String.valueOf(min));
        mWriter.write(ComtradeInfo.CFG_LINE_SPLIT);
        mWriter.write(String.valueOf(max));
        mWriter.write(ComtradeInfo.CFG_LINE_SPLIT);
        mWriter.write(String.valueOf(primary));
        mWriter.write(ComtradeInfo.CFG_LINE_SPLIT);
        mWriter.write(String.valueOf(secondary));
        mWriter.write(ComtradeInfo.CFG_LINE_SPLIT);
        mWriter.write(String.valueOf(ps));
    }

    public String toLineStr() {
        return String.valueOf(An) +
                ComtradeInfo.CFG_LINE_SPLIT +
                ComtradeUtils.valueOf(ch_id) +
                ComtradeInfo.CFG_LINE_SPLIT +
                ComtradeUtils.valueOf(ph) +
                ComtradeInfo.CFG_LINE_SPLIT +
                ComtradeUtils.valueOf(ccbm) +
                ComtradeInfo.CFG_LINE_SPLIT +
                ComtradeUtils.valueOf(uu) +
                ComtradeInfo.CFG_LINE_SPLIT +
                String.valueOf(a) +
                ComtradeInfo.CFG_LINE_SPLIT +
                String.valueOf(b) +
                ComtradeInfo.CFG_LINE_SPLIT +
                String.valueOf(skew) +
                ComtradeInfo.CFG_LINE_SPLIT +
                String.valueOf(min) +
                ComtradeInfo.CFG_LINE_SPLIT +
                String.valueOf(max) +
                ComtradeInfo.CFG_LINE_SPLIT +
                String.valueOf(primary) +
                ComtradeInfo.CFG_LINE_SPLIT +
                String.valueOf(secondary) +
                ComtradeInfo.CFG_LINE_SPLIT +
                String.valueOf(ps);
    }

    /**
     * 模拟通道索引号。必需，数字，整数，
     * 最小长度=1 字符，最大长度=6 字符，最小值=1，最大值=999999。
     * 不需要引导零或空格。
     * 从 1 至总数顺序计数模拟通道（##A），不记录装置通道数量。
     */
    public int An;

    /**
     * —通道识别符。非必需，字母数字，最小长度=零字符，最大长度=64字符。
     */
    public String ch_id;

    /**
     * 通道相位特征。非必需，字母数字，最小长度=零字符，最大长度=2字符。
     */
    public String ph;


    /**
     * 被监视的回路元件。非必需，字母数字，最小长度=零字符，最大长度=64字符。
     */
    public String ccbm;

    /**
     * 通道单位（比如 kV，V，kA，A）必需，字母，最小长度=1字符，最大长度=32字符。
     * <p>
     * 物理量的单位必须使用 IEEE/ ANSI 或IEC[4，5]标准所规定的标准名称或缩写，
     * 如果这样的标准名称存在的话。数字乘数不应包括在内。
     * 标准倍数如 k（千），m（千分之一），M（百万）等等可以被使用。
     */
    public String uu;


    /**
     * 通道乘数。必需，实数，数字，最小长度=1字符，最大长度=32字符。可以使用标准浮点表示法[4]。
     */
    public float a;

    /**
     * 通道偏移加数。必需，实数，最小长度=1字符，最大长度=32字符，可以使用标准浮点表示法[4]。
     */
    public float b;

//    通道转换系数是 ax+b。
//    数据（.DAT）文件中的存储数据值 x
//    与上面规定的单位（uu）中的（ax+b）的抽样值相对应。
//    它遵循数学分折的规则，比如数据抽样数“x”乘以增益系数“a”，然后加上偏移系数“b”。

    /**
     * —从抽样周期开始后的通道时滞（μs）。
     * 非必需，实数，最小长度=1字符，最大长度 = 32字符。可以使用标准浮点表示方法[4]。
     */
    public String skew;

//   该区提供关于在一个记录的抽样周期内通道抽样之间的时差的信息。例如，在一个带
//   A/D 转换器、无同步抽样、采样率为 1毫秒的 8通道装置内，第一个采样在由“时间标记”
//   所表示的时间，其余通道在每一采样周期内的采样时间可以是 125μ秒以下。在此情况下，
//   顺序通道的时滞是 0；125；250；375……等。

    /**
     * —该通道数据值的最小数据值（可能数据范围的最小极限）。
     * 必需，整数，数字，最小长度=1字符，最大长度=6字符，
     * 最小值=-999999，最大值=999999（在二进制数据文件中，数据值范围限制于-32767至 32767）。
     */
    public int min;


    /**
     * 该通道数据值最大数据值范围（可能数据值范围的最大极限）。
     * 必需，整数，数字，最小长度=1字符，最大长度=6字符，
     * 最小值=-99999，最大值=99999（在二进制数据文件中，数据值范围限制于-32767至 32767）。
     */
    public int max;

    /**
     * 通道电压或电流变换比一次系数，必需，实数，数字，最小长度=1字符，最大长度=32
     */
    public float primary;

    /**
     * —通道电压或电流变换比二次系数，必需，实数，数字，最小长度=1字符，最大长度=32。
     */
    public float secondary;

    /**
     * 如同从等同通道转换系数 ax+b 得到的数值的特征符将返回一个一次（P）或二次（S）值。
     * 必需，字母，最小长度=1 字符，最大长度=1 字符。仅有的有效字符为：P，P，S， S。
     */
    public String ps;

//    数据文件中的数据，通道转换系数和通道单位，可以指一次或二次单位。单位为 kV 的
//    通道的 345kV 比 120V 的变换器的一次系数为 345、二次系数为 0、12（345，0，12）。一次
//    或二次变量（PS）作为在要求一次或二次值和提供变换值之处计算等价的一次或二次值的
//    方法被提供。如果数据是在没有一次/二次关系的环境下产生，比如一个模拟电力系统模拟
//    器，一次与二次的变比应被定为 1，1。从 ax+b 的等式中确定了一次（P）或二次（S）值之
//    后，用户就可确定为分析或重放所需的值。

//    等式 ax+b 提供
//    要求值 一次 二次
//    一次 使用值 乘以一次值、除以二二次 除以一次值、乘以二次值 使用值

    public float ration(short value) {
        return a * value + b;
    }

    @Override
    public String toString() {
        return "AnalogChannel{" +
                "An=" + An +
                ", ch_id='" + ch_id + '\'' +
                ", ph='" + ph + '\'' +
                ", ccbm='" + ccbm + '\'' +
                ", uu='" + uu + '\'' +
                ", a=" + a +
                ", b=" + b +
                ", skew=" + skew +
                ", min=" + min +
                ", max=" + max +
                ", primary=" + primary +
                ", secondary=" + secondary +
                "}";
    }

}
