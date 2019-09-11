package cn.com.startai.awsai.comtrade.cfg;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

import cn.com.startai.awsai.comtrade.exception.WrongFormatException;
import cn.com.startai.awsai.comtrade.utils.ComtradeUtils;

/**
 * author Guoqiang_Sun
 * date 2019/9/6
 * desc
 * 状态通道的正常状态不承载关于状态信号的物理表征的信息，不论它是一个触点（断开
 * 或闭合）或电压（有电或无电）。其目的是定义 1 代表正常或不正常状态。
 */
public class StateChannel {

    public StateChannel() {
    }

    public StateChannel(String line) throws WrongFormatException {
        String[] split = ComtradeUtils.split(line);
        ComtradeUtils.checkLength(split, 5);
        Dn = Integer.parseInt(split[0]);
        ch_id = split[1];
        ph = split[2];
        ccbm = split[3];
        y = Integer.parseInt(split[4]);
    }


    public void write(BufferedWriter mWriter) throws IOException {
        mWriter.write(String.valueOf(Dn));
        mWriter.write(",");
        mWriter.write(ComtradeUtils.valueOf(ch_id));
        mWriter.write(",");
        mWriter.write(ComtradeUtils.valueOf(ph));
        mWriter.write(",");
        mWriter.write(ComtradeUtils.valueOf(ccbm));
        mWriter.write(",");
        mWriter.write(String.valueOf(y));
    }

    /**
     * 状态通道索引号，必需，整数，数字，最小长度=1 字符，最大长度=6 字符，
     * 最小值=1，最大值=999999。不需要导引零或空格。
     * 从 1 顺序计数至状态通道（##D）总数， 不计装置通道数量。
     */
    public int Dn;

    /**
     * 通道名，非必需，字母，最小长度=0 字符，最大长度=32 字符。
     */
    public String ch_id;

    /**
     * 通道相位特征。非必需，字母数字，最小长度=零字符，最大长度=2 字符。
     */
    public String ph;

    /**
     * 被监视回路元件。非必需，字母数字，最小长度=零字符，最大长度=64 字符。
     */
    public String ccbm;

    /**
     * 当一次器件处于稳态“服务”条件时作为输入状态的状态通道的状态（仅用于状态通道）。
     * 必需，整数，数字，最小长度=1 字符，最大长度=1 字符。仅有的有效值为 0 或 1。
     */
    public int y;

    @Override
    public String toString() {
        return "StateChannel{" +
                "Dn=" + Dn +
                ", ch_id='" + ch_id + '\'' +
                ", ph='" + ph + '\'' +
                ", ccbm='" + ccbm + '\'' +
                ", y=" + y +
                "}";
    }

}
