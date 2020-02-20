package com.github.novotnyr.android.obesenec;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String BUNDLE_KEY_GAME = "game";

    private EditText letterEditText;

    private TextView foundLettersTextView;

    private ImageView gallowsImageView;

    private HangmanGame game;

    private int[] gallowsLayouts = {
            R.drawable.gallows6,
            R.drawable.gallows5,
            R.drawable.gallows4,
            R.drawable.gallows3,
            R.drawable.gallows2,
            R.drawable.gallows1,
            R.drawable.gallows0,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        letterEditText = findViewById(R.id.letterEditText);
        foundLettersTextView = findViewById(R.id.foundLettersTextView);
        gallowsImageView = findViewById(R.id.gallowsImageView);

        if (savedInstanceState == null) {
            resetGame();
        } else {
            restoreGameState(savedInstanceState);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_KEY_GAME, game);
    }

    private void restoreGameState(Bundle savedInstanceState) {
        game = (HangmanGame) savedInstanceState.get(BUNDLE_KEY_GAME);
        updateGallows();
        updateGuessedCharacters();
    }

    public void gallowsImageViewClick(View v) {
        if (game.getAttemptsLeft() == 0 || game.isWon()) {
            resetGame();
            return;
        }
        CharSequence letter = letterEditText.getText();
        if (letter == null || letter.length() == 0) {
            alertEmptyLetter();
            return;
        }
        boolean isGuessed = game.guess(letter.charAt(0));

        updateGuessedCharacters();
        letterEditText.getText().clear();

        if (isGuessed) {
            if (game.isWon()) {
                alertSuccessfulGame();
            }
        } else {
            updateGallows();
            if (game.getAttemptsLeft() == 0) {
                alertFailedGame();
            }
        }
    }

    private void updateGuessedCharacters() {
        foundLettersTextView.setText(game.getGuessedCharacters());
    }

    private void resetGame() {
        game = new HangmanGame();
        gallowsImageView.setImageResource(R.drawable.gallows6);
        gallowsImageView.setColorFilter(null);

        letterEditText.getText().clear();
        updateGuessedCharacters();
    }

    private void updateGallows() {
        gallowsImageView.setImageResource(gallowsLayouts[game.getAttemptsLeft()]);
    }

    private void alertEmptyLetter() {
        Toast.makeText(this, "You must enter a letter.", Toast.LENGTH_SHORT)
                .show();
    }

    private void alertFailedGame() {
        foundLettersTextView.setText(game.getChallengeWord());

        ColorFilter filter = new LightingColorFilter(Color.RED, Color.BLACK);
        gallowsImageView.setColorFilter(filter);
    }

    private void alertSuccessfulGame() {
        ColorFilter filter = new LightingColorFilter(Color.GREEN, Color.BLACK);
        gallowsImageView.setColorFilter(filter);
    }
}
