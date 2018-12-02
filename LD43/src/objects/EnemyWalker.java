package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

import hud.HUD;
import utilities.Images;
import world.World;

public class EnemyWalker {
	
	public float x, y, velX, velY;
	public int direction = 0;
	
	public float speed = 3;
	public int health;
	public int hpCooldown = 0;
	
	public float enemyFrame = 0;
	
	public int flickerTime = 7;
	public boolean shouldDraw = true;
	
	public AudioInputStream siren;
	public Clip sirenClip;
	
	public EnemyWalker(float x, float y, int health)
	{
		this.x = x;
		this.y = y;
		this.health = health;
		
		try {
			siren = AudioSystem.getAudioInputStream(getClass().getResource("/audio/siren.wav"));
			sirenClip = AudioSystem.getClip();
			sirenClip.open(siren);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update(Graphics2D g)
	{
		enemyFrame += 0.05f;
		
		if(enemyFrame >= 4)
		{
			enemyFrame = 0;
		}
		
		g.setColor(Color.RED);
		if(shouldDraw) g.drawImage(Images.enemySprites[(int) enemyFrame], (int) x, (int) y, 32, 32, null);
		
		if(direction == 0)
		{
			velX = speed;
		}
		if(direction == 1)
		{
			velX = -speed;
		}
		
		collisionUpdate();
		
		x += velX;
		y += velY;
	}
	
	public void collisionUpdate()
	{
		for(int x = 0; x < 26; x++)
		for(int y = 0; y < 20; y++)
		{
			if(World.tiles[x][y].getBounds().intersects(getBottomBounds()) && World.tiles[x][y].id == 1)
			{
				velY = 0;
			}
			else if(World.tiles[x][y].getBounds().intersects(getBottomBounds()) && World.tiles[x][y].id != 1)
			{
				velY += 0.1f;
				if(velY > 5)
				{
					velY = 5;
				}
			}
			
			if(World.tiles[x][y].getBounds().intersects(getRightBounds()) && World.tiles[x][y].id == 1 && World.tiles[x][y - 1].id != 1)
			{
				this.y -= 5;
				velY = -5;
			}
			else if(World.tiles[x][y].getBounds().intersects(getRightBounds()) && World.tiles[x][y].id == 1 && World.tiles[x][y - 1].id == 1)
			{
				direction = 1;
			}
			
			if(World.tiles[x][y].getBounds().intersects(getLeftBounds()) && World.tiles[x][y].id == 1 && World.tiles[x][y - 1].id != 1)
			{
				this.y -= 5;
				velY = -5;
			}
			else if(World.tiles[x][y].getBounds().intersects(getLeftBounds()) && World.tiles[x][y].id == 1 && World.tiles[x][y - 1].id == 1)
			{
				direction = 0;
			}
		}
		
		for(int x = 0; x < 26; x++)
		for(int y = 0; y < 20; y++)
		{
			if(World.tiles[x][y].getBounds().intersects(getBounds()) && World.tiles[x][y].id == 4 && hpCooldown == 0)
			{
				health -= 1;
				hpCooldown = 6;
			}
			
			if(World.tiles[x][y].getBounds().intersects(getBounds()) && World.tiles[x][y].id == 15 && World.hpCooldown == 0)
			{
				HUD.hp -= 1;
				World.hpCooldown = 100;
				
				sirenClip.setFramePosition(0);
				sirenClip.start();
			}
		}
		
		for(int i = 0; i < World.projectiles.size(); i++)
		{
			if(World.projectiles.get(i).getBounds().intersects(getBounds()) && hpCooldown == 0)
			{
				health -= 2;
				hpCooldown = 20;
				World.projectiles.remove(i);
			}
		}
		
		for(int i = 0; i < World.helixProjectiles.size(); i++)
		{
			if(World.helixProjectiles.get(i).getBounds().intersects(getBounds()) && hpCooldown == 0)
			{
				health -= 5;
				hpCooldown = 50;
				speed = 1.5f;
			}
		}
		
		for(int i = 0; i < World.saws.size(); i++)
		{
			if(World.saws.get(i).getBounds().intersects(getBounds()) && hpCooldown == 0)
			{
				health -= 12;
				hpCooldown = 25;
				speed = 0.5f;
			}
		}
		
		if(hpCooldown > 0)
		{
			shouldDraw = true;
			flickerTime -= 1;
			if(flickerTime <= 1)
			{
				shouldDraw = false;
				flickerTime = 7;
			}
			hpCooldown -= 1;
		}
		else
		{
			shouldDraw = true;
			speed = 3;
		}
	}
	
	public Rectangle getBottomBounds()
	{
		return new Rectangle((int) x + 2, (int) y + 24, 28, 8);
	}
	
	public Rectangle getTopBounds()
	{
		return new Rectangle((int) x + 2, (int) y, 28, 8);
	}
	
	public Rectangle getLeftBounds()
	{
		return new Rectangle((int) x, (int) y + 2, 4, 20);
	}
	
	public Rectangle getRightBounds()
	{
		return new Rectangle((int) x + 28, (int) y + 2, 4, 20);
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle((int) x, (int) y, 32, 32);
	}

}
