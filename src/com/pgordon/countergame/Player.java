package com.pgordon.countergame;

public class Player {
	String name;
	Game game;
	int roll;
	boolean invalidMove = false;
	boolean endCt = false; // "Your counter is at the end!"
	boolean sameSquare = false; // "You can't have both counters on the same square!"
	Counter c1, c2;
	int num;
	Player(String name, int num, Game game) { // num is the player number
		this.name = name;
		this.game = game;
		this.roll = -1;
		this.num = num;
		// the window height is 1000, take 60 off that to account for counter width
		c1 = new Counter(90 + (num * 280), 1000-60, game);
		c2 = new Counter(90 + (num * 280) + 140, 1000-60, game);
	}
	void resetCt() {
		c1 = new Counter(90 + (num * 280), 1000-60, game);
		c2 = new Counter(90 + (num * 280) + 140, 1000-60, game);
	}
}
