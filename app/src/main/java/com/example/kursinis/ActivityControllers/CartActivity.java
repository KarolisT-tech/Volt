package com.example.kursinis.ActivityControllers;

import static com.example.kursinis.utils.Constants.CREATE_ORDER_URL;
import static com.example.kursinis.utils.Constants.GET_CUSTOMER_BY_ID;
import static com.example.kursinis.utils.Constants.GET_RESTAURANT;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursinis.R;
import com.example.kursinis.model.BasicUser;
import com.example.kursinis.model.Cuisine;
import com.example.kursinis.model.OrderStatus;
import com.example.kursinis.model.Restaurant;
import com.example.kursinis.utils.RLCartAdapter;
import com.example.kursinis.utils.RestOperations;
import com.example.kursinis.utils.Session;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.cart);
        populateCartList();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chatRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void populateCartList() {
        RecyclerView recyclerView = findViewById(R.id.cartItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Cuisine> cartList = Session.getInstance().getCart().getItems();
        RLCartAdapter adapter =  new RLCartAdapter(cartList, this);
        recyclerView.setAdapter(adapter);

    }

    public void placeOrder(View view) {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        Gson gson = new Gson();
        JsonObject json = new JsonObject();

        List<Cuisine> cartList = Session.getInstance().getCart().getItems();
        if(cartList.isEmpty()){
            handler.post(() -> {
                Toast.makeText(CartActivity.this, "❗No Items Selected!", Toast.LENGTH_SHORT).show();
            });
            return;
        }

        executor.execute(() -> {
            try {
                String urlCustomer = String.format(GET_CUSTOMER_BY_ID, Session.getInstance().getUserId());
                String responseCustomer = RestOperations.sendGet(urlCustomer);
                BasicUser user = gson.fromJson(responseCustomer, BasicUser.class);
                json.addProperty("buyer", user.getId());

                String urlRestaurant = String.format(GET_RESTAURANT, getIntent().getIntExtra("restaurantId", -1));
                String responseRestaurant = RestOperations.sendGet(urlRestaurant);
                Restaurant restaurant = gson.fromJson(responseRestaurant, Restaurant.class);
                json.addProperty("restaurant", restaurant.getId());

                json.addProperty("ordersStatus", OrderStatus.PENDING.name());
                json.add("items", packCartToJson(cartList));
                json.addProperty("price", calculateOrderPrice(cartList));

                String info = gson.toJson(json);
                Log.d("INFO", info);
                String postResponse = RestOperations.sendPost(CREATE_ORDER_URL, info);
                if (!postResponse.equals("error")) {
                    handler.post(() -> {
                        Toast.makeText(CartActivity.this, "Order Placed!", Toast.LENGTH_SHORT).show();
                        Session.getInstance().getCart().clear();
                        Intent intent = new Intent(this, MainLoggedInActivity.class);
                        startActivity(intent);
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public double calculateOrderPrice(List<Cuisine> cart) {
        double totalPrice = 0;
        for(Cuisine c : cart){
            totalPrice += c.getPrice();
        }
        return totalPrice;
    }

    public JsonArray packCartToJson(List<Cuisine> cart) {
        JsonArray jsonArray = new JsonArray();
        Gson gson = new Gson();
        for(Cuisine c : cart){
            jsonArray.add(c.getId());
        }
        return jsonArray;
    }
}