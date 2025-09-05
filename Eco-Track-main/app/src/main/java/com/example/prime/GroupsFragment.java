package com.example.prime;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


public class GroupsFragment extends Fragment {


    private ImageView backgroup;
    private String userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (getArguments() != null) {
            userId = getArguments().getString("userId");
            Log.d("GroupsFragment", "Received userId: " + userId);
        }
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        // Initialize the back button
        backgroup = view.findViewById(R.id.backtocommunity);

        // Set up the back button listener
        backgroup.setOnClickListener(new View.OnClickListener() {
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
        fragmentTransaction.replace(R.id.frameLayout, new CommunityFragment());
        fragmentTransaction.addToBackStack(null); // Optional: Adds fragment to back stack
        fragmentTransaction.commit();  // Commit the transaction
    }
}