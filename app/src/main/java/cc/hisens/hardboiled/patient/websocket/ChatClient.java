package cc.hisens.hardboiled.patient.websocket;

import android.util.Log;

import com.google.gson.Gson;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.PingFrame;
import org.java_websocket.framing.PongFrame;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cc.hisens.hardboiled.patient.db.bean.UserConfig;

public class ChatClient implements WebSocketClientListener {

    private static URI mURI;   //websocket需要的url地址
    private static ChatClient mChatClient;   //客户端类
    public WebSocketClientImpl webSocketClientImpl;
    private Timer connectTimer;   //连接定时器
    private TimerTask connectTimerTask;
    private Timer pingTimer;  //心跳定时器
    private TimerTask pingTimerTask;
    private boolean isConnected;  //判断是否连接
    private boolean isConnectTimerScheduled;
    private boolean isPingTimerScheduled;
    private List<WebSocketClientListener> mClientListenerList = new ArrayList<>();

    private ChatClient(URI uri) {
        mURI = uri;
    }    //连接地址，url

    public static ChatClient getInstance(URI uri) {     //初始化对象
        if (mChatClient == null) {
            mChatClient = new ChatClient(uri);
        }
        return mChatClient;
    }

    public void connect() {
        startConnectTask();
    }


    //发送消息,这个发送消息需要用户id，和你需要发送的人的id
    public void sendMessage(MessageModel message) {
        if (message == null) {
            throw new NullPointerException("Message is Empty");
        }
        if(isConnected&&webSocketClientImpl!=null) {

            webSocketClientImpl.send(message.toString().getBytes());
        }
    }

    @Override
    public void onConnected() {

        Log.e("连接成功","sosososososo");
        MessageModel message = new MessageModel();
        message.setFrom(Long.parseLong(UserConfig.UserInfo.getUid()));   //由登录用户发起长连接
        message.setType((byte) 6);
//        message.setTime(System.currentTimeMillis() / 1000);

        sendMessage(message);   //向服务器发送一条消息

        cancelConnectTimer();  //取消重复连接定时器

        startPingTask();    //开始发送心跳包，保证长连接状态
    }


    //连接断开回调，如果断开进行重新连接，并且停止心跳测试

    @Override
    public void onDisconnected() {
        startConnectTask();

        cancelPingTimer();
    }

    @Override
    public void onMessage(MessageModel message) {


    }

    public class WebSocketClientImpl extends WebSocketClient {

        public WebSocketClientImpl(URI serverUri) {
            super(serverUri);
        }

        public WebSocketClientImpl(URI serverUri, Draft protocolDraft) {
            super(serverUri, protocolDraft);
        }

        public WebSocketClientImpl(URI serverUri, Map<String, String> httpHeaders) {
            super(serverUri, httpHeaders);
        }

        public WebSocketClientImpl(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders) {
            super(serverUri, protocolDraft, httpHeaders);
        }

        public WebSocketClientImpl(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
            super(serverUri, protocolDraft, httpHeaders, connectTimeout);
        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            Log.e("WebSocketClientImpl", "onOpen");
            ChatClient.this.setConnected(true);
            ChatClient.this.onConnected();
        }

        @Override
        public void onMessage(String message) {
            Log.e("WebSocketClientImpl", "onMessage");
        }


        //websocket监听到的服务器返回的消息
        @Override
        public void onMessage(ByteBuffer bytes) {
            super.onMessage(bytes);
            String json = new String(bytes.array());
            Log.e("收到的消息", "onMessage: " + json);
            Gson gson = new Gson();
            MessageModel message = gson.fromJson(json, MessageModel.class);

            for (WebSocketClientListener listener : mClientListenerList) {
                listener.onMessage(message);
            }

        }

        @Override
        public void onWebsocketMessageFragment(WebSocket conn, Framedata frame) {
            super.onWebsocketMessageFragment(conn, frame);
        }

        @Override
        public void onWebsocketPing(WebSocket conn, Framedata f) {
            super.onWebsocketPing(conn, f);
            Log.e("onWebsocketPing", f.toString());
            conn.sendFrame(new PongFrame((PingFrame) f));
        }

        @Override
        public void onWebsocketPong(WebSocket conn, Framedata f) {
            super.onWebsocketPong(conn, f);
            conn.sendFrame(f);
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            Log.i("关闭", "onClose");
            ChatClient.this.setConnected(false);
            ChatClient.this.onDisconnected();
        }

        @Override
        public void onError(Exception ex) {
            Log.i("WebSocketClientImpl", "onError");
            ChatClient.this.setConnected(false);
        }

    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public void registerClientListener(WebSocketClientListener listener) {
        mClientListenerList.add(listener);
    }

    public void unregisterClientListener(WebSocketClientListener listener) {
        mClientListenerList.remove(listener);
    }

    private void startConnectTask() {
        connectTimer = new Timer();

        connectTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (!isConnected()) {
                    webSocketClientImpl = new WebSocketClientImpl(mURI);
                    webSocketClientImpl.connect();
                } else {
                    cancelConnectTimer();
                }

            }
        };

        connectTimer.schedule(connectTimerTask, 0, 10 * 1000);
        isConnectTimerScheduled = true;
    }

    public void cancelConnectTimer() {
        if (connectTimer != null) {
            connectTimer.cancel();
            connectTimer.purge();
            connectTimer = null;

            connectTimerTask.cancel();
            connectTimerTask = null;
        }
    }

    private void startPingTask() {
        pingTimer = new Timer();

        pingTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (isConnected) {
                    webSocketClientImpl.sendPing();
                }
            }
        };

        pingTimer.schedule(pingTimerTask, 10 * 1000, 10 * 1000);
    }

    public void cancelPingTimer() {
        if (pingTimer != null) {
            pingTimer.cancel();
            pingTimer.purge();
            pingTimer = null;

            pingTimerTask.cancel();
            pingTimerTask = null;
        }
    }

}
