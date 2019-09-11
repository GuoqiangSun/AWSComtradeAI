package cn.com.startai.awsai.comtrade.dat;

/**
 * author Guoqiang_Sun
 * date 2019/9/6
 * desc
 */
public class ComtradeDatAscii {

    /**
     * n=采样数，必需，整数，数字，最小长度=1 字符，最大长度=10 字符，
     * 最小值=1，最大值=9999999999。
     */
    public String seq;

    /**
     * timestamp=时间标记，
     * 如果.CFG 文件中的 nrates 和 samp 为非零则为非必需，
     * 如果.CFG文件中的 nrates 和 samp 为零则为必需，数字，最小长度=1 字符，最大长度=10 字符，
     * 时间的基本单位是微秒（μs）。
     * 一个数据文件中以第一个数据采样至任意一个时间标记区所标志的采样所经过的时间
     * 是配置文件中的时间标记与时间乘数（timestamp * timemult）的乘积（单 位为微秒）。
     */
    public String timestamp;

    /**
     * Erreur！=被逗号分隔、直至所有状态通道的数据被显示的状态通道数据值。
     * 非必需，整数，数字，最小长度=1 字符，最大长率=1 字符，仅有的有效值为 0 或 1。
     * 对缺少的状态数据未作规定，在此情况下，该区必须定为 1 或 0。
     */
    public String[] channelValue;

}
