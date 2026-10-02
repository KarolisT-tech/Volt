package com.example.kursinis.ActivityControllers;

import static com.example.kursinis.utils.Constants.CREATE_REVIEW;
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

import com.example.kursinis.DataTransmissionObject.ReviewDTO;
import com.example.kursinis.R;
import com.example.kursinis.model.FoodOrder;
import com.example.kursinis.model.Review;
import com.example.kursinis.utils.LocalDateAdapter;
import com.example.kursinis.utils.RestOperations;
import com.example.kursinis.utils.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ReviewActivity extends AppCompatActivity {

    private FoodOrder order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.review);
        addTitle();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void addTitle() {
        TextView name = findViewById(R.id.orderReviewName);
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

        executor.execute(() -> {
            try {
                String url = String.format(GET_ORDER_URL, getIntent().getIntExtra("orderId", -1));
                String json = RestOperations.sendGet(url);
                order = gson.fromJson(json, FoodOrder.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        handler.post(() -> {
            name.setText("Review For " + order.getName());
        });
    }

    public void submitReview(View view) {
        TextView rating = findViewById(R.id.reviewRating);
        TextView message = findViewById(R.id.ReviewTW);
        
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        
        executor.execute(() -> {
            try {
                String url = String.format(GET_ORDER_URL, getIntent().getIntExtra("orderId", -1));
                String json = RestOperations.sendGet(url);
                order = gson.fromJson(json, FoodOrder.class);
                
                ReviewDTO reviewDTO = new ReviewDTO(Integer.parseInt(rating.getText().toString()), message.getText().toString(),
                                                    Session.getInstance().getUserId(), order.getRestaurant().getId(), order.getId());
                String info = gson.toJson(reviewDTO);
                String response = RestOperations.sendPut(CREATE_REVIEW, info);
                handler.post(() -> {
                    if (!response.equals("error")) {
                        Toast.makeText(this, "Review Created Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}