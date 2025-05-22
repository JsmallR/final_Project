package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;

public class ExchangeRateActivity extends AppCompatActivity implements Runnable {
    private static final String TAG = "ScoringActivity";
    private EditText inputRmb;
    private TextView tvResult;
    float dollarRate=34.5f;
    float euroRate=66.66f;
    float wonRate=78.62f;

    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exchange_rate);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inputRmb = findViewById(R.id.inpRMB);
        tvResult = findViewById(R.id.resultMoney);

        handler = new Handler(){
            public void handleMessage(@NonNull Message msg){
                Log.i(TAG,"handleMessage:接收消息");
                if(msg.what==7){
                    Bundle bdl2 = (Bundle) msg.obj;
                    dollarRate = bdl2.getFloat("web_dollar");
                    euroRate = bdl2.getFloat("web_euro");
                    wonRate = bdl2.getFloat("web_won");
                    Log.i(TAG,"handleMessage: ret_dollar = " + dollarRate);
                    Log.i(TAG,"handleMessage: ret_euro = " + euroRate);
                    Log.i(TAG,"handleMessage: ret_won = " + wonRate);
                }
                super.handleMessage(msg);
            }
        };

        //thread
        Log.i(TAG,"onCreate:启动线程");
        Thread t = new Thread(this);
        t.start();  //this.run()



    }
    public void myclick(View btn){
        Log.i(TAG,"myclick:222222222222");

        String StrInput = inputRmb.getText().toString();

        try {
            float inputf = Float.parseFloat(StrInput);
            float result = 0;
            if(btn.getId()==R.id.btnDollar){
                result = inputf * dollarRate;
            }
            else if(btn.getId()==R.id.btnEuro){
                result = inputf * euroRate;
            }
            else{
                result = inputf * wonRate;
            }
            tvResult.setText(String.valueOf(result));
        } catch (NumberFormatException e) {
            Toast.makeText(this,"请输入正确的数据",Toast.LENGTH_SHORT).show();
        }

    }

    public void openConfig(View btn){
        Intent intent = new Intent(this, ExchangeRateActivity2.class);
        intent.putExtra("key_dollar",dollarRate);
        intent.putExtra("key_euro",euroRate);
        intent.putExtra("key_won",wonRate);

        Log.i(TAG,"openConfig:dollarRate="+dollarRate);
        Log.i(TAG,"openConfig:euroRate="+euroRate);
        Log.i(TAG,"openConfig:wonRate="+wonRate);

        startActivityForResult(intent,6);
    }

    public void run(){
        Log.i(TAG,"run:run.........");
        Bundle retbdl = new Bundle();

        try {
            Thread.sleep(5000);
            try {
//                URL url = null;
//                url = new URL("https://www.boc.cn/sourcedb/whpj/");
//                HttpURLConnection http = (HttpURLConnection) url.openConnection();
//                InputStream in = http.getInputStream();
//                String html = inputStream2String(in);
//                Log.i(TAG,"run:html="+html);

                Document doc = Jsoup.connect("https://www.boc.cn/sourcedb/whpj/").get();
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

                    float r = 100/Float.parseFloat(str2);
                    if("美元".equals(str1)){
                        retbdl.putFloat("web_dollar",r);
                    }
                    else if("欧元".equals(str1)){
                        retbdl.putFloat("web_euro",r);
                    }
                    else if("韩国元".equals(str1)){
                        retbdl.putFloat("web_won",r);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //向主线程发送消息
        Message msg = handler.obtainMessage();
        msg.what=7;
        msg.obj="hello from run";
        handler.sendMessage(msg);
        Log.i(TAG,"run:消息发送完毕");
    }

    private String inputStream2String(InputStream inputStream)
            throws IOException{
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream,"UTF-8");
        while(true){
            int rsz = in.read(buffer,0,buffer.length);
            if(rsz < 0)
                break;
            out.append(buffer,0,rsz);
        }
        return out.toString();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if(requestCode==6 && resultCode==3){
            //从data获取带回的数据
            Bundle ret = data.getExtras();
            dollarRate=ret.getFloat("ret_dollar");
            euroRate=ret.getFloat("ret_euro");
            wonRate=ret.getFloat("ret_won");

            Log.i(TAG,"onActivityResult:dollarRate="+dollarRate);
            Log.i(TAG,"onActivityResult:euroRate="+euroRate);
            Log.i(TAG,"onActivityResult:wonRate="+wonRate);
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

}