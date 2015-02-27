package game.menu;

import game.Game;
import game.graphics.Screen;
import game.sound.Sound;

import java.awt.*;
import java.awt.event.WindowEvent;

public class MainMenu extends Menu {

    String title = "I Wanna Be a Sonic Game";
    String[] options = {"New Game", "Continue", "Exit"};
    int selection = 0;
    boolean moved = false;

    static {
        selectSound = new Sound("/sound/select.wav");
    }

    public static Sound selectSound;

    public void update() {
        System.out.println(selection);
        if(Game.controller.downDown && selection < 2) {
            if(!moved) {
                selection++;
                selectSound.play();
            }
            moved = true;
        } else if(Game.controller.jumpDown && selection > 0) {
            if(!moved){
                selection--;
                selectSound.play();
            }
            moved = true;
        } else {
            moved = false;
        }
        if(Game.controller.selectDown) {
            switch(selection){
                case 1:
                    Game.setGameState(Game.GameState.GAME);
                    break;
                case 2:
                    Game.game.frame.dispatchEvent(new WindowEvent(Game.game.frame, WindowEvent.WINDOW_CLOSING));
            }
        }
    }

    public void render(Screen screen) {
        screen.clearGraphics();
        for (int i = 0; i < options.length; i++) {
            screen.renderText(selection == i ? "> " + options[i] : options[i], Game.HEIGHT / 2 + 35*4, 300 + 50*i, 35,
                    Color.BLACK);
        }
        screen.renderText(title, 25, 100, 70, Color.BLACK);
    }
}
