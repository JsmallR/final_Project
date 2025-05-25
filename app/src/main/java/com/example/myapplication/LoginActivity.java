package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {
    private EditText  etPhone,etCode;
    private Button btnGetCode,btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etPhone = findViewById(R.id.et_phone);
        etCode = findViewById(R.id.et_code);
        btnGetCode = findViewById(R.id.btn_get_code);
        btnLogin = findViewById(R.id.btn_login);

        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerificationCode();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    //获取验证码
    private void getVerificationCode(){
        String phone = etPhone.getText().toString().trim();
        //检查输入手机号是否为空
        if(phone.isEmpty()){
            Toast.makeText(this,"请输入手机号",Toast.LENGTH_SHORT).show();
            return;
        }
        //检查手机号格式是否正确
        if (!Patterns.PHONE.matcher(phone).matches()) {
            Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }

        //发送验证码
        Toast.makeText(this,"验证码已发送",Toast.LENGTH_SHORT).show();
    }

    //登录缴费平台首页
    private void login(){
        String phone = etPhone.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        //检查手机号或验证码是否为空
        if(phone.isEmpty()||code.isEmpty()){
            Toast.makeText(this,"手机号或验证码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        //验证输入的验证码是否正确(此处假设验证码为1234)
        if(!code.equals("1234")){
            Toast.makeText(this,"验证码错误",Toast.LENGTH_SHORT).show();
            return;
        }

        //保存登录状态
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        sp.edit().putBoolean("is_login", true).putString("phone", phone).apply();

        //跳转到主页面(即首页)
        startActivity(new Intent(this,HomePageActivity.class));
        finish();
    }
}