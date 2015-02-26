package game.level;

import game.Vector2d;
import game.entity.Player;
import game.level.block.Block;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Level {
	
	public Vector2d startPosition;
	
	private int width, height;
	
	public BufferedImage tilesImage;
	private BufferedImage overlayImage = null;
	public int[] overlayPixels;
	private Block[] blocks;
	private Rectangle[] bounds;
	private int id;
	
	public Player player;
	
	public static Level[] levels  = new Level[100];
	
	static {
		levels[0] = new Level("/levels/level0.png", "/levels/overlay0.png", 0, new Vector2d(64*2, 64*5+32));
		levels[1] = new Level("/levels/level1.png", "/levels/overlay1.png", 1, new Vector2d(64, 64*6+32));
		levels[2] = new Level("/levels/level2.png", "/levels/overlay2.png", 2, new Vector2d(64, 64*6+32));
		levels[3] = new Level("/levels/level3.png", 3, new Vector2d(64, 64*6+32));
		levels[4] = new Level("/levels/level4.png", 4, new Vector2d(64, 64+32));
		levels[5] = new Level("/levels/level5.png", 5, new Vector2d(64*2, 64*6+32));
		levels[6] = new Level("/levels/level6.png", 6, new Vector2d(62*2, 64*5+32));
		levels[7] = new Level("/levels/level7.png", 7, new Vector2d(64*1, 64*4+32));
		levels[8] = new Level("/levels/level8.png", 8, new Vector2d(64*1.5, 64*2.5));
		levels[9] = new Level("/levels/level9.png", 9, new Vector2d(64*14.5, 64*7.5));
		levels[10] = new Level("/levels/level10.png", 10, new Vector2d(64*1, 64*3+32));
        levels[11] = new Level("/levels/level11.png", 11, new Vector2d(64*13 + 32, 64*5 + 32));
		levels[12] = new Level("/levels/level12.png", 12, new Vector2d(64*2, 64*7+32));
		levels[13] = new Level("/levels/level13.png", 13, new Vector2d(64*2, 64*5+32));
		levels[14] = new Level("/levels/level14.png", 14, new Vector2d(64*1.5, 64*5.5));
		levels[15] = new Level("/levels/level15.png", 15, new Vector2d(64*2, 64*6+32));
		levels[16] = new Level("/levels/level16.png", 16, new Vector2d(64*1, 64*1+32));
		levels[17] = new Level("/levels/level17.png", 17, new Vector2d(64*2, 64*4+32));
		levels[18] = new Level("/levels/level18.png", 18, new Vector2d(64*1.5, 64*5.5));
	}
	
	private static Level currentLevel = levels[14];
	private static int currentLevelId = 0;
	
	public Level(String levelPath, int id, Vector2d startPosition){
		loadLevel(levelPath, id, startPosition);
	}
	
	public Level(String levelPath, String overlayPath, int id, Vector2d startPosition){
		loadOverlayImage(overlayPath);
		loadLevel(levelPath, id, startPosition);
	}
	
	private void loadLevel(String levelPath, int id, Vector2d startPosition){
		this.id = id;
		loadLevelImage(levelPath);
		width = tilesImage.getWidth();
		height = tilesImage.getHeight();
		this.startPosition = startPosition;
		blocks = new Block[width*height];
		bounds = new Rectangle[width * height];
		
		for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = (tilesImage.getRGB(x, y) >> 16 & 0xFF);
                int g = ((tilesImage.getRGB(x, y) >> 8) & 0xFF);
                int b = ((tilesImage.getRGB(x, y) >> 0) & 0xFF);
                String color = String.format("#%02x%02x%02x", r, g, b);
                bounds[x + y * width] = new Rectangle(x * Block.SIZE, y * Block.SIZE, Block.SIZE, Block.SIZE);
                if (color.equals("#000000")) {
                    blocks[x + y * width] = Block.blockGeneric;
                } else if (color.equals("#ff0000")) {
                    blocks[x + y * width] = Block.blockKill;
                } else if (color.equals("#00ff00")) {
                    blocks[x + y * width] = Block.blockGreenGoal;
                } else if (color.equals("#fafa90")) {
                    blocks[x + y * width] = Block.blockDisableTransform;
                } else if (color.equals("#ccff88")) {
                    blocks[x + y * width] = Block.blockEnableTransform;
                } else {
                    blocks[x + y * width] = Block.blockVoid;
                }
            }
        }
		
		levels[id] = this;
		player = new Player(startPosition, 32, 32, Color.BLACK);
	}
	
	private void loadLevelImage(String path){
		try {
			tilesImage = ImageIO.read(getClass().getResource(path));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadOverlayImage(String path) {
		try {
			overlayImage = ImageIO.read(getClass().getResource(path));
		} catch(IOException e) {
			e.printStackTrace();
		}
		overlayPixels = new int[overlayImage.getWidth() * overlayImage.getHeight()];
		for(int y = 0; y < overlayImage.getHeight(); y++){
			for(int x = 0; x < overlayImage.getWidth(); x++){
				overlayPixels[x + y * overlayImage.getWidth()] = overlayImage.getRGB(x, y);
			}
		}
	}
	
	public BufferedImage getOverlayImage() {
		return overlayImage;
	}
	
	public Block getBlock(int x, int y){
		if(x + y * width < 0 || x + y * width >= blocks.length) return Block.blockVoid;
		return blocks[x+y*width];
	}
	
	public Block getBlock(int n){
		if(n < 0 || n >= blocks.length) return Block.blockVoid;
		return blocks[n];
	}

	public Block[] getBlocks(){
		return blocks;
	}
	
	public Block getBlockAtPos(int x, int y){
		return getBlock((int) Math.floor(x), (int) Math.floor(y));
	}
	
	public Rectangle getBounds(int x, int y) {
		return bounds[x + y * width];
	}
	
	public int getWidthBlocks(){
		return width;
	}
	
	public int getHeightBlocks(){
		return height;
	}
	
	public static Level getCurrentLevel() {
		return currentLevel;
	}

	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player){
		this.player = player;
	}
	
	public static void advanceLevel(){
		currentLevel = levels[++currentLevelId];
	}
	
	public int getId(){
		return id;
	}
	
}
