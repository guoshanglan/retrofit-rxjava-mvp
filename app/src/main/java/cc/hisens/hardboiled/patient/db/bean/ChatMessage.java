package cc.hisens.hardboiled.patient.db.bean;

import org.json.JSONException;
import org.json.JSONObject;

import cc.hisens.hardboiled.patient.Appconfig;
import io.realm.RealmObject;


//聊天消息的bean类,双方进行会话时需要用到的
public class ChatMessage extends RealmObject {

    //from 消息发送方
    private String senderId;

    //to    消息接收方
    private String receiverId;

    //消息类型 0 文本，1图片，2语言
    private int messageType;

    //文本消息内容
    private String textMessage;

    //图片消息，图片url
    private String imagePath;

    //缩略图url
    private String thumbImagePath;

    //图片宽度
    private int imageWidth = 120;

    //图片高度
    private int imageHeight = 160;

    //语音消息，语音消息url
    private String voicePath;

    //语音时长
    private double voiceTime;

    //发送消息时间
    private long timestamp;

    //文件保存地址
    private String filePath;

    //消息是来自好友还是自己发出
    private int messageFrom;

    //消息是否阅读
    private boolean isRead = false;

    public ChatMessage() {

    }

    public ChatMessage(JSONObject jsonObject) {
        try {
            senderId = jsonObject.getString("from");
            receiverId = jsonObject.getString("to");
            messageType = jsonObject.getInt("type");
            messageFrom = Appconfig.CHAT_RECEIVER;
            switch (messageType) {
                case 0:
                    textMessage = jsonObject.getString("text");
                    break;
                case 1:
                    break;
                case 2:
                    voiceTime = jsonObject.getDouble("time");
                    voicePath = jsonObject.getString( "audio");
                    break;
                default:
            }
            timestamp = jsonObject.getLong("time");
        } catch (JSONException e) {

        }

    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getVoicePath() {
        return voicePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getThumbImagePath() {
        return thumbImagePath;
    }

    public void setThumbImagePath(String thumbImagePath) {
        this.thumbImagePath = thumbImagePath;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public double getVoiceTime() {
        return voiceTime;
    }

    public void setVoiceTime(double voiceTime) {
        this.voiceTime = voiceTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getMessageFrom() {
        return messageFrom;
    }

    public void setMessageFrom(int messageFrom) {
        this.messageFrom = messageFrom;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public enum MessageType {
        TEXT, PICTURE, VOICE
    }

}
