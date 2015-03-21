package game.graphics;

public class Font {

    private static SpriteSheet font  = new SpriteSheet("/fonts/fontsheet.png", 28, 36);
    private static Sprite[] characters = Sprite.split(font);

    private static String charIndex = "ABCDEFGHIJKLM" +
                                       "NOPQRSTUVWXYZ" +
                                       "abcdefghijklm" +
                                       "nopqrstuvwxyz" +
                                       "0123456789.,!" +
                                       "?'@#$%&()/-+=";

    public Font() {

    }

    public void render(int x, int y, String text, Screen screen) {
        for(int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            int index = charIndex.indexOf(currentChar);
            if(index == -1) {
                x += 8;
                continue;
            }
            screen.renderSprite(characters[index], x + 18*i, y);
        }
    }
}
