package cc.hisens.hardboiled.patient.utils;



import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import cc.hisens.hardboiled.patient.bean.Ed;
import cc.hisens.hardboiled.patient.bean.EdInfo;
import cc.hisens.hardboiled.patient.ble.algorithm.EdAnalyze;
import cc.hisens.hardboiled.patient.ble.algorithm.ErectionDataModel;
import cc.hisens.hardboiled.patient.database.EdRepository;
import cc.hisens.hardboiled.patient.database.impl.EdRepositoryImpl;
import io.realm.RealmList;

import static cc.hisens.hardboiled.patient.utils.BytesUtils.bytesToInt;


/**
 * @author Waiban
 * @package cc.hisens.hardboiled.utils
 * @fileName EdUtils
 * @date on 2017/6/9 10:36
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 */

public class EdUtils {

    public static Ed getEd(List<EdInfo> edInfoList, long startTimestamp, long endTimestamp) {
        Ed ed = new Ed();
        int maxStrength = 0;
        long maxDuration = 0;
        HashMap<Integer, Long> edMap = new HashMap<>();
        if (startTimestamp > 0 && endTimestamp > 0) {
            ed.setStartTimestamp(startTimestamp);
            ed.setEndTimestamp(endTimestamp);
        }
        for (EdInfo info : edInfoList) {
            edMap.put(info.getErectileStrength(), info.getDuration());
            maxStrength = Math.max(maxStrength, info.getErectileStrength());
        }
        maxDuration = edMap.get(maxStrength);
        ed.setMaxStrength(maxStrength);
        ed.setMaxDuration(maxDuration);
        ed.setEdInfos(edInfoList);
        return ed;
    }


    /**
     * 添加测试数据
     */

    public static void addTestData(boolean add) {

        if (!add) return;

        // 添加测试数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                initRealm();
            }
        }).start();
        testErectionStrengthAlgorithm();
        test();
    }

    private static void initRealm() {

        long startTime = 1524320660000L;
        long endTime = 1524350660000L;

        for (int j = 0; j < 25; j++) {
            Ed ed = new Ed();

            ed.setStartTimestamp(startTime);
            ed.setEndTimestamp(endTime + DateUtils.dayDurationMillis() * j);
            RealmList<EdInfo> edInfoList = new RealmList<>();
            long startTimeTmp = startTime;
            for (int i = 0; i < 6; i++) {
                long offset = ((long) (Math.random() * 3600 * 1000L));
                //            EdInfo edInfo = realm.createObject(EdInfo.class);
                EdInfo edInfo = new EdInfo();
                edInfo.setStartTime(startTimeTmp + offset);
                edInfo.setEndTime(edInfo.getStartTime() + (long) (Math.random() * 30 * 60 * 1000L));
                edInfo.setDuration(edInfo.getEndTime() - edInfo.getStartTime());
                edInfo.setErectileStrength((int) (Math.random() * 100) + 20);
                edInfoList.add(edInfo);
                startTimeTmp += offset;
            }
            ed.setEdInfos(edInfoList);


            int maxStrength = 0;
            long maxDuration = 0;
            HashMap<Integer, Long> edMap = new HashMap<>();
            for (EdInfo edInfo : edInfoList) {
                edMap.put(edInfo.getErectileStrength(), edInfo.getDuration());
                maxStrength = Math.max(maxStrength, edInfo.getErectileStrength());
            }

            maxDuration = edMap.get(maxStrength);
            ed.setMaxStrength(maxStrength);
            ed.setMaxDuration(maxDuration);
            ed.setEdInfos(edInfoList);

            if (j < 10) {
                ed.setInterferenceFactor("西地那非");
            }


            EdRepository edRepository = new EdRepositoryImpl();
            edRepository.saveEd(ed);

            startTime += DateUtils.dayDurationMillis();
        }
    }

    private static void testErectionStrengthAlgorithm() {
        int[] erectionStrengths = new int[]{
                100, 101, 100, 101, 109, 107, 110, 128, 119, 130, 190, 189, 188, 190, 170, 140, 141, 120, 119,
                110, 108, 102, 100, 101, 100, 101, 109, 107, 110, 128, 119, 130, 140, 141, 120, 119,
                110, 108, 102, 100, 101, 100, 101
        };
        long startTime = 1524300660000L;
        List<ErectionDataModel> list = new ArrayList<>();
        long offset = 0;
        for (int i = 0; i < erectionStrengths.length; i++) {
            offset += (Math.random() * 20000) + 20000;
            ErectionDataModel erectionDataModel = new ErectionDataModel(erectionStrengths[i], startTime + offset);
            list.add(erectionDataModel);
        }

        List<EdInfo> edInfos = EdAnalyze.analyzeEdInfo(list);
        for (EdInfo edInfo : edInfos) {
            KLog.i("-->> " + edInfo);
        }
    }

    private static void test() {

        byte[] payload = new byte[]{0x3b, 0x26, (byte) 0x99, 0x57, 0x0a, 0x23, 0x0b, 0x05, 0x00, 0x0a,
                0x31, 0x0b, 0x0a, 0x00, 0x0a, (byte) 0x80, (byte) 0xb5, 0x1d};

        List<ErectionDataModel> list = new ArrayList<>();
        int offset = 0;
        int firstTimestamp = 0;
        byte[] bytes = new byte[]{(byte) 0xA9};
        KLog.i("数值=" + (bytes[0] << 4) + 0x01);

        if (payload.length > 7) {
            int timestamp = bytesToInt(Arrays.copyOfRange(payload, offset, offset + 4));
            offset += 4;


            byte[] bytes1 = new byte[2];
            bytes1[0] = payload[offset];
            offset += 1;
            byte dataFlipt = payload[offset];
            bytes1[1] = (byte) (dataFlipt >> 4);
            offset += 1;

            byte[] bytes2 = new byte[2];
            bytes2[0] = (byte) (dataFlipt & 0x0F);
            bytes2[1] = payload[offset];
            offset += 1;

            int data1 = bytesToInt(bytes1);
            int data2 = bytesToInt(bytes2);
            KLog.i("数据1=" + data1 + "---");
            KLog.i("数据2=" + data2 + "---");
            list.add(new ErectionDataModel(data1, TimeUtils.toMillis(timestamp)));
            firstTimestamp = timestamp;
        }
        int count = (payload.length - offset) / 5;
        for (int i = 0; i < count; i++) {
            int from = offset + i * 5;
            int timestamp = bytesToInt(Arrays.copyOfRange(payload, from, from + 2)) + firstTimestamp;
            from += 2;
            byte[] bytes1 = new byte[2];
            bytes1[0] = payload[from];
            from += 1;
            byte dataFlipt = payload[from];
            bytes1[1] = (byte) (dataFlipt >> 4);
            from += 1;

            byte[] bytes2 = new byte[2];
            bytes2[0] = (byte) (dataFlipt & 0x0F);
            bytes2[1] = payload[from];

            int data1 = bytesToInt(bytes1);
            int data2 = bytesToInt(bytes2);
            list.add(new ErectionDataModel(data1, TimeUtils.toMillis(timestamp)));
            KLog.i("第" + i + "组数据1=" + data1 + "---");
            KLog.i("第" + i + "组数据2=" + data2 + "---");
        }
    }

}
