package com.leoliu.estate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Created by liuming on 2017/8/15.
 */

public class House_Add extends AppCompatActivity {

    static String TAG = "House_Add";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_house_add);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {   //確定按下退出鍵

            //ConfirmExit(); //呼叫ConfirmExit()函數
            Log.d(TAG, "onKeyDown keyCode=" + keyCode);
            setResult(RESULT_OK);
            finish();
            return true;

        }

        return super.onKeyDown(keyCode, event);

    }
}
