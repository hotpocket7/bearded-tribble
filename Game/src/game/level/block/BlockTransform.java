package game.level.block;

import game.entity.Player;
import game.level.Level;

import java.awt.Color;

public class BlockTransform extends Block {

	private boolean playerCanTransform;
	private Player.Form form;
	
	public BlockTransform(Color color, int id, boolean playerCanTransform, Player.Form form){
		super(color, false, id);
		this.playerCanTransform = playerCanTransform;
	}
	
	public void collide() {
		if(form != null)
			Level.getCurrentLevel().player.transform(form);
		Level.getCurrentLevel().player.canTransform = playerCanTransform;
	}

}
