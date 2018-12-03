package engine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import hud.HUD;
import hud.MainMenu;
import hud.MapSelection;
import utilities.Images;
import world.World;

/*
 * WRITTEN BY TREVOR STRICKLAND
 * DEC 2, 2018
 * 
 * THIS CODE IS FREE TO USE AND MODIFY
 */

public class Engine extends Canvas implements Runnable, MouseListener, MouseMotionListener, KeyListener
{
	private Thread thread;
	public JFrame frame;
	
	public boolean isRunning = false;
	public boolean shouldUpdate = false;
	
	public static boolean refreshing = false;
	
	public int frameCount = 0;
	
	public static double FPS_LOCK = 60;
	
	public Images images;
	public World world;
	public HUD hud;
	public MainMenu menu;
	public MapSelection select;
	
	public AudioInputStream click;
	public Clip clickClip;
	
	public static int STATE = 0;
	public static int RESOLUTION = 0;
		
	public static int mouseX, mouseY;
	
	public Engine()
	{
		String[] resOptions = {"800x600", "1600x1200"};
		RESOLUTION = JOptionPane.showOptionDialog(null, "Which resolution?", "Launcher", 
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, resOptions, resOptions[0]);
		
		frame = new JFrame("Cavern Clash");
		
		if(RESOLUTION == 0) this.setPreferredSize(new Dimension(800, 600));
		if(RESOLUTION == 1) this.setPreferredSize(new Dimension(1600, 1200));
		
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		
		frame.add(this, BorderLayout.CENTER);
		
		images = new Images();
		menu = new MainMenu();
		select = new MapSelection();
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		
		try {
			click = AudioSystem.getAudioInputStream(getClass().getResource("/audio/click.wav"));
			clickClip = AudioSystem.getClip();
			clickClip.open(click);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		start();
	}
	
	public synchronized void start()
	{
		thread = new Thread(this);
		thread.start();
		
		isRunning = true;
	}
	
	public synchronized void stop()
	{
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		double startTime = (double) (System.nanoTime() / 1000000000.0);
		double currentTime = 0;
		double elapsedTime = 0;
		double chunkTime = 0;
		
		long timer = System.currentTimeMillis();
		
		while(isRunning)
		{
			shouldUpdate = false;
			
			currentTime = System.nanoTime() / 1000000000.0;
			//ELAPSED TIME: Time, in nanoseconds, between frames
			elapsedTime = currentTime - startTime;
			startTime = currentTime;
			
			chunkTime += elapsedTime;
									
			while(chunkTime >= (1.0 / FPS_LOCK))
			{
				chunkTime -= (1.0 / FPS_LOCK);
				shouldUpdate = true;
			}
			
			if(shouldUpdate)
			{
				frameCount++;
				
				update();
				
				if(System.currentTimeMillis() - timer >= 1000)
				{
					System.out.println("FPS: " + frameCount);
					frameCount = 0;
					timer += 1000;
				}
			}
			else
			{
				try {
					thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		stop();
	}
	
	public static void main(String[] args)
	{
		new Engine();
	}
	
	public void update()
	{
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null)
		{
			this.createBufferStrategy(3);
			return;
		}
		Graphics graphics = bs.getDrawGraphics();
		Graphics2D g = (Graphics2D) graphics;
		
		g.scale(RESOLUTION + 1, RESOLUTION + 1);
		
		g.setColor(new Color(15, 10, 30));
		g.fillRect(0, 0, 800, 600);
		
		if(refreshing)
		{
			world.musicClip.stop();
			world.musicClip.flush();
			world.musicClip.close();
			
			refreshing = false;
		}
		
		if(STATE == 0)
		{
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 800, 600);
			
			menu.update(g);
			
			g.setColor(Color.WHITE);
			g.setFont(new Font("Tahoma", Font.PLAIN, 20));
			g.drawString("By Trevor Strickand", 25, 100);
		}
		
		if(STATE == 1)
		{
			world.update(g);
			hud.update(g);
		}
		
		if(STATE == 2)
		{
			g.drawImage(Images.helpScreen, 0, 0, 800, 600, null);
		}
		
		if(STATE == 3)
		{
			select.update(g);
		}
		
		graphics.dispose();
		bs.show();
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) 
	{
		int x = e.getX();
		int y = e.getY();
		
		if(STATE == 1)
		{
			if(SwingUtilities.isLeftMouseButton(e) && x >= 720 * (RESOLUTION + 1) && y >= 560 * (RESOLUTION + 1) && x <= 780 * (RESOLUTION + 1) && y <= 590 * (RESOLUTION + 1))
			{
				hud.currentHud = 1;
				clickClip.setFramePosition(0);
				clickClip.start();
			}
			
			if(SwingUtilities.isLeftMouseButton(e) && x >= 570 * (RESOLUTION + 1) && y >= 560 * (RESOLUTION + 1) && x <= 690 * (RESOLUTION + 1) && y <= 590 * (RESOLUTION + 1))
			{
				hud.currentHud = 2;
				clickClip.setFramePosition(0);
				clickClip.start();
			}
			
			if(SwingUtilities.isLeftMouseButton(e) && x >= 420 * (RESOLUTION + 1) && y >= 560 * (RESOLUTION + 1) && x <= 570 * (RESOLUTION + 1) && y <= 590 * (RESOLUTION + 1))
			{
				hud.currentHud = 3;
				clickClip.setFramePosition(0);
				clickClip.start();
			}
			
			if(SwingUtilities.isLeftMouseButton(e) && hud.canSkipWave && x >= 270 * (RESOLUTION + 1) && y >= 560 * (RESOLUTION + 1) && x <= 420 * (RESOLUTION + 1) && y <= 590 * (RESOLUTION + 1))
			{
				hud.time = 0;
				clickClip.setFramePosition(0);
				clickClip.start();
			}
			
			if(SwingUtilities.isLeftMouseButton(e) && hud.currentHud == 1 && hud.sacrifices >= 3 && x >= 680 * (RESOLUTION + 1) && y >= 500 * (RESOLUTION + 1) && x <= 780 * (RESOLUTION + 1) && y <= 540 * (RESOLUTION + 1))
			{
				hud.currentShopSelection = 1;
				world.placeTowerCooldown = 10;
				clickClip.setFramePosition(0);
				clickClip.start();
			}
			
			if(SwingUtilities.isLeftMouseButton(e) && hud.currentHud == 1 && hud.sacrifices >= 5 && x >= 680 * (RESOLUTION + 1) && y >= 450 * (RESOLUTION + 1) && x <= 780 * (RESOLUTION + 1) && y <= 490 * (RESOLUTION + 1))
			{
				hud.currentShopSelection = 2;
				world.placeTowerCooldown = 10;
				clickClip.setFramePosition(0);
				clickClip.start();
			}
			
			if(SwingUtilities.isLeftMouseButton(e) && hud.currentHud == 1 && hud.sacrifices >= 8 && x >= 680 * (RESOLUTION + 1) && y >= 400 * (RESOLUTION + 1) && x <= 780 * (RESOLUTION + 1) && y <= 440 * (RESOLUTION + 1))
			{
				hud.currentShopSelection = 3;
				world.placeTowerCooldown = 10;
				clickClip.setFramePosition(0);
				clickClip.start();
			}
			
			if(SwingUtilities.isLeftMouseButton(e) && hud.currentHud == 1 && hud.sacrifices >= 15 && x >= 680 * (RESOLUTION + 1) && y >= 350 * (RESOLUTION + 1) && x <= 780 * (RESOLUTION + 1) && y <= 390 * (RESOLUTION + 1))
			{
				hud.currentShopSelection = 4;
				world.placeTowerCooldown = 10;
				clickClip.setFramePosition(0);
				clickClip.start();
			}
			
			if(SwingUtilities.isRightMouseButton(e) && hud.currentHud == 4)
			{
				hud.currentHud = 0;
				hud.currentShopSelection = 0;
			}
			
			if(SwingUtilities.isRightMouseButton(e) && hud.currentHud != 4)
			{
				hud.currentHud = 0;
			}
		}
		
		if(SwingUtilities.isLeftMouseButton(e) && STATE == 0 && menu.menuHightlight == 1)
		{
			clickClip.setFramePosition(0);
			clickClip.start();
			
			STATE = 3;
		}
		
		if(SwingUtilities.isLeftMouseButton(e) && STATE == 0 && menu.menuHightlight == 2)
		{
			clickClip.setFramePosition(0);
			clickClip.start();
			
			STATE = 2;
		}
		
		if(SwingUtilities.isLeftMouseButton(e) && STATE == 0 && menu.menuHightlight == 3)
		{
			clickClip.setFramePosition(0);
			clickClip.start();
			
			System.exit(0);
		}
		
		if(SwingUtilities.isLeftMouseButton(e) && STATE == 3 && select.mapHighlight == 1)
		{
			clickClip.setFramePosition(0);
			clickClip.start();
			
			world = new World();
			hud = new HUD();
			world.loadLevel("slither");
			
			STATE = 1;
		}
		
		if(SwingUtilities.isLeftMouseButton(e) && STATE == 3 && select.mapHighlight == 2)
		{
			clickClip.setFramePosition(0);
			clickClip.start();
			
			hud = new HUD();
			world = new World();
			world.loadLevel("loop");
			
			STATE = 1;
		}
		
		if(SwingUtilities.isLeftMouseButton(e) && STATE == 3 && select.mapHighlight == 3)
		{
			clickClip.setFramePosition(0);
			clickClip.start();
			
			hud = new HUD();
			world = new World();
			world.loadLevel("bracket");
			
			STATE = 1;
		}
		
		if(SwingUtilities.isLeftMouseButton(e) && STATE == 3 && select.mapHighlight == 4)
		{
			clickClip.setFramePosition(0);
			clickClip.start();
			
			hud = new HUD();
			world = new World();
			world.loadLevel("pit");
			
			STATE = 1;
		}
		
		mouseX = x * (RESOLUTION + 1);
		mouseY = y * (RESOLUTION + 1);
	}

	public void mouseReleased(MouseEvent e) 
	{
		mouseX = -5;
		mouseY = -5;
	}

	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) 
	{
		int x = e.getX();
		int y = e.getY();
		
		if(STATE == 0)
		{
			if(x > 25 * (RESOLUTION + 1) && x < 125 * (RESOLUTION + 1) && y > 470 * (RESOLUTION + 1) && y < 500 * (RESOLUTION + 1))
			{
				menu.menuHightlight = 1;
			}
			else if(x > 25 * (RESOLUTION + 1) && x < 125 * (RESOLUTION + 1) && y > 500 * (RESOLUTION + 1) && y < 530 * (RESOLUTION + 1))
			{
				menu.menuHightlight = 2;
			}
			else if(x > 25 * (RESOLUTION + 1) && x < 125 * (RESOLUTION + 1) && y > 530 * (RESOLUTION + 1) && y < 560 * (RESOLUTION + 1))
			{
				menu.menuHightlight = 3;
			}
			else
			{
				menu.menuHightlight = 0;
			}
		}
		
		if(STATE == 3)
		{
			if(x > 25 * (RESOLUTION + 1) && x < 200 * (RESOLUTION + 1) && y > 200 * (RESOLUTION + 1) && y < 225 * (RESOLUTION + 1))
			{
				select.mapHighlight = 1;
			}
			else if(x > 25 * (RESOLUTION + 1) && x < 200 * (RESOLUTION + 1) && y > 230 * (RESOLUTION + 1) && y < 255 * (RESOLUTION + 1))
			{
				select.mapHighlight = 2;
			}
			else if(x > 25 * (RESOLUTION + 1) && x < 200 * (RESOLUTION + 1) && y > 260 * (RESOLUTION + 1) && y < 285 * (RESOLUTION + 1))
			{
				select.mapHighlight = 3;
			}
			else if(x > 25 * (RESOLUTION + 1) && x < 200 * (RESOLUTION + 1) && y > 290 * (RESOLUTION + 1) && y < 315 * (RESOLUTION + 1))
			{
				select.mapHighlight = 4;
			}
			else
			{
				select.mapHighlight = 0;
			}
		}
	}

	public void keyPressed(KeyEvent e) 
	{
		int k = e.getKeyCode();
		if(k == e.VK_ESCAPE && (STATE == 2 || STATE == 3))
		{
			STATE = 0;
		}
		if(k == e.VK_ESCAPE && STATE == 1)
		{
			select.mapHighlight = 0;
			Engine.refreshing = true;
			STATE = 0;
		}
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}