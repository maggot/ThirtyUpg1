package com.example.axel.thirtyupg1;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Axel on 6/26/2017.
 */

public class GameActivity extends AppCompatActivity {

    private GameHandler gh;
    private int[] dice, white, grey, red;
    private Button rollBtn, nxtRoundBtn;
    private TextView scoreView ;
    private TextView numRollsView;
    private Spinner spinner;
    private ArrayAdapter<String> spinAdapter;
    private int score = 0;

    //Part of my unsuccessful attempt at recovering score data..
//    private String prev_score_key = "scoreValKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initiates an instance of gameHandler
        gh = new GameHandler();

        //Fetches text ImageView IDs
        numRollsView = (TextView) findViewById(R.id.numRolls_view);
        scoreView = (TextView) findViewById(R.id.score_view);

        //An attempt at recovering the score data once an activity had been recreated.
        //Yet with the methodology i chose for implementing this task, doing this proved to be a bit hard for me to do since
        //as far as i know i would have to save and overwrite the objects etc to do implement the functionality fully.
        //I could not find any comprehensible guides that I could follow for this.

//        if(savedInstanceState != null){
//            CharSequence savedScore = savedInstanceState.getCharSequence(prev_score_key);
//            scoreView.setText(savedScore);
//        }

        //Fetching die ImageViews IDs
        dice = new int [] {R.id.die_11, R.id.die_12, R.id.die_13, R.id.die_21, R.id.die_22, R.id.die_23, };

        //Fetching die image resources
        white = new int[] {R.drawable.white1, R.drawable.white2, R.drawable.white3, R.drawable.white4, R.drawable.white5, R.drawable.white6};
        grey = new int[] {R.drawable.grey1, R.drawable.grey2, R.drawable.grey3, R.drawable.grey4, R.drawable.grey5, R.drawable.grey6};
        red = new int[] {R.drawable.red1, R.drawable.red2, R.drawable.red3, R.drawable.red4, R.drawable.red5, R.drawable.red6};

        //Fetching a string array from res/string.xml and setting the items to the spinner-menu
        Resources res = getResources();
        String[] spinnerOptions = res.getStringArray(R.array.spinner_options);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(Arrays.asList(spinnerOptions)));
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinAdapter);

        //Setting listeners to all the dice ImageViews
        for(int i = 0; i < 6; i++){
            ImageView dieImg = (ImageView) findViewById(dice[i]);

            final int finalI = i;
            dieImg.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    //If the game has not yet been started the dice will not be clickable
                    if(!gh.gameOn) return;
                    gh.setDieState(finalI);
                    dieRefresh();
                }
            });
        }

        //Create a listener for the roll-button with corresponding method-calls
        rollBtn = (Button) findViewById(R.id.roll_btn);
        rollBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                gh.setGameOn(true);
                gh.rollDice();
                dieRefresh();
                numRollsView.setText(Integer.toString(3-gh.getNumRolls()) + " left"); //should be num rolls

                //When the user has rolled the dice 3 times, the button will be disabled
                if(gh.getNumRolls() >= 3){
                    Toast.makeText(GameActivity.this, "Select dice you want to score", Toast.LENGTH_SHORT).show();
                    rollBtn.setEnabled(false);
                    nxtRoundBtn.setEnabled(true);
                }
            }
        });

        //Creates a listener for the next round-button.
        nxtRoundBtn = (Button) findViewById(R.id.next_round);
        nxtRoundBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Will present a toast if uset has not selected an option from the spinner
                if(spinner.getSelectedItemPosition() == 0){
                    Toast.makeText(GameActivity.this, "Please choose an option in the drop-down menu", Toast.LENGTH_SHORT).show();
                } else {
                    //Count up the score for this round and update the TextView
                    score += gh.calculateScore(spinner.getSelectedItem().toString());
                    scoreView.setText("Score: " + Integer.toString(score));

                    //Remove used item from the spinner
                    spinAdapter.remove((String) spinner.getSelectedItem());
                    spinAdapter.notifyDataSetChanged();
                    newRound();
                }
            }
        });

        newRound();
    }


    //Attempt at recovering the score when activity is recreated.

//    @Override
//    protected void onSaveInstanceState(Bundle outState){
//        super.onSaveInstanceState(outState);
//        outState.putCharSequence(prev_score_key, scoreView.getText());
//
//    }

    //Refresh the images for all dice (grey for selected dice)
    private void dieRefresh(){
        for(int i = 0;i < 6;i++){
            if(gh.getDieState(i)) {
                ImageView die = (ImageView) findViewById(dice[i]);
                die.setImageResource(grey[gh.getDieVal(i) - 1]);
            } else {
                ImageView die = (ImageView) findViewById(dice[i]);
                die.setImageResource(white[gh.getDieVal(i) - 1]);
            }
        }

    }

    //Creates a new round
    public void newRound(){
        if(spinner.getAdapter().getCount() == 1) showScoreboard();

        gh.setGameOn(false);
        gh.resetDice();
        dieRefresh();
        gh.setNumRolls();
        rollBtn.setEnabled(true);
        nxtRoundBtn.setEnabled(false);
        numRollsView.setText(String.format("%d left", 3 - gh.getNumRolls()));
        spinner.setSelection(0);
    }

    //Begins the new activity once all 10 rounds have been finished
    public void showScoreboard(){
        Intent i = new Intent(GameActivity.this, Scoring.class);
        i.putExtra("total_score", gh.returnScoreList(score));
        startActivity(i);
    }

}
