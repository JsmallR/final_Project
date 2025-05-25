package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class QueryActivity extends AppCompatActivity {
    private EditText etAccount;
    private Button btnQuery;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_query);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etAccount = findViewById(R.id.et_account);
        btnQuery = findViewById(R.id.btn_query);
        tvResult = findViewById(R.id.tv_result);

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryElectricityBill();
            }
        });
    }

    //查询电费
    private void queryElectricityBill(){
        String account = etAccount.getText().toString().trim();
        //检查是否输入户号
        if(account.isEmpty()){
            Toast.makeText(this,"请输入户号",Toast.LENGTH_SHORT).show();
        }

        tvResult.setText("查询中. . .");
        btnQuery.setEnabled(false); //禁用按钮防止重复点击

        //生成随机电费数据
        generateAndShowBillData(account);

        btnQuery.setEnabled(true); //重新启用按钮
    }

    //生成并显示电费数据
    private void generateAndShowBillData(String account){
        double currentBill = 50 + Math.random() * 300;  //50~350元
        int usage = 100 + new Random().nextInt(300); //100！400度
        String dueDate = "2025-06-09"; //随机固定日期
        String result = "户号：" +account +"\n" +
                        "当前电费：" + String.format("%.2f", currentBill) + "元\n" +
                        "本月用电量：" + usage + "度\n" +
                        "缴费截止日期：" + dueDate;
        tvResult.setText(result);
    }
}