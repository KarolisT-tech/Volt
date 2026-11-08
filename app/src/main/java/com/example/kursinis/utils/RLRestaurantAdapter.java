package com.example.kursinis.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursinis.R;
import com.example.kursinis.ActivityControllers.RestaurantCuisineActivity;
import com.example.kursinis.model.Restaurant;

import java.util.List;

public class RLRestaurantAdapter extends RecyclerView.Adapter<RLRestaurantAdapter.ViewHolder> {

    private Context context;
    private List<Restaurant> restaurants;

    public RLRestaurantAdapter(Context context, List<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public RLRestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false);
        return new RLRestaurantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant r = restaurants.get(position);

        holder.name.setText(r.getName());
        holder.address.setText("📍 " + r.getAddress());
        holder.phone.setText("📞 " + r.getPhoneNumber());

        String hours = r.getWorkHours();
        holder.workHours.setText(hours != null && !hours.isEmpty()
                ? "🕒 " + hours
                : "🕒 Darbo valandos nenurodytos");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RestaurantCuisineActivity.class);
            intent.putExtra("restaurantId", r.getId());
            intent.putExtra("restaurantName", r.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, phone, workHours;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.messageContent);
            address = itemView.findViewById(R.id.orderDate);
            phone = itemView.findViewById(R.id.restaurantPhone);
            workHours = itemView.findViewById(R.id.restaurantWorkHours);
        }
    }
}
