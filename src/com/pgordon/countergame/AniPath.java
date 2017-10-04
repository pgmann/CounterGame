package com.pgordon.countergame;

public class AniPath extends Thread {
	Game game;
	int counter;
	public AniPath(Game game, int counter) {
		this.game = game;
		this.counter = counter;
	}
	public void run() {
		if (counter == 1) {
			while (game.p1.c1.fancyY != game.p1.c1.y - (game.p1.c1.square * 75 - 75)) {
				if (game.p1.c1.fancyY < game.p1.c1.y - (game.p1.c1.square * 75 - 75)) {
					game.p1.c1.fancyY++;
				} else {
					game.p1.c1.fancyY--;
				}
				try {Thread.sleep(33);} catch (InterruptedException e) {} // 30 FPS, 1 sec = 1000 ms so 1000/30 = 33.3333... (rounded to 33)
			}
		} else if (counter == 2) {
			while (game.p1.c2.fancyY != game.p1.c2.y - (game.p1.c2.square * 75 - 75)) {
				if (game.p1.c2.fancyY < game.p1.c2.y - (game.p1.c2.square * 75 - 75)) {
					game.p1.c2.fancyY++;
				} else {
					game.p1.c2.fancyY--;
				}
				try {Thread.sleep(33);} catch (InterruptedException e) {} // 30 FPS, 1 sec = 1000 ms so 1000/30 = 33.3333... (rounded to 33)
			}
		} else if (counter == 3) {
			while (game.p2.c1.fancyY != game.p2.c1.y - (game.p2.c1.square * 75 - 75)) {
				if (game.p2.c1.fancyY < game.p2.c1.y - (game.p2.c1.square * 75 - 75)) {
					game.p2.c1.fancyY++;
				} else {
					game.p2.c1.fancyY--;
				}
				try {Thread.sleep(33);} catch (InterruptedException e) {} // 30 FPS, 1 sec = 1000 ms so 1000/30 = 33.3333... (rounded to 33)
			}
		} else if (counter == 4) {
			while (game.p2.c2.fancyY != game.p2.c2.y - (game.p2.c2.square * 75 - 75)) {
				if (game.p2.c2.fancyY < game.p2.c2.y - (game.p2.c2.square * 75 - 75)) {
					game.p2.c2.fancyY++;
				} else {
					game.p2.c2.fancyY--;
				}
				try {Thread.sleep(33);} catch (InterruptedException e) {} // 30 FPS, 1 sec = 1000 ms so 1000/30 = 33.3333... (rounded to 33)
			}
		}
	}
}
