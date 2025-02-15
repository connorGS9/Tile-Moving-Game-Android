package com.example.project_2_the_9_square_problem;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    class Pair { //Using the Pair class to store and pass coordinates of a cell as a single object
        int x, y;
        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int[][] solvedPuzzle = new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, Integer.MIN_VALUE}}; //The solved puzzle state
    int[][] currentPuzzle = new int[3][3]; //The board the player will use
    Pair emptyPos; //The pair of coordinates for the empty cell
    Button[][] buttons = new Button[3][3]; //For connecting buttons to a specific index in the array
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttons[0][0] = findViewById(R.id.button1);
        buttons[0][1] = findViewById(R.id.button2);
        buttons[0][2] = findViewById(R.id.button3);
        buttons[1][0] = findViewById(R.id.button4);
        buttons[1][1] = findViewById(R.id.button5);
        buttons[1][2] = findViewById(R.id.button6);
        buttons[2][0] = findViewById(R.id.button7);
        buttons[2][1] = findViewById(R.id.button8);
        buttons[2][2] = findViewById(R.id.button9);

        Button buttonSolved = findViewById(R.id.button_solved);
        buttonSolved.setOnClickListener(v -> checkSolved());

        findSolvable(solvedPuzzle);
        changeButtonDisplay();
        buttonListeners();
    }

    protected void findSolvable(int[][] solved) { //Finds a random always solvable puzzle by changing the puzzle 0 - 250 times from the completed state using the same rules for traversal
        emptyPos = new Pair(2,2);
        int[][] directions = new int[][] {{0,-1}, {0,1}, {-1,0}, {1,0}}; //Up, Down, Left, Right
        currentPuzzle = solved;
        Random rand = new Random(); //To randomly alter the solved array
        int alterations = rand.nextInt(151) + 100; //Number of times the board will be changed from solved state

        for (int i = 0; i < alterations; i++) {
            int direction = rand.nextInt(4); //Row of directions that will be used
            int xChange = directions[direction][0];
            int yChange = directions[direction][1];
            if (emptyPos.x + xChange <= 2 && emptyPos.x + xChange >= 0 && emptyPos.y + yChange <= 2 && emptyPos.y + yChange >= 0) { //If the move is inbounds
                Pair swapTo = new Pair(emptyPos.x + xChange, emptyPos.y + yChange);
                swap(emptyPos, swapTo, currentPuzzle);
            }
        }
        //Current Puzzle is now a randomized solvable puzzle
    }

    protected void swap(Pair empty, Pair swapTo, int[][] currentPuzzle) { //This will be the new 'empty" tile
        //Swap the numbers within the array
        currentPuzzle[empty.x][empty.y] = currentPuzzle[swapTo.x][swapTo.y];
        currentPuzzle[swapTo.x][swapTo.y] = Integer.MIN_VALUE;

        //Swap the position of the empty pair and swapped pair
        int temp_x = empty.x;
        int temp_y = empty.y;
        empty.x = swapTo.x;
        empty.y = swapTo.y;
        swapTo.x = temp_x;
        swapTo.y = temp_y;
    }

    protected void changeButtonDisplay() { //Make sure all buttons show the correct number
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                    if (currentPuzzle[i][j] == Integer.MIN_VALUE) { //The empty cell contains Integer min val
                        buttons[i][j].setText("");
                    } else {
                        buttons[i][j].setText(String.valueOf(currentPuzzle[i][j]));
                }
            }
        }
    }
    protected void buttonListeners() { //To create listeners for the 9 buttons to the button click method
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int row = i;
                final int col = j;
                buttons[i][j].setOnClickListener(v -> buttonClick(row, col));
            }
        }
    }

    protected void buttonClick(int row, int col) { //On button click swap the empty cell and the button's cell if its valid
        //we want to swap the given cell with the empty if its adjacent or give a toast if not
        if ((Math.abs(emptyPos.x - row) == 1 && emptyPos.y == col) || (Math.abs(emptyPos.y - col) == 1 && emptyPos.x == row)) {
            //If the empty cell is adjacent swap them
            swap(emptyPos, new Pair(row, col), currentPuzzle);
            changeButtonDisplay();
        } else {
            Toast.makeText(this, "invalid move.", Toast.LENGTH_SHORT).show();
        }
    }
    protected void checkSolved() {
        if (currentPuzzle[0][0] == 1 && currentPuzzle[0][1] == 2 && currentPuzzle[0][2] == 3 &&
            currentPuzzle[1][0] == 4 && currentPuzzle[1][1] == 5 && currentPuzzle[1][2] == 6 &&
            currentPuzzle[2][0] == 7 && currentPuzzle[2][1] == 8 && currentPuzzle[2][2] == Integer.MIN_VALUE) {
            Toast.makeText(this, "Congratulations! You Won the Game", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "The Game Is Not Over Yet!", Toast.LENGTH_SHORT).show();
        }
    }
}