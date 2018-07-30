package com.example.joeytayyiqin.orbital;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class EditNoteActivity extends AppCompatActivity{
    private EditText mEditTextTitle;
    //private EditText mEditTextMessage;
    private String noteId;
    private Button mBtnDel;
    private Button mBtnYes;
    private Button mBtnNo;
    private TextView mDisplayDate;
    private TextView NumGoing;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean mProcessyes = false;
    private boolean mProcessno = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);


        // Initialise Widgets
        mEditTextTitle = findViewById(R.id.editTextTitle);
       // mEditTextMessage = findViewById(R.id.editTextMessage);
        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        mBtnDel = findViewById(R.id.buttonDelete);
        mBtnYes = findViewById(R.id.Yes);
        mBtnNo = findViewById(R.id.No);
        NumGoing = findViewById(R.id.NumGoing);

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditNoteActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;

                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        // Get Note from bundle
        Bundle b = this.getIntent().getExtras();
        if (b != null){
            Note note = b.getParcelable("note");
            mEditTextTitle.setText(note.getTitle());
            mDisplayDate.setText(note.getMessage());
            //mEditTextMessage.setText(note.getMessage());
            noteId = note.getId();



        } else {
            new NullPointerException();
        }

        mBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
                backToMainActivity();
            }
        });


//        final DatabaseReference m = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference mDatabaseyes = FirebaseDatabase.getInstance().getReference().child("yes");
        final DatabaseReference mDatabaseNumYes = mDatabaseyes.child(noteId);
        mDatabaseNumYes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                NumGoing.setText((Long.toString(dataSnapshot.getChildrenCount())));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        mDatabaseyes.keepSynced(true);

        mBtnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProcessyes = true;

                if (mProcessyes) {

                    mDatabaseyes.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child(noteId).hasChild(mAuth.getCurrentUser().getUid())) {

                            } else {
                                mDatabaseyes.child(noteId).child(mAuth.getCurrentUser().getUid()).setValue("1");
                            }
                            NumGoing.setText((Long.toString(dataSnapshot.child(noteId).getChildrenCount())));
                            mDatabaseyes.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            };
        });

        mBtnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProcessno = true;

                if (mProcessno) {

                    mDatabaseyes.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child(noteId).hasChild(mAuth.getCurrentUser().getUid())) {
                                mDatabaseyes.child(noteId).child(mAuth.getCurrentUser().getUid()).removeValue();
                            }
                            NumGoing.setText((Long.toString(dataSnapshot.child(noteId).getChildrenCount())));
                            mDatabaseyes.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
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
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("notes");
        mDatabase.child(noteId).removeValue();
    }

    private static Menu AddEvent;
    private void setMenu(Menu menu){
        AddEvent = menu;
    }

    private static void hideMenu(){
        AddEvent.setGroupVisible(0, false);
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
                if (!((boolean) dataSnapshot.getValue())) EditNoteActivity.hideMenu();
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
        String message = mDisplayDate.getText().toString().trim();
        // Write a message to the database
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("notes");
        // Note object to store title and message
        Note note = new Note();
        note.setTitle(title);
        note.setMessage(message);
        note.setId(noteId);
        mDatabase.child(noteId).setValue(note);
        Toast.makeText(EditNoteActivity.this, "Note Added!", Toast.LENGTH_SHORT).show();
    }

    private void backToMainActivity() {
        Intent intent = new Intent(EditNoteActivity.this, HallEvent.class);
        startActivity(intent);
        finish();
    }

}


