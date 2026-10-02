package com.example.kursinis.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursinis.R;
import com.example.kursinis.model.Cuisine;
import com.example.kursinis.model.Message;

import java.util.List;

public class RLChatAdapter extends RecyclerView.Adapter<RLChatAdapter.ViewHolder> {

    private Context context;
    private List<Message> messageList;

    public RLChatAdapter(List<Message> messages, Context context) {
        this.messageList = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public RLChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new RLChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RLChatAdapter.ViewHolder holder, int position) {
        Message m = messageList.get(position);
        holder.message.setText(m.getMessageOwner().getName() + ": " + m.getReviewText());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.messageContent);
        }
    }
}
