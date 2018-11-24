package com.example.david.testproject;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    GameView gameView;
    ImageButton restartButton, pauseButton, startButton;
    Button saveButton;
    TextView timeCounter, scoreText;
    EditText playerName;
    View pauseScreen;
    View scoreScreen;

    User user;

    private boolean pause;

    private Record record;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // make the app full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        record = Record.getInstant(this);


        FrameLayout game = new FrameLayout(this);
        gameView = new GameView (this, this);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View gameWidgets = layoutInflater.inflate(R.layout.game_widgets, null);

        pauseScreen = layoutInflater.inflate(R.layout.pause_screen, null);
        pauseScreen.setVisibility(View.INVISIBLE);

        scoreScreen = layoutInflater.inflate(R.layout.add_record, null);
        scoreScreen.setVisibility(View.INVISIBLE);

        game.addView(gameView);
        game.addView(gameWidgets);
        game.addView(pauseScreen);
        game.addView(scoreScreen);

        setContentView(game);

        init();

        setListener();



    }


    @Override
    protected void onPause(){
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        gameView.resume();
    }

    public void init(){
        timeCounter = findViewById(R.id.time_counter);
        restartButton = findViewById(R.id.restart_button);
        pauseButton = findViewById(R.id.pause_button);
        startButton = findViewById(R.id.start_button);
        playerName = findViewById(R.id.player_name);
        saveButton = findViewById(R.id.save_button);
        scoreText = findViewById(R.id.score_text);

        pause = false;
    }

    public void setListener(){
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.killThread = true;
                gameView.reset();
                startButton.setVisibility(View.VISIBLE);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pause) {
                    pause = false;
                    pauseScreen.setVisibility(View.INVISIBLE);
                    gameView.resume();
                }else{
                    pause = true;
                    pauseScreen.setVisibility(View.VISIBLE);
                    gameView.pause();
                }
            }
        });
        pauseScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pause) {
                    pause = false;
                    pauseScreen.setVisibility(View.INVISIBLE);
                    gameView.resume();
                }
            }
        });
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.start = true;
                gameView.running = true;
                gameView.startCounting();
                startButton.setVisibility(View.INVISIBLE);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.name = playerName.getText().toString();
                SaveRecordTask saveRecordTask = new SaveRecordTask();
                saveRecordTask.execute(user);
            }
        });
    }

    public void endGame(int score){
        user = new User();
        user.score = score;
        playerName.setText(user.name);
        scoreText.setText(getString(R.string.score, score));
        scoreScreen.setVisibility(View.VISIBLE);
    }


    private class SaveRecordTask extends AsyncTask<User, Void, Void> {

        @Override
        protected void onPreExecute() {
            saveButton.setText("Saving...");
        }

        @Override
        protected Void doInBackground(User... users) {
            Log.i("Saving data", users[0].name);
            record.addRecord(users[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            GameActivity.this.finish();
        }
    }

}
