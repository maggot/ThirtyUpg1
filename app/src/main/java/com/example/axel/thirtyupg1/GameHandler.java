package com.example.axel.thirtyupg1;

/**
 * Created by Axel on 6/28/2017.
 */

class GameHandler {

    private Die[] dice;
    private int numRolls = 0;

    GameHandler(){
        resetDice();
    }

    void rollDice(){
        for(int i = 0;i < 6;i++){
            dice[i].rollDie();
        }
        numRolls++;
    }

    void resetDice(){
        dice = new Die[] {new Die(), new Die(),new Die(),new Die(),new Die(),new Die()};
    }

    int getDieVal(int num){
        return dice[num].getValue();
    }

    void setDieState(int num){
        dice[num].setSavedDie();
    }

    boolean getDieState(int num){
        return dice[num].getSavedDie();
    }

    int getNumRolls(){
        return numRolls;
    }

    void setNumRolls(){ numRolls = 0; }
}
