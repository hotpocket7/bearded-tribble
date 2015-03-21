package game.graphics;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpriteSheet {

    private String path;
    public final int SIZE, SPRITE_WIDTH, SPRITE_HEIGHT;
    private int width, height;
    public int[] pixels;

    public SpriteSheet(SpriteSheet sheet, int x, int y, int width, int height, int spriteSize) {
        int xx = x * spriteSize;
        int yy = y * spriteSize;
        int w = width * spriteSize;
        int h = height * spriteSize;
        if(w == h)
            SIZE = w;
        else
            SIZE = -1;
        SPRITE_WIDTH = w;
        SPRITE_HEIGHT = h;
        pixels = new int[w * h];
        for(int yCount = 0; yCount < w; yCount++){
            int yPos = yy + yCount;
            for(int xCount = 0; xCount < h; xCount++) {
                int xPos = xx + xCount;
                pixels[xPos + yPos * w] = sheet.pixels[xPos + yPos * sheet.SPRITE_WIDTH];
            }
        }
    }

    public SpriteSheet(String path, int size){
        this.path = path;
        SIZE = size;
        SPRITE_WIDTH = SIZE;
        SPRITE_HEIGHT = SIZE;
        load();
    }

    public SpriteSheet(String path, int width, int height){
        this.path = path;
        SIZE = -1;
        SPRITE_WIDTH = width;
        SPRITE_HEIGHT = height;
        pixels = new int[SPRITE_WIDTH * SPRITE_HEIGHT];
        load();
    }

    public void load(){
        try {
            BufferedImage image = ImageIO.read(SpriteSheet.class.getResource(path));
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public int getWidth(){ return width; }
    public int getHeight(){ return height; }
}
