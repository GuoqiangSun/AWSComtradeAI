package cn.com.startai.awsai.app;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.com.startai.awsai.R;
import cn.com.startai.awsai.app.fragment.BaseFragment;
import cn.com.startai.awsai.app.fragment.ComtradeFragment1;
import cn.com.startai.awsai.app.fragment.ComtradeFragment2;
import cn.com.startai.awsai.app.fragment.ComtradeFragment3;
import cn.com.startai.awsai.comtrade.ComtradeWorker;
import cn.com.startai.awsai.comtrade.CfgData;
import cn.com.startai.awsai.comtrade.cfg.ComtradeConfig;
import cn.com.startai.awsai.comtrade.dat.ComtradeChannelData;
import cn.com.startai.awsai.comtrade.utils.ComtradeUtils;
import cn.com.startai.awsai.utils.FileScanner;
import cn.com.swain.baselib.log.Tlog;

/**
 * author Guoqiang_Sun
 * date 2019/9/9
 * desc
 */
public class ComtradeActivity extends AppCompatActivity {

    public static final String TAG = "ComtradeActivity";
    private ArrayList<BaseFragment> fragments;
    private ViewPager pager;
    private ExecutorService executorService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comtrade);

        fragments = new ArrayList<>(3);
        fragments.add(ComtradeFragment1.newInstance());
        fragments.add(ComtradeFragment2.newInstance());
        fragments.add(ComtradeFragment3.newInstance());

        pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);

        PageAdapter a = new PageAdapter(getSupportFragmentManager());
        pager.setAdapter(a);


        executorService = Executors.newSingleThreadExecutor();

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        fragments.get(i).runview(lastCfgData);
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fragments != null) {
            fragments.clear();
            fragments = null;
        }
        avaliableCfgFiles.clear();
        avaliableDatFiles.clear();
        avaliableStr.clear();
        showFiles = null;
    }

    public void clearExt(View view) {
        scanned = false;
        avaliableCfgFiles.clear();
        avaliableDatFiles.clear();
        avaliableStr.clear();
        showFiles = null;
        lastWhich = -1;
        Toast.makeText(getApplicationContext(), "clear success", Toast.LENGTH_SHORT).show();
    }

    private class PageAdapter extends FragmentPagerAdapter {

        PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            return fragments.get(pos);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    private CfgData lastCfgData;

    private void postview(CfgData tCfgData) {
        lastCfgData = tCfgData;
        fragments.get(pager.getCurrentItem()).runview(tCfgData);
    }

    private void clear() {
        for (BaseFragment f : fragments) {
            f.clear();
        }
    }

    private volatile boolean exe;

    @SuppressLint("StaticFieldLeak")
    private void loadData() {

        new AsyncTask<Void, Void, CfgData>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                exe = true;
            }

            @Override
            protected CfgData doInBackground(Void... voids) {

                try {
                    ComtradeWorker comtradeWorker = new ComtradeWorker();

                    InputStream open = getAssets().open("exampleComtrade.cfg");
                    BufferedReader br = new BufferedReader(new InputStreamReader(open));
                    ComtradeConfig config = comtradeWorker.getConfigWorker().read(br);

                    InputStream open1 = getAssets().open("exampleComtrade.dat");
                    BufferedInputStream br1 = new BufferedInputStream(open1);
                    ComtradeChannelData mChannelData = comtradeWorker.getDatWorker()
                            .readBinaryChannelDat(config, br1);

                    if (mChannelData == null) {
                        return null;
                    }
                    CfgData cfgData = new CfgData(config, mChannelData);
                    postview(cfgData);
                    return cfgData;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(CfgData mCfgData) {
                super.onPostExecute(mCfgData);

                exe = false;
            }
        }.execute();
    }

    public void selectExt(View view) {
        if (exe || exeexter) {
            Toast.makeText(getApplicationContext(), "is execute,please wait retry", Toast.LENGTH_SHORT).show();
            return;
        }
        scanner();
    }

    private volatile boolean scanned;
    private String[] showFiles;
    private ArrayList<String> avaliableStr = new ArrayList<>();
    private ArrayList<File> avaliableCfgFiles = new ArrayList<>();
    private ArrayList<File> avaliableDatFiles = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    private void scanner() {
        if (scanned) {
            Tlog.v(TAG, " already scan ");
            dialogView();
            return;
        }

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("正在扫描文件...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                scanned = false;
                avaliableCfgFiles.clear();
                avaliableDatFiles.clear();
                avaliableStr.clear();
                showFiles = null;
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                ArrayList<File> scanner = new ArrayList<>();
                File externalStorageDirectory = Environment.getExternalStorageDirectory();
                FileScanner.scan(externalStorageDirectory, new String[]{".cfg", ".CFG"}, scanner);
                Tlog.v(TAG, "scan finish size:" + scanner.size());

                for (File f : scanner) {
                    File file = ComtradeUtils.cfgFile2DatFile(f);
                    if (ComtradeUtils.cfgFile2DatFile(f) != null) {
                        avaliableCfgFiles.add(f);
                        avaliableDatFiles.add(file);
                        avaliableStr.add(f.getPath());
                    }
                }
                showFiles = avaliableStr.toArray(new String[0]);
                Tlog.v(TAG, " avaliableFiles size:" + showFiles.length);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (showFiles != null && showFiles.length > 0) {
                    scanned = true;
                }
                dialog.dismiss();
                dialogView();
            }
        }.execute();
    }

    private volatile int lastWhich = -1;

    public void dialogView() {
        if (showFiles == null || showFiles.length <= 0) {
            Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("CFG文件总个数:" + String.valueOf(showFiles.length));
        //    设置一个下拉的列表选择项
        builder.setItems(showFiles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (avaliableCfgFiles.size() <= which || avaliableDatFiles.size() <= which) {
                    Toast.makeText(getApplicationContext(), "Please select the smaller index", Toast.LENGTH_SHORT).show();
                    return;
                }
                loadExterData(which);
            }
        });
        builder.show();
    }

    private volatile boolean exeexter;

    @SuppressLint("StaticFieldLeak")
    private void loadExterData(int which) {

        if (which == lastWhich) {
            Tlog.w(TAG, " cur which = lastWhich ; currentItem = lastWhichItem");
            Toast.makeText(getApplicationContext(), "No repetition of choices", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(new ComtradeWorkerRun(which));

    }

    private class ComtradeWorkerRun implements Runnable {
        int which;

        private ComtradeWorkerRun(int which) {
            this.which = which;
        }

        @Override
        public void run() {
            exeexter = true;
            try {
                File cfgFile = avaliableCfgFiles.get(which);
                File datFile = avaliableDatFiles.get(which);
                ComtradeWorker comtradeWorker = new ComtradeWorker();
                ComtradeConfig config = comtradeWorker.getConfigWorker().read(cfgFile);
                ComtradeChannelData mChannelData = comtradeWorker.getDatWorker()
                        .readBinaryChannelDat(config, datFile);
                if (mChannelData == null) {
                    return;
                }
                CfgData cfgData = new CfgData(config, mChannelData);
                clear();
                postview(cfgData);
                lastWhich = which;
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "parse data error", Toast.LENGTH_SHORT).show();
                    }
                });
            } finally {
                exeexter = false;
            }
        }
    }
}
