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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by rick on 2016/7/9.
 */


// 要分成交和未成交
public class PageTwo extends PageView {

    static String TAG = "PageTwo";
    private ProgressDialog Loadingdialog;   // loading
    Button button_customer_add;      // 新增顧客

    Context mContext;

    public ListView listView;
    private List Customerlist = new ArrayList();
    private ArrayAdapter<String> listAdapter;
    private  static  int DeletePosition = 0;    // 刪除的位置

    public PageTwo(Context context) {
        super(context);
        mContext = context;

        View view = LayoutInflater.from(context).inflate(R.layout.pager_item2, null);
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText("顧客管理");
        addView(view);


        button_customer_add = (Button) findViewById(R.id.button_addCustomer);
        button_customer_add.setOnClickListener(new Button.OnClickListener(){

            @Override

            public void onClick(View v) {

                Intent intent = new Intent();
                //從MainActivity 到Main2Activity
                intent.setClass(mContext , Customer_Add.class);

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
        listAdapter = new ArrayAdapter(mContext,android.R.layout.simple_list_item_single_choice,Customerlist);
        listView.setAdapter(listAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(mContext, "你選擇的是" + Tasklist[position], Toast.LENGTH_SHORT).show();
                normalDialogEvent("工作項目", (String)Customerlist.get(position), position );

            }
        });

        // 更新view
        refresh();
        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        //lp.gravity = Gravity.CENTER_VERTICAL;
        //textView.setLayoutParams(lp);
    }

    // 跳出訊息匡
    private void normalDialogEvent( String Title, String Message, final  int position ){
        new AlertDialog.Builder(mContext)
                .setTitle(Title)
                .setMessage(Message)
                .setNeutralButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(mContext, "點到修改", Toast.LENGTH_SHORT).show();

                        Bundle bundle = new Bundle();
                        bundle.putInt("position",position );

                        Intent intent = new Intent();
                        //從MainActivity 到Main2Activity
                        intent.setClass(mContext, Customer_Update.class);

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

                        //String item = (String) listAdapter.getItem(position);
                        //listAdapter.remove(item);
                        //listAdapter.notifyDataSetChanged();

                        DeletePosition = position;

                        try {

                            String customerList = EzSharedPreferences.readDataString("customerList");
                            String key = Integer.toString(position);

                            String Data_x = new JSONObject(customerList).getString(key);

                            int customer_id = new JSONObject(Data_x).getInt("customer_id");
                            int user_id = new JSONObject(Data_x).getInt("user_id");
                            String nickname = new JSONObject(Data_x).getString("nickname");
                            String createtime = new JSONObject(Data_x).getString("createtime");
                            String updatetime = new JSONObject(Data_x).getString("updatetime");

                            String customer_name = new JSONObject(Data_x).getString("customer_name");
                            int icustomer_age = new JSONObject(Data_x).getInt("customer_age");

                            int icustomer_home_id = new JSONObject(Data_x).getInt("customer_home_id");

                            JSONObject jsonObject= new JSONObject();

                            jsonObject.put("sys", "system");
                            jsonObject.put("cmd", NET_CMD.NET_CMD_CUSTOMER_DELETE);
                            jsonObject.put("sn", 12345);
                            jsonObject.put("isEncode", false);

                            JSONObject jsonObjectData= new JSONObject();
                            jsonObjectData.put("customer_id", customer_id);
                            jsonObjectData.put("user_id", user_id);
                            jsonObjectData.put("nickname", nickname);
                            jsonObjectData.put("createtime", createtime);
                            jsonObjectData.put("updatetime", updatetime);

                            jsonObjectData.put("customer_home_id", icustomer_home_id);


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
        Log.d(TAG, "refresh");

        Customerlist.clear();

        Loadingdialog = ProgressDialog.show(mContext, "傳送資料中", "請耐心等待3秒...",true);


        try {

            String AccountStr = EzSharedPreferences.readDataString("Account");
            String PasswordStr = EzSharedPreferences.readDataString("Password");

            JSONObject jsonObject= new JSONObject();

            jsonObject.put("sys", "system");
            jsonObject.put("cmd", NET_CMD.NET_CMD_CUSTOMER_LIST_GET);
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
                        Log.d(TAG, "讀取顧客清單成功 str=" + str  );

                        String Cmd = new JSONObject(str).getString("ret");
                        String Data = new JSONObject(str).getString("data");

                        button_customer_add.setEnabled(true);

                        switch (Cmd)
                        {
                            case NET_CMD.NET_CMD_CUSTOMER_LIST_GET:
                            {

                                int data_count = new JSONObject(Data).getInt("data_count");
                                String customerList = new JSONObject(Data).getString("customer_list");
                                for( int i = 0; i < data_count; i++ ) {
                                    String key = Integer.toString(i);

                                    String Data_x = new JSONObject(customerList).getString(key);
                                    int user_id = new JSONObject(Data_x).getInt("user_id");
                                    String nickname = new JSONObject(Data_x).getString("nickname");

                                    String createtime = new JSONObject(Data_x).getString("createtime");
                                    String updatetime = new JSONObject(Data_x).getString("updatetime");

                                    String customer_name = new JSONObject(Data_x).getString("customer_name");
                                    String customer_age = new JSONObject(Data_x).getString("customer_age");
                                    String customer_gender = new JSONObject(Data_x).getString("customer_gender");

                                    String customer_identityNumber = new JSONObject(Data_x).getString("customer_identityNumber");
                                    String customer_phone_number = new JSONObject(Data_x).getString("customer_phone_number");
                                    String customer_address = new JSONObject(Data_x).getString("customer_address");

                                    String customer_home_id = new JSONObject(Data_x).getString("customer_home_id");
                                    String customer_home_age = new JSONObject(Data_x).getString("customer_home_age");
                                    String customer_home_footage = new JSONObject(Data_x).getString("customer_home_footage");
                                    String customer_home_price = new JSONObject(Data_x).getString("customer_home_price");
                                    String vip_rank = new JSONObject(Data_x).getString("vip_rank");

                                    listAdapter.add(customer_name);
                                    //Memberlist.notifyDataSetChanged();
                                }
                                listAdapter.notifyDataSetChanged();

                                // 存進 SharedPreferences
                                EzSharedPreferences.SaveData("customerList", customerList);
                            }
                            break;

                            case NET_CMD.NET_CMD_CUSTOMER_DELETE:
                            {
                                String item = (String) listAdapter.getItem(DeletePosition);
                                listAdapter.remove(item);
                                listAdapter.notifyDataSetChanged();
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

                    default:
                        Log.d(TAG, " 未處理的 msg=" + msg);
                        EzLib.setAlertDialog1Event("發生錯誤", msg.toString());
                        button_customer_add.setEnabled(false);
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
