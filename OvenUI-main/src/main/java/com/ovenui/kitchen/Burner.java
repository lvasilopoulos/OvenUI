package com.ovenui.kitchen;

public class Burner extends OvenObject{

    private String name;
    private int index;

    public Burner(String name, int index) {
        super();
        this.name = name;
        this.index = index;
    }

    public String getName(){
        return name;
    }
    public int getIndex(){ return index; }

}
