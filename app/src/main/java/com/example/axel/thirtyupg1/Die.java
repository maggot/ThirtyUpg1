package com.example.axel.thirtyupg1;

import java.util.Random;

/**
 * Created by Axel on 6/27/2017.
 */

public class Die {
    private int value;
    private Random randomGen = new Random();
    private boolean savedDie = false;

    public Die(){
        value = 1;
    }

    public void rollDie(){
        if(!savedDie) value = 1 + randomGen.nextInt(6);
    }

    public int getValue(){
        return value;
    }

    public void setSavedDie(boolean state){
        savedDie = state;
    }

    public boolean getSavedDie(){
        return savedDie;
    }
}
