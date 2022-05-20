package com.example.quixoteandroidtask.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.quixoteandroidtask.Adapters.NotesAdapter;
import com.example.quixoteandroidtask.Databases.UsersDatabaseHelper;
import com.example.quixoteandroidtask.Models.NotesModel;
import com.example.quixoteandroidtask.R;
import com.example.quixoteandroidtask.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesAdapter.OnItemClick {

    private ActivityMainBinding binding;
    private ArrayList<NotesModel> notesList;
    private UsersDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = new UsersDatabaseHelper(this);
        notesList = new ArrayList<>();

        initializeList();
        setUpRecyclerView();

        binding.addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AddNote.class));
                finish();
            }
        });

        binding.logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutCurUser();
            }
        });
    }

    private void logOutCurUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("usersSp",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putBoolean("isLoggedIn",false);
        editor.apply();
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    private void initializeList() {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("usersSp",MODE_PRIVATE);
        String email = sharedPreferences.getString("data","");
        Type type = new TypeToken<ArrayList<NotesModel>>() {}.getType();
        String list = db.getNotesList(email);
        notesList = gson.fromJson(list,type);

        if(notesList == null) notesList = new ArrayList<>();
    }

    private void setUpRecyclerView() {
        NotesAdapter notesAdapter = new NotesAdapter(this,notesList,this);
        binding.notesRecyclerView.setHasFixedSize(true);
        binding.notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.notesRecyclerView.setAdapter(notesAdapter);
    }

    @Override
    public void ItemClicked(NotesModel model, int color) {
        Intent intent = new Intent(MainActivity.this,seeNote.class);
        intent.putExtra("note",model);
        intent.putExtra("bgColor",color);
        startActivity(intent);
    }
}