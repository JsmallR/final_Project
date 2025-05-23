package com.example.myapplication;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RateListActivity extends ListActivity {
    private static final String TAG = "RateActivity";
    Handler handler;
    private String logDate = "";
    private final String DATE_SP_KEY = "lastRateDateStr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
        logDate = sp.getString(DATE_SP_KEY,"");
        Log.i("List","lastRateDateStr="+logDate);

        handler = new Handler(Looper.getMainLooper()){
            public void handleMessage(@NonNull Message msg){
                if(msg.what==9){
                    List<String> list2 = (List<String>) msg.obj;
                    ListAdapter adapter2 = new ArrayAdapter<String>(RateListActivity.this,android.R.layout.simple_list_item_1,list2);
                    setListAdapter(adapter2);
                }
                super.handleMessage(msg);
            }
        };


        Thread t =new Thread(()->{
            //获取数据，带回到主线程
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            List<String> retlist = new ArrayList<String>();

            String curDateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());

            Log.i("run","curDateStr:"+curDateStr+"logDate:"+logDate);
            if(curDateStr.equals(logDate)){
                Log.i("run","日期相等，从数据库中获取数据");
                RateManager rateManager = new RateManager(RateListActivity.this);
                for(RateItem rateItem:rateManager.listAll()){
                    retlist.add(rateItem.getCurName()+"=>"+rateItem.getCurRate());
                }
            }
            else{
                Document doc = null;
                Log.i("run","日期不相等，从网络中获取在线数据");

                try {
                    List<RateItem> rateList = new ArrayList<>();
                    doc = Jsoup.connect("https://www.boc.cn/sourcedb/whpj/").get();
                    Log.i(TAG,"run:title ="+doc.title());

                    Elements tables = doc.getElementsByTag("table");
                    Element table = tables.get(1);
                    Log.i(TAG,"run:table2="+table);
                    Elements trs = table.getElementsByTag("tr");
                    trs.remove(0);
                    for(Element tr:trs){
                        Elements tds = tr.children();
                        Element td1 = tds.first();
                        Element td2 = tds.get(5);
                        String str1 = td1.text();
                        String str2 = td2.text();

                        Log.i(TAG,"run:"+str1+"==>"+str2);
                        retlist.add(str1+"-->"+str2);

                        RateItem rateItem = new RateItem(str1,str2);
                        rateList.add(rateItem);
                    }

                    RateManager rateManager = new RateManager(RateListActivity.this);
                    rateManager.deleteAll();
                    Log.i("rate","删除所有记录");
                    rateManager.addALL(rateList);
                    Log.i("rate","添加新记录集");

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                //更新记录日期
                SharedPreferences.Editor edit = sp.edit();
                edit.putString(DATE_SP_KEY,curDateStr);
                edit.commit();
                Log.i("run","更新日期结束"+curDateStr);
            }

            Log.i(TAG,"onCreate:返回数据");
            Message msg = handler.obtainMessage();
            msg.obj=retlist;
            handler.sendMessage(msg);
        });
        t.start();
    }
}





































