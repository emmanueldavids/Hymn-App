package com.example.hymnapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HymnAdapter extends RecyclerView.Adapter<HymnAdapter.HymnViewHolder> {

    private List<Hymn> hymnList;
    private static OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Hymn hymn);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public HymnAdapter(List<Hymn> hymnList) {
        this.hymnList = hymnList;
    }

    @NonNull
    @Override
    public HymnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hymn_item, parent, false);
        return new HymnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HymnViewHolder holder, int position) {
        Hymn hymn = hymnList.get(position);
        holder.titleTextView.setText(hymn.getTitle());
        holder.authorTextView.setText(hymn.getAuthor());
    }

    @Override
    public int getItemCount() {
        return hymnList.size();
    }

    class HymnViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView authorTextView;

        HymnViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleView);
            authorTextView = itemView.findViewById(R.id.authorView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Hymn hymn = hymnList.get(position);
                            listener.onItemClick(hymn);
                        }
                    }
                }
            });
        }
    }
}
