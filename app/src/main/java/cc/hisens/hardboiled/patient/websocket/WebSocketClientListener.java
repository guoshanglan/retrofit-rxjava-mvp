package cc.hisens.hardboiled.patient.websocket;


//监听消息接受的一个回调类
public interface WebSocketClientListener {

    void onConnected();

    void onDisconnected();

    void onMessage(MessageModel json);
}
