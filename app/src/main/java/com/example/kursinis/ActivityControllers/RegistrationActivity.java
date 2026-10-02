package com.example.kursinis.ActivityControllers;

import static com.example.kursinis.utils.Constants.CREATE_CUSTOMER_URL;
import static com.example.kursinis.utils.Constants.CREATE_DRIVER_URL;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kursinis.DataTransmissionObject.UserDTO;
import com.example.kursinis.R;
import com.example.kursinis.model.BasicUser;
import com.example.kursinis.model.Driver;
import com.example.kursinis.model.VehicleType;
import com.example.kursinis.utils.LocalDateAdapter;
import com.example.kursinis.utils.RestOperations;
import com.example.kursinis.utils.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        toggleDriverFields();
        PopulateSpinner();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chatRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void createNewUser(View view){
        TextView username = findViewById(R.id.username);
        TextView name = findViewById(R.id.firstName);
        TextView surname = findViewById(R.id.lastName);
        TextView password = findViewById(R.id.password);
        TextView phoneNumber = findViewById(R.id.phoneNumber);
        TextView address = findViewById(R.id.address);
        CheckBox driverCHK = findViewById(R.id.driverCHK);

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        if(!driverCHK.isChecked()){
            BasicUser basicUser = new BasicUser(username.getText().toString(), password.getText().toString(), name.getText().toString(),
                                                surname.getText().toString(), phoneNumber.getText().toString(), address.getText().toString());
            String info = gson.toJson(basicUser, BasicUser.class);
            executor.execute(()->{
                try {
                    String response = RestOperations.sendPost(CREATE_CUSTOMER_URL, info);
                    SetUserSession(response);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        else{
            TextView license = findViewById(R.id.licence);
            TextView bDate = findViewById(R.id.bDate);
            Spinner vType = findViewById(R.id.vType);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birthDate = LocalDate.parse(bDate.getText().toString(), formatter);

            Driver driver = new Driver(username.getText().toString(), password.getText().toString(), name.getText().toString(),
                                        surname.getText().toString(), phoneNumber.getText().toString(), address.getText().toString(),
                                        license.getText().toString(), birthDate, (VehicleType) vType.getSelectedItem());
            String info = gson.toJson(driver, Driver.class);
            executor.execute(()->{
                try {
                    String response = RestOperations.sendPost(CREATE_DRIVER_URL, info);
                    SetUserSession(response);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void toggleDriverFields() {
        CheckBox checkBox = findViewById(R.id.driverCHK);
        TextView license = findViewById(R.id.licence);
        TextView bDate = findViewById(R.id.bDate);
        Spinner vType = findViewById(R.id.vType);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                license.setVisibility(View.VISIBLE);
                bDate.setVisibility(View.VISIBLE);
                vType.setVisibility(View.VISIBLE);
            } else {
                license.setVisibility(View.GONE);
                bDate.setVisibility(View.GONE);
                vType.setVisibility(View.GONE);
            }
        });
    }

    public void PopulateSpinner() {
        Spinner vType = findViewById(R.id.vType);
        ArrayAdapter<VehicleType> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, VehicleType.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vType.setAdapter(adapter);
    }

    public void SetUserSession(String response) {
        Gson gson = new Gson();
        UserDTO userDTO = gson.fromJson(response, UserDTO.class);
        if (!response.equals("Error")) {
            Session.getInstance().setUser(userDTO.getId(), userDTO.getType());
            Intent intent = new Intent(RegistrationActivity.this, MainLoggedInActivity.class);
            startActivity(intent);
        }
    }
}