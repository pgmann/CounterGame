package com.pgordon.countergame;

public class GameBoard {
	// Stores variables for each game played
	/* Stages:
		1 = waiting to start
		2 = player 1's turn
		3 = waiting for player 2 to start
		4 = player 2's turn
		5 = game over, winner will be announced
		... in a loop
	 */
	int stage;
	boolean paused;
	boolean pauseAllowed;
	int winner;
	public GameBoard() {
		stage = 1;
		pauseAllowed = true;
		paused = false;
	}
}
