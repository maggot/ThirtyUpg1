package com.example.axel.thirtyupg1;

import java.util.Random;

/**
 * Created by Axel on 6/27/2017.
 */

public class Die {
    private int value;
    private Random randomGen = new Random();

    public Die(){
        value = 1;
    }

    public int rollDie(){
        value = 1 + randomGen.nextInt(6);
        return value;
    }

    public int getValue(){
        return value;
    }
}
