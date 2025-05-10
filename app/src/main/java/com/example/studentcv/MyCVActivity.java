package com.example.studentcv;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class MyCVActivity extends AppCompatActivity {

    private static final String TAG = "MyCVActivity";
    private LinearLayout chatBodyContainer;
    private ProgressBar progressBar;
    private ImageButton sendButton;
    private EditText messageInput;
    private ScrollView scrollView;
    private HashMap<String, String> userMetadata;

    private final String[] metadataFields = {
            "name", "contact", "education", "experience", "skills"
    };

    private final String[] prompts = {
            "Please provide your full name.",
            "What is your contact information? (email, phone, location)",
            "Tell me about your education background (institutions, degrees, dates).",
            "Describe your work experience (company names, positions, dates, responsibilities).",
            "List your technical and soft skills."
    };

    private int currentStep = 0;
    private GeminiPro geminiPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cv);

        initializeViews();
        setupClickListeners();
        startConversation();

        geminiPro = new GeminiPro(); // Initialize GeminiPro
    }

    private void initializeViews() {
        chatBodyContainer = findViewById(R.id.chatBodyContainer);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        progressBar = findViewById(R.id.progressBar);
        scrollView = findViewById(R.id.scrollView);

        userMetadata = new HashMap<>();
    }

    private void setupClickListeners() {
        sendButton.setOnClickListener(v -> {
            String userMessage = messageInput.getText().toString().trim();
            if (userMessage.isEmpty()) {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
                return;
            }

            // Clear input and show user message
            messageInput.setText("");
            addChatMessage("You", userMessage, getCurrentTime());

            // Process message
            if (userMessage.equalsIgnoreCase("generate CV")) {
                handleGenerateCV();
            } else {
                handleUserMessage(userMessage);
            }
        });
    }

    private void startConversation() {
        addChatMessage("VOVO", "Welcome to CV Generator! " + prompts[currentStep], getCurrentTime());
    }

    private void handleUserMessage(String userMessage) {
        if (currentStep < metadataFields.length) {
            // Save user input
            String field = metadataFields[currentStep];
            userMetadata.put(field, userMessage);
            currentStep++;

            // Show next prompt or completion message
            if (currentStep < prompts.length) {
                addChatMessage("VOVO", prompts[currentStep], getCurrentTime());
            } else {
                addChatMessage("VOVO", "Great! All information collected. Type 'generate CV' to create your CV.", getCurrentTime());
            }
        }
    }

    private void handleGenerateCV() {
        if (userMetadata.size() < metadataFields.length) {
            addChatMessage("VOVO", "Please complete all fields before generating the CV.", getCurrentTime());
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        addChatMessage("VOVO", "Generating your CV...", getCurrentTime());

        // Prepare the prompt for CV generation
        String cvPrompt = constructCVPrompt(userMetadata);

        // Use GeminiPro to generate the CV
        GeminiPro.getResponse(geminiPro.getModel().startChat(), cvPrompt, new ResponseCallBack() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);

                // Handle the response to ensure the 'license' field is present
                String sanitizedResponse = sanitizeResponse(response);

                navigateToCVPreview(sanitizedResponse);
            }

            @Override
            public void onError(Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Error generating CV", t);
                addChatMessage("VOVO", "Failed to generate CV. Please try again.", getCurrentTime());
            }
        });
    }

    private String constructCVPrompt(HashMap<String, String> metadata) {
        return "Generate a professional CV in HTML format using the following information:\n" +
                "Name: " + metadata.get("name") + "\n" +
                "Contact: " + metadata.get("contact") + "\n" +
                "Education: " + metadata.get("education") + "\n" +
                "Experience: " + metadata.get("experience") + "\n" +
                "Skills: " + metadata.get("skills") + "\n" +
                "Please format it professionally with appropriate sections and styling.";
    }

    private String sanitizeResponse(String jsonResponse) {
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            JsonArray candidates = jsonObject.getAsJsonArray("candidates");

            for (JsonElement candidate : candidates) {
                JsonObject citationMetadata = candidate.getAsJsonObject()
                        .getAsJsonObject("citationMetadata");
                JsonArray citationSources = citationMetadata.getAsJsonArray("citationSources");

                for (JsonElement source : citationSources) {
                    JsonObject sourceObject = source.getAsJsonObject();
                    if (!sourceObject.has("license")) {
                        sourceObject.addProperty("license", "Unknown License");
                    }
                }
            }

            return jsonObject.toString();
        } catch (Exception e) {
            Log.e(TAG, "Error sanitizing response", e);
            return jsonResponse; // Return the original response if sanitization fails
        }
    }

    private void navigateToCVPreview(String cvData) {
        Intent intent = new Intent(MyCVActivity.this, CVPreviewActivity.class);
        intent.putExtra("cvData", cvData);
        startActivity(intent);
    }

    private void addChatMessage(String sender, String message, String date) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.chat_message_block, null);

        TextView userAgentName = view.findViewById(R.id.userAgentNameTextfield);
        TextView userAgentMessage = view.findViewById(R.id.userAgentMessageTextView);
        TextView dateTextView = view.findViewById(R.id.dateTextView);

        userAgentName.setText(sender);
        userAgentMessage.setText(message);
        dateTextView.setText(date);

        // Add different background colors for user and AI messages
        if (sender.equals("You")) {
            view.setBackgroundResource(R.drawable.user_message_background);
        } else {
            view.setBackgroundResource(R.drawable.ai_message_background);
        }

        chatBodyContainer.addView(view);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    private String getCurrentTime() {
        Instant instant = Instant.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                .withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }
}