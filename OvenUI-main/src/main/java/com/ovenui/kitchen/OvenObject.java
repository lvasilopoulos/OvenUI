package com.ovenui.kitchen;

public class OvenObject {

    private final MealState mealState;
    private int temperature;

    public OvenObject(){
        mealState = new MealState();
    }

    public MealState getMealState(){
        return mealState;
    }

    public int getTemperature(){
        return temperature;
    }

    public void setTemperature(int temperature){
        this.temperature = temperature;
    }

}
