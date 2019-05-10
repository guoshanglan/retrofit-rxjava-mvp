package cc.hisens.hardboiled.patient.db;

import java.util.List;


import cc.hisens.hardboiled.patient.db.bean.Conversation;
import io.reactivex.Observable;


//会话操作接口
public interface ConversationRepo {

    void saveConversation(List<Conversation> conversations);

    void saveConversation(Conversation conversation);

    List<Conversation> getConversation();

    Observable<List<Conversation>> getUnreadConversation();

    void setConversationState(String uid, boolean isRead);

    Observable<Long> getConversationCount();
}
