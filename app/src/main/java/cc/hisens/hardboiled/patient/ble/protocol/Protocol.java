package cc.hisens.hardboiled.patient.ble.protocol;

import android.content.Context;

import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.exception.BleException;
import com.socks.library.KLog;

import java.util.LinkedList;

;import cc.hisens.hardboiled.patient.ble.BLEManagerWrapper;
import cc.hisens.hardboiled.patient.ble.callbacks.IBleReadCallback;
import cc.hisens.hardboiled.patient.ble.callbacks.IDeviceUtilityTimeCallback;
import cc.hisens.hardboiled.patient.ble.protocol.callbacks.OnBatteryCallback;
import cc.hisens.hardboiled.patient.ble.protocol.callbacks.OnErectionCallback;
import cc.hisens.hardboiled.patient.ble.protocol.callbacks.OnFoundationStateCallback;
import cc.hisens.hardboiled.patient.ble.protocol.callbacks.OnSerialNoCallback;
import cc.hisens.hardboiled.patient.ble.protocol.callbacks.OnSetTimeCallback;
import cc.hisens.hardboiled.patient.utils.BytesUtils;


/**
 * @author Ou Weibin
 * @version 1.0
 */
public class Protocol implements CmdCoder.OnCmdCodeListener, IBleReadCallback {
    // 查询版本号
    public static final String ACTION_FOUNDATION_CONNECTION_STATE = Protocol.class.getName() + ".action.FOUNDATION_CONNECTION_STATE";
    public static final String EXTRA_IS_CONNECTED = Protocol.class.getName() + ".extra.IS_CONNECTED";
    // 设置时间
    static final int CMD_LEN_INDEX = 1; // 数据包长度下标
    static final int CMD_INDEX = 0; // 命令字下标
    static final int PACKET_HEADER_LEN = 2;
    public static boolean DEBUG = false;
    public static boolean CLEAN_FLASH = true;
    private static Protocol sInstance;

    private BLEManagerWrapper mBluetoothManager;
    private CmdCoder mCoder;
    private CmdDecoder mDecoder;

    private LinkedList<Packet> mUnSentQueue = new LinkedList<>();
    private LinkedList<Packet> mUnAckedQueue = new LinkedList<>();
    /* 判断当前数据包是否接受成功，如果没有接收成功不允许发送下一个包 */
    private boolean mIsRcvSuccess = true;
    private BleWriteCallback mBleCharacterCallback = new BleCharacterCallbackImpl();
    private OnSetTimeCallback mOnSetTimeCallback;
    private OnBatteryCallback mOnBatteryCallback;

    private OnSerialNoCallback mSerialNoCallback;
    private OnErectionCallback mOnErectionCallback;
    private OnFoundationStateCallback mOnFoundationStateCallback;
    private IDeviceUtilityTimeCallback mIDeviceUtilityTimeCallback;

    private Protocol(Context context) {
        mBluetoothManager = BLEManagerWrapper.getInstance();
        mCoder = new CmdCoder(mBluetoothManager, this);
        mDecoder = new CmdDecoder(context, this);
        mBluetoothManager.setIBleReadCallback(this);
    }

    public static Protocol getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Protocol(context);
        }
        return sInstance;
    }

    /**
     * 每次连接成功清除队列
     */
    public void clear() {
        KLog.i("-->> clear protocol cache");
        mIsRcvSuccess = true;
        mUnAckedQueue.clear();
        mUnSentQueue.clear();
    }

    /**
     * 接收来自蓝牙底层的自定义协议数据包
     *
     * @param rcv
     */
    @Override
    public void onRead(byte[] rcv) {
        if (rcv != null && rcv.length > 0) {
            readFromBle(rcv);
        }
    }

    /**
     * 接收编码后的数据包
     *
     * @param data
     */
    @Override
    public void onCoded(byte[] data) {

        String threadName = Thread.currentThread().getName();
        long threadId = Thread.currentThread().getId();

        KLog.i("thread name:" + threadName +
                " , thread id:" + threadId);

        Packet packet = new Packet(data, new Retransmission(this, data, isNeedToResponse(data)));
        synchronized (this) {
            mUnSentQueue.offer(packet);
        }
        // 发送数据
        if (mIsRcvSuccess) {
            pollQueueAndWriteToBle();
        }
    }

    /**
     * 重发数据，由于超时
     *
     * @param retransmission
     */
    public synchronized void resendSinceTimeout(Retransmission retransmission) {
        if (mUnAckedQueue.size() > 0) {
            Packet packet = mUnAckedQueue.peek();
            if (retransmission == packet.getRetransmission()) {
                // 重发数据包
                retransmission.resend();
                pollQueueAndWriteToBle();
            }
        }
    }

    /**
     * 数据经过重发模块，并接收到回复数据模块时被调用
     *
     * @param rcv
     * @param retransmission
     */
    public void rcvResponse(byte[] rcv, Retransmission retransmission) {
        rcvSuccess(rcv, retransmission);
    }

    /**
     * 命令等待超时失败，可能是数据链路信号差导致的
     *
     * @param retransmission
     */
    public synchronized void rcvFailure(Retransmission retransmission) {
        // 从等待确认队列头中删除数据
//        KLog.i("-->> 接收失败");
        // 接收超时
        rcvTimeout(retransmission);
        removeDataPacketFromUnAckedQueue(retransmission);
        // 发送新的数据包
        pollQueueAndWriteToBle();
    }

    public void closeConnect() {
        mBluetoothManager.disconnect();
        mBluetoothManager.disableBluetooth();
    }

    /**
     * 接收数据成功
     *
     * @param rcv
     * @param retransmission
     */
    private void rcvSuccess(byte[] rcv, Retransmission retransmission) {
        removeDataPacketFromUnAckedQueue(retransmission);
        // 发送新的数据包
        pollQueueAndWriteToBle();

        // 解析分发数据
        decodeAndDispatch(rcv);
    }

    public CmdCoder getCmdCoder() {
        return mCoder;
    }

    private void decodeAndDispatch(byte[] rcv) {

        mDecoder.decode(rcv);
    }

    private void rcvTimeout(Retransmission retransmission) {
        if (mUnAckedQueue.size() > 0) {
            Packet packet = mUnAckedQueue.peek();
            if (retransmission == packet.getRetransmission() && packet.getData().length > 0) {
                mDecoder.rcvTimeout(packet.getData()[0]);
            }
        }
    }

    private synchronized void pollQueueAndWriteToBle() {

        String threadName = Thread.currentThread().getName();
        long threadId = Thread.currentThread().getId();

        KLog.i("thread name:" + threadName +
                " , thread id:" + threadId);

        // 如果没有收到上条回复，不发送下一条数据
        if (mUnAckedQueue.size() > 0) {// 需要回复
            // 写入蓝牙
            BytesUtils.printHex("rewrite", mUnAckedQueue.peek().getData());
            mBluetoothManager.write(mUnAckedQueue.peek().getData(), mBleCharacterCallback);
            mIsRcvSuccess = false;
        } else if (mUnSentQueue.size() > 0) {// 发送下一条
            // 从发送队列中取出元素
            Packet sentPacket = mUnSentQueue.poll();

            // 写入蓝牙
            BytesUtils.printHex("write  ", sentPacket.getData());
            mBluetoothManager.write(sentPacket.getData(), mBleCharacterCallback);

            mIsRcvSuccess = false;
            // 插入到未确认队列中
            mUnAckedQueue.offer(sentPacket);
            // 启动重发定时器
            sentPacket.getRetransmission().start();
        }
    }

    private void readFromBle(byte[] rcv) {
        // TODO: 2017/7/10  勃起数据特殊处理
        if (rcv[0] == 0x24) {
            KLog.i(rcv);
        }
        // 勃起数据特殊处理
        if ((rcv[CMD_INDEX] == CmdSet.CMD_ERECTION_DATA) || (rcv[CMD_INDEX] == CmdSet.CMD_LOST_ERECTION_DATA)) {
            // 取出最新发送的数据包
            if (mUnAckedQueue.size() > 0) {
                // 上报到重发块中
                mUnAckedQueue.peek().getRetransmission().reported(rcv);
            } else {
                decodeAndDispatch(rcv);
            }
            return;
        }

        if (isNotifiedByBleDevice(rcv)) {// 直接分发
            decodeAndDispatch(rcv);
        } else {
            // 取出最新发送的数据包
            if (mUnAckedQueue.size() > 0) {
                // 上报到重发块中
                mUnAckedQueue.peek().getRetransmission().reported(rcv);
            }
        }
    }

    private boolean removeDataPacketFromUnAckedQueue(Retransmission retransmission) {
        if (mUnAckedQueue.size() > 0) {
            Packet packet = mUnAckedQueue.peek();
            if (retransmission == packet.getRetransmission()) {
//                KLog.i("-->> 接收成功");
                mIsRcvSuccess = true;
                return mUnAckedQueue.poll() != null;
            }
        }
        return false;
    }

    /**
     * 判断当前发送的命令是否需要接收回复
     *
     * @param data
     * @return
     */
    private boolean isNeedToResponse(byte[] data) {
        switch (data[CMD_INDEX]) {
            case CmdSet.CMD_SET_TIME:
            case CmdSet.CMD_BATTERY:
            case CmdSet.CMD_SERIAL_NO:
            case CmdSet.CMD_ERECTION_DATA_COUNTS:
            case CmdSet.CMD_ERECTION_DATA:
            case CmdSet.CMD_LOST_ERECTION_DATA:
            case CmdSet.CMD_GET_ERECTION_DATA_COMPLETED:
            case CmdSet.CMD_FOUNDATION_CONNECTION_STATE:
            case CmdSet.CMD_DEVICE_UTILITY_TIME:
                return true;
            default:
                return false;
        }
    }

    /**
     * 判断当前接收的命令包是不是ble设备主动推送的
     *
     * @param rcv
     * @return
     */
    private boolean isNotifiedByBleDevice(byte[] rcv) {
        switch (rcv[CMD_INDEX]) {
            case CmdSet.CMD_FOUNDATION_CONNECTED:
            case CmdSet.CMD_FOUNDATION_DISCONNECTED:
                return true;
            case CmdSet.CMD_GET_ERECTION_DATA_COMPLETED:
                return rcv[2] == 0;
            default:
                return false;
        }
    }

    OnSetTimeCallback getOnSetTimeCallback() {
        return mOnSetTimeCallback;
    }

    void setOnSetTimeCallback(OnSetTimeCallback mOnSetTimeCallback) {
        this.mOnSetTimeCallback = mOnSetTimeCallback;
    }

    OnBatteryCallback getOnBatteryCallback() {
        return mOnBatteryCallback;
    }

    void setOnBatteryCallback(OnBatteryCallback callback) {
        mOnBatteryCallback = callback;
    }

    OnErectionCallback getOnErectionCallback() {
        return mOnErectionCallback;
    }

    void setOnErectionCallback(OnErectionCallback callback) {
        mOnErectionCallback = callback;
    }

    OnFoundationStateCallback getOnFoundationStateCallback() {
        return mOnFoundationStateCallback;
    }

    void setOnFoundationStateCallback(OnFoundationStateCallback callback) {
        mOnFoundationStateCallback = callback;
    }

    IDeviceUtilityTimeCallback getOnDeviceUtilityTimeCallback() {
        return mIDeviceUtilityTimeCallback;
    }

    void setOnDeviceUtilityTimeCallback(IDeviceUtilityTimeCallback callback) {
        this.mIDeviceUtilityTimeCallback = callback;
    }

    public OnSerialNoCallback getSerialNoCallback() {
        return mSerialNoCallback;
    }

    public void setSerialNoCallback(OnSerialNoCallback mSerialNoCallback) {
        this.mSerialNoCallback = mSerialNoCallback;
    }

    private class BleCharacterCallbackImpl extends BleWriteCallback {

        @Override
        public void onWriteSuccess(int current, int total, byte[] justWrite) {
            KLog.i("onWriteSuccess");
        }

        @Override
        public void onWriteFailure(BleException exception) {
            KLog.i("onWriteFailure");
        }

    }


}
