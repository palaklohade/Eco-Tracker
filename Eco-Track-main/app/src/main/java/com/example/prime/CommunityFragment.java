package com.example.prime;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CommunityFragment extends Fragment {

    private ImageView gogroup;
    private ImageView communitynotification;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null) {
            userId = getArguments().getString("userId");
            Log.d("HomeFragment", "Received userId: " + userId);
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        // Initialize views
        gogroup = view.findViewById(R.id.gotogroup);
        communitynotification = view.findViewById(R.id.communitynotification);

        // Set click listener for 'gogroup'
        gogroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBackToInsights();
            }
        });

        // Set click listener for 'communitynotification'
        communitynotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToNotification();
            }
        });

        return view; // Return the inflated view
    }

    private void navigateBackToInsights() {
        // Navigate to GroupsFragment
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, new GroupsFragment());
        fragmentTransaction.addToBackStack(null); // Optional: Adds fragment to back stack
        fragmentTransaction.commit(); // Commit the transaction
    }

    private void navigateToNotification() {
        // Navigate to NotificationFragment
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, new NotificationFragment());
        fragmentTransaction.addToBackStack(null); // Optional: Adds fragment to back stack
        fragmentTransaction.commit(); // Commit the transaction
    }
}
