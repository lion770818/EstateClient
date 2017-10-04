package com.leoliu.estate;

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

public class Task_Add extends AppCompatActivity {

    static String TAG = "Task_Add";

    private EditText Edit_Account;
    private EditText Edit_Account2;

    Button button_submit;      // 送出
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_task_add);

        Edit_Account = (EditText)findViewById(R.id.editText1);
        Edit_Account2 = (EditText)findViewById(R.id.editText2);

        button_submit = (Button)findViewById(R.id.button_submit);
        button_submit.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {

                String AccountStr = Edit_Account.getText().toString();
                String AccountStr2 = Edit_Account2.getText().toString();
                Log.d(TAG, "AccountStr=" + AccountStr );
                Log.d(TAG, "AccountStr2=" + AccountStr2 );

                Log.d(TAG, "送出");
            }

        });
    }
}
