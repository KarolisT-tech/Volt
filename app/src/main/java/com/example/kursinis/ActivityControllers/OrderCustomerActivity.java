package com.example.kursinis.ActivityControllers;

import static com.example.kursinis.utils.Constants.GET_ORDER_URL;
import static com.example.kursinis.utils.Constants.GET_REVIEW_BY_ORDER_ID;

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

import com.example.kursinis.R;
import com.example.kursinis.model.Cuisine;
import com.example.kursinis.model.FoodOrder;
import com.example.kursinis.model.Review;
import com.example.kursinis.utils.LocalDateAdapter;
import com.example.kursinis.utils.RLCuisineAdapter;
import com.example.kursinis.utils.RestOperations;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class OrderCustomerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.order_customer);
        fillOrderData();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chatRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void fillOrderData() {
        TextView name = findViewById(R.id.orderNameCust);
        TextView price = findViewById(R.id.orderPriceCust);
        TextView date = findViewById(R.id.orderDateCust);
        TextView restaurant = findViewById(R.id.orderRestaurantCust);
        TextView status = findViewById(R.id.orderStatusCust);

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
                    items.addAll(arr);
                    adapter.notifyDataSetChanged();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void leaveAReview(View view) {

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

        executor.execute(() -> {
            try {
                String checkUrl = String.format(GET_REVIEW_BY_ORDER_ID, getIntent().getIntExtra("orderId", -1));
                String checkResponse = RestOperations.sendGet(checkUrl);
                Review review = gson.fromJson(checkResponse, Review.class);
                if(review != null && review.getId() > 0){
                    handler.post(() -> {
                        Toast.makeText(this, "❗ Already Left Review For This Order!", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                String url = String.format(GET_ORDER_URL, getIntent().getIntExtra("orderId", -1));
                String json = RestOperations.sendGet(url);
                FoodOrder foodOrder = gson.fromJson(json, FoodOrder.class);

                handler.post(() -> {
                    if (foodOrder.getOrderStatus().name().equals("COMPLETE")) {
                        Intent intent = new Intent(this, ReviewActivity.class);
                        intent.putExtra("orderId", getIntent().getIntExtra("orderId", -1));
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "❗ Can't Leave A Review! Order Not Complete!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void openChat(View view){

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

        executor.execute(() -> {
            try {
                String url = String.format(GET_ORDER_URL, getIntent().getIntExtra("orderId", -1));
                String json = RestOperations.sendGet(url);
                FoodOrder foodOrder = gson.fromJson(json, FoodOrder.class);

                handler.post(() -> {
                    if (foodOrder.getOrderStatus().name().equals("COMPLETE") || foodOrder.getOrderStatus().name().equals("IN_DELIVERY")) {
                        Intent intent = new Intent(this, ChatActivity.class);
                        intent.putExtra("orderId", getIntent().getIntExtra("orderId", -1));
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "❗ Can't Open Chat! Order Doesn't Have A Driver Yet!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}