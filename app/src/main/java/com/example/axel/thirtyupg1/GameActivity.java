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

public class GameActivity extends AppCompatActivity {

    private GameHandler gh;
    private int[] dice, white, grey, red;
    private Button rollBtn, nxtRoundBtn;
    private TextView scoreView ;
    private TextView numRollsView;
    private Spinner spinner;
    private ArrayAdapter<String> spinAdapter;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initiates an instance of gameHandler
        gh = new GameHandler();

        //Fetches text ImageView IDs
        numRollsView = (TextView) findViewById(R.id.numRolls_view);
        scoreView = (TextView) findViewById(R.id.score_view);

        //Fetching die ImageViews IDs
        dice = new int [] {R.id.die_11, R.id.die_12, R.id.die_13, R.id.die_21, R.id.die_22, R.id.die_23, };

        //Fetching die image resources
        white = new int[] {R.drawable.white1, R.drawable.white2, R.drawable.white3, R.drawable.white4, R.drawable.white5, R.drawable.white6};
        grey = new int[] {R.drawable.grey1, R.drawable.grey2, R.drawable.grey3, R.drawable.grey4, R.drawable.grey5, R.drawable.grey6};
        red = new int[] {R.drawable.red1, R.drawable.red2, R.drawable.red3, R.drawable.red4, R.drawable.red5, R.drawable.red6};

        Resources res = getResources();
        String[] spinnerOptions = res.getStringArray(R.array.spinner_options);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(Arrays.asList(spinnerOptions)));
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinAdapter);


        for(int i = 0; i < 6; i++){
            ImageView dieImg = (ImageView) findViewById(dice[i]);

            final int finalI = i;
            dieImg.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if(!gh.gameOn) return;
                    gh.setDieState(finalI);
                    dieRefresh();
                }
            });
        }

        rollBtn = (Button) findViewById(R.id.roll_btn);
        rollBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                gh.setGameOn(true);
                gh.rollDice();
                dieRefresh();
                numRollsView.setText(Integer.toString(3-gh.getNumRolls()) + " left"); //should be num rolls

                if(gh.getNumRolls() >= 3){
                    Toast.makeText(GameActivity.this, "Select dice you want to score", Toast.LENGTH_SHORT).show();
                    rollBtn.setEnabled(false);
                    nxtRoundBtn.setEnabled(true);
                    spinner.setEnabled(true);
                }
            }
        });

        nxtRoundBtn = (Button) findViewById(R.id.next_round);
        nxtRoundBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(spinner.getSelectedItemPosition() == 0){
                    Toast.makeText(GameActivity.this, "Please choose an option in the drop-down menu" + spinner.getAdapter().getCount(), Toast.LENGTH_SHORT).show();
                } else {
                    //Count up the score
                    score += gh.calculateScore(spinner.getSelectedItem().toString());
                    scoreView.setText("Score: " + Integer.toString(score));

                    spinAdapter.remove((String) spinner.getSelectedItem());
                    spinAdapter.notifyDataSetChanged();
                    newGame();
                }
            }
        });

        newGame();
    }

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

    public void newGame(){
        if(spinner.getAdapter().getCount() == 9) showScoreboard();

        gh.setGameOn(false);
        gh.resetDice();
        dieRefresh();
        gh.setNumRolls();
        rollBtn.setEnabled(true);
        nxtRoundBtn.setEnabled(false);
        numRollsView.setText(String.format("%d left", 3 - gh.getNumRolls()));
        spinner.setSelection(0);
        spinner.setEnabled(false);
    }

    public void showScoreboard(){
        Intent i = new Intent(GameActivity.this, Scoring.class);
        i.putExtra("final_score", score);
        startActivity(i);
    }

}
