package com.pgordon.countergame;

import java.awt.Rectangle;

public class Counter {
	int x;
	int y;
	int square;
	int fancyY; // for smooth counter movement animation
	boolean hover; // is the mouse over this counter?
	boolean selected; // was this counter clicked?
	Game game;
	Counter(int x, int y, Game game) {
		this.x = x;
		this.y = y;
		this.square = 1;
		this.fancyY = y;
		this.game = game;
		this.hover = false;
		this.selected = false;
	}
	int getY() {
		return y - (square * 75 - 75);
	}
	int getFancyY() {
		return fancyY;
	}
	
	Rectangle getColumn() {
		return new Rectangle(x-50, game.windowHeight-75*11, 140, game.windowHeight);
	}
	Rectangle getDisplayColumn() {
		return new Rectangle(x-50+2, game.windowHeight-75*11+2, 140-5, game.windowHeight-(game.windowHeight-75*11)-6);
	}
}
