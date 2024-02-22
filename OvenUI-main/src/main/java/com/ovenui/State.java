package com.ovenui;

import com.ovenui.kitchen.Burner;
import com.ovenui.kitchen.OvenObject;

import java.util.ArrayList;
import java.util.List;

public class State {

    List<Burner> burners;
    OvenObject oven;
    String program;

    private static State instance = null;

    private State() {
        // initialize the burners
        burners = new ArrayList<>();
        burners.add(new Burner("TopRight", 0));
        burners.add(new Burner("TopLeft", 1));
        burners.add(new Burner("BottomRight", 2));
        burners.add(new Burner("BottomLeft", 3));
        // initialize the oven
        program = "";
        oven = new OvenObject();
    }

    public static State getInstance() {
        if(instance == null) {
            synchronized (State.class) {
                if (instance == null) {
                    instance = new State();
                }
            }
        }
        return instance;
    }

}
