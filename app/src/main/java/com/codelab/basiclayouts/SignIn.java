package com.codelab.basiclayouts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignIn extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in); // Set the layout for the sign-in page

        // Initialize UI elements
        Button signinbtn = findViewById(R.id.sighupbtn);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button backToSignUp = findViewById(R.id.backToSignIn);
        TextView username = findViewById(R.id.username);
        TextView password = findViewById(R.id.password);
        TextView email = findViewById(R.id.email);
        TextView firstName = findViewById(R.id.firstName);
        TextView lastName = findViewById(R.id.lastName);
        TextView topSign = findViewById(R.id.signup);

        textToSpeech = new TextToSpeech(this, this); // Initialize TextToSpeech

        // Sign Up button click listener
        signinbtn.setOnClickListener(view -> new Thread(() -> {
            OkHttpClient client = new OkHttpClient();

            // Build the JSON for the HTTP request with user details
            String json = "{\"username\":\"" + username.getText().toString() + "\",\"email\":\"" + email.getText().toString() + "\",\"first_name\":\"" + firstName.getText().toString() + "\",\"last_name\":\"" + lastName.getText().toString() + "\",\"password\":\"" + password.getText().toString() +"\" }";
            String url = "http://10.0.2.2:9000/auth/create/user"; // URL for the API endpoint

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"), json); // Create the request body

            // Create the POST request
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Call call = client.newCall(request); // Create a new call
            try {
                Response response = call.execute(); // Execute the call
                System.out.println(response); // Print the response to the console
                speak("User created successfully"); // Speak the success message
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start());

        // Back to Log In button click listener
        backToSignUp.setOnClickListener(view -> {
            Intent i = new Intent(SignIn.this, LogIn.class); // Create a new intent to navigate to the LogIn activity
            startActivity(i); // Start the LogIn activity
            speak("Back to Log In"); // Speak the confirmation message
        });

        // Sign Up button click listener
        signinbtn.setOnClickListener(view -> {
            speak("You have Signed Up for Global Vision!"); // Speak the success message
        });

        // OnTouchListener created
        View.OnTouchListener touchListener = (v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                speak(v.getContentDescription().toString());
            }
            return false;
        };

        // OnTouchListener set for the top title
        topSign.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                speak("Sign Up");
            }
            return false;
        });

        // Rest of info boxes
        // OnTouchListener set for username and password field
        // OnTouchListener also set for email and name fields
        username.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                String currentUsername = username.getText().toString();
                speak("Username field: " + currentUsername);
            }
            return false;
        });

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    String fieldValue = password.getText().toString();
                    String message = "Password field. Current value: " + fieldValue;
                    speak(message);
                }
                return false;
            }
        });

// Email field
        email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    String fieldValue = email.getText().toString();
                    String message = "Email field. Current value: " + fieldValue;
                    speak(message);
                }
                return false;
            }
        });

// First name field
        firstName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    String fieldValue = firstName.getText().toString();
                    String message = "First name field. Current value: " + fieldValue;
                    speak(message);
                }
                return false;
            }
        });

// Last name field
        lastName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    String fieldValue = lastName.getText().toString();
                    String message = "Last name field. Current value: " + fieldValue;
                    speak(message);
                }
                return false;
            }
        });
    }

    // Helps to create the text to speech engine
    // Sets language and sets the TTS to success
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.getDefault());

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                System.out.println("Language not supported");
            }
        } else {
            System.out.println("Initialization failed");
        }
    }

    // Must use speak method for the text to speech
    private void speak(String message) {
        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    // Closes the text to speech to save resources
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}