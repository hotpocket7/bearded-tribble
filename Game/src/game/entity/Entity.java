package game.entity;

import game.Vector2d;

import java.awt.Color;
import java.awt.Rectangle;

public class Entity {
	
	public int width, height;
	public Vector2d position, velocity, acceleration;
	public Color color;
	public Rectangle bounds;
	protected boolean onGround;
	
	public Entity(Vector2d position, int width, int height, Color color){
		this.position = new Vector2d(position.x, position.y);
		this.width = width;
		this.height = height;
		this.color = color;
		velocity = new Vector2d(0, 0);
		acceleration = new Vector2d(0, 0);
		bounds = new Rectangle((int) position.x, (int) position.y, width, height);
	}
	
	public void update(){
		position.x += velocity.x;
		bounds.x = (int) position.x;
		position.y += velocity.y;
		bounds.y = (int) position.y;
		bounds.setSize(width, height);
	}
}
