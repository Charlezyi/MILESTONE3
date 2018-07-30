package com.example.joeytayyiqin.orbital;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Status extends AppCompatActivity {

    private FirebaseAuth auth;
    Button btn_Send, btn_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        // Firebase Auth Instance
        auth = FirebaseAuth.getInstance();

        btn_refresh = findViewById(R.id.btn_refresh);
        btn_Send = findViewById(R.id.btn_send);

        //Set Event
        btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_Send.setEnabled(false);

                FirebaseAuth.getInstance().getCurrentUser()
                       .sendEmailVerification()
                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               btn_Send.setEnabled(true);

                               if(task.isSuccessful())
                                   Toast.makeText(Status.this, "Verification email sent to : "+FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                               else
                                   Toast.makeText(Status.this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                           }
                       });
            }
        });

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_refresh.setEnabled(false);
                FirebaseAuth.getInstance().getCurrentUser()
                        .reload()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                btn_refresh.setEnabled(true);

                                FirebaseUser user = auth.getCurrentUser();

                                if (user.isEmailVerified()){
                                    startActivity(new Intent(Status.this, MainActivity.class));
                                }
                                else
                                    Toast.makeText(Status.this, "Email not verified", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

}
