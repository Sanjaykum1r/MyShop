package com.example.mybookshop.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybookshop.Buyers.LoginActivity;
import com.example.mybookshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminRegisterActivity extends AppCompatActivity {
    private Button CreateAdminAccountButton;
    private EditText InputName,InputPhoneNumber,InputPassword;

    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);
        CreateAdminAccountButton=findViewById(R.id.Admin_register_btn);
        InputName=findViewById(R.id.Admin_register_name_input);
        InputPhoneNumber=findViewById(R.id.Admin_register_phone_number_input);
        InputPassword=findViewById(R.id.Admin_register_password_input);
        loadingBar=new ProgressDialog(this);
        CreateAdminAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });


    }

    private void CreateAccount() {

        String name=InputName.getText().toString();
        String phone=InputPhoneNumber.getText().toString();
        String password=InputPassword.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Please write your name...",Toast.LENGTH_LONG).show();

        }

        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"Please write your phone Number...",Toast.LENGTH_LONG).show();

        }

        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please write your Password...",Toast.LENGTH_LONG).show();

        }

        else
        {
            loadingBar.setTitle("Create Admin Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            validatephoneNumber(name,phone,password);



        }
    }
    private void validatephoneNumber(String name,String phone,String password)
    {

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Admins").child(phone).exists()))
                {

                    HashMap<String, Object> userdataMap=new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("password",password);
                    userdataMap.put("name",name);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(AdminRegisterActivity.this,"Congratulations, your account created ",Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();

                                Intent intent=new Intent(AdminRegisterActivity.this, AdminLoginActivity.class);
                                startActivity(intent);

                            }
                            else
                            {
                                loadingBar.dismiss();
                                Toast.makeText(AdminRegisterActivity.this,"Network issues???",Toast.LENGTH_LONG).show();

                            }

                        }
                    });

                }
                else
                {
                    Toast.makeText(AdminRegisterActivity.this,"This"+phone+ "is already Exits",Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    Toast.makeText(AdminRegisterActivity.this,"Please try another phone number",Toast.LENGTH_LONG).show();

                    Intent intent=new Intent(AdminRegisterActivity.this, LoginActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

}
