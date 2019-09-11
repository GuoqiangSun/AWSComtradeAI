package cn.com.startai.awsai.utils;

import com.opencsv.CSVReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * 创建CSV文件
 */
public class CSVCrate {

    public static void main(String[] args) {
        CSVCrate csv = new CSVCrate();
//        csv.createCSV();
//        System.out.println("createCSV end");

//        try {
//            csv.readCSV();
//            System.out.println("readCSV end");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        try {
//            csv.readNum();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        csv.xy();

        String[] strings = new String[0];
        System.out.println(strings.length);
        System.out.println("end");
    }

    private final int WIDTH_COUNT = 8;
    private final int HEIGHT_COUNT = 8;
    private final float[] xy = new float[WIDTH_COUNT * HEIGHT_COUNT * 2];

    private void xy() {
        for (int h = 0; h < HEIGHT_COUNT; h++) {
            for (int w = 0; w < WIDTH_COUNT; w++) {
                xy[(h * WIDTH_COUNT + w) * 2] = w;
                xy[(h * WIDTH_COUNT + w) * 2 + 1] = h;
                System.out.print("xy[" + ((h * WIDTH_COUNT + w) * 2) + "] =" + w + "," + h);
                System.out.print(' ');
            }
            System.out.println();
        }


        for (int i = 0; i < WIDTH_COUNT * HEIGHT_COUNT; i++) {
            System.out.print("xy[" + i * 2 + "]=" + (int)xy[i * 2] + "," + (int)xy[i * 2 + 1]);
            if (i != 0 && i % (WIDTH_COUNT-1) == 0) {
                System.out.println();
            }
        }
    }

    private double getDoubleNumber(String str) {
        double number = 0;
        BigDecimal bd = new BigDecimal(str);
        number = Double.parseDouble(bd.toPlainString());
        return number;
    }

    public void parseNum() {
        String n = "3.281250000000000000e-01";
        double doubleNumber = getDoubleNumber(n);
        System.out.println(doubleNumber);
    }


    public void readNum() throws Exception {
        File file = new File("D:\\AWS C2 KEY\\examples0");
        FileReader fReader = new FileReader(file);
        CSVReader csvReader = new CSVReader(fReader);
        String[] strs = csvReader.readNext();
        if (strs != null && strs.length > 0) {
            int i = 0;
            for (String str : strs)
                if (null != str && !str.equals("")) {
//                    System.out.print("["+str + "]");
                    double doubleNumber = getDoubleNumber(str);
                    if (doubleNumber == 0) {
                        System.out.print("0,");
                    } else {
                        System.out.print("1,");
                    }
                    if (++i % 28 == 0) {
                        System.out.println();
                    }
                }


            System.out.println("\n---------------");
        }
        csvReader.close();
    }


    public void readCSV() throws Exception {
//        File file = new File(filePath, fileName);
        File file = new File("D:\\AWS C2 KEY\\examples0");
        FileReader fReader = new FileReader(file);
        CSVReader csvReader = new CSVReader(fReader);
//        String[] strs = csvReader.readNext();
//        if (strs != null && strs.length > 0) {
//            for (String str : strs)
//                if (null != str && !str.equals(""))
//                    System.out.print(str + " , ");
//            System.out.println("\n---------------");
//        }
        List<String[]> list = csvReader.readAll();
        for (String[] ss : list) {
            for (String s : ss)
                if (null != s && !s.equals(""))
                    System.out.print(new String(s.getBytes(), "UTF-8") + " , ");
            System.out.println();
        }
        csvReader.close();
    }

    String fileName = "testCSV.csv";//文件名称
    String filePath = "E:/test/"; //文件路径

    /**
     * 创建CSV文件
     */
    public void createCSV() {

        // 表格头
        Object[] head = {"客户姓名", "证件类型", "日期",};
        List<Object> headList = Arrays.asList(head);

        //数据
        List<List<Object>> dataList = new ArrayList<List<Object>>();
        List<Object> rowList = null;
        for (int i = 0; i < 100; i++) {
            rowList = new ArrayList<Object>();
            rowList.add("张三" + i);
            rowList.add("263834194" + i);
            rowList.add(new Date());
            dataList.add(rowList);
        }


        File csvFile = null;
        BufferedWriter csvWtriter = null;
        try {
            csvFile = new File(filePath + fileName);
            File parent = csvFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            csvFile.createNewFile();

            // GB2312使正确读取分隔符","
//            "GB2312"
            csvWtriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "UTF-8"), 1024);

            //文件下载，使用如下代码
//            response.setContentType("application/csv;charset=gb18030");
//            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
//            ServletOutputStream out = response.getOutputStream();
//            csvWtriter = new BufferedWriter(new OutputStreamWriter(out, "GB2312"), 1024);

            int num = headList.size() / 2;
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < num; i++) {
                buffer.append(" ,");
            }
            csvWtriter.write(buffer.toString() + fileName + buffer.toString());
            csvWtriter.newLine();

            // 写入文件头部
            writeRow(headList, csvWtriter);

            // 写入文件内容
            for (List<Object> row : dataList) {
                writeRow(row, csvWtriter);
            }
            csvWtriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvWtriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 写一行数据
     *
     * @param row       数据列表
     * @param csvWriter
     * @throws IOException
     */
    private static void writeRow(List<Object> row, BufferedWriter csvWriter) throws IOException {
        for (Object data : row) {
            StringBuffer sb = new StringBuffer();
            String rowStr = sb.append("\"").append(data).append("\",").toString();
            csvWriter.write(rowStr);
        }
        csvWriter.newLine();
    }
}