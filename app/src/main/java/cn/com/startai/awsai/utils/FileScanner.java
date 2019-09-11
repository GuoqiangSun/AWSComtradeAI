package cn.com.startai.awsai.utils;

import java.io.File;
import java.util.List;

import cn.com.swain.baselib.log.Tlog;

/**
 * author Guoqiang_Sun
 * date 2019/9/9
 * desc
 */
public class FileScanner {

    private static String TAG = "FileScanner";

    /**
     * 扫描文件
     *
     * @param path   扫描的路径
     * @param suffix 文件的后缀名
     * @param files  存放有效数据的集合
     */
    public static void scan(String path, String[] suffix, List<File> files) {
        if (path == null) {
            Tlog.e(TAG, " start scan path=null");
            return;
        }
        scan(new File(path), suffix, files);
    }

    /**
     * 扫描文件
     *
     * @param path   扫描的路径
     * @param suffix 文件的后缀名
     * @param files  存放有效数据的集合
     */
    public static void scan(File path, String[] suffix, List<File> files) {
        if (path == null) {
            Tlog.e(TAG, " start scan path=null");
            return;
        }
        String path1 = path.getPath();
        Tlog.v(TAG, " start scan:" + path1);
        if (!path.exists()) {
            Tlog.e(TAG, "scan path not exit ");
            return;
        }
        list(path, suffix, files);
        Tlog.d(TAG, "scan finish:" + path1);
    }

    /**
     * 列出目录下所有的文件
     *
     * @param path   路径
     * @param suffix 文件的后缀
     * @param data   存放数据的集合
     */
    private static void list(File path, String[] suffix, List<File> data) {
        if(path.exists()){
            if (path.isDirectory()) {
                File[] files = path.listFiles();
                if (files != null && files.length > 0) {
                    for (File f : files) {
                        list(f, suffix, data);
                    }
                } else {
//                Tlog.w(TAG, " list path.isDirectory() path.listFiles()=null; " + path.getPath());
                }
            } else {
                listFile(path, suffix, data);
            }
        }
    }

    /**
     * 列出目录下所有的文件
     *
     * @param path   文件
     * @param suffix 文件的后缀
     * @param data   存放数据的集合
     */
    private static void listFile(File path, String[] suffix, List<File> data) {
        String path1 = path.getPath();
//        Tlog.v(TAG, " listFile:" + path1);
        for (String s : suffix) {
            if (path1.endsWith(s)) {
                data.add(path);
                break;
            }
        }
    }

}
