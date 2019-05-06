package cc.hisens.hardboiled.patient.ble.callbacks;

/**
 * @author Ou Weibin
 * @version 1.0
 *
 * 蓝牙数据读取接口回调
 */
public interface IBleReadCallback {
    void onRead(byte[] rcv);
}
