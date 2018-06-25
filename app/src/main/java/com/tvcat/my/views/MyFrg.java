package com.tvcat.my.views;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.sunian.baselib.baselib.RxFragment;
import com.sunian.baselib.util.FastClick;
import com.tvcat.App;
import com.tvcat.DialogUpdate;
import com.tvcat.R;
import com.tvcat.beans.ConfigBean;
import com.tvcat.beans.MyInfos;
import com.tvcat.beans.UpdateBean;
import com.tvcat.my.presenter.IMyView;
import com.tvcat.my.adapters.MyFrgPresenter;
import com.tvcat.util.DeviceUtil;
import com.tvcat.util.DownLoadService;
import com.tvcat.util.TipUtil;

import butterknife.BindView;

public class MyFrg extends RxFragment<MyFrgPresenter, Object> implements IMyView<Object> {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_vip_left)
    TextView tvVipLeft;
    @BindView(R.id.tv_history)
    TextView tvHistory;
    @BindView(R.id.tv_get_vip)
    TextView tvGetVip;
    @BindView(R.id.tv_online_service)
    TextView tvOnlineService;
    @BindView(R.id.tv_about_us)
    TextView tvAboutUs;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.tv_update)
    TextView tvUpdate;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_clear)
    TextView tvClear;
    @BindView(R.id.tv_id)
    TextView tvId;

    private int type;

    @Override
    protected int getLayout() {
        return R.layout.frg_my;
    }


    @Override
    protected void adjustView(Bundle savedInstanceState) {
        super.adjustView(savedInstanceState);
        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        if (myDevice != null) {
            String deviceName = myDevice.getName();
            tvName.setText(deviceName);
        }
        tvVersion.setText(DeviceUtil.getVersionName());
        mPresenter.getMyInfos();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MyFrgPresenter();
    }


    @Override
    protected void initListener() {
        super.initListener();
        tvVipLeft.setOnClickListener(v -> {
            if (FastClick.isFastClick())
                return;
           // startActivity(new Intent(getContext(), VIPChargeActivity.class));
            startActivity(new Intent(mContext,VipChargeViewActivity.class));
        });

        tvGetVip.setOnClickListener(v -> {
            if (FastClick.isFastClick())
                return;
         //   startActivity(new Intent(getContext(), VIPHistoryActivity.class));
            startActivity(new Intent(mContext,VipChargeViewActivity.class));
        });

        tvHistory.setOnClickListener(v -> {
            if (FastClick.isFastClick())
                return;
            startActivity(new Intent(getContext(), LookHistoryAcitvity.class));
        });
        tvUpdate.setOnClickListener(v -> mPresenter.checkUpdate(true));


        tvAboutUs.setOnClickListener(v -> {
            if (FastClick.isFastClick())
                return;
            Intent intent = new Intent(getContext(), MyWebViewActiviy.class);
            intent.putExtra("type", MyWebViewActiviy.type_about_us);
            startActivity(intent);
        });

        tvQuestion.setOnClickListener(v -> {
            if (FastClick.isFastClick())
                return;
            type = MyWebViewActiviy.type_common_question;
            startMyWeb(type);
        });

        tvOnlineService.setOnClickListener(v -> {
            if (FastClick.isFastClick())
                return;
            Intent intent = new Intent(getContext(), MyWebViewActiviy.class);
            intent.putExtra("type", MyWebViewActiviy.type_online_serviece);
            startActivity(intent);
        });

        tvClear.setOnClickListener(v -> {
            if (FastClick.isFastClick())
                return;
            Toast.makeText(getContext(), "缓存已清空", Toast.LENGTH_SHORT).show();
        });

    }


    @Override
    public void resultInfos(MyInfos myInfos) {
        tvVipLeft.setText(myInfos.getLeft_days());
        tvId.setText("ID:" + myInfos.getId());

    }


    @Override
    public void update(UpdateBean updateBean) {

        if (updateBean == null) {
            Toast.makeText(getContext(), TipUtil.NEW_DATE, Toast.LENGTH_SHORT).show();
            return;
        }

        if (updateBean.getChangelog() == null)
            return;


        String version = updateBean.getVersion();
        if (version == null)
            return;

        version = version.replace(".", "").trim();

        if (!version.matches("[0-9]+"))
            return;

        int versionInt = Integer.parseInt(version);

        if (versionInt > DeviceUtil.getVersionCode()) {
            new DialogUpdate(getContext(), updateBean)
                    .setiClickBack((postion, s) -> {
                        if (s == null || "".equals(s.trim())) {
                            Toast.makeText(getContext(), "下载地址有错", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent service = new Intent(getContext(), DownLoadService.class);
                        service.putExtra(DownLoadService.TYPE_DOWN, s);
                        getActivity().startService(service);
                    }).show();
        }


    }

    public void startMyWeb(int type) {
        if (App.getConfigBean() == null) {
            mPresenter.getConfig();
        } else {
            Intent intent = new Intent(getContext(), MyWebViewActiviy.class);
            intent.putExtra("type", type);
            startActivity(intent);
        }
    }

    @Override
    public void resultConfig(ConfigBean configBean) {
        App.setConfigBean(configBean);
        startMyWeb(type);
    }
}
