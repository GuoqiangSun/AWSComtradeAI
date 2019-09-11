package cn.com.startai.awsai.app.fragment;

import android.support.v4.app.Fragment;

import cn.com.startai.awsai.comtrade.CfgData;

/**
 * author Guoqiang_Sun
 * date 2019/9/10
 * desc
 */
public abstract class BaseFragment extends Fragment {

    public abstract void runview(CfgData lastCfgData);

    public abstract void clear() ;

}
