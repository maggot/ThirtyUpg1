package com.example.axel.thirtyupg1;

/**
 * Created by Axel on 6/28/2017.
 */

class GameHandler {

    private Die[] dice;
    private int numRolls = 0;
    boolean gameOn = false;

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

    void setGameOn(boolean req){
        gameOn = req;
    }

    int calculateScore(String scoringOption){
        int score = 0;
        int temp;
        int tempScore = 0;

        if(!scoringOption.matches("Low")) {
            int val = Integer.parseInt(scoringOption);

            for (int i = 0; i < 6; i++) {
                temp = dice[i].getValue();
                if (temp <= val) {
                    if(temp == val && dice[i].getSavedDie()){
                        score += temp;
                    } else if (dice[i].getSavedDie()){
                        tempScore += temp;
                    }
                }
            }

            if(tempScore%val == 0){
                score += tempScore;
            }

        } else{
            for (int i = 0; i < 6; i++) {
                if (dice[i].getValue() <= 3) {
                    score += dice[i].getValue();
                }
            }
        }
        return score;
    }
}
