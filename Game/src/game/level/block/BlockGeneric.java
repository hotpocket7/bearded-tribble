package game.level.block;


import java.awt.Color;

public class BlockGeneric extends Block {
	public BlockGeneric(Color color, int id){
		super(color, true, id);
	}

	public void collide() {}
}
