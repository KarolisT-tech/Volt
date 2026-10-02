package com.example.kursinis.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursinis.R;
import com.example.kursinis.model.Cuisine;

import java.util.List;

public class RLCuisineAdapter extends RecyclerView.Adapter<RLCuisineAdapter.ViewHolder> {

    private Context context;
    private List<Cuisine> cuisines;
    private String mode;

    public RLCuisineAdapter(Context context, List<Cuisine> cuisines, String mode) {
        this.context = context;
        this.cuisines = cuisines;
        this.mode = mode;
    }

    public RLCuisineAdapter(Context context, List<Cuisine> cuisines) {
        this.context = context;
        this.cuisines = cuisines;
    }

    @NonNull
    @Override
    public RLCuisineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cuisine, parent, false);
        return new RLCuisineAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cuisine c = cuisines.get(position);

        holder.name.setText(c.getName());
        holder.price.setText("€" + c.getPrice());

        if(mode.equals("menu"))
        {
            holder.itemView.setOnClickListener(v -> {
                Session.getInstance().getCart().addItem(c);
                Toast.makeText(context, "Added: " + c.getName(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return cuisines.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.messageContent);
            price = itemView.findViewById(R.id.orderDate);
        }
    }
}
