package cc.hisens.hardboiled.patient.db.bean;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


//会话双方的bean类,展示在聊天列表的

public class Conversation extends RealmObject {

    //当前会话者的id
    @PrimaryKey
    private String friendId;

    //当前会话者名字
    private String friendName;

    //当前会话者头像url
    private String imageUrl;

    //当前会话者头像缩略图
    private String thumbUrl;

    //与会话者最后一条消息
    private ChatMessage lastMessage;

    //最后一条消息时间
    private double lastMessageTime;

    //是否阅读
    private boolean isRead;

    //当前会话的所有消息
    public RealmList<ChatMessage> messages = new RealmList<>();

    public Conversation() {

    }

    public Conversation(JSONObject jsonObject) {
        try {
            friendId = jsonObject.getString("from");
            lastMessage = new ChatMessage(jsonObject);
            lastMessageTime = lastMessage.getTimestamp();
            messages.add(lastMessage);
            isRead = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public ChatMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(ChatMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public double getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(double lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public RealmList<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(RealmList<ChatMessage> messages) {
        this.messages = messages;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

}
