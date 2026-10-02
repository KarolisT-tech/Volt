package com.example.kursinis.ActivityControllers;

import static com.example.kursinis.utils.Constants.GET_CUSTOMER_BY_ID;
import static com.example.kursinis.utils.Constants.GET_DRIVER_BY_ID;
import static com.example.kursinis.utils.Constants.UPDATE_CUSTOMER;
import static com.example.kursinis.utils.Constants.UPDATE_DRIVER;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kursinis.R;
import com.example.kursinis.model.BasicUser;
import com.example.kursinis.model.Driver;
import com.example.kursinis.model.VehicleType;
import com.example.kursinis.utils.LocalDateAdapter;
import com.example.kursinis.utils.RestOperations;
import com.example.kursinis.utils.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.time.LocalDate;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.account);
        setFieldDisplay();
        PopulateSpinner();
        fillField();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chatRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void setFieldDisplay() {
        TextView license = findViewById(R.id.licenceE);
        TextView bDate = findViewById(R.id.bDateE);
        Spinner vType = findViewById(R.id.vTypeE);

        if(Session.getInstance().getUserType().equals("driver")) {
            license.setVisibility(View.VISIBLE);
            bDate.setVisibility(View.VISIBLE);
            vType.setVisibility(View.VISIBLE);
        }
        else {
            license.setVisibility(View.GONE);
            bDate.setVisibility(View.GONE);
            vType.setVisibility(View.GONE);
        }
    }

    public void fillField() {

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                if(Session.getInstance().getUserType().equals("customer"))
                {
                    String url = String.format(GET_CUSTOMER_BY_ID, Session.getInstance().getUserId());
                    String response = RestOperations.sendGet(url);
                    BasicUser user = gson.fromJson(response, BasicUser.class);
                    handler.post(() -> {
                        TextView login = findViewById(R.id.usernameE);
                        TextView password = findViewById(R.id.passwordE);
                        TextView firstName = findViewById(R.id.firstNameE);
                        TextView surname = findViewById(R.id.lastNameE);
                        TextView phoneNumber = findViewById(R.id.phoneNumberE);
                        TextView Address = findViewById(R.id.addressE);

                        login.setText(user.getLogin());
                        password.setText(user.getPassword());
                        firstName.setText(user.getName());
                        surname.setText(user.getSurname());
                        phoneNumber.setText(user.getPhoneNumber());
                        Address.setText(user.getAddress());
                    });
                }
                else if(Session.getInstance().getUserType().equals("driver")) {
                    String url = String.format(GET_DRIVER_BY_ID, Session.getInstance().getUserId());
                    String response = RestOperations.sendGet(url);
                    Driver user = gson.fromJson(response, Driver.class);
                    handler.post(() -> {
                        TextView login = findViewById(R.id.usernameE);
                        TextView password = findViewById(R.id.passwordE);
                        TextView firstName = findViewById(R.id.firstNameE);
                        TextView surname = findViewById(R.id.lastNameE);
                        TextView phoneNumber = findViewById(R.id.phoneNumberE);
                        TextView Address = findViewById(R.id.addressE);
                        TextView license = findViewById(R.id.licenceE);
                        TextView bDate = findViewById(R.id.bDateE);
                        Spinner vType = findViewById(R.id.vTypeE);

                        login.setText(user.getLogin());
                        password.setText(user.getPassword());
                        firstName.setText(user.getName());
                        surname.setText(user.getSurname());
                        phoneNumber.setText(user.getPhoneNumber());
                        Address.setText(user.getAddress());
                        license.setText(user.getLicence());
                        bDate.setText(user.getbDate());

                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) vType.getAdapter();
                        vType.setSelection(adapter.getPosition(user.getVehicleType().name()));
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void saveUserInfo(View view){

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String login = ((TextView) findViewById(R.id.usernameE)).getText().toString();
                String password = ((TextView) findViewById(R.id.passwordE)).getText().toString();
                String name = ((TextView) findViewById(R.id.firstNameE)).getText().toString();
                String surname = ((TextView) findViewById(R.id.lastNameE)).getText().toString();
                String phone = ((TextView) findViewById(R.id.phoneNumberE)).getText().toString();
                String address = ((TextView) findViewById(R.id.addressE)).getText().toString();

                JsonObject json = new JsonObject();
                json.addProperty("login", login);
                json.addProperty("password", password);
                json.addProperty("name", name);
                json.addProperty("surname", surname);
                json.addProperty("phoneNumber", phone);
                json.addProperty("address", address);

                if(Session.getInstance().getUserType().equals("driver")){
                    String licence = ((TextView) findViewById(R.id.licenceE)).getText().toString();
                    String bDate = ((TextView) findViewById(R.id.bDateE)).getText().toString();
                    Spinner vType = findViewById(R.id.vTypeE);
                    String vehicleType = vType.getSelectedItem().toString();

                    json.addProperty("licence", licence);
                    json.addProperty("bdate", bDate);
                    json.addProperty("vehicleType", vehicleType);
                }

                String body = gson.toJson(json);
                Log.d("USERINFO", body);
                String url;
                if(Session.getInstance().getUserType().equals("driver")){ url = String.format(UPDATE_DRIVER, Session.getInstance().getUserId()); }
                else{ url = String.format(UPDATE_CUSTOMER, Session.getInstance().getUserId()); }
                String response = RestOperations.sendPut(url, body);

                if(!response.equals("error")){
                    handler.post(() -> Toast.makeText(this, "Saved successfully!", Toast.LENGTH_SHORT).show());
                    Intent intent = new Intent(this, MainLoggedInActivity.class);
                    startActivity(intent);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public void PopulateSpinner() {
        Spinner vType = findViewById(R.id.vTypeE);
        ArrayAdapter<VehicleType> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, VehicleType.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vType.setAdapter(adapter);
    }
}