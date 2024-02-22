package com.ovenui.kitchen;

public class MealState {

    private long timestamp;
    private long timer;

    private boolean isActive;
    private boolean hasTimer = false;
    private boolean hasFinished;

    public MealState(){ }

    public boolean activate(){
        // Sets the timestamp on UTC time
        isActive = true;
        return true;
    }

    public void deactivate(){
        isActive = false;
        hasTimer = false;
        hasFinished = false;
        timestamp = 0;
    }

    public void set() {
        hasTimer = false;
        timestamp = System.currentTimeMillis() / 1000;
    }

    public void setTimer(int minutes, int seconds){
        hasTimer = true;
        timestamp = System.currentTimeMillis() / 1000;
        timer = timestamp + minutes * 60 + seconds;
    }

    public void tick() {
        if(hasTimer) {
            if(System.currentTimeMillis() / 1000 == timer) {
                hasFinished = true;
                isActive = false;
            }
        }
    }

    public int getProgress() {
        long current = System.currentTimeMillis() / 1000;
        if(timer <= current) return 100;
        long currentTimeSeconds = current - timestamp;
        if(currentTimeSeconds <= 0) return 0;
        long timerSeconds = timer - timestamp;
        return (int)(currentTimeSeconds * 100 / timerSeconds);
    }

    public long getTimeLapsed() {
        return System.currentTimeMillis() / 1000 - timestamp;
    }

    public long getRemainingTime() {
        if(!hasTimer) return 0;
        return hasFinished? 0 : timer - System.currentTimeMillis() / 1000;
    }

    public int colorState(){
        if(!isActive && !hasFinished) return 0;
        if(isActive && !hasFinished || isActive && !hasTimer) return 1;
        return 2;
    }

    public boolean isActive(){
        return isActive || hasFinished;
    }

    public boolean getFinished(){
        return hasFinished;
    }

    public boolean hasTimer() { return hasTimer; }
}
