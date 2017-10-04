package com.pgordon.countergame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

public class Input implements KeyListener, MouseListener {
	boolean[] keys = new boolean[256];
	Game game;
	Input(Game game) {
		game.addKeyListener(this);
		game.addMouseListener(this);
		this.game = game;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
	public boolean isKeyDown(int code) {
		return keys[code];
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Main menu
		if (game.mode == 0) {
			// For each box, check if a mouse click occurred inside them.
			if(e.getX() > game.start.x && e.getY() > game.start.y && e.getX() < game.start.x+150 && e.getY() < game.start.y+150) {
				game.setMode(1, true);
			}
			if(e.getX() > game.names.x && e.getY() > game.names.y && e.getX() < game.names.x+150 && e.getY() < game.names.y+150) {
				game.setMode(2, true);
			}
			if(e.getX() > game.quit.x && e.getY() > game.quit.y && e.getX() < game.quit.x+150 && e.getY() < game.quit.y+150) {
				game.gameActive = false;
			}
			// Settings button
			if(e.getX() > game.windowWidth-58 && e.getY() > game.windowHeight-105 && e.getX() < game.windowWidth-8 && e.getY() < game.windowHeight-55) {
				game.settingsHover = true;
			}
			// Info button
			if(e.getX() > game.windowWidth-118 && e.getY() > game.windowHeight-105 && e.getX() < game.windowWidth-68 && e.getY() < game.windowHeight-55) {
				JOptionPane.showMessageDialog(null, "Counter movements: 1 = Move forward one square, 2 = Move forward two squares, 3 = Move forward three squares, 4 = Move backward one square, 5 = Move forward to next free square or safe square");
			}
		}
		if (game.mode == 1 && !game.currentGame.paused) {
			if((game.currentGame.stage == 1 || game.currentGame.stage == 3) && e.getX() > game.rollBox.x && e.getY() > game.rollBox.y && e.getX() < game.rollBox.x+(game.rollFm.stringWidth("Roll"))+50 && e.getY() < game.rollBox.y+game.rollFm.getHeight()+25) {
				game.rollDice = true;
			}
			if (game.currentGame.stage == 2) {
				if(game.p1.c1.getColumn().intersects(e.getX(), e.getY(), 1, 1)) {
					game.p1.c1.selected = true;
				} else {
					game.p1.c1.selected = false;
				}
				if(game.p1.c2.getColumn().intersects(e.getX(), e.getY(), 1, 1)) {
					game.p1.c2.selected = true;
				} else {
					game.p1.c2.selected = false;
				}
			}
			if (game.currentGame.stage == 4) {
				if(game.p2.c1.getColumn().intersects(e.getX(), e.getY(), 1, 1)) {
					game.p2.c1.selected = true;
				} else {
					game.p2.c1.selected = false;
				}
				if(game.p2.c2.getColumn().intersects(e.getX(), e.getY(), 1, 1)) {
					game.p2.c2.selected = true;
				} else {
					game.p2.c2.selected = false;
				}
			}
			if (game.currentGame.stage == 5) {
				// New game
				if(e.getX() > game.playAgain.x && e.getY() > game.playAgain.y && e.getX() < game.playAgain.x+game.windowWidth/2 && e.getY() < game.playAgain.y+100) {
					game.setMode(0, true);
				}
				// Rematch
				if(e.getX() > game.quitEnd.x && e.getY() > game.quitEnd.y && e.getX() < game.quitEnd.x+game.windowWidth/2 && e.getY() < game.quitEnd.y+100) {
					game.setMode(1, true);
				}
			}
		}
		if (game.mode == 1 && game.currentGame.paused && game.currentGame.stage != 5) {
			// resume
			if(e.getX() > game.playAgain.x && e.getY() > game.playAgain.y && e.getX() < game.playAgain.x+game.windowWidth/2 && e.getY() < game.playAgain.y+100) {
				game.currentGame.paused = false;
			}
			// menu
			if(e.getX() > game.quitEnd.x && e.getY() > game.quitEnd.y && e.getX() < game.quitEnd.x+game.windowWidth/2 && e.getY() < game.quitEnd.y+100) {
				game.setMode(0, true);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}
}
