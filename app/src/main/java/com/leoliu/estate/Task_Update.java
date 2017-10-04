package com.leoliu.estate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by liuming on 2017/8/15.
 */

public class Task_Update extends AppCompatActivity {

    static String TAG = "Task_Update";
    private EditText Edit_Task;
    private EditText Edit_Task2;

    Button button_submit;      // 送出
    Context mContext;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_task_update);

        mContext = getApplicationContext();
        Edit_Task = (EditText)findViewById(R.id.editText1);
        Edit_Task2 = (EditText)findViewById(R.id.editText2);

        button_submit = (Button)findViewById(R.id.button_submit);
        button_submit.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {

                String taskStr = Edit_Task.getText().toString();
                String taskStr2 = Edit_Task2.getText().toString();
                Log.d(TAG, "taskStr=" + taskStr );
                Log.d(TAG, "taskStr=" + taskStr );

                Log.d(TAG, "送出");

                Intent intent = new Intent();
                //從MainActivity 到Main2Activity
                intent.setClass(mContext, MainMenu.class);
                //開啟Activity
                mContext.startActivity(intent);
            }

        });
    }
}
