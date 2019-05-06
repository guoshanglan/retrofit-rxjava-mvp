package cc.hisens.hardboiled.patient.ble.protocol.callbacks;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.data.ble.protocol.callbacks
 * @fileName OnFoundationStateCallback
 * @date on 2017/7/5 18:06
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 *
 * 蓝牙与设备的连接状态的回调
 */

public interface OnFoundationStateCallback {
    void onConnectionState(boolean isConnected);
}
