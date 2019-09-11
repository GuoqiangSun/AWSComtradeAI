package cn.com.startai.awsai.comtrade;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.startai.awsai.comtrade.cfg.ComtradeConfig;
import cn.com.startai.awsai.comtrade.dat.ComtradeChannelData;
import cn.com.startai.awsai.comtrade.exception.ComtradeNullException;
import cn.com.startai.awsai.comtrade.utils.ComtradeInfo;

/**
 * author Guoqiang_Sun
 * date 2019/9/4
 * desc
 */
public class JavaTest {

    private static void f() {
        long l = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        String format = simpleDateFormat.format(new Date(l));
        System.out.println(format);
    }

    public static void main(String[] args) {

//        readCfg();

//        convertB2A();

//        readBinaryChannelDat();

//        binaryAnalogDatToCsv();

        System.out.println("end");
    }

    private static void readCfg() {
        String path = "D:\\国网电力\\5种典型波形";
        String fileNameCfg = "2017_03_12_16_27_20_000.cfg";
        String fileNameABC = "2017_03_12_16_27_20_000_ABC.cfg";
        try {
            CfgWorker worker = new CfgWorker();
            ComtradeConfig config = worker.read(new File(path, fileNameCfg));
            System.out.println(String.valueOf(config));
            config.ft = ComtradeInfo.ASCII;
            worker.write(config, new File(path, fileNameABC));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void convertB2A() {
        String path = "D:\\国网电力\\5种典型波形";
        String fileNameCfg = "2017_03_12_16_27_20_000.cfg";
        String fileNameDat = "2017_03_12_16_27_20_000.dat";
        String outNameDatAscii = "2017_03_12_16_27_20_000_ASCII.dat";
        ComtradeWorker worker = new ComtradeWorker();
        try {
            worker.Binary2ASCII(new File(path, fileNameCfg),
                    new File(path, fileNameDat)
                    , new File(path, outNameDatAscii));
        } catch (ComtradeNullException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readChannelDat() {
        String path = "D:\\国网电力\\5种典型波形";
        String fileNameCfg = "2017_03_12_16_27_20_000.cfg";
        String fileNameDat = "2017_03_12_16_27_20_000.dat";
        try {
            CfgWorker mCfgWorker = new CfgWorker();
            ComtradeConfig config = mCfgWorker.read(new File(path, fileNameCfg));

            DatWorker mDatWorker = new DatWorker();
            ComtradeChannelData mChannelData = mDatWorker.readBinaryChannelDat(config,
                    new File(path, fileNameDat));
            System.out.println(mChannelData);
        } catch (ComtradeNullException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void datToCsv() {
        String path = "D:\\国网电力\\5种典型波形";
        String fileNameCfg = "2017_03_12_16_27_20_000.cfg";
        String fileNameDat = "2017_03_12_16_27_20_000.dat";
        String csvNameDat = "2017_03_12_16_27_20_000";
        try {
            ComtradeWorker worker = new ComtradeWorker();
            worker.binaryAnalogDatToCsv(path, fileNameCfg, fileNameDat, csvNameDat);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
