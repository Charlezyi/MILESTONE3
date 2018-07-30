package com.example.joeytayyiqin.orbital;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity{

    private EditText mEditTextEmail;
    private EditText mEditTextPw;
    private Button mBtnSignup;
    private FirebaseAuth auth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialise widgets
        mEditTextEmail = findViewById(R.id.editTextEmail);
        mEditTextPw = findViewById(R.id.editTextPassword);
        mBtnSignup = findViewById(R.id.buttonSignup);
        progressBar = findViewById(R.id.progressbar);

        // Firebase Auth Instance
        auth = FirebaseAuth.getInstance();

        mBtnSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = mEditTextEmail.getText().toString().trim();
                String password = mEditTextPw.getText().toString().trim();
                // Check if email is empty
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignupActivity.this, "Enter Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignupActivity.this, "Enter Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!email.contains("@u.nus.edu")){
                    Toast.makeText(SignupActivity.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }

                // check if password is empty
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignupActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                //check if password is alphanumeric
                if(!password.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z])[a-zA-Z0-9]+$")){
                    Toast.makeText(SignupActivity.this, "Password must contain one upper and lower case letter and one number", Toast.LENGTH_LONG).show();
                    return;
                }

                // Set your own additional constraints
                progressBar.setVisibility(View.VISIBLE);

                // Create a new user
                auth.createUserWithEmailAndPassword(email, password).
                        addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                } else {
                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("admin");
                                    mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(false);
                                    // Signup successful, got to main activity
                                    startActivity(new Intent(SignupActivity.this, Status.class));
                                    // End the activity
                                    finish();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });

    }
}
