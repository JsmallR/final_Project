package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.homework1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button calculate = findViewById(R.id.btn);
        EditText heightInput = findViewById(R.id.inp1);
        EditText weightInput = findViewById(R.id.inp2);
        TextView resultTextView = findViewById(R.id.result);

        calculate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //获取用户输入数据
                String heightStr = heightInput.getText().toString();
                String weightStr = weightInput.getText().toString();

                if(!heightStr.isEmpty()&&!weightStr.isEmpty()){
                    //类型转换 String-->double
                    double height=Double.parseDouble(heightStr);
                    double weight=Double.parseDouble(weightStr);
                    double bmi = weight/(height*height);
                    String advice;
                    if(bmi<18.5){
                        advice="您的体重过轻，建议增加营养、适当锻炼。";
                    }
                    else if(bmi<24){
                        advice="您的体重正常，建议保持健康的生活方式。";
                    }
                    else if(bmi<28){
                        advice="您的体重超重，建议控制饮食、增加运动。";
                    }
                    else{
                        advice="您可能处于肥胖状态，建议咨询医生并制定减肥计划";
                    }
                    String result = String.format("您的BMI值为：%.2f\n%s",bmi,advice);
                    resultTextView.setText(result);
                }
                else{
                    resultTextView.setText("请输入完整的身高和体重信息！");
                }
            }

        });

    }

}