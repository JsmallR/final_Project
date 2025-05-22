package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalcuateRateActivity extends AppCompatActivity {

    private EditText inpRMB;
    private TextView tvMoney,tvResultMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calcuate_rate);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvMoney = findViewById(R.id.Money);
        tvResultMoney = findViewById(R.id.resultMoney);

        //获取传入的数据
        Intent intent = getIntent();
        String currencyName = intent.getStringExtra("currency_name");
        tvMoney.setText(String.valueOf(currencyName));

    }

    public void myclick(View btn){
        inpRMB = findViewById(R.id.inpRMB);
        String RMBStr = inpRMB.getText().toString();
        float RMB = Float.parseFloat(RMBStr);

        Intent intent = getIntent();
        String rateStr = intent.getStringExtra("exchange_rate");
        float rate = Float.parseFloat(rateStr);

        float resultMoney = RMB*rate/100;

        tvResultMoney.setText(String.valueOf(resultMoney));
    }
}