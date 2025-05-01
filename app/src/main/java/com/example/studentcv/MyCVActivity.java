package com.example.studentcv;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCVActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private EditText messageInput;
    private ImageButton sendButton;
    private ChatAdapter chatAdapter;
    private ArrayList<ChatMessage> chatMessages;

    // Metadata to capture user inputs
    private HashMap<String, String> userMetadata;
    private GeminiApiService geminiApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cv);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        // Initialize chat messages list and adapter
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // Initialize metadata storage
        userMetadata = new HashMap<>();

        // Initialize Gemini API Service with API key from BuildConfig
        geminiApiService = RetrofitClient.getInstance(BuildConfig.GEMINI_API_KEY).create(GeminiApiService.class);

        // Handle send button click
        sendButton.setOnClickListener(v -> {
            String userMessage = messageInput.getText().toString().trim();
            if (!userMessage.isEmpty()) {
                // Add user message to chat
                chatMessages.add(new ChatMessage(userMessage, true));
                chatAdapter.notifyDataSetChanged();
                chatRecyclerView.scrollToPosition(chatMessages.size() - 1);

                // Clear input
                messageInput.setText("");

                // Process chatbot response
                processUserMessage(userMessage);
            }
        });
    }

    private void processUserMessage(String userMessage) {
        String chatbotResponse;

        if (userMessage.toLowerCase().contains("name")) {
            userMetadata.put("name", userMessage.replace("My name is ", "").trim());
            chatbotResponse = "Got it! What's your contact information?";
        } else if (userMessage.toLowerCase().contains("contact")) {
            userMetadata.put("contact", userMessage.replace("My contact is ", "").trim());
            chatbotResponse = "Great! Can you share your education details?";
        } else if (userMessage.toLowerCase().contains("education")) {
            userMetadata.put("education", userMessage.replace("My education is ", "").trim());
            chatbotResponse = "Thanks! How about your work experience?";
        } else if (userMessage.toLowerCase().contains("experience")) {
            userMetadata.put("experience", userMessage.replace("My experience is ", "").trim());
            chatbotResponse = "Noted! Do you have any skills to add?";
        } else if (userMessage.toLowerCase().contains("skills")) {
            userMetadata.put("skills", userMessage.replace("My skills are ", "").trim());
            chatbotResponse = "Perfect! Say 'generate CV' to proceed.";
        } else if (userMessage.equalsIgnoreCase("generate CV")) {
            chatbotResponse = "Generating your CV...";

            // Generate CV using Gemini
            generateCV();
            return;
        } else {
            chatbotResponse = "I'm here to help! Please share your details step by step.";
        }

        // Add chatbot response to chat
        chatMessages.add(new ChatMessage(chatbotResponse, false));
        chatAdapter.notifyDataSetChanged();
        chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
    }

    private void generateCV() {
        String prompt = new Gson().toJson(userMetadata); // Serialize metadata to JSON
        GeminiRequest request = new GeminiRequest(prompt);

        geminiApiService.generateCV(request).enqueue(new Callback<GeminiResponse>() {
            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String generatedCV = response.body().getText();

                    // Navigate to CV Preview Activity with generated CV
                    Intent intent = new Intent(MyCVActivity.this, CVPreviewActivity.class);
                    intent.putExtra("cvData", generatedCV);
                    startActivity(intent);
                } else {
                    chatMessages.add(new ChatMessage("Failed to generate CV. Please try again.", false));
                    chatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<GeminiResponse> call, Throwable t) {
                chatMessages.add(new ChatMessage("Error: " + t.getMessage(), false));
                chatAdapter.notifyDataSetChanged();
            }
        });
    }
}