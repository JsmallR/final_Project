package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ScroingActivity extends AppCompatActivity {
    private static final String TAG = "ScoringActivity";
    private TextView scoring1;
    private int team1=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scroing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        scoring1 = findViewById(R.id.teamScore);
        Button reset = findViewById(R.id.btnReset);
        reset.setOnClickListener((v)->{
            team1=0;
            scoring1.setText(String.valueOf(team1));
        });
    }
    public void click(View btn){
        Log.i(TAG,"1111111111111");
        if(btn.getId()==R.id.btnAdd3){
            team1+=3;
        }
        else if(btn.getId()==R.id.btnAdd2){
            team1+=2;
        }
        else if(btn.getId()==R.id.btnAdd1){
            team1+=1;
        }
        else if(btn.getId()==R.id.btnReset){
            team1=0;
        }
        scoring1.setText(String.valueOf(team1));
    }
}