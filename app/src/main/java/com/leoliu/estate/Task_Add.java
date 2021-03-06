package com.leoliu.estate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

/**
 * Created by liuming on 2017/8/15.
 */

public class Task_Add extends AppCompatActivity {

    static String TAG = "Task_Add";
    private static Context context;
    private ProgressDialog Loadingdialog;

    private static EditText Edit_TaskName;
    private static EditText Edit_TaskDescribe;
    private static EditText Edit_Memo;

    Button button_submit;      // 送出
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_task_add);
        context = this;
        EzLib.onCreate(this,TAG);

        Edit_TaskName = (EditText)findViewById(R.id.editTaskName);
        Edit_TaskDescribe = (EditText)findViewById(R.id.editTaskDescribe);
        Edit_Memo = (EditText)findViewById(R.id.editMemo);

        button_submit = (Button)findViewById(R.id.button_submit);
        button_submit.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {



                try {

                    Loadingdialog = ProgressDialog.show(context, "傳送資料中", "請耐心等待3秒...",true);

                    int user_id = EzSharedPreferences.readDataInt("user_id");
                    String nickname = EzSharedPreferences.readDataString("nickname");
                    Log.d(TAG, "user_id=" + user_id );
                    Log.d(TAG, "nickname=" + nickname );

                    String TaskName = Edit_TaskName.getText().toString();
                    String TaskDescribe = Edit_TaskDescribe.getText().toString();
                    String Memo = Edit_Memo.getText().toString();
                    Log.d(TAG, "TaskName=" + TaskName );
                    Log.d(TAG, "TaskDescribe=" + TaskDescribe );
                    Log.d(TAG, "Memo=" + Memo );


                    JSONObject jsonObject= new JSONObject();
                    jsonObject.put("sys", "system");
                    jsonObject.put("cmd", NET_CMD.NET_CMD_TASK_INSERT);
                    jsonObject.put("sn", 12345);
                    jsonObject.put("isEncode", false);

                    //組合data
                    JSONObject jsonObjectData= new JSONObject();
                    jsonObjectData.put("user_id", user_id);
                    jsonObjectData.put("nickname", nickname);
                    jsonObjectData.put("task_name", TaskName);
                    jsonObjectData.put("task_describe", TaskDescribe);
                    jsonObjectData.put("memo", Memo);

                    String Data = jsonObjectData.toString();

                    jsonObject.put("data", Data);
                    String jsonStr = jsonObject.toString();
                    EzWebsocket.SendMessage(jsonStr, mHandler);

                    Log.d(TAG, "送出");

                }catch (Exception ex)
                {
                    ex.printStackTrace();
                    Loadingdialog.dismiss();
                    EzLib.setAlertDialog1Event("錯誤", "輸入資料格式錯誤");
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

                switch(msg.what){
                    case ERROR_CODE.ERROR_CODE_SUCCESS:

                        String str = (String)msg.obj;
                        Log.d(TAG, "新增工作項目成功 str=" + str  );
                        String Data = new JSONObject(str).getString("data");

                        EzLib.setAlertDialog1Event("新增工作項目成功", Data);

                        // 清除輸入的資料
                        Edit_TaskName.setText("");
                        Edit_TaskDescribe.setText("");
                        Edit_Memo.setText("");

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

    //==============================================================================================
    // 確定按下退出鍵
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {   //確定按下退出鍵

            //ConfirmExit(); //呼叫ConfirmExit()函數
            Log.d(TAG, "onKeyDown keyCode=" + keyCode);
            setResult(0);  // 0:工作 1:客戶 2:房屋物件 3:員工
            finish();
            return true;

        }

        return super.onKeyDown(keyCode, event);

    }
}
