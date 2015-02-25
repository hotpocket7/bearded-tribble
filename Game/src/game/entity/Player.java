package game.entity;

import game.Game;
import game.Vector2d;
import game.level.Level;
import game.level.block.Block;
import game.sound.Sound;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Player extends Entity {
	
	public enum Form{
		NORMAL, FLAT, TALL
	}
	
	public static enum State{
		GROUNDED, JUMPING, DOUBLEJUMPING, DYING
	}
	
	private enum CollisionDirection {
		HORIZONTAL, VERTICAL
	}
	
	static {
		singleJumpSound = new Sound("/sound/singlejump.wav");
		doubleJumpSound = new Sound("/sound/doublejump.wav");
		transformFastSound = new Sound("/sound/transformfast.wav");
		transformNormalSound = new Sound("/sound/transformnormal.wav");
		transformSlowSound = new Sound("/sound/transformslow.wav");
		deathSound = new Sound("/sound/death.wav");
		winSound = new Sound("/sound/win.wav");
	}
	
	private State state;
	
	private double speed;
	public double gravity;
	private int doubleJump;
	private double jumpVelocity;
	private double doubleJumpVelocity;
	public boolean canTransform;
	public boolean active;

	private Form form;
	
	public static Sound singleJumpSound, doubleJumpSound, transformFastSound, transformNormalSound, transformSlowSound, deathSound, winSound;
		
	public Player(Vector2d position, int width, int height, Color color){
		super(position, width, height, color);
		state = State.JUMPING;
		gravity = 1;
		speed = 5;
		doubleJump = -1;
		canTransform = true;
        active = true;
		transform(Form.NORMAL);
	}
	
	public void update(double delta) {
		if(!active) return;
		if(state == State.DYING) {
			// Player shrinks then respawns
			width -= 2;
			height -= 2;
			position.x += 1.0;
			position.y += 1.0;
			if(width <= 0 || height <= 0) {
				position.x = Level.getCurrentLevel().startPosition.x;
				position.y = Level.getCurrentLevel().startPosition.y;
				width = 32;
				height = 32;
				state = State.JUMPING;
				Game.deaths++;
				updateBounds();
			}
			return;
		}
		
		// Movement (walking, jumping, double jumping)
		if(Game.controller.leftDown && !Game.controller.rightDown ) {
			velocity.x = -speed;
		} else if(Game.controller.rightDown && !Game.controller.leftDown){
			velocity.x = speed;
		} else {
			velocity.x = 0;
		}
		if(state == State.GROUNDED) {
			velocity.y = 0;
			if(Game.controller.jumpDown && doubleJump == 0){
				velocity.y = jumpVelocity;
				state = State.JUMPING;
				doubleJump = 1;
				Player.singleJumpSound.play();
			}
			if(!Game.controller.jumpDown) {
				doubleJump = 0;
			}
		} else if(state == State.JUMPING) {
			if(!Game.controller.jumpDown){
				if(velocity.y < jumpVelocity/3)
					velocity.y = jumpVelocity/3;
				doubleJump = 2;
			}
			if(doubleJump == 2 && Game.controller.jumpDown){
				velocity.y = doubleJumpVelocity;
				state = State.DOUBLEJUMPING;
				Player.doubleJumpSound.play();
			}
			velocity.y += gravity * delta;
		} else if(state == State.DOUBLEJUMPING){
			if(!Game.controller.jumpDown){
				if(velocity.y < doubleJumpVelocity/3)
					velocity.y = doubleJumpVelocity/3;
			}
			doubleJump = 3;
			velocity.y += gravity * delta;
		}
		
		// Collision detection
		onGround = false;
		position.y += velocity.y * delta;
		bounds.y = (int) position.y;
		handleCollisions(CollisionDirection.VERTICAL);
		if(state == State.DYING) return;
		position.x += velocity.x * delta;
		bounds.x = (int) position.x;
		handleCollisions(CollisionDirection.HORIZONTAL);
		if(state == State.DYING) return;
		if(state == State.DYING) update();
		bounds.setSize(width, height);
		
		// Player falls when they walk off of a block
		if(onGround) 
			state = State.GROUNDED;
		else if(state == State.GROUNDED)
			state = State.JUMPING;
	}
	
	public void handleTransformInput(KeyEvent e) {
		if(!canTransform) return;
		switch(e.getKeyCode()){
		case KeyEvent.VK_J:
			if(form == Form.FLAT) break;
			transform(Form.FLAT);
			transformFastSound.play();
			break;
		case KeyEvent.VK_K:
			if(form == Form.NORMAL) break;
			transform(Form.NORMAL);
			transformNormalSound.play();
			break;
		case KeyEvent.VK_L:
			if(form == Form.TALL) break;
			transform(Form.TALL);
			transformSlowSound.play();
		}
	}
	
	private void handleCollisions(CollisionDirection direction){
		int leftTile = (int)Math.floor(bounds.x / Block.SIZE);
		int rightTile = (int)Math.floor((bounds.x + bounds.width) / Block.SIZE);
		int topTile = (int)Math.floor(bounds.y / Block.SIZE);
		int bottomTile = (int)Math.floor((bounds.y + bounds.height) / Block.SIZE);
		
		for(int y = topTile; y <= bottomTile; y++){
			for(int x = leftTile; x <= rightTile; x++){
				if(state == State.DYING) break;
				if(!Level.getCurrentLevel().getBlock(x, y).isSolid()){ 
					Level.getCurrentLevel().getBlock(x,y).collide();
					continue;
				}
				Block block = Level.getCurrentLevel().getBlock(x,y);
				Rectangle tileBounds = Level.getCurrentLevel().getBounds(x, y);
				Rectangle intersection = bounds.intersection(tileBounds);
				int w = intersection.width;
				int h = intersection.height;
				if(bounds.intersects(tileBounds)) {
					switch(direction) {
					case HORIZONTAL:
						if(w >= h) break; // No horizontal collision
						if(bounds.x < tileBounds.x){
							// Collision to right of player
							position.x -= intersection.width;
						}
						else if(bounds.x > tileBounds.x){
							// Collision to left of player
							position.x += intersection.width;
						}
						velocity.x = 0;
						break;
					case VERTICAL:
						if(w < h) break; // No vertical collision
						if(bounds.y < tileBounds.y){
							// Collision below player
							position.y -= intersection.height;
							onGround = true;
						}
						else if(bounds.y > tileBounds.y){
							// Collision above player
							position.y += intersection.height;
						}
						velocity.y = 0;
						break;
					}
					block.collide();
				}
				updateBounds();
				if(direction == CollisionDirection.VERTICAL && new Rectangle(bounds.x, bounds.y+1, bounds.width, bounds.height).intersects(tileBounds)){
					// Player is on a block, but not intersecting the block
					onGround = true;
					block.collide();
				}
			}
		}
		// Prevent player from going off-screen
		if(position.x < 0) position.x = 0;
		if(position.x + width > Game.WIDTH) position.x = Game.WIDTH - width;
		if(position.y < 0) {
			position.y = 0;
			velocity.y = 0;
		}
		if(position.y + height > Game.HEIGHT){
			position.y = Game.HEIGHT - height;
			setState(State.DYING);
		}
	}
	
	private void updateBounds() {
        bounds = new Rectangle((int) position.x, (int) position.y, width, height);
	}
	
	public void transform(Form form){
		this.form = form;
		switch(form){
			case NORMAL:
				color = Color.BLACK;
				speed = 4;
				jumpVelocity = -18;
				doubleJumpVelocity = -15;
				break;
			case TALL:
				color = new Color(0x0000AA);
				speed = 2;
				jumpVelocity = -21;
				doubleJumpVelocity = -18;
				break;
			case FLAT:
				color = new Color(0xAA0012);
				speed = 6;
				jumpVelocity = -13;
				doubleJumpVelocity = -10;
				break;
		}
	}

	public void setState(State state) {
		this.state = state;
		if(state == State.DYING) {
			Player.deathSound.play();
		}
}
	
	public Form getForm(){
        return form;
	}

	public State getState() {
        return state;
	}
	
}
