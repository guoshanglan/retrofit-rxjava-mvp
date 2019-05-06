package cc.hisens.hardboiled.patient.ble.callbacks;

import android.bluetooth.BluetoothGatt;

import com.clj.fastble.data.BleDevice;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.data.ble.callbacks
 * @fileName ISyncDataCallback
 * @date on 2017/7/5 14:57
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 *
 * 蓝牙数据回调
 */

public interface ISyncDataCallback {
    void onBleStateOn();

    void onBleStateOff();

    void onBattery(int value);

    void onSerialNo(String serialNo);

    void onConnectionSuccessful(BleDevice device);

    void onConnectionFailed();

    void onNotFoundDevice();

    void onDisconnect();

    void onSyncProgressUpdate(int value);

    void onSyncSuccessful(long startSleep);

    void onSyncFailed();

    void onServiceDiscovered(final BluetoothGatt gatt, int status);

    void onSetTimeFailed();

    void onSyncData();

    class SimpleSyncDataCallback implements ISyncDataCallback {
        @Override
        public void onBleStateOn() {

        }

        @Override
        public void onBleStateOff() {

        }

        @Override
        public void onBattery(int value) {

        }

        @Override
        public void onSerialNo(String serialNo) {

        }

        @Override
        public void onConnectionSuccessful(BleDevice device) {

        }

        @Override
        public void onConnectionFailed() {

        }

        @Override
        public void onNotFoundDevice() {

        }

        @Override
        public void onDisconnect() {

        }

        @Override
        public void onSyncProgressUpdate(int value) {

        }

        @Override
        public void onSyncSuccessful(long startSleep) {

        }

        @Override
        public void onSyncFailed() {

        }

        @Override
        public void onServiceDiscovered(BluetoothGatt gatt, int status) {

        }

        @Override
        public void onSetTimeFailed() {

        }

        @Override
        public void onSyncData() {

        }
    }
}
