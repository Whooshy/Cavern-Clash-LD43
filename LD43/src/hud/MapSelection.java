package hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class MapSelection {
	
	public static int mapHighlight = 0;
	
	public MapSelection()
	{
		
	}

	public void update(Graphics2D g)
	{
		g.setFont(new Font("Tahoma", Font.BOLD, 30));
		g.setColor(Color.WHITE);
		
		g.drawString("Select a map to play", 25, 55);
		
		g.setFont(new Font("Tahoma", Font.PLAIN, 25));
		
		if(mapHighlight == 1) g.setColor(Color.YELLOW);
		if(mapHighlight != 1) g.setColor(Color.WHITE);
		
		g.drawString("Slither [Easy]", 25, 225);
		
		if(mapHighlight == 2) g.setColor(Color.YELLOW);
		if(mapHighlight != 2) g.setColor(Color.WHITE);
		
		g.drawString("Loop [Medium]", 25, 255);
		
		if(mapHighlight == 3) g.setColor(Color.YELLOW);
		if(mapHighlight != 3) g.setColor(Color.WHITE);
		
		g.drawString("Bracket [Medium]", 25, 285);
		
		if(mapHighlight == 4) g.setColor(Color.YELLOW);
		if(mapHighlight != 4) g.setColor(Color.WHITE);
		
		g.drawString("Pit [Hard]", 25, 315);
	}
}
