package com.example.axel.thirtyupg1;

import java.util.Random;

/**
 * Created by Axel on 6/27/2017.
 */

class Die {
    private int value;
    private Random randomGen = new Random();
    private boolean savedDie = false;

    Die(){
        value = 1;
    }

    void rollDie(){
        if(!savedDie) value = 1 + randomGen.nextInt(6);
    }

    int getValue(){
        return value;
    }

    void setSavedDie(){
        savedDie = !savedDie;
    }

    boolean getSavedDie(){
        return savedDie;
    }
}
