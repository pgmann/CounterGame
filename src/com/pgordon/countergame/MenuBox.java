package com.pgordon.countergame;

import java.awt.Point;
import java.awt.Rectangle;

public class MenuBox {
	int x, y;
	boolean hover;
	MenuBox(int x, int y) {
		this.x = x;
		this.y = y;
		this.hover = false;
	}
	public Rectangle getRect() {
		return new Rectangle(x, y, 150, 150);
	}
	public Point getCorner() {
		return new Point(x, y);
	}
}
