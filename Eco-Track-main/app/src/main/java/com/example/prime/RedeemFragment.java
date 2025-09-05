package com.example.prime;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


public class RedeemFragment extends Fragment {


    private ImageView notified;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_redeem, container, false);
        // Initialize the back button
        notified = view.findViewById(R.id.notifi);

        // Set up the back button listener
        notified.setOnClickListener(new View.OnClickListener() {
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
        fragmentTransaction.replace(R.id.frameLayout, new NotificationFragment());
        fragmentTransaction.addToBackStack(null); // Optional: Adds fragment to back stack
        fragmentTransaction.commit();  // Commit the transaction
    }


}