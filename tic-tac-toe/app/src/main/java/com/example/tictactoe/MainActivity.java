package com.example.tictactoe;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * The game screen: draws the board from Game, passes taps to it, and keeps the score.
 * The board, score and turn survive rotating the phone.
 */
public class MainActivity extends AppCompatActivity {
    private Game game;
    private int scoreX, scoreO;

    private final Button[] cells = new Button[9];
    private TextView status, textScoreX, textScoreO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_main);
        status = findViewById(R.id.textStatus);
        textScoreX = findViewById(R.id.textScoreX);
        textScoreO = findViewById(R.id.textScoreO);

        // The nine cells are the children of the GridLayout, in order 0 to 8.
        GridLayout board = findViewById(R.id.board);
        for (int i = 0; i < cells.length; i++) {
            int index = i;
            cells[i] = (Button) board.getChildAt(i);
            cells[i].setOnClickListener(v -> play(index));
        }

        findViewById(R.id.btnPlayAgain).setOnClickListener(v -> newRound());
        findViewById(R.id.btnReset).setOnClickListener(v -> {
            scoreX = 0;
            scoreO = 0;
            newRound();
        });

        if (savedInstanceState == null) {
            game = new Game();
        } else {
            game = new Game(savedInstanceState.getIntArray("cells"));
            scoreX = savedInstanceState.getInt("scoreX");
            scoreO = savedInstanceState.getInt("scoreO");
        }
        render();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("cells", game.cells());
        outState.putInt("scoreX", scoreX);
        outState.putInt("scoreO", scoreO);
    }

    private void play(int index) {
        if (!game.play(index)) return;
        if (game.winner() == Game.X) scoreX++;
        else if (game.winner() == Game.O) scoreO++;
        render();
    }

    private void newRound() {
        game = new Game();
        render();
    }

    /** Makes the screen match the game: marks, colours, highlighted winning line, status and score. */
    private void render() {
        int[] line = game.winningLine();
        for (int i = 0; i < cells.length; i++) {
            int mark = game.cell(i);
            cells[i].setText(mark == Game.X ? "X" : mark == Game.O ? "O" : "");
            cells[i].setTextColor(colour(mark));
            boolean winning = line != null && (i == line[0] || i == line[1] || i == line[2]);
            cells[i].setBackgroundTintList(ColorStateList.valueOf(
                    winning ? ContextCompat.getColor(this, R.color.winning_cell) : Color.TRANSPARENT));
        }

        String player = game.turn() == Game.X ? "X" : "O";
        if (game.winner() != Game.EMPTY) {
            status.setText(getString(R.string.status_win, game.winner() == Game.X ? "X" : "O"));
        } else if (game.isDraw()) {
            status.setText(R.string.status_draw);
        } else {
            status.setText(getString(R.string.status_turn, player));
        }
        textScoreX.setText(getString(R.string.score, "X", scoreX));
        textScoreO.setText(getString(R.string.score, "O", scoreO));
    }

    private int colour(int mark) {
        return ContextCompat.getColor(this, mark == Game.O ? R.color.player_o : R.color.player_x);
    }
}
