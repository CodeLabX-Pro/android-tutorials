package com.example.tictactoe;

/**
 * The rules of Tic Tac Toe, with no Android code: a board of 9 cells, whose turn it is, and who won.
 * Cells are numbered 0 to 8, left to right and top to bottom.
 */
final class Game {
    static final int EMPTY = 0;
    static final int X = 1;
    static final int O = 2;

    /** Every row, column and diagonal that wins. */
    private static final int[][] LINES = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
    };

    private final int[] cells;

    Game() {
        this(new int[9]);
    }

    /** Continue a saved game, for example after the phone is rotated. */
    Game(int[] cells) {
        this.cells = cells;
    }

    int[] cells() {
        return cells;
    }

    int cell(int index) {
        return cells[index];
    }

    /** X always starts, so it's X's turn whenever both players have played the same number of moves. */
    int turn() {
        int x = 0, o = 0;
        for (int cell : cells) {
            if (cell == X) x++;
            else if (cell == O) o++;
        }
        return x == o ? X : O;
    }

    /** Puts the current player's mark in a cell. Returns false if the cell is taken or the game is over. */
    boolean play(int index) {
        if (isOver() || cells[index] != EMPTY) return false;
        cells[index] = turn();
        return true;
    }

    /** The three winning cells, or null if nobody has won yet. */
    int[] winningLine() {
        for (int[] line : LINES) {
            int first = cells[line[0]];
            if (first != EMPTY && first == cells[line[1]] && first == cells[line[2]]) return line;
        }
        return null;
    }

    /** X or O if someone has three in a row, otherwise EMPTY. */
    int winner() {
        int[] line = winningLine();
        return line == null ? EMPTY : cells[line[0]];
    }

    boolean isDraw() {
        if (winner() != EMPTY) return false;
        for (int cell : cells) if (cell == EMPTY) return false;
        return true;
    }

    boolean isOver() {
        return winner() != EMPTY || isDraw();
    }
}
