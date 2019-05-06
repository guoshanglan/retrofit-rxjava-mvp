package cc.hisens.hardboiled.patient.ble.protocol;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.data.ble.protocol
 * @fileName CmdSet
 * @date on 2017/6/26 10:55
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 */

public class CmdSet {
    /* 设置时间*/
    public static final byte CMD_SET_TIME = (byte) 0x11;
    /* 查询电量 */
    public static final byte CMD_BATTERY = (byte) 0x12;

    /* 开始获取勃起数据*/
    public static final byte CMD_ERECTION_DATA_COUNTS = (byte) 0x21;
    /* 勃起数据发送完成*/
    public static final byte CMD_GET_ERECTION_DATA_COMPLETED = (byte) 0x22;
    /* 勃起数据*/
    public static final byte CMD_ERECTION_DATA = (byte) 0x23;
    public static final byte CMD_LOST_ERECTION_DATA = (byte) 0x24;
//    public static final byte CMD_ERECTION_DATA_TEST = (byte) 0x25;    //用于debug已废弃

    /**
     *获取设备id
     * */
    public static final byte CMD_SERIAL_NO = (byte) 0x25;

    /* 设备接上底座*/
    public static final byte CMD_FOUNDATION_CONNECTED = (byte) 0x31;
    /* 设备断开底座*/
    public static final byte CMD_FOUNDATION_DISCONNECTED = (byte) 0x32;
    /* 设备和底座的连接状态*/
    public static final byte CMD_FOUNDATION_CONNECTION_STATE = (byte) 0x33;
    /* 获取设备使用时长 */
    public static final byte CMD_DEVICE_UTILITY_TIME = (byte) 0x34;
}
