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

public class AnnounceRvAdapter extends RecyclerView.Adapter<AnnounceRvAdapter.AnnounceViewHolder> {
    private Context mContext;
    private List<Note> notesList;

    public AnnounceRvAdapter(Context context, List<Note> notes){
        mContext = context;
        notesList = notes;
    }


    @NonNull
    @Override
    public AnnounceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_announce, parent, false);
        AnnounceViewHolder notesViewHolder = new AnnounceViewHolder(itemView);


        return notesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AnnounceViewHolder holder, final int position) {
        final Note note = notesList.get(position);
        holder.mTitle.setText(note.getTitle());
//        holder.mDate.setText(note.getMessage());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditAnnounce.class);
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

    public class AnnounceViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
//        public TextView mDate;
        public CardView mCardView;


        public AnnounceViewHolder(View view) {
            super(view);
            mTitle = view.findViewById(R.id.AnnounceTitle);
            mCardView = view.findViewById(R.id.cardViewAnnounce);
        }
    }
}
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_announce_rv_adapter);
//    }
