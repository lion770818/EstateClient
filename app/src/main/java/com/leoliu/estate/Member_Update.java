package com.leoliu.estate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by liuming on 2017/8/15.
 */

public class Member_Update extends AppCompatActivity {

    static String TAG = "Member_Update";
    private static Context context;
    private ProgressDialog Loadingdialog;

    private static EditText EditAccount;
    private static EditText EditPassword;
    private static EditText EditName;
    private static EditText EditIdentityNumber;
    private static EditText EditAddress;
    private static EditText EditPhoneNumber;
    private static EditText EditSalary;

    //private static  int max = 8;
    private static Spinner mSpinner;
    final String[] lunch = { "新進員工", "一般員工", "組長", "主任", "經理", "人資", "會計", "最高管理者", "董事長"};
    private static int iVip_rank = 0; // 會員等級
    private static Button button_UpdateMember;

    //==============================================================================================
    // init
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_member_update);
        context = this;
        EzLib.onCreate(this,TAG);

        Bundle bundle =this.getIntent().getExtras();
        int position = bundle.getInt("position");


        EditAccount         = (EditText)findViewById(R.id.editText_Account);
        EditPassword        = (EditText)findViewById(R.id.editText_Password);
        EditName            = (EditText)findViewById(R.id.editText_Name);
        EditIdentityNumber  = (EditText)findViewById(R.id.editText_IdentityNumber);
        EditAddress         = (EditText)findViewById(R.id.editText_Address);
        EditPhoneNumber     = (EditText)findViewById(R.id.editText_PhoneNumber);
        EditSalary          = (EditText)findViewById(R.id.editText_Salary);

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
        button_UpdateMember = (Button) findViewById(R.id.button_submit);
        button_UpdateMember.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                try {

                    Loadingdialog = ProgressDialog.show(context, "傳送資料中", "請耐心等待3秒...",true);

                    String Account          = EditAccount.getText().toString();
                    String Password         = EditPassword.getText().toString();
                    String Name             = EditName.getText().toString();
                    String IdentityNumber   = EditIdentityNumber.getText().toString();
                    String Address          = EditAddress.getText().toString();
                    String PhoneNumber      = EditPhoneNumber.getText().toString();
                    String Salary           = EditSalary.getText().toString();

                    // 薪資
                    int iSalary             = Integer.valueOf(Salary);

                    JSONObject jsonObject= new JSONObject();
                    jsonObject.put("sys", "system");
                    jsonObject.put("cmd", NET_CMD.NET_CMD_MEMBER_UPDATE);
                    jsonObject.put("sn", 12345);
                    jsonObject.put("isEncode", false);

                    //組合data
                    JSONObject jsonObjectData= new JSONObject();
                    jsonObjectData.put("account", Account);
                    jsonObjectData.put("password", Password);
                    jsonObjectData.put("nickname", Name);
                    jsonObjectData.put("identityNumber", IdentityNumber);
                    jsonObjectData.put("address", Address);
                    jsonObjectData.put("phone_number", PhoneNumber);
                    jsonObjectData.put("Salary", iSalary);
                    jsonObjectData.put("vip_rank", iVip_rank);

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

            String numberLis = EzSharedPreferences.readDataString("numberLis");
            String key = Integer.toString(position);

            String Data_x = new JSONObject(numberLis).getString(key);
            int user_id = new JSONObject(Data_x).getInt("user_id");
            String account = new JSONObject(Data_x).getString("account");
            String password = new JSONObject(Data_x).getString("password");
            String nickname = new JSONObject(Data_x).getString("nickname");

            String IdentityNumber = new JSONObject(Data_x).getString("identityNumber");
            String Address = new JSONObject(Data_x).getString("address");
            String PhoneNumber = new JSONObject(Data_x).getString("phone_number");
            String Salary = new JSONObject(Data_x).getString("salary");
            int vip_rank = new JSONObject(Data_x).getInt("vip_rank");

            EditAccount.setText(account);
            EditPassword.setText(password);
            EditName.setText(nickname);
            EditIdentityNumber.setText(IdentityNumber);
            EditAddress.setText(Address);
            EditPhoneNumber.setText(PhoneNumber);
            EditSalary.setText(Salary);

            if( vip_rank >= 0 && vip_rank <= 8 )
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
                        Log.d(TAG, "更新會員成功 str=" + str  );
                        String Data = new JSONObject(str).getString("data");
                        EzLib.setAlertDialog1Event("更新會員成功", Data);

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
