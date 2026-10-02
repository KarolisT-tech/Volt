package com.example.kursinis.ActivityControllers;

import static com.example.kursinis.utils.Constants.CREATE_CHAT;
import static com.example.kursinis.utils.Constants.GET_AVAILABLE_ORDERS;
import static com.example.kursinis.utils.Constants.UPDATE_ORDER_STATUS_TO_IN_DELIVERY;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursinis.DataTransmissionObject.ChatDTO;
import com.example.kursinis.DataTransmissionObject.OrderStatusDTO;
import com.example.kursinis.R;
import com.example.kursinis.model.FoodOrder;
import com.example.kursinis.model.OrderStatus;
import com.example.kursinis.utils.LocalDateAdapter;
import com.example.kursinis.utils.OnOrderClickListener;
import com.example.kursinis.utils.RLOrderAdapterDriver;
import com.example.kursinis.utils.RestOperations;
import com.example.kursinis.utils.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainLoggedInDriverActivity extends AppCompatActivity implements OnOrderClickListener {

    private List<FoodOrder> orders = new ArrayList<>();
    private RLOrderAdapterDriver adapter =  new RLOrderAdapterDriver(this, orders, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_logged_in_driver);
        populateAvailableOrderList();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chatRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void populateAvailableOrderList() {
        RecyclerView recyclerView = findViewById(R.id.avaliableOrderList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

        recyclerView.setAdapter(adapter);

        executor.execute(() -> {
            try {
                List<FoodOrder> allOrders = new ArrayList<>();
                OrderStatus[] statuses = {OrderStatus.PENDING, OrderStatus.SEEN_BY_STAFF, OrderStatus.IN_PROGRESS};

                for(OrderStatus status : statuses) {
                    String url = String.format(GET_AVAILABLE_ORDERS, status);
                    String json = RestOperations.sendGet(url);
                    FoodOrder[] arr = gson.fromJson(json, FoodOrder[].class);
                    allOrders.addAll(Arrays.asList(arr));
                }

                handler.post(() -> {
                    orders.clear();
                    orders.addAll(allOrders);
                    adapter.notifyDataSetChanged();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void updateOrderStatus(int orderId, int pos) {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                    OrderStatusDTO orderStatusDTO = new OrderStatusDTO(orderId,Session.getInstance().getUserId(), OrderStatus.IN_DELIVERY);
                    ChatDTO chatDTO = new ChatDTO(orderId);
                    String info = gson.toJson(orderStatusDTO);
                    String response = RestOperations.sendPut(UPDATE_ORDER_STATUS_TO_IN_DELIVERY, info);
                    String infoChat = gson.toJson(chatDTO);
                    String responseChat = RestOperations.sendPut(CREATE_CHAT, infoChat);
                    handler.post(() -> {
                    if (!response.equals("error")) {
                        orders.remove(pos);
                        adapter.notifyItemRemoved(pos);
                        Toast.makeText(this, "✅ Order Taken Successfully", Toast.LENGTH_SHORT).show();
                    }
                });

            }catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void LogOut(View view) {
        Session.getInstance().clear();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void RedirectToAccount(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    public void RedirectToMyOrders(View view) {
        Intent intent = new Intent(this, MyOrdersActivity.class);
        startActivity(intent);
    }
}