package com.example.mybookshop.Buyers;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybookshop.Prevalent.Prevalent;
import com.example.mybookshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText nameEditText,phoneEditText,addressEditText,cityEditText;
    private Button confirnOrderbtn;
    private String totalAmount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        totalAmount=getIntent().getStringExtra("Total Price = ");
        Toast.makeText(this,"Total Price = "+totalAmount,Toast.LENGTH_SHORT).show();

        setContentView(R.layout.activity_confirm_final_order);
        confirnOrderbtn=findViewById(R.id.confirm_final_order_btn);
        nameEditText=findViewById(R.id.shipment_name);
        phoneEditText=findViewById(R.id.shipment_phone_number);
        addressEditText=findViewById(R.id.shipment_address);
        cityEditText=findViewById(R.id.shipment_city);

        confirnOrderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check();

            }
        });



    }

    private void Check() {
        if (TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(this,"Please Provide your full name",Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            Toast.makeText(this,"Please Provide your phone number",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this,"Please Provide your Home address",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(this,"Please Provide your City Name",Toast.LENGTH_SHORT).show();
        }
        else
        {
            ConfirmOrder();
        }


    }

    private void ConfirmOrder() {
        final String saveCurrentTime,saveCurrentDate;
        Calendar calForDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calForDate.getTime());
        final DatabaseReference ordersRef= FirebaseDatabase.getInstance()
                .getReference().child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());
        HashMap<String,Object> carMap=new HashMap<>();
        carMap.put("TotalAmount",totalAmount);
        carMap.put("Name",nameEditText.getText().toString());
        carMap.put("phone",phoneEditText.getText().toString());
        carMap.put("address",addressEditText.getText().toString());
        carMap.put("City",cityEditText.getText().toString());
        carMap.put("date",saveCurrentDate);
        carMap.put("Time",saveCurrentTime);
        carMap.put("state","Not Shipped");
        ordersRef.updateChildren(carMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User List")
                            .child("User View")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful())
                                    {
                                       // Toast.makeText(ConfirmFinalOrderActivity.this,"Congratulation Your Final Order has been placed successfully",Toast.LENGTH_SHORT).show();

                                        Intent intent=new Intent(com.example.mybookshop.Buyers.ConfirmFinalOrderActivity.this,DeliveryActivity .class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }

            }
        });
    }
}