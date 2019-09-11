package cn.com.startai.awsai.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.com.swain.baselib.log.Tlog;

/**
 * author Guoqiang_Sun
 * date 2019/9/2
 * desc
 */
public class NumberData {

    private NumberData() {
    }

    private static class ClassHolder {
        private static final NumberData DATA = new NumberData();
    }

    public static NumberData getInstance() {
        return ClassHolder.DATA;
    }

    private List<String[]> strings;
    private List<String> lines;
    private String[] cities;

    public void clear() {
        strings = null;
        lines = null;
        cities = null;
    }

    public List<String[]> getStrings() {
        return strings;
    }

    public List<String> getLines() {
        return lines;
    }

    public String[] getCities() {
        return cities;
    }


    @SuppressLint("StaticFieldLeak")
    public void openExample(Context ctx) {
        if (strings != null && strings.size() > 0
                && lines != null && lines.size() > 0
                && cities != null && cities.length > 0) {
            return;
        }
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                CSVReader csvReader = null;
                try {

                    String name = "Number.csv";
                    InputStream open = ctx.getAssets().open(name);
                    InputStreamReader isr = new InputStreamReader(open);

                    csvReader = new CSVReader(isr);
                    long recordsRead = csvReader.getRecordsRead();
                    System.out.println(" recordsRead:" + recordsRead);
                    long linesRead = csvReader.getLinesRead();
                    System.out.println(" linesRead:" + linesRead);
                    int multilineLimit = csvReader.getMultilineLimit();
                    System.out.println(" multilineLimit:" + multilineLimit);

                    strings = csvReader.readAll();
                    int total = strings != null ? strings.size() : 0;
                    cities = new String[total];
                    for (int j = 0; j < total; j++) {
                        cities[j] = "下标为" + String.valueOf(j) + "的测试数据";
                    }

                    System.out.println("--------------line");
                    lines = new ArrayList<>(total);
                    StringBuffer sb;
                    for (String[] strs : strings) {
                        sb = new StringBuffer(1024 * 19);
                        for (int i = 0; i < strs.length; i++) {
                            if (i != (strs.length - 1)) {
                                sb.append(strs[i]).append(",");
                            } else {
                                sb.append(strs[i]);
                            }
                        }
                        String lineStr = sb.toString();
//                        System.out.println(lineStr);
                        lines.add(lineStr);
                    }

                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    Tlog.e("System.out", " IOException ", e);
                } finally {
                    if (csvReader != null) {
                        try {
                            csvReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                Toast.makeText(ctx, aBoolean ? "number.csv load success" : "number.csv load fail", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }


}
