package cn.com.startai.awsai.comtrade.utils;

import android.graphics.Color;

import java.io.File;

import cn.com.startai.awsai.comtrade.cfg.AnalogChannel;
import cn.com.startai.awsai.comtrade.exception.WrongFormatException;

/**
 * author Guoqiang_Sun
 * date 2019/9/6
 * desc
 */
public class ComtradeUtils {

    public static String[] split(String line) {
        if (line == null || line.length() <= 0) {
            throw new NullPointerException();
        }
        return line.split(ComtradeInfo.CFG_LINE_SPLIT);
    }

    public static void checkLength(String[] split, int length) throws WrongFormatException {
        if (split == null || split.length < length) {
            throw new WrongFormatException("Wrong format");
        }
    }

    /**
     * null字符串 给 ""
     */
    public static String valueOf(String str) {
        return str == null ? "" : str;
    }

    /**
     * 系数转换
     */
    public static float ratioConv(AnalogChannel mChannel, short value) {
        return mChannel.a * value + mChannel.b;
    }

    public enum UI {
        U("u"), I("i"), UN("un");

        public final String[] simpleName;

        UI(String... u) {
            this.simpleName = u;
        }

        boolean eq(String s) {
            for (String iName : simpleName) {
                if (s.contains(iName)) {
                    return true;
                }
            }
            return false;
        }

        public static UI of(String channelName) {
            if (channelName == null || channelName.equalsIgnoreCase("")) {
                return UI.UN;
            }
            String s = channelName.toLowerCase();

            if (UI.I.eq(s)) {
                return UI.I;
            } else if (UI.U.eq(s)) {
                return UI.U;
            }
            return UI.UN;
        }
    }

    public enum PL {
        A(Color.YELLOW, "a"), // A相
        B(Color.GREEN, "b"),// B相
        C(Color.RED, "c"),// C相
        N(Color.BLACK, "0"),// 零线
        L(Color.RED, "l"), // 火线
        E(Color.rgb(0x9A, 0xCD, 0x32), "e"),// 地线
        U(Color.rgb(0x0F, 0x0F, 0x0F), "un"); // unknown

        public final int color;
        public final String simpleName;

        PL(int color, String simpleName) {
            this.color = color;
            this.simpleName = simpleName;
        }

        boolean eq(String s) {
            return s.contains(simpleName);
        }

        public static PL of(String channelName) {
            if (channelName == null || channelName.equalsIgnoreCase("")) {
                return PL.U;
            }
            String s = channelName.toLowerCase();
            if (PL.A.eq(s)) {
                return PL.A;
            } else if (PL.B.eq(s)) {
                return PL.B;
            } else if (PL.C.eq(s)) {
                return PL.C;
            } else if (PL.N.eq(s)) {
                return PL.N;
            }
            return PL.U;
        }
    }

    /**
     * 已知配置文件路径,换算数据文件路径
     *
     * @param cfgPath 配置文件路径
     * @return 数据文件路径
     */
    public static File cfgFile2DatFile(String cfgPath) {
        if (cfgPath == null) {
            return null;
        }
        return cfgFile2DatFile(new File(cfgPath));
    }

    /**
     * 已知配置文件路径,换算数据文件路径
     *
     * @param cfgFile 配置文件路径
     * @return 数据文件路径
     */
    public static File cfgFile2DatFile(File cfgFile) {
        if (cfgFile == null || !cfgFile.exists()) {
            return null;
        }
        String parent = cfgFile.getParent();
        String name = cfgFile.getName();
        int i = name.lastIndexOf(".");
        if (i > 0) {
            String substring = name.substring(0, i);
            File dat = new File(parent, substring + "." + ComtradeInfo.DAT_SUFFIX);
            if (dat.exists()) {
                return dat;
            } else {
                dat = new File(parent, substring + "." + ComtradeInfo.DAT_SUFFIX_UPPER);
                if (dat.exists()) {
                    return dat;
                }
            }
        }
        return null;
    }

    /**
     * 已知数据文件路径,换算配置文件路径
     *
     * @param datPath 数据文件路径
     * @return 配置文件路径
     */
    public static File datFile2CfgFile(String datPath) {
        if (datPath == null) {
            return null;
        }
        return datFile2CfgFile(new File(datPath));
    }

    /**
     * 已知数据文件路径,换算配置文件路径
     *
     * @param datFile 数据文件路径
     * @return 配置文件路径
     */
    public static File datFile2CfgFile(File datFile) {
        if (datFile == null || !datFile.exists()) {
            return null;
        }
        String parent = datFile.getParent();
        String name = datFile.getName();
        int i = name.lastIndexOf(".");
        if (i > 0) {
            String substring = name.substring(0, i);
            File cfg = new File(parent, substring + "." + ComtradeInfo.CFG_SUFFIX);
            if (cfg.exists()) {
                return cfg;
            } else {
                cfg = new File(parent, substring + "." + ComtradeInfo.CFG_SUFFIX_UPPER);
                if (cfg.exists()) {
                    return cfg;
                }
            }
        }
        return null;
    }

}
