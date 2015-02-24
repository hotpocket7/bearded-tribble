package game;

import game.entity.Player;
import game.graphics.Screen;
import game.level.Level;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;


public class Game extends Canvas implements Runnable {

	public static enum GameState {
		MENU, GAME
	}
	
	private static final long serialVersionUID = 1L;
	JFrame frame = new JFrame();
	public static String title = "yo";
	public static final int WIDTH = 1024;
	public static final int HEIGHT = WIDTH / 16 * 9;

	private boolean isRunning;
	private Thread thread;
	public static int updates;
	private int frames;

	private BufferedImage img;
	private int[] pixels;
	private Screen screen;
	
	public GameState gameState;
	public static Controller controller;
	
	public static int deaths;

	public Game() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);

		isRunning = false;
		updates = 0;
		frames = 0;

		screen = new Screen(WIDTH, HEIGHT);
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		
		controller = new Controller();
		deaths = 0;
		
		setGameState(GameState.GAME);
		
		addKeyListener(controller);
	}

	public void update() {
		title = String.format("Game | FPS: %d | Deaths: %d | Level %d", frames, deaths, Level.getCurrentLevel().getId() + 1);
		frame.setTitle(title);
		
		controller.update();
		Level.getCurrentLevel().player.update();
	}
	
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
		if(gameState == GameState.GAME){
			Level.getCurrentLevel().player = new Player(Level.getCurrentLevel().startPosition, 32, 32, Color.BLACK);
		}
	}

	public void render() {
        BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();

		screen.render();
		for(int i = 0; i < pixels.length; i++){
			pixels[i] = screen.pixels[i];
		}

		
		
		/*g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);*/
		g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);

		g.dispose();
		bs.show();
	}

	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		double ns = 1000000000d / 60;
		double delta = 0;
		int frames = 0;
		long now = System.nanoTime();
		while (isRunning) {
			now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				updates++;
				render();
				frames++;
  				delta--;
			}
			
			if(System.currentTimeMillis() - timer  > 1000){
				timer += 1000;
				this.frames = frames;
				frames = 0;
			}
		}
	}

	public synchronized void start() {
		isRunning = true;
		thread = new Thread(this, "Game");
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.frame.add(game);
		game.frame.setTitle(title);
		game.frame.setSize(game.getSize());
		game.frame.setResizable(false);
		game.frame.pack();
		game.frame.setVisible(true);
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);

		game.start();
	}

}
