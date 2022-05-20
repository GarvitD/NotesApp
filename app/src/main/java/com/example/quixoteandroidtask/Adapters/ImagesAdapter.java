package com.example.quixoteandroidtask.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quixoteandroidtask.R;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder> {
    Context context;
    List<String> imageList;
    ImageOnClick imageOnClick;

    public ImagesAdapter(Context context, List<String> imageList, ImageOnClick imageOnClick) {
        this.context = context;
        this.imageList = imageList;
        this.imageOnClick = imageOnClick;
    }

    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImagesViewHolder(LayoutInflater.from(context).inflate(R.layout.imageslist_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder holder, int position) {
        holder.photo.setImageURI(Uri.parse(imageList.get(holder.getAdapterPosition())));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageOnClick.openPopup(imageList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ImagesViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        public ImagesViewHolder(@NonNull View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.photo);
        }
    }

    public interface ImageOnClick {
        void openPopup(String path);
    }
}
