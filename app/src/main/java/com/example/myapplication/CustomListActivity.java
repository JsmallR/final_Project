package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{

    private static final String TAG = "CustomListActivity";
    private ListView mylist;
    Handler handler;
    ProgressBar progressBar;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_custom_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ArrayList<HashMap<String, String>> listItems = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", "Rate: " +i); // 标题文字
            map.put("ItemDetail", "detail:" +i); // 详情描述
            listItems.add(map);
        }

        // 生成适配器的 Item 和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,
                listItems, // ListItems 数据源
                R.layout.list_item, // ListItem 的 XML 布局实现
                new String[] { "ItemTitle", "ItemDetail" },
                new int[] { R.id.itemTitle, R.id.itemDetail }
        );

        handler = new Handler(Looper.getMainLooper()){
            public void handleMessage(@NonNull Message msg){
                if(msg.what==3){
                    Log.i(TAG,"handleMessage:获得网络数据");
                    ArrayList<HashMap<String, String>> list2 = (ArrayList<HashMap<String, String>>) msg.obj;
                    adapter = new MyAdapter(CustomListActivity.this,R.layout.list_item,list2);
                    mylist.setAdapter(adapter);

                    //隐藏进度条
                    progressBar.setVisibility(View.GONE);
                }
                super.handleMessage(msg);
            }
        };

        MyAdapter myAdapter = new MyAdapter(this,R.layout.list_item,listItems);

        progressBar = findViewById(R.id.progressBar);

        mylist = findViewById(R.id.mylist2);
        mylist.setAdapter(myAdapter);
        mylist.setOnItemClickListener(this);
        mylist.setOnItemLongClickListener(this);

        //启动线程
        new Thread(()->{
            Document doc = null;
            ArrayList<HashMap<String, String>> retlist = new ArrayList<HashMap<String, String>>();
            try {
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

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("ItemTitle",str1);
                    map.put("ItemDetail",str2);
                    retlist.add(map);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Message msg = handler.obtainMessage(3,retlist);
            handler.sendMessage(msg);
            Log.i(TAG,"onCreate:返回数据");

        }).start();
    }

    //单击处理事件
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.i(TAG,"onItemClick:");
        Object itemAtPosition = mylist.getItemAtPosition(position);
        HashMap<String,String> map = (HashMap<String, String>) itemAtPosition;
        String titlestr = map.get("ItemTitle");
        String detailStr = map.get("ItemDetail");
        Log.d(TAG, "onItemClick: titlestr=" + titlestr);
        Log.d(TAG, "onItemClick: detailStr=" + detailStr);

        //打开计算窗口
        Intent intent = new Intent(CustomListActivity.this, CalcuateRateActivity.class);
        intent.putExtra("currency_name", titlestr);
        intent.putExtra("exchange_rate", detailStr);
        startActivity(intent);

        //删除数据项
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否删除当前数据")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "onClick:对话框事件处理");
                        adapter.remove(mylist.getItemAtPosition(position));
                    }
                }).setNegativeButton("否", null);
        builder.create().show();
    }

    //长按处理事件
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG,"onItemLongClick:");
        //adapter.remove(mylist.getItemAtPosition(position)); //此处是实现长按删除，单击删除要放在单击处理事件中
        return false;  //设置为false时长按结束单击事件也会进行，设置为true时单击事件不会进行
    }




}