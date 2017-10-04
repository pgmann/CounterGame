package com.pgordon.countergame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NameInputBoard extends JPanel {
	private static final long serialVersionUID = 5259764690655677111L;
	Game game;
	NameInput ni;
	JTextField p1name, p2name;
	JButton submit;
	Font bold = new Font("Arial", Font.BOLD, 32);
	ActionListener listener;
	
	
	
	public NameInputBoard(Game game, NameInput ni) {
		this.game = game;
		this.ni = ni;
		setBackground(Color.BLACK);
		setLayout(null);
		
		p1name = new JTextField(20);
		p2name = new JTextField(20);
		submit = new JButton();

		p1name.setSize(250, 40);
		p1name.setLocation(20, 20);
		p1name.setBackground(Color.RED);
		p1name.setForeground(Color.BLACK);
		p1name.setFont(bold);
		p1name.setText(game.p1.name);
		p1name.setBorder(BorderFactory.createCompoundBorder(p1name.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		p1name.addKeyListener(ni);
		
		p2name.setSize(250, 40);
		p2name.setLocation(20, 70);
		p2name.setBackground(Color.BLUE);
		p2name.setForeground(Color.WHITE);
		p2name.setFont(bold);
		p2name.setText(game.p2.name);
		p2name.setBorder(BorderFactory.createCompoundBorder(p2name.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		p2name.addKeyListener(ni);
		
		submit.setSize(250, 40);
		submit.setLocation(20, 120);
		submit.setBackground(Color.WHITE);
		submit.setForeground(Color.BLACK);
		submit.setFont(bold);
		submit.setText("Submit");
		submit.setBorder(BorderFactory.createCompoundBorder(p2name.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		submit.addKeyListener(ni);
		listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeWindow();
			}
		};
		submit.addActionListener(listener);
		
		add(p1name);
		add(p2name);
		add(submit);
	}
	public void closeWindow() {
		game.p1.name = p1name.getText();
		if (p1name.getText().equals("")) {game.p1.name="Player 1";}
		game.p2.name = p2name.getText();
		if (p2name.getText().equals("")) {game.p1.name="Player 2";}
		ni.setVisible(false);
		game.setMode(0, false);
		ni.dispose();
	}
}
