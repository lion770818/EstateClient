package com.leoliu.estate;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.id.list;

/**
 * Created by rick on 2016/7/9.
 */
/*
public class MainActivity extends FragmentActivity implements
        ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener {
 */

// 如何動態增減自訂ListView http://givemepass.blogspot.tw/2011/10/listview.html
// ArrayAdapter的使用 http://hhliu-skills.blogspot.tw/2012/04/arrayadapter.html
public class PageOne extends PageView  {

    static String TAG = "PageOne";

    Button button_addTask;      // 新增工作

    Context mContext;

    public ListView listView;

    private List Tasklist = new ArrayList();
    private ArrayAdapter<String> listAdapter;

    public PageOne(Context context, Bundle savedInstanceStateBk) {

        super(context);
        mContext = context;

        View view = LayoutInflater.from(context).inflate(R.layout.pager_item, null);
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText("請選擇要執行的項目");
        addView(view);

        Tasklist.add("工作1");
        Tasklist.add("工作2");
        Tasklist.add("工作3");
        Tasklist.add("工作4");
        Tasklist.add("工作5");
        Tasklist.add("工作6");
        Tasklist.add("工作7");

        button_addTask = (Button) findViewById(R.id.button_addTask);
        button_addTask.setOnClickListener(new Button.OnClickListener(){

            @Override

            public void onClick(View v) {

                Intent intent = new Intent();
                //從MainActivity 到Main2Activity
                intent.setClass(mContext , Task_Add.class);

                //開啟Activity
                mContext.startActivity(intent);
                //startActivity(intent);
            }

        });

        listView = (ListView) findViewById(R.id.listView);
        //找到ListView


        // RadioButton Layout 樣式 : android.R.layout.simple_list_item_single_choice
        // CheckBox Layout 樣式    : android.R.layout.simple_list_item_multiple_choice
        listAdapter = new ArrayAdapter(mContext,android.R.layout.simple_list_item_single_choice,Tasklist);
        listView.setAdapter(listAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(mContext, "你選擇的是" + Tasklist[position], Toast.LENGTH_SHORT).show();


                //String item = (String) listAdapter.getItem(position);
                //listAdapter.remove(item);
                //listAdapter.notifyDataSetChanged();
                normalDialogEvent("工作項目", (String)Tasklist.get(position), position );
                /*
                switch (position){
                    case 0: {
                        Intent intent = new Intent();
                        //從MainActivity 到Main2Activity
                        intent.setClass(mContext, Task_Add.class);
                        //intent.setComponent(new ComponentName(Context, Member_Add.class));
                        //開啟Activity
                        mContext.startActivity(intent);
                        //startActivity(intent);
                    }
                        break;
                    case 1: {
                        Intent intent = new Intent();
                        //從MainActivity 到Main2Activity
                        intent.setClass(mContext, Task_Update.class);
                        //開啟Activity
                        mContext.startActivity(intent);
                    }
                        break;
                    case 2: {
                        Intent intent = new Intent();
                        //從MainActivity 到Main2Activity
                        intent.setClass(mContext , Task_Delete.class);
                        //開啟Activity
                        mContext.startActivity(intent);
                    }
                        break;
                    case 3:{

                    }
                        break;
                    default:

                        break;
                }
                */
            }
        });

        //將ListAdapter設定至ListView裡面
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
                        intent.setClass(mContext, Task_Update.class);
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

                        Tasklist.add("新工作1");
                        Tasklist.add("新工作2");
                        Tasklist.add("新工作3");
                        Log.d(TAG, "Tasklist size=" + Tasklist.size() );
                        listAdapter = new ArrayAdapter(mContext,android.R.layout.simple_list_item_single_choice,Tasklist);
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
