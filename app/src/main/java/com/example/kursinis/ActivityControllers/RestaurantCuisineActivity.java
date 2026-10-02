package com.example.kursinis.ActivityControllers;

import static com.example.kursinis.utils.Constants.GET_RESTAURANT_MENU;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursinis.R;
import com.example.kursinis.model.Cuisine;
import com.example.kursinis.utils.CartManager;
import com.example.kursinis.utils.OnCuisineClickListener;
import com.example.kursinis.utils.RLCuisineAdapter;
import com.example.kursinis.utils.RestOperations;
import com.example.kursinis.utils.Session;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RestaurantCuisineActivity extends AppCompatActivity {

    private List<Cuisine> addedCuisines = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.restaurant_cuisine);
        populateCuisineList_AddTitle();
        enforceRestaurantCartRules();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chatRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void populateCuisineList_AddTitle() {

        TextView title = findViewById(R.id.restaurantTitle);
        title.setText(getIntent().getStringExtra("restaurantName") + "'s Menu \uD83C\uDF74");

        RecyclerView recyclerView = findViewById(R.id.restaurantMeniu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Cuisine> cuisineList = new ArrayList<>();
        RLCuisineAdapter adapter =  new RLCuisineAdapter(this, cuisineList, "menu");
        recyclerView.setAdapter(adapter);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        Gson gson = new Gson();

        executor.execute(() -> {
            try {
                String url = String.format(GET_RESTAURANT_MENU, getIntent().getIntExtra("restaurantId", -1));
                String json = RestOperations.sendGet(url);
                Cuisine[] arr = gson.fromJson(json, Cuisine[].class);

                handler.post(() -> {
                    cuisineList.clear();
                    cuisineList.addAll(Arrays.asList(arr));
                    adapter.notifyDataSetChanged();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void enforceRestaurantCartRules() {
        int restaurantId = getIntent().getIntExtra("restaurantId", -1);
        CartManager cart = Session.getInstance().getCart();

        if (cart.getRestaurantId() == null) {
            cart.setRestaurantId(restaurantId);
        }
        else if (!cart.getRestaurantId().equals(restaurantId)) {
            cart.clear();
            cart.setRestaurantId(restaurantId);
        }
    }

    public void goToCart(View view) {
        Intent intent = new Intent(this, CartActivity.class);
        intent.putExtra("selectedItems", new ArrayList<>(addedCuisines));
        intent.putExtra("restaurantId", getIntent().getIntExtra("restaurantId", -1));
        startActivity(intent);
    }
}