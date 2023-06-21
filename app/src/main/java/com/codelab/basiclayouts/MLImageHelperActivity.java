package com.codelab.basiclayouts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MLImageHelperActivity extends AppCompatActivity  implements TextToSpeech.OnInitListener {


    // All the variables are declared
    // This includes REQUEST_CODEs, ImageViews, TextViews and other instance variables

    public List<Drawable> images;

    Drawable tempDrawable;

    public Drawable capturedImageDrawable;
    private TextToSpeech textToSpeech;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
    public final String LOG_TAG = "MLImageHelper";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 1064;
    public final static int REQUEST_READ_EXTERNAL_STORAGE = 2031;
    File photoFile;

    private ImageView inputImageView;
    private TextView outputTextView;

    private TextView titleTextView;

    private ImageView camView;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the UI to the proper XML file

        setContentView(R.layout.activity_image_helper);


        // Set each button, image or text to the corresponding element in the XML
        textToSpeech = new TextToSpeech(this, this);

        inputImageView = findViewById(R.id.imageView);
        outputTextView = findViewById(R.id.textView);
        titleTextView = findViewById(R.id.funnyText);
        camView = findViewById(R.id.camView);

        // Creates the touchListener, which determines when a text view or image has been clicked

        View.OnTouchListener touchListener = (v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                speak(v.getContentDescription().toString());
            }
            return false;
        };

        // The text view that displays the top message has been assigned a touchListener
        // When the text is clicked, TTS activates to provide auditory feedback

        titleTextView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                speak("Choose a Photo Using These Buttons");
            }
            return false;
        });


        // Checks to ensure the user has granted camera permissions


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
        }

        // Checks to ensure the user has granted storage permissions

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
            }
        }
    }

    public void onTakeImage(View view) {
        speak("Take Photo");
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg");

        // wrap File object into a content provider
        // required for API >= 24

        Uri fileProvider = FileProvider.getUriForFile(this, "com.iago.fileprovider1", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }



    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), LOG_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(LOG_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    /**
     * getCapturedImage():
     *     Decodes and crops the captured image from camera.
     */
    public Bitmap getCapturedImage() {
        // Get the dimensions of the View
        int targetW = inputImageView.getWidth();
        int targetH = inputImageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFile.getAbsolutePath());
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = Math.max(1, Math.min(photoW / targetW, photoH /targetH));

        bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inMutable = true;
        return BitmapFactory.decodeFile(photoFile.getAbsolutePath(), bmOptions);
    }

    // Rotates the image to always fit the phone screen
    private void rotateIfRequired(Bitmap bitmap) {
        try {
            ExifInterface exifInterface = new ExifInterface(photoFile.getAbsolutePath());
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED
            );
            // Checks the current orientation, and rotates the image for it to display vertically on the screen

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                rotateImage(bitmap, 90f);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                rotateImage(bitmap, 180f);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                rotateImage(bitmap, 270f);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rotate the given bitmap.
     */
    // This image holds the logic to rotate an image, given the bitmap and angle
    private Bitmap rotateImage(Bitmap source, float angle) {
        // Post-concats the matrix with the specified rotation
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        // returns the new image as a bitmap
        return Bitmap.createBitmap(
                source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true
        );
    }

    public void onPickImage(View view) {
        speak("Pick Image from Library");
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, PICK_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public void onPickImageReal() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, PICK_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Getter methods use to access private variables
    protected TextView getOutputTextView() {
        return outputTextView;
    }

    protected ImageView getCamView() {
        return camView;
    }

    protected TextView getTitleTextView() {
        return titleTextView;
    }

    protected ImageView getInputImageView() {
        return inputImageView;
    }

    protected void runDetection(Bitmap bitmap) {

    }


    // Decodes the bitmap from URI
    protected Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;


        // Uses ImageDecoder for converting encoded images like PNG, JPEG, or HEIF into Drawable or Bitmap object
        try {
            if (Build.VERSION.SDK_INT > 27) {
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        return image;
    }




    // Handles the image taken by user

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Once image is taken from camera, converts to bitmap and rotates if needed
                Bitmap bitmap = getCapturedImage();
                rotateIfRequired(bitmap);

                // Sets bitmap to the image view for user to see their capture
                inputImageView.setImageBitmap(bitmap);




                // Send the bitmap to runDetection method for image detection
                runDetection(bitmap);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PICK_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Getting bitmap from user storage
                Bitmap takenImage = loadFromUri(data.getData());

                // Sets bitmap to the image view for user to see their capture
                inputImageView.setImageBitmap(takenImage);

                // Send the bitmap to runDetection method for image detection
                runDetection(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't selected!", Toast.LENGTH_SHORT).show();
            }
        }
    }









    /**
     * Draw bounding boxes around objects together with the object's name.
     */


    // Helps to declare the text to speech engine
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

    // Accepts a string, and uses the TextToSpeech to say it out loud
    private void speak(String message) {
        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    // Closes the text to speech engine
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }


}