package cc.hisens.hardboiled.patient.ble;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;

import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleScanAndConnectCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import cc.hisens.hardboiled.patient.ble.callbacks.IBleReadCallback;
import cc.hisens.hardboiled.patient.ble.callbacks.IBleWriteCallback;
import cc.hisens.hardboiled.patient.ble.callbacks.ISyncDataCallback;
import cc.hisens.hardboiled.patient.ble.protocol.Protocol;
import cc.hisens.hardboiled.patient.ble.protocol.callbacks.OnSetTimeCallback;
import cc.hisens.hardboiled.patient.utils.BytesUtils;
import cc.hisens.hardboiled.patient.utils.ToastUtils;


/**
 * @author Waiban
 * @package cc.hisens.hardboiled.data.ble
 * @fileName BLEManagerWrapper
 * @date on 2017/5/18 10:57
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 */

public class BLEManagerWrapper {

    private static BLEManagerWrapper sInstance;   //当前类的对象
    private Context mContext;
    private BleService mBleService;
    private SyncDataService mSyncDataService;   //同步数据的服务

    private CallbackInfo mCallbackInfo;
    private List<ISyncDataCallback> mISyncDataCallbackList;
    private BleNotifyCallback mBleNotifyCallback;

    private BleDevice mBleDevice;   //蓝牙设备的bean类
    private static int mConnectionCount = 0;  //连接次数
    private static int mNotifyCount = 0;    //通知的次数
    private boolean mNotifySuccess = false;   //蓝牙回调通知是否成功
    private BluetoothGatt mGatt;
    private List<BleDevice> deviceList;  //扫描到多个设备的集合
    private ServiceConnection mBleServiceConnection = new ServiceConnection() {    //蓝牙连接绑定服务连接
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            KLog.i("onServiceConnected connected");
            mBleService = ((BleService.MyBinder) binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            KLog.i("onServiceDisconnected");
        }
    };

    private ServiceConnection mSyncDataServiceConnection = new ServiceConnection() {    //数据同步绑定服务
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            mSyncDataService = ((SyncDataService.MyBinder) binder).getService();
            mSyncDataService.setISyncDataCallbackList(mISyncDataCallbackList);
            mSyncDataService.syncData();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mSyncDataService = null;
        }
    };

    private BroadcastReceiver mBleStateChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {     //广播接收者
            String action = intent.getAction();
            // 蓝牙状态改变
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                switch (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)) {
                    case BluetoothAdapter.STATE_ON:
                        KLog.i("-->> ble on");
                        for (ISyncDataCallback callback : mISyncDataCallbackList) {
                            callback.onBleStateOn();
                        }
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        KLog.i("-->> ble off");
                        for (ISyncDataCallback callback : mISyncDataCallbackList) {
                            callback.onBleStateOff();
                        }
                        break;
                }
            }
        }
    };

    private BLEManagerWrapper() {
    }

    public static BLEManagerWrapper getInstance() {
        if (sInstance == null) {
            sInstance = new BLEManagerWrapper();
        }
        return sInstance;
    }


    public void initialize(Context context) {
        mContext = context;
        context.bindService(BleService.getCallingIntent(context), mBleServiceConnection, Context.BIND_AUTO_CREATE);
        registerReceiver();
        mBleNotifyCallback = new BleNotifyCallbackImpl();
        mISyncDataCallbackList = new ArrayList<>();
        deviceList=new ArrayList<>();
    }


    //注销广播
    public void recycle() {
        unregisterReceiver();

    }


    //连接蓝牙设备
    public void connectDevice() {

        if (mBleService != null) {

            scanNameAndConnect();

        }

    }


    //判断当前蓝牙是否已经连接
    public boolean isConnected() {
        return mBleService != null && mBleService.isConnected(mBleDevice);
    }

    /**
     * notify
     *
     * @param serviceUUID
     * @param notifyUUID
     * @param callback
     * @return
     */
    public void notify(BleDevice device, String serviceUUID,
                       String notifyUUID,
                       BleNotifyCallback callback) {
        if (mBleService != null) {
            mBleService.notify(device, serviceUUID, notifyUUID, callback);
        }
    }

    public void write(byte[] data, BleWriteCallback characterCallback) {
//        BytesUtils.printHex("write", data);
        String threadName = Thread.currentThread().getName();
        long threadId = Thread.currentThread().getId();

        KLog.i("thread name:" + threadName +
                " , thread id:" + threadId);

        writeDevice(UUIDConstants.SERVICE.toString(),
                UUIDConstants.CHAR_WRITE.toString(), data, characterCallback);
    }

    /**
     * write
     *
     * @param serviceUUID
     * @param writeUUID
     * @param data
     * @param callback
     * @return
     */
    public void writeDevice(String serviceUUID,
                            String writeUUID,
                            byte[] data,
                            BleWriteCallback callback) {
        String threadName = Thread.currentThread().getName();
        long threadId = Thread.currentThread().getId();

        KLog.i("thread name:" + threadName +
                " , thread id:" + threadId);

        if (mBleService != null) {
            mBleService.writeDevice(mBleDevice, serviceUUID, writeUUID, data, callback);
        }

    }

    /**
     * read
     *
     * @param serviceUUID
     * @param readUUID
     * @param callback
     * @return
     */
    public void readDevice(BleDevice device, String serviceUUID,
                           String readUUID,
                           BleReadCallback callback) {
        mBleService.readDevice(device, serviceUUID, readUUID, callback);
    }

    /**
     * open bluetooth
     * 打开蓝牙
     */
    public void enableBluetooth() {
        if (mBleService != null)
            mBleService.enableBluetooth();
    }

    /**
     * close bluetooth
     * 关闭蓝牙
     */
    public void disableBluetooth() {
        if (mBleService != null)
            mBleService.disableBluetooth();
    }

    public boolean isBlueEnable() {
        return mBleService != null && mBleService.isBlueEnable();
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void syncData() {

        Protocol.getInstance(mContext).clear();   //同步数据之前，先清除上一次同步的数据


        if (mSyncDataService != null) {    //如果这个同步数据服务不为空

            if (mNotifySuccess) {

                mSyncDataService.syncData();
            } else {
                notifyCharacteristics(mGatt.getService(UUIDConstants.SERVICE));
            }

        } else {
            mContext.bindService(SyncDataService.getCallingIntent(mContext), mSyncDataServiceConnection, Context.BIND_AUTO_CREATE); //开始启动bindservice
        }
    }

    public void setIBleReadCallback(IBleReadCallback callback) {
        getCallbackInfo().mIBleReadCallback = callback;
    }

    public void setIBleWriteCallback(IBleWriteCallback callback) {
        getCallbackInfo().mIBleWriteCallback = callback;
    }

    public void addSyncDataCallback(ISyncDataCallback callback) {
        if (mBleService != null && callback != null && !mISyncDataCallbackList.contains(callback)) {
            mISyncDataCallbackList.add(callback);
        }
    }

    public void removeSyncDataCallback(ISyncDataCallback callback) {
        if (mBleService != null && callback != null && mISyncDataCallbackList.contains(callback)) {
            mISyncDataCallbackList.remove(callback);
        }
    }

    /**
     * close gatt
     */
    private void closeBluetoothGatt() {
        if (mBleService != null)
            mBleService.closeBluetoothGatt();
    }


    /**
     * 使能特征
     *
     * @param service
     */
    @SuppressLint("NewApi")
    public void notifyCharacteristics(BluetoothGattService service) {
        if (mBleService == null || service == null) {
            return;
        }

        // 使能自定义服务通知
        if (UUIDConstants.SERVICE.equals(service.getUuid())
                && service.getCharacteristic(UUIDConstants.CHAR_NOTIFY) != null) {

            mNotifyCount += 1;
            if (mNotifyCount == 1) {
                KLog.i("--> 使能成功");
                Protocol.getInstance(mContext).clear();
                notifyService(mBleDevice);
            }

        } else {

            KLog.i("--> 使能失败");
            // 断开蓝牙
            closeBluetoothGatt();

        }

    }
//断开连接
    public void disconnect() {
        mBleService.disconnect(mBleDevice);
    }

    private void notifyService(BleDevice device) {
        notify(device, UUIDConstants.SERVICE.toString(), UUIDConstants.CHAR_NOTIFY.toString(), mBleNotifyCallback);

    }


    //注册蓝牙广播
    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        mContext.registerReceiver(mBleStateChangedReceiver, intentFilter);
    }

    //注销蓝牙广播
    private void unregisterReceiver() {
        mContext.unregisterReceiver(mBleStateChangedReceiver);
    }


    //信息的接口回调
    private CallbackInfo getCallbackInfo() {
        if (mCallbackInfo != null) {
            return mCallbackInfo;
        }

        mCallbackInfo = new CallbackInfo();
        return mCallbackInfo;
    }


    private static class CallbackInfo {
        IBleWriteCallback mIBleWriteCallback;   //写入的信息回调
        IBleReadCallback mIBleReadCallback;    //读取的信息回调
    }

    private class BleNotifyCallbackImpl extends BleNotifyCallback {

        @Override
        public void onNotifySuccess() {
            KLog.i("onNotifySuccess");
            mNotifySuccess = true;
            setTime();

        }

        @Override
        public void onNotifyFailure(BleException exception) {
            KLog.i("onNotifyFailure");
            mNotifySuccess = false;
            mNotifyCount = 0;
        }

        @Override
        public void onCharacteristicChanged(byte[] data) {
            KLog.i("onCharacteristicChanged");

            BytesUtils.printHex("read   ", data);
            // 上传到协议层解析
            if (getCallbackInfo().mIBleReadCallback != null) {
                getCallbackInfo().mIBleReadCallback.onRead(data);
            }

        }

    }

    private void setTime() {

        KLog.i("setTime");

        Protocol.getInstance(mContext).getCmdCoder().setTime(new OnSetTimeCallback() {
            @Override
            public void onSetTime(boolean isSuccess) {

                KLog.i("onSetTime:" + isSuccess);
                if (isSuccess) {
                    // 同步数据
                    for (ISyncDataCallback callback : mISyncDataCallbackList) {
                        callback.onSyncData();
                    }

                    syncData();

                } else {
                    for (ISyncDataCallback callback : mISyncDataCallbackList) {
                        callback.onSetTimeFailed();
                    }
                }

            }
        });

    }


    //蓝牙连接，有多个设备时用这个方法进行连接,传进来的position来确认是需要连接哪个设备
    private  void  Connect(int position){



        if(deviceList!=null&&deviceList.size()!=0) {
            //判断当前是否已经连接上了蓝牙设备，所以需要先断开之前的连接在进行重连
            if (isConnected()){

                this.disconnect();
            }

            mBleService.connectDevice(deviceList.get(position), new BleGattCallback() {
                @Override
                public void onStartConnect() {
                    mConnectionCount = 0;
                    mNotifyCount = 0;

                }

                @Override
                public void onConnectFail(BleDevice bleDevice, BleException exception) {
                    mConnectionCount = 0;
                    mNotifyCount = 0;

                    KLog.i("onConnectFail" + exception);
                    for (ISyncDataCallback callback : mISyncDataCallbackList) {
                        callback.onConnectionFailed();
                    }
                }

                @SuppressLint("NewApi")
                @Override
                public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                    KLog.i("onConnectSuccess " + status);
                    mConnectionCount += 1;
                    mBleDevice = bleDevice;
                    mGatt = gatt;

                    if (mConnectionCount == 1) {
                        for (ISyncDataCallback callback : mISyncDataCallbackList) {
                            callback.onConnectionSuccessful(bleDevice);
                        }

                    }

                    notifyCharacteristics(gatt.getService(UUIDConstants.SERVICE));
                }

                @Override
                public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                    KLog.i("onDisConnected");
                    mConnectionCount = 0;
                    mNotifyCount = 0;
                    for (ISyncDataCallback callback : mISyncDataCallbackList) {
                        callback.onDisconnect();
                    }
                }
            });
        }else{
            ToastUtils.show(mContext,"当前没有可以连接的蓝牙设备~");
        }

    }




    //扫描设备，没有进行连接,可能有多个设备需要展示
    private  void  ScanName(){
        mBleService.scanDevice(new BleScanCallback() {
            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                if(scanResultList==null||scanResultList.size()==0){
                    for (ISyncDataCallback callback : mISyncDataCallbackList) {
                        callback.onNotFoundDevice();
                    }
                }else{
                    deviceList=scanResultList;

                }

            }

            @Override
            public void onScanStarted(boolean success) {

            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                KLog.i("onScanning");
                mBleService.cancelScan();
            }
        });
    }




    //扫描蓝牙设备并且进行连接
    private void scanNameAndConnect() {


        mBleService.scanNameAndConnect(new BleScanAndConnectCallback() {
            @Override
            public void onScanFinished(BleDevice scanResult) {
                KLog.i("onScanFinished " + scanResult);

                if (scanResult == null) {    //如果扫描为空，则返回设备未找到的回调
                    for (ISyncDataCallback callback : mISyncDataCallbackList) {
                        callback.onNotFoundDevice();
                    }
                }


            }

            @Override
            public void onStartConnect() {
                mConnectionCount = 0;
                mNotifyCount = 0;

                KLog.i("onStartConnect");
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                mConnectionCount = 0;
                mNotifyCount = 0;

                KLog.i("onConnectFail" + exception);
                for (ISyncDataCallback callback : mISyncDataCallbackList) {
                    callback.onConnectionFailed();
                }
            }

            @SuppressLint("NewApi")
            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {

                KLog.i("onConnectSuccess " + status);
                mConnectionCount += 1;
                mBleDevice = bleDevice;
                mGatt = gatt;

                if (mConnectionCount == 1) {
                    for (ISyncDataCallback callback : mISyncDataCallbackList) {
                        callback.onConnectionSuccessful(bleDevice);
                    }

                }

                notifyCharacteristics(gatt.getService(UUIDConstants.SERVICE));

            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                KLog.i("onDisConnected");
                mConnectionCount = 0;
                mNotifyCount = 0;
                for (ISyncDataCallback callback : mISyncDataCallbackList) {
                    callback.onDisconnect();
                }

            }

            @Override
            public void onScanStarted(boolean success) {
                KLog.i("onScanStarted");
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                KLog.i("onScanning");
                mBleService.cancelScan();
            }

        });
    }


}
