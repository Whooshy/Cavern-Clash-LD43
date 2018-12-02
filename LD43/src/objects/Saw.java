package objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import utilities.Images;
import world.World;

public class Saw 
{
	public float x, y, velX, velY;
	public int frameCount = 0;
	public int direction = 0;

	public Saw(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void update(Graphics2D g)
	{
		frameCount++;
		if(frameCount >= 20)
		{
			frameCount = 0;
		}
		
		g.drawImage(Images.saws[frameCount / 10], (int) x, (int) y, 32, 32, null);
		
		collision();
		
		if(direction == 0) velX = 4;
		if(direction == 1) velX = -4;
		
		x += velX;
		y += velY;
	}
	
	public void collision()
	{
		for(int x = 0; x < World.tiles.length; x++)
		for(int y = 0; y < World.tiles[0].length; y++)
		{
			if(World.tiles[x][y].getBounds().intersects(getLeftBounds()) && World.tiles[x][y].id == 1)
			{
				direction = 0;
			}
			if(World.tiles[x][y].getBounds().intersects(getRightBounds()) && World.tiles[x][y].id == 1)
			{
				direction = 1;
			}
		}
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle((int) x, (int) y, 32, 32);
	}
	
	public Rectangle getTopBounds()
	{
		return new Rectangle((int) x + 5, (int) y, 22, 5);
	}
	
	public Rectangle getBottomBounds()
	{
		return new Rectangle((int) x + 5, (int) y + 27, 22, 5);
	}
	
	public Rectangle getLeftBounds()
	{
		return new Rectangle((int) x, (int) y + 5, 5, 22);
	}
	
	public Rectangle getRightBounds()
	{
		return new Rectangle((int) x + 27, (int) y + 5, 5, 22);
	}
}
