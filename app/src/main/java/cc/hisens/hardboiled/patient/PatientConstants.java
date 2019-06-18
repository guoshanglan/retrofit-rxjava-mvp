package cc.hisens.hardboiled.patient;



public class PatientConstants  {

//        public static final String DEVICE_NAME = "HIENSOR807310006";
    public static final String DEVICE_NAME = "HIENSOR";   //蓝牙名称
    public static final int CONNECTION_TIMEOUT = 30000;
    public static final String KEY_SEARCHED_DOCTOR = "KEY_SEARCHED_DOCTOR";
    public static final String KEY_SEARCHED_DOCTOR_LIST = "KEY_SEARCHED_DOCTOR_LIST";

    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_HEALTH_RECORD = "KEY_HEALTH_RECORD";

    public static final String KEY_SCORE = "KEY_SCORE";
    public static final String KEY_DISEASE_NAMES = "KEY_DISEASE_NAMES";
    public static final String KEY_MEDICINE_NAMES = "KEY_MEDICINE_NAMES";
    public static final String KEY_OPERATION_NAMES = "KEY_OPERATION_NAMES";
    public static final String KEY_IS_SLEEP = "KEY_IS_SLEEP";

    public static final String KEY_INITIAL_SETUP = "KEY_INITIAL_SETUP";
    //broadcast action
    public static final String ACTION_GROUP_CHANGED = "action_group_changed";
    public static final String ACTION_NEW_INVITE_MESSAGE = "action_invite_message";
    public static final String ACTION_NEW_MESSAGE = "action_new_message";
    //preference
    public static final String USER_ROLE = "user_role";
    public static final String ROLE_DOCTOR = "doctor";
    public static final String ROLE_PATIENT = "patient";
    //chat
    public static final String CHAT_SERVER_URL = "https://www.hisens.cc";
    //public static final String CHAT_SERVER_URL = "http://10.0.1.222:201 00";
    public static final int CHAT_SENDER = 1002;
    public static final int CHAT_RECEIVER = 1003;
    public static final String EXTRA_USER_ID = "userId";
    public static final String EXTRA_USER_NAME = "userName";
    //key
    public static final String KEY_CHAT_USER_NAME = "CHAT_USER_NAME";
    public static final String KEY_CHAT_USER_ID = "CHAT_USER_ID";
    public enum HistoricDisplayType {
        LATEST, NPT, SPECIFIC
    }

    public static final String COMMA = ",";

    public static final String KEY_CUSTOM_SERVICE_ID = "customservice01";


}
