package com.example.axel.thirtyupg1;

/**
 * Created by Axel on 6/28/2017.
 */

public class GameHandler {

    private Die[] dice;

    public GameHandler(){
        resetDice();
    }

    public void rollDice(){
        for(int i = 0;i < 6;i++){
            dice[i].rollDie();

        }
    }

    public void resetDice(){
        dice = new Die[] {new Die(), new Die(),new Die(),new Die(),new Die(),new Die()};
    }

    public int getDieVal(int num){
        return dice[num].getValue();
    }

    public void setDieState(int num, boolean state){
        dice[num].setSavedDie(state);
    }

    public boolean getDieState(int num){
        return dice[num].getSavedDie();
    }

}
