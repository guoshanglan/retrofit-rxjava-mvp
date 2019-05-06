package cc.hisens.hardboiled.patient.ble.protocol;

import android.content.Context;
import android.content.Intent;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cc.hisens.hardboiled.patient.ble.algorithm.ErectionDataModel;
import cc.hisens.hardboiled.patient.utils.BytesUtils;
import cc.hisens.hardboiled.patient.utils.TimeUtils;


/**
 * @author Ou Weibin
 * @version 1.0
 */
public class CmdDecoder {

    private Context mContext;
    private Protocol mProtocol;
    private long sleepTime = 0;//佩戴时间
    private long wakeTime = 0;//摘下时间

    public CmdDecoder(Context context, Protocol protocol) {
        mContext = context;
        mProtocol = protocol;
    }

    /**
     * 解析通过自定义协议，解析byte数据
     *
     * @param rcv
     */
    public void decode(byte[] rcv) {

        byte cmd = rcv[Protocol.CMD_INDEX];
        KLog.i("接收数据成功=" + cmd);
        String threadName = Thread.currentThread().getName();
        long threadId = Thread.currentThread().getId();

        KLog.i("thread name:" + threadName +
                " , thread id:" + threadId);

        switch (cmd) {
            case CmdSet.CMD_SET_TIME:
                setTime(getPayload(rcv));
                break;
            case CmdSet.CMD_BATTERY:
                decodeBattery(getPayload(rcv));
                break;
            case CmdSet.CMD_SERIAL_NO:
                decodeSerialNo(getPayload(rcv));
                break;
            case CmdSet.CMD_ERECTION_DATA_COUNTS:
                getErectionDataCounts(getPayload(rcv));
                break;
            case CmdSet.CMD_GET_ERECTION_DATA_COMPLETED:
                getErectionDataCompleted(getPayload(rcv));
                break;
            case CmdSet.CMD_ERECTION_DATA:
                erectionData(getPayload(rcv));
                break;
            case CmdSet.CMD_LOST_ERECTION_DATA:
                lostErectionData(getPayload(rcv));
                break;
            case CmdSet.CMD_FOUNDATION_CONNECTED:
                foundationConnected(getPayload(rcv));
                break;
            case CmdSet.CMD_FOUNDATION_DISCONNECTED:
                foundationDisconnected(getPayload(rcv));
                break;
            case CmdSet.CMD_FOUNDATION_CONNECTION_STATE:
                getFoundationState(getPayload(rcv));
                break;
            case CmdSet.CMD_DEVICE_UTILITY_TIME:
                getDeviceUtilityTime(getPayload(rcv));
                break;
            default:
                break;
        }
    }

    public void rcvTimeout(byte cmd) {
        switch (cmd) {
            case CmdSet.CMD_SET_TIME:
                if (mProtocol.getOnSetTimeCallback() != null) {
                    mProtocol.getOnSetTimeCallback().onSetTime(true);
                }
                break;
            case CmdSet.CMD_BATTERY:
                if (mProtocol.getOnBatteryCallback() != null) {
                    mProtocol.getOnBatteryCallback().onBattery(0);
                }
                break;
            case CmdSet.CMD_SERIAL_NO:
                if (mProtocol.getSerialNoCallback() != null) {
                    mProtocol.getSerialNoCallback().onSerialNo("");
                }
                break;
            case CmdSet.CMD_ERECTION_DATA_COUNTS:
                if (mProtocol.getOnErectionCallback() != null) {
                    mProtocol.getOnErectionCallback().onGetErectionDataCount(0);
                }
                break;
            case CmdSet.CMD_GET_ERECTION_DATA_COMPLETED:
                if (mProtocol.getOnErectionCallback() != null) {
                    mProtocol.getOnErectionCallback().onCleanFlash(false);
                }
                break;
            case CmdSet.CMD_FOUNDATION_CONNECTION_STATE:
                if (mProtocol.getOnFoundationStateCallback() != null) {
                    mProtocol.getOnFoundationStateCallback().onConnectionState(false);
                }
                break;
            case CmdSet.CMD_DEVICE_UTILITY_TIME:
                if (mProtocol.getOnDeviceUtilityTimeCallback() != null) {
                    mProtocol.getOnDeviceUtilityTimeCallback().onDeviceUtilityTime(-1, -1, -1);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置时间
     *
     * @param payload
     */
    public void setTime(byte[] payload) {
        KLog.i("-->> 设置时间成功");
        if (mProtocol.getOnSetTimeCallback() != null) {
            mProtocol.getOnSetTimeCallback().onSetTime(true);
        }
    }

    /**
     * 解析byte类型的电量
     *
     * @param payload
     */
    public void decodeBattery(byte[] payload) {
        int value = BytesUtils.bytesToInt(payload);
        //        KLog.i("-->> 电量：" + value);
        if (mProtocol.getOnBatteryCallback() != null) {
            mProtocol.getOnBatteryCallback().onBattery(value);
        }
    }

    public void decodeSerialNo(byte[] payload) {
        String serialNo = BytesUtils.byteArrayToHexStr(payload);
        if (mProtocol.getSerialNoCallback() != null) {
            mProtocol.getSerialNoCallback().onSerialNo(serialNo);
        }
    }

    /**
     * 获取勃起数据记录条数
     *
     * @param payload
     */
    private void getErectionDataCounts(byte[] payload) {
        int count = payload.length > 0 ? BytesUtils.bytesToInt(payload) : 0;
        KLog.i("-->> 数据个数 ：" + count);
        if (mProtocol.getOnErectionCallback() != null) {
            mProtocol.getOnErectionCallback().onGetErectionDataCount(count);
        }
    }

    /**
     * 勃起数据发送完成
     *
     * @param payload
     */
    private void getErectionDataCompleted(byte[] payload) {
        KLog.i("-->> 勃起数据发送完成");
        if (mProtocol.getOnErectionCallback() != null) {
            if ((payload[0] & 0xFF) == 0) {
                mProtocol.getOnErectionCallback().onGetErectionDataCompleted(true);
            } else {
                mProtocol.getOnErectionCallback().onCleanFlash(true);
            }
        }
    }

    /**
     * 结束心率测量
     *
     * @param payload
     */
    private void erectionData(byte[] payload) {

        for (int i = 0; i < payload.length; i++) {
            KLog.i("勃起数据帧=" + (payload[i] & 0xFF) + " ");
        }

        List<ErectionDataModel> list = new ArrayList<>();
        int offset = 0;
        int firstTimestamp = 0;
        byte[] bytes = new byte[]{(byte) 0x90};

        int packetId = BytesUtils.bytesToInt(Arrays.copyOfRange(payload, 15, 17));

        if (payload.length > 7) {

            int timestamp = BytesUtils.bytesToInt(Arrays.copyOfRange(payload, offset, offset + 4));
            offset += 4;

            String dataString = BytesUtils.byteArrayToHexStr(Arrays.copyOfRange(payload, offset, offset + 3));
            if (dataString.equals("FFFFFF") || dataString.equals("FFEFFE")) {//佩戴时间和结束佩戴时间数据帧

                if (dataString.equals("FFEFFE"))
                    sleepTime = TimeUtils.toMillis(timestamp);
                else {
                    wakeTime = TimeUtils.toMillis(timestamp);
                    if (mProtocol.getOnErectionCallback() != null) {
                        mProtocol.getOnErectionCallback().onErectionData(packetId, list, sleepTime, wakeTime);
                    }
                }
//                KLog.i("数值=" + dataString + " " + timestamp);
                return;

            }

            int data1 = BytesUtils.bytesToInt(Arrays.copyOfRange(payload, offset, offset + 2));

            offset += 2;

//            int data2 = BytesUtils.bytesToIntBig(BytesUtils.hexStrToByteArray("0" + dataString.substring(3)));
//            KLog.i("数据1=" + dataString + "---" + data1 + " " + BytesUtils.hexStrToByteArray("0" + dataString.substring(0, 3))[0] + " " + BytesUtils.hexStrToByteArray("0" + dataString.substring(0, 3))[1]);
//            KLog.i("数据2=" + dataString + "--- " + data2);
            list.add(new ErectionDataModel(data1, TimeUtils.toMillis(timestamp)));
            firstTimestamp = timestamp;

        }

        int count = (payload.length - offset) / 4;
        for (int i = 0; i < count - 1; i++) {

            int from = offset + i * 4;
            int timestamp = BytesUtils.bytesToInt(Arrays.copyOfRange(payload, from, from + 2)) + firstTimestamp;
            from += 2;

//            String dataString = BytesUtils.byteArrayToHexStr(Arrays.copyOfRange(payload, from, from + 2));

            int data1 = BytesUtils.bytesToInt(Arrays.copyOfRange(payload, from, from + 2));

//            int data2 = BytesUtils.bytesToIntBig(BytesUtils.hexStrToByteArray("0" + dataString.substring(3)));
//            KLog.i("第" + i + "数据1=" + dataString + "--- " + data1 + " " + BytesUtils.hexStrToByteArray("0" + dataString.substring(0, 3))[0] + " " + BytesUtils.hexStrToByteArray("0" + dataString.substring(0, 3))[1]);
//            KLog.i("第" + i + "数据2=" + dataString + "--- " + data2);
            list.add(new ErectionDataModel(data1, TimeUtils.toMillis(timestamp)));

        }


        if (mProtocol.getOnErectionCallback() != null) {
            mProtocol.getOnErectionCallback().onErectionData(packetId, list, sleepTime, wakeTime);
        }

    }

    private void lostErectionData(byte[] payload) {
        List<ErectionDataModel> list = new ArrayList<>();

        int offset = 0;
        int firstTimestamp = 0;

        if (payload.length > 7) {

            int timestamp = BytesUtils.bytesToInt(Arrays.copyOfRange(payload, offset, offset + 4));
            offset += 4;

            int data1 = BytesUtils.bytesToInt(Arrays.copyOfRange(payload, offset, offset + 2));

            offset += 2;

            list.add(new ErectionDataModel(data1, TimeUtils.toMillis(timestamp)));
            firstTimestamp = timestamp;

        }

        int count = (payload.length - offset) / 4;

        for (int i = 0; i < count - 1; i++) {

            int from = offset + i * 4;
            int timestamp = BytesUtils.bytesToInt(Arrays.copyOfRange(payload, from, from + 2)) + firstTimestamp;
            from += 2;

            int data1 = BytesUtils.bytesToInt(Arrays.copyOfRange(payload, from, from + 2));

            list.add(new ErectionDataModel(data1, TimeUtils.toMillis(timestamp)));

        }

        int packetId = BytesUtils.bytesToInt(Arrays.copyOfRange(payload, 15, 17));

        if (mProtocol.getOnErectionCallback() != null) {
            mProtocol.getOnErectionCallback().onLostErectionData(packetId, list);
        }

    }

    /**
     * 设备接上底座
     *
     * @param payload
     */
    private void foundationConnected(byte[] payload) {
        KLog.i("-->> 设备接上底座");
        Intent intent = new Intent(Protocol.ACTION_FOUNDATION_CONNECTION_STATE);
        intent.putExtra(Protocol.EXTRA_IS_CONNECTED, true);
        mContext.sendBroadcast(intent);
    }

    /**
     * 设备断开底座
     *
     * @param payload
     */
    private void foundationDisconnected(byte[] payload) {
        KLog.i("-->> 设备断开底座");
        Intent intent = new Intent(Protocol.ACTION_FOUNDATION_CONNECTION_STATE);
        intent.putExtra(Protocol.EXTRA_IS_CONNECTED, false);
        mContext.sendBroadcast(intent);
    }

    /**
     * 获取设备和底座当前连接状态
     *
     * @param payload
     */
    private void getFoundationState(byte[] payload) {
        KLog.i("-->> 获取设备和底座当前连接状态 : " + String.valueOf((payload[0] & 0xFF) == 0));
        if (mProtocol.getOnFoundationStateCallback() != null) {
            mProtocol.getOnFoundationStateCallback().onConnectionState((payload[0] & 0xFF) == 0);
        }
    }

    public void getDeviceUtilityTime(byte[] payload) {
        int offset = 0;
        long firstUseTimestamp = TimeUtils.toMillis(BytesUtils.bytesToInt(Arrays.copyOfRange(payload, offset, offset + 4)));
        offset += 4;
        int utilityTime = BytesUtils.bytesToInt(Arrays.copyOfRange(payload, offset, offset + 4));
        offset += 4;
        int usageCount = BytesUtils.bytesToInt(Arrays.copyOfRange(payload, offset, offset + 4));

        KLog.i("-->> firstUseTimestamp = " + firstUseTimestamp + ", utilityTime = " + utilityTime + ", usageCount = " + usageCount);
        if (mProtocol.getOnDeviceUtilityTimeCallback() != null) {
            mProtocol.getOnDeviceUtilityTimeCallback().onDeviceUtilityTime(firstUseTimestamp, utilityTime, usageCount);
        }
    }

    /**
     * 得到数据包的数据域
     *
     * @param data
     * @return
     */
    private byte[] getPayload(byte[] data) {
        if (data.length > Protocol.PACKET_HEADER_LEN) {
            return Arrays.copyOfRange(data, Protocol.PACKET_HEADER_LEN,
                    Protocol.PACKET_HEADER_LEN + (data[Protocol.CMD_LEN_INDEX] & 0xFF));
        } else {
            return new byte[0];
        }
    }
}
