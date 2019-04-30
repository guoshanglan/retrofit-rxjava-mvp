package cc.hisens.hardboiled.patient;

public class Appconfig {

    public static final String PREF_FILE_NAME = "public_preference";
    public static final String PREF_INITIAL_SETUP_FINISHED = "PREF_INITIAL_SETUP_FINISHED";
    public static final String PREF_INTRODUCTION_FINISHED = "PREF_INTRODUCTION_FINISHED";
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
    //    public static final String CHAT_SERVER_URL = "http://10.0.1.222:201 00";
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
