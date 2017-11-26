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

public class Customer_Update extends AppCompatActivity {

    static String TAG = "Customer_Update";
    private static Context context;
    private ProgressDialog Loadingdialog;

    private static EditText editCustomerName;
    private static EditText editCustomerAge;
    private static Spinner  SpinnerCustomerGender;
    private static EditText editCustomerIdentityNumber;
    private static EditText editCustomerPhoneNumber;
    private static EditText editCustomerAddress;
    private static EditText editCustomerHomeID;
    private static EditText editCustomerHomeAge;
    private static EditText editCustomerHomeFootage;
    private static EditText editCustomerHomePrice;
    private static Spinner  SpinnerVipRank;

    private static  int sex_max = 2;
    final String[] Sexlunch = { "男性", "女性"};
    private static int iSex = 0;        // 顧客性別

    private static  int max = 8;
    final String[] lunch = { "一般", "熟客", "VIP"};
    private static int iVip_rank = 0;   // 顧客等級

    private static int customer_id = 0;
    private static Button button_UpdateCustomer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_customer_update);

        context = this;
        EzLib.onCreate(this,TAG);

        Bundle bundle =this.getIntent().getExtras();
        int position = bundle.getInt("position");


        editCustomerName            = (EditText)findViewById(R.id.editCustomerName);
        editCustomerAge             = (EditText)findViewById(R.id.editCustomerAge);

        editCustomerIdentityNumber  = (EditText)findViewById(R.id.editCustomerIdentityNumber);
        editCustomerPhoneNumber     = (EditText)findViewById(R.id.editCustomerPhoneNumber);
        editCustomerAddress         = (EditText)findViewById(R.id.editCustomerAddress);

        editCustomerHomeID          = (EditText)findViewById(R.id.editCustomerHomeID);
        editCustomerHomeAge         = (EditText)findViewById(R.id.editCustomerHomeAge);
        editCustomerHomeFootage     = (EditText)findViewById(R.id.editCustomerHomeFootage);
        editCustomerHomePrice       = (EditText)findViewById(R.id.editCustomerHomePrice);


        // 顧客性別
        SpinnerCustomerGender = (Spinner)findViewById(R.id.SpinnerCustomerGender);
        ArrayAdapter<String> lunchList = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item,
                Sexlunch);
        SpinnerCustomerGender.setAdapter(lunchList);
        SpinnerCustomerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                iSex = position;
                Log.d(TAG, "你選的是 position=" + position);
                //Toast.makeText(context, "你選的是" + lunch[iVip_rank], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 顧客等級
        SpinnerVipRank = (Spinner)findViewById(R.id.SpinnerVipRank);
        ArrayAdapter<String> lunchList2 = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item,
                lunch);
        SpinnerVipRank.setAdapter(lunchList2);
        SpinnerVipRank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        //SpinnerVipRank.setVisibility(View.GONE);    // 暫時關閉權限等級 客戶好像不該有?

        // 送出按鈕
        button_UpdateCustomer = (Button) findViewById(R.id.button_submit);
        button_UpdateCustomer.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                try {

                    Loadingdialog = ProgressDialog.show(context, "傳送資料中", "請耐心等待3秒...",true);

                    String CustomerName             = editCustomerName.getText().toString();
                    String CustomerAge              = editCustomerAge.getText().toString();
                    int    iCustomerAge             = Integer.valueOf(CustomerAge);
                    String CustomerGender           = (iSex <= 0) ? "M":"F" ;   // 0:男 1:女
                    String CustomerIdentityNumber   = editCustomerIdentityNumber.getText().toString();
                    String CustomerPhoneNumber      = editCustomerPhoneNumber.getText().toString();
                    String CustomerAddress          = editCustomerAddress.getText().toString();

                    String CustomerHomeID           = editCustomerHomeID.getText().toString();
                    int    iCustomerHomeID          = Integer.valueOf(CustomerHomeID);

                    String CustomerHomeAge          = editCustomerHomeAge.getText().toString();
                    int    iCustomerHomeAge         = Integer.valueOf(CustomerHomeAge);

                    String CustomerHomeFootage      = editCustomerHomeFootage.getText().toString();
                    Float  fCustomerHomeFootage     = Float.valueOf(CustomerHomeFootage);

                    String CustomerHomePrice        = editCustomerHomePrice.getText().toString();
                    int    iCustomerHomePrice       = Integer.valueOf(CustomerHomePrice);


                    JSONObject jsonObject= new JSONObject();
                    jsonObject.put("sys", "system");
                    jsonObject.put("cmd", NET_CMD.NET_CMD_CUSTOMER_UPDATE);
                    jsonObject.put("sn", 12345);
                    jsonObject.put("isEncode", false);

                    //組合data
                    JSONObject jsonObjectData= new JSONObject();

                    jsonObjectData.put("customer_id", customer_id);
                    jsonObjectData.put("customer_name", CustomerName);
                    jsonObjectData.put("customer_age", iCustomerAge);
                    jsonObjectData.put("customer_gender", CustomerGender);

                    jsonObjectData.put("customer_identityNumber", CustomerIdentityNumber);
                    jsonObjectData.put("customer_phone_number", CustomerPhoneNumber);
                    jsonObjectData.put("customer_address", CustomerAddress);

                    jsonObjectData.put("customer_home_id", iCustomerHomeID);  // 等房屋好了再弄
                    jsonObjectData.put("customer_home_age", iCustomerHomeAge);// 等房屋好了再弄
                    jsonObjectData.put("customer_home_footage", fCustomerHomeFootage);// 等房屋好了再弄
                    jsonObjectData.put("customer_home_price", iCustomerHomePrice);// 等房屋好了再弄

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
                   // mHandler.sendMessage(msg);
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

            String customerList = EzSharedPreferences.readDataString("customerList");
            String key = Integer.toString(position);

            String Data_x = new JSONObject(customerList).getString(key);

            customer_id = new JSONObject(Data_x).getInt("customer_id");
            int user_id = new JSONObject(Data_x).getInt("user_id");
            String nickname = new JSONObject(Data_x).getString("nickname");
            String createtime = new JSONObject(Data_x).getString("createtime");
            String updatetime = new JSONObject(Data_x).getString("updatetime");

            String customer_name = new JSONObject(Data_x).getString("customer_name");
            int icustomer_age = new JSONObject(Data_x).getInt("customer_age");
            String customer_age = Integer.toString(icustomer_age);

            String customer_gender = new JSONObject(Data_x).getString("customer_gender");

            String customer_identityNumber = new JSONObject(Data_x).getString("customer_identityNumber");
            String customer_phone_number = new JSONObject(Data_x).getString("customer_phone_number");
            String customer_address = new JSONObject(Data_x).getString("customer_address");



            int icustomer_home_id = new JSONObject(Data_x).getInt("customer_home_id");
            String customer_home_id = Integer.toString(icustomer_home_id);
            int icustomer_home_age = new JSONObject(Data_x).getInt("customer_home_age");
            String customer_home_age = Integer.toString(icustomer_home_age);

            double fcustomer_home_footage = new JSONObject(Data_x).getDouble("customer_home_footage");
            String customer_home_footage = Double.toString(fcustomer_home_footage);

            String customer_home_price = new JSONObject(Data_x).getString("customer_home_price");
            int ivip_rank = new JSONObject(Data_x).getInt("vip_rank");

            editCustomerName.setText(customer_name);
            editCustomerAge.setText(customer_age);

            int a = customer_gender.compareTo("M");
            int icustomer_gender = 0;
            if(customer_gender.compareTo("M") == 0)  icustomer_gender = 0;
            else                                     icustomer_gender = 1;
            SpinnerCustomerGender.setSelection(icustomer_gender);

            editCustomerIdentityNumber.setText(customer_identityNumber);
            editCustomerPhoneNumber.setText(customer_phone_number);
            editCustomerAddress.setText(customer_address);

            editCustomerHomeID.setText(customer_home_id);
            editCustomerHomeAge.setText(customer_home_age);
            editCustomerHomeFootage.setText(customer_home_footage);
            editCustomerHomePrice.setText(customer_home_price);

            if( ivip_rank >= 0 && ivip_rank < 3 )
                SpinnerVipRank.setSelection(ivip_rank);
            else{
                String str = "錯誤的長度 vip_rank=" +  Integer.toString(ivip_rank);
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
                        Log.d(TAG, "更新顧客成功 str=" + str  );
                        String Data = new JSONObject(str).getString("data");
                        EzLib.setAlertDialog1Event("更新顧客成功", Data);

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
