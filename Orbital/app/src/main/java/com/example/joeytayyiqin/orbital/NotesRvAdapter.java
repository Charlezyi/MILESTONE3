package com.example.joeytayyiqin.orbital;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NotesRvAdapter extends RecyclerView.Adapter<NotesRvAdapter.NotesViewHolder> {
    private Context mContext;
    private List<Note> notesList;

    public NotesRvAdapter(Context context, List<Note> notes){
        mContext = context;
        notesList = notes;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_note, parent, false);
        NotesViewHolder notesViewHolder = new NotesViewHolder(itemView);


        return notesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, final int position) {
        final Note note = notesList.get(position);
        holder.mTitle.setText(note.getTitle());
        holder.mDate.setText(note.getMessage());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditNoteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("note", note);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public TextView mDate;
        public CardView mCardView;


        public NotesViewHolder(View view) {
            super(view);
            mTitle = view.findViewById(R.id.cardNoteTitle);
            mDate = view.findViewById(R.id.DateTitle);
            mCardView = view.findViewById(R.id.cardViewNote);
        }
    }
}