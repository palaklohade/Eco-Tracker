package com.example.prime;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment {

    // Declare the variables for the "Like" buttons and their count
    private TextView tip1LikeCount;
    private Button tip1LikeButton;
    private int tip1Likes = 0; // Initialize like count for Tip 1

    private TextView tip2LikeCount;
    private Button tip2LikeButton;
    private int tip2Likes = 0; // Initialize like count for Tip 2

    private TextView tip3LikeCount;
    private Button tip3LikeButton;
    private int tip3Likes = 0; // Initialize like count for Tip 3

    private ImageView navigateButton; // Declare the navigation button
    private String userId;
    private Button viewTrackingHistory;
    private ImageView profileIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        if (getArguments() != null) {
            userId = getArguments().getString("userId");
            Log.d("HomeFragment", "Received userId: " + userId);
        }
        viewTrackingHistory = view.findViewById(R.id.view_tracking_history_button);
        viewTrackingHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHistory();
            }
        });
        // Initialize the "Like" buttons and count displays
        tip1LikeCount = view.findViewById(R.id.tip1_like);
        tip1LikeButton = view.findViewById(R.id.tip1_like);

        tip2LikeCount = view.findViewById(R.id.tip2_like);
        tip2LikeButton = view.findViewById(R.id.tip2_like);

        tip3LikeCount = view.findViewById(R.id.tip3_like);
        tip3LikeButton = view.findViewById(R.id.tip3_like);
        profileIcon = view.findViewById(R.id.profile_icon);
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Replace the fragment in the container (e.g., R.id.frameLayout) with NotificationFragment
                fragmentTransaction.replace(R.id.frameLayout, new ProfileFragment());
                fragmentTransaction.addToBackStack(null); // Optional: Adds fragment to back stack
                fragmentTransaction.commit(); // Commit the transaction
            }
        });

        // Set OnClickListeners for the "Like" buttons
        tip1LikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tip1Likes++; // Increase like count for Tip 1
                tip1LikeCount.setText(String.valueOf(tip1Likes)); // Update the count display
            }
        });

        tip2LikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tip2Likes++; // Increase like count for Tip 2
                tip2LikeCount.setText(String.valueOf(tip2Likes)); // Update the count display
            }
        });

        tip3LikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tip3Likes++; // Increase like count for Tip 3
                tip3LikeCount.setText(String.valueOf(tip3Likes)); // Update the count display
            }
        });

        // Initialize the navigation button
        navigateButton = view.findViewById(R.id.notification_icon); // Ensure the button ID matches your XML

        // Set an OnClickListener for the navigation button
        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToActivityf();
            }
        });

        return view; // Return the inflated view
    }

    private void increaseLikeCount(int tipNumber) {
        // Update the like count based on the tip number
        if (tipNumber == 1) {
            tip1Likes++;
            tip1LikeCount.setText(tip1Likes + " 👍");
            Toast.makeText(getActivity(), "You liked Tip 1", Toast.LENGTH_SHORT).show();
        } else if (tipNumber == 2) {
            tip2Likes++;
            tip2LikeCount.setText(tip2Likes + " 👍");
            Toast.makeText(getActivity(), "You liked Tip 2", Toast.LENGTH_SHORT).show();
        } else if (tipNumber == 3) {
            tip3Likes++;
            tip3LikeCount.setText(tip3Likes + " 👍");
            Toast.makeText(getActivity(), "You liked Tip 3", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToActivityf() {
        // Replace current fragment (HomeFragment) with NotificationFragment
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the fragment in the container (e.g., R.id.frameLayout) with NotificationFragment
        fragmentTransaction.replace(R.id.frameLayout, new NotificationFragment());
        fragmentTransaction.addToBackStack(null); // Optional: Adds fragment to back stack
        fragmentTransaction.commit(); // Commit the transaction
    }
    private void navigateToHistory() {
        // Replace current fragment (HomeFragment) with NotificationFragment
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the fragment in the container (e.g., R.id.frameLayout) with NotificationFragment
        fragmentTransaction.replace(R.id.frameLayout, new HistoryFragment());
        fragmentTransaction.addToBackStack(null); // Optional: Adds fragment to back stack
        fragmentTransaction.commit(); // Commit the transaction
    }
}
