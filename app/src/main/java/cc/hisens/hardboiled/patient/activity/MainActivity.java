package cc.hisens.hardboiled.patient.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;

import com.tbruyelle.rxpermissions2.RxPermissions;

import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.utils.ToastUtils;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


//主要的Activity
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        grantPermissions();

    }




    //动态申请蓝牙权限，判断是否含有蓝牙权限
    @SuppressLint("CheckResult")
    private void grantPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        if (!rxPermissions.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION) && !rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@NonNull Boolean isGranted) {
                            if (isGranted) {
                                // 判断蓝牙是否开启
                            } else {
                                ToastUtils.show(MainActivity.this, "permission not granted");
                            }
                        }
                    });
        }
    }


    @Override
    protected int getLayoutId() {

        return R.layout.activity_main;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
