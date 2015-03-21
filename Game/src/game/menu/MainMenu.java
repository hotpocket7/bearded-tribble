package game.menu;

import game.Game;
import game.graphics.Screen;
import game.sound.Sound;
import game.graphics.Font;

import java.awt.*;
import java.awt.event.WindowEvent;

public class MainMenu extends Menu {

    String title = "I Wanna Be a Sonic Game";
    String[] options = {"New Game", "Continue", "Exit"};
    int selection = 0;
    boolean moved = false;
    Font font = new Font();

    static {
        selectSound = new Sound("/sound/select.wav");
    }

    public static Sound selectSound;

    public void update() {
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
                case 0:
                    Game.setGameState(Game.GameState.GAME);
                    break;
                case 1:
                    break;
                case 2:
                    Game.game.frame.dispatchEvent(new WindowEvent(Game.game.frame, WindowEvent.WINDOW_CLOSING));
            }
        }
    }

    public void render(Screen screen) {
        for (int i = 0; i < options.length; i++) {
            font.render(Game.WIDTH / 2 - 18*6, 300 + 50 * i, selection == i ? "-" + options[i] + "-" : options[i],
                    screen);
        }
    }
}
