package com.example.kursinis.ActivityControllers;

import static com.example.kursinis.utils.Constants.VALIDATE_USER_URL;

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

import com.example.kursinis.DataTransmissionObject.UserDTO;
import com.example.kursinis.R;
import com.example.kursinis.utils.RestOperations;
import com.example.kursinis.utils.Session;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        GoToRegisterPage();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chatRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void validateUser(View view) {
        TextView loginField = findViewById(R.id.loginField);
        TextView passwordField = findViewById(R.id.passwordField);

        Gson gson = new Gson();
        JsonObject data = new JsonObject();
        data.addProperty("login", loginField.getText().toString());
        data.addProperty("password", passwordField.getText().toString());
        String jsonBody = gson.toJson(data);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String response = RestOperations.sendPost(VALIDATE_USER_URL, jsonBody);
                UserDTO userDTO = gson.fromJson(response, UserDTO.class);
                if(userDTO == null){
                    handler.post(() -> {
                        Toast.makeText(this, "❗ Incorrect Login Or Password!", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }
                handler.post(() -> {
                    if (!response.equals("Error")) {
                        Session.getInstance().setUser(userDTO.getId(), userDTO.getType());
                        if(Session.getInstance().getUserType().equals("customer")){
                            Intent intent = new Intent(MainActivity.this, MainLoggedInActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Intent intent = new Intent(MainActivity.this, MainLoggedInDriverActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void GoToRegisterPage() {
        TextView registerLink = findViewById(R.id.registerLink);

        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
    }

}