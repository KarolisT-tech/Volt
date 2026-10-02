package com.example.kursinis.ActivityControllers;

import static com.example.kursinis.utils.Constants.CREATE_MESSAGE;
import static com.example.kursinis.utils.Constants.GET_MESSAGES_BY_CHAT;
import static com.example.kursinis.utils.Constants.GET_ORDER_URL;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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

import com.example.kursinis.DataTransmissionObject.MessageDTO;
import com.example.kursinis.R;
import com.example.kursinis.model.FoodOrder;
import com.example.kursinis.model.Message;
import com.example.kursinis.utils.LocalDateAdapter;
import com.example.kursinis.utils.RLChatAdapter;
import com.example.kursinis.utils.RestOperations;
import com.example.kursinis.utils.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.chat);
        populateMessageList();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void populateMessageList() {
        List<Message> messageList = new ArrayList<>();
        RLChatAdapter adapter =  new RLChatAdapter(messageList, this);
        RecyclerView recyclerView = findViewById(R.id.messageList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        TextView chatName = findViewById(R.id.chatName);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

        executor.execute(() -> {
            try {
                String url = String.format(GET_ORDER_URL, getIntent().getIntExtra("orderId", -1));
                String json = RestOperations.sendGet(url);
                FoodOrder order = gson.fromJson(json, FoodOrder.class);
                if(order != null) {
                    handler.post(()->chatName.setText("Chat-" + order.getName()));
                    String messageUrl = String.format(GET_MESSAGES_BY_CHAT, order.getChat().getId());
                    String response = RestOperations.sendGet(messageUrl);
                    Message[] arr = gson.fromJson(response, Message[].class);
                    if(!response.equals("error") && arr != null && arr.length > 0) {
                        handler.post(() -> {
                            messageList.clear();
                            List<Message> sorted = Arrays.asList(arr);
                            sorted.sort(Comparator.comparingInt(Message::getId));
                            messageList.addAll(sorted);
                            adapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(messageList.size() - 1);
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void sendMessage(View view) {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

        TextView chatMessage = findViewById(R.id.textMessageContent);
        executor.execute(() -> {
            try {
                String url = String.format(GET_ORDER_URL, getIntent().getIntExtra("orderId", -1));
                String json = RestOperations.sendGet(url);
                FoodOrder order = gson.fromJson(json, FoodOrder.class);

                if(order.getOrderStatus().name().equals("COMPLETE")){
                    handler.post(() -> {
                        Toast.makeText(this, "❗ Order Complete! View Mode Only!", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                if(chatMessage.getText().toString().isEmpty()) {
                    handler.post(()->{
                        Toast.makeText(this, "❗ Can't Send An Empty Message!", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                int senderId = Session.getInstance().getUserId();
                int receiverId;
                if (senderId == order.getBuyer().getId()) {receiverId = order.getDriver().getId(); }
                else {receiverId = order.getBuyer().getId(); }

                MessageDTO messageDTO = new MessageDTO(chatMessage.getText().toString(), Session.getInstance().getUserId(),
                                                                receiverId, order.getChat().getId());
                String info = gson.toJson(messageDTO);
                Log.d("MESSAGE_INFO", info);
                String response = RestOperations.sendPut(CREATE_MESSAGE, info);
                if(!response.equals("error")) {
                    handler.post(() -> {
                        chatMessage.setText("");
                        populateMessageList();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}