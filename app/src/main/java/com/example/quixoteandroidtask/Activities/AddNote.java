package com.example.quixoteandroidtask.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.quixoteandroidtask.Databases.NotesDatabaseHelper;
import com.example.quixoteandroidtask.Models.NotesModel;
import com.example.quixoteandroidtask.R;
import com.example.quixoteandroidtask.databinding.ActivityAddNoteBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.github.muddz.styleabletoast.StyleableToast;

public class AddNote extends AppCompatActivity {

    private ActivityAddNoteBinding binding;
    private NotesDatabaseHelper notesDb;

    private static final int TITLE_MIN_CHAR = 5;
    private static final int DESCP_MIN_CHAR = 100;
    private static final int IMAGE_MIN_NUM = 1;
    private static final int IMAGE_MAX_NUM = 10;

    private ArrayList<Uri> images;
    private String title;
    private String description;

    private ArrayList<NotesModel> oldNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        images = new ArrayList<>();
        title = binding.noteTitle.getText().toString();
        description = binding.noteDescp.getText().toString();

        notesDb = new NotesDatabaseHelper(this);

        Gson gson = new Gson();
        oldNotes = gson.fromJson(notesDb.getNotesList(1),new TypeToken<ArrayList<String>>() {}.getType());

        binding.noteTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(title.length() < TITLE_MIN_CHAR) {
                    binding.noteTitle.requestFocus();
                    binding.noteTitle.setError("Please enter more than "+TITLE_MIN_CHAR+" characters");
                }
            }
        });

        binding.noteDescp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(description.length() < DESCP_MIN_CHAR) {
                    binding.noteDescp.requestFocus();
                    binding.noteDescp.setError("Please enter more than "+DESCP_MIN_CHAR+" characters");
                }
            }
        });

        binding.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        binding.submitNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(images.size() >= IMAGE_MIN_NUM
                        && images.size() <= IMAGE_MAX_NUM
                        && title.length() >= TITLE_MIN_CHAR
                        && description.length() >= DESCP_MIN_CHAR ) {

                    NotesModel note = new NotesModel(title,description,images);
                    oldNotes.add(note);
                    if(notesDb.addNotetoDb(convertListToString(oldNotes))) {
                        StyleableToast.makeText(AddNote.this, "Note Added Successfully", Toast.LENGTH_LONG, R.style.successToast).show();
                        Intent intent = new Intent(AddNote.this,MainActivity.class);
                        startActivity(intent);
                    } else {
                        StyleableToast.makeText(AddNote.this, "Failed to store data", Toast.LENGTH_LONG, R.style.errorToast).show();
                    }
                } else {
                    if(images.size() < IMAGE_MIN_NUM || images.size() > IMAGE_MAX_NUM) {
                        StyleableToast.makeText(AddNote.this, "Please submit a minimum of "+ IMAGE_MIN_NUM + " and a maximum of "+ IMAGE_MAX_NUM+" images!", Toast.LENGTH_LONG, R.style.errorToast).show();
                    } else if(title.length() < TITLE_MIN_CHAR ) {
                        binding.noteTitle.requestFocus();
                        binding.noteTitle.setError("Please enter more than "+TITLE_MIN_CHAR+" characters");
                    } else if(description.length() < DESCP_MIN_CHAR) {
                        binding.noteDescp.requestFocus();
                        binding.noteDescp.setError("Please enter more than "+DESCP_MIN_CHAR+" characters");
                    }
                }
            }
        });
    }

    private String convertListToString(ArrayList<NotesModel> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }


    private void openGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        resultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK) {
                        if(result.getData().getClipData() != null) {
                            int number = result.getData().getClipData().getItemCount();
                            if(number > IMAGE_MAX_NUM) {
                                Toast.makeText(AddNote.this, "You cannot select more than "+IMAGE_MAX_NUM+" images", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(number < IMAGE_MIN_NUM) {
                                Toast.makeText(AddNote.this, "Please select atleast "+IMAGE_MIN_NUM+" images", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            for(int i=0;i<number;i++){
                                images.add(result.getData().getClipData().getItemAt(i).getUri());
                            }
                        } else {
                            images.add(result.getData().getData());
                        }
                    }
                }
            }
    );
}