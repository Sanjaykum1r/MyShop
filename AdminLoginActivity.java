package com.example.mybookshop.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybookshop.Model.Users;
import com.example.mybookshop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class AdminLoginActivity extends AppCompatActivity {
    private EditText InputPhoneNumber,InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private String parentDbName="Users";
    private TextView AdminRegisterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        LoginButton=findViewById(R.id.Admin_login_btn);
        InputPhoneNumber=findViewById(R.id.Admin_login_phone_number_input);
        InputPassword=findViewById(R.id.Admin_login_password_input);
        loadingBar=new ProgressDialog(this);
        AdminRegisterText=findViewById(R.id.Admin_register_Activity);


        AdminRegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminLoginActivity.this, AdminRegisterActivity.class);
                startActivity(intent);
            }
        });

        Paper.init(this);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
    }

    private void LoginUser() {

        String phone=InputPhoneNumber.getText().toString();
        String password=InputPassword.getText().toString();

        if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"Please write your phone Number...",Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please write your Password...",Toast.LENGTH_LONG).show();
        }
        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone,password);

        }

    }

    private void AllowAccessToAccount(String phone, String password) {


        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDbName).child(phone).exists())
                {

                    Users usersData=snapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            Toast.makeText(AdminLoginActivity.this,"logged in successfully",Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                            Intent intent=new Intent(AdminLoginActivity.this, AdminHomeActivity.class);


                            startActivity(intent);
                        }


                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(AdminLoginActivity.this,"Password is incorrect",Toast.LENGTH_LONG).show();

                        }
                    }

                }
                else
                {
                    Toast.makeText(AdminLoginActivity.this,"Account with this"+phone+"do not exits..",Toast.LENGTH_LONG).show();

                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}