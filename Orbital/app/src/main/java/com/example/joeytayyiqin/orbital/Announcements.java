package com.example.joeytayyiqin.orbital;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Announcements extends AppCompatActivity {

    private FloatingActionButton AddNewAnnounce;
    private RecyclerView mRvNotes;
    private ArrayList<Note> noteList;
    private AnnounceRvAdapter AnnounceRvAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);
        // Check if user is logged in
        // If user is not logged in, direct user to login page
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(Announcements.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        AddNewAnnounce = findViewById(R.id.fabAddAnnouncements);
        AddNewAnnounce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                writeNewNote();
            }
        });

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("admin");
        DatabaseReference user = mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Result will be holded Here
                if (!((boolean) dataSnapshot.getValue())) {
                    AddNewAnnounce.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle db error
            }
        });


        // Part 2: CRUD display
        noteList = new ArrayList<>();
        mRvNotes = findViewById(R.id.recycleViewNotes);
        mRvNotes = findViewById(R.id.recycleViewNotes);
        mRvNotes.setHasFixedSize(true);
        AnnounceRvAdapter = new AnnounceRvAdapter(this, noteList);
        mRvNotes.setAdapter(AnnounceRvAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvNotes.setLayoutManager(linearLayoutManager);

        getFirebaseData(new NotesCallback() {
            @Override
            public void onCallBack(Note note) {
                noteList.add(0, note);
//              Collections.reverse(noteList);
                AnnounceRvAdapter.notifyDataSetChanged();
            }
        });
    }

    private void writeNewNote() {
        Intent intent = new Intent(Announcements.this, AddAnnounce.class);
        startActivity(intent);
        finish();
    }

    private void getFirebaseData(final NotesCallback notesCallback) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference notesRef = reference.child("announcements");
        notesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Result will be holded Here
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                    Note note = new Note();
                    String noteId = String.valueOf(dataSnap.child("id").getValue());
                    String title = String.valueOf(dataSnap.child("title").getValue());
                    String message = String.valueOf(dataSnap.child("message").getValue());
                    note.setId(noteId);
                    note.setTitle(title);
                    note.setMessage(message);
                    notesCallback.onCallBack(note);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle db error
            }
        });

    }
    public interface NotesCallback {
        void onCallBack(Note note);
    }
}