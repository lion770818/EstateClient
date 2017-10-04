package com.leoliu.estate;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by liuming on 2017/10/4.
 */

public class EzLib {

    static String TAG = "EzLib";
    private static Activity mActivity;

    public static void onCreate(Activity activity, String activity_name ){

        mActivity = activity;
    }
    //==============================================================================================
    // http://rx1226.pixnet.net/blog/post/305873256-%5Bandroid%5D-10-1-%E5%9F%BA%E7%A4%8Edialog
    public static void setAlertDialog1Event( String Title, String Message ){

        AlertDialog.Builder builder = new AlertDialog.Builder( mActivity );

        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.setPositiveButton("關閉", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}