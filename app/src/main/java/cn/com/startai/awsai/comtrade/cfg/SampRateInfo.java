package cn.com.startai.awsai.comtrade.cfg;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

import cn.com.startai.awsai.comtrade.exception.WrongFormatException;
import cn.com.startai.awsai.comtrade.utils.ComtradeUtils;

/**
 * author Guoqiang_Sun
 * date 2019/9/6
 * desc 采样率信息
 */
public class SampRateInfo {

    public SampRateInfo() {
    }

    public SampRateInfo(String line1, String line2) throws WrongFormatException {
        if (!line1.equalsIgnoreCase("")) {
            nrates = Integer.parseInt(line1);
        }
        String[] split = ComtradeUtils.split(line2);
        ComtradeUtils.checkLength(split, 2);
        if (!split[0].equalsIgnoreCase("")) {
            samp = Float.parseFloat(split[0]);
        }

        if (!split[1].equalsIgnoreCase("")) {
            endsamp = Integer.parseInt(split[1]);
        }
    }

    public void write(BufferedWriter bw) throws IOException {
        bw.write(String.valueOf(nrates));
        bw.newLine();
        bw.write(String.valueOf(samp));
        bw.write(",");
        bw.write(String.valueOf(endsamp));
    }

    /**
     * 数据文件中采样率数字。必需，整数，数字，
     * 最小长度=1 字符，最大长度=3字符，最小值=0，最大值=999。
     */
    public int nrates;

    /**
     * samp — 以赫兹（Hz）为单位的采样率。必需，实数，数字，
     * 最小长度=1 字符，最大长度=32 字符。可以使用标准浮点表示法[4]。
     */
    public float samp;

    /**
     * 在该采样率下最后一次采样数。必需，整数，数字，
     * 最小长度=1 字符，最大长度=10 字符，最小值=1，最大值=9999999999。
     */
    public int endsamp;

    @Override
    public String toString() {
        return "SampRateInfo{" +
                "nrates=" + nrates +
                ", samp=" + samp +
                ", endsamp=" + endsamp +
                '}';
    }

}
