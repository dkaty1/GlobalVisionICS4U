package com.codelab.basiclayouts;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageClassificationActivity extends MLImageHelperActivity implements TextToSpeech.OnInitListener {
    private ImageLabeler imageLabeler;
    private TextToSpeech textToSpeech;

    public int imageCounter = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textToSpeech = new TextToSpeech(this, this);

        // Create an instance of ImageLabeler with custom options
        imageLabeler = ImageLabeling.getClient(new ImageLabelerOptions.Builder()
                .setConfidenceThreshold(0.7f)
                .build());
    }

    // This method connects to our trained TensorFlow model via an ML Vision Kit
    // The image bitmap is sent to the API for recognition
    @Override
    protected void runDetection(Bitmap bitmap) {
        imageCounter++;

        // Reset the title text view
        getTitleTextView().setText("");

        // Hide the camera view
        getCamView().setVisibility(View.INVISIBLE);

        AtomicInteger counter = new AtomicInteger(0);

        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
        imageLabeler.process(inputImage).addOnSuccessListener(imageLabels -> {
            StringBuilder sb = new StringBuilder();
            for (ImageLabel label : imageLabels) {
                // Calculate the confidence percentage
                int confidencePercentage = (int) (label.getConfidence() * 100);
                sb.append(label.getText()).append(": ").append(confidencePercentage).append("%\n");

                if (counter.incrementAndGet() >= 3) {
                    break; // Exit the loop after three iterations
                }
            }
            if (imageLabels.isEmpty()) {
                // Set the output text view to indicate classification failure
                getOutputTextView().setText("Could not classify!!");
                speak("Could not Classify");
            } else {
                // Set the output text view to display the classification results
                getOutputTextView().setText(sb.toString());
                speak(sb.toString());
            }
        }).addOnFailureListener(e -> {
            e.printStackTrace();
        });
    }

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

    private void speak(String message) {
        // Speak the given message using TextToSpeech
        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            // Stop and release the TextToSpeech resources
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    // Getter Method
    public int getImageCounter() {
        return imageCounter;
    }
}
