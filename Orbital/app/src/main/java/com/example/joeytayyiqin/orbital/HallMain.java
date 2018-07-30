package com.example.joeytayyiqin.orbital;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class HallMain extends AppCompatActivity {

    public Button btnevents;

    public void init(){

        //hall button
        btnevents = findViewById(R.id.ButtonEvents);
        btnevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HallMain.this,HallEvent.class);
                startActivity(intent);
            }
        });

    }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_hallmain);
            init();
            ConfigureNextButton();
            ButtonAnnounce();
            // Check if user is logged in
            // If user is not logged in, direct user to login page
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Intent intent = new Intent(HallMain.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

     private void ConfigureNextButton(){
        Button ButtonFeedback = (Button) findViewById(R.id.ButtonFeedBack);
        ButtonFeedback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(HallMain.this, Feedback1.class));
            }
        });
    }

    private void ButtonAnnounce(){
        Button ButtonAnnounce = (Button) findViewById(R.id.ButtonAnnoucements);
        ButtonAnnounce.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent (HallMain.this, Announcements.class));
            }
        });
    }
        @Override
        public boolean onCreateOptionsMenu(Menu menu){
            getMenuInflater().inflate(R.layout.main_menu, menu);
            return super.onCreateOptionsMenu(menu);
        }

        //Select items on Main page
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.logout:
                    userLogout();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }

        // Signout User and return user to LoginActivity
        private void userLogout() {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(HallMain.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
}

