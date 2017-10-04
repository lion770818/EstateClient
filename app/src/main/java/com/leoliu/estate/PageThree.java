package com.leoliu.estate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 2016/7/9.
 */

public class PageThree extends PageView {

    static String TAG = "PageThree";

    Button button_addHouse;      // 新增房屋

    Context mContext;

    public ListView listView;

    private List Houselist = new ArrayList();
    private ArrayAdapter<String> listAdapter;

    public PageThree(Context context) {
        super(context);
        mContext = context;

        View view = LayoutInflater.from(context).inflate(R.layout.pager_item3, null);
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText("Page Three");
        addView(view);


        Houselist.add("房屋1");
        Houselist.add("房屋2");
        Houselist.add("房屋3");
        Houselist.add("房屋4");
        Houselist.add("房屋5");
        Houselist.add("房屋6");
        Houselist.add("房屋7");

        button_addHouse = (Button) findViewById(R.id.button_addHouse);
        button_addHouse.setOnClickListener(new Button.OnClickListener(){

            @Override

            public void onClick(View v) {

                Intent intent = new Intent();
                //從MainActivity 到Main2Activity
                intent.setClass(mContext , House_Add.class);

                //開啟Activity
                mContext.startActivity(intent);
                //startActivity(intent);
            }

        });

        listView = (ListView) findViewById(R.id.listView);
        //找到ListView


        // RadioButton Layout 樣式 : android.R.layout.simple_list_item_single_choice
        // CheckBox Layout 樣式    : android.R.layout.simple_list_item_multiple_choice
        listAdapter = new ArrayAdapter(mContext,android.R.layout.simple_list_item_single_choice,Houselist);
        listView.setAdapter(listAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(mContext, "你選擇的是" + Tasklist[position], Toast.LENGTH_SHORT).show();
                normalDialogEvent("工作項目", (String)Houselist.get(position), position );

            }
        });
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
                        Intent intent = new Intent();
                        //從MainActivity 到Main2Activity
                        intent.setClass(mContext, House_Update.class);
                        //開啟Activity
                        mContext.startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "點到刪除", Toast.LENGTH_SHORT).show();

                        String item = (String) listAdapter.getItem(position);
                        listAdapter.remove(item);
                        listAdapter.notifyDataSetChanged();
                    }
                })
                .setPositiveButton(R.string.cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "點到取消", Toast.LENGTH_SHORT).show();

                        Houselist.add("新房屋1");
                        Houselist.add("新房屋2");
                        Houselist.add("新房屋3");
                        Log.d(TAG, "Houselist size=" + Houselist.size() );
                        listAdapter = new ArrayAdapter(mContext,android.R.layout.simple_list_item_single_choice,Houselist);
                        listView.setAdapter(listAdapter);
                        //listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    }
                })
                .show();
    }

    @Override
    public void refresh() {

    }
}
