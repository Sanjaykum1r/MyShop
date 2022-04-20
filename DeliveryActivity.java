package com.example.mybookshop.Buyers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mybookshop.R;

public class DeliveryActivity extends AppCompatActivity {
    private ImageButton codbtn,paytmbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        codbtn=findViewById(R.id.cod_btn);
        paytmbtn=findViewById(R.id.paytm_btn);

        codbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(com.example.mybookshop.Buyers.DeliveryActivity.this,CashActivity.class);
                startActivity(intent);
                finish();
            }
        });
        paytmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(com.example.mybookshop.Buyers.DeliveryActivity.this,PayActivity.class);
                startActivity(intent);


            }
        });
    }
}