package game.graphics;

import game.Game;
import game.entity.Player;
import game.level.Level;
import game.level.block.Block;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Screen {
	private int width, height;
	public int[] pixels;
	
	public Screen(int width, int height){
		this.width = width;
		this.height = height;
		pixels = new int[width*height];
	}
	
	public void render(){
		fillRect(0, 0, width, height, Color.WHITE);
		renderLevel(Level.getCurrentLevel());
		renderPlayer(Level.getCurrentLevel().player);
	}
	
	private void renderLevel(Level level){
		for(int y = 0; y < level.getHeightBlocks(); y++){
			for(int x = 0; x < level.getWidthBlocks(); x++){
				fillRect(x * Block.SIZE, y * Block.SIZE, Block.SIZE, Block.SIZE, level.getBlock(x, y).getColor());
			}
		}
		drawOverlayImage(Level.getCurrentLevel(), 0, 0);
	}
	
	private void renderPlayer(Player player) {
		fillRect((int) player.position.x, (int) player.position.y, player.width, player.height, player.color);
	}
	
	private void drawOverlayImage(Level level, int x, int y) {
		BufferedImage img = Level.getCurrentLevel().getOverlayImage();
		if(img == null) return;
		int[] pix = Level.getCurrentLevel().overlayPixels;
		for(int ya = 0; ya < img.getHeight(); ya++){
			for(int xa = 0; xa < img.getWidth(); xa++){
				if(pix[xa+ya*width] == Color.WHITE.getRGB()) continue;
				pixels[(x + xa) + (y + ya) * width] = pix[xa+ya*width];
			}
		}
	}
	
	private void fillRect(int x, int y, int width, int height, Color color){
		for(int yCount = y; yCount < y + height; yCount++){
			for(int xCount = x; xCount < x + width; xCount++){
				if(xCount < 0 || xCount > this.width || yCount < 0 || yCount > this.height) continue;
				pixels[xCount + yCount*this.width] = color.getRGB();
			}
		}
	}
}
