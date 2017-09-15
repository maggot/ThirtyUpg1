package com.example.axel.thirtyupg1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Axel on 6/28/2017.
 */

class GameHandler implements Parcelable {

    private Die[] dice;
    private int numRolls = 0;
    boolean gameOn = false;
    private int score;
    private int[] scoreList = new int[13];

    GameHandler() {
        resetDice();
    }

    //Calls rollDie for each of the 6 dice
    void rollDice() {
        for (int i = 0; i < 6; i++) {
            dice[i].rollDie();
        }
        numRolls++;
    }

    void resetDice() {
        dice = new Die[]{new Die(), new Die(), new Die(), new Die(), new Die(), new Die()};
    }

    int getDieVal(int num) {
        return dice[num].getValue();
    }

    void setDieState(int num) {
        dice[num].setSavedDie();
    }

    boolean getDieState(int num) {
        return dice[num].getSavedDie();
    }

    int getNumRolls() {
        return numRolls;
    }

    void setNumRolls() {
        numRolls = 0;
    }

    void setGameOn(boolean req) {
        gameOn = req;
    }

    int getScore() {
        return score;
    }

    //Returns an array of the scores for each round and takes the total score as a parameter and adds it to the array
    int[] returnScoreList(int totScore) {
        scoreList[10] = totScore;
        return scoreList;
    }

    //Algorithm that calculates the score. Cheating is prevented and illegal combinations will not yield any points
    //The user must select all specific dice that will be tallied up in the score calculation
    int calculateScore(String scoringOption) {
        int score = 0;
        int temp;
        int tempScore = 0;
        int val;

        if (!scoringOption.matches("Low")) {
            val = Integer.parseInt(scoringOption);

            for (int i = 0; i < 6; i++) {
                temp = dice[i].getValue();
                if (temp <= val) {
                    if (temp == val && dice[i].getSavedDie()) {
                        score += temp;
                    } else if (dice[i].getSavedDie()) {
                        tempScore += temp;
                    }
                }
            }

            if (tempScore % val == 0) {
                score += tempScore;
            }
            val = val - 3;

        } else {
            val = 0;
            for (int i = 0; i < 6; i++) {
                if (dice[i].getValue() <= 3) {
                    score += dice[i].getValue();
                }
            }
        }
        //Tracking score for the final scoreboard
        scoreList[val] = score;

        this.score = score;
        return score;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        int[] savedDieVals = new int[7];
        boolean[] savedDieStates = new boolean[7];

        for (int i = 0; i < 6; i++) {
            savedDieVals[i] = getDieVal(i); //dice[i].getValue();
            savedDieStates[i] = getDieState(i); //dice[i].getSavedDie();
        }

        dest.writeIntArray(savedDieVals);
        dest.writeBooleanArray(savedDieStates);
        //dest.writeParcelable(this.dice, flags);
        dest.writeInt(this.numRolls);
        dest.writeByte(this.gameOn ? (byte) 1 : (byte) 0);
        dest.writeIntArray(this.scoreList);
    }

    protected GameHandler(Parcel in) {

        resetDice();

        int[] savedDieVals = new int[7];
        boolean[] savedDieStates = new boolean[7];

        in.readIntArray(savedDieVals);
        in.readBooleanArray(savedDieStates);

        for (int i = 0; i < 6; i++) {
            dice[i].setValue(savedDieVals[i]);
            if (savedDieStates[i]) {
                setDieState(i);
            }
        }

        this.numRolls = in.readInt();
        this.gameOn = in.readByte() != 0;
        this.scoreList = in.createIntArray();
    }

    public static final Parcelable.Creator<GameHandler> CREATOR = new Parcelable.Creator<GameHandler>() {
        @Override
        public GameHandler createFromParcel(Parcel source) {
            return new GameHandler(source);
        }

        @Override
        public GameHandler[] newArray(int size) {
            return new GameHandler[size];
        }
    };
}
