package com.example.prime;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class ActivityPage extends AppCompatActivity {

    Button arrowback;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        arrowback = findViewById(R.id.backtoinsight);

        // Set an onClick listener for the button
        arrowback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to InsightsFragment
                getSupportFragmentManager().popBackStack(); // This pops the fragment from the back stack
            }
        });
    }
}
