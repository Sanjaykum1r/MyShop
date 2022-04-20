package com.example.mybookshop.Buyers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybookshop.Prevalent.Prevalent;
import com.example.mybookshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {
    private String check = "";
    private TextView pageTitle, titleQuestion;
    private EditText phoneNumber, Question1, Question2;
    private Button verifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        check = getIntent().getStringExtra("check");
        pageTitle = findViewById(R.id.page_title);

        titleQuestion = findViewById(R.id.title_question);

        phoneNumber = findViewById(R.id.find_phone_number);

        Question1 = findViewById(R.id.question1);

        Question2 = findViewById(R.id.question2);

        verifyButton = findViewById(R.id.verify_btn);


    }

    @Override
    protected void onStart() {
        super.onStart();

        phoneNumber.setVisibility(View.GONE);


        if (check.equals("settings")) {

            pageTitle.setText("Set Questions");
            titleQuestion.setText("Please set Answer for the Following Security Questions?");
            verifyButton.setText("Set");

            displayPreviousAnswers();


            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    setAnswers();

                }
            });
        }
        else if (check.equals("login"))
        {
            phoneNumber.setVisibility(View.VISIBLE);
            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verifyUser();
                }
            });
        }

    }

    private void verifyUser()
    {
        final String phone=phoneNumber.getText().toString();
        final String answer1=Question1.getText().toString().toLowerCase();
        final String answer2=Question2.getText().toString().toLowerCase();

        if (!phone.equals("") && !answer1.equals("") && !answer2.equals(""))
        {

            final DatabaseReference ref= FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists())
                    {

                        String mphone=snapshot.child("phone").getValue().toString();
                        if (snapshot.hasChild("Security Questions"))
                        {
                            String ans1=snapshot.child("Security Questions")
                                    .child("answer1").getValue().toString();
                            String ans2=snapshot.child("Security Questions")
                                    .child("answer2").getValue().toString();

                            if (!ans1.equals(answer1) )
                            {
                                Toast.makeText(com.example.mybookshop.Buyers.ResetPasswordActivity.this,"your 1st Answer is wrong",Toast.LENGTH_LONG).show();
                            }
                            else if (!ans2.equals(answer2))
                            {
                                Toast.makeText(com.example.mybookshop.Buyers.ResetPasswordActivity.this,"your 2nd Answer is wrong",Toast.LENGTH_LONG).show();

                            }
                            else
                            {

                                AlertDialog.Builder builder=new AlertDialog.Builder(com.example.mybookshop.Buyers.ResetPasswordActivity.this);
                                builder.setTitle("New Password");
                                final EditText newPassword=new EditText(com.example.mybookshop.Buyers.ResetPasswordActivity.this);
                                newPassword.setHint("Write New Password here...");
                                builder.setView(newPassword);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (!newPassword.getText().toString().equals(""))
                                        {

                                            ref.child("password")
                                                    .setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful())
                                                            {
                                                                Toast.makeText(com.example.mybookshop.Buyers.ResetPasswordActivity.this,"Password Change successfully...",Toast.LENGTH_LONG).show();

                                                                Intent intent=new Intent(com.example.mybookshop.Buyers.ResetPasswordActivity.this, LoginActivity.class);

                                                                startActivity(intent);

                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                                builder.setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                builder.show();
                            }
                        }
                        else
                        {
                            Toast.makeText(com.example.mybookshop.Buyers.ResetPasswordActivity.this,"You have not set the Security Questions.",Toast.LENGTH_LONG).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(com.example.mybookshop.Buyers.ResetPasswordActivity.this,"This phone number not exits.",Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
        else
        {
            Toast.makeText(com.example.mybookshop.Buyers.ResetPasswordActivity.this,"Please complete the form.",Toast.LENGTH_LONG).show();

        }

    }


    private void setAnswers()
    {
        String answer1 = Question1.getText().toString().toLowerCase();
        String answer2 = Question2.getText().toString().toLowerCase();
        if (Question1.equals("") && Question2.equals("")) {
            Toast.makeText(com.example.mybookshop.Buyers.ResetPasswordActivity.this, "Please answer both the question...", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("answer1", answer1);
            userdataMap.put("answer2", answer2);


            ref.child("Security Questions")
                    .updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(com.example.mybookshop.Buyers.ResetPasswordActivity.this, "You have answer security questions successfully...", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(com.example.mybookshop.Buyers.ResetPasswordActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }

                }
            });


        }

    }

    private void displayPreviousAnswers()
    {

        DatabaseReference ref= FirebaseDatabase.getInstance()
                .getReference().child(" Users")
                .child(Prevalent.currentOnlineUser.getPhone());

        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    String ans1=snapshot.child("answer1").getValue().toString();
                    String ans2=snapshot.child("answer2").getValue().toString();

                    Question1.setText(ans1);
                    Question2.setText(ans2);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    }


