package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CourtCounterActivity extends AppCompatActivity {
    private static final String TAG = "CourtCounterActivity";
    private TextView scoring1;
    private int team1=0;
    private TextView scoring2;
    private int team2=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_court_counter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        scoring1 = findViewById(R.id.teamScore1);
        scoring2 = findViewById(R.id.teamScore2);
        Button reset = findViewById(R.id.btnReset);
        reset.setOnClickListener((v)->{
            team1=0;
            scoring1.setText(String.valueOf(team1));
            team2=0;
            scoring2.setText(String.valueOf(team2));
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
        else if(btn.getId()==R.id.btnA){
            team1+=1;
        }
        else if(btn.getId()==R.id.btnAdd3_2){
            team2+=3;
        }
        else if(btn.getId()==R.id.btnAdd2_2){
            team2+=2;
        }
        else if(btn.getId()==R.id.btnB){
            team2+=1;
        }
        else if(btn.getId()==R.id.btnReset){
            team1=0;
            team2=0;
        }
        scoring1.setText(String.valueOf(team1));
        scoring2.setText(String.valueOf(team2));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("key1",team1);
        outState.putInt("key2",team2);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        team1 = savedInstanceState.getInt("key1");
        team2 = savedInstanceState.getInt("key2");
        scoring1.setText(String.valueOf(team1));
        scoring2.setText(String.valueOf(team2));
    }
}