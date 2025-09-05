package com.example.prime;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextFromImage extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private TextView tvResult;
    private TextView tvResult2;
    private Uri imageUri; // For storing the URI of the captured image
    private Integer recognizedText1 = 0;
    private Integer recognizedText2 = 0;
    private Button submit;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_from_image);

        // Initialize buttons and TextViews
        Button btnCaptureImage1 = findViewById(R.id.btnCaptureImage);  // First button
        Button btnCaptureImage2 = findViewById(R.id.btnCaptureImage2);  // Second button
        tvResult = findViewById(R.id.tvResult);  // TextView for displaying result of the first image
        tvResult2 = findViewById(R.id.tvResult2);  // TextView for displaying result of the second image

        submit = findViewById(R.id.submit);
        userId = getIntent().getStringExtra("userId");
        // Set click listeners for both buttons
        btnCaptureImage1.setOnClickListener(v -> captureImageFromCamera(1));  // Capture image for first button
        btnCaptureImage2.setOnClickListener(v -> captureImageFromCamera(2));  // Capture image for second button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoInsights();
            }
        });
    }

    private void gotoInsights() {
        // Ensure the recognized values are valid before proceeding
        if (recognizedText1 == null || recognizedText2 == null) {
            Toast.makeText(this, "Please capture both images before proceeding.", Toast.LENGTH_SHORT).show();
            return;
        }

        int difference = recognizedText2 - recognizedText1;

        // Create a new instance of the Fragment
        InsightsFragment insightsFragment = new InsightsFragment();

        // Pass data to the Fragment using a Bundle
        Bundle args = new Bundle();
        args.putString("userId", userId);
        //args.putInt("difference", difference);
        insightsFragment.setArguments(args);

        // Navigate to the Fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, insightsFragment) // Replace with your container ID
                .addToBackStack(null) // Optional: adds the transaction to the back stack
                .commit();
    }



    // Step 1: Open camera to take a photo
    private void captureImageFromCamera(int buttonId) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File photoFile = createImageFile();
                imageUri = FileProvider.getUriForFile(this,
                        "com.example.prime.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, buttonId == 1 ? REQUEST_IMAGE_CAPTURE : 2);  // Different request code for each button
            } catch (IOException ex) {
                Toast.makeText(this, "Error creating file: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Step 2: Handle the captured image result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                processUploadedImage(imageUri, tvResult, 1);  // Process the first captured image
            } else if (requestCode == 2) {
                processUploadedImage(imageUri, tvResult2, 2);  // Process the second captured image
            }
        }
    }

    // Step 3: Process the uploaded image (similar to how you processed the gallery image)
    private void processUploadedImage(Uri imageUri, TextView resultTextView, int buttonId) {
        try {
            InputImage image = InputImage.fromFilePath(this, imageUri);
            recognizeText(image, resultTextView, buttonId);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Step 4: Recognize text in the image
    private void recognizeText(InputImage image, TextView resultTextView, int buttonId) {
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image)
                .addOnSuccessListener(visionText -> {
                    StringBuilder resultText = new StringBuilder();
                    for (Text.TextBlock block : visionText.getTextBlocks()) {
                        String blockText = block.getText();
                        if (isRelevantReading(blockText)) {
                            resultText.append(blockText).append("\n");
                        }
                    }

                    Integer recognizedValue = tryParseInt(resultText.toString());

                    // Store the recognized text in the appropriate variable based on the button clicked
                    if (buttonId == 1) {
                        recognizedText1 = recognizedValue;
                        tvResult.setText(recognizedText1 != 0 ? recognizedText1.toString() : "No valid number found!");
                    } else if (buttonId == 2) {
                        recognizedText2 = recognizedValue;
                        tvResult2.setText(recognizedText2 != 0 ? recognizedText2.toString() : "No valid number found!");
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(this, "Text recognition failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private Integer tryParseInt(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return 0; // Return 0 if parsing fails
        }
    }


    // Step 5: Filter relevant readings
    private boolean isRelevantReading(String text) {
        return text.matches("\\d{1,5}") ||                // Numbers (e.g., odometer/speedometer)
                text.matches("[A-Z0-9\\-]{6,10}");         // Alphanumeric (e.g., license plates)
    }

    // Helper function to create an image file
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }
}

