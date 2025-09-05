package com.example.prime;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class NotificationFragment extends Fragment {

    private ImageView backarrow;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        // Initialize the back button
        backarrow = view.findViewById(R.id.backarrow); // Ensure this ID matches your button in XML

        // Set up the back button listener
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to InsightsFragment when back button is clicked
                navigateBackToInsights();
            }
        });

        return view;  // Return the inflated view
    }

    private void navigateBackToInsights() {
        // Replace current fragment with InsightsFragment
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the fragment in the container (frameLayout) with InsightsFragment
        fragmentTransaction.replace(R.id.frameLayout, new HomeFragment());
        fragmentTransaction.addToBackStack(null); // Optional: Adds fragment to back stack
        fragmentTransaction.commit();  // Commit the transaction
    }
}