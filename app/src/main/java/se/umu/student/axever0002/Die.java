package se.umu.student.axever0002;

import java.util.Random;

/**
 * Created by Axel on 6/27/2017.
 */


// A Die object to represent the non-graphical side of the dice
// and is used by the GameHandler

class Die {
    private int value;
    private Random randomGen = new Random();
    private boolean savedDie = false;

    //The die object is instantiated to 1
    Die() {
        value = 1;
    }

    //Random generator gives the die a new value between 1-6 upon call
    void rollDie() {
        if (!savedDie) value = 1 + randomGen.nextInt(6);
    }

    //Sets a int value to the die
    void setValue(int val) {
        value = val;
    }

    //Gets the int value of a die
    int getValue() {
        return value;
    }

    //Toggles the die's boolean savedDie state
    void setSavedDie() {
        savedDie = !savedDie;
    }

    //Gets the boolean state of the die
    boolean getSavedDie() {
        return savedDie;
    }
}
