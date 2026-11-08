package com.example.kursinis.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursinis.ActivityControllers.OrderCustomerActivity;
import com.example.kursinis.R;
import com.example.kursinis.model.FoodOrder;

import java.util.List;

public class RLOrderAdapterCustomer extends RecyclerView.Adapter<RLOrderAdapterCustomer.ViewHolder> {

    private Context context;
    private List<FoodOrder> orders;

    public RLOrderAdapterCustomer(Context context, List<FoodOrder> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public RLOrderAdapterCustomer.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new RLOrderAdapterCustomer.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RLOrderAdapterCustomer.ViewHolder holder, int position) {
        FoodOrder fo = orders.get(position);

        holder.name.setText(fo.getName());
        holder.date.setText("⏱" + fo.getDateCreated().toString());
        holder.status.setText("\uD83D\uDE9A " + fo.getOrderStatus().getDisplayName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderCustomerActivity.class);
            intent.putExtra("orderId", fo.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, date, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.messageContent);
            date = itemView.findViewById(R.id.orderDate);
            status = itemView.findViewById(R.id.orderStatus);
        }
    }
}
