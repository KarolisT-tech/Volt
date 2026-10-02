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

public class RLCartAdapter extends RecyclerView.Adapter<RLCartAdapter.ViewHolder> {

    private Context context;
    private List<Cuisine> cart;

    public RLCartAdapter(List<Cuisine> cart, Context context) {
        this.cart = cart;
        this.context = context;
    }

    @NonNull
    @Override
    public RLCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cuisine, parent, false);
        return new RLCartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RLCartAdapter.ViewHolder holder, int position) {
        Cuisine c = cart.get(position);
        holder.name.setText(c.getName());
        holder.price.setText("€" + c.getPrice());

        holder.itemView.setOnClickListener(v -> {
            Session.getInstance().getCart().removeItem(c);
            cart.remove(c);
            notifyDataSetChanged();
            Toast.makeText(context, "Removed: " + c.getName(), Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return cart.size();
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
