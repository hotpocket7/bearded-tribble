package game;

import game.entity.Player;
import game.level.Level;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller implements KeyListener {
	
	public boolean[] keyDown;
	public boolean leftDown, rightDown, jumpDown;
	public boolean transformFlatDown, transformTallDown, transformNormalDown;
	int right;
	
	public Controller() {
		keyDown  = new boolean[65535];
	}
	
	public void update(){
		// We update movement keys every frame because speed needs to update when the player transforms
		leftDown = keyDown[KeyEvent.VK_LEFT] || keyDown[KeyEvent.VK_A];
		rightDown = keyDown[KeyEvent.VK_RIGHT] || keyDown[KeyEvent.VK_D];
		jumpDown = keyDown[KeyEvent.VK_UP] || keyDown[KeyEvent.VK_W];
	}
	
	public void keyPressed(KeyEvent e) {
		keyDown[e.getKeyCode()] = true;
		// Transform controls handled differently to avoid problems with multiple keys being pressed at once
		if(Level.getCurrentLevel().player.getState() != Player.State.DYING){
			Level.getCurrentLevel().player.handleTransformInput(e);
		}
	}

	public void keyReleased(KeyEvent e) {
		keyDown[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e) {
		
	}

}
