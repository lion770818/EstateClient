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

public class House_Add extends AppCompatActivity {

    static String TAG = "House_Add";
    private static Context context;
    private ProgressDialog Loadingdialog;

    //private static EditText editHomeID;
    //private static EditText editUser_ID;
    //private static EditText editNickName;
    //private static EditText editCreateTime;
    //private static EditText editUpdateTime;
    private static EditText editHomeName;
    private static EditText editHomeAddress;
    private static EditText editHomeAge;
    private static EditText editHomeFootage;
    private static EditText editHomePrice;
    //private static EditText editVip_rank;
    private static EditText editMemo;

    private static Spinner mSpinner;

    final String[] lunch = { "雅房", "套房", "兩房一廳", "三房兩廳", "工廠", "辦公室", "透天厝", "豪宅"};
    private static int iVip_rank = 0; // 會員等級

    private static Button button_AddHouse;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_house_add);

        context = this;
        EzLib.onCreate(this,TAG);

        editHomeName     = (EditText)findViewById(R.id.editHomeName);
        editHomeAddress  = (EditText)findViewById(R.id.editHomeAddress);
        editHomeAge      = (EditText)findViewById(R.id.editHomeAge);
        editHomeFootage  = (EditText)findViewById(R.id.editHomeFootage);
        editHomePrice    = (EditText)findViewById(R.id.editHomePrice);
        editMemo         = (EditText)findViewById(R.id.editMemo);

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
        button_AddHouse = (Button) findViewById(R.id.button_submit);
        button_AddHouse.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                try {

                    Loadingdialog = ProgressDialog.show(context, "傳送資料中", "請耐心等待3秒...",true);

                    String HomeName      = editHomeName.getText().toString();
                    String HomeAddress   = editHomeAddress.getText().toString();
                    String HomeAge       = editHomeAge.getText().toString();
                    String HomeFootage   = editHomeFootage.getText().toString();
                    String HomePrice     = editHomePrice.getText().toString();
                    String Memo          = editMemo.getText().toString();


                    int     iHomeAge     = Integer.valueOf(HomeAge);
                    float   fHomeFootage = Float.valueOf(HomeFootage);
                    int     iHomePrice   = Integer.valueOf(HomePrice);

                    JSONObject jsonObject= new JSONObject();
                    jsonObject.put("sys", "system");
                    jsonObject.put("cmd", NET_CMD.NET_CMD_HOME_INSERT);
                    jsonObject.put("sn", 12345);
                    jsonObject.put("isEncode", false);

                    //組合data
                    JSONObject jsonObjectData= new JSONObject();
                    jsonObjectData.put("home_name", HomeName);
                    jsonObjectData.put("home_address", HomeAddress);
                    jsonObjectData.put("home_age", iHomeAge);
                    jsonObjectData.put("home_footage", fHomeFootage);
                    jsonObjectData.put("home_price", iHomePrice);
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
    }

    //==============================================================================================
    // 封包事件接收函式
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            Log.d(TAG, "handleMessage msg=" + msg);
            Loadingdialog.dismiss();

            try {

                //int value = ERROR_CODE.ERROR_CODE_SUCCESS.getValue();
                switch(msg.what){
                    case ERROR_CODE.ERROR_CODE_SUCCESS:

                        String str = (String)msg.obj;
                        Log.d(TAG, "創建會員成功 str=" + str  );
                        String Data = new JSONObject(str).getString("data");

                        EzLib.setAlertDialog1Event("創建房屋成功", Data);

                        // 清除輸入的資料
                        editHomeName.setText("");
                        editHomeAddress.setText("");
                        editHomeAge.setText("");
                        editHomeFootage.setText("");
                        editHomePrice.setText("");
                        editMemo.setText("");
                        mSpinner.setSelection(0);

                        break;

                    default:
                        Log.d(TAG, " 未處理的 msg=" + msg);
                        EzLib.setAlertDialog1Event("錯誤", msg.toString());
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {   //確定按下退出鍵

            //ConfirmExit(); //呼叫ConfirmExit()函數
            Log.d(TAG, "onKeyDown keyCode=" + keyCode);
            setResult(2);  // 0:工作 1:客戶 2:房屋物件 3:員工
            finish();
            return true;

        }

        return super.onKeyDown(keyCode, event);

    }
}
