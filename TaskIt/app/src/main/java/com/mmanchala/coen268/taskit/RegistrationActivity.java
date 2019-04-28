package com.mmanchala.coen268.taskit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mmanchala.coen268.taskit.Model.User;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailText, passwordText, nameText;
    private Button regButton;
    private TextView loginText;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        emailText = findViewById(R.id.emailSignUp);
        passwordText = findViewById(R.id.passwordSignUp);
        regButton = findViewById(R.id.signupButton);
        nameText = findViewById(R.id.nameField);
        loginText = findViewById(R.id.loginText);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("MemberInfo");

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString().trim();
                String pass = passwordText.getText().toString().trim();
                String name = nameText.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    nameText.setError("Required Field");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    emailText.setError("Required Field");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    passwordText.setError("Required Field");
                    return;
                }

                dialog.setMessage("Processing.....");
                dialog.show();
                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            dialog.dismiss();
                        }else {
                            Toast.makeText(getApplicationContext(),"problem",Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }
                });
                String uid = mAuth.getCurrentUser().getUid();
                User user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setUserid(uid);

                mDatabase.child(uid).setValue(user);
            }
        });
    }
}
