package cc.hisens.hardboiled.patient.db.bean;

import org.json.JSONException;
import org.json.JSONObject;

import cc.hisens.hardboiled.patient.Appconfig;
import io.realm.RealmObject;


//聊天消息的bean类,双方进行会话时需要用到的，不需要主键，因为所有的会话都需要存储
public class ChatMessage extends RealmObject {

    //from 消息发送方
    private String senderId;
    //to    消息接收方
    private String receiverId;

    //文本消息内容
    private String textMessage;

    //缩略图文件的本地路径
    private String thumbPath;
    //缩略图远程地址
    private String thumbUrl;
    //是否压缩(false:原图，true：压缩过)
    private boolean compress;
    //高度
    private int imageheight;
    //宽度
    private int iamgewidth;

    //视频消息长度
    private long duration;


    //语音本地文件保存路径
    private String voicefilePath;

    //视频高度
    private int videoheight;
    //视频宽度
    private int videowidth;

    //语音时长
    private double voiceTime;

    //发送消息时间
    private long timestamp;


    //消息是来自好友还是自己发出
    private int messageFrom;

    //消息是否阅读
    private boolean isRead = false;




    public ChatMessage() {

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

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public boolean isCompress() {
        return compress;
    }

    public void setCompress(boolean compress) {
        this.compress = compress;
    }

    public int getImageheight() {
        return imageheight;
    }

    public void setImageheight(int imageheight) {
        this.imageheight = imageheight;
    }

    public int getIamgewidth() {
        return iamgewidth;
    }

    public void setIamgewidth(int iamgewidth) {
        this.iamgewidth = iamgewidth;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getVideoheight() {
        return videoheight;
    }

    public void setVideoheight(int videoheight) {
        this.videoheight = videoheight;
    }

    public int getVideowidth() {
        return videowidth;
    }

    public void setVideowidth(int videowidth) {
        this.videowidth = videowidth;
    }

    public String getVoicefilePath() {
        return voicefilePath;
    }

    public void setVoicefilePath(String localfilePath) {
        this.voicefilePath = localfilePath;
    }

    public double getVoiceTime() {
        return voiceTime;
    }

    public void setVoiceTime(double voiceTime) {
        this.voiceTime = voiceTime;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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
        TEXT ,//文本消息
        AUDIO ,//语音消息
        VIDEO ,//视频消息
        IMAGE ,//图片消息
        FILE ,//文件消息
        LOCATION //位置消息
    }


    //消息发送状态
    public enum MsgSendStatus {
        DEFAULT,
        //发送中
        SENDING,
        //发送失败
        FAILED,
        //已发送
        SENT
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", textMessage='" + textMessage + '\'' +
                ", thumbPath='" + thumbPath + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                ", compress=" + compress +
                ", imageheight=" + imageheight +
                ", iamgewidth=" + iamgewidth +
                ", duration=" + duration +
                ", videoheight=" + videoheight +
                ", videowidth=" + videowidth +
                ", voicefilePath='" + voicefilePath + '\'' +
                ", voiceTime=" + voiceTime +
                ", timestamp=" + timestamp +
                ", messageFrom=" + messageFrom +
                ", isRead=" + isRead +
                '}';
    }
}
