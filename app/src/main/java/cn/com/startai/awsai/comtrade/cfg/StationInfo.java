package cn.com.startai.awsai.comtrade.cfg;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import cn.com.startai.awsai.comtrade.JavaTest;
import cn.com.startai.awsai.comtrade.exception.ComtradeNullException;
import cn.com.startai.awsai.comtrade.exception.WrongFormatException;
import cn.com.startai.awsai.comtrade.utils.ComtradeInfo;
import cn.com.startai.awsai.comtrade.utils.ComtradeUtils;

/**
 * author Guoqiang_Sun
 * date 2019/9/6
 * desc
 * <p>
 * 站名、特征和修改年份
 */
public class StationInfo {

    public StationInfo() {
    }

    public StationInfo(String line) throws WrongFormatException, ComtradeNullException {
        String[] split = ComtradeUtils.split(line);
        ComtradeUtils.checkLength(split, 3);
        station_name = split[0];
        rec_dev_id = split[1];
        if (!split[2].equalsIgnoreCase("")) {
            rev_year = Integer.parseInt(split[2]);
        }
    }

    public void write(BufferedWriter mWriter) throws IOException {
        mWriter.write(ComtradeUtils.valueOf(station_name));
        mWriter.write(ComtradeInfo.CFG_LINE_SPLIT);
        mWriter.write(ComtradeUtils.valueOf(rec_dev_id));
        mWriter.write(ComtradeInfo.CFG_LINE_SPLIT);
        mWriter.write(String.valueOf(rev_year));
    }

    public String toLineStr() {
        return ComtradeUtils.valueOf(station_name)
                + ComtradeInfo.CFG_LINE_SPLIT
                + ComtradeUtils.valueOf(rec_dev_id)
                + ComtradeInfo.CFG_LINE_SPLIT
                + rev_year;
    }

    /**
     * 字站位置名称。非必需，字母数字，最小长度=零字符，最大长度=64 字符。
     */
    public String station_name;
    /**
     * 记录装置的特征号或名称。非必需，字母数字，最小长度=零字符，最大长度=64 字符。
     */
    public String rec_dev_id;

    /**
     * COMTRADE 文件版本定义为标准修改年份，比如 1997。必需，数字，最小长度=4 字符，最大长度=4 字符。
     * <p>
     * 这个区标志着文件结构不同于原来的 C37、111-1991COMTRADE 标准。
     * 缺少这个区或一个定区被理解为它意味着文件与标准的 1991 年版兼容。
     */
    public int rev_year;

    @Override
    public String toString() {
        return "StationInfo{" +
                "station_name='" + station_name + '\'' +
                ", rec_dev_id='" + rec_dev_id + '\'' +
                ", rev_year=" + rev_year +
                '}';
    }
}
