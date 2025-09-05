package com.example.prime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {
    private List<Goal> goals;

    public GoalAdapter(List<Goal> goals) {
        this.goals = goals;
    }

    @Override
    public GoalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goal_item, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GoalViewHolder holder, int position) {
        Goal goal = goals.get(position);
        holder.goalTitle.setText(goal.getTitle());
        holder.goalProgress.setText(goal.getProgressPercentage() + "%");
        holder.goalProgressBar.setProgress(goal.getProgressPercentage());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class GoalViewHolder extends RecyclerView.ViewHolder {
        TextView goalTitle, goalProgress;
        ProgressBar goalProgressBar;

        GoalViewHolder(View view) {
            super(view);
            goalTitle = view.findViewById(R.id.goalTitle);
            goalProgress = view.findViewById(R.id.goalProgress);
            goalProgressBar = view.findViewById(R.id.goalProgressBar);
        }
    }
}
