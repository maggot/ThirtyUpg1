package se.umu.student.axever0002;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Axel on 6/28/2017.
 */

/**
 * GameHandler handles the state of the game, such as calculations
 * of score, management of the dice data and score.
 * GH is a custom object that implements parcelable since it needs to
 * be able to be stored in "savedInstanceState" when the main activity is recreated.
 */

class GameHandler implements Parcelable {

    private Die[] dice;
    private int numRolls = 0;
    boolean gameOn = false;
    private int score;
    private int[] scoreList = new int[13];

    //Constructor of the object GH, creates new instance of dice objects.
    GameHandler() {
        createDice();
    }

    //Calls rollDie for each of the 6 dice.
    void rollDice() {
        for (int i = 0; i < 6; i++) {
            dice[i].rollDie();
        }
        numRolls++;
    }

    //Creates new instances of Dice and place them in the local dice array.
    void createDice() {
        dice = new Die[]{new Die(), new Die(), new Die(), new Die(), new Die(), new Die()};
    }

    //Get the int value of a specific die(#num).
    int getDieVal(int num) {
        return dice[num].getValue();
    }

    //Set the state of a specific die(#num).
    void setDieState(int num) {
        dice[num].setSavedDie();
    }

    //Get the boolean value of a specific die(#num).
    boolean getDieState(int num) {
        return dice[num].getSavedDie();
    }

    //Get the int value of number rolls that has been made so far in this round.
    int getNumRolls() {
        return numRolls;
    }

    //Set the number of rolls made back to 0.
    void setNumRolls() {
        numRolls = 0;
    }

    //Set "gameOn" to either true or false
    void setGameOn(boolean req) {
        gameOn = req;
    }

    //Return the int value of the current score
    int getScore() {
        return score;
    }

    //Returns an array of the scores for each round and takes the total score as a parameter and adds it to the array
    int[] returnScoreList() {
        scoreList[10] = score;
        return scoreList;
    }

    //Algorithm that calculates the score. Cheating is prevented and illegal combinations will not yield any points
    //The user must select all specific dice that will be tallied up in the score calculation
    void calculateScore(String scoringOption) {
        int score = 0;
        int temp;
        int tempScore = 0;
        int val;

        if (!scoringOption.matches("Low")) {   //If the scoring option is something else than "Low":
            val = Integer.parseInt(scoringOption);      //Get a int value of the selected scoring option from the spinner.

            for (int i = 0; i < 6; i++) {                        //Iterate for each of the 6 dice.
                temp = dice[i].getValue();
                if (temp <= val) {                               //Check if the currently checked die's value is smaller than selected scoring option.
                    if (temp == val && dice[i].getSavedDie()) {     //Check whether the die is the exact same value as the selected scoring option and if it's saved
                        score += temp;                          //Add it's value to the score if above is true.
                    } else if (dice[i].getSavedDie()) {             //If the die is saved but doesn't fit above, add to tempScore
                        tempScore += temp;
                    }
                }
            }

            if (tempScore % val == 0) {     //If the tempScore from above is divisible with the scoringOption, add to score.
                score += tempScore;
            }
            val = val - 3;      //Since the scoringOption goes numerically from 4-12, we need to subtract 3 when adding them to the saved score array.

        } else {
            val = 0;
            for (int i = 0; i < 6; i++) {       //Iterate for each of the 6 dice.
                if (dice[i].getValue() <= 3) {      //If the die's value is smaller or equal to 3, add to score.
                    score += dice[i].getValue();
                }
            }
        }
        scoreList[val] = score;     //Tracking score for the final scoreboard.

        this.score += score;        //Add this methods collected score to the player's score.
    }

    /**
     * Following is (mostly) auto-generated code as part of implementing parcelable.
     */

    @Override
    public int describeContents() {
        return 0;
    }

    //Pack game data in parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        int[] savedDieVals = new int[7];
        boolean[] savedDieStates = new boolean[7];

        //Get die data and package into arrays.
        for (int i = 0; i < 6; i++) {
            savedDieVals[i] = getDieVal(i);
            savedDieStates[i] = getDieState(i);
        }

        //Add current game data into the parcel
        dest.writeIntArray(savedDieVals);
        dest.writeBooleanArray(savedDieStates);
        dest.writeInt(this.numRolls);
        dest.writeByte(this.gameOn ? (byte) 1 : (byte) 0);
        dest.writeIntArray(this.scoreList);
    }

    //Unpack game data from parcel
    private GameHandler(Parcel in) {

        createDice();   //Create new dice before we insert the packaged data into them.

        int[] savedDieVals = new int[7];
        boolean[] savedDieStates = new boolean[7];

        in.readIntArray(savedDieVals);
        in.readBooleanArray(savedDieStates);

        //Insert the parceled data into the die objects.
        for (int i = 0; i < 6; i++) {
            dice[i].setValue(savedDieVals[i]);
            if (savedDieStates[i]) {
                setDieState(i);
            }
        }

        //Insert the parceled data into the game state variables.
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
