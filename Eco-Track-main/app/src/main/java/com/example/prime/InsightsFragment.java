package com.example.prime;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InsightsFragment extends Fragment {

    private Button navigateButton;
    private ImageView notification;

    private ProgressBar greenPointsBar, totalCarbonBar, dailyEmissionBar;
    private String userId;
    private Integer difference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null) {
            userId = getArguments().getString("userId");
            //int difference = args.getInt("difference");
            Log.d("InsightsFragment", "Received userId: " + userId);
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_insights, container, false);

        // Initialize the navigate button and notification icon
        navigateButton = view.findViewById(R.id.button);
        notification = view.findViewById(R.id.notification_icon);
        dailyEmissionBar = view.findViewById(R.id.totalCarbonBar);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("totalEmissions");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    float totalEmission = dataSnapshot.getValue(Float.class);

                    // Calculate the progress value
                    int progress = calculateProgress(totalEmission);

                    // Set the progress on the ProgressBar
                    dailyEmissionBar.setProgress(progress);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getMessage());
            }
        });

        // Debugging logs to verify view initialization
        if (notification == null) {
            Log.e("InsightsFragment", "Notification icon is null. Check your layout file.");
        }

        if (navigateButton == null) {
            Log.e("InsightsFragment", "Navigate button is null. Check your layout file.");
        }

        // Set OnClickListener for navigate button to navigate to Activityf
        if (navigateButton != null) {
            navigateButton.setOnClickListener(v -> navigateToActivityf());
        }

        // Set OnClickListener for the notification icon to navigate to NotificationFragment
        if (notification != null) {
            notification.setOnClickListener(v -> navigateToNotification());
        }

        return view;  // Return the inflated view
    }

    private int calculateProgress(float totalEmission) {
        float maxEmission;

        // Determine the maxEmission threshold dynamically
        if (totalEmission <= 100) {
            maxEmission = 100; // Scale for hundreds
        } else if (totalEmission <= 1000) {
            maxEmission = 1000; // Scale for thousands
        } else {
            maxEmission = 10000; // Scale for tens of thousands or higher
        }

        // Normalize the value to a 0-100 scale
        float normalizedValue = (totalEmission / maxEmission) * 100;

        // Ensure the progress value is within bounds (0–100)
        return Math.min(100, Math.round(normalizedValue));
    }



    private void navigateToActivityf() {
        // Create a Bundle and add the userId to it
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);  // Replace with actual userId

        // Create the fragment and set its arguments
        Activityf activityfFragment = new Activityf();
        activityfFragment.setArguments(bundle);

        // Perform the fragment transaction
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, activityfFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    private void navigateToNotification() {
        // Create a Bundle and add the userId to it
        Bundle bundle = new Bundle();
        bundle.putString("userId", "YourUserIdHere");  // Replace with actual userId

        // Create the fragment and set its arguments
        NotificationFragment notificationFragment = new NotificationFragment();
        notificationFragment.setArguments(bundle);

        // Perform the fragment transaction
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, notificationFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
