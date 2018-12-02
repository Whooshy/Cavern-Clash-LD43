package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import utilities.Images;

public class Projectile {
	
	public float x, y, dirX, dirY, speed;
	private float distance, distX, distY;
	private int lifetime, id;
	
	public Projectile(int id, float x, float y, float dirX, float dirY, float speed, int lifetime)
	{
		this.x = x;
		this.y = y;
		this.id = id;
		this.dirX = dirX;
		this.dirY = dirY;
		this.speed = speed;
		this.lifetime = lifetime;
		
		distance = (float) Math.sqrt(Math.pow((dirX), 2) + Math.pow((dirY), 2));
	}
	
	public void update(Graphics2D g)
	{
		g.setColor(Color.WHITE);
		if(id == 0)
		{
			g.setColor(Color.CYAN);
			g.fillOval((int) x, (int) y, 5, 10);
		}
		if(id == 1)
		{
			g.setColor(Color.ORANGE);
			g.drawOval((int) x, (int) y, 10, 5);
		}
		if(id == 2) g.drawImage(Images.projectiles.getSubimage(10, 0, 5, 5), (int) x, (int) y, 10, 10, null);
		
		x += (dirX / distance) * speed;
		y += (dirY / distance) * speed;
		
		lifetime--;
	}
	
	public int getLifetime()
	{
		return lifetime;
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle((int) x, (int) y, 10, 10);
	}

}
