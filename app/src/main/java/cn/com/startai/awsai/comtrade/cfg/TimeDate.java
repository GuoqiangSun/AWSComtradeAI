package cn.com.startai.awsai.comtrade.cfg;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import cn.com.startai.awsai.comtrade.exception.WrongFormatException;
import cn.com.startai.awsai.comtrade.utils.ComtradeUtils;

/**
 * author Guoqiang_Sun
 * date 2019/9/6
 * desc 日期/时间标记
 * 要配置文件中必须有两个日期/时间标记。
 * 第一个用于数据文件中第一个数据值的时间。
 * 第二个用于触发点的时间。
 * <p>
 * * 格式::dd/mm/yyyy，hh：mm：ss.ssssss
 * * <p>
 * * dd — 月份中的日。非必需，整数，数字，最小长度=1 字符，最大长度=2 字符，最小
 * * 值=1，最大值=31。
 * * mm — 月份。非必需，整数，数字，最小长度=1 字符，最大长度=2 字符，最小值=1，
 * * 最大值=12。
 * * yy — 年份。非必需，整数，数字，最小长度=4 字符，最大长度=4 字符，最小值=1900，
 * * 最大值=9999。应包括年份的所有 4 个字符。
 * * 变数 dd，mm 和 yyyy 被组成一个区，数字被斜杠符分隔开，中间不带空格。
 * * hh — 小时。非必需，整数，数字，最小长度=2 字符，最大长度=2 字符，最小值=00，
 * * 最大值=23。所有时间应以 24 小时的格式显示。
 * * mm — 分钟。非必需，整数，数字，最小长度=2 字符，最大长度=2 字符，最小值=00，
 * * 最大值=59。
 * * ss.ssssss — 秒。非必须，十进制数字，分辩率=1 微秒，最小长度=9 字符，最大长度=9
 * * 字符，最小值=00.000000，最大值=59.999999。
 * * <p>
 * * 如果需要，日期和时间的所有值应由零所代替或补充。如果缺失时间和日期标记所需的
 * * 日期，将随之以区分隔符/<CR/LF>，而无间隔字符，有正确格式的区将被由零所代替的数
 * * 字值所填充。
 */
public class TimeDate {

    public TimeDate() {
    }

    public TimeDate(String line, String line1) throws WrongFormatException {
        String[] split = ComtradeUtils.split(line);
        ComtradeUtils.checkLength(split, 2);

        String[] split1 = ComtradeUtils.split(line1);
        ComtradeUtils.checkLength(split1, 2);

        this.firstTime = line;
        this.secondTime = line1;
        String firstTimeFormat = split[0].trim() + " " + split[1].trim();
        String secondTimeFormat = split1[0].trim() + " " + split1[1].trim();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss.SSSSSS", Locale.getDefault());
        try {
            if (!firstTimeFormat.equalsIgnoreCase("")) {
                firstTimeL = sdf.parse(firstTimeFormat).getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            if (!secondTimeFormat.equalsIgnoreCase("")) {
                secondTimeL = sdf.parse(secondTimeFormat).getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void write(BufferedWriter bw) throws IOException {
        bw.write(firstTime);
        bw.newLine();
        bw.write(secondTime);
    }

//    格式::dd/mm/yyyy，hh：mm：ss.ssssss

    /**
     * 用于数据文件中第一个数据值的时间
     */
    public String firstTime;

    public long firstTimeL;

    /**
     * 用于触发点的时间
     */
    public String secondTime;

    public long secondTimeL;

    @Override
    public String toString() {
        return "TimeDate{" +
                "firstTime='" + firstTime + '\'' +
                ", firstTimeL=" + firstTimeL +
                ", secondTime='" + secondTime + '\'' +
                ", secondTimeL=" + secondTimeL +
                '}';
    }

}
