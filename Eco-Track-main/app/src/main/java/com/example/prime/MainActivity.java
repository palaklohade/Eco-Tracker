package com.example.prime;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.prime.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        userId = getIntent().getStringExtra("userId");
        Log.d("MainActivity", "Received userId: " + userId);
        // Initialize binding and set the content view
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initially load the HomeFragment
        replaceFragment(new HomeFragment());

        // Set the BottomNavigationView item selection listener
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                Log.d("MainActivity", "Home selected");
                replaceFragment(new HomeFragment());

            } else if (itemId == R.id.group) {
                Log.d("MainActivity", "Group selected");
                replaceFragment(new CommunityFragment());

            } else if (itemId == R.id.eco) {
                Log.d("MainActivity", "Eco selected");
                replaceFragment(new RedeemFragment());

            } else if (itemId == R.id.insight) {
                Log.d("MainActivity", "Insight selected");
                replaceFragment(new InsightsFragment());

            } else if (itemId == R.id.setting) {
                Log.d("MainActivity", "Setting selected");
                replaceFragment(new WalletFragment());
            }
            return true;
        });
    }

    // Method to replace fragments
    // Method to replace fragments only if the new fragment is different from the current one
    private void replaceFragment(Fragment fragment) {
        // Pass userId to the fragment using Bundle
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);  // Replace with actual userId

        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Check if the currently displayed fragment is the same as the new fragment
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frameLayout);
        if (currentFragment != null && currentFragment.getClass().equals(fragment.getClass())) {
            Log.d("MainActivity", "Same fragment selected, no replacement needed.");
            return; // Do not replace if the same fragment is already displayed
        }

        // Replace the fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null); // Optional: Adds fragment to back stack for better navigation
        fragmentTransaction.commit();
    }


}
