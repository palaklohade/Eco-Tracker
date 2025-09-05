package com.example.prime;

public class Goal {
    private String title;
    private int currentProgress;
    private int target;

    public int getTarget() {
        return target;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public Goal(String title, int currentProgress, int target) {
        this.title = title;
        this.currentProgress = currentProgress;
        this.target = target;
    }

    public int getProgressPercentage() {
        return (currentProgress * 100) / target;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }
}