package game.level.block;

import game.entity.Player;
import game.level.Level;

import java.awt.Color;

public class BlockWin extends Block {
	public BlockWin(Color color, int id) {
		super(color, true, id);
	}
	
	public void collide() {
		Level.advanceLevel();
		Player.winSound.play();
	}
}
