package com.leoliu.estate;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.UUID;


import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;

import static com.leoliu.estate.ERROR_CODE.ERROR_CODE_SUCCESS;


//  Android之WebSocket使用方法 http://www.nljb.net/default/Android%E4%B9%8BWebSocket%E4%BD%BF%E7%94%A8%E6%96%B9%E6%B3%95/
//==============================================================================================
//
public class LoginActivity extends AppCompatActivity {

    static String TAG = "LoginActivity";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    // 記得我
    private CheckBox Remember;

    private EditText EditAccount;
    private EditText EditPassword;
    private String AccountStr = "";
    private String PasswordStr = "";

    private boolean IsLoop = true;
    private Thread thread;
    private ProgressDialog Loadingdialog;

    private Handler mHandlerCtrl = new Handler();

    //==============================================================================================
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        EzSharedPreferences.onCreate(this,TAG);
        EzNetWork.onCreate(this,TAG);
        EzWebsocket.onCreate(this,"ws://192.168.43.75:3000/One1CloudGameCmd");  //my
        //EzWebsocket.onCreate(this,"ws://192.168.0.104:3000/One1CloudGameCmd");   //home
        //EzWebsocket.onCreate(this,"ws://52.198.59.96:3000/One1CloudGameCmd");  //onmyhomd

        // 抓uuid
        // http://blog.mosil.biz/2014/05/android-device-id-uuid/#randomUUID
        // String uuid = UUID.randomUUID().toString();
        // Log.d(TAG,"uuid=" + uuid );

        try {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId = tm.getDeviceId();
            Log.d(TAG, "deviceId=" + deviceId);
        }
        catch (Exception ex)
        {
            Log.d(TAG, "Exception toString =" + ex.toString());
            Log.d(TAG, "Exception getMessage =" + ex.getMessage());
        }

        // 記憶密碼
        CheckBox Remember = (CheckBox) findViewById(R.id.remember);
        EditAccount = (EditText)findViewById(R.id.Account);
        EditPassword = (EditText)findViewById(R.id.Password);
        String Account = EzSharedPreferences.readDataString("Account");
        String Password = EzSharedPreferences.readDataString("Password");
        boolean RememberFlag = EzSharedPreferences.readDataBoolean("Remember");

        if(RememberFlag == true )
        {
            Log.d(TAG, "記住我的-Account =" + Account);
            Log.d(TAG, "記住我的-Password" + Password);
            EditAccount.setText(Account);
            EditPassword.setText(Password);

            Remember.setChecked(true);
        }else
            Remember.setChecked(false);

        Remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(LoginActivity.this, "記住帳密:" + isChecked + "", Toast.LENGTH_SHORT).show();

                // 儲存是否記憶密碼flag
                EzSharedPreferences.SaveData("Remember", isChecked);
            }
        });

        // 登錄
        ImageButton button = (ImageButton) findViewById(R.id.login_button);
        button.setOnClickListener(new ImageButton.OnClickListener() {

            @Override
            public void onClick(View v) {

                // 抓帳秘
                IsLoop = true;
                AccountStr = EditAccount.getText().toString();
                PasswordStr = EditPassword.getText().toString();
                Loadingdialog = ProgressDialog.show(LoginActivity.this, "登錄中", "請耐心等待3秒...",true);
                //HttpPost httpRequest = new HttpPost("http://13.113.26.157:3000/MblieLogin");

                //HttpPost httpRequest = new HttpPost("http://13.113.26.157:3000/MblieLogin");


                //WebScoketClient client;

                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        while(IsLoop){
                            try{

                                JSONObject jsonObject= new JSONObject();

                                jsonObject.put("sys", "system");
                                jsonObject.put("cmd", NET_CMD.NET_CMD_LOGIN);
                                jsonObject.put("sn", 12345);
                                jsonObject.put("isEncode", false);

                                JSONObject jsonObjectData= new JSONObject();
                                jsonObjectData.put("PlatformID", 1);
                                jsonObjectData.put("GameID", 1001);
                                jsonObjectData.put("Account", AccountStr);
                                jsonObjectData.put("Password", PasswordStr);
                                String Data = jsonObjectData.toString();

                                jsonObject.put("data", Data);
                                String jsonStr = jsonObject.toString();
                                //String msg2 = "{\"sys\":\"system\", \"cmd\":\"login\", \"sn\":12345, \"isEncode\":false,\"data\":\"{\\\"PlatformID\\\":1,\\\"GameID\\\":0,\\\"Account\\\":\\\"cat111\\\",\\\"Password\\\":\\\"1234\\\"}\"}";
                                EzWebsocket.SendMessage(jsonStr, mHandler);

                                // 如果要記憶密碼
                                boolean RememberFlag = EzSharedPreferences.readDataBoolean("Remember");
                                if( RememberFlag == true )
                                {
                                    Log.d(TAG, "記憶密碼開始 AccountStr=" + AccountStr + " PasswordStr=" + PasswordStr);
                                    // 記憶密碼
                                    EzSharedPreferences.SaveData("Account", AccountStr);
                                    EzSharedPreferences.SaveData("Password", PasswordStr);
                                }


                                IsLoop = false;
                                //Loadingdialog.dismiss();
                                Thread.sleep(500);
                            }
                            catch(Exception e){
                                e.printStackTrace();
                                IsLoop = false;
                                Loadingdialog.dismiss();
                                Message msg = new Message();
                                msg.what = 0;
                                mHandler.sendMessage(msg);
                                Log.d(TAG, "Exception=" + e.toString());
                            }
                        }
                    }
                });

                thread.start();
            }

        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();



    }

    //==============================================================================================
    // http://rx1226.pixnet.net/blog/post/305873256-%5Bandroid%5D-10-1-%E5%9F%BA%E7%A4%8Edialog
    private void setAlertDialog1Event( String Message ){

        AlertDialog.Builder builder = new AlertDialog.Builder( LoginActivity.this);

        builder.setTitle("錯誤");
        builder.setMessage(Message);
        builder.setPositiveButton("關閉", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //==============================================================================================
    // 封包事件接收函式
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            Log.d(TAG, "handleMessage msg=" + msg);
            Loadingdialog.dismiss();

            try {

                switch(msg.what){
                    case ERROR_CODE_SUCCESS:

                        String str = (String)msg.obj;
                        Log.d(TAG, "登入成功 str=" + str  );
                        String Data = new JSONObject(str).getString("data");

                        int user_id = new JSONObject(Data).getInt("user_id");
                        String nickname = new JSONObject(Data).getString("nickname");
                        String ip = new JSONObject(Data).getString("ip");
                        String phone_number = new JSONObject(Data).getString("phone_number");

                        EzSharedPreferences.SaveData("user_id", user_id);
                        EzSharedPreferences.SaveData("nickname", nickname);

                        GotoMainMenu();
                        break;

                    default:
                        Log.d(TAG, " 未處理的 msg=" + msg);
                        setAlertDialog1Event(msg.toString());
                        break;
                }

            }catch (Exception ex)
            {
                Log.d(TAG, "例外 msg=" + ex.getMessage());
                Log.d(TAG, "例外 msg=" + ex.toString());
                setAlertDialog1Event(ex.toString());
            }

        }
    };

    //==============================================================================================
    //
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.leoliu.estate/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.leoliu.estate/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    //==============================================================================================
    //
    public void GotoMainMenu(){

        // 登入成功 轉到第二頁
        Intent intent = new Intent();
        //從MainActivity 到Main2Activity
        intent.setClass(LoginActivity.this, MainMenu.class);
        //intent.setClassName("com.leoliu.estate.LoginActivity", "com.leoliu.estate.MainMenu" );

        //intent.setClass(LoginActivity.this, Member_Add.class);
        //開啟Activity
        startActivity(intent);
    }
/*
    //宣告一個新的類別並擴充Thread
    class HttpThread extends Thread {

        //宣告變數並指定預設值
        public String MyName = "NoData";
        public String MyMessage = "Nodata";
        public String Url = "http://192.168.1.49/test/test.php";

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();

            //宣告一個新的Bundle物件，Bundle可以在多個執行緒之間傳遞訊息
            Bundle myBundle = new Bundle();

            try {
                HttpClient client = new DefaultHttpClient();
                URI website = new URI(this.Url);

                //指定POST模式
                HttpPost request = new HttpPost();

                //POST傳值必須將key、值加入List<NameValuePair>
                List<NameValuePair> parmas = new ArrayList<NameValuePair>();

                //逐一增加POST所需的Key、值
                parmas.add(new BasicNameValuePair("MyName", this.MyName));
                parmas.add(new BasicNameValuePair("MyMessage", this.MyMessage));

                //宣告UrlEncodedFormEntity來編碼POST，指定使用UTF-8
                UrlEncodedFormEntity env = new UrlEncodedFormEntity(parmas, HTTP.UTF_8);
                request.setURI(website);

                //設定POST的List
                request.setEntity(env);

                HttpResponse response = client.execute(request);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    myBundle.putString("response", EntityUtils.toString(resEntity));
                } else {
                    myBundle.putString("response", "Nothing");
                }

                Message msg = new Message();
                msg.setData(myBundle);
                mHandler.sendMessage(msg);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/
}
