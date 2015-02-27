package game.menu;

import game.graphics.Screen;

public class Menu {

    public static Menu main = new MainMenu();

    public static Menu currentMenu = main;

    public Menu(){}
    public void render(Screen screen) {}
    public void update() {}

}
