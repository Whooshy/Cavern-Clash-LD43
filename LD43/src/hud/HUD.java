package hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import engine.Engine;
import utilities.Images;

public class HUD {
	
	public static int wave, hp, sacrifices, traps, time, frameTime;
	public static int currentHud = 0;
	public static int currentShopSelection = 0;
	
	public int deathCounter = 0;
	
	public static boolean canSkipWave = false;
	
	public static boolean newWave = false;
	
	public HUD()
	{
		wave = 0;
		hp = 10;
		sacrifices = 0;
		traps = 3;
		time = 15;
		frameTime = 0;
		currentHud = 0;
		currentShopSelection = 0;
		deathCounter = 0;
		canSkipWave = false;
	}
	
	public void update(Graphics2D g)
	{
		g.setFont(new Font("Tahoma", Font.PLAIN, 25));
		g.setColor(Color.RED);
		g.drawString("Wave: " + wave, 20, 50);
		g.setFont(new Font("Tahoma", Font.PLAIN, 10));
		g.drawString("Time: " + time, 20, 62);
		
		g.setFont(new Font("Tahoma", Font.BOLD, 18));
		g.setColor(Color.CYAN);
		g.drawString("HP: " + hp, 20, 580);
		
		g.setFont(new Font("Tahoma", Font.ITALIC, 22));
		g.setColor(Color.YELLOW);
		g.drawString("Sacrifices: " + sacrifices, 650, 50);
		
		if(currentHud != 1) g.setColor(Color.WHITE);
		if(currentHud == 1) g.setColor(Color.RED);
		g.drawRect(720, 550, 60, 40);
		g.setFont(new Font("Tahoma", Font.PLAIN, 20));
		g.drawString("Shop", 729, 577);
		
		g.setColor(Color.MAGENTA);
		g.drawRect(570, 550, 120, 40);
		g.setFont(new Font("Tahoma", Font.PLAIN, 23));
		g.drawString("Sacrifice", 586, 580);
		
		g.setColor(Color.GREEN);
		g.drawRect(420, 550, 120, 40);
		g.setFont(new Font("Tahoma", Font.PLAIN, 14));
		g.drawString("Place Traps (" + traps + ")", 436, 575);
		
		if(canSkipWave)
			g.setColor(Color.WHITE);
		else
			g.setColor(Color.DARK_GRAY);
		
		g.drawRect(270, 550, 120, 40);
		g.setFont(new Font("Tahoma", Font.ITALIC, 20));
		g.drawString("NEXT WAVE", 277, 578);
		
		frameTime++;
		if(frameTime >= (int) Engine.FPS_LOCK)
		{
			frameTime = 0;
			time -= 1;
		}
		
		if(time == 0)
		{
			wave += 1;
			time = 60;
			newWave = true;
			canSkipWave = false;
		}
		
		if(hp <= 0)
		{
			currentHud = 5;
			hp = 0;
		}
		
		if(currentHud == 1)
		{
			if(currentShopSelection != 1) g.setColor(Color.WHITE);
			if(currentShopSelection == 1) g.setColor(Color.RED);
			g.drawRect(680, 500, 100, 40);
			if(currentShopSelection != 2) g.setColor(Color.WHITE);
			if(currentShopSelection == 2) g.setColor(Color.RED);
			g.drawRect(680, 450, 100, 40);
			if(currentShopSelection != 3) g.setColor(Color.WHITE);
			if(currentShopSelection == 3) g.setColor(Color.RED);
			g.drawRect(680, 400, 100, 40);
			if(currentShopSelection != 4) g.setColor(Color.WHITE);
			if(currentShopSelection == 4) g.setColor(Color.RED);
			g.drawRect(680, 350, 100, 40);
			
			if(currentShopSelection != 1) g.setColor(Color.WHITE);
			if(currentShopSelection == 1) g.setColor(Color.RED);
			g.setFont(new Font("Tahoma", Font.PLAIN, 15));
			g.drawString("Verturret", 685, 515);
			g.setFont(new Font("Tahoma", Font.PLAIN, 10));
			g.drawString("3 Sacrifices", 685, 535);
			
			if(currentShopSelection != 2) g.setColor(Color.WHITE);
			if(currentShopSelection == 2) g.setColor(Color.RED);
			g.setFont(new Font("Tahoma", Font.PLAIN, 15));
			g.drawString("Wave Wiper", 685, 465);
			g.setFont(new Font("Tahoma", Font.PLAIN, 10));
			g.drawString("5 Sacrifices", 685, 485);
			
			if(currentShopSelection != 3) g.setColor(Color.WHITE);
			if(currentShopSelection == 3) g.setColor(Color.RED);
			g.setFont(new Font("Tahoma", Font.PLAIN, 15));
			g.drawString("Helix Core", 685, 415);
			g.setFont(new Font("Tahoma", Font.PLAIN, 10));
			g.drawString("8 Sacrifices", 685, 435);
			
			if(currentShopSelection != 4) g.setColor(Color.WHITE);
			if(currentShopSelection == 4) g.setColor(Color.RED);
			g.setFont(new Font("Tahoma", Font.PLAIN, 15));
			g.drawString("Circle Saw", 685, 365);
			g.setFont(new Font("Tahoma", Font.PLAIN, 10));
			g.drawString("15 Sacrifices", 685, 385);
			
			if(currentShopSelection >= 1)
			{
				currentHud = 4;
			}
		}
		if(currentHud == 2)
		{	
			g.drawImage(Images.overlayColors.getSubimage(0, 0, 1, 1), 0, 0, 800, 600, null);
			
			g.setColor(Color.BLACK);
			g.setFont(new Font("Tahoma", Font.BOLD, 20));
			g.drawString("Select traps or turrets to sacrifice", 215, 50);
		}
		if(currentHud == 3)
		{
			g.drawImage(Images.overlayColors.getSubimage(1, 0, 1, 1), 0, 0, 800, 600, null);

			g.setColor(Color.BLACK);
			g.setFont(new Font("Tahoma", Font.BOLD, 20));
			g.drawString("Place a trap", 330, 50);
		}
		if(currentHud == 4)
		{
			g.drawImage(Images.overlayColors.getSubimage(2, 0, 1, 1), 0, 0, 800, 600, null);

			g.setColor(Color.BLACK);
			g.setFont(new Font("Tahoma", Font.BOLD, 20));
			g.drawString("Place your turret", 330, 50);
		}
		if(currentHud == 5)
		{
			g.drawImage(Images.overlayColors.getSubimage(3, 0, 1, 1), 0, 0, 800, 600, null);
			
			g.setColor(Color.BLACK);
			g.setFont(new Font("Tahoma", Font.BOLD, 20));
			g.drawString("GAME OVER", 330, 310);
			
			deathCounter++;
			
			if(deathCounter > 500)
			{
				deathCounter = 0;
				MapSelection.mapHighlight = 0;
				Engine.refreshing = true;
				Engine.STATE = 0;
			}
		}
	}

}
