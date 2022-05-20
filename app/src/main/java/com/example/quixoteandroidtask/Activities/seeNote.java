package com.example.quixoteandroidtask.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.example.quixoteandroidtask.Adapters.ImagesAdapter;
import com.example.quixoteandroidtask.Models.NotesModel;
import com.example.quixoteandroidtask.R;
import com.example.quixoteandroidtask.databinding.ActivitySeeNoteBinding;

import java.io.FileNotFoundException;

import io.github.muddz.styleabletoast.StyleableToast;

public class seeNote extends AppCompatActivity implements ImagesAdapter.ImageOnClick {

    private ActivitySeeNoteBinding binding;
    private NotesModel note;
    private int bgColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeeNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        note = intent.getParcelableExtra("note");
        bgColor = intent.getIntExtra("bgColor",R.color.dark_blue);
        Log.i("colorCode", String.valueOf(bgColor));

        setUpViews();
    }

    private void setUpViews() {
        binding.title.setText(note.getTitle());
        binding.description.setText(note.getDescription());
        binding.seeNoteLayout.setBackgroundColor(getResources().getColor(bgColor,this.getTheme()));

        ImagesAdapter adapter = new ImagesAdapter(this,note.getPhoto(),this);
        binding.images.setHasFixedSize(true);
        binding.images.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        binding.images.setAdapter(adapter);
    }

    @Override
    public void openPopup(String path) {
        final ImagePopup imagePopup = new ImagePopup(this);

        imagePopup.setWindowHeight(800);
        imagePopup.setWindowWidth(800);
        imagePopup.setBackground(getDrawable(R.drawable.ic_baseline_person_24));
        imagePopup.setBackgroundColor(getResources().getColor(R.color.black,this.getTheme()));
        imagePopup.setFullScreen(true);
        imagePopup.setHideCloseIcon(true);
        imagePopup.setImageOnClickClose(true);
        try {
            imagePopup.initiatePopup(Drawable.createFromStream(getContentResolver().openInputStream(Uri.parse(path)),path));
        } catch (FileNotFoundException e) {
            StyleableToast.makeText(seeNote.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT, R.style.errorToast).show();
        }

        imagePopup.viewPopup();
    }
}