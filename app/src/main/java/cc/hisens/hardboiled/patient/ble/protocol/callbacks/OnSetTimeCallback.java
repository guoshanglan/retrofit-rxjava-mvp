package cc.hisens.hardboiled.patient.ble.protocol.callbacks;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.data.ble.protocol.callbacks
 * @fileName OnSetTimeCallback
 * @date on 2017/7/7 9:56
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 *
 * 时间回调机制
 */

public interface OnSetTimeCallback {
    void onSetTime(boolean isSuccess);
}
