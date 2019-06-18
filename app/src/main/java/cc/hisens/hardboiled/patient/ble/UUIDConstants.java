package cc.hisens.hardboiled.patient.ble;

import java.util.Locale;
import java.util.UUID;

/**
 * @author Ou Shanglan
 * @version 1.0
 */
public class UUIDConstants {
    private final static String MY_PREFIX = "F000";
    private final static String MY_SUFFIX = "-0451-4000-0000-00000000B000";
    public final static UUID CHAR_WRITE = concat(MY_PREFIX, "FFF1", MY_SUFFIX);  //写入蓝牙设备的UUID
    public final static UUID CHAR_NOTIFY = concat(MY_PREFIX, "FFF4", MY_SUFFIX);//通知的UUID
    private final static String COMMON_PREFIX = "0000";
    private final static String COMMON_SUFFIX = "-0000-1000-8000-00805F9B34FB";
    // 自定义服务和特征的UUID
    public final static UUID SERVICE = concat(COMMON_PREFIX, "B000", COMMON_SUFFIX);
    private final static String DFU_PREFIX = "0000";
    private final static String DFU_SUFFIX = "-1212-efde-1523-785feabcd123";


    // 设备信息
//    public final static UUID SERVICE_DEVICE_INFO = concat(COMMON_PREFIX, "180A", COMMON_SUFFIX);
//    public final static UUID CHAR_MODEL_NUMBER = concat(COMMON_PREFIX, "2A24", COMMON_SUFFIX);
//    public final static UUID CHAR_SERIAL_NUMBER = concat(COMMON_PREFIX, "2A25", COMMON_SUFFIX);
//    public final static UUID CHAR_FIRMWARE_REVISION = concat(COMMON_PREFIX, "2A26", COMMON_SUFFIX);
//    public final static UUID CHAR_HW_REVEISION = concat(COMMON_PREFIX, "2A27", COMMON_SUFFIX);
//    public final static UUID CHAR_SOFTWARE_REVISION = concat(COMMON_PREFIX, "2A28", COMMON_SUFFIX);
//    public final static UUID CHAR_MANUFACTURER_NAME = concat(COMMON_PREFIX, "2A29", COMMON_SUFFIX);

    private static UUID concat(String prefix, String serialNum, String suffix) {
        return UUID.fromString(String.format(Locale.getDefault(), "%s%s%s", prefix, serialNum, suffix));
    }
}
