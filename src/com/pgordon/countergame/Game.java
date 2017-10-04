package com.pgordon.countergame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Game extends JFrame {
	// Global variables declaration //
	int windowHeight, windowWidth;
	boolean gameActive = true;
	boolean fullScreen;
	boolean settingsHover, infoHover;
	boolean rollFive = true;
	int frames = 30;
	BufferedImage toDraw;
	int mode;
	Font roll = new Font("Arial", Font.PLAIN, 60);
	FontMetrics rollFm;
	Font title = new Font("Times New Roman", Font.ITALIC, 90);
	FontMetrics titleFm;
	Font menu = new Font("Arial", Font.BOLD, 30);
	FontMetrics menuFm;
	Font nText = new Font("Arial", Font.PLAIN, 20);
	FontMetrics nTextFm;
	Font nTextBold = new Font("Arial", Font.BOLD, 20);
	FontMetrics nTextBoldFm;
	Font boardText = new Font("Arial", Font.PLAIN, 30);
	FontMetrics boardTextFm;
	NameInput nameBox;
	Color transparentBlack = new Color(0, 0, 0, 200);

	BasicStroke xthick = new BasicStroke(9);
	BasicStroke thick = new BasicStroke(5);
	BasicStroke normal = new BasicStroke(1);
	float dash[] = {10.0f};
	BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
	BasicStroke dashed2 = new BasicStroke(5.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
	Input input = new Input(this);
	Player p1, p2;
	GameBoard currentGame;
	Random random = new Random(System.currentTimeMillis());
	MenuBox start, names, quit, rollBox, playAgain, quitEnd;
	boolean rollDice = false;
	Ghost[] ghosts = new Ghost[2]; // Holds the ghost counters that are clicked on to move the actual counter
	
	boolean addedBox = false;

	private static final long serialVersionUID = 1602041416151273261L;

	public static void main(String[] args) { // The main method
		new Game(); 								// Creates the game window (see below)
	}

	public Game() { // The game window
		setTitle("Counter Game - by Peter Gordon"); // Title on the taskbar
		setDefaultCloseOperation(EXIT_ON_CLOSE); 	// Close the program when 'x' is pressed
		setVisible(true); 							// Make sure we can see it!
		setResizable(false); 						// Resizing disabled due to problems with display update
		fullScreen = false;
		init();
		// ---------------------------- //
		// Box positioning objects 		//
		start = new MenuBox(windowWidth/2-250, windowHeight-300);
		names = new MenuBox(windowWidth/2-75, windowHeight-300);
		quit = new MenuBox(windowWidth/2+100, windowHeight-300);
		rollBox = new MenuBox(windowWidth/2, 60);

		playAgain = new MenuBox(windowWidth/4, windowHeight/2-50);
		quitEnd = new MenuBox(windowWidth/4, windowHeight/2+75);
		// ---------------------------- //
		// Timer loop - the game should update 30x per second
		while (gameActive) {
			long time = System.currentTimeMillis();
			update();
			draw();
			time = (1000 / frames) - (System.currentTimeMillis() - time);
			if (time > 0) {
				try {Thread.sleep(time);} catch (Exception e) {}
			}
		}
		setVisible(false);
		System.exit(0);
	}

	public void init() {
		setMode(0, true);							// Main menu mode (creates player classes)
	}
	
	public void draw() {
		// Graphics (tdg and g) are used for drawing objects to the screen
		Graphics2D tdg = (Graphics2D) toDraw.getGraphics();
		Graphics2D g = (Graphics2D) getGraphics();
		titleFm = getFontMetrics(title); // used for centring fonts
		rollFm = getFontMetrics(roll); // used for centring fonts
		menuFm = getFontMetrics(menu); // used for centring fonts
		nTextFm = getFontMetrics(nText); // used for centring fonts
		nTextBoldFm = getFontMetrics(nTextBold); // used for centring fonts
		boardTextFm = getFontMetrics(boardText); // used for centring fonts
		rollBox.x = windowWidth/2-(rollFm.stringWidth("Roll")/2)-25;

		// DRAWING CODE for shapes
		// draw background
		tdg.setColor(Color.GREEN);
		tdg.setFont(title);
		tdg.fillRect(0, 0, windowWidth, windowHeight);
		
		// Main menu
		if (mode == 0) {
			// title box border: thick brush
			tdg.setColor(Color.YELLOW);
			tdg.fillRect(10, 35, windowWidth-20, 200);
			tdg.setStroke(xthick);
			tdg.setColor(Color.BLACK);
			tdg.drawRect(10, 35, windowWidth-20, 200);
			tdg.setStroke(thick);
			tdg.setColor(Color.ORANGE);
			tdg.drawRect(10, 35, windowWidth-20, 200);
			tdg.setStroke(normal);
			// draw "AQA do" inside title box
			tdg.setFont(title);
			tdg.setColor(Color.RED);
			tdg.drawString("AQA ", (windowWidth/2)-(titleFm.stringWidth("AQA do")/2), 150);
			tdg.setColor(Color.BLUE);
			tdg.drawString("do", (windowWidth/2)-(titleFm.stringWidth("AQA do")/2)+titleFm.stringWidth("AQA "), 150);
			// show opponents' names
			/* 210 */
			tdg.setFont(roll);
			tdg.setColor(Color.RED);
			tdg.drawString(p1.name, windowWidth/2-rollFm.stringWidth(p1.name)-rollFm.stringWidth(" VS ")/2, 310);
			tdg.setColor(Color.BLACK);
			tdg.drawString(" VS ", windowWidth/2-rollFm.stringWidth(" VS ")/2, 310);
			tdg.setColor(Color.BLUE);
			tdg.drawString(p2.name, windowWidth/2+rollFm.stringWidth(" VS ")/2, 310);
			// menu boxes
			tdg.setColor(Color.BLACK);
			tdg.setStroke(thick);
			if(start.hover) {tdg.setColor(Color.RED);} else {tdg.setColor(Color.BLACK);}
			tdg.draw(start.getRect());
			if(names.hover) {tdg.setColor(Color.RED);} else {tdg.setColor(Color.BLACK);}
			tdg.draw(names.getRect());
			if(quit.hover) {tdg.setColor(Color.RED);} else {tdg.setColor(Color.BLACK);}
			tdg.draw(quit.getRect());
			// menu items
			tdg.setFont(menu);
			if(start.hover) {tdg.setColor(Color.RED);} else {tdg.setColor(Color.BLACK);}
			tdg.drawString("Start", start.x+(75-menuFm.stringWidth("Start")/2), start.y+125);
			if(names.hover) {tdg.setColor(Color.RED);} else {tdg.setColor(Color.BLACK);}
			tdg.drawString("Names", names.x+(75-menuFm.stringWidth("Names")/2), names.y+125);
			if(quit.hover) {tdg.setColor(Color.RED);} else {tdg.setColor(Color.BLACK);}
			tdg.drawString("Quit", quit.x+(75-menuFm.stringWidth("Quit")/2), quit.y+125);
			// Settings button
			if (settingsHover) {tdg.setColor(Color.WHITE);} else {tdg.setColor(Color.BLACK);}
			tdg.drawRect(windowWidth-58, windowHeight-105, 50, 50);
			tdg.drawString("s", windowWidth-40, windowHeight-72);
			// Info button
			if (infoHover) {tdg.setColor(Color.WHITE);} else {tdg.setColor(Color.BLACK);}
			tdg.drawRect(windowWidth-118, windowHeight-105, 50, 50);
			tdg.drawString("i", windowWidth-97, windowHeight-70);
			// Bottom box
			tdg.setColor(Color.YELLOW);
			tdg.fillRect(10, windowHeight-45, windowWidth-20, 35);
			tdg.setStroke(xthick);
			tdg.setColor(Color.BLACK);
			tdg.drawRect(10, windowHeight-45, windowWidth-20, 35);
			tdg.setStroke(thick);
			tdg.setColor(Color.ORANGE);
			tdg.drawRect(10, windowHeight-45, windowWidth-20, 35);
			tdg.setStroke(normal);
			// Credits
			tdg.setFont(nText);
			tdg.setColor(Color.BLACK);
			tdg.drawString("© Counter Game Developed by Peter Gordon", 20, windowHeight-20);
		}
		// Play game
		if (mode == 1) {
			// Game board
			tdg.setColor(Color.WHITE);
			tdg.fillRect(120, 0, windowWidth-240, windowHeight);
			tdg.setColor(Color.BLACK);
			tdg.drawRect(120, 0, windowWidth-240, windowHeight);
			// Draw backgrounds and grid
			for (int i=1; i < 12; i++) {
				String drawTo;
				if (i == 1||i == 5||i == 11) {
					tdg.setColor(Color.CYAN);
					tdg.fillRect(320, windowHeight-(i*75), windowWidth-440, 75);
				}
				switch (i) {
				case 1:
					drawTo = i + " - START";
					break;
				case 5:
					drawTo = i + " - SAFE";
					break;
				case 11:
					drawTo = i + " - FINISH";
					break;
				default:
					drawTo = i + "";
				}
				tdg.setColor(Color.BLACK);
				tdg.drawRect(320, windowHeight-(i*75), windowWidth-440, 75);
				tdg.drawRect(120, windowHeight-(i*75), 200, 75);
				tdg.setFont(boardText);
				tdg.drawString(drawTo, 220-(boardTextFm.stringWidth(drawTo)/2), windowHeight-(i*75-50));
			}
			// Column boundary boxes (to show which counters can be moved)
			if (currentGame.stage == 2) {
				if (p1.c1.hover && ghosts[0].y <= windowHeight) {
					tdg.setStroke(dashed2);
					tdg.setColor(Color.RED);
					tdg.draw(p1.c1.getDisplayColumn());
				}
				if (p1.c2.hover && ghosts[1].y <= windowHeight) { 
					tdg.setStroke(dashed2);
					tdg.setColor(Color.RED);
					tdg.draw(p1.c2.getDisplayColumn());
				}
			}
			if (currentGame.stage == 4) {
				if (p2.c1.hover && ghosts[0].y <= windowHeight) { 
					tdg.setStroke(dashed2);
					tdg.setColor(Color.BLUE);
					tdg.draw(p2.c1.getDisplayColumn());
				}
				if (p2.c2.hover && ghosts[1].y <= windowHeight) { 
					tdg.setStroke(dashed2);
					tdg.setColor(Color.BLUE);
					tdg.draw(p2.c2.getDisplayColumn());
				}
			}
			tdg.setColor(Color.BLACK);
			// Draw counter ghosts (at the place where the proper counter will move to)
			if(currentGame.stage == 2 || currentGame.stage == 4) {
				if (ghosts[0] != null) {
					tdg.setStroke(dashed);
					tdg.drawOval(ghosts[0].x, ghosts[0].y, 45, 45);
				}
				if (ghosts[1] != null) {
					tdg.setStroke(dashed);
					tdg.drawOval(ghosts[1].x, ghosts[1].y, 45, 45);
				}
			}
			// Draw counters (using fancy smooth paths!)
			if (fullScreen) {
				tdg.setColor(Color.RED);
				tdg.fillOval(p1.c1.x, p1.c1.getFancyY()+25, 45, 45);
				tdg.fillOval(p1.c2.x, p1.c2.getFancyY()+25, 45, 45);
				tdg.setColor(Color.BLUE);
				tdg.fillOval(p2.c1.x, p2.c1.getFancyY()+25, 45, 45);
				tdg.fillOval(p2.c2.x, p2.c2.getFancyY()+25, 45, 45);
			} else {
				// Normal mode
				tdg.setColor(Color.RED);
				tdg.fillOval(p1.c1.x, p1.c1.getFancyY(), 45, 45);
				tdg.fillOval(p1.c2.x, p1.c2.getFancyY(), 45, 45);
				tdg.setColor(Color.BLUE);
				tdg.fillOval(p2.c1.x, p2.c1.getFancyY(), 45, 45);
				tdg.fillOval(p2.c2.x, p2.c2.getFancyY(), 45, 45);
			}
			// Task bar
			// Roll button
			tdg.setColor(Color.BLACK);
			tdg.setFont(roll);
			if(currentGame.stage == 1 || currentGame.stage == 3) {
				tdg.drawString("Roll", (windowWidth/2)-(rollFm.stringWidth("Roll")/2), 125);
			}
			if(rollBox.hover) {tdg.setColor(Color.ORANGE);} else {tdg.setColor(Color.BLACK);}
			tdg.setStroke(thick);
			if(currentGame.stage == 1 || currentGame.stage == 3) {tdg.drawRect(rollBox.x, rollBox.y, (rollFm.stringWidth("Roll"))+50, rollFm.getHeight()+25);}
			// Draw player names on the board...
			if (currentGame.stage != 5) {
				tdg.setColor(Color.RED);
				tdg.drawString(p1.name, 125, 80);
				tdg.setColor(Color.BLUE);
				tdg.drawString(p2.name, windowWidth-125-rollFm.stringWidth(p2.name), 80);
			}
			// Roll RESULT text
			if(currentGame.stage == 2) {
				tdg.setColor(Color.ORANGE);
				tdg.drawString("" + p1.roll + "", 200, 150);
			}
			if(currentGame.stage == 4) {
				tdg.setColor(Color.ORANGE);
				tdg.drawString("" + p2.roll + "", windowWidth-200-rollFm.stringWidth("" + p2.roll + ""), 150);
			}
			// Show who won if neccessary, and restart menu
			tdg.setFont(title);
			if (currentGame.stage == 5) {
				// Restart menu
				tdg.setColor(transparentBlack);
				tdg.fillRect(0, 0, windowWidth, windowHeight);
				// "<player> wins!"
				tdg.setFont(title);
				tdg.setColor(Color.WHITE);
				if (currentGame.winner == 1) {
					tdg.setColor(Color.RED);
					tdg.drawString(p1.name + " wins!", windowWidth/2-titleFm.stringWidth(p1.name + " wins!")/2, windowHeight/2-100);
				}
				if (currentGame.winner == 2) {
					tdg.setColor(Color.BLUE);
					tdg.drawString(p2.name + " wins!", windowWidth/2-titleFm.stringWidth(p2.name + " wins!")/2, windowHeight/2-100);
				}
				// "New game"
				tdg.setColor(Color.WHITE);
				tdg.fillRect(windowWidth/4, windowHeight/2-50, windowWidth/2, 100);
				if(playAgain.hover) {tdg.setColor(Color.ORANGE);} else {tdg.setColor(Color.BLUE);}
				tdg.drawRect(playAgain.x, playAgain.y, windowWidth/2, 100);
				tdg.setColor(Color.RED);
				tdg.drawString("New game", windowWidth/2-titleFm.stringWidth("New game")/2, windowHeight/2+25);
				// "Rematch"
				tdg.setColor(Color.WHITE);
				tdg.fillRect(windowWidth/4, windowHeight/2+75, windowWidth/2, 100);
				if (quitEnd.hover) {tdg.setColor(Color.ORANGE);} else {tdg.setColor(Color.BLUE);}
				tdg.drawRect(quitEnd.x, quitEnd.y, windowWidth/2, 100);
				tdg.setColor(Color.RED);
				tdg.drawString("Rematch", windowWidth/2-titleFm.stringWidth("Rematch")/2, windowHeight/2+150);
			}
			if (currentGame.stage != 5 && currentGame.paused) {
				tdg.setColor(transparentBlack);
				tdg.fillRect(0, 0, windowWidth, windowHeight);
				// "Game paused"
				tdg.setFont(title);
				tdg.setColor(Color.WHITE);
				tdg.drawString("Game paused", windowWidth/2-titleFm.stringWidth("Game paused")/2, windowHeight/2-100);
				// "Resume"
				tdg.setColor(Color.WHITE);
				tdg.fillRect(windowWidth/4, windowHeight/2-50, windowWidth/2, 100);
				if(playAgain.hover) {tdg.setColor(Color.ORANGE);} else {tdg.setColor(Color.BLUE);}
				tdg.drawRect(playAgain.x, playAgain.y, windowWidth/2, 100);
				tdg.setColor(Color.RED);
				tdg.drawString("Resume", windowWidth/2-titleFm.stringWidth("Resume")/2, windowHeight/2+25);
				// "Main menu"
				tdg.setColor(Color.WHITE);
				tdg.fillRect(windowWidth/4, windowHeight/2+75, windowWidth/2, 100);
				if (quitEnd.hover) {tdg.setColor(Color.ORANGE);} else {tdg.setColor(Color.BLUE);}
				tdg.drawRect(quitEnd.x, quitEnd.y, windowWidth/2, 100);
				tdg.setColor(Color.RED);
				tdg.drawString("Main menu", windowWidth/2-titleFm.stringWidth("Main menu")/2, windowHeight/2+150);
			}
		}
		// Names
		if (mode == 2) {
			
		}

		// Draw the created image onto the screen
		if (mode != 2) g.drawImage(toDraw, 0, 0, null);
	}

	public void update() {
		if (mode == 1 && input.isKeyDown(KeyEvent.VK_ESCAPE)) { // Pause menu
			if (currentGame.pauseAllowed) {
				currentGame.paused = !currentGame.paused;
				currentGame.pauseAllowed = false;
			}
		} else if(mode == 1) {
			currentGame.pauseAllowed = true;
		}
		// Main menu
		// convert the point to use the top left of game window as 0 instead of whole screen
		Point mouse = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(mouse, this);
		if (mode == 0) {
			// menu boxes
			if(mouse.getX() > start.x && mouse.getY() > start.y && mouse.getX() < start.x+150 && mouse.getY() < start.y+150) {
				start.hover = true;
			} else {
				start.hover = false;
			}
			if(mouse.getX() > names.x && mouse.getY() > names.y && mouse.getX() < names.x+150 && mouse.getY() < names.y+150) {
				names.hover = true;
			} else {
				names.hover = false;
			}
			if(mouse.getX() > quit.x && mouse.getY() > quit.y && mouse.getX() < quit.x+150 && mouse.getY() < quit.y+150) {
				quit.hover = true;
			} else {
				quit.hover = false;
			}
			// Settings button
			if(mouse.getX() > windowWidth-58 && mouse.getY() > windowHeight-105 && mouse.getX() < windowWidth-8 && mouse.getY() < windowHeight-55) {
				settingsHover = true;
			} else {
				settingsHover = false;
			}
			// Info button
			if(mouse.getX() > windowWidth-118 && mouse.getY() > windowHeight-105 && mouse.getX() < windowWidth-68 && mouse.getY() < windowHeight-55) {
				infoHover = true;
			} else {
				infoHover = false;
			}
			/*windowWidth-58, windowHeight-105, 50, 50);
			 Info button: windowWidth-118, windowHeight-105, 50, 50*/
		}
		// Play game
		if (mode == 1 && !currentGame.paused) {
			if(mouse.getX() > rollBox.x && mouse.getY() > rollBox.y && mouse.getX() < rollBox.x+(rollFm.stringWidth("Roll"))+50 && mouse.getY() < rollBox.y+rollFm.getHeight()+25) {
				rollBox.hover = true;
			} else {
				rollBox.hover = false;
			}
			if (currentGame.stage == 2) {
				if(p1.c1.getColumn().intersects(mouse.getX(), mouse.getY(), 1, 1)) {
					p1.c1.hover = true;
				} else {
					p1.c1.hover = false;
				}
				if(p1.c2.getColumn().intersects(mouse.getX(), mouse.getY(), 1, 1)) {
					p1.c2.hover = true;
				} else {
					p1.c2.hover = false;
				}
			}
			if (currentGame.stage == 4) {
				if(p2.c1.getColumn().intersects(mouse.getX(), mouse.getY(), 1, 1)) {
					p2.c1.hover = true;
				} else {
					p2.c1.hover = false;
				}
				if(p2.c2.getColumn().intersects(mouse.getX(), mouse.getY(), 1, 1)) {
					p2.c2.hover = true;
				} else {
					p2.c2.hover = false;
				}
			}
			if (currentGame.stage == 5) {
				// play again
				if(mouse.getX() > playAgain.x && mouse.getY() > playAgain.y && mouse.getX() < playAgain.x+windowWidth/2 && mouse.getY() < playAgain.y+100) {
					playAgain.hover = true;
				} else {
					playAgain.hover = false;
				}
				// quitEnd
				if(mouse.getX() > quitEnd.x && mouse.getY() > quitEnd.y && mouse.getX() < quitEnd.x+windowWidth/2 && mouse.getY() < quitEnd.y+100) {
					quitEnd.hover = true;
				} else {
					quitEnd.hover = false;
				}
			}
			// Roll the dice!
			if(rollDice) {
				if(currentGame.stage == 1) {
					// Player 1's turn
					currentGame.stage = 2;
					p2.roll = -1;
					do {
						p1.roll = roll();
					} while (p1.roll == 4 && (p1.c1.square == 1 && p1.c2.square == 1));
					int y;
					// Player counters are at xPos: 370, 510, 650 and 790 (p1.c1, p1.c2, p2.c1, p2.c2)
					if (p1.roll < 4) {
						// Moving forwards
						// Don't display the ghost if counter is at the end (ghost would be off the board)
						if (p1.c1.getY()-(p1.roll*75) <= windowHeight-75*11+15) {
							if (p1.c1.square == 11) {
								y = windowHeight+75;
							} else {
								if (fullScreen) {y = windowHeight-60-75*10-25;} else {y = windowHeight-60-75*10;}
							}
						} else {
							y = p1.c1.getY()-(p1.roll*75);
						}
						// Test for counters being on the same square
						if (y == p1.c2.getY() && (y != windowHeight-75+15 && y != windowHeight-75*5+15 && y != windowHeight-75*11+15)) {
							y = windowHeight+75;
						}
						if (fullScreen) ghosts[0] = new Ghost(370, y+25); else ghosts[0] = new Ghost(370, y);
						if (p1.c2.getY()-(p1.roll*75) <= windowHeight-75*11-15) {
							if (p1.c2.square == 11) {
								y = windowHeight+75;
							} else {
								if (fullScreen) {y = windowHeight-60-75*10-25;} else {y = windowHeight-60-75*10;}
							}
						} else {
							y = p1.c2.getY()-(p1.roll*75);
						}
						// Test for counters being on the same square
						if (y == p1.c1.getY() && (y != windowHeight-75+15 && y != windowHeight-75*5+15 && y != windowHeight-75*11+15)) {
							y = windowHeight+75;
						}
						if (fullScreen) ghosts[1] = new Ghost(510, y+25); else ghosts[1] = new Ghost(510, y);
					} else if (p1.roll == 4) {
						// Moving backwards (rolled 4)
						y = p1.c1.getY()+75;
						// Counters will be on same square?
						if (y == p1.c2.getY() && (y != windowHeight-75+15 && y != windowHeight-75*5+15 && y != windowHeight-75*11+15)) {
							y = windowHeight+75;
						}
						if (fullScreen) ghosts[0] = new Ghost(370, y+25); else ghosts[0] = new Ghost(370, y);
						
						y = p1.c2.getY()+75;
						// Counters will be on the same square?
						if (y == p1.c1.getY() && (y != windowHeight-75+15 && y != windowHeight-75*5+15 && y != windowHeight-75*11+15)) {
							y = windowHeight+75;
						}
						if (fullScreen) ghosts[1] = new Ghost(510, y+25); else ghosts[1] = new Ghost(510, y);
					} else {
						// Roll is 5
						// Moving forwards, but cannot land on same square as another counter
						// Don't display the ghost if counter is at the end (ghost would be off the board)

						// Roll a 5
						int i = p1.c1.square+1;
						// Find the next free square (safe squares are always free)
						while ((i != 5 && i != 11) && (i == p1.c2.square || i == p2.c1.square || i == p2.c2.square)) {
							i++;
						}
						y = windowHeight - i*75+15;
						if (y < windowHeight-75*11+15) {
							y = windowHeight+75;
						}
						ghosts[0] = new Ghost(370, y);
						i = p1.c2.square+1;
						// Find the next free square (safe squares are always free)
						while ((i != 5 && i != 11) && (i == p1.c1.square || i == p2.c1.square || i == p2.c2.square)) {
							i++;
						}
						y = windowHeight - i*75+15;
						if (y < windowHeight-75*11+15) {
							y = windowHeight+75;
						}
						ghosts[1] = new Ghost(510, y);
					}
				} else if (currentGame.stage == 3) {
					// Player 2's turn
					currentGame.stage = 4;
					p1.roll = -1;
					do {
						p2.roll = roll();
					} while (p2.roll == 4 && (p2.c1.square == 1 && p2.c2.square == 1));
					// Player counters are at xPos: 370, 510, 650 and 790 (p1.c1, p1.c2, p2.c1, p2.c2)
					int y;
					if (p2.roll < 4) {
						// Moving forwards
						// Don't display the ghost if counter is at the end (ghost would be off the board)
						if (p2.c1.getY()-(p2.roll*75) <= windowHeight-75*11-15) {
							if (p2.c1.square == 11) {
								y = windowHeight+75;
							} else {
								if (fullScreen) {y = windowHeight-60-75*10-25;} else {y = windowHeight-60-75*10;}
							}
						} else {
							y = p2.c1.getY()-(p2.roll*75);
						}
						// Test for counters being on the same square
						if (y == p2.c2.getY() && (y != windowHeight-75+15 && y != windowHeight-75*5+15 && y != windowHeight-75*11+15)) {
							y = windowHeight+75;
						}
						if (fullScreen) ghosts[0] = new Ghost(650, y+25); else ghosts[0] = new Ghost(650, y);
						if (p2.c2.getY()-(p2.roll*75) <= windowHeight-75*11-15) {
							if (p2.c2.square == 11) {
								y = windowHeight+75;
							} else {
								if (fullScreen) {y = windowHeight-60-75*10;} else {y = windowHeight-60-75*10;}
							}
						} else {
							y = p2.c2.getY()-(p2.roll*75);
						}
						// Test for counters being on the same square
						if (y == p2.c1.getY() && (y != windowHeight-75+15 && y != windowHeight-75*5+15 && y != windowHeight-75*11+15)) {
							y = windowHeight+75;
						}
						if (fullScreen) ghosts[1] = new Ghost(790, y+25); else ghosts[1] = new Ghost(790, y);
					} else if (p2.roll == 4) {
						// Moving backwards (rolled 4)
						y = p2.c1.getY()+75;
						if (fullScreen) ghosts[0] = new Ghost(650, y+25); else ghosts[0] = new Ghost(650, y);
						if (y == p2.c2.getY() && (y != windowHeight-75+15 && y != windowHeight-75*5+15 && y != windowHeight-75*11+15)) {
							y = windowHeight+75;
						}
						y = p2.c2.getY()+75;
						if (fullScreen) ghosts[1] = new Ghost(790, y+25); else ghosts[1] = new Ghost(790, y);
						if (y == p2.c1.getY() && (y != windowHeight-75+15 && y != windowHeight-75*5+15 && y != windowHeight-75*11+15)) {
							y = windowHeight+75;
						}
					} else {
						// Roll is 5
						// Moving forwards, but cannot land on same square as another counter
						// Don't display the ghost if counter is at the end (ghost would be off the board)

						// Roll a 5
						int i = p2.c1.square+1;
						// Find the next free square (safe squares are always free)
						while ((i != 5 && i != 11) && (i == p1.c1.square || i == p1.c2.square || i == p2.c2.square)) {
							i++;
						}
						y = windowHeight - i*75+15;
						if (y < windowHeight-75*11+15) {
							y = windowHeight+75;
						}
						ghosts[0] = new Ghost(650, y);
						i = p2.c2.square+1;
						// Find the next free square (safe squares are always free)
						while ((i != 5 && i != 11) && (i == p1.c1.square || i == p1.c2.square || i == p2.c1.square)) {
							i++;
						}
						y = windowHeight - i*75+15;
						if (y < windowHeight-75*11+15) {
							y = windowHeight+75;
						}
						ghosts[1] = new Ghost(790, y);
					}
				}
				rollDice = false;
			}
			// Check if the roll can be carried out then execute it (only if at - stage 2: player 1, or 4: player 2)
			if (currentGame != null) { // in case the currentGame class hasn't been initialised!
				if (currentGame.stage == 1 || currentGame.stage == 3 || currentGame.stage == 5) {
					p1.c1.selected = false;
					p1.c1.hover = false;
					p1.c2.selected = false;
					p1.c2.hover = false;
					p2.c1.selected = false;
					p2.c1.hover = false;
					p2.c2.selected = false;
					p2.c2.hover = false;
				}
				if (currentGame.stage == 2) {
					p2.endCt = false;
					p2.sameSquare = false;
					if (p1.c1.selected && ghosts[0].y <= windowHeight) {
						p1.endCt = false;
						p1.sameSquare = false;
						if (p1.roll == 4) {
							if (!(p1.c1.square - 1 == 1 || p1.c1.square - 1 == 5 || p1.c1.square - 1 == 11) && p1.c1.square - 1 == p1.c2.square) {
								p1.sameSquare = true;
							} else if (p1.c1.square == 1) {
								// don't move off the board!
								p1.invalidMove = true;
							} else {
								p1.c1.square -= 1;

								p1.invalidMove = false;
								currentGame.stage = 3;
							}
						} else if (p1.roll < 4) {
							if (!(p1.c1.square + p1.roll == 1 || p1.c1.square + p1.roll == 5 || p1.c1.square + p1.roll == 11) && p1.c1.square + p1.roll == p1.c2.square) {
								p1.sameSquare = true;
							} else if (p1.c1.square == 11) {
								p1.endCt = true;
							} else {
								p1.c1.square += p1.roll;
								currentGame.stage = 3;
								p1.invalidMove = false;
							}
						} else {
							// Roll a 5
							int i = p1.c1.square+1;
							// Find the next free square (safe squares are always free)
							while ((i != 5 && i != 11) && (i == p1.c2.square || i == p2.c1.square || i == p2.c2.square)) {
								i++;
							}
							p1.c1.square = i;
							currentGame.stage = 3;
						}
					}
					if (p1.c2.selected && ghosts[1].y <= windowHeight) {
						p1.endCt = false;
						p1.sameSquare = false;
						if (p1.roll == 4) {
							if (!(p1.c2.square - 1 == 1 || p1.c2.square - 1 == 5 || p1.c2.square - 1 == 11) && p1.c2.square - 1 == p1.c1.square) {
								p1.sameSquare = true;
							} else if (p1.c2.square == 1) {
								// don't move off the board!
								p1.invalidMove = true;
							} else {
								p1.c2.square -= 1;
								currentGame.stage = 3;
								p1.invalidMove = false;
							}
						} else if (p1.roll < 4) {
							if (!(p1.c2.square + p1.roll == 1 || p1.c2.square + p1.roll == 5 || p1.c2.square + p1.roll == 11) && p1.c2.square + p1.roll == p1.c1.square) {
								p1.sameSquare = true;
							} else if (p1.c2.square == 11) {
								p1.endCt = true;
							} else {
								p1.c2.square += p1.roll;
								p1.invalidMove = false;
								currentGame.stage = 3;
							}
						} else {
							// Roll a 5
							int i = p1.c2.square+1;
							// Find the next free square (safe squares are always free)
							while ((i != 5 && i != 11) && (i == p1.c1.square || i == p2.c1.square || i == p2.c2.square)) {
								i++;
							}
							p1.c2.square = i;
							currentGame.stage = 3;
						}
					}
					// Send a player back to start if move landed on the same square
					/* COUNTER 1 */
					if (p1.c1.square == p2.c1.square) {
						if (p1.c1.square == 1 || p1.c1.square == 5 || p1.c1.square == 11) {
							// The square is safe, players cannot be moved back to start!
						} else {
							p2.c1.square = 1;
						}
					}
					if (p1.c1.square == p2.c2.square) {
						if (p1.c1.square == 1 || p1.c1.square == 5 || p1.c1.square == 11) {
							// The square is safe, players cannot be moved back to start!
						} else {
							p2.c2.square = 1;
						}
					}
					/* COUNTER 2 */
					if (p1.c2.square == p2.c1.square) {
						if (p1.c2.square == 1 || p1.c2.square == 5 || p1.c2.square == 11) {
							// The square is safe, players cannot be moved back to start!
						} else {
							p2.c1.square = 1;
						}
					}
					if (p1.c2.square == p2.c2.square) {
						if (p1.c2.square == 1 || p1.c2.square == 5 || p1.c2.square == 11) {
							// The square is safe, players cannot be moved back to start!
						} else {
							p2.c2.square = 1;
						}
					}
				}
				if (currentGame.stage == 4) {
					p1.endCt = false;
					p1.sameSquare = false;
					if (p2.c1.selected && ghosts[0].y <= windowHeight) {
						p2.endCt = false;
						p2.sameSquare = false;
						if (p2.roll == 4) {
							if (!(p2.c1.square - 1 == 1 || p2.c1.square - 1 == 5 || p2.c1.square - 1 == 11) && p2.c1.square - 1 == p2.c2.square) {
								p2.sameSquare = true;
							} else if (p2.c1.square == 1) {
								// don't move off the board!
								p2.invalidMove = true;
							} else {
								p2.c1.square -= 1;
								currentGame.stage = 1;
								p2.invalidMove = false;
							}
						} else if (p2.roll < 4) {
							if (!(p2.c1.square + p2.roll == 1 || p2.c1.square + p2.roll == 5 || p2.c1.square + p2.roll == 11) && p2.c1.square + p2.roll == p2.c2.square) {
								p2.sameSquare = true;
							} else if (p2.c1.square == 11) {
								p2.endCt = true;
							} else {
								p2.c1.square += p2.roll;
								p2.invalidMove = false;
								currentGame.stage = 1;
							}
						} else {
							// Roll a 5
							int i = p2.c1.square+1;
							// Find the next free square (safe squares are always free)
							while ((i != 5 && i != 11) && (i == p1.c1.square || i == p1.c2.square || i == p2.c2.square)) {
								i++;
							}
							p2.c1.square = i;
							currentGame.stage = 1;
						}
					}
					if (p2.c2.selected && ghosts[1].y <= windowHeight) {
						p2.endCt = false;
						p2.sameSquare = false;
						if (p2.roll == 4) {
							if (!(p2.c2.square - 1 == 1 || p2.c2.square - 1 == 5 || p2.c2.square - 1 == 11) && p2.c2.square - 1 == p2.c1.square) {
								p2.sameSquare = true;
							} else if (p2.c2.square == 1) {
								// don't move off the board!
								p2.invalidMove = true;
							} else {
								p2.c2.square -= 1;
								currentGame.stage = 1;
								p2.invalidMove = false;
							}
						} else if (p2.roll < 4) {
							if (!(p2.c2.square + p2.roll == 1 || p2.c2.square + p2.roll == 5 || p2.c2.square + p2.roll == 11) && p2.c2.square + p2.roll == p2.c1.square) {
								p2.sameSquare = true;
							} else if (p2.c2.square == 11) {
								p2.endCt = true;
							} else {
								p2.c2.square += p2.roll;
								p2.invalidMove = false;
								currentGame.stage = 1;
							}
						} else {
							// Roll a 5
							int i = p2.c2.square+1;
							// Find the next free square (safe squares are always free)
							while ((i != 5 && i != 11) && (i == p1.c1.square || i == p1.c2.square || i == p2.c1.square)) {
								i++;
							}
							p2.c2.square = i;
							currentGame.stage = 1;
						}
					}
					// Send a player back to start if move landed on the same square
					/* COUNTER 1 */
					if (p2.c1.square == p1.c1.square) {
						if (p2.c1.square == 1 || p2.c1.square == 5 || p2.c1.square == 11) {
							// The square is safe, players cannot be moved back to start!
						} else {
							p1.c1.square = 1;
						}
					}
					if (p2.c1.square == p1.c2.square) {
						if (p2.c1.square == 1 || p2.c1.square == 5 || p2.c1.square == 11) {
							// The square is safe, players cannot be moved back to start!
						} else {
							p1.c2.square = 1;
						}
					}
					/* COUNTER 2 */
					if (p2.c2.square == p1.c1.square) {
						if (p2.c2.square == 1 || p2.c2.square == 5 || p2.c2.square == 11) {
							// The square is safe, players cannot be moved back to start!
						} else {
							p1.c1.square = 1;
						}
					}
					if (p2.c2.square == p1.c2.square) {
						if (p2.c2.square == 1 || p2.c2.square == 5 || p2.c2.square == 11) {
							// The square is safe, players cannot be moved back to start!
						} else {
							p1.c2.square = 1;
						}
					}
				}
				// Make sure the counters don't go off the board
				if (p1.c1.square > 11) {
					p1.c1.square = 11;
				} else if (p1.c1.square < 1) {
					p1.c1.square = 1;
				}
				if (p1.c2.square > 11) {
					p1.c2.square = 11;
				} else if (p1.c2.square < 1) {
					p1.c2.square = 1;
				}
				if (p2.c1.square > 11) {
					p2.c1.square = 11;
				} else if (p2.c1.square < 1) {
					p2.c1.square = 1;
				}
				if (p2.c2.square > 11) {
					p2.c2.square = 11;
				} else if (p2.c2.square < 1) {
					p2.c2.square = 1;
				}
				// See if there's a winner
				if (p1.c1.square == 11 && p1.c2.square == 11) {
					// Player 1 wins!
					currentGame.stage = 5;
					currentGame.winner = 1;
					p1.roll = -1;
					p2.roll = -1;
				}
				if (p2.c1.square == 11 && p2.c2.square == 11) {
					// Player 2 wins!
					currentGame.stage = 5;
					currentGame.winner = 2;
					p1.roll = -1;
					p2.roll = -1;
				}
			}
			// Update the counter positions. The counter will speed up as it moves!
			new AniPath(this, 1).start();
			new AniPath(this, 2).start();
			new AniPath(this, 3).start();
			new AniPath(this, 4).start();
		}
		if (mode == 1 && currentGame.paused && currentGame.stage != 5) {
			// resume
			if(mouse.getX() > playAgain.x && mouse.getY() > playAgain.y && mouse.getX() < playAgain.x+windowWidth/2 && mouse.getY() < playAgain.y+100) {
				playAgain.hover = true;
			} else {
				playAgain.hover = false;
			}
			// main menu
			if(mouse.getX() > quitEnd.x && mouse.getY() > quitEnd.y && mouse.getX() < quitEnd.x+windowWidth/2 && mouse.getY() < quitEnd.y+100) {
				quitEnd.hover = true;
			} else {
				quitEnd.hover = false;
			}
		}
		// Names
		if (mode == 2) {
			// Make sure the user didn't press cancel or leave the box blank
			if (p1.name == null || p1.name.length() == 0) {
				p1.name = "Player 1";
			}
			if (p2.name == null || p2.name.length() == 0) {
				p2.name = "Player 2";
			}
			//setMode(0, false);
		}
	}
	public void setMode(int mode, boolean reset) {
		switch (mode) {
		case 0:
			// Main menu
			windowHeight = 650;
			windowWidth = 650;
			settingsHover = false;
			infoHover = false;
			if (fullScreen) {
				windowHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
				windowWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
				setLocation(0, 0);
				setAlwaysOnTop(true);
				dispose();
				setUndecorated(true);
				pack();
			}
			if (reset) {
				// does the game need reset?
				p1 = new Player("Player 1", 1, this);
				p2 = new Player("Player 2", 2, this);
			}
			setVisible(true);
			break;
		case 1:
			// Play game
			currentGame = new GameBoard();
			p1.resetCt();
			p2.resetCt();
			windowHeight = 1000;
			windowWidth = 1000;
			if (fullScreen) {
				windowHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
				windowWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
				setLocation(0, 0);
				setAlwaysOnTop(true);
				dispose();
				setUndecorated(true);
				pack();
			}
			// Window width has changed, so reposition boxes!
			playAgain = new MenuBox(windowWidth/4, windowHeight/2-50);
			quitEnd = new MenuBox(windowWidth/4, windowHeight/2+75);
			setVisible(true);
			break;
		case 2:
			// Names
			nameBox = new NameInput(this);
			setVisible(false);
			break;
		}
		toDraw = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_RGB);
		setSize(windowWidth, windowHeight); // Set window size to windowHeight and windowWidth
		
		if (!fullScreen) {setLocationRelativeTo(null);}
		this.mode = mode;
	}
	public int roll() {
		// extra random, new random number is generated each time!
		random = new Random(System.currentTimeMillis());
		if (rollFive) {
			return random.nextInt(5)+1;
		} else {
			return random.nextInt(4)+1;
		}
	}
}
