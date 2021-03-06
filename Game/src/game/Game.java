package game;

import game.entity.Player;
import game.graphics.Font;
import game.graphics.Screen;
import game.level.Level;
import game.menu.Menu;

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

    public static Game game;
	
	private static final long serialVersionUID = 1L;
	public JFrame frame = new JFrame();
	public static String title = "yo";
	public static final int WIDTH = 1024;
	public static final int HEIGHT = WIDTH / 16 * 9;

	private boolean isRunning;
	private Thread thread;
	public static int updates;
	private int frames;

	private BufferedImage img;
	public static int[] pixels;
	private Screen screen;

    public static Font font;

	private static GameState gameState;
	public static Controller controller;
	
	public static int deaths;

    double dt = 0;

	public Game() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);

		isRunning = false;
		updates = 0;
		frames = 0;

		screen = new Screen(WIDTH, HEIGHT);
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
        font = new Font();
		
		controller = new Controller();
		deaths = 0;
		
		setGameState(GameState.MENU);
		
		addKeyListener(controller);
	}

	public void update(double delta) {
		title = String.format("Game | FPS: %d | Deaths: %d | Level %d | Delta: %f", frames, deaths, Level
                .getCurrentLevel().getId() + 1, dt);
		frame.setTitle(title);
        controller.update();
		if(gameState == GameState.GAME) {
            Level.getCurrentLevel().player.update(delta);
            if(controller.escapeDown) setGameState(GameState.MENU);
        } else if(gameState == GameState.MENU) {
            Menu.currentMenu.update();
        }
	}
	
	public static void setGameState(GameState gs) {
		gameState = gs;
		if(gameState == GameState.GAME){
			Level.getCurrentLevel().player = new Player(Level.getCurrentLevel().startPosition, 32, 32, Color.BLACK);
		}
	}

    public static GameState getGameState() { return gameState; }

	public void render() {
        BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(1);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
        screen.clear();
        if(gameState == GameState.GAME)
		    screen.renderGame();
        else if(gameState == GameState.MENU) {
            Menu.main.render(screen);
        }

		for(int i = 0; i < pixels.length; i++){
			pixels[i] = screen.pixels[i];
		}
        g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
		g.dispose();
		bs.show();
	}

    public void run() {
        long lastLoopTime = System.nanoTime() + 1;
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        int lastFpsTime = 0;
        int fps = 0;
        while (isRunning) {
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double)OPTIMAL_TIME);

            // update the frame counter
            lastFpsTime += updateLength;
            fps++;

            // update our FPS counter if a second has passed since
            // we last recorded
            if (lastFpsTime >= 1000000000)
            {
                frames = fps;
                lastFpsTime = 0;
                fps = 0;
            }
            update(delta);
            dt = delta;
            render();
            try {
                long c = Math.abs((lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000000);
                Thread.sleep(c);
            } catch(InterruptedException e){
                e.printStackTrace();
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
		game = new Game();
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
