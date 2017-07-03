package com.example.axel.thirtyupg1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Axel on 6/30/2017.
 */


public class Scoring extends Activity {

    TextView finalScore, endScore1, endScore2, endScore3, endScore4, endScore5, endScore6, endScore7, endScore8, endScore9, endScore10;
    TextView[] allScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoring);

        finalScore = (TextView) findViewById(R.id.final_score);

        allScores = new TextView[] {
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
        };

        Intent i = getIntent();
        finalScore.setText(String.valueOf("Total Score: " + i.getIntExtra("final_score", 0)));
    }
}
