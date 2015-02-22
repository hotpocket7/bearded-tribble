package game.entity;

import game.Vector2i;

import java.awt.Color;
import java.awt.Rectangle;

public class Entity {
	
	public int width, height;
	public Vector2i position, velocity, acceleration;
	public Color color;
	public Rectangle bounds;
	protected boolean onGround;
	
	public Entity(Vector2i position, int width, int height, Color color){
		this.position = new Vector2i(position.x, position.y);
		this.width = width;
		this.height = height;
		this.color = color;
		velocity = new Vector2i(0, 0);
		acceleration = new Vector2i(0, 0);
		bounds = new Rectangle(position.x, position.y, width, height);
	}
	
	public void update(){
		position.x += velocity.x;
		bounds.x = position.x;
		position.y += velocity.y;
		bounds.y = position.y;
		bounds.setSize(width, height);
	}
}
