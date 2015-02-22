package game.level.block;


import java.awt.Color;

public abstract class Block {
	
	public static final int SIZE = 64;
	
	private boolean isSolid;
	private Color color;
	private int id;
	
	public static Block[] blocks = new Block[127];
	public static Block blockVoid = new BlockBackground(Color.WHITE, 0);
	public static Block blockGeneric = new BlockGeneric(Color.BLACK, 1);
	public static Block blockKill = new BlockKill(Color.RED, 2);
	public static Block blockGreenGoal = new BlockWin(Color.GREEN, 3);
	public static Block blockDisableTransform = new BlockTransform(new Color(0xfafa90), 4, false, null);
	public static Block blockEnableTransform = new BlockTransform(new Color(0xccff88), 5, true, null);
		
	public Block(Color color, boolean isSolid, int id){
		this.color = color;
		this.isSolid = isSolid;
		this.id = id;
		blocks[id] = this;
	}	
	
	public Color getColor(){
		return color;
	}
	
	public int getId(){
		return id;
	}
	
	public boolean isSolid() {
		return isSolid;
	}
	
	public abstract void collide();
	
}
