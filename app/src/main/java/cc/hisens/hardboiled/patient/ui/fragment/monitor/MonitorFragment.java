package cc.hisens.hardboiled.patient.ui.fragment.monitor;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.bluetooth.BluetoothGatt;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.clj.fastble.data.BleDevice;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BaseFragment;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.ble.BLEManagerWrapper;
import cc.hisens.hardboiled.patient.ble.UUIDConstants;
import cc.hisens.hardboiled.patient.ble.callbacks.ISyncDataCallback;
import cc.hisens.hardboiled.patient.eventbus.onDeviceMessage;
import cc.hisens.hardboiled.patient.ui.activity.main.MainActivity;
import cc.hisens.hardboiled.patient.utils.SyncDatadialog;

/**
   数据监测页面
*/

public class MonitorFragment extends BaseFragment{


    private BLEManagerWrapper mBleManagerWrapper;  //蓝牙操作管理者
    private ISyncDataCallback mISyncDataCallback = new ISyncDataCallbackImpl();   //同步数据的回调callback如查询·蓝牙设备的电量，设备号等
    private SyncDatadialog datadialog;




    //订阅的evnetbus回调蓝牙事件
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDeviceMessage(onDeviceMessage event) {
        if (event.device!=null)
        mBleManagerWrapper.ConnectDevices(event.device);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    //初始化我们所需要的数据
    private void initView() {
        mBleManagerWrapper = BLEManagerWrapper.getInstance();
        mBleManagerWrapper.addSyncDataCallback(mISyncDataCallback);  //添加接口数据回调
        datadialog=new SyncDatadialog(getActivity());

    }




    @OnClick(R.id.btn_tongbu)
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.btn_tongbu:
             /*   BlueIsOpen();*/
                datadialog.inintDialog();
                break;

        }
    }



    @Override
    protected int getLayoutId() {
        return R.layout.monitorfragment_layout;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }




    //判断是否打开蓝牙
    public void BlueIsOpen() {

        //显判断蓝牙是否可用，如果可用，那么就开始同步数据
        if (mBleManagerWrapper.isBlueEnable()) {

            syncData();

        } else {

            mBleManagerWrapper.enableBluetooth();  //需要打开蓝牙
//            mSwipeRefreshView.setRefreshing(false);  //同时关闭数据刷新效果
        }
    }


    //开始同步数据
    private void syncData() {
        if (mBleManagerWrapper.isConnected()) {   //判断蓝牙是否已经连接成功

           //updateSyncState();  //改变连接的UI状态（连接成功）
            mBleManagerWrapper.syncData();

        } else {

//            setWaveState();
//            setCurrentStateText(getString(R.string.connecting));
//            setCurrentStateHintText(getString(R.string.message_waiting_connection));

            mBleManagerWrapper.ScanDevice();   //蓝牙管理者发送指令进行连接,扫描设备，可能会有多个设备，要进行选择
        }

    }


    /**
     *
     * 定义的蓝牙数据回调类，这个类用来处理蓝牙的回调数据的
     */

    private class ISyncDataCallbackImpl implements ISyncDataCallback {

        @Override
        public void onBleStateOn() {   //蓝牙设备开启
                syncData();  //同步数据
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
        public void DeviceCount(List<BleDevice> deviceList) {  //发现的蓝牙设备数
            KLog.i("-->> DeviceCount"+"扫描完成"+deviceList.toString());
            if (deviceList != null && deviceList.size() != 0) {
                String blue = sharedUtils.readString("blue");
                if (!TextUtils.isEmpty(blue)) {
                    for (int i=0;i<deviceList.size();i++){
                        if (deviceList.get(i).getName().equals(blue)){
                            mBleManagerWrapper.ConnectDevices(deviceList.get(i));
                            break;
                        }

                    }

                }

            }else{
                ShowToast("没有可连接的设备");
            }


        }

    }


    private void notifyCharacteristics(final BluetoothGatt gatt) {
        getActivity().runOnUiThread(new Runnable() {
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
        EventBus.getDefault().unregister(this);
    }


}
