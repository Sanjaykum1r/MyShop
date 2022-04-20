package com.example.mybookshop.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybookshop.Admin.AdminLoginActivity;
import com.example.mybookshop.Model.Users;
import com.example.mybookshop.Prevalent.Prevalent;
import com.example.mybookshop.R;
import com.example.mybookshop.Sellers.SellerRegistrationActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinButton,loginButton;
    private ProgressDialog loadingBar;
    private TextView sellerBegin,Adminlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        joinButton=findViewById(R.id.main_join_now_btn);
        loginButton=findViewById(R.id.main_login_btn);
        loadingBar=new ProgressDialog(this);
        sellerBegin=findViewById(R.id.seller_begin);
        Adminlayout=findViewById(R.id.admin_panel_link);


        Paper.init(this);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        sellerBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this, SellerRegistrationActivity.class);
                startActivity(intent);
            }
        });



        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        String UserPhoneKey=Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey=Paper.book().read(Prevalent.UserPasswordKey);

        if (UserPhoneKey!=null && UserPasswordKey!=null)
        {
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccess(UserPhoneKey,UserPasswordKey);


                loadingBar.setTitle("Already Logged Account");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }



    }


    private void AllowAccess(String phone, String password) {

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child("Users").child(phone).exists())
                {

                    Users usersData=snapshot.child("Users").child(phone).getValue(Users.class);
                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {

                            Toast.makeText(MainActivity.this,"logged in successfully",Toast.LENGTH_LONG).show();

                            loadingBar.dismiss();
                            Intent intent=new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.currentOnlineUser=usersData;
                            startActivity(intent);

                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this,"Password is incorrect",Toast.LENGTH_LONG).show();

                        }
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Account with this"+phone+"do not exits..",Toast.LENGTH_LONG).show();

                    loadingBar.dismiss();




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}