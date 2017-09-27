package se.umu.student.axever0002;

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

import com.example.axel.thirtyupg1.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Axel on 6/26/2017.
 */

//  GameActivity is the main activity of which is started when the app has been launched.
//  GameActivity handles the interface & user input, and communicates with the GameHandler
//  until the game has ended. When the game has ended, GameActivity starts a new activity
//  "Scoring", where data relevant for that activity is passed along.

public class GameActivity extends AppCompatActivity {

    private GameHandler gh;
    private int[] dice, white, grey;
    private Button rollBtn;
    private TextView scoreView;
    private TextView numRollsView;
    private Spinner spinner;
    private ArrayAdapter<String> spinAdapter;

    //onCreate is called when the activity is started, it creates the GUI and starts the game.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check whether there already is some activity data stored in savedInstance rebuild with "old" data if so.
        if (savedInstanceState != null) {
            this.gh = savedInstanceState.getParcelable("parcel");
            rebuildGUI();
            this.spinAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(savedInstanceState.getStringArrayList("spinnerItems")));
            this.spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spinner.setAdapter(spinAdapter);
            this.spinner.setSelection(savedInstanceState.getInt("spinnerPos", 0));
        } else {
            //Initiates an instance of gameHandler, create the GUI and start a new game if there are no "old" data.
            gh = new GameHandler();
            createGUI();
            newRound();
        }
    }

    //createGUI is called when the onCreate method hasn't found any previous data in the bundle, and will create new GUI elements.
    protected void createGUI() {
        //Fetches text ImageView IDs.
        numRollsView = (TextView) findViewById(R.id.numRolls_view);
        scoreView = (TextView) findViewById(R.id.score_view);

        //Fetching die ImageViews IDs.
        dice = new int[]{R.id.die_11, R.id.die_12, R.id.die_13, R.id.die_21, R.id.die_22, R.id.die_23,};

        //Fetching die image resources
        white = new int[]{R.drawable.white1, R.drawable.white2, R.drawable.white3, R.drawable.white4, R.drawable.white5, R.drawable.white6};
        grey = new int[]{R.drawable.grey1, R.drawable.grey2, R.drawable.grey3, R.drawable.grey4, R.drawable.grey5, R.drawable.grey6};

        //Fetching a string array from res/string.xml and setting the items to the spinner-menu.
        Resources res = getResources();
        String[] spinnerOptions = res.getStringArray(R.array.spinner_options);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(Arrays.asList(spinnerOptions)));
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinAdapter);

        //calls a method that invokes listeners for the spinner and buttons.
        createListeners();
    }

    private void createListeners() {
        //Setting listeners to all the dice ImageViews.
        for (int i = 0; i < 6; i++) {
            ImageView dieImg = (ImageView) findViewById(dice[i]);

            final int finalI = i;
            dieImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //If the game has not yet been started the dice will not be clickable.
                    if (!gh.gameOn) return;
                    gh.setDieState(finalI);
                    dieRefresh();
                }
            });
        }

        //Create a listener for the roll-button with corresponding method-calls.
        rollBtn = (Button) findViewById(R.id.roll_btn);
        rollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gh.setGameOn(true);
                gh.rollDice();      //call for the gh's RNG to give new values to the dice.
                dieRefresh();       //update the dice graphically.
                numRollsView.setText(getString(R.string.rolls_text, 3 - gh.getNumRolls()));     //get number of rolls from gh and update the view

                //When the user has rolled the dice 3 times, the roll button will be disabled.
                if (gh.getNumRolls() >= 3) {
                    rollBtn.setEnabled(false);
                }
            }
        });

        //Creates a listener for the next round-button.
        Button nxtRoundBtn = (Button) findViewById(R.id.next_round);
        nxtRoundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Will present a toast if uset has not selected an option from the spinner.
                if (spinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(GameActivity.this, "Please choose an option in the drop-down menu", Toast.LENGTH_SHORT).show();
                } else {
                    //Count up the score for this round and update the TextView.
                    gh.calculateScore(spinner.getSelectedItem().toString());
                    scoreView.setText(getString(R.string.score_text, gh.getScore()));

                    //Remove currently selected item from the spinner.
                    spinAdapter.remove((String) spinner.getSelectedItem());
                    spinAdapter.notifyDataSetChanged();
                    newRound();
                }
            }
        });
    }

    //Called when he onCreate method has stored data and needs to create and update the GUI with said data.
    private void rebuildGUI() {
        createGUI();
        if (gh.getNumRolls() >= 3) rollBtn.setEnabled(false);
        scoreView.setText(getString(R.string.score_text, gh.getScore()));
        numRollsView.setText(getString(R.string.rolls_text, 3 - gh.getNumRolls()));
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinAdapter);
        dieRefresh();
    }

    //Before onStop() is called this method saves the running instance of the object gh and the spinner state.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("parcel", gh);
        outState.putStringArrayList("spinnerItems", retrieveAllItems());
        outState.putInt("spinnerPos", spinner.getSelectedItemPosition());
        super.onSaveInstanceState(outState);
    }

    //Method that reads all items from the spinner and returns them in an ArrayList.
    private ArrayList<String> retrieveAllItems() {
        int num = spinAdapter.getCount();
        ArrayList<String> items = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            String item = spinAdapter.getItem(i);
            items.add(item);
        }
        return items;
    }

    //Refresh the images (grey for saved dice) for all dice to the corresponding number generated from gh's RNG.
    private void dieRefresh() {
        for (int i = 0; i < 6; i++) {
            if (gh.getDieState(i)) {
                ImageView die = (ImageView) findViewById(dice[i]);
                die.setImageResource(grey[gh.getDieVal(i) - 1]);
            } else {
                ImageView die = (ImageView) findViewById(dice[i]);
                die.setImageResource(white[gh.getDieVal(i) - 1]);
            }
        }
    }

    //Creates a new round and sets all the presets required
    private void newRound() {
        if (spinner.getAdapter().getCount() == 1) showScoreboard();  //If there are no more items in the spinner, call the method that will transition activities.

        gh.setGameOn(false);
        gh.createDice();
        dieRefresh();
        gh.setNumRolls();
        rollBtn.setEnabled(true);
        numRollsView.setText(getString(R.string.rolls_text, 3 - gh.getNumRolls()));
        spinner.setSelection(0);
    }

    //Begins the new activity once all 10 rounds have been finished
    private void showScoreboard() {
        Intent i = new Intent(GameActivity.this, Scoring.class);
        i.putExtra("total_score", gh.returnScoreList());    //add to the intent the list of scoring options with the corresponding game scores
        startActivity(i);
    }
}
