package com.example.kursinis.ActivityControllers;

import static com.example.kursinis.utils.Constants.GET_CUSTOMER_ORDERS;
import static com.example.kursinis.utils.Constants.GET_DRIVER_ORDERS;
import static com.example.kursinis.utils.Constants.GET_REVIEW_BY_ORDER_ID;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursinis.R;
import com.example.kursinis.model.FoodOrder;
import com.example.kursinis.model.OrderStatus;
import com.example.kursinis.model.Review;
import com.example.kursinis.utils.LocalDateAdapter;
import com.example.kursinis.utils.RLOrderAdapterCustomer;
import com.example.kursinis.utils.RLOrderAdapterDriver;
import com.example.kursinis.utils.RestOperations;
import com.example.kursinis.utils.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MyOrdersActivity extends AppCompatActivity {

    private List <FoodOrder> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.my_orders);
        fillOrders();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chatRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void fillOrders() {
        RecyclerView recyclerView = findViewById(R.id.orderList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

        if(Session.getInstance().getUserType().equals("customer")){

            RLOrderAdapterCustomer adapter =  new RLOrderAdapterCustomer(this, orders);
            recyclerView.setAdapter(adapter);

            executor.execute(() -> {
                try {
                    String url = String.format(GET_CUSTOMER_ORDERS, Session.getInstance().getUserId());
                    String json = RestOperations.sendGet(url);
                    FoodOrder[] arr = gson.fromJson(json, FoodOrder[].class);

                    handler.post(() -> {
                        orders.clear();
                        orders.addAll(Arrays.asList(arr));
                        adapter.notifyDataSetChanged();
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        else{

            RLOrderAdapterDriver adapter =  new RLOrderAdapterDriver(this, orders);
            recyclerView.setAdapter(adapter);

            executor.execute(() -> {
                try {
                    String url = String.format(GET_DRIVER_ORDERS, Session.getInstance().getUserId());
                    String json = RestOperations.sendGet(url);
                    FoodOrder[] arr = gson.fromJson(json, FoodOrder[].class);

                    handler.post(() -> {
                        orders.clear();
                        orders.addAll(Arrays.asList(arr));
                        adapter.notifyDataSetChanged();
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}