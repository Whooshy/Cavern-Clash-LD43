package hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import utilities.Images;

public class MainMenu 
{
	public int menuHightlight = 0;
	
	public MainMenu()
	{
		
	}
	
	public void update(Graphics2D g)
	{
		g.drawImage(Images.logo, 25, 25, 400, 50, null);
		
		g.setFont(new Font("Tahoma", Font.PLAIN, 25));
		
		if(menuHightlight == 1) g.setColor(Color.RED);
		if(menuHightlight != 1) g.setColor(Color.WHITE);
		
		g.drawString("Play", 25, 500);
		
		if(menuHightlight == 2) g.setColor(Color.RED);
		if(menuHightlight != 2) g.setColor(Color.WHITE);
		
		g.drawString("Help", 25, 530);
		
		if(menuHightlight == 3) g.setColor(Color.RED);
		if(menuHightlight != 3) g.setColor(Color.WHITE);
		
		g.drawString("Quit", 25, 560);
	}

}
