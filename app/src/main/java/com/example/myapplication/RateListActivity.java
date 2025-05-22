package com.example.myapplication;

import android.app.ListActivity;
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
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class RateListActivity extends ListActivity {
    private static final String TAG = "RateActivity";
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

            List<String> rateList = new ArrayList<String>();
            try {
                Document doc = Jsoup.connect("https://www.huilvbiao.com/bank/spdb").get();
                Log.i(TAG,"run:title ="+doc.title());

                Elements tbodys = doc.getElementsByTag("tbody");
                Element tbody = tbodys.get(0);
                Log.i(TAG,"run:table2="+tbody);
                Elements trs = tbody.getElementsByTag("tr");
                for(Element tr:trs){
                    Element currencyName = tr.selectFirst("th.table-coin a.coin span");
                    Elements tds = tr.select("td");
                    Element td2 = tds.get(0);
                    Element td3 = tds.get(1);
                    String str1 = currencyName.text();
                    String str2 = td2.text();
                    String str3 = td3.text();
                    float buyRate = Float.parseFloat(str2);
                    float soldRate = Float.parseFloat(str3);
                    float rate = (buyRate + soldRate) / 2.0f;
                    String rateStr = String.valueOf(rate);

                    rateList.add(str1+"==>"+rateStr);

                    Log.i(TAG,"run:"+str1+"==>"+rateStr);

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }

            Log.i(TAG,"onCreate:返回数据");
            Message msg = handler.obtainMessage(9,rateList);
            msg.obj=rateList;
            handler.sendMessage(msg);
        });
        t.start();
    }
}





































