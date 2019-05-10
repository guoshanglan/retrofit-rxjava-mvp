package cc.hisens.hardboiled.patient.db;

import java.util.List;

import cc.hisens.hardboiled.patient.db.bean.ChatMessage;
import io.reactivex.Observable;



//聊天的message的操作接口
public interface ChatMsgRepo {

    void saveChatMsg(ChatMessage message);

    void saveChatMsgList(List<ChatMessage> messageList);

    List<ChatMessage> getChatMessageList(String senderId, String receiverId);

    List<ChatMessage> getChatMessageList();

    Observable<Long> getDoctorUnreadMessageCount();

    Observable<Long> getHelperUnreadMessageCount();

    void setDoctorUnreadMessageState(String uid, boolean isRead);

    void setHelperUnreadMessageState(String uid, boolean isRead);
}
