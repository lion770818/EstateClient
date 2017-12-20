package com.leoliu.estate;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONObject;

/**
 * Created by liuming on 2017/8/15.
 */

public class House_Update extends AppCompatActivity {

    static String TAG = "House_Update";
    private static Context context;
    private ProgressDialog Loadingdialog;

    private static EditText editHomeID;
    private static EditText editUser_ID;
    private static EditText editNickName;
    private static EditText editCreateTime;
    private static EditText editUpdateTime;
    private static EditText editHomeName;

    private static EditText editHomeAddress;
    private static EditText editHomeAge;
    private static EditText editHomeFootage;
    private static EditText editHomePrice;
    private static EditText editVip_rank;
    private static EditText editMemo;


    private static Spinner  spinnerHomeArea;
    final String[] HomeArea = { "萬里區", "金山區", "板橋區", "汐止區", "深坑區", "石碇區", "瑞芳區", "平溪區", "雙溪區", "貢寮區", "新店區",
            "坪林區", "烏來區", "永和區", "中和區", "土城區", "山峽區", "樹林區", "鶯歌區", "三重區", "新莊區", "泰山區", "林口區", "蘆洲區",
            "五股區", "八里區", "淡水區", "三芝區", "石門區"};
    private static int iHomeArea = 0; // 房屋地區

    private static Spinner mSpinner;
    final String[] lunch = { "雅房", "套房", "兩房一廳", "三房兩廳", "工廠", "辦公室", "透天厝", "豪宅"};
    private static int iVip_rank = 0; // 會員等級

    private static Button button_UpdateHouse;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_house_update);
        context = this;
        EzLib.onCreate(this,TAG);

        Bundle bundle =this.getIntent().getExtras();
        int position = bundle.getInt("position");

        editHomeID       = (EditText)findViewById(R.id.editHomeID);
        editUser_ID      = (EditText)findViewById(R.id.editUser_ID);
        editNickName     = (EditText)findViewById(R.id.editNickName);
        editCreateTime   = (EditText)findViewById(R.id.editCreateTime);
        editUpdateTime   = (EditText)findViewById(R.id.editUpdateTime);
        editHomeName     = (EditText)findViewById(R.id.editHomeName);

        editHomeAddress  = (EditText)findViewById(R.id.editHomeAddress);
        editHomeAge      = (EditText)findViewById(R.id.editHomeAge);
        editHomeFootage  = (EditText)findViewById(R.id.editHomeFootage);
        editHomePrice    = (EditText)findViewById(R.id.editHomePrice);
        editMemo         = (EditText)findViewById(R.id.editMemo);

        // 房屋地區
        spinnerHomeArea  = (Spinner)findViewById(R.id.SpinnerHomeArea);
        ArrayAdapter<String> lunchHomeList = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item,
                HomeArea);
        spinnerHomeArea.setAdapter(lunchHomeList);
        spinnerHomeArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                iHomeArea = position;
                Log.d(TAG, "你選的是 position=" + position);
                //Toast.makeText(context, "你選的是" + lunchHomeList[iHomeArea], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 會員等級
        mSpinner = (Spinner)findViewById(R.id.SpinnerVipRank);
        ArrayAdapter<String> lunchList = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item,
                lunch);
        mSpinner.setAdapter(lunchList);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                iVip_rank = position;
                Log.d(TAG, "你選的是 position=" + position);
                //Toast.makeText(context, "你選的是" + lunch[iVip_rank], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 送出按鈕
        button_UpdateHouse = (Button) findViewById(R.id.button_submit);
        button_UpdateHouse.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                try {

                    Loadingdialog = ProgressDialog.show(context, "傳送資料中", "請耐心等待3秒...",true);

                    String HomeID          = editHomeID.getText().toString();
                    String User_ID         = editUser_ID.getText().toString();
                    String NickName        = editNickName.getText().toString();
                    String CreateTime      = editCreateTime.getText().toString();
                    String UpdateTime      = editUpdateTime.getText().toString();
                    String HomeName        = editHomeName.getText().toString();

                    String HomeAddress     = editHomeAddress.getText().toString();

                    String HomeAge         = editHomeAge.getText().toString();
                    String HomeFootage     = editHomeFootage.getText().toString();
                    String HomePrice       = editHomePrice.getText().toString();
                    String Memo            = editMemo.getText().toString();


                    int iHomeAge             = Integer.valueOf(HomeAge);
                    float iHomeFootage       = Float.valueOf(HomeFootage);
                    // 薪資
                    int iHomePrice             = Integer.valueOf(HomePrice);

                    JSONObject jsonObject= new JSONObject();
                    jsonObject.put("sys", "system");
                    jsonObject.put("cmd", NET_CMD.NET_CMD_HOME_UPDATE);
                    jsonObject.put("sn", 12345);
                    jsonObject.put("isEncode", false);

                    //組合data
                    JSONObject jsonObjectData= new JSONObject();
                    jsonObjectData.put("home_id", Integer.valueOf(HomeID));
                    jsonObjectData.put("user_id", Integer.valueOf(User_ID));
                    jsonObjectData.put("nickname", NickName);
                    jsonObjectData.put("home_name", HomeName);
                    jsonObjectData.put("home_area", iHomeArea);
                    jsonObjectData.put("home_address", HomeAddress);
                    jsonObjectData.put("home_age", Integer.valueOf(HomeAge));
                    jsonObjectData.put("home_price", Integer.valueOf(HomePrice));
                    jsonObjectData.put("home_footage", Float.valueOf(HomeFootage));
                    jsonObjectData.put("vip_rank", iVip_rank);
                    jsonObjectData.put("memo", Memo);

                    String Data = jsonObjectData.toString();

                    jsonObject.put("data", Data);
                    String jsonStr = jsonObject.toString();
                    //String jsonStr = "{\"sys\":\"system\", \"cmd\":\"lobbyInfoGet\", \"sn\":12345, \"isEncode\":false,\"data\":\"{\\\"platform_id\\\":1}\"}";
                    EzWebsocket.SendMessage(jsonStr, mHandler);

                }catch (Exception ex)
                {
                    ex.printStackTrace();
                    Loadingdialog.dismiss();
                    Message msg = new Message();
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                    Log.d(TAG, "Exception=" + ex.toString());
                }

            }

        });

        // 讀取資料
        ReadData(position);
    }

    //==============================================================================================
    // 讀取資料 (將收到的資料更新到UI上)
    private void ReadData(int position)
    {

        try{

            String homeList = EzSharedPreferences.readDataString("homeList");
            String key = Integer.toString(position);

            String Data_x = new JSONObject(homeList).getString(key);
            int home_id = new JSONObject(Data_x).getInt("home_id");
            int user_id = new JSONObject(Data_x).getInt("user_id");
            String nickname = new JSONObject(Data_x).getString("nickname");
            String createtime = new JSONObject(Data_x).getString("createtime");
            String updatetime = new JSONObject(Data_x).getString("updatetime");

            String home_name = new JSONObject(Data_x).getString("home_name");
            int ihome_area = new JSONObject(Data_x).getInt("home_area");
            String home_address = new JSONObject(Data_x).getString("home_address");
            int home_age = new JSONObject(Data_x).getInt("home_age");
            float home_footage = new JSONObject(Data_x).getInt("home_footage");
            String shome_footage = String.valueOf(home_footage);
            int home_price = new JSONObject(Data_x).getInt("home_price");
            int vip_rank = new JSONObject(Data_x).getInt("vip_rank");

            String memo = new JSONObject(Data_x).getString("memo");

            editHomeID.setText(Integer.toString(home_id));
            editUser_ID.setText(Integer.toString(user_id));
            editNickName.setText(nickname);
            editCreateTime.setText(createtime);
            editUpdateTime.setText(updatetime);
            editHomeName.setText(home_name);

            editHomeAddress.setText(home_address);

            editHomeAge.setText(Integer.toString(home_age));
            editHomeFootage.setText(shome_footage);
            editHomePrice.setText( Integer.toString(home_price));
            editMemo.setText(memo);

            if( ihome_area >= 0 && ihome_area < HomeArea.length )
                spinnerHomeArea.setSelection(ihome_area);
            else{
                String str = "錯誤的長度 ihome_area=" +  Integer.toString(ihome_area);
                EzLib.setAlertDialog1Event( "錯誤", str );
            }

            if( vip_rank >= 0 && vip_rank < lunch.length )
                mSpinner.setSelection(vip_rank);
            else{
                String str = "錯誤的長度 vip_rank=" +  Integer.toString(vip_rank);
                EzLib.setAlertDialog1Event( "錯誤", str );
            }

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
                        Log.d(TAG, "更新房屋成功 str=" + str  );
                        String Data = new JSONObject(str).getString("data");
                        EzLib.setAlertDialog1Event("更新房屋成功", Data);

                        break;

                    default:
                        Log.d(TAG, " 未處理的 msg=" + msg);
                        EzLib.setAlertDialog1Event("發生錯誤", msg.toString());
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

    //==============================================================================================
    // 確定按下退出鍵
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {   //確定按下退出鍵

            //ConfirmExit(); //呼叫ConfirmExit()函數
            Log.d(TAG, "onKeyDown keyCode=" + keyCode);
            setResult(3);
            finish();
            return true;

        }

        return super.onKeyDown(keyCode, event);

    }
}
