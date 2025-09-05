package com.example.prime;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class ProfileFragment extends Fragment {

    private ImageView backtohome;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Initialize the back button
        backtohome = view.findViewById(R.id.profile_icon); // Ensure this ID matches your button in XML

        // Set up the back button listener
        backtohome.setOnClickListener(new View.OnClickListener() {
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