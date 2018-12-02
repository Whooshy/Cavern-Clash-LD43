package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import utilities.Images;

public class Tile
{
	public int x, y, id;
	
	public Tile(int x, int y, int id)
	{
		this.x = x;
		this.y = y;
	
		this.id = id;
	}
	
	public void update(Graphics2D g)
	{
		if(id > 0) g.drawImage(Images.tiles[id - 1], x, y, 32, 32, null);
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle(x, y, 32, 32);
	}

}
