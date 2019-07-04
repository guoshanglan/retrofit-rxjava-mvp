package cc.hisens.hardboiled.patient.utils;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.Gravity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cc.hisens.hardboiled.patient.retrofit.RetrofitUtils;

public class AppUpdateUtils {

    public static AppUpdateUtils getInstence = null;


    //私有化构造函数，不允许通过这个函数实例化对象
    private AppUpdateUtils() {

    }


    //单例模式获取这个对象
    public static AppUpdateUtils getGetInstence() {
        if (getInstence == null) {
            synchronized (AppUpdateUtils.class) {
                if (getInstence == null) {
                    getInstence = new AppUpdateUtils();
                }
            }
        }
        return getInstence;
    }


    //淡出对话框
    public void popUpdateDialog(List<String> message, final  List<String> url,int status, Context context) {

        TextView textView = new TextView(context);
        textView.setText("发现新版本");
        textView.setGravity(Gravity.CENTER);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("发现新版本");
        StringBuffer buffer=new StringBuffer();
        for (String content:message){

          buffer.append(content+"\r\n");
        }
        builder.setMessage(buffer);
        builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                downloadNewest(url, context);
            }

        });
        AlertDialog dialog = builder.create();
        if (status==2){
            dialog.setCancelable(false);
        }else{
            dialog.setCancelable(true);
        }

        dialog.show();

    }

    private void downloadNewest(List<String> url, Context context) {

//        List<String> name = new ArrayList<>();
//        name.add("com.huawei.appmarket");
//        name.add("com.xiaomi.market");
//        name.add("com.qihoo.appstore");
//        name.add("com.oppo.market");

        if (isMarketInstalled(context, url)) {
            goToMarket(context, "cc.hisens.hardboiled.patient");
        }else{
            ToastUtils.show(context,"请先安装应用商店");
        }
//        } else {
//            goToBrowser(url, context);
//        }

    }


    //判断是否安装
    private boolean isMarketInstalled(Context context, List<String> packageNames) {

        boolean installed = false;
        PackageManager manager = context.getPackageManager();
        List<PackageInfo> packageInfos = manager.getInstalledPackages(0);

        for (int i = 0; i < packageInfos.size(); i++) {
            if (packageNames.contains(
                    packageInfos.get(i).packageName)) {
                installed = true;
                break;
            }
        }
        return installed;

    }

    //去应用市场
    private void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException exception) {
            exception.printStackTrace();
        }

    }

    //跳转到浏览器
    private void goToBrowser(String url, Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri contentUrl = Uri.parse(url);
        intent.setData(contentUrl);
        context.startActivity(intent);
    }


}
