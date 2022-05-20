package com.example.quixoteandroidtask.Models;

import android.net.Uri;

import java.util.ArrayList;

public class NotesModel {
    private String title;
    private String description;
    private ArrayList<Uri> photo;

    public NotesModel(String title, String description, ArrayList<Uri> photo) {
        this.title = title;
        this.description = description;
        this.photo = photo;
    }

    public ArrayList<Uri> getPhoto() {
        return photo;
    }

    public void setPhoto(ArrayList<Uri> photo) {
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


}
