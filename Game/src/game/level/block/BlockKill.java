package game.level.block;

import game.entity.Player;
import game.level.Level;

import java.awt.Color;

public class BlockKill extends Block {
	public BlockKill(Color color, int id) {
		super(color, true, id);
	}

	public void collide() {
		Level.getCurrentLevel().getPlayer().setState(Player.State.DYING);
	}
}
