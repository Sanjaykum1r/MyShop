package com.example.mybookshop.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mybookshop.Buyers.HomeActivity;
import com.example.mybookshop.Buyers.MainActivity;
import com.example.mybookshop.R;

public class AdminHomeActivity extends AppCompatActivity {

    private Button maintainProductsBtn,LogoutBtn,checkOrdersBtn,checkandapprovebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        LogoutBtn=findViewById(R.id.admin_logout_btn);
        checkOrdersBtn=findViewById(R.id.check_orders_btn);
        maintainProductsBtn=findViewById(R.id.maintain_btn);
        checkandapprovebtn=findViewById(R.id.check_approve_orders_btn);

        checkandapprovebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHomeActivity.this,AdminCheckNewProductActivity.class);
                startActivity(intent);
                finish();
            }
        });



        maintainProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomeActivity.this, HomeActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);
                finish();
            }
        });
        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        checkOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomeActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });

    }
}