package com.example.axel.thirtyupg1;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Axel on 6/30/2017.
 */

public class Scoring extends Activity {

    TextView finalScore, endScore1, endScore2, endScore3, endScore4, endScore5, endScore6, endScore7, endScore8, endScore9, endScore10;
    TextView[] allScores;
    Button newGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoring);

        finalScore = (TextView) findViewById(R.id.final_score);

        //Array holding all view addresses
        allScores = new TextView[]{
                endScore1 = (TextView) findViewById(R.id.end_score1),
                endScore2 = (TextView) findViewById(R.id.end_score2),
                endScore3 = (TextView) findViewById(R.id.end_score3),
                endScore4 = (TextView) findViewById(R.id.end_score4),
                endScore5 = (TextView) findViewById(R.id.end_score5),
                endScore6 = (TextView) findViewById(R.id.end_score6),
                endScore7 = (TextView) findViewById(R.id.end_score7),
                endScore8 = (TextView) findViewById(R.id.end_score8),
                endScore9 = (TextView) findViewById(R.id.end_score9),
                endScore10 = (TextView) findViewById(R.id.end_score10),
                finalScore = (TextView) findViewById(R.id.final_score)
        };

        //Gets the array saved in the intent
        Intent intent = getIntent();
        int scrData[] = intent.getIntArrayExtra("total_score");

        //Gets the items otherwise used for the spinner to use on the score display
        Resources res = getResources();
        String[] scoringOptions = res.getStringArray(R.array.spinner_options);

        //Putting the score in the corresponding TextView together with "identifiers"
        for (int i = 0; i < 10; i++) {
            allScores[i].setText((scoringOptions[i + 1] + ": " + String.valueOf(scrData[i])));
        }

        allScores[10].setText(("Total Score : " + String.valueOf(scrData[10])));


        newGame = (Button) findViewById(R.id.newGameBtn);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Scoring.this, GameActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
