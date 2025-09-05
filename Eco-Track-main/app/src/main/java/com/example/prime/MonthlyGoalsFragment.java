package com.example.prime;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class MonthlyGoalsFragment extends Fragment {
    private RecyclerView goalsRecyclerView;
    private TextView daysLeft;
    private GoalAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monthly_goals, container, false);

        goalsRecyclerView = view.findViewById(R.id.goalsRecyclerView);
        daysLeft = view.findViewById(R.id.daysLeft);

        setupRecyclerView();
        updateDaysLeft();

        return view;
    }

    private void setupRecyclerView() {
        List<Goal> goals = Arrays.asList(
                new Goal("Reduce Daily Carbon Emission", 75, 100),
                new Goal("Use Public Transport", 4, 8),
                new Goal("Plant Trees", 3, 5)
        );

        adapter = new GoalAdapter(goals);
        goalsRecyclerView.setAdapter(adapter);
        goalsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void updateDaysLeft() {
        Calendar calendar = Calendar.getInstance();
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int remaining = daysInMonth - currentDay;
        daysLeft.setText(remaining + " days left");
    }
}