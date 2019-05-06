package cc.hisens.hardboiled.patient.ble.protocol.callbacks;

import java.util.List;


import cc.hisens.hardboiled.patient.ble.algorithm.ErectionDataModel;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.data.ble.protocol.callbacks
 * @fileName OnErectionCallback
 * @date on 2017/7/5 17:59
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 *   /**
 *      * 获取勃起数据记录条数
 *
 */

public interface OnErectionCallback {
    void onGetErectionDataCount(int count);

    void onErectionData(int packetId, List<ErectionDataModel> list, long sleepTime, long wakeTime);

    void onLostErectionData(int packetId, List<ErectionDataModel> list);

    void onGetErectionDataCompleted(boolean isCompleted);

    void onCleanFlash(boolean isSuccessful);
}
