package com.example.joeytayyiqin.orbital;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable,Comparable<Note>{
    private String title;
    private String id;
    private String message;

    public Note(){}

    public void setId(String id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(message);
    }
    public Note(Parcel source) {
        this.id = source.readString();
        this.title = source.readString();
        this.message = source.readString();
    }
    public static final Parcelable.Creator<Note> CREATOR
            = new Parcelable.Creator<Note>() {
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int compareTo(Note n){
        return -(this.getMessage().compareTo(n.getMessage()));
    }


}

