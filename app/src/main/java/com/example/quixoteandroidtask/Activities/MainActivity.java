package com.example.quixoteandroidtask.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.quixoteandroidtask.Databases.NotesDatabaseHelper;
import com.example.quixoteandroidtask.Models.NotesModel;
import com.example.quixoteandroidtask.R;
import com.example.quixoteandroidtask.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<NotesModel> notesList;
    private NotesDatabaseHelper notesDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        notesDb = new NotesDatabaseHelper(this);
        notesList = new ArrayList<>();
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("usersSp",MODE_PRIVATE);
        int id = sharedPreferences.getInt("id",-1);
        Log.i("userId", String.valueOf(id));
        notesList = gson.fromJson(notesDb.getNotesList(id),new TypeToken<ArrayList<String>>() {}.getType());

        Toast.makeText(this, notesList.size(), Toast.LENGTH_SHORT).show();
    }
}