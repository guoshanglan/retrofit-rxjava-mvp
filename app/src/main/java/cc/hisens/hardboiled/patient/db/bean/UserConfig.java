package cc.hisens.hardboiled.patient.db.bean;

import android.content.Context;

import cc.hisens.hardboiled.patient.Appconfig;
import cc.hisens.hardboiled.patient.utils.SharedUtils;


/**
 * @author Waiban
 * @package cc.hisens.hardboiled.patient.global
 * @fileName UserConfig
 * @date on 2017/8/9 11:08
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 *
 * 用户标记类
 */

public class UserConfig {

    private  static SharedUtils sharedUtils;

    public static void init(Context context) {

        sharedUtils= new SharedUtils(context);
    }
    public UserConfig(Context context){
        sharedUtils= new SharedUtils(context);
    }



    // TODO: 2018/6/6 remove it


    public static class UserInfo {

        private static final String TAG = UserInfo.class.getName();
        public static String EXTRA_IS_LOGIN = TAG + ".extra.IS_LOGIN";
        private static String EXTRA_AVATAR_URL = TAG + ".extra.AVATAR_URL";
        private static String EXTRA_EMUID = TAG + ".extra.EMUID";
        public static String EXTRA_UID = TAG + ".extra.UID";
        private static String EXTRA_NAME = TAG + ".extra.NICKNAME";
        private static String EXTRA_PHONE = TAG + ".extra.PHONE";

        public static boolean isLogin() {
            return sharedUtils.readBoolean(EXTRA_IS_LOGIN, false);
        }

        public static void setLogin(boolean isLogin) {
            sharedUtils.writeBoolean(EXTRA_IS_LOGIN, isLogin);
        }

        public static String getAvatarUrl() {
            return sharedUtils.readString(EXTRA_AVATAR_URL);
        }

        public static void setAvatarUrl(String avatarUrl) {
            sharedUtils.writeString(EXTRA_AVATAR_URL, avatarUrl);
        }

        public static String getEmuid() {
            return sharedUtils.readString(EXTRA_EMUID);
        }

        public static void setEmuid(String emuid) {
            sharedUtils.writeString(EXTRA_EMUID, emuid);
        }

        public static String getUid() {
            return sharedUtils.readString(EXTRA_UID);
        }

        public static void setUid(String uid) {
            sharedUtils.writeString(EXTRA_UID, uid);
        }

        public static String getName() {
            return sharedUtils.readString(EXTRA_NAME);
        }

        public static void setName(String nickname) {
            sharedUtils.writeString(EXTRA_NAME, nickname);
        }

        public static String getPhone() {
            return sharedUtils.readString(EXTRA_PHONE);
        }

        public static void setPhone(String phone) {
            sharedUtils.writeString(EXTRA_NAME, phone);
        }

        public static boolean isDoctor() {
            return sharedUtils.readString(Appconfig.USER_ROLE).equals(Appconfig.ROLE_DOCTOR);
        }

        public static boolean isPatient() {
            return Appconfig.ROLE_PATIENT.equals(sharedUtils.readString(Appconfig.USER_ROLE));
        }
    }

    public static class NotificationSetting {
        private static final String TAG = NotificationSetting.class.getName();
        private static String EXTRA_MSG_NOTIFICATION = "extra.MSG_NOTIFICATION";
        private static String EXTRA_SOUND = "extra.SOUND";
        private static String EXTRA_VIBRATION = "extra.VIBRATION";

        public static boolean getMsgNotification() {
            return sharedUtils.readBoolean(EXTRA_MSG_NOTIFICATION);
        }

        public static void setMsgNotification(boolean flag) {
            sharedUtils.writeBoolean(EXTRA_MSG_NOTIFICATION, flag);
        }

        public static boolean getSound() {
            return sharedUtils.readBoolean(EXTRA_SOUND);
        }

        public static void setSound(boolean flag) {
            sharedUtils.writeBoolean(EXTRA_SOUND, flag);
        }

        public static boolean getVibration() {
            return sharedUtils.readBoolean(EXTRA_VIBRATION);
        }

        public static void setVibration(boolean flag) {
            sharedUtils.writeBoolean(EXTRA_VIBRATION, flag);
        }
    }
}
