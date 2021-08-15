import java.awt.Color;
import java.awt.Graphics;

public class NPC extends Sprite {

	public Rect path;
    int rateOfFire = 10;
    int triggerTimer = 0;

	public NPC(int x, int y, int w, int h, String name, String[] pose, int[] count, String filetype, int leadingZeroes,
			String folder) {
		super(x, y, w, h, name, pose, count, filetype, leadingZeroes, folder);

		// Set animation delays
		animation[LEFT_IDLE].setDelay(10);
		animation[RIGHT_IDLE].setDelay(10);
		animation[UP_JUMP].setDelay(3);
		animation[LEFT_WALK].setDelay(2);
		animation[RIGHT_WALK].setDelay(2);
		
		animation[DEATH].setLoop(false);

    	int bw = w + w;
    	if(motion_horizontal < 0) {
    		bw = 0 - w;
    	}
    	
    	this.path = new Rect((int)px + bw, (int)py + (h / 2), 100, 2, Color.pink);
	}
	
	public void handlePhysics() {
		super.handlePhysics();

		path.px = px;
		path.py = py;
	}

	public void moveInDirectPath() {
		if(bottomRightTile != null && bottomRightTile.index != -1 && motion_horizontal > 0) {
			moveLeft(3);
			return;
		}
		else if(bottomLeftTile != null && bottomLeftTile.index != -1 && motion_horizontal < 0) {
			moveRight(3);
			return;
		}
		
		if (motion_horizontal > 0) {
			moveRight(3);
		}
		else if (motion_horizontal < 0) {
			moveLeft(3);
		}
	}

	
	public void fixPath() {
    	int bw = w + w;
    	if(motion_horizontal < 0) {
    		bw = 0 - w;
    	}
    	
		path.px = px + bw;
		path.py = py + (h / 2);
	}

	public void resolveCollisions(int tileWidth, int tileHeight, int mapWidth, int mapHeight, Tile[]levelLayout) {
		super.resolveCollisions(tileWidth, tileHeight, mapWidth, mapHeight, levelLayout);

    	int pty = (int)Math.round(py / tileHeight), ptx = (int)Math.round((px) / tileWidth);
    	
    	int pathX = ptx;
    	int pathIndex = ((pty) * mapWidth) + pathX;
    	if(motion_horizontal == RIGHT) {
    		while(pathX < mapWidth) {
    			System.out.println("test");
    			if(levelLayout[pathIndex].index == -1) {
    				pathIndex++;
    			}
    			else {
    				path.w = (int)(path.px - levelLayout[pathIndex].px);
    				break;
    			}
    		}
    		
    		if(pathX > mapWidth) {
    			pathIndex = ((pty) * mapWidth) + pathX - 1;
				path.w = 100;
    		}
    	}
	}

	public boolean fireBullet() {
		if(health > 0) {
			if(triggerTimer >= rateOfFire)  {
	    		triggerTimer = 0;
	    		return true;
	        }
	    	else {
	    		triggerTimer++;
	    	}
		}
		return false;
	}
	
	public void draw(Graphics pen, int offsetX, int offsetY, double flipScaleFactor) {
		super.draw(pen, offsetX, offsetY, flipScaleFactor);
		
		path.draw(pen);
	}
}
