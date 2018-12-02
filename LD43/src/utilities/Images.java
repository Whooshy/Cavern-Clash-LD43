package utilities;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Images {
	
	public static BufferedImage overlayColors;
	public static BufferedImage tileset_1;
	public static BufferedImage projectiles;
	public static BufferedImage enemy;
	public static BufferedImage logo;
	public static BufferedImage helpScreen;
	public static BufferedImage sawset;
	
	public static BufferedImage[] tiles = new BufferedImage[16];
	public static BufferedImage[] saws = new BufferedImage[2];
	public static BufferedImage[] enemySprites = new BufferedImage[4];

	public Images()
	{
		try {
			overlayColors = ImageIO.read(getClass().getResource("/hud/overlayColors.png"));
			tileset_1 = ImageIO.read(getClass().getResource("/textures/tileset_1.png"));
			projectiles = ImageIO.read(getClass().getResource("/textures/projectiles.png"));
			enemy = ImageIO.read(getClass().getResource("/textures/enemy.png"));
			logo = ImageIO.read(getClass().getResource("/logo.png"));
			helpScreen = ImageIO.read(getClass().getResource("/help.png"));
			sawset = ImageIO.read(getClass().getResource("/textures/saw.png"));
			
			for(int i = 0; i < 16; i++)
			{
				int y = i / 4;
				int x = i - (y * 4);
				
				tiles[i] = getTileSprite(tileset_1, x, y);
			}
			
			for(int i = 0; i < 4; i++)
			{
				enemySprites[i] = getTileSprite(enemy, i, 0);
			}
			for(int i = 0; i < 2; i++)
			{
				saws[i] = getTileSprite(sawset, i, 0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getTileSprite(BufferedImage img, int x, int y)
	{
		return img.getSubimage(x * 32, y * 32, 32, 32);
	}
	
}
