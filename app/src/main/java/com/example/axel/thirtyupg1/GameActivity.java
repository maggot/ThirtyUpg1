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

import java.util.ArrayList;
import java.util.Arrays;

public class GameActivity extends AppCompatActivity {

    private GameHandler gh;
    private int[] dice, white, grey, red;
    private Button rollBtn, nxtRoundBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gh = new GameHandler();

        //nxtRoundBtn.setEnabled(false);

        dice = new int [] {R.id.die_11, R.id.die_12, R.id.die_13, R.id.die_21, R.id.die_22, R.id.die_23, };

        white = new int[] {R.drawable.white1, R.drawable.white2, R.drawable.white3, R.drawable.white4, R.drawable.white5, R.drawable.white6};
        grey = new int[] {R.drawable.grey1, R.drawable.grey2, R.drawable.grey3, R.drawable.grey4, R.drawable.grey5, R.drawable.grey6};
        red = new int[] {R.drawable.red1, R.drawable.red2, R.drawable.red3, R.drawable.red4, R.drawable.red5, R.drawable.red6};

        Resources res = getResources();
        String[] spinnerOptions = res.getStringArray(R.array.spinner_options);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(Arrays.asList(spinnerOptions)));
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinAdapter);

        for(int i = 0; i < 6; i++){
            ImageView dieImg = (ImageView) findViewById(dice[i]);

            final int finalI = i;
            dieImg.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    gh.setDieState(finalI);
                    dieRefresh();
                }
            });
        }

        rollBtn = (Button) findViewById(R.id.roll_btn);
        rollBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                gh.rollDice();
                dieRefresh();
                if(gh.getNumRolls() >= 3){
                    rollBtn.setEnabled(false);
                    nxtRoundBtn.setEnabled(true);
                }
            }
        });

        nxtRoundBtn = (Button) findViewById(R.id.next_round);
        nxtRoundBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Count up the score
                Intent i = new Intent(GameActivity.this, Scoring.class);
                startActivity(i);
//                newGame();
//                nxtRoundBtn.setEnabled(false);
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
        gh.resetDice();
        gh.rollDice();
        dieRefresh();
        gh.setNumRolls();
        rollBtn.setEnabled(true);
    }

}
