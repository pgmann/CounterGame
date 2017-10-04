package com.pgordon.countergame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class NameInput extends JFrame implements KeyListener {
	private static final long serialVersionUID = 3729597673585462872L;

	Game game;
	NameInputBoard nib;
	boolean[] keys = new boolean[256];

	public NameInput(Game game) {
		this.game = game;

		setTitle("Enter names");
		setSize(290, 200);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		nib = new NameInputBoard(game, this);
		add(nib);
		addWindowListener(new CustomWindowAdapter(game));
		setVisible(true);
		
		addKeyListener(this);
		
		
	}
	
	public void update() {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			nib.closeWindow();
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
class CustomWindowAdapter extends WindowAdapter {
	Game game;

	CustomWindowAdapter(Game game) {
		this.game = game;
	}
	public void windowClosing(WindowEvent e) {
		game.setMode(0, false);
	}
}
