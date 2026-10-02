package com.example.kursinis.ActivityControllers;

import static com.example.kursinis.utils.Constants.GET_ALL_RESTAURANTS;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursinis.R;
import com.example.kursinis.model.Restaurant;
import com.example.kursinis.utils.RLRestaurantAdapter;
import com.example.kursinis.utils.RestOperations;
import com.example.kursinis.utils.Session;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainLoggedInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_logged_in);
        populateRestaurantList();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chatRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void populateRestaurantList() {
        RecyclerView recyclerView = findViewById(R.id.avaliableOrderList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Restaurant> restaurantList = new ArrayList<>();
        RLRestaurantAdapter adapter =  new RLRestaurantAdapter(this, restaurantList);
        recyclerView.setAdapter(adapter);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        Gson gson = new Gson();

        executor.execute(() -> {
            try {
                String json = RestOperations.sendGet(GET_ALL_RESTAURANTS);
                Restaurant[] arr = gson.fromJson(json, Restaurant[].class);

                handler.post(() -> {
                    restaurantList.clear();
                    restaurantList.addAll(Arrays.asList(arr));
                    adapter.notifyDataSetChanged();
                });

            } catch (Exception e) {
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