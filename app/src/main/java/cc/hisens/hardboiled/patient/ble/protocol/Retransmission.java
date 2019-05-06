package cc.hisens.hardboiled.patient.ble.protocol;

import android.os.Looper;

import java.util.Timer;
import java.util.TimerTask;


/**
 * @author Ou Weibin
 * @version 1.0
 */
public class Retransmission {
    private static final int ACK_TIMEOUT = 1000;

    private static final int MAX_ACK_COUNT = 3;

    private Protocol mProtocol;
    /* 收到指令确认计数*/
    private int mAckCount = 0;

    private boolean mNeedToResponse = false;
    private byte[] mSent;

    private Timer mAckedTimer = new Timer();
    private AckTimerTask mAckTimerTask;

    public Retransmission(Protocol protocol, byte[] sent, boolean needToResponse) {
        mProtocol = protocol;
        mSent = sent;
        mNeedToResponse = needToResponse;
    }

    /**
     * 启动重发机制
     */
    public void start() {
        mAckTimerTask = new AckTimerTask();
        mAckedTimer.schedule(mAckTimerTask, ACK_TIMEOUT);
    }

    public void resend() {
//        KLog.i("开始重传-->>resend");
        if (mAckCount < MAX_ACK_COUNT) {
            mAckTimerTask = new AckTimerTask();
            mAckedTimer.schedule(mAckTimerTask, ACK_TIMEOUT);
        } else {
            // 重传失败，可能是蓝牙链路信号差
            // 回调失败接口
//            KLog.i("-->>确认超时");
            mProtocol.rcvFailure(this);
        }
    }

    /**
     * 底层上报接收到数据
     *
     * @param rcv
     */
    public void reported(byte[] rcv) {
        if (rcv != null && rcv.length > Protocol.CMD_INDEX) {
            // 收到回复
            if (mNeedToResponse && mSent != null && mSent.length > Protocol.CMD_INDEX) {
                // TODO: 2017/7/10 测试
                if (rcv[Protocol.CMD_INDEX] == mSent[Protocol.CMD_INDEX] /*|| isDebug(rcv)*/) {
                    mAckCount = 0;
                    if (mAckedTimer != null) {
                        if (mAckTimerTask != null) {
                            mAckTimerTask.cancel();
                            mAckTimerTask = null;
                        }
                    }
                    // 返回到上层解析分发
                    mProtocol.rcvResponse(rcv, this);
                }
            }
        }
    }

    /*private boolean isDebug(byte[] rcv) {
        return (Protocol.DEBUG && (rcv[Protocol.CMD_INDEX] == CmdSet.CMD_ERECTION_DATA
                && mSent[Protocol.CMD_INDEX] == CmdSet.CMD_ERECTION_DATA_TEST));
    }*/

    private class AckTimerTask extends TimerTask {
        @Override
        public void run() {
            // 重发指令等待Ack
//            KLog.i("--->> 确认超时  mAckCount = " + mAckCount);
            Looper.prepare();
            mAckCount++;
            mProtocol.resendSinceTimeout(Retransmission.this);
            Looper.loop();
        }
    }
}
