package com.example.prime;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.google.android.material.textfield.TextInputLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Activityf extends Fragment {

    // Views for travel emissions
    private Bitmap capturedImage;
    private LinearLayout buttonTravel;
    private TextView textViewTravelEmissions, textViewTotalEmissions;
    private EditText inputCar, inputBike, inputPublicTransport, inputTrainTransport, inputElectricTransport;
    private LinearLayout dropdownTravel;
    private Button buttonSubmitTravel;

    // Views for energy usage
    private LinearLayout buttonEnergyUsage;
    private EditText inputElectricity, inputGas, inputSolar, inputAirConditioner, inputHeater, inputWashingMachine;
    private TextView textViewEnergyEmissions;
    private LinearLayout dropdownEnergyUsage;
    private Button buttonSubmitEnergy;

    // Back button for navigation
    private ImageView backButton;

    // Icon and Text for the travel button
    private ImageView travelIcon;
    private TextView travelText;
    private DatabaseReference databaseReference;
    private String userId;

    // ImageView to display the captured image
    private ImageView imageView;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for the fragment
        View view = inflater.inflate(R.layout.fragment_activityf, container, false);

        if (getArguments() != null) {
            userId = getArguments().getString("userId");
            Log.d("Activity 'f' Fragment", "Received userId: " + userId);
        }

        // Initialize travel views
        buttonTravel = view.findViewById(R.id.button2);
        travelIcon = buttonTravel.findViewById(R.id.icon);
        travelText = buttonTravel.findViewById(R.id.text);
        dropdownTravel = view.findViewById(R.id.dropdownTravel);
        buttonSubmitTravel = view.findViewById(R.id.buttonSubmitTravel);
        inputCar = view.findViewById(R.id.inputCar);
        inputBike = view.findViewById(R.id.inputBike);
        inputPublicTransport = view.findViewById(R.id.inputPublicTransport);
        inputTrainTransport = view.findViewById(R.id.inputTrainTransport);
        inputElectricTransport = view.findViewById(R.id.inputElectricTransport);
        inputAirConditioner = view.findViewById(R.id.inputAirConditioner);
        inputHeater = view.findViewById(R.id.inputHeater);
        inputWashingMachine = view.findViewById(R.id.inputWashingMachine);
        textViewTravelEmissions = view.findViewById(R.id.textView19);
        textViewTotalEmissions = view.findViewById(R.id.textView17);

        // Initialize energy views
        buttonEnergyUsage = view.findViewById(R.id.button3);
        dropdownEnergyUsage = view.findViewById(R.id.dropdownEnergyUsage);
        buttonSubmitEnergy = view.findViewById(R.id.buttonSubmitEnergy);
        inputElectricity = view.findViewById(R.id.inputelectricity);
        inputGas = view.findViewById(R.id.inputgas);
        inputSolar = view.findViewById(R.id.inputsolar);
        textViewEnergyEmissions = view.findViewById(R.id.textView21);

        // Initialize back button
        backButton = view.findViewById(R.id.backtoinsight);

        // Initialize imageView
        imageView = view.findViewById(R.id.imageView); // Replace with the actual ID

        // Set up button listeners
        buttonTravel.setOnClickListener(v -> navToTextFromImage());
        buttonEnergyUsage.setOnClickListener(v -> toggleDropdown(dropdownEnergyUsage));

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        buttonSubmitTravel.setOnClickListener(v -> {
            calculateTravelEmissions();
            clearInputs("travel");
        });
        buttonSubmitEnergy.setOnClickListener(v -> {
            calculateEnergyEmissions();
            clearInputs("energy");
        });
        backButton.setOnClickListener(v -> navigateBackToInsights());

        // Add this in your onCreateView() method to initialize the button
        Button cameraUploadButton = view.findViewById(R.id.buttonCameraUpload);
        cameraUploadButton.setOnClickListener(v -> openCamera());

        Button cameraUploadButton1 = view.findViewById(R.id.buttonCameraUpload1);
        cameraUploadButton1.setOnClickListener(v -> openCamera());

        return view;
    }

    private void navToTextFromImage() {
        Intent i = new Intent(getActivity(), TextFromImage.class);
        i.putExtra("userId",userId);
        startActivity(i);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the camera
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            } else {
                // Permission denied, show a message
                Toast.makeText(getContext(), "Camera permission is required", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with storage-related operations
                // (e.g., saving the captured image)
            } else {
                // Permission denied, show a message
                Toast.makeText(getContext(), "Storage permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void toggleDropdown(LinearLayout dropdown) {
        // Hide other dropdowns if visible
        if (dropdownTravel.getVisibility() == View.VISIBLE && dropdown != dropdownTravel) {
            dropdownTravel.setVisibility(View.GONE);
        }
        if (dropdownEnergyUsage.getVisibility() == View.VISIBLE && dropdown != dropdownEnergyUsage) {
            dropdownEnergyUsage.setVisibility(View.GONE);
        }

        // Toggle the selected dropdown
        dropdown.setVisibility(dropdown.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    private void calculateTravelEmissions() {
        try {
            // Retrieve user inputs and handle missing values
            double carDistance = 0.0, bikeDistance = 0.0, publicTransportDistance = 0.0, trainDistance = 0.0, electricScooterDistance = 0.0;

            if (!inputCar.getText().toString().isEmpty()) {
                carDistance = Double.parseDouble(inputCar.getText().toString());
            }

            if (!inputBike.getText().toString().isEmpty()) {
                bikeDistance = Double.parseDouble(inputBike.getText().toString());
            }

            if (!inputPublicTransport.getText().toString().isEmpty()) {
                publicTransportDistance = Double.parseDouble(inputPublicTransport.getText().toString());
            }

            if (!inputTrainTransport.getText().toString().isEmpty()) {
                trainDistance = Double.parseDouble(inputTrainTransport.getText().toString());
            }
            if (!inputElectricTransport.getText().toString().isEmpty()) {
                electricScooterDistance = Double.parseDouble(inputElectricTransport.getText().toString());
            }

            // Calculate emissions (example calculation: 0.12 tons of CO2 per km for car)
            // **Replace with accurate emission factors**
            double carEmissions = carDistance * 0.12;
            double trainEmissions = trainDistance * 0.014;
            double electricScooter = electricScooterDistance * 0.02;
            double bikeEmissions = bikeDistance * 0.05;
            double publicTransportEmissions = publicTransportDistance * 0.08;

            double evEnergyConsumption = 0.15;  // kWh per km for EVs
            double gridEmissionFactor = 0.4;   // kg CO

            double evDistance = 0.0;  // Distance traveled by EV in kilometers

            // Calculate emissions for EV or hybrid vehicles
            double evEmissions = evDistance * evEnergyConsumption * gridEmissionFactor;

            // Update the total emissions calculation
            double totalTravelEmissions = carEmissions + bikeEmissions + publicTransportEmissions +
                    trainEmissions + electricScooter + evEmissions;

            totalTravelEmissions = Math.round(totalTravelEmissions * 1000.0) / 1000.0;

            // Display results for travel
            textViewTravelEmissions.setText("Travel Emissions: " + totalTravelEmissions + " tons of CO2");

            // Upload the image if captured
            if (capturedImage != null) {
                // You can now upload the image to your database or server, or display it in an ImageView
                imageView.setImageBitmap(capturedImage); // Display the captured image in an ImageView

                // Optionally upload to Firebase or other servers
                uploadImageToDatabase(capturedImage);
            }

            // Update total emissions
            databaseReference.child(userId).child("TravelEmissions").setValue(totalTravelEmissions);

            // Hide the dropdown after submission
            dropdownTravel.setVisibility(View.GONE);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter valid values for all fields.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openCamera() {
        // Check for camera permission before opening the camera
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        }
    }


    @Deprecated
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Get the image captured by the camera
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            capturedImage = photo; // Store the image for later use
            imageView.setImageBitmap(capturedImage); // Display the captured image in ImageView

            // Upload the image to Firebase
            if (capturedImage != null) {
                uploadImageToDatabase(capturedImage);
            }
        }
    }

    private void uploadImageToDatabase(Bitmap image) {
        // Convert Bitmap to Base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataBytes = baos.toByteArray();
        String base64Image = Base64.encodeToString(dataBytes, Base64.DEFAULT);

        // Get Firebase Realtime Database reference
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        // Create a unique ID for the image
        String imageId = UUID.randomUUID().toString();

        // Create a map to store image data
        Map<String, Object> imageData = new HashMap<>();
        imageData.put("image", base64Image);
        imageData.put("timestamp", System.currentTimeMillis());

        // Upload image data to Realtime Database
        databaseRef.child("images").child(imageId).setValue(imageData)
                .addOnSuccessListener(aVoid -> {
                    // Image uploaded successfully
                    Toast.makeText(getContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    // Optionally update UI
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(getContext(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Optionally handle the error or retry logic
                });
    }




    private void calculateEnergyEmissions() {
        try {
            // Retrieve user inputs and handle missing values
            double electricity = 0.0, gas = 0.0, solar = 0.0, AirConditioner = 0.0, Heater = 0.0, WashingMachine = 0.0;

            if (!inputElectricity.getText().toString().isEmpty()) {
                electricity = Double.parseDouble(inputElectricity.getText().toString());
            }

            if (!inputGas.getText().toString().isEmpty()) {
                gas = Double.parseDouble(inputGas.getText().toString());
            }

            if (!inputSolar.getText().toString().isEmpty()) {
                solar = Double.parseDouble(inputSolar.getText().toString());
            }
            if (!inputAirConditioner.getText().toString().isEmpty()) {
                AirConditioner = Double.parseDouble(inputAirConditioner.getText().toString());
            }
            if (!inputHeater.getText().toString().isEmpty()) {
                Heater = Double.parseDouble(inputHeater.getText().toString());
            }
            if (!inputWashingMachine.getText().toString().isEmpty()) {
                WashingMachine = Double.parseDouble(inputWashingMachine.getText().toString());
            }

            // Example emissions calculation: 0.5 tons of CO2 per unit for electricity, gas, etc.
            // **Replace with accurate emission factors**
            double electricityEmissions = electricity * 0.5;
            double gasEmissions = gas * 0.3;
            double solarEmissions = solar * 0.05;
            double AirConditionerEmissions = AirConditioner * 1.46;
            double HeaterEmissions = Heater * 1.23;
            double WashingMachineEmissions = WashingMachine * 0.41;

            // Calculate total emissions for energy
            double totalEnergyEmissions = electricityEmissions + gasEmissions - solarEmissions + AirConditionerEmissions + HeaterEmissions + WashingMachineEmissions; // solar reduces emissions

            // Display results for energy
            totalEnergyEmissions = Math.round(totalEnergyEmissions * 1000.0) / 1000.0;
            textViewEnergyEmissions.setText("Energy Emissions: " + totalEnergyEmissions + " tons of CO2");
            databaseReference.child(userId).child("EnergyEmissions").setValue(totalEnergyEmissions);

            // Update total emissions
            updateTotalEmissions(totalEnergyEmissions);

            // Hide the dropdown after submission
            dropdownEnergyUsage.setVisibility(View.GONE);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter valid values for all fields.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTotalEmissions(double emissions) {
        // Retrieve the current total emissions displayed
        double currentTotalEmissions = 0.0;
        String currentTotalText = textViewTotalEmissions.getText().toString();

        if (!currentTotalText.isEmpty()) {
            try {
                // Extract the numerical part from the text
                currentTotalEmissions = Double.parseDouble(currentTotalText.replace("Total Emissions: ", "").replace(" tons of CO2", ""));
            } catch (NumberFormatException e) {
                // Handle any parsing errors gracefully
            }
        }

        // Update the total emissions
        double newTotalEmissions = currentTotalEmissions + emissions;

        newTotalEmissions = Math.round(newTotalEmissions * 1000.0) / 1000.0;

        textViewTotalEmissions.setText("Total Emissions: " + newTotalEmissions + " tons of CO2");
        databaseReference.child(userId).child("totalEmissions").setValue(newTotalEmissions);
    }

    private void clearInputs(String section) {
        if (section.equals("travel")) {
            inputCar.setText("");
            inputBike.setText("");
            inputPublicTransport.setText("");
        } else if (section.equals("energy")) {
            inputElectricity.setText("");
            inputGas.setText("");
            inputSolar.setText("");
        }
    }

    private void navigateBackToInsights() {
        // Logic to navigate back to the previous fragment or activity
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.popBackStack();
    }
}