package com.example.mybookshop.Buyers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mybookshop.R;

public class CodActivity extends AppCompatActivity {
    private Button homepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_method);
        homepage=findViewById(R.id.homepage);


        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(com.example.mybookshop.Buyers.CodActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}