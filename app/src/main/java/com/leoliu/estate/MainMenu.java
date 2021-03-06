package com.leoliu.estate;

/**
 * Created by actio on 2017/4/13.
 */

import android.content.Intent;

import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.support.design.widget.TabLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//===============================================================================================================
// 頁面
interface PAGE_KIND {

    public static final int PAGE_KIND_TASK = 0;         // 工作清單頁面
    public static final int PAGE_KIND_CUSTOMER = 1;     // 顧客頁面
    public static final int PAGE_KIND_HOME = 2;         // 房屋頁面
    public static final int PAGE_KIND_MEMBER = 3;       // 會員頁面

}
//==================================================================================================
//
public class MainMenu extends AppCompatActivity {

    // 如何使用 ViewPager + TabLayout http://givemepass.blogspot.tw/2016/07/viewpagertablayout_9.html
    // 如何使用 ViewPager + TabLayout http://givemepass.blogspot.tw/2016/07/viewpagertablayout_9.html
    // 在Android应用程序使用YouTube API来嵌入视频 http://www.bkjia.com/Androidjc/1100111.html
    // 小黑人Fragment取代TabActivity實作Tab分頁標籤 http://dean-android.blogspot.tw/2015/01/androidfragmenttabactivitytab.html
    // http://givemepass.blogspot.tw/2016/07/viewpager.html

    static String TAG = "MainMenu";
    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private List<PageView> pageList;
    private Boolean [] RefershPageFlag = {false,false,false,false}; // 四個分頁
    Bundle savedInstanceStateBk;

    private static TabLayout.Tab tab;
    private static TabLayout.Tab tab1;
    private static TabLayout.Tab tab2;
    private static TabLayout.Tab tab3;

    //==============================================================================================
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        savedInstanceStateBk = savedInstanceState;
        initData();
        initView();

    }

    //==============================================================================================
    //
    private void initData() {
        pageList = new ArrayList<>();
        pageList.add(new PageOne(MainMenu.this, savedInstanceStateBk));     // 工作管理
        pageList.add(new PageTwo(MainMenu.this));                           // 顧客管理
        pageList.add(new PageThree(MainMenu.this));
        pageList.add(new PageFour(MainMenu.this,savedInstanceStateBk));


        EzSharedPreferences.onCreate(this,TAG);
        EzNetWork.onCreate(this,TAG);
        EzLib.onCreate(this,TAG);

        String Account = EzSharedPreferences.readDataString("Account");
        String Name = EzSharedPreferences.readDataString("Name");
        int UID = EzSharedPreferences.readDataInt("UID");

    }

    //==============================================================================================
    //
    private void initView() {
        mTablayout = (TabLayout) findViewById(R.id.tabs);
        //mTablayout.addTab(mTablayout.newTab().setText("輸入會員資料"));
        tab = mTablayout.newTab();
        tab.setText("工作管理");
        tab.setTag(PAGE_KIND.PAGE_KIND_TASK);
        tab.setIcon( R.drawable.notebook );
        mTablayout.addTab( tab );


        tab1 = mTablayout.newTab();
        tab1.setText("客戶管理");
        tab1.setTag(PAGE_KIND.PAGE_KIND_CUSTOMER);
        tab1.setIcon( R.drawable.member );
        mTablayout.addTab( tab1 );


        tab2 = mTablayout.newTab();
        tab2.setText("房屋管理");
        tab2.setTag(PAGE_KIND.PAGE_KIND_HOME);
        tab2.setIcon( R.drawable.home );
        mTablayout.addTab( tab2 );

        tab3 = mTablayout.newTab();
        tab3.setText("員工管理");
        tab3.setTag(PAGE_KIND.PAGE_KIND_MEMBER);
        tab3.setIcon( R.drawable.member );
        mTablayout.addTab( tab3 );

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        initListener();

        // 預設選到0
        int PageIdx = PAGE_KIND.PAGE_KIND_TASK;
        if(PageIdx == PAGE_KIND.PAGE_KIND_TASK )
        {
            RefershPageFlag[PageIdx] = true;
            RefreshPageData(PageIdx); // 第0頁 不會觸發onTabSelected事件, 所以直接呼叫RefreshPageData
        }else{
            mViewPager.setCurrentItem(PageIdx);
        }


    }

    //==============================================================================================
    // 送出封包 更新該頁面資訊
    private  void RefreshPageData( int PageIdx ){

        Log.d(TAG, "RefreshPageData(送出封包 更新該頁面資訊) PageIdx=" + PageIdx);
        try
        {
            // 送出封包 更新該頁面資訊
            PageView view = pageList.get(PageIdx);
            view.refresh();
        }
        catch (Exception ex)
        {
            Log.d(TAG, "例外 msg=" + ex.getMessage());
            Log.d(TAG, "例外 msg=" + ex.toString());
        }
    }
    //==============================================================================================
    //
    private void initListener() {
        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG,"onTabSelected 被選擇到 tag=" + tab.getTag());
                //mViewPager.setBackgroundResource(R.drawable.selector_tab_background);

                int PageIdx = tab.getPosition();
                mViewPager.setCurrentItem(PageIdx);

                if( RefershPageFlag[PageIdx] == false)
                {
                    RefershPageFlag[PageIdx] = true;
                    RefreshPageData(PageIdx);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d(TAG,"onTabUnselected tag=" + tab.getTag());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d(TAG,"onTabReselected tag=" + tab.getTag());
            }
        });
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
    }

    //==============================================================================================
    //
    private class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pageList.get(position));
            return pageList.get(position);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    //==============================================================================================
    // 封包事件接收函式
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            Log.d(TAG, "handleMessage msg=" + msg);
            //Loadingdialog.dismiss();

            try {

                switch(msg.what){
                    case ERROR_CODE.ERROR_CODE_SUCCESS:

                        String str = (String)msg.obj;
                        Log.d(TAG, "取得資料成功 str=" + str  );
                        String Data = new JSONObject(str).getString("data");

                        String Name = new JSONObject(Data).getString("ip");
                        String NickName = new JSONObject(Data).getString("phone_number");
                        //JSONArray numberLis = new JSONObject(str).getJSONArray("Data");
                        //String Name = numberLis.getJSONObject(0).getString("Name");
                        //GotoMainMenu();
                        break;

                    default:
                        Log.d(TAG, " 未處理的 msg=" + msg);
                        //setAlertDialog1Event(msg.toString());
                        break;
                }

            }catch (Exception ex)
            {
                Log.d(TAG, "例外 msg=" + ex.getMessage());
                Log.d(TAG, "例外 msg=" + ex.toString());
                //setAlertDialog1Event(ex.toString());
            }

        }
    };

    //==============================================================================================
    // 當玩家按下返回按鈕時候
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult requestCode=" + requestCode + " resultCode=" + resultCode );


        EzLib.onCreate(this,TAG);

        // 送出封包 更新該頁面UI
        RefreshPageData(resultCode);

    }
}
