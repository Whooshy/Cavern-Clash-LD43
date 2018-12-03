package world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import engine.Engine;
import hud.HUD;
import objects.EnemyWalker;
import objects.HelixProjectile;
import objects.Projectile;
import objects.Saw;
import objects.Tile;

public class World 
{
	public static Tile[][] tiles = new Tile[26][20];
	
	public ArrayList<EnemyWalker> enemies = new ArrayList<EnemyWalker>();
	
	public static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	public static ArrayList<HelixProjectile> helixProjectiles = new ArrayList<HelixProjectile>();
	public static ArrayList<Saw> saws = new ArrayList<Saw>();
	
	public AudioInputStream music;
	public Clip musicClip;
	
	public AudioInputStream click;
	public Clip clickClip;
	
	int enemySpawnTime = 0;
	int enemiesSpawned = 0;
	int enemyHealth = 7;
	int waveQuota = 10;
	
	int deathCount = 0;
	int deathX = 0;
	int deathY = 0;
	
	int verturretCooldown = 50;
	int waveWiperCooldown = 70;
	int helixCoreCooldown = 100;
	
	public int placeTowerCooldown = 0;
	
	public static int hpCooldown = 0;
	
	public int enemySpawnX, enemySpawnY;
	
	public World()
	{
		projectiles.clear();
		helixProjectiles.clear();
		saws.clear();
		enemies.clear();
		
		for(int x = 0; x < 26; x++)
		for(int y = 0; y < 20; y++)
		{
			tiles[x][y] = new Tile((x * 32) - 16, (y * 32) - 20, 0);
			if(x == 0 || x == 25 || y == 0 || y == 19) tiles[x][y].id = 1;
		}
		
		try {
			music = AudioSystem.getAudioInputStream(getClass().getResource("/audio/music.wav"));
			musicClip = AudioSystem.getClip();
			musicClip.open(music);
			
			click = AudioSystem.getAudioInputStream(getClass().getResource("/audio/click.wav"));
			clickClip = AudioSystem.getClip();
			clickClip.open(click);
			
			musicClip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadLevel(String name)
	{
		try {
			BufferedImage level = ImageIO.read(getClass().getResource("/levels/" + name + ".png"));
			
			for(int x = 0; x < level.getWidth(); x++)
			for(int y = 0; y < level.getHeight(); y++)
			{
				if(level.getRGB(x, y) == Color.BLACK.getRGB())
				{
					tiles[x + 1][y + 1].id = 0;
				}
				else if(level.getRGB(x, y) == Color.WHITE.getRGB())
				{
					tiles[x + 1][y + 1].id = 1;
				}
				else if(level.getRGB(x, y) == Color.RED.getRGB())
				{
					tiles[x + 1][y + 1].id = 16;
					enemySpawnX = (x + 1) * 32;
					enemySpawnY = (y + 1) * 32;
				}
				else if(level.getRGB(x, y) == Color.BLUE.getRGB())
				{
					tiles[x + 1][y + 1].id = 15;
				}
				else if(level.getRGB(x, y) == Color.DARK_GRAY.getRGB())
				{
					tiles[x + 1][y + 1].id = 2;
				}
				else if(level.getRGB(x, y) == Color.GRAY.getRGB())
				{
					tiles[x + 1][y + 1].id = 3;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update(Graphics2D g)
	{
		verturretCooldown--;
		waveWiperCooldown--;
		helixCoreCooldown--;
		
		if(hpCooldown > 0)
		{
			hpCooldown--;
		}
		if(placeTowerCooldown > 0)
		{
			placeTowerCooldown--;
		}
		
		for(int x = 0; x < 26; x++)
		for(int y = 0; y < 20; y++)
		{
			tiles[x][y].update(g);
			
			//Verturrets
			
			if(tiles[x][y].id == 5 && verturretCooldown < 38)
			{
				tiles[x][y].id = 6;
			}
			
			if(tiles[x][y].id == 6 && verturretCooldown < 25)
			{
				tiles[x][y].id = 7;
			}
			
			if(tiles[x][y].id == 7 && verturretCooldown < 12)
			{
				tiles[x][y].id = 8;
			}
			
			if(tiles[x][y].id == 8 && tiles[x][y - 1].id == 1 && verturretCooldown <= 0)
			{
				projectiles.add(new Projectile(0, x * 32 - 5, y * 32 - 8, 0, 1, 5, 100));
				tiles[x][y].id = 5;
				System.out.println(projectiles.size());
			}
			if(tiles[x][y].id == 8 && tiles[x][y + 1].id == 1 && verturretCooldown <= 0)
			{
				projectiles.add(new Projectile(0, x * 32 - 5, y * 32 + 8, 0, -1, 5, 100));
				tiles[x][y].id = 5;
			}
			if(x == 25 && y == 19 && verturretCooldown <= 0)
			{
				verturretCooldown = 50;
			}
			
			//Wave Wiper
			
			if(tiles[x][y].id == 9 && waveWiperCooldown < 53)
			{
				tiles[x][y].id = 10;
			}
			
			if(tiles[x][y].id == 10 && waveWiperCooldown < 35)
			{
				tiles[x][y].id = 11;
			}
			
			if(tiles[x][y].id == 11 && waveWiperCooldown < 17)
			{
				tiles[x][y].id = 12;
			}
			
			if(tiles[x][y].id == 12 && waveWiperCooldown <= 0)
			{
				projectiles.add(new Projectile(1, x * 32 - 5, y * 32, 1, 0, 6, 100));
				projectiles.add(new Projectile(1, x * 32 - 5, y * 32, -1, 0, 6, 100));
				tiles[x][y].id = 9;
			}
			if(x == 25 && y == 19 && waveWiperCooldown <= 0)
			{
				waveWiperCooldown = 70;
			}
			
			//Helix Core
			
			if(tiles[x][y].id == 13 && helixCoreCooldown < 50)
			{
				tiles[x][y].id = 14;
			}
			
			if(tiles[x][y].id == 14 && helixCoreCooldown <= 0)
			{
				helixProjectiles.add(new HelixProjectile(x * 32 - 5, y * 32, 25, 30, 300));
				tiles[x][y].id = 13;
			}
			if(x == 25 && y == 19 && helixCoreCooldown <= 0)
			{
				helixCoreCooldown = 100;
			}
		}
		
		if(HUD.newWave)
		{
			waveQuota = 5 + (HUD.wave * 2);
			enemyHealth = 5 + (HUD.wave - 1);
			enemySpawnTime++;
			if(enemySpawnTime == 30)
			{
				enemies.add(new EnemyWalker(enemySpawnX, enemySpawnY, enemyHealth));
				enemySpawnTime = 0;
				enemiesSpawned += 1;
			}
			if(enemiesSpawned == waveQuota)
			{
				enemySpawnTime = 0;
				enemiesSpawned = 0;
				HUD.traps += 2;
				HUD.newWave = false;
			}
		}
		
		if(enemies.size() == 0 && !HUD.newWave)
		{
			HUD.canSkipWave = true;
		}
		else
		{
			HUD.canSkipWave = false;
		}
		
		for(int i = 0; i < enemies.size(); i++)
		{
			enemies.get(i).update(g);
		}
		
		for(int i = 0; i < enemies.size(); i++)
		{
			if(enemies.get(i).health <= 0)
				enemies.remove(i);
		}
		
		for(int i = 0; i < projectiles.size(); i++)
		{
			projectiles.get(i).update(g);
		}
		
		for(int i = 0; i < projectiles.size(); i++)
		{
			if(projectiles.get(i).getLifetime() <= 0)
				projectiles.remove(i);
		}
		
		for(int i = 0; i < helixProjectiles.size(); i++)
		{
			helixProjectiles.get(i).update(g);
		}
		
		for(int i = 0; i < saws.size(); i++)
		{
			saws.get(i).update(g);
		}
		
		for(int i = 0; i < helixProjectiles.size(); i++)
		{
			if(helixProjectiles.get(i).getLifetime() <= 0)
				helixProjectiles.remove(i);
		}
		
		if(Engine.mouseX > 0 && HUD.traps > 0 && HUD.currentHud == 3 && tiles[(Engine.mouseX + 16) / 32][(Engine.mouseY + 20) / 32].id == 0)
		{
			HUD.traps -= 1;
			clickClip.setFramePosition(0);
			clickClip.start();
			tiles[(Engine.mouseX + 16) / 32][(Engine.mouseY + 20) / 32].id = 4;
		}
		
		if(Engine.mouseX > 0 && HUD.currentHud == 2 && tiles[(Engine.mouseX + 16) / 32][(Engine.mouseY + 20) / 32].id >= 4)
		{
			if(tiles[(Engine.mouseX + 16) / 32][(Engine.mouseY + 20) / 32].id == 4) HUD.sacrifices += 1;
			if(tiles[(Engine.mouseX + 16) / 32][(Engine.mouseY + 20) / 32].id >= 5 && tiles[(Engine.mouseX + 16) / 32][(Engine.mouseY + 20) / 32].id <= 8) HUD.sacrifices += 3;
			if(tiles[(Engine.mouseX + 16) / 32][(Engine.mouseY + 20) / 32].id >= 9 && tiles[(Engine.mouseX + 16) / 32][(Engine.mouseY + 20) / 32].id <= 12) HUD.sacrifices += 5;
			if(tiles[(Engine.mouseX + 16) / 32][(Engine.mouseY + 20) / 32].id >= 13 && tiles[(Engine.mouseX + 16) / 32][(Engine.mouseY + 20) / 32].id <= 14) HUD.sacrifices += 8;
			
			tiles[(Engine.mouseX + 16) / 32][(Engine.mouseY + 20) / 32].id = 0;
			clickClip.setFramePosition(0);
			clickClip.start();
		}
		
		if(Engine.mouseX > 0 && placeTowerCooldown == 0 && HUD.currentHud == 4 && HUD.currentShopSelection == 1 && tiles[(Engine.mouseX + 16) / 32][(Engine.mouseY + 20) / 32].id == 0 && (tiles[(Engine.mouseX + 16) / 32][((Engine.mouseY + 20) / 32) + 1].id == 1 || tiles[(Engine.mouseX + 16) / 32][((Engine.mouseY + 20) / 32) - 1].id == 1))
		{
			HUD.sacrifices -= 3;
			tiles[(Engine.mouseX + 16) / 32][(Engine.mouseY + 20) / 32].id = 5;
			HUD.currentShopSelection = 0;
			HUD.currentHud = 1;
			clickClip.setFramePosition(0);
			clickClip.start();
		}
		
		if(Engine.mouseX > 0 && placeTowerCooldown == 0 && HUD.currentHud == 4 && HUD.currentShopSelection == 2 && tiles[(Engine.mouseX + 16) / 32][(Engine.mouseY + 20) / 32].id == 0 && (tiles[(Engine.mouseX + 16) / 32][((Engine.mouseY + 20) / 32) + 1].id == 1 || tiles[(Engine.mouseX + 16) / 32][((Engine.mouseY + 20) / 32) - 1].id == 1))
		{
			HUD.sacrifices -= 5;
			tiles[(Engine.mouseX + 16) / 32][(Engine.mouseY + 20) / 32].id = 9;
			HUD.currentShopSelection = 0;
			HUD.currentHud = 1;
			clickClip.setFramePosition(0);
			clickClip.start();
		}
		
		if(Engine.mouseX > 0 && placeTowerCooldown == 0 && HUD.currentHud == 4 && HUD.currentShopSelection == 3 && tiles[(Engine.mouseX + 16) / 32][(Engine.mouseY + 20) / 32].id == 0 && (tiles[(Engine.mouseX + 16) / 32][((Engine.mouseY + 20) / 32) + 1].id == 1 || tiles[(Engine.mouseX + 16) / 32][((Engine.mouseY + 20) / 32) - 1].id == 1))
		{
			HUD.sacrifices -= 8;
			tiles[(Engine.mouseX + 16) / 32][(Engine.mouseY + 20) / 32].id = 13;
			HUD.currentShopSelection = 0;
			HUD.currentHud = 1;
			clickClip.setFramePosition(0);
			clickClip.start();
		}
		
		if(Engine.mouseX > 0 && placeTowerCooldown == 0 && HUD.currentHud == 4 && HUD.currentShopSelection == 4 && tiles[(Engine.mouseX + 16) / 32][(Engine.mouseY + 20) / 32].id == 0)
		{
			HUD.sacrifices -= 15;
			saws.add(new Saw(Engine.mouseX - 16, Engine.mouseY - 16));
			HUD.currentShopSelection = 0;
			HUD.currentHud = 1;
			clickClip.setFramePosition(0);
			clickClip.start();
		}
		
		if(HUD.currentHud == 5)
		{
			die();
		}
	}
	
	public void die()
	{
		deathCount--;
		if(deathCount < 0)
		{
			deathCount = 0;
			tiles[deathX][deathY].id = 0;
			deathX += 1;
			if(deathX > 25)
			{
				deathX = 0;
				deathY += 1;
			}
			if(deathY > 19) deathY = 0;
		}
	}
}
