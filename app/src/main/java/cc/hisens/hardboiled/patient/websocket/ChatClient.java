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

public class ChatClient implements WebSocketClientListener {

    private static URI mURI;
    private static ChatClient mChatClient;
    private WebSocketClientImpl webSocketClientImpl;
    private Timer connectTimer;
    private TimerTask connectTimerTask;
    private Timer pingTimer;
    private TimerTask pingTimerTask;

    private boolean isConnected;
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

    public void sendMessage(MessageModel message) {
        if (message == null) {
            throw new NullPointerException("Message is Empty");
        }
        webSocketClientImpl.send(message.toString().getBytes());
    }

    @Override
    public void onConnected() {
        MessageModel message = new MessageModel();
        message.setFrom(10000);
        message.setType((byte) 6);
        message.setTime(System.currentTimeMillis() / 1000);

        sendMessage(message);

        cancelConnectTimer();

        startPingTask();
    }

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
            Log.i("WebSocketClientImpl", "onOpen");
            ChatClient.this.setConnected(true);
            ChatClient.this.onConnected();
        }

        @Override
        public void onMessage(String message) {
            Log.i("WebSocketClientImpl", "onMessage");
        }

        @Override
        public void onMessage(ByteBuffer bytes) {
            super.onMessage(bytes);
            String json = new String(bytes.array());
            Log.i("WebSocketClientImpl", "onMessage: " + json);
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
            Log.d("onWebsocketPing", f.toString());
            conn.sendFrame(new PongFrame((PingFrame) f));
        }

        @Override
        public void onWebsocketPong(WebSocket conn, Framedata f) {
            super.onWebsocketPong(conn, f);
            conn.sendFrame(f);
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            Log.i("WebSocketClientImpl", "onClose");
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

    private void cancelConnectTimer() {
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

    private void cancelPingTimer() {
        if (pingTimer != null) {
            pingTimer.cancel();
            pingTimer.purge();
            pingTimer = null;

            pingTimerTask.cancel();
            pingTimerTask = null;
        }
    }

}
