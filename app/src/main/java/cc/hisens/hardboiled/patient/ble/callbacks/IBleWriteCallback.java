package cc.hisens.hardboiled.patient.ble.callbacks;

/**
 * @author Ou Weibin
 * @version 1.0
 *
 * 蓝牙数据写入接口回调
 */
public interface IBleWriteCallback {
    void onWrite(byte[] data);
}
