package com.tvcat.my.views;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tvcat.R;
import com.tvcat.util.ActivityManager;
import com.tvcat.util.DialogTip;
import com.tvcat.util.HttpConstance;
import com.tvcat.util.HttpModel;
import com.tvcat.util.RegisterBeanHelper;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VIPChargeActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_vip_code)
    EditText etVipCode;
    @BindView(R.id.bt_sure)
    Button btSure;
    private Unbinder bind;
    private View statusBarView;
    private DialogTip dialogTip;
    private Disposable subscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.fullScreen(this);
        starSinkBar(getResources().getColor(R.color.main_color));
        setContentView(R.layout.activity_vipcharge);
        bind = ButterKnife.bind(this);
        initClick();
    }

    private void initClick() {
        ivBack.setImageResource(R.mipmap.btn_close);
        ivBack.setOnClickListener(v -> finish());
        tvTitle.setText("新增VIP充值");

        btSure.setOnClickListener(v -> {

            if (etVipCode.getText() == null && etVipCode.getText().toString().trim().equals("")) {
                Toast.makeText(this, "请输入VIP激活码", Toast.LENGTH_SHORT).show();
                return;
            }

            HashMap<String, String> map = new HashMap<>();
            map.put("token", RegisterBeanHelper.getToken());
            map.put("code", etVipCode.getText().toString());

            charge(map);

        });

    }

    @Override
    protected void onDestroy() {
        if (bind != null)
            bind.unbind();
        dismissTip();
        if (subscribe != null)
            subscribe.dispose();
        super.onDestroy();//672299
    }


    void charge(HashMap<String, String> map) {


        showTIp();
        subscribe = HttpModel.getApiServer().postBackString(HttpConstance.HTTP_VIP_ACTIVE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                            dismissTip();
                            if(subscribe == null || subscribe.isDisposed())
                                return;
                            JSONObject object = new JSONObject(s);
                            if (object.getInt("code") == 0 && "ok".equalsIgnoreCase(object.getString("message"))) {
                                Toast.makeText(this, "充值成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(this, object.getString("message"), Toast.LENGTH_SHORT).show();
                            }


                        }
                        , throwable -> {
                            dismissTip();
                            if(subscribe == null || subscribe.isDisposed())
                                return;
                            Toast.makeText(this, "充值失败", Toast.LENGTH_SHORT).show();
                            throwable.toString();
                        });
    }


    private void showTIp() {
        if (dialogTip == null)
            dialogTip = new DialogTip(this);
        dialogTip.show();
    }

    private void dismissTip() {

        if (dialogTip != null && dialogTip.isShowing())
            dialogTip.dismiss();
    }


    /**
     * 开始沉浸式
     */
    private void starSinkBar(int color) {
//设置 paddingTop
        ViewGroup rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        // if (isPadding)
        rootView.setPadding(0, getStatusBarHeight(), 0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0 以上直接设置状态栏颜色
            getWindow().setStatusBarColor(color);
        } else  /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)*/ {
            //根布局添加占位状态栏
            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            if (statusBarView != null) {
                decorView.removeView(statusBarView);
            }
            statusBarView = new View(this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight());
            statusBarView.setBackgroundColor(color);
            decorView.addView(statusBarView, lp);
        }
    }


    /**
     * 利用反射获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }



}
