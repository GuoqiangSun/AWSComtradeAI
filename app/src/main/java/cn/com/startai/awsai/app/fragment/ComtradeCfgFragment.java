package cn.com.startai.awsai.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.com.startai.awsai.R;
import cn.com.startai.awsai.comtrade.CfgData;
import cn.com.startai.awsai.comtrade.cfg.AnalogChannel;
import cn.com.startai.awsai.comtrade.cfg.ComtradeConfig;
import cn.com.startai.awsai.comtrade.cfg.StateChannel;
import cn.com.startai.awsai.comtrade.cfg.StationInfo;
import cn.com.startai.awsai.typeface.TypefaceUtils;

/**
 * author Guoqiang_Sun
 * date 2019/9/11
 * desc
 */
public class ComtradeCfgFragment extends BaseFragment {

    public static BaseFragment newInstance() {
        return new ComtradeCfgFragment();
    }

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private Handler UIHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIHandler = new Handler(Looper.getMainLooper());
    }

    MyRecycler adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_comtrade_cfg, container, false);

        TextView titleTxt = v.findViewById(R.id.title);
        titleTxt.setTypeface(TypefaceUtils.tfregular);
        titleTxt.setText("配置文件");

        RecyclerView recycler = v.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyRecycler();
        recycler.setAdapter(adapter);

        return v;
    }

    @Override
    public void runview(CfgData lastCfgData) {
        data.clear();
        ComtradeConfig config = lastCfgData.getConfig();
        data.add(config.mStationInfo.toLineStr());
        data.add(config.mChannelType.toLineStr());
        if (config.mAnalogChannels != null && config.mAnalogChannels.length > 0) {
            for (AnalogChannel analogChannel : config.mAnalogChannels) {
                data.add(analogChannel.toLineStr());
            }
        }
        if (config.mStateChannels != null && config.mStateChannels.length > 0) {
            for (StateChannel stateChannel : config.mStateChannels) {
                data.add(stateChannel.toLineStr());
            }
        }
        data.add(config.IF);
        data.add(config.mSampRateInfo.toLineStr());
        data.add(config.mTimeDates.toLineStr());
        data.add(config.ft);
        data.add(String.valueOf(config.timemult));
        if (UIHandler != null) {
            UIHandler.post(notifyDataSetChanged);
        }
    }

    private Runnable notifyDataSetChanged = new Runnable() {
        @Override
        public void run() {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void clear() {
        data.clear();
        if (UIHandler != null) {
            UIHandler.post(notifyDataSetChanged);
        }
    }

    private ArrayList<String> data = new ArrayList<>();

    public class MyRecycler extends RecyclerView.Adapter {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View inflate = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recy_item_txt, viewGroup, false);
            return new LineTxtHolder(inflate);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            ((LineTxtHolder) viewHolder).mTxtView.setText(data.get(i));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        private class LineTxtHolder extends RecyclerView.ViewHolder {
            private TextView mTxtView;

            public LineTxtHolder(@NonNull View itemView) {
                super(itemView);
                mTxtView = (TextView) itemView;
            }

        }
    }

}
