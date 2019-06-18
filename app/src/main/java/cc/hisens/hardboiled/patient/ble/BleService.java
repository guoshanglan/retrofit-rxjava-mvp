package cc.hisens.hardboiled.patient.ble;

import android.app.Service;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleIndicateCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleScanAndConnectCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.data.BleScanState;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.socks.library.KLog;

import cc.hisens.hardboiled.patient.PatientConstants;


//蓝牙服务
public class  BleService  extends Service {

    private BleManager  mBleManager;
    private boolean mInitialized;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, BleService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        KLog.i("<<--onCreate-->>");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        KLog.i("<<--onStartCommand-->>");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        KLog.i("<<--onBind-->>");

        String name = Thread.currentThread().getName();
        long id = Thread.currentThread().getId();
        KLog.i("thread name:" + name + " ,thread id:" + id);

        if (!mInitialized) {
            mInitialized = true;
            initialize();
        }
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        KLog.i("<<--onUnbind-->>");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        KLog.i("<<--onDestroy-->>");
        mBleManager.destroy();     // 进行蓝牙管理者的销毁
        super.onDestroy();
    }


    //蓝牙的初始化，这个是运行在后台的
    private void initialize() {
        BleManager.getInstance().init(getApplication());
        mBleManager = BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setSplitWriteNum(20)
                .setConnectOverTime(20*1000)
                .setOperateTimeout(5000);

        BleScanRuleConfig config = new BleScanRuleConfig.Builder()
                .setAutoConnect(true)
                .setDeviceName(true, PatientConstants.DEVICE_NAME)
                .setScanTimeOut(10*1000)
                .build();
        mBleManager.initScanRule(config);
    }

    /**
     * scan device around
     */
    public void scanDevice(BleScanCallback callback) {

        mBleManager.scan(callback);
    }

    /**
     * connect a searched device
     * @param device   searched device
     * @param callback
     */
    public void connectDevice(BleDevice device,
                              BleGattCallback callback) {
        mBleManager.connect(device, callback);
    }

    /**
     * scan a known name device, then connect
     *
     * @param callback
     * @return
     */
    public void scanNameAndConnect(BleScanAndConnectCallback callback) {
        mBleManager.scanAndConnect(callback);

    }


    /**
     * cancel scan
     */
    public void cancelScan() {
        mBleManager.cancelScan();
    }

    /**
     * notify
     *
     * @param serviceUUID
     * @param notifyUUID
     * @param callback
     * @return
     */
    public void notify(BleDevice device,
                       String serviceUUID,
                       String notifyUUID,
                       BleNotifyCallback callback) {

        mBleManager.notify(device, serviceUUID, notifyUUID, callback);

    }

    /**
     * indicate
     *
     * @param serviceUUID
     * @param indicateUUID
     * @param callback
     * @return
     */
    public void indicate(BleDevice device, String serviceUUID,
                         String indicateUUID,
                         BleIndicateCallback callback) {
        mBleManager.indicate(device, serviceUUID, indicateUUID, callback);
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
    public void writeDevice(final BleDevice device, final String serviceUUID,
                            final String writeUUID,
                            final byte[] data,
                            final BleWriteCallback callback) {

        String threadName = Thread.currentThread().getName();
        long threadId = Thread.currentThread().getId();

        KLog.i("thread name:" + threadName +
                " , thread id:" + threadId);
        final boolean[] temp = new boolean[1];

        if (Looper.myLooper() == null) {

            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    mBleManager.write(device, serviceUUID, writeUUID, data, callback);
                    Looper.loop();
                }
            }.start();

        } else {
            mBleManager.write(device, serviceUUID, writeUUID, data, callback);
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

        mBleManager.read(device, serviceUUID, readUUID, callback);

    }

    /**
     * get state
     */
    public void getBluetoothState() {
    }

    /**
     * close gatt
     */
    public void closeBluetoothGatt() {


    }

    public void disconnect(BleDevice device) {
        mBleManager.disconnect(device);
    }
    /**
     * is support ble?
     */
    public boolean isSupportBle() {
        return mBleManager.isSupportBle();
    }

    /**
     * open bluetooth
     */
    public void enableBluetooth() {
        mBleManager.enableBluetooth();
    }

    /**
     * close bluetooth
     */
    public void disableBluetooth() {
        mBleManager.disableBluetooth();
        mBleManager.destroy();
    }

    public boolean isBlueEnable() {
        return mBleManager.isBlueEnable();
    }

    public boolean isInScanning() {

        return mBleManager.getScanSate() == BleScanState.STATE_SCANNING;
    }

    public boolean isConnectingOrConnected(BleDevice device) {

        int connectState = mBleManager.getConnectState(device);
        return connectState == BluetoothProfile.STATE_CONNECTING
                || connectState == BluetoothProfile.STATE_CONNECTED;
    }

    public boolean isConnected(BleDevice device) {
        return mBleManager.isConnected(device);
    }

    public boolean isServiceDiscovered() {
        return false;
    }

    /**
     * remove callback form a character
     */
    public void stopListenCharacterCallback(String uuid) {
//        mBleManager.stopListenCharacterCallback(uuid);
    }

    /**
     * remove callback for gatt connect
     */
    public void stopListenConnectCallback(BleDevice device) {
        mBleManager.clearCharacterCallback(device);
    }

    /**
     * stop notify, remove callback
     */
    public boolean stopNotify(BleDevice device, String serviceUUID, String notifyUUID) {
        return mBleManager.stopNotify(device, serviceUUID, notifyUUID);
    }

    /**
     * stop indicate, remove callback
     */
    public boolean stopIndicate(BleDevice device, String serviceUUID, String indicateUUID) {
        return mBleManager.stopIndicate(device, serviceUUID, indicateUUID);
    }

    public class MyBinder extends Binder {
        public BleService getService() {
            String name = Thread.currentThread().getName();
            long id = Thread.currentThread().getId();
            KLog.i("thread name:" + name + " ,thread id:" + id);
            return BleService.this;
        }
    }
}
