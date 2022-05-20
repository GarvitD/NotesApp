package com.example.quixoteandroidtask.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quixoteandroidtask.Models.NotesModel;
import com.example.quixoteandroidtask.R;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    Context context;
    List<NotesModel> notesModelList;
    OnItemClick onItemClick;

    private final int[] colors = {R.color.pink,R.color.light_red,R.color.light_green,R.color.light_yellow,R.color.cyan};

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
        holder.photo.setImageURI(Uri.parse(notesModelList.get(holder.getAdapterPosition()).getPhoto().get(0)));
        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context,colors[(holder.getAdapterPosition())%5]));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.ItemClicked(notesModelList.get(holder.getAdapterPosition()), colors[(holder.getAdapterPosition())%5]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesModelList.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView photo;
        CardView cardView;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.note_item_title);
            photo = itemView.findViewById(R.id.notes_image);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    public interface OnItemClick {
        void ItemClicked(NotesModel model,int color);
    }
}
