package com.example.quixoteandroidtask.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quixoteandroidtask.Models.NotesModel;
import com.example.quixoteandroidtask.R;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    Context context;
    List<NotesModel> notesModelList;
    OnItemClick onItemClick;

    public NotesAdapter(Context context, List<NotesModel> notesModelList, OnItemClick onItemClick) {
        this.context = context;
        this.notesModelList = notesModelList;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.title.setText(notesModelList.get(holder.getAdapterPosition()).getTitle());
        holder.title.setText(notesModelList.get(holder.getAdapterPosition()).getDescription());
        holder.photo.setImageURI(notesModelList.get(holder.getAdapterPosition()).getPhoto().get(0));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.ItemClicked(notesModelList.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesModelList.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        TextView title,description;
        ImageView photo;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.note_item_title);
            description = itemView.findViewById(R.id.notes_item_descp);
            photo = itemView.findViewById(R.id.notes_image);
        }
    }

    public interface OnItemClick {
        void ItemClicked(NotesModel model,int position);
    }
}
