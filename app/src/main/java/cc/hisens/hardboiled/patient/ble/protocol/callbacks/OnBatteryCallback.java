package cc.hisens.hardboiled.patient.ble.protocol.callbacks;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.data.ble.protocol.callbacks
 * @fileName OnBatteryCallback
 * @date on 2017/7/5 17:58
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 *
 *
 *
 * 电量数据回调
 */

public interface OnBatteryCallback {
    void onBattery(int value);
}
