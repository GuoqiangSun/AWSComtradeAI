package cn.com.startai.awsai.comtrade.dat;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * author Guoqiang_Sun
 * date 2019/9/6
 * desc
 */
public class ComtradeDatBinary {

    public ComtradeDatBinary() {
        mChannel = 0;
    }

    private final int mChannel;
    public byte[] lineData;

    public ComtradeDatBinary(int channel) {
        this.mChannel = channel;
        this.lineData = new byte[lineLength()];
        this.channelValue = new short[channel];
    }

    public int read(BufferedInputStream fis) throws IOException {
        int read = fis.read(lineData);

        if (read != -1) {
            parse();
        }

        return read;
    }

    private void parse() {
        this.seq = (lineData[3] & 0xFF) << 24 | (lineData[2] & 0xFF) << 16
                | (lineData[1] & 0xFF) << 8 | (lineData[0] & 0xFF);
        this.timestamp = (lineData[7] & 0xFF) << 24 | (lineData[6] & 0xFF) << 16
                | (lineData[5] & 0xFF) << 8 | (lineData[4] & 0xFF);
        for (int i = 0; i < mChannel; i++) {
            channelValue[i] = (short) ((lineData[8 + i * 2 + 1] & 0xFF) << 8 | (lineData[8 + i * 2] & 0xFF));
        }
    }

    /**
     * 二进制数据:
     * n=采样数量，必需，整数，数字，最小长度=4 字节，最大长度=4 字节，
     * 最小值=00000001，最大值=FFFFFFFF
     * <p>
     * ASCII数据:
     * n=采样数，必需，整数，数字，最小长度=1 字符，最大长度=10 字符，
     * 最小值=1，最大值=9999999999。
     */
    public int seq;
    /**
     * timestamp=时间标记，
     * 如果.CFG 文件中的 nrates 和 samp 变量非零时为非必需，
     * 如果.CFG文件中的 nrates 和 samp 变量为零时为必需。
     * 最小值=4 字节，最大长度=4 字节，最小值=00000000，最大值=FFFFFFFF。
     * 缺失的时间标记值必须通过将数值 FFFFFFFF 置放于区风来代替，以维持区结构的完整性。
     * 时间的基本单位是微秒（μs）。
     * 从数据文件中第一个数据采样到由时间标记区所标志的数样所经过的时间
     * 是配置文件中时间标记和时间乘数timestamp*timemult）的乘积，其单位是微秒。
     */
    public int timestamp;

    /**
     * 模拟通道数据值，以二字节为单位连续显示，直至所有模拟通道的数据显示完毕。
     * 非必需，整数，二进制，二互补格式，最小长度=2 字节，最大长度=2 字节，最小值=8001，最大值=7FFF。
     * 缺失模拟数值必须通过将数值 8000 置于区内来表示。
     */
    public short[] channelValue;

    /**
     * 一行的长度
     */
    public int lineLength() {
        return lineLength(mChannel);
    }

    /**
     * 前面 4 个字节 表示序号， 后面4个字节表示 时间, channel 表示通道个数，* 2 表示字长
     *
     * @param channel 通道个数
     * @return 一行字长
     */
    public static int lineLength(int channel) {
        return 4 + 4 + channel * 2;
    }

}
