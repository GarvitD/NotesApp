package com.example.quixoteandroidtask.Models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class NotesModel implements Parcelable {
    private String description;
    private ArrayList<String> photo;
    private String title;

    public NotesModel(String description, ArrayList<String> photo, String title) {
        this.title = title;
        this.description = description;
        this.photo = photo;
    }

    protected NotesModel(Parcel in) {
        title = in.readString();
        description = in.readString();
        photo = in.createStringArrayList();
    }

    public static final Creator<NotesModel> CREATOR = new Creator<NotesModel>() {
        @Override
        public NotesModel createFromParcel(Parcel in) {
            return new NotesModel(in);
        }

        @Override
        public NotesModel[] newArray(int size) {
            return new NotesModel[size];
        }
    };

    public ArrayList<String> getPhoto() {
        return photo;
    }

    public void setPhoto(ArrayList<String> photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeStringList(photo);
    }
}
