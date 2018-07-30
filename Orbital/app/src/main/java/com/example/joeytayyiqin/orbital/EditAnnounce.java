package com.example.joeytayyiqin.orbital;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditAnnounce extends AppCompatActivity {
    private EditText mEditTextTitle;
    private EditText mEditTextMessage;
    private String noteId;
    private Button mBtnDel;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_announce);

        // Initialise Widgets
        mEditTextTitle = findViewById(R.id.editTextTitle);
        mEditTextMessage = findViewById(R.id.editTextMessage);
        mBtnDel = findViewById(R.id.buttonDelete);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        button = (Button) findViewById(R.id.buttonShare);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "New Announcement from iHONUS! Go check it out! https://www.facebook.com/EusoffHall/";
                String shareSub = mEditTextTitle.getText().toString();
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, "Share using"));
            }
        });
        // Get Note from bundle
        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            Note note = b.getParcelable("note");
            mEditTextTitle.setText(note.getTitle());
            mEditTextMessage.setText(note.getMessage());
            noteId = note.getId();
        } else {
            new NullPointerException();
        }

     mBtnDel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deleteNote();
        }
    });

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("admin");
        DatabaseReference user = mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Result will be holded Here
                if (!((boolean) dataSnapshot.getValue())) {
                    mBtnDel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle db error
            }
        });
}

    private void deleteNote() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("announcements");
        mDatabase.child(noteId).removeValue();
        Toast.makeText(this, "Announcement deleted", Toast.LENGTH_SHORT).show();
        finish();
        backToMainActivity();
    }

    private static Menu addButton;
    private void setMenu(Menu menu){
        addButton = menu;
    }

    private static void hideMenu(){
        addButton.setGroupVisible(0,false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setMenu(menu);
        getMenuInflater().inflate(R.layout.menu_add_note, menu);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("admin");
        DatabaseReference user = mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Result will be holded Here
                if (!((boolean) dataSnapshot.getValue())) EditAnnounce.hideMenu();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle db error
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onSupportNavigateUp() {
        backToMainActivity();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addnote:
                // Add Note to FireBase
                updateNote();
                // Back to MainActivity
                backToMainActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateNote() {
        String title = mEditTextTitle.getText().toString().trim();
        String message = mEditTextMessage.getText().toString().trim();
        // Write a message to the database
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("announcements");
        // Note object to store title and message
        Note note = new Note();
        note.setTitle(title);
        note.setMessage(message);
        note.setId(noteId);
        mDatabase.child(noteId).setValue(note);
        Toast.makeText(EditAnnounce.this, "Announcements added!", Toast.LENGTH_SHORT).show();
    }

    private void backToMainActivity() {
        Intent intent = new Intent(EditAnnounce.this, Announcements.class);
        startActivity(intent);
        finish();
    }

}

