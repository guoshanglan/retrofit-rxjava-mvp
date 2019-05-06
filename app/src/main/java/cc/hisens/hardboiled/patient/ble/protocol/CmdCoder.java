package cc.hisens.hardboiled.patient.ble.protocol;


import java.util.List;

import cc.hisens.hardboiled.patient.ble.BLEManagerWrapper;
import cc.hisens.hardboiled.patient.ble.callbacks.IDeviceUtilityTimeCallback;
import cc.hisens.hardboiled.patient.ble.protocol.callbacks.OnBatteryCallback;
import cc.hisens.hardboiled.patient.ble.protocol.callbacks.OnErectionCallback;
import cc.hisens.hardboiled.patient.ble.protocol.callbacks.OnFoundationStateCallback;
import cc.hisens.hardboiled.patient.ble.protocol.callbacks.OnSerialNoCallback;
import cc.hisens.hardboiled.patient.ble.protocol.callbacks.OnSetTimeCallback;
import cc.hisens.hardboiled.patient.utils.BytesUtils;
import cc.hisens.hardboiled.patient.utils.TimeUtils;


/**
 * @author Ou Weibin
 * @version 1.0
 *
 *
 * 蓝牙的命令操作
 */
public class CmdCoder {

    private Protocol mProtocol;
    private BLEManagerWrapper mBluetoothManager;

    public CmdCoder(BLEManagerWrapper bluetoothLeManager, Protocol protocol) {
        mBluetoothManager = bluetoothLeManager;
        mProtocol = protocol;
    }

    /**
     * 设置时间
     */
    public void setTime(OnSetTimeCallback callback) {
        mProtocol.setOnSetTimeCallback(callback);
        byte[] header = createPacketHeader(CmdSet.CMD_SET_TIME);
        byte[] payload = BytesUtils.intToBytes(TimeUtils.toSecs(System.currentTimeMillis()));
        code(BytesUtils.concat(header, payload));
    }

    /**
     * 查询电量
     */
    public void queryBattery(OnBatteryCallback callback) {
        mProtocol.setOnBatteryCallback(callback);
        byte[] header = createPacketHeader(CmdSet.CMD_BATTERY);
        code(header);
    }


    /**
     * 查询设备序列号
     */
    public void querySerialNo(OnSerialNoCallback callback) {
        mProtocol.setSerialNoCallback(callback);
        byte[] header = createPacketHeader(CmdSet.CMD_SERIAL_NO);
        code(header);
    }
    /**
     * 获取勃起数据记录条数
     */
    public void getErectionDataCounts(OnErectionCallback callback) {
        mProtocol.setOnErectionCallback(callback);
        byte[] header = createPacketHeader(CmdSet.CMD_ERECTION_DATA_COUNTS);
        code(header);
    }

    /**
     * 获取勃起数据
     */
    public void getErectionData() {
        byte[] header = createPacketHeader(CmdSet.CMD_ERECTION_DATA);
        code(header);
    }

    /**
     * 获取丢失勃起数据
     */
    public void getLostErectionData(List<Integer> lostPacketId) {

        if (lostPacketId == null) return;

        byte[] header = createPacketHeader(CmdSet.CMD_LOST_ERECTION_DATA);

        byte[] payload = new byte[lostPacketId.size() * 2];


        for (int i = 0; i < lostPacketId.size(); i++) {
            System.arraycopy(BytesUtils.intToBytes(lostPacketId.get(i), (byte) 2), 0, payload, i * 2, 2);
        }
        code(BytesUtils.concatHeaderAndPayload(header, payload));

    }

    /**
     * 获取勃起数据成功,清除数据
     */
    public void getErectionDataCompleted() {
        byte[] header = createPacketHeader(CmdSet.CMD_GET_ERECTION_DATA_COMPLETED);
        byte[] payload = new byte[]{0x00};
        code(BytesUtils.concat(header, payload));
    }

    /**
     * 获取底座状态
     */
    public void getFoundationState(OnFoundationStateCallback callback) {
        mProtocol.setOnFoundationStateCallback(callback);
        byte[] header = createPacketHeader(CmdSet.CMD_FOUNDATION_CONNECTION_STATE);
        code(header);
    }

    /**
     * 获取设备连接时长
     */
    public void getDeviceUtilityTime(IDeviceUtilityTimeCallback callback) {
        mProtocol.setOnDeviceUtilityTimeCallback(callback);
        byte[] header = createPacketHeader(CmdSet.CMD_DEVICE_UTILITY_TIME);
        code(header);
    }

    /**
     * 创建帧头部包含len和cmd
     *
     * @param cmd
     * @return
     */
    private byte[] createPacketHeader(byte cmd) {
        byte[] header = new byte[Protocol.PACKET_HEADER_LEN];
        header[Protocol.CMD_INDEX] = cmd;
        return header;
    }

    //返回的蓝牙命令操作
    private void code(byte[] data) {
        data[Protocol.CMD_LEN_INDEX] = (byte) ((data.length - Protocol.PACKET_HEADER_LEN) & 0xFF);
        if (mProtocol != null && mProtocol instanceof OnCmdCodeListener) {
            mProtocol.onCoded(data);
        }
    }

    public interface OnCmdCodeListener {
        void onCoded(byte[] data);
    }
}
