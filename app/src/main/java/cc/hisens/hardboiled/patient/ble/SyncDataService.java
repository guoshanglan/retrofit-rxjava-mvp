package cc.hisens.hardboiled.patient.ble;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;


import cc.hisens.hardboiled.patient.bean.Ed;
import cc.hisens.hardboiled.patient.bean.EdInfo;
import cc.hisens.hardboiled.patient.ble.algorithm.EdAnalyze;
import cc.hisens.hardboiled.patient.ble.algorithm.ErectionDataModel;
import cc.hisens.hardboiled.patient.ble.callbacks.ISyncDataCallback;
import cc.hisens.hardboiled.patient.ble.protocol.CmdCoder;
import cc.hisens.hardboiled.patient.ble.protocol.Protocol;
import cc.hisens.hardboiled.patient.ble.protocol.callbacks.OnBatteryCallback;
import cc.hisens.hardboiled.patient.ble.protocol.callbacks.OnErectionCallback;
import cc.hisens.hardboiled.patient.ble.protocol.callbacks.OnFoundationStateCallback;
import cc.hisens.hardboiled.patient.ble.protocol.callbacks.OnSerialNoCallback;
import cc.hisens.hardboiled.patient.database.impl.EdRepositoryImpl;
import cc.hisens.hardboiled.patient.utils.EdUtils;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class SyncDataService extends Service {

    private static final String TAG = SyncDataService.class.getName();

    private final boolean DEBUG = false;
    long startTime = 1500213562000L;
    long endTime = 1500247990000L;
    private CmdCoder mCmdCoder;
    private boolean mSyncingData;

    private List<ISyncDataCallback> mISyncDataCallbackList;
    private List<ErectionDataModel> mErectionDataModelList = new ArrayList<>();
    private List<ErectionDataModel> mLostErectionDataModelList = new ArrayList<>();

    private List<Integer> mPacketIdList = new ArrayList<>();
    //    private Queue<List> mLostQueue;
    private Timer mTimer;

    private long mStartSleep = -1;
    private long mEndSleep = -1;
    private int mTotalCount = 0;
    private int index = 0;
    private List<Integer> mLostPacketId = new ArrayList<>();
    private final int RETRY_MAX_TIMES = 3;
    private int mRetryTimes = 0;
//    private int mCompletionCount = 0;
//    private int mResendTimes = 1;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SyncDataService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        KLog.i(TAG, "<<--onCreate-->>");
        mCmdCoder = Protocol.getInstance(this).getCmdCoder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        KLog.i(TAG, "<<--onStartCommand-->>");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        KLog.i(TAG, "<<--onBind-->>");
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        KLog.i(TAG, "<<--onUnbind-->>");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        KLog.i(TAG, "<<--onDestroy-->>");
        super.onDestroy();
    }

    public void setISyncDataCallbackList(List<ISyncDataCallback> list) {
        mISyncDataCallbackList = list;
    }

    public void syncData() {

        String threadName = Thread.currentThread().getName();
        long threadId = Thread.currentThread().getId();

        KLog.i(TAG, "thread name:" + threadName +
                " , thread id:" + threadId);

        if (!mSyncingData) {

            mSyncingData = true;
//            startTimer();

            queryBattery();

            querySerialNo();

            getFoundationState();

        }
    }

    private void reset() {

        mErectionDataModelList.clear();
        mLostErectionDataModelList.clear();
        mPacketIdList.clear();
        mLostPacketId.clear();
        mRetryTimes = 0;

//        if (mLostQueue != null) {
//            mLostQueue.clear();
//            mLostQueue = null;
//        }

//        mCompletionCount = 0;
//        mResendTimes = 1;

        mStartSleep = -1;
        mEndSleep = -1;
        mTotalCount = 0;
        index = 0;
    }

    public class MyBinder extends Binder {
        public SyncDataService getService() {
            return SyncDataService.this;
        }
    }

    private void startTimer() {

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                mSyncingData = false;

                if (mISyncDataCallbackList != null) {
                    for (ISyncDataCallback callback : mISyncDataCallbackList) {
                        callback.onSyncFailed();
                    }
                }

            }
        }, 65 * 1000);

    }

    private void stopTimer() {
        Protocol.getInstance(this).clear();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private void queryBattery() {
        mCmdCoder.queryBattery(new OnBatteryCallback() {
            @Override
            public void onBattery(int value) {

                if (mISyncDataCallbackList != null) {
                    for (ISyncDataCallback callback : mISyncDataCallbackList) {
                        callback.onBattery(value);
                    }
                }

            }
        });
    }

    private void querySerialNo() {
        mCmdCoder.querySerialNo(new OnSerialNoCallback() {
            @Override
            public void onSerialNo(String value) {
                if (mISyncDataCallbackList != null) {
                    for (ISyncDataCallback callback : mISyncDataCallbackList) {
                        callback.onSerialNo(processSerialNo(value));
                    }
                }
            }
        });
    }

    private void getFoundationState() {

        mCmdCoder.getFoundationState(new OnFoundationStateCallback() {
            @Override
            public void onConnectionState(boolean isConnected) {

                KLog.i(TAG, "-->> onConnectionState = " + isConnected);

                if (isConnected) {

                    getErectionData();

                } else {

                    KLog.i(TAG, "设备和底座断开");
                    syncDataFailed();

                }
            }

        });

    }

    private void getErectionData() {

        mCmdCoder.getErectionDataCounts(new OnErectionCallback() {
            @Override
            public void onGetErectionDataCount(int count) {

                reset();

                if (Protocol.DEBUG) {

                    mCmdCoder.getErectionData();
                    count = 8;

                } else {

                    if (count > 0) {

                        KLog.i(TAG, "data counts:" + count);
                        mCmdCoder.getErectionData();

                    } else if (count == 0) {
                        KLog.i(TAG, "-->> 无最新数据");
                        syncDataSuccessfully(-1);

                    }

                }
                mTotalCount = count;
            }

            @Override
            public void onErectionData(int packetId, List<ErectionDataModel> list, long sleepTime, long wakeTime) {

                KLog.i(packetId);

                mPacketIdList.add(packetId);
                mErectionDataModelList.addAll(list);

                index++;

                if (mISyncDataCallbackList != null) {

                    for (ISyncDataCallback callback : mISyncDataCallbackList) {
                        callback.onSyncProgressUpdate((index * 100 / Math.max(1, mTotalCount)));
                    }
                }

            }

            @Override
            public void onLostErectionData(int packetId, List<ErectionDataModel> list) {

                insertPacketId(mPacketIdList, packetId);

                mLostErectionDataModelList.addAll(list);

                KLog.i("receiving lost data:" + packetId);
                for (ErectionDataModel model : list) {
                    KLog.i(TAG, model);
                }

            }

            @Override
            public void onGetErectionDataCompleted(boolean isCompleted) {

                KLog.i(TAG, "-->> completed = " + isCompleted);
                KLog.i(TAG, "erection data model size:" + mErectionDataModelList.size());

                resendData();

            }

            @Override
            public void onCleanFlash(boolean isSuccessful) {

                KLog.i(TAG, "-->> onCleanFlash = " + isSuccessful);

                //不用管clean flash是否成功
                mSyncingData = false;

            }

        });
    }

    private void resendData() {

        if (mLostPacketId.size() == 0) {
            mLostPacketId = getLostPacketId();
            ++mRetryTimes;
        }

        if (mLostPacketId.size() == 0 || mRetryTimes > RETRY_MAX_TIMES) {
            afterErectionDataComplete();
            return; //重试次数超过限制
        }

        if (mLostPacketId.size() < 8) {
            mCmdCoder.getLostErectionData(mLostPacketId);
            mLostPacketId.clear();
        } else if (mLostPacketId.size() >= 8) {

            mCmdCoder.getLostErectionData(mLostPacketId.subList(0, 8));
            for (int i = 0; i < 8; i++) {
                mLostPacketId.remove(0);
            }
        }

    }

//    private void sendLostData() {
//
//        mCompletionCount = mCompletionCount + 1;
//        List<Integer> lostPacketId = null;
//
//        if (mCompletionCount == 1) {
//
//            lostPacketId = getLostPacketId();
//
//            KLog.i("get lost data：" + lostPacketId);
//            mLostQueue = createLostPacketQueue(lostPacketId);
//            mResendTimes = mResendTimes + 1;
//
//        }
//
//        if (mLostQueue != null && mLostQueue.size() > 0) {
//
//            mCmdCoder.getLostErectionData(mLostQueue.poll());
//
//        } else if ((lostPacketId != null && lostPacketId.size() == 0) || (mResendTimes > 3)) {
//
//            afterErectionDataComplete();
//
//        } else if (mResendTimes <= 3) {
//
//            mCompletionCount = 0;
//            sendLostData();
//
//        }
//
//
//    }

    private void afterErectionDataComplete() {

        addLostErectionData();

        // 同步成功,清除数据 TODO 测试时暂时去掉
        if (Protocol.CLEAN_FLASH) {

            String threadName = Thread.currentThread().getName();
            long threadId = Thread.currentThread().getId();

            KLog.i(TAG, "thread name:" + threadName +
                    " , thread id:" + threadId);

            mCmdCoder.getErectionDataCompleted();

            saveEdInfos();
            Protocol.getInstance(SyncDataService.this).clear();

        } else {

            mSyncingData = false;
            saveEdInfos();

        }
    }

    @SuppressLint("CheckResult")
    private void saveEdInfos() {


        /**
         * 从底座获取到的勃起数据，每一次的勃起强度和勃起持续时间
         * */
        List<EdInfo> edInfos = EdAnalyze.analyzeEdInfo(mErectionDataModelList);

        if (edInfos != null && edInfos.size() > 0) {
            mStartSleep = edInfos.get(0).getStartTime();
            mEndSleep = edInfos.get(edInfos.size() - 1).getEndTime();
        }

        if (DEBUG) {
            // 测试数据
            mStartSleep = startTime;
            mEndSleep = endTime;
        }

        KLog.i(TAG, "-->> edInfo size:" + edInfos.size());
        for (EdInfo edInfo : edInfos) {
            KLog.i(TAG, "-->> " + edInfo);
        }

        // 分析出现勃起的数据，写入数据库
        if (edInfos.size() > 0 || (edInfos.size() == 0 && EdAnalyze.isNormalNPT(mStartSleep, mEndSleep))) {

            KLog.i(TAG, "-->> startTime = " + mStartSleep);

            new EdRepositoryImpl()
                    .saveEd(EdUtils.getEd(edInfos, mStartSleep, mEndSleep))
                    .subscribe(new Consumer<Ed>() {
                        @Override
                        public void accept(@NonNull Ed ed) {

                            syncDataSuccessfully(ed.getStartTimestamp());

                        }
                    });

        } else {

            KLog.i(TAG, "-->> 算法分析出错" + edInfos.size() + " " + EdAnalyze.isNormalNPT(mStartSleep, mEndSleep));

            // 同步成功
            syncDataSuccessfully(-1);

        }

    }

    private void syncDataFailed() {

        mSyncingData = false;

        // 设备和底座断开
        stopTimer();

        if (mISyncDataCallbackList != null) {
            for (ISyncDataCallback callback : mISyncDataCallbackList) {
                callback.onSyncFailed();
            }
        }

    }

    private void syncDataSuccessfully(long timeStamp) {

        mSyncingData = false;


        stopTimer();
        if (mISyncDataCallbackList != null) {
            for (ISyncDataCallback callback : mISyncDataCallbackList) {
                callback.onSyncSuccessful(timeStamp);
            }
        }

    }

    private String getProcessName(int pid) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List list = manager.getRunningAppProcesses();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) iterator.next();
            if (info.pid == pid) {
                return info.processName;
            }
        }

        return null;
    }

    private List<Integer> getLostPacketId() {

        int startId = 1;
        int endId = mTotalCount;

        List<Integer> lostPacketId = new ArrayList<>();

        for (int i = 0; i < mPacketIdList.size(); i++) {

            int packetId = mPacketIdList.get(i);
            int diff = packetId - startId;
            for (int j = 0; j < diff; j++) {
                lostPacketId.add(startId + j);
            }
            startId = packetId + 1;

        }
        startId = mPacketIdList.get(mPacketIdList.size() - 1);
        int tail = endId - startId;
        for (int i = 0; i < tail; i++) {
            lostPacketId.add(startId + i);
        }

        return lostPacketId;

    }

    private Queue<List> createLostPacketQueue(List<Integer> lost) {

        int totalPackets = lost.size() / 8;
        int tail = lost.size() % 8;

        int queueLength = tail > 0 ? totalPackets + 1 : totalPackets;

        if (queueLength == 0) return null;

        Queue<List> queue = new ArrayBlockingQueue(queueLength);

        for (int i = 1; i <= totalPackets; i++) {
            List sub = lost.subList(8 * (i - 1), 8 * i);
            queue.offer(sub);
        }

        if (tail > 0) {
            List subTail = lost.subList(8 * totalPackets, 8 * totalPackets + tail);
            queue.offer(subTail);
        }

        return queue;
    }


    private void addLostErectionData() {

        if (mLostErectionDataModelList.size() == 0) return;

        for (int i = 0; i < mLostErectionDataModelList.size(); i++) {

            long lost = mLostErectionDataModelList.get(i).getTimestamp();

            for (int j = 0; j < mErectionDataModelList.size() - 1; j++) {

                long first = mErectionDataModelList.get(j).getTimestamp();
                long second = mErectionDataModelList.get(j + 1).getTimestamp();

                if (lost > first && lost < second) {
                    mErectionDataModelList.add(j + 1, mLostErectionDataModelList.get(i));
                    break;
                } else if (lost < first) {
                    mErectionDataModelList.add(j, mLostErectionDataModelList.get(i));
                    break;
                } else if ((j == mErectionDataModelList.size() - 2) && (lost > second)) {
                    mErectionDataModelList.add(j + 2, mLostErectionDataModelList.get(i));
                    break;
                }

            }
        }

    }

    private void insertPacketId(List<Integer> packetIdList, int packetId) {

        for (int i = 0; i < packetIdList.size() - 1; i++) {

            int first = packetIdList.get(i);
            int second = packetIdList.get(i + 1);

            if (packetId < first) {

                packetIdList.add(i, packetId);
                break;

            } else if (packetId > first && packetId < second) {

                packetIdList.add(i + 1, packetId);
                break;

            } else if (packetId > second && (packetIdList.size() - 2 == i)) {

                packetIdList.add(i + 2, packetId);
                break;

            }

        }
    }

    private String processSerialNo(String serialNo) {

        StringBuilder builder = new StringBuilder();
        if (!TextUtils.isEmpty(serialNo)) {
            int length = serialNo.length();
            for (int i = 0; i < length / 2; i++) {
                builder.append(serialNo.substring(2 * i, 2 * (i + 1)));
                if (i != length / 2 - 1) {
                    builder.append(":");
                }
            }

        }
        return builder.toString();
    }

}
