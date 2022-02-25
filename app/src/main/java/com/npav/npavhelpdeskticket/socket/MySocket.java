package com.npav.npavhelpdeskticket.socket;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.core.util.Pair;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import javax.net.ssl.SSLSocketFactory;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MySocket {
    private static final String TAG = MySocket.class.getSimpleName();

    private static URI mServerUri;
    private static Gson gson;
    private final Context mContext;

    private static WebSocketClient webSocketClient;

    private static ObservableEmitter<Pair<String, JsonElement>> emit;
    private static ObservableOnSubscribe<Pair<String, JsonElement>> emisible = e -> emit = e;

    private static Observable<Pair<String, JsonElement>> observable;
    private static boolean tryReconnecting = false;
    private int mCountForReconnect = 0;
    private passDataListener mListener;

    public static MySocket initSocket(Context context, String url) {
        return new MySocket(context, url);
    }

    public MySocket(Context context, String url) {
        this.mContext = context;
        mListener = (passDataListener) mContext;
        if (gson == null) {
            gson = new Gson();
        }

        if (observable == null) {
            observable = Observable.create(emisible);
        }

        if (webSocketClient == null) {
            URI uri = null;
            try {
                uri = new URI(url);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            mServerUri = uri;

            if (mServerUri != null) {
                createWebSocket();
            }
        } else {
            reconnectIfNecessary();
        }
    }

    public Observable<Pair<String, JsonElement>> getObservableListener() {
        return observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public synchronized void reconnectIfNecessary() {
        Log.e("Websocket", "reconnectIfNecessary at " + System.currentTimeMillis());
        if (tryReconnecting) {
            return;
        }
        /*ESTADOS
         *
         * ReadyState.NOT_YET_CONNECTED
         * ReadyState.CLOSED
         * ReadyState.CLOSING
         * ReadyState.OPEN
         *
         * */

        if (webSocketClient == null) {
            createWebSocket();

        } else if (!webSocketClient.isOpen()) {
            tryReconnecting = true;
            invocarDentroDelTimerEspecial();
        }
    }


    private void invocarDentroDelTimerEspecial() {
        int delay;
        if (webSocketClient == null) {
            createWebSocket();
            delay = 1;
            mCountForReconnect = 1;
        } else if (webSocketClient.isClosed()) {
            webSocketClient.reconnect();
            mCountForReconnect++;
            delay = getCurrentFibonacci(mCountForReconnect);
        } else if (webSocketClient.isOpen()) {
            tryReconnecting = false;
            mCountForReconnect = 1;
            delay = 0;
        } else {
            mCountForReconnect++;
            delay = getCurrentFibonacci(mCountForReconnect);
        }

        if (delay != 0) {
            handler.postDelayed(runnableTimer, delay * 1000);
        }
    }

    private void createWebSocket() {
        SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();

        webSocketClient = new WebSocketClient(mServerUri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.e("Websocket", "Opened at " + System.currentTimeMillis());
            }

            @Override
            public void onMessage(String message) {
                Log.e(TAG + " socket", "onMessage" + message);
                mListener.passData(message);

            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.e("Websocket", "Closed at " + System.currentTimeMillis());
                reconnectIfNecessary();
            }

            @Override
            public void onError(Exception ex) {
                Log.e("Websocket", "Error " + ex.getMessage());
                reconnectIfNecessary();
            }
        };

//        webSocketClient.setSocketFactory(socketFactory);
        webSocketClient.connect();
    }

    public void send(String msg) {
        if (isOpen()) {
            webSocketClient.send(msg);
        }
    }

    public boolean isOpen() {
        return webSocketClient != null && webSocketClient.isOpen();
    }

    public boolean isClosed() {
        return webSocketClient != null && webSocketClient.isClosed();
    }

    public boolean isClosing() {
        return webSocketClient != null && webSocketClient.isClosing();
    }

    private final Handler handler = new Handler();
    private final Runnable runnableTimer = this::invocarDentroDelTimerEspecial;

    public static int getCurrentFibonacci(int item) {
        int maxNumber = 1000;
        int previousNumber = 0;
        int nextNumber = 1;

        int iterator = 0;
        for (int i = 1; i <= maxNumber; ++i) {
            int sum = previousNumber + nextNumber;
            previousNumber = nextNumber;
            nextNumber = sum;
            if (iterator++ == item) {
                return nextNumber;
            }
        }
        return maxNumber;
    }

    public interface passDataListener {
        void passData(String s);
    }
}
