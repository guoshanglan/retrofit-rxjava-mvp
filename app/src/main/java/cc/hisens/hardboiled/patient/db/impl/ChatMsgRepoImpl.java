package cc.hisens.hardboiled.patient.db.impl;

import java.util.ArrayList;
import java.util.List;


import cc.hisens.hardboiled.patient.Appconfig;
import cc.hisens.hardboiled.patient.db.ChatMsgRepo;
import cc.hisens.hardboiled.patient.db.RealmHelper;
import cc.hisens.hardboiled.patient.db.bean.ChatMessage;
import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmResults;


//聊天信息数据库的操作，消息列表
public class ChatMsgRepoImpl implements ChatMsgRepo {



    //存储聊天信息
    @Override
    public void saveChatMsg(ChatMessage message) {
        Realm realm = RealmHelper.getRealm();
        realm.beginTransaction();
        realm.insert(message);
        realm.commitTransaction();
        realm.close();
    }

    //存储多条聊天信息
    @Override
    public void saveChatMsgList(List<ChatMessage> messageList) {
        Realm realm = RealmHelper.getRealm();
        realm.beginTransaction();
        realm.insert(messageList);
        realm.commitTransaction();
        realm.close();
    }


    //获取多条聊天信息
    @Override
    public List<ChatMessage> getChatMessageList(String senderId, String receiverId) {
        Realm realm = RealmHelper.getRealm();
        realm.beginTransaction();
        List<ChatMessage> list = realm.copyFromRealm(realm.where(ChatMessage.class)
                .findAll());
        realm.commitTransaction();
        realm.close();
        return list;
    }


    //获取聊天聊天信息列表
    @Override
    public List<ChatMessage> getChatMessageList() {
        Realm realm = RealmHelper.getRealm();
        final List<ChatMessage> list = new ArrayList<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<ChatMessage> messages = realm.where(ChatMessage.class)
                        .findAll();
                for (ChatMessage message : messages) {
                    message.setRead(true);
                }

                list.addAll(realm.copyFromRealm(messages));
            }
        });
        realm.close();
        return list;
    }


    //获取医生未读信息的个数
    @Override
    public Observable<Long> getDoctorUnreadMessageCount() {
        Realm realm = RealmHelper.getRealm();
        realm.beginTransaction();
        Long count = realm.where(ChatMessage.class)
                .notEqualTo("senderId", Appconfig.KEY_CUSTOM_SERVICE_ID)
                .equalTo("isRead", false).count();
        realm.commitTransaction();
        realm.close();
        return Observable.just(count);
    }

    //获取客服未读信息的个数
    @Override
    public Observable<Long> getHelperUnreadMessageCount() {
        Realm realm = RealmHelper.getRealm();
        realm.beginTransaction();
        Long count = realm.where(ChatMessage.class)
                .equalTo("senderId", Appconfig.KEY_CUSTOM_SERVICE_ID)
                .equalTo("isRead", false).count();
        realm.commitTransaction();
        realm.close();
        return Observable.just(count);
    }


    //改变医生未读消息的状态
    @Override
    public void setDoctorUnreadMessageState(final String uid, final boolean isRead) {
        Realm realm = RealmHelper.getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<ChatMessage> messages = realm.where(ChatMessage.class)
                        .notEqualTo("senderId", uid)
                        .findAll();
                for (ChatMessage message : messages) {
                    message.setRead(isRead);
                }
            }
        });
        realm.close();
    }


    //改变客服可读消息的状态
    @Override
    public void setHelperUnreadMessageState(final String uid, final boolean isRead) {
        Realm realm = RealmHelper.getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<ChatMessage> messages = realm.where(ChatMessage.class)
                        .equalTo("senderId", uid)
                        .findAll();
                for (ChatMessage message : messages) {
                    message.setRead(isRead);
                }
            }
        });
        realm.close();
    }

}
