import java.awt.Color;
import java.awt.Graphics;

public class Bullet extends Sprite {
	public boolean hitTarget = false;
	public Sprite shooter;

	public Bullet(Sprite shooter, int w, int h, String name, String[] pose, int[] count, String filetype,
			int leadingZeroes, String folder, int animation_state) {
		super((int)shooter.px, (int)shooter.py + (shooter.h / 2), w, h, name, pose, count, filetype, leadingZeroes, folder);


    	int bw = shooter.w + shooter.w;
    	if(shooter.motion_horizontal < 0) {
    		bw = 0 - shooter.w;
    	}
    	
    	px = (int)(px + bw);
    	
    	this.animation_state = animation_state;
    	
    	this.shooter = shooter;
	}
	
	public void resolveCollisions(int tileWidth, int tileHeight, int mapWidth, int mapHeight, Tile[]levelLayout) {
    	int pty = (int)Math.round(py / tileHeight), ptx = (int)Math.round((px) / tileWidth);
    	
    	int btl = ((pty) * mapWidth) + ptx;
    	if(btl > levelLayout.length) {
    		btl = ((pty) * mapWidth) + ptx;
    	}
    	int btr = ((pty) * mapWidth) + ptx;
    	if(btr > levelLayout.length) {
    		btr = ((pty) * mapWidth) + ptx;
    	}
    	
    	bottomRightTile = levelLayout[btr];
    	bottomLeftTile = levelLayout[btl];
    	

    	//Player bounds
    	if(px <= 0) {
    		hitTarget = true;
    	}
    	if (px > (mapWidth * tileWidth) - w) {
    		hitTarget = true;
    	}
	}
	
	public void fireBullet() {

		//if(!hitTarget) 
		{
	    	if((shooter.motion_horizontal < 0 && vx == 0) || vx < 0) {
	    		moveLeftIgnorePhysics(3);
	    	}
	    	if((shooter.motion_horizontal > 0 && vx == 0) || vx > 0) {
	    		moveRightIgnorePhysics(3);
	    	}
	    	
			if(bottomLeftTile != null && bottomLeftTile.index != -1 
					&& px < bottomLeftTile.px + bottomLeftTile.w
					&& motion_horizontal < 0) {
	    		hitTarget = true;
			}
			else if(bottomRightTile != null && bottomRightTile.index != -1
						&& px + (w) > bottomRightTile.px
						&& motion_horizontal > 0) {
	    		hitTarget = true;
			}
		}
		
		if (hitTarget == true) {
			stopHorizontalMovement(0);
		}
	}
}
