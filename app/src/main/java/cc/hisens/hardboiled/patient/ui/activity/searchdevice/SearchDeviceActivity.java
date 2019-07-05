package cc.hisens.hardboiled.patient.ui.activity.searchdevice;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.os.Build;
import android.os.Bundle;


import com.clj.fastble.data.BleDevice;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.adapter.DeviceAdapter;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;

import cc.hisens.hardboiled.patient.ble.BLEManagerWrapper;
import cc.hisens.hardboiled.patient.ble.UUIDConstants;
import cc.hisens.hardboiled.patient.ble.callbacks.ISyncDataCallback;



//搜索设备，

public class SearchDeviceActivity extends BaseActivity {

    private BLEManagerWrapper mBleManagerWrapper;  //蓝牙操作管理者
    private ISyncDataCallback mISyncDataCallback = new ISyncDataCallbackImpl();   //同步数据的回调callback如查询·蓝牙设备的电量，设备号等
    private List<BleDevice>deviceList;
    private DeviceAdapter adapter; //设备适配器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    //初始化数据和界面
    private void initView() {
        deviceList=new ArrayList<>();
        adapter=new DeviceAdapter(this,deviceList);
        mBleManagerWrapper=BLEManagerWrapper.getInstance();
        mBleManagerWrapper.addSyncDataCallback(mISyncDataCallback);

    }



    //打开蓝牙and扫描
    public void OpenAndScan() {

       //判断蓝牙是否打开
        if (mBleManagerWrapper.isBlueEnable()) {

            mBleManagerWrapper.ScanDevice();   //蓝牙管理者发送指令进行连接,扫描设备，可能会有多个设备，要进行选择

        } else {

            mBleManagerWrapper.enableBluetooth();  //需要打开蓝牙

        }
    }




    @Override
    protected int getLayoutId() {
        return R.layout.search_device;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }


    /**
     *
     * 定义的蓝牙数据回调类，这个类用来处理蓝牙的回调数据的
     */

    private class ISyncDataCallbackImpl implements ISyncDataCallback {

        @Override
        public void onBleStateOn() {   //蓝牙设备开启

        }

        @Override
        public void onBleStateOff() {  //监听蓝牙关闭
            KLog.i("-->> onBleStateOff"+"蓝牙关闭");
        }

        @Override
        public void onBattery(int value) {  //蓝牙电量
            KLog.i("-->> onBattery"+value+"");
        }

        @Override
        public void onSerialNo(String serialNo) {  //蓝牙下围基序列号
            KLog.i("-->> onSerialNo"+serialNo.toString());
        }

        @Override
        public void onConnectionSuccessful(BleDevice device) {  //蓝牙设备连接成功
            KLog.i("-->> onConnectionSuccessful"+device.toString());
        }

        @Override
        public void onConnectionFailed() {  //蓝牙设备连接失败
            KLog.i("-->> onConnectionFailed"+"设备连接失败");
        }

        @Override
        public void onNotFoundDevice() { //没有发现设备

            KLog.i("-->> onNotFoundDevice");
        }

        @Override
        public void onDisconnect() {  //拒绝连接
            KLog.i("-->> onDisconnect"+"拒绝连接");
        }

        @Override
        public void onSyncProgressUpdate(int value) {

        }

        @Override
        public void onSyncSuccessful(long startSleep) {  //蓝牙数据同步成功
            KLog.i("-->> onSyncSuccessful"+"数据同步成功"+startSleep);
        }

        @Override
        public void onSyncFailed() {  //蓝牙数据同步失败
            KLog.i("-->> onSyncFailed"+"数据同步失败");
        }

        @Override
        public void onServiceDiscovered(BluetoothGatt gatt, int status) {
            notifyCharacteristics(gatt);
        }

        @Override
        public void onSetTimeFailed() {
            KLog.i("-->> onSetTimeFailed"+"设置时间失败");
        }

        @Override
        public void onSyncData() {  //同步数据中

        }

        @Override
        public void DeviceCount(List<BleDevice> list) {  //发现的蓝牙设备数用来显示在列表中
            KLog.i("-->> DeviceCount"+"扫描完成"+list.toString());
            if (list != null && list.size() != 0) {
                  deviceList.addAll(list);
                  adapter.notifyDataSetChanged(); //更新数据

            }else{
                ShowToast("没有发现设备");
            }


        }

    }


    private void notifyCharacteristics(final BluetoothGatt gatt) {
        runOnUiThread(new Runnable() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void run() {
                mBleManagerWrapper.notifyCharacteristics(gatt.getService(UUIDConstants.SERVICE));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBleManagerWrapper.recycle();
    }
}
