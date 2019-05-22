package cc.hisens.hardboiled.patient.db;

import java.util.List;


import cc.hisens.hardboiled.patient.db.bean.ChatMessage;
import cc.hisens.hardboiled.patient.db.bean.Conversation;
import io.reactivex.Observable;
import io.realm.RealmList;


//会话操作接口
public interface ConversationRepo {

    void saveConversation(List<Conversation> conversations);

    void saveConversation(Conversation conversation);

    List<Conversation> getConversation();

    Observable<List<Conversation>> getUnreadConversation();

    //更新会话消息的聊天记录，将聊天记录全部存储进去
    void setConversationChatmessage(String uid, RealmList<ChatMessage> messages);

    //更新会话的聊天记录的是否未读的状态
    void setConversationChatmessageState(String uid);

    void setConversationState(String uid, boolean isRead);

    Observable<Long> getConversationCount();
}
