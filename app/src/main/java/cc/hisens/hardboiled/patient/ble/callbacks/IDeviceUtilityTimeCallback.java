package cc.hisens.hardboiled.patient.ble.callbacks;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.data.ble.callbacks
 * @fileName IDeviceUtilityTimeCallback
 * @date on 2017/7/11 18:04
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 *
 * 蓝牙时长数据回调
 */

public interface IDeviceUtilityTimeCallback {
    void onDeviceUtilityTime(long firstUseTimestamp, int utilityTime, int usageCount);
}
