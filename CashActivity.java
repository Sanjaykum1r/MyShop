package com.example.mybookshop.Buyers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mybookshop.R;

public class CashActivity extends AppCompatActivity {

    private Button homepage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);
        homepage=findViewById(R.id.homepage);


        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(com.example.mybookshop.Buyers.CashActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}