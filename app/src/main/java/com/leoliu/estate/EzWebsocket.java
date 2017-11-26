package com.leoliu.estate;

/**
 * Created by liuming on 2017/9/4.
 */

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;

import org.java_websocket.drafts.Draft_17;
import org.java_websocket.drafts.Draft_75;
import org.java_websocket.drafts.Draft_76;
import org.java_websocket.drafts.Draft_6455;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;

import javax.net.ssl.SSLSocketFactory;

import static com.leoliu.estate.NET_STATUS.NET_STATUS_ERROR;
import static com.leoliu.estate.NET_STATUS.NET_STATUS_ERROR_CLOSE;
import static com.leoliu.estate.NET_STATUS.NET_STATUS_ERROR_RECEIVE;
import static com.leoliu.estate.NET_STATUS.NET_STATUS_ERROR_SEND;


//===============================================================================================================
// 網路狀態
enum NET_STATUS {
    NET_STATUS_SUCCESS,
    NET_STATUS_ERROR_RECEIVE,
    NET_STATUS_ERROR_SEND,
    NET_STATUS_ERROR_CLOSE,
    NET_STATUS_ERROR,

}

/*
//===============================================================================================================
// 網路狀態
enum ERROR_CODE {
    ERROR_CODE_SUCCESS(0),          // 執行成功
    ERROR_CODE_NO_FIND_CMD(-1),     // 找不到cmd
    ERROR_CODE_NO_FIND_ACCOUNT(-2)  // 找不到帳號
    ;

    private int value;

    private ERROR_CODE(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}*/
//===============================================================================================================
// 網路封包
interface ERROR_CODE {
    public static final int ERROR_CODE_SUCCESS = 0;
}
//===============================================================================================================
// 網路封包
interface NET_CMD {

    // 一般帳號
    public static final String NET_CMD_LOGIN = "login";
    public static final String NET_CMD_LOGOUT = "logout";

    // 會員
    public static final String NET_CMD_MEMBER_INSERT = "member_insert";
    public static final String NET_CMD_MEMBER_UPDATE = "member_update";
    public static final String NET_CMD_MEMBER_DELETE = "member_delete";
    public static final String NET_CMD_MEMBER_LIST_GET = "member_list_get";

    // 顧客
    public static final String NET_CMD_CUSTOMER_INSERT = "customer_insert";
    public static final String NET_CMD_CUSTOMER_UPDATE = "customer_update";
    public static final String NET_CMD_CUSTOMER_DELETE = "customer_delete";
    public static final String NET_CMD_CUSTOMER_LIST_GET  = "customer_list_get";

    // 工作日誌
    public static final String NET_CMD_TASK_INSERT = "task_insert";
    public static final String NET_CMD_TASK_UPDATE = "task_update";
    public static final String NET_CMD_TASK_DELETE = "task_delete";
    public static final String NET_CMD_TASK_LIST_GET = "task_list_get";

    // 房屋
    public static final String NET_CMD_HOME_INSERT = "home_insert";
    public static final String NET_CMD_HOME_UPDATE = "home_update";
    public static final String NET_CMD_HOME_DELETE = "home_delete";
    public static final String NET_CMD_HOME_LIST_GET = "home_list_get";
}

// SSLClientExample https://github.com/TooTallNate/Java-WebSocket/blob/master/src/main/example/SSLClientExample.java

// https://stackoverflow.com/questions/5571092/convert-object-to-json-in-android
public class EzWebsocket {

    static private WebSocketClient webSocketClient;// 连接客户端

    static String TAG = "EzWebsocket";
    private static Activity mActivity;
    static public String Url = "";          // IP
    static public URI serverURI;

    private static Handler mHandler;
    //***********************************************************************************************************
    // 創建
    //***********************************************************************************************************
    public static void onCreate(Activity activity, String IP) {
        Log.d(TAG, "onCreate");

        mActivity = activity;

        try {

            WebSocketImpl.DEBUG = true;
            System.setProperty("java.net.preferIPv6Addresses", "false");
            System.setProperty("java.net.preferIPv4Stack", "true");

            //String address = "ws://192.168.43.75:1234/One1CloudGameCmd";
            //String address = "ws://192.168.43.75:8080/";
            serverURI = new URI(IP);

            Log.e(TAG, "準備连接服务器.....【" + IP + "】");
            //client.setWebSocketFactory();
            //client.setSocket(SSLSocketFactory.getDefault().createSocket(serverURI.getHost(), serverURI.getPort()));
            //setSocket(SSLSocketFactory.getDefault().createSocket(serverURI.getHost(), 443));
            //client.connect();

            //webSocketClient = new WebSocketClient( serverURI, new Draft_6455() ) {
            webSocketClient = new WebSocketClient( serverURI, new Draft_6455() ) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    System.out.println("onOpen");
                    Log.d(TAG, "已经连接到服务器【" + getURI() + "】");

                    //webSocketClient.send("Hello, World!");
                }

                @Override
                public void onMessage(String str) {
                    Log.d(TAG, "#onMessage 收到的訊息?  str=" + str );

                    try {
                        int Code = new JSONObject(str).getInt("code");
                        Message msg = new Message();
                        msg.what = Code;
                        msg.obj = str;
                        mHandler.sendMessage(msg);
                    }catch (Exception ex )
                    {
                        Log.d(TAG, "Exception=" + ex.toString());
                        Log.d(TAG, "Exception1=" + ex.getMessage());

                        Message msg = new Message();
                        msg.what = NET_STATUS_ERROR_RECEIVE.ordinal();
                        msg.obj = ex.getMessage();
                        mHandler.sendMessage(msg);
                    }

                }

                @Override
                public void onClose(final int code, final String reason, final boolean remote) {
                    Log.d(TAG, "断开服务器连接【" + getURI() + "，状态码： " + code + "，断开原因：" + reason + "】");

                    try {

                        Message msg = new Message();
                        msg.what = NET_STATUS_ERROR_CLOSE.ordinal();
                        msg.obj = "斷線了";
                        mHandler.sendMessage(msg);

                        //webSocketClient = new WebSocketClient( serverURI, new Draft_6455() ){};
                    }catch (Exception ex )
                    {
                        Log.d(TAG, "Exception=" + ex.toString());
                        Log.d(TAG, "Exception1=" + ex.getMessage());
                    }
                }

                @Override
                public void onError(Exception ex) {
                    System.out.println("onError");
                    Log.d(TAG, "發生onError =" + ex.toString());
                    Log.d(TAG, "發生onError=" + ex.getMessage());

                    try {

                        Message msg = new Message();
                        msg.what = NET_STATUS_ERROR.ordinal();
                        msg.obj = ex.getMessage();
                        mHandler.sendMessage(msg);
                    }catch (Exception ex2 )
                    {
                        Log.d(TAG, "Exception=" + ex2.toString());
                        Log.d(TAG, "Exception1=" + ex2.getMessage());
                    }
                }

            };
        }catch (Exception ex)
        {
            Log.d(TAG, "發生例外Exception=" + ex.toString());
            Log.d(TAG, "發生例外Exception1=" + ex.getMessage());
            ex.printStackTrace();
        }

        //webSocketClient.setSocket(SSLSocketFactory.getDefault().createSocket(serverURI.getHost(), serverURI.getPort()));

        webSocketClient.connect();


    }

    // 傳送訊息
    public static void SendMessage( String Message, Handler mHandlerCtrl )
    {
        Log.d(TAG, "#SendMessage 送出的訊息  Message=" + Message );

        try
        {
            mHandler = mHandlerCtrl;
            webSocketClient.send(Message);
        }
        catch (Exception ex )
        {
            Log.d(TAG, "發生例外Exception=" + ex.toString());
            Log.d(TAG, "發生例外Exception1=" + ex.getMessage());
            ex.printStackTrace();

            Message msg = new Message();
            msg.what = NET_STATUS_ERROR_SEND.ordinal();
            msg.obj = ex.toString();
            mHandler.sendMessage(msg);
        }

    }

    private class DraftInfo {

        private final String draftName;
        private final Draft draft;

        public DraftInfo(String draftName, Draft draft) {
            this.draftName = draftName;
            this.draft = draft;
        }

        @Override
        public String toString() {
            return draftName;
        }
    }
}
