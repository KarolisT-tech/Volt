package com.example.kursinis.ActivityControllers;

import static com.example.kursinis.utils.Constants.GET_ORDER_URL;
import static com.example.kursinis.utils.Constants.UPDATE_ORDER_STATUS_TO_IN_DELIVERY;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursinis.DataTransmissionObject.OrderStatusDTO;
import com.example.kursinis.R;
import com.example.kursinis.model.Cuisine;
import com.example.kursinis.model.FoodOrder;
import com.example.kursinis.model.OrderStatus;
import com.example.kursinis.utils.LocalDateAdapter;
import com.example.kursinis.utils.RLCuisineAdapter;
import com.example.kursinis.utils.RestOperations;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class OrderDriverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.order_driver);
        fillOrderData();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chatRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void fillOrderData() {
        TextView name = findViewById(R.id.orderNameDriver);
        TextView price = findViewById(R.id.orderPriceDriver);
        TextView date = findViewById(R.id.orderDateDriver);
        TextView restaurant = findViewById(R.id.orderRestaurantDriver);
        TextView status = findViewById(R.id.orderStatusDriver);
        TextView customer = findViewById(R.id.orderCustDriver);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

        executor.execute(() -> {
            try {
                RecyclerView recyclerView = findViewById(R.id.orderItemListDriver);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                List<Cuisine> items = new ArrayList<>();
                RLCuisineAdapter adapter =  new RLCuisineAdapter(this, items, "orderView");
                recyclerView.setAdapter(adapter);

                String url = String.format(GET_ORDER_URL, getIntent().getIntExtra("orderId", -1));
                String json = RestOperations.sendGet(url);
                FoodOrder foodOrder = gson.fromJson(json, FoodOrder.class);

                List<Cuisine> arr = foodOrder.getItems();

                handler.post(() -> {
                    name.setText(foodOrder.getName());
                    price.setText("\uD83D\uDCB2" + String.valueOf(foodOrder.getPrice()));
                    date.setText("\uD83D\uDD5B" + foodOrder.getDateCreated().toString());
                    status.setText("\uD83D\uDE9A " + foodOrder.getOrderStatus().getDisplayName());
                    restaurant.setText("\uD83C\uDF7D\uFE0F" + foodOrder.getRestaurant().getName());
                    customer.setText("\uD83E\uDDCD" + foodOrder.getBuyer().getName() + " " + foodOrder.getBuyer().getSurname());
                    items.addAll(arr);
                    adapter.notifyDataSetChanged();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void finishOrder(View view) {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

        executor.execute(() -> {
            try {
                OrderStatusDTO orderStatusDTO = new OrderStatusDTO(getIntent().getIntExtra("orderId", -1), 0, OrderStatus.COMPLETE);
                String info = gson.toJson(orderStatusDTO);
                String response = RestOperations.sendPut(UPDATE_ORDER_STATUS_TO_IN_DELIVERY, info);
                handler.post(() -> {
                    if (!response.equals("error")) {
                        Intent intent = new Intent(this, MyOrdersActivity.class);
                        startActivity(intent);
                        Toast.makeText(this, "✅ Order Completed Successfully", Toast.LENGTH_SHORT).show();
                    }
                });

            }catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void redirectToChat(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("orderId", getIntent().getIntExtra("orderId", -1));
        startActivity(intent);
    }
}