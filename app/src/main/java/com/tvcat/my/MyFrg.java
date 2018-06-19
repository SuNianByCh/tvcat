package com.tvcat.my;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tvcat.App;
import com.tvcat.DialogUpdate;
import com.tvcat.ILauncherView;
import com.tvcat.LancherPresenter;
import com.tvcat.R;
import com.tvcat.beans.ConfigBean;
import com.tvcat.beans.MyInfos;
import com.tvcat.beans.UpdateBean;
import com.tvcat.util.DeviceUtil;
import com.tvcat.util.DownLoadService;
import com.tvcat.util.TipUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyFrg extends Fragment implements IMyView {
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

    Unbinder unbinder;
    private MyFrgPresenter myFrgPresenter;
    private LancherPresenter lancherPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_my, null, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        if (myDevice != null) {
            String deviceName = myDevice.getName();
            tvName.setText(deviceName);
        }


        myFrgPresenter = new MyFrgPresenter(this);
        myFrgPresenter.getMyInfos();

        iniclick();

    }

    private void iniclick() {


        tvVipLeft.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), VIPChargeActivity.class));
        });

        tvGetVip.setOnClickListener(v -> startActivity(new Intent(getContext(), VIPHistoryActivity.class)));

        tvHistory.setOnClickListener(v -> startActivity(new Intent(getContext(), LookHistoryAcitvity.class)));
        tvUpdate.setOnClickListener(v -> myFrgPresenter.checkUpdate());
        tvVersion.setText(DeviceUtil.getVersionName());

        tvAboutUs.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MyWebViewActiviy.class);
            intent.putExtra("type", MyWebViewActiviy.type_about_us);
            startActivity(intent);
        });

        tvQuestion.setOnClickListener(v -> {
            startMyWeb(MyWebViewActiviy.type_common_question);
        });

        tvOnlineService.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MyWebViewActiviy.class);
            intent.putExtra("type", MyWebViewActiviy.type_online_serviece);
            startActivity(intent);
        });

        tvClear.setOnClickListener(v -> {
            Toast.makeText(getContext(), "缓存已清空", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public void onDestroyView() {
        if (myFrgPresenter != null)
            myFrgPresenter.unSubscribe();

        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void resultInfos(MyInfos myInfos) {
        tvVipLeft.setText(myInfos.getLeft_days());

        tvId.setText("ID:" + myInfos.getId());


    }

    @Override
    public void getMyInfosFailed(String url) {
        Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void noInterNet() {
        Toast.makeText(getContext(), TipUtil.NO_NET, Toast.LENGTH_SHORT).show();
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
            if (lancherPresenter == null)
                lancherPresenter = new LancherPresenter(new ILauncherView() {
                    @Override
                    public void noInterNet() {
                        Toast.makeText(getContext(), TipUtil.NO_NET, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void getConfigFailed(String reason) {
                        if (reason == null)
                            return;
                        Toast.makeText(getContext(), reason, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void resultConfig(ConfigBean configBean) {
                        App.setConfigBean(configBean);
                        startMyWeb(type);
                    }
                });
            lancherPresenter.getConfig();
        } else {
            Intent intent = new Intent(getContext(), MyWebViewActiviy.class);
            intent.putExtra("type", type);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        if (lancherPresenter != null)
            lancherPresenter.unSubscribe();
        super.onDestroy();

    }
}
