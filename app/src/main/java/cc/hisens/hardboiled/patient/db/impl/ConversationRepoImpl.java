package cc.hisens.hardboiled.patient.db.impl;

import java.util.List;


import cc.hisens.hardboiled.patient.Appconfig;
import cc.hisens.hardboiled.patient.db.ConversationRepo;
import cc.hisens.hardboiled.patient.db.RealmHelper;
import cc.hisens.hardboiled.patient.db.bean.ChatMessage;
import cc.hisens.hardboiled.patient.db.bean.Conversation;
import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmList;


//有消息过来的时候会用到的
public class ConversationRepoImpl implements ConversationRepo {


    //存储多条会话消息，有就更新，没有就新建对象
    @Override
    public void saveConversation(final List<Conversation> conversions) {
        Realm realm = RealmHelper.getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(conversions);
            }
        });
        realm.close();
    }

    //存储会话消息，有就更新，没有就新建，会根据主键来进行相应的操作
    @Override
    public void saveConversation(final Conversation conversation) {
        Realm realm = RealmHelper.getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(conversation);
            }
        });
        realm.close();
    }


    //获取多条会话消息
    @Override
    public List<Conversation> getConversation() {

        Realm realm = RealmHelper.getRealm();
        List<Conversation> list = realm.copyFromRealm(realm.where(Conversation.class)
                .notEqualTo("friendId", Appconfig.KEY_CUSTOM_SERVICE_ID).findAll());  //查询会话消息，friendId不等于客服id的所有会话消息
        realm.close();
        return list;
    }


    //获取未读的会话消息
    @Override
    public Observable<List<Conversation>> getUnreadConversation() {
        Realm realm = RealmHelper.getRealm();
        List<Conversation> list = realm.copyFromRealm(
                realm.where(Conversation.class).equalTo("isRead", false).findAll());
        realm.close();
        return Observable.just(list);
    }



    //更新会话的聊天记录,将聊天记录全部存储进去
    @Override
    public void setConversationChatmessage(String uid,final RealmList<ChatMessage> messages) {
        Realm realm = RealmHelper.getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Conversation conversation = realm.where(Conversation.class).equalTo("friendId", uid).findFirst();

                conversation.messages.addAll(messages);
                conversation.setMessages(conversation.messages);
            }
        });
        realm.close();
    }



    //更新会话的聊天记录消息状态，将未读改为已读
    @Override
    public void setConversationChatmessageState(String uid) {
        Realm realm = RealmHelper.getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Conversation conversation = realm.where(Conversation.class).equalTo("friendId", uid).findFirst();

                 for (int i=0;i<conversation.messages.size();i++){
                     conversation.messages.get(i).setRead(true);
                 }

            }
        });
        realm.close();
    }



    //更新会话消息的聊天状态
    @Override
    public void setConversationState(final String uid, final boolean isRead) {
        Realm realm = RealmHelper.getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Conversation conversation = realm.where(Conversation.class).equalTo("friendId", uid).findFirst();
                conversation.setRead(isRead);
            }
        });
        realm.close();
    }


    //获取会话消息的个数
    @Override
    public Observable<Long> getConversationCount() {
        Realm realm = RealmHelper.getRealm();
        final Long[] counts = new Long[1];
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                counts[0] = realm.where(Conversation.class).count();
            }
        });
        realm.close();
        return Observable.just(counts[0]);
    }


}
