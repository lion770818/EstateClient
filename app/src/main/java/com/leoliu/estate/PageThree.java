package com.leoliu.estate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 2016/7/9.
 */

public class PageThree extends PageView {

    static String TAG = "PageThree";
    private ProgressDialog Loadingdialog;   // loading
    Button button_addHouse;                 // 新增房屋

    Context mContext;

    public ListView listView;

    private List Houselist = new ArrayList();
    private ArrayAdapter<String> listAdapter;
    private  static  int DeletePosition = 0;    // 刪除的位置

    public PageThree(Context context) {
        super(context);
        mContext = context;

        View view = LayoutInflater.from(context).inflate(R.layout.pager_item3, null);
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText("房屋管理");
        addView(view);


        button_addHouse = (Button) findViewById(R.id.button_addHouse);
        button_addHouse.setOnClickListener(new Button.OnClickListener(){

            @Override

            public void onClick(View v) {

                Intent intent = new Intent();
                //從MainActivity 到Main2Activity
                intent.setClass(mContext , House_Add.class);

                //開啟Activity
                //mContext.startActivity(intent);
                if (mContext instanceof MainMenu) {
                    ((MainMenu) mContext).startActivityForResult(intent,0);
                } else {
                    Log.d(TAG, "mContext should be an instanceof Activity."  );
                }
            }

        });

        listView = (ListView) findViewById(R.id.listView);
        //找到ListView


        // RadioButton Layout 樣式 : android.R.layout.simple_list_item_single_choice
        // CheckBox Layout 樣式    : android.R.layout.simple_list_item_multiple_choice
        listAdapter = new ArrayAdapter(mContext,android.R.layout.simple_list_item_single_choice,Houselist);
        listView.setAdapter(listAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(mContext, "你選擇的是" + Tasklist[position], Toast.LENGTH_SHORT).show();
                normalDialogEvent("房屋項目", (String)Houselist.get(position), position );

            }
        });
        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        //lp.gravity = Gravity.CENTER_VERTICAL;
        //textView.setLayoutParams(lp);

        // 更新view
        //refresh();
    }


    // 跳出訊息匡
    private void normalDialogEvent( String Title, String Message, final  int position ){
        new AlertDialog.Builder(mContext)
                .setTitle(Title)
                .setMessage(Message)
                .setNeutralButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Bundle bundle = new Bundle();
                        bundle.putInt("position",position );

                        Intent intent = new Intent();
                        //從MainActivity 到Main2Activity
                        intent.setClass(mContext, House_Update.class);

                        //將Bundle物件assign給intent
                        intent.putExtras(bundle);

                        //開啟Activity
                        //mContext.startActivity(intent);
                        if (mContext instanceof MainMenu) {
                            ((MainMenu) mContext).startActivityForResult(intent,0);
                        } else {
                            Log.d(TAG, "mContext should be an instanceof Activity."  );
                        }
                    }
                })
                .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "點到刪除", Toast.LENGTH_SHORT).show();

                        DeletePosition = position;

                        try {

                            String homeList = EzSharedPreferences.readDataString("homeList");
                            String key = Integer.toString(position);

                            String Data_x = new JSONObject(homeList).getString(key);
                            int user_id = new JSONObject(Data_x).getInt("user_id");
                            String nickname = new JSONObject(Data_x).getString("nickname");
                            String createtime = new JSONObject(Data_x).getString("createtime");
                            String updatetime = new JSONObject(Data_x).getString("updatetime");
                            int home_id = new JSONObject(Data_x).getInt("home_id");
                            String home_name = new JSONObject(Data_x).getString("home_name");
                            int home_price = new JSONObject(Data_x).getInt("home_price");

                            String menmo = new JSONObject(Data_x).getString("memo");


                            JSONObject jsonObject= new JSONObject();

                            jsonObject.put("sys", "system");
                            jsonObject.put("cmd", NET_CMD.NET_CMD_HOME_DELETE);
                            jsonObject.put("sn", 12345);
                            jsonObject.put("isEncode", false);

                            JSONObject jsonObjectData= new JSONObject();
                            jsonObjectData.put("user_id", user_id);
                            jsonObjectData.put("nickname", nickname);

                            jsonObjectData.put("home_id", home_id);
                            jsonObjectData.put("home_name", home_name);


                            String Data = jsonObjectData.toString();

                            jsonObject.put("data", Data);
                            String jsonStr = jsonObject.toString();
                            EzWebsocket.SendMessage(jsonStr, mHandler);

                        }catch (Exception ex)
                        {
                            Log.d(TAG, "例外 msg=" + ex.getMessage());
                            Log.d(TAG, "例外 msg=" + ex.toString());
                            EzLib.setAlertDialog1Event( "例外", ex.toString());
                        }

                    }
                })
                .setPositiveButton(R.string.cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "點到取消", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    @Override
    public void refresh() {
        Houselist.clear();

        Loadingdialog = ProgressDialog.show(mContext, "傳送資料中", "請耐心等待3秒...",true);


        try {

            String AccountStr = EzSharedPreferences.readDataString("Account");
            String PasswordStr = EzSharedPreferences.readDataString("Password");

            JSONObject jsonObject= new JSONObject();

            jsonObject.put("sys", "system");
            jsonObject.put("cmd", NET_CMD.NET_CMD_HOME_LIST_GET);
            jsonObject.put("sn", 12345);
            jsonObject.put("isEncode", false);

            JSONObject jsonObjectData= new JSONObject();
            jsonObjectData.put("PlatformID", 0);
            jsonObjectData.put("Account", AccountStr);
            jsonObjectData.put("Password", PasswordStr);
            String Data = jsonObjectData.toString();

            jsonObject.put("data", Data);
            String jsonStr = jsonObject.toString();
            //String msg2 = "{\"sys\":\"system\", \"cmd\":\"login\", \"sn\":12345, \"isEncode\":false,\"data\":\"{\\\"PlatformID\\\":1,\\\"GameID\\\":0,\\\"Account\\\":\\\"cat111\\\",\\\"Password\\\":\\\"1234\\\"}\"}";
            EzWebsocket.SendMessage(jsonStr, mHandler);

            //String jsonStr = "{\"sys\":\"system\", \"cmd\":\"member_list_get\", \"sn\":12345, \"isEncode\":false,\"data\":\"{\\\"platform_id\\\":0,\\\"account\\\":\\\"cat111\\\",\\\"password\\\":\\\"1234\\\" }\"}";
            //EzWebsocket.SendMessage(jsonStr, mHandler);
        }catch (Exception ex)
        {
            Log.d(TAG, "例外 msg=" + ex.getMessage());
            Log.d(TAG, "例外 msg=" + ex.toString());
            EzLib.setAlertDialog1Event( "例外", ex.toString());
        }
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
                    case ERROR_CODE.ERROR_CODE_SUCCESS:

                        String str = (String)msg.obj;
                        Log.d(TAG, "讀取房屋清單成功 str=" + str  );

                        String Cmd = new JSONObject(str).getString("ret");
                        String Data = new JSONObject(str).getString("data");

                        button_addHouse.setEnabled(true);

                        switch (Cmd)
                        {
                            case NET_CMD.NET_CMD_HOME_LIST_GET:
                            {

                                int data_count = new JSONObject(Data).getInt("data_count");
                                String homeList = new JSONObject(Data).getString("home_list");
                                for( int i = 0; i < data_count; i++ ) {
                                    String key = Integer.toString(i);

                                    String Data_x = new JSONObject(homeList).getString(key);
                                    int user_id = new JSONObject(Data_x).getInt("user_id");
                                    String nickname = new JSONObject(Data_x).getString("nickname");

                                    String createtime = new JSONObject(Data_x).getString("createtime");
                                    String updatetime = new JSONObject(Data_x).getString("updatetime");
                                    String home_name = new JSONObject(Data_x).getString("home_name");
                                    int home_id = new JSONObject(Data_x).getInt("home_id");
                                    String memo = new JSONObject(Data_x).getString("memo");

                                    listAdapter.add(home_name);
                                    //Memberlist.notifyDataSetChanged();
                                }
                                listAdapter.notifyDataSetChanged();

                                // 存進 SharedPreferences
                                EzSharedPreferences.SaveData("homeList", homeList);
                            }
                            break;

                            case NET_CMD.NET_CMD_HOME_DELETE:
                            {
                                //String item = (String) listAdapter.getItem(DeletePosition);
                                //listAdapter.remove(item);
                                //listAdapter.notifyDataSetChanged();

                                // 清除所有UI
                                listAdapter.clear();
                                // 送出讀取清單封包, 重新更新UI
                                refresh();
                            }
                            break;
                            default:
                                EzLib.setAlertDialog1Event("未處理的Cmd", Cmd );
                                break;
                        }




                        Gson gson = new Gson();

                        //JSONArray numberLis = new JSONArray(Data);
                        /*
                        for(int i=0; i<numberLis.length(); i++){

                            //获取数组中的数组
                            int user_id = numberLis.getJSONObject(i).getInt("user_id");
                            String account = numberLis.getJSONObject(i).getString("account");
                            String password = numberLis.getJSONObject(i).getString("password");
                            String nickname = numberLis.getJSONObject(i).getString("nickname");
                            Memberlist.add(nickname);
                        }
                        */
                        //setAlertDialog1Event("讀取會員成功", Data);

                        break;
                    case ERROR_CODE.ERROR_CODE_DISCONNECT:
                        Log.d(TAG, "斷線了返回登入頁");
                        EzLib.GotoLogin(mContext);
                        break;
                    default:
                        Log.d(TAG, " 未處理的 msg=" + msg);
                        EzLib.setAlertDialog1Event("發生錯誤", msg.toString());
                        button_addHouse.setEnabled(false);
                        break;
                }

            }catch (Exception ex)
            {
                Log.d(TAG, "例外 msg=" + ex.getMessage());
                Log.d(TAG, "例外 msg=" + ex.toString());
                EzLib.setAlertDialog1Event( "例外", ex.toString());
            }

        }
    };
}
