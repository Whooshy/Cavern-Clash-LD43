package objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import utilities.Images;

public class HelixProjectile {
	
	public float centerX, centerY, distance, speed;
	private int lifetime;
	public float x, y, velX, velY;
	
	public BufferedImage projectileImage = Images.projectiles.getSubimage(10, 0, 5, 5);

	public HelixProjectile(float centerX, float centerY, float distance, float speed, int lifetime)
	{
		this.centerX = centerX;
		this.centerY = centerY;
		this.distance = distance;
		this.speed = speed;
		this.lifetime = lifetime;
		
		x = centerX - distance;
		y = centerY - distance;
	}
	
	public void update(Graphics2D g)
	{
		lifetime--;
		
		g.drawImage(projectileImage, (int) x, (int) y, 10, 10, null);
		
		if(x <= centerX)
		{
			velX += (0.01f * speed);
		}
		
		if(x > centerX)
		{
			velX -= (0.01f * speed);
		}
		
		if(y <= centerY)
		{
			velY += (0.001f * speed);
		}
		
		if(y > centerY)
		{
			velY -= (0.001f * speed);
		}
		
		x += velX;
		y += velY;
	}

	public int getLifetime() 
	{
		return lifetime;
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle((int) x, (int) y, 15, 15);
	}
}
