package cn.com.startai.awsai.comtrade;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.com.startai.awsai.comtrade.cfg.AnalogChannel;
import cn.com.startai.awsai.comtrade.cfg.ComtradeConfig;
import cn.com.startai.awsai.comtrade.dat.ComtradeChannelData;
import cn.com.startai.awsai.comtrade.dat.ComtradeDatBinary;
import cn.com.startai.awsai.comtrade.exception.ComtradeNullException;
import cn.com.startai.awsai.comtrade.exception.WrongFormatException;
import cn.com.startai.awsai.comtrade.utils.ComtradeInfo;

/**
 * author Guoqiang_Sun
 * date 2019/9/10
 * desc
 */
public class ComtradeWorker {

    public CfgWorker getConfigWorker() {
        return mConfigWorker;
    }

    public DatWorker getDatWorker() {
        return mDatWorker;
    }

    private final CfgWorker mConfigWorker;
    private final DatWorker mDatWorker;

    public ComtradeWorker() {
        this.mConfigWorker = new CfgWorker();
        this.mDatWorker = new DatWorker();
    }

    public ComtradeWorker(CfgWorker mConfigWorker, DatWorker mDatWorker) {
        this.mConfigWorker = mConfigWorker;
        this.mDatWorker = mDatWorker;
    }


    /**
     * 二进制数据文件转ASCII文件
     *
     * @param inputCfgPath  配置文件路径
     * @param inputDatPath  数据文件路径
     * @param outputDatPath 输出路径
     * @throws IOException e
     */
    public void Binary2ASCII(File inputCfgPath, File inputDatPath, File outputDatPath) throws IOException {
        ComtradeConfig config = mConfigWorker.read(inputCfgPath);
        Binary2ASCII(config, inputDatPath, outputDatPath);
    }

    /**
     * 二进制数据文件转ASCII文件
     *
     * @param config        配置属性
     * @param inputDatPath  输入数据路径
     * @param outputDatPath 输出数据路径
     * @throws ComtradeNullException e
     */
    public void Binary2ASCII(ComtradeConfig config, File inputDatPath, File outputDatPath) throws ComtradeNullException {
        if (config == null) {
            throw new ComtradeNullException("ComtradeConfig must not be null");
        }

        if (config.ft.equalsIgnoreCase(ComtradeInfo.ASCII)) {
            return;
        }

        BufferedWriter bw = null;
        BufferedInputStream bis = null;
        try {

            bis = new BufferedInputStream(new FileInputStream(inputDatPath));
            bw = new BufferedWriter(new FileWriter(outputDatPath));

            int count = config.mChannelType.analog_channel_A_count
                    + config.mChannelType.state_channel_D_count;

            ComtradeDatBinary binary = new ComtradeDatBinary(count);

            while (binary.read(bis) != -1) {
                bw.write(binary.seq + ",");
                bw.write(binary.timestamp + ",");
                short[] channelValue = binary.channelValue;
                for (int i = 0; i < count; i++) {
                    if (i == count - 1) {
                        bw.write(String.valueOf(channelValue[i]));
                    } else {
                        bw.write(channelValue[i] + ",");
                    }
                }
                bw.newLine();
            }
            bw.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 二进制模拟数据传csv
     *
     * @param fileNameCfg 配置文件路径
     * @param fileNameDat 数据文件路径
     * @param csvDatDir   输出csv父类路径
     * @param csvDatName  输出csv名称
     * @throws IOException e
     */
    public void binaryAnalogDatToCsv(File fileNameCfg, File fileNameDat,
                                     File csvDatDir, String csvDatName) throws IOException {
        ComtradeConfig config = mConfigWorker.read(fileNameCfg);
        if (config == null) {
            throw new ComtradeNullException("ComtradeConfig must not be null");
        }
        if (!config.ft.equalsIgnoreCase(ComtradeInfo.BINARY)) {
            throw new WrongFormatException("config.ft must be " + ComtradeInfo.BINARY);
        }
        ComtradeChannelData channelData =
                mDatWorker.readBinaryChannelDat(config, fileNameDat);
        if (channelData == null) {
            throw new ComtradeNullException("ComtradeChannelData must not be null");
        }
        analogDatToCsv(config, channelData, csvDatDir, csvDatName);
    }


    /**
     * 二进制模拟数据传csv
     *
     * @param parentPath  父类路径
     * @param fileNameCfg 配置文件路径
     * @param fileNameDat 数据文件路径
     * @param csvDatName  输出csv名称
     * @throws IOException e
     */
    public void binaryAnalogDatToCsv(String parentPath, String fileNameCfg,
                                     String fileNameDat, String csvDatName) throws IOException {
        ComtradeConfig config = mConfigWorker.read(new File(parentPath, fileNameCfg));
        if (config == null) {
            throw new ComtradeNullException("ComtradeConfig must not be null");
        }
        if (!config.ft.equalsIgnoreCase(ComtradeInfo.BINARY)) {
            throw new WrongFormatException("config.ft must be " + ComtradeInfo.BINARY);
        }
        ComtradeChannelData channelData =
                mDatWorker.readBinaryChannelDat(config, new File(parentPath, fileNameDat));
        analogDatToCsv(config, channelData, parentPath, csvDatName);
    }

    /**
     * 二进制模拟数据传csv
     *
     * @param config      配置属性
     * @param parentPath  父类路径
     * @param channelData 数据
     * @param csvDatName  输出csv名称
     * @throws IOException e
     */
    public void binaryAnalogDatToCsv(ComtradeConfig config, ComtradeChannelData channelData,
                                     String parentPath, String csvDatName) throws IOException {
        if (config == null) {
            throw new ComtradeNullException("ComtradeConfig must not be null");
        }
        if (!config.ft.equalsIgnoreCase(ComtradeInfo.BINARY)) {
            throw new WrongFormatException("config.ft must be " + ComtradeInfo.BINARY);
        }
        if (channelData == null) {
            throw new ComtradeNullException("ComtradeChannelData must not be null");
        }
        analogDatToCsv(config, channelData, parentPath, csvDatName);
    }

    /**
     * 二进制模拟数据传csv
     *
     * @param config      配置属性
     * @param parentPath  父类路径
     * @param channelData 数据
     * @param csvDatName  输出csv名称
     * @throws IOException e
     */
    public void binaryAnalogDatToCsv(ComtradeConfig config, ComtradeChannelData channelData,
                                     File parentPath, String csvDatName) throws IOException {
        if (config == null) {
            throw new ComtradeNullException("ComtradeConfig must not be null");
        }
        if (!config.ft.equalsIgnoreCase(ComtradeInfo.BINARY)) {
            throw new WrongFormatException("config.ft must be " + ComtradeInfo.BINARY);
        }
        if (channelData == null) {
            throw new ComtradeNullException("ComtradeChannelData must not be null");
        }
        analogDatToCsv(config, channelData, parentPath, csvDatName);
    }

    /**
     * 二进制模拟数据传csv
     *
     * @param config      配置属性
     * @param channelData 数据内容
     * @param parentPath  父类路径
     * @param csvDatName  csv文件名
     * @throws IOException e
     */
    public void analogDatToCsv(ComtradeConfig config,
                               ComtradeChannelData channelData,
                               String parentPath,
                               String csvDatName) throws IOException {
        analogDatToCsv(config, channelData, new File(parentPath), csvDatName);
    }

    /**
     * 二进制模拟数据传csv
     *
     * @param config      配置属性
     * @param channelData 数据内容
     * @param parentPath  父类路径
     * @param csvDatName  csv文件名
     * @throws IOException e
     */
    public void analogDatToCsv(ComtradeConfig config,
                               ComtradeChannelData channelData,
                               File parentPath,
                               String csvDatName) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss.SSS", Locale.getDefault());
        long secondTimeL = config.mTimeDates.secondTimeL;
        AnalogChannel[] mAnalogChannels = config.mAnalogChannels;
        short[][] values = channelData.getValues();
        int[] ts = channelData.getTs();

        for (int i = 0; i < mAnalogChannels.length; i++) {
            String ch_id = mAnalogChannels[i].ch_id;
            int an = mAnalogChannels[i].An - 1;
            File csvPath = new File(parentPath,
                    csvDatName + "_" + an + "_" + ch_id + ".csv");
            BufferedWriter bw = null;

            try {
                bw = new BufferedWriter(new FileWriter(csvPath));
                bw.write("timestamp,value");
                bw.newLine();

                short[] value = values[an];

                for (int j = 0; j < value.length; j++) {
                    bw.write(simpleDateFormat.format(new Date(secondTimeL + ts[j])));
                    bw.write("," + value[j]);
                    bw.newLine();
                }

                bw.flush();

            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            } finally {
                if (bw != null) {
                    try {
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
