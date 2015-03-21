package game.graphics;

public class Sprite {

    private final int SIZE;
    public int width, height;
    private int x, y;
    public int[] pixels;
    private SpriteSheet sheet;

    public Sprite(int size, int x, int y, SpriteSheet sheet) {
        SIZE = size;
        pixels = new int[SIZE * SIZE];
        this.x = x * size;
        this.y = y * size;
        this.sheet = sheet;
        load();
    }

    public Sprite(int[] pixels, int width, int height) {
        SIZE = (width == height) ? width : -1;
        this.width = width;
        this.height = height;
        this.pixels = new int[pixels.length];
        for (int q = 0; q < pixels.length; q++) {
            this.pixels[q] = pixels[q];
        }
    }

    public static Sprite[] split(SpriteSheet sheet) {
        int amount = (sheet.getWidth() * sheet.getHeight()) / (sheet.SPRITE_WIDTH * sheet.SPRITE_HEIGHT);
        Sprite[] sprites = new Sprite[amount];
        int current = 0;
        int[] pixels = new int[sheet.SPRITE_WIDTH * sheet.SPRITE_HEIGHT];
        for(int yPos = 0; yPos < sheet.getHeight() / sheet.SPRITE_HEIGHT; yPos++) {
            for(int xPos = 0; xPos < sheet.getWidth() / sheet.SPRITE_WIDTH; xPos++) {

                for(int y = 0; y < sheet.SPRITE_HEIGHT; y++) {
                    for(int x = 0; x < sheet.SPRITE_WIDTH; x++) {
                        int xo = x + xPos * sheet.SPRITE_WIDTH;
                        int yo = y + yPos * sheet.SPRITE_HEIGHT;
                        pixels[x+y*sheet.SPRITE_WIDTH] = sheet.pixels[xo + yo * sheet.getWidth()];
                    }
                }

                sprites[current++] = new Sprite(pixels, sheet.SPRITE_WIDTH, sheet.SPRITE_HEIGHT);
            }
        }

        return sprites;
    }

    private void load() {
        for(int y = 0; y < SIZE; y++) {
            for(int x = 0; x < SIZE; x++) {
                pixels[x+y*SIZE] = sheet.pixels[(this.x + x) + (this.y + y) * sheet.SIZE];
            }
        }
    }
}
