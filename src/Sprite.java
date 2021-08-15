//-----------------------------------------------------------------------------
// The Sprite class implements an object that can move around the screen
// while displaying animations appropriate to the circumstances  
//-----------------------------------------------------------------------------



import java.awt.*;
//-----------------------------------------------------------------------------

public class Sprite extends Rect
{
    //new String[]{"Jump", "Slide", "Walk", "Walk", "Run", "Run", "Sit-down00", "Idle", "Idle", "Death", "Sneaking"}, 
	//-------------------------------------------------------------------------
		
	Animation[] animation;  // an array of Animation objects to select from
	boolean animationsLoaded = false;
        
        final int UP    = -1; 
        final int CENTERED  = 0; 
        final int DOWN  = 1; 
        final int LEFT  = -1; 
        final int RIGHT = 1;  
		
        final int UP_JUMP    = 0;    // Convenience constants
        final int DOWN_FALL  = 1;    // indexing the array of
        final int LEFT_WALK  = 2;    // Animations according to
        final int RIGHT_WALK = 3;    // the type of motion
        final int RIGHT_RUN = 4;
        final int LEFT_RUN = 5;
        final int DOWN_SIT = 6;
        final int LEFT_IDLE = 7;
        final int RIGHT_IDLE = 8;
        final int DEATH = 9;
        final int SNEAKING = 10;

    	
    	int health = 5;

    
	boolean moving  = false; // the Sprite can be moving or still
	
	int motion_horizontal = RIGHT;        // index indicating the type of motion
	int motion_vertical = CENTERED;        // index indicating the type of motion
	int animation_state = RIGHT_IDLE;        // index indicating the type of motion
	
	boolean alive = true;

	int pushGroundTimer = 0;
	int jumpForce = 10;
	int jumpTimer = 0;
	int jumpMax = 50;
	boolean isGrounded = true;
	boolean jumping = false;
	int jumpHeight;
	
	public Tile bottomTile;
	public Tile bottomRightTile;
	public Tile bottomLeftTile;
	
	
	//-------------------------------------------------------------------------
	// Construct the Sprite
	//-------------------------------------------------------------------------
	
	public Sprite(int x, int y, int w, int h, String name, String[] pose, int count, String filetype)
	{
		super(x, y, w, h, Color.RED);
		 
		// Set length of array to match the number of Animations
		animation = new Animation[pose.length];
		 
		// Load the Animations
		for(int i = 0; i < pose.length; i++)
		{
			animation[i] = new Animation(name + "_" + pose[i] + "_", count, filetype);
		}
	}
	
	//-------------------------------------------------------------------------
	// Construct the Sprite
	//-------------------------------------------------------------------------
	
	public Sprite(int x, int y, int w, int h, String name, String[] pose, int[] count, String filetype)
	{
		super(x, y, w, h, Color.RED);
		 
		// Set length of array to match the number of Animations
		animation = new Animation[pose.length];
		 
		// Load the Animations
		for(int i = 0; i < pose.length; i++)
		{
			animation[i] = new Animation(name + "_" + pose[i] + "_", count[i], filetype);
		}
	}
        
        // Explicitly loads the player
	/*public Sprite(int x, int y, int w, int h, String name, String[] pose, int[] count, String filetype, String delimiter, int leadingZeroes, String folder)
	{
		super(x, y, w, h, Color.RED);
		 
		// Set length of array to match the number of Animations
		animation = new Animation[pose.length];
		 
		// Load the Animations
		for(int i = 0; i < pose.length; i++)
		{
			//animation[i] = new Animation(pose[i] + delimiter + name + "_", count[i], filetype, leadingZeroes);
            //System.out.println(folder + "/" + pose[i] + delimiter + name + "_" + pose[i] + ", " + count[i]);
			animation[i] = new Animation(folder + "/" + pose[i] + delimiter + name + "_", count[i], filetype, leadingZeroes);
		}
	}*/
	
	public Sprite(int x, int y, int w, int h, String name, String[] pose, int[] count, String filetype, int leadingZeroes, String folder)
	{
		super(x, y, w, h, Color.RED);
		 
		// Set length of array to match the number of Animations
		animation = new Animation[pose.length];
		 
		// Load the Animations
		for(int i = 0; i < pose.length; i++)
		{
			//animation[i] = new Animation(pose[i] + delimiter + name + "_", count[i], filetype, leadingZeroes);
            //System.out.println(folder + "/" + pose[i] + delimiter + name + "_" + pose[i] + ", " + count[i]);
			animation[i] = new Animation(folder + "/" + pose[i], count[i], filetype, leadingZeroes);
			System.out.println(folder + "/" + name + pose[i] + "_");
		}
	}
	
	public int getMotionHorizontal() {
		return motion_horizontal;
	}
	
	//-------------------------------------------------------------------------
	
	public void jump(int dy)
	{
		if(isGrounded) {
			jumpHeight = dy;
			
			moving = true;   // Cause paint method to use Animation's current image
			
			animation_state = UP_JUMP;     // Set Animation array index according to this action
		}
	}
	
	public void resolveCollisions(int tileWidth, int tileHeight, int mapWidth, int mapHeight, Tile[]levelLayout) {
    	int pty = (int)Math.round(py / tileHeight), ptx = (int)Math.round((px) / tileWidth);
    	
    	int btl = ((pty + 1) * mapWidth) + ptx - 1;
    	if(btl > levelLayout.length) {
    		btl = ((pty + 1) * mapWidth) + ptx;
    	}
    	int btr = ((pty + 1) * mapWidth) + ptx + 1;
    	if(btr > levelLayout.length) {
    		btr = ((pty + 1) * mapWidth) + ptx;
    	}
    	
    	bottomRightTile = levelLayout[btr];
    	bottomLeftTile = levelLayout[btl];

    	pty = (int)Math.round(py / tileHeight);
    	ptx = (int)Math.round((px) / tileWidth);

    	int bti = ((pty + 2) * mapWidth) + ptx;
    	
    	
    	bottomTile = levelLayout[bti];
    	
    	// Handle fall collisions
    	if(bottom_overlaps(bottomTile)  && bottomTile.index != -1) {
			setGrounded(true);
   			py = bottomTile.py - h;
		}
		else {

	    	bottomTile = levelLayout[bti];
			if(bottom_overlaps(bottomTile)  && bottomTile.index == -1) {
				setGrounded(false);
			}
		}
    	

    	//Player bounds
    	if(px <= 0) {
    		px = 0;
    	}
    	if (px > (mapWidth * tileWidth) - w) {
    		px = (mapWidth * tileWidth) - w;
    	}
    	if(py <= 0) {
    		py = 0;
    	}
    	if (py >= (mapHeight * tileHeight) - h) {
    		py = (mapHeight * tileHeight) - h;
    	}
	}


	public void handlePhysics() {		
		
		if(health > 0)
			if(vx < 0) {
				motion_horizontal = LEFT;
			}
			else if(vx > 0) {
				motion_horizontal = RIGHT;
			}
			
		
		if(pushGroundTimer < jumpForce && animation_state == UP_JUMP && health > 0) {
			pushGroundTimer++;
		}
		else if (pushGroundTimer < jumpForce && pushGroundTimer != 0 && animation_state != UP_JUMP &&  jumpTimer < jumpMax){
			// Player moved or canceled jump
			pushGroundTimer = 0;
			moving = false;
            animation[UP_JUMP].reset();
		}
		else if(pushGroundTimer >= jumpForce && jumpTimer < jumpMax) {
			isGrounded = false;
			
			if(vy == 0) {
				vy -= jumpHeight;         // moving up on the screen corresponds to y going down
			}
			
			move();
			
			// listener
			onJump();
			
			jumping = true;
			
			jumpTimer++;
		}
		else if (((pushGroundTimer >= jumpForce && jumpTimer >= jumpMax) || !jumping) && !isGrounded) {
			// Player should be falling
			fall(10);
			
			move();
			
			animation_state = DOWN_FALL;
		}
		else if(health > 0){
			// Reset jump ability
			if(jumping && jumpTimer >= jumpMax) {
				jumping = false;
				jumpTimer = 0;
				pushGroundTimer = 0;
	            animation[UP_JUMP].reset();
	            moving = false;
			}

			// Player is grounded and can either stay in motion or remain idle.

			if(vx != 0) {
				move();
			}

				
			if(vx < 0) {
                animation_state = LEFT_WALK;
                animation[UP_JUMP].reset();
			}
			else if(vx > 0) {
                animation_state = RIGHT_WALK;
                animation[UP_JUMP].reset();
			}
			else {
				moving = false;
			}
			
	        if(!moving && motion_horizontal == LEFT)
			{
	            animation_state = LEFT_IDLE;
			}
	        else if(!moving && motion_horizontal == RIGHT){
	            animation_state = RIGHT_IDLE;
	        }
		}
		
		if(health <= 0) {
			death();
		}
	}
	
	public void onJump() {
		// Place code here, or subclasses should
	}
	
	public void fall(int dy) {
		if(vy <= 0) {
			vy = 0;
			vy += dy;
		}
		
		moving = true;
	}
	
	public void setGrounded(boolean value) {
		if(value)
			vy = 0;
		isGrounded = value;
	}
	
	//-------------------------------------------------------------------------
		
	public void moveUp(int dy)
	{
		//vy -= dy;         // moving up on the screen corresponds to y going down
		
		//moving = true;   // Cause paint method to use Animation's current image
		
		//animation_state = UP_JUMP;     // Set Animation array index according to this action
	}
	
	//-------------------------------------------------------------------------

	public void moveDown(int dy)
	{
		//vy += dy;
		
		//moving = true;

		if(animation_state != UP_JUMP) {
			animation_state = DOWN_SIT;
            animation[UP_JUMP].reset();
		}
	}
	
	//-------------------------------------------------------------------------

	public void moveLeft(int dx)
	{
		if((motion_horizontal != LEFT || vx >= 0)) {
			vx = 0;
			vx -= dx;
		}
		moving = true;

		if(bottomLeftTile != null && bottomLeftTile.index != -1 
				&& px < bottomLeftTile.px + bottomLeftTile.w
				&& motion_horizontal < 0) {
			px = bottomLeftTile.px + bottomLeftTile.w;
			stopHorizontalMovement(0);
		}
	}
	
	//-------------------------------------------------------------------------

	public void moveRight(int dx)
	{
		
		if((motion_horizontal != RIGHT || vx <= 0)) {
			vx = 0;
			vx += dx;
		}
		moving = true;
		

		if(bottomRightTile != null && bottomRightTile.index != -1
				&& px + (w) > bottomRightTile.px
				&& motion_horizontal > 0) {
			px = bottomRightTile.px  - (w);
			stopHorizontalMovement(0);
		}
	}
	
	public void moveLeftIgnorePhysics(int dx) {
		vx -= dx;
		moving = true;
		move();
	}

	
	public void moveRightIgnorePhysics(int dx) {
		vx += dx;
		moving = true;
		move();
	}
	
	//-------------------------------------------------------------------------

	public void stopHorizontalMovement(int dx)
	{
		vx = 0;
	}
	
	//-------------------------------------------------------------------------

	public void death()
	{
		//alive = false;
		
		//py = -100000;

		animation_state = DEATH;
	}

	
	public void hurt() {
		stopHorizontalMovement(0);
		health--;
	}
	
	//-------------------------------------------------------------------------
	// Draws the Sprite on the screen according to its state:
	//   moving indicated whether to use the Animation's current or still image
	//   motion is an index into the array of Animations to pick the right pose    
	//-------------------------------------------------------------------------

        @Override
	public void draw(Graphics pen)
	{
        super.draw(pen);
	    if(alive)
		{
            if(motion_horizontal == LEFT)
                pen.drawImage(animation[animation_state].getCurrentImage(),
                        (int)(px + w - Camera.dx()),
                        (int)(py - Camera.dy()),
                        (int)(motion_horizontal * animation[animation_state].getCurrentImage().getWidth(null) * .5),
                        (int)(animation[animation_state].getCurrentImage().getHeight(null) * .5), null);
            else if (motion_horizontal == RIGHT){
                pen.drawImage(animation[animation_state].getCurrentImage(),
                        (int)(px - Camera.dx()),
                        (int)(py - Camera.dy()),
                        (int)(motion_horizontal * animation[animation_state].getCurrentImage().getWidth(null) * .5),
                        (int)(animation[animation_state].getCurrentImage().getHeight(null) * .5), null);
            }
			
			//super.draw(pen);
		}
	}
        
	public void draw(Graphics pen, int offsetX, int offsetY, double flipScaleFactor)
	{
        super.draw(pen);
	    if(alive)
		{
            if(motion_horizontal == LEFT)
                pen.drawImage(animation[animation_state].getCurrentImage(),
                        (int)(px + (w * flipScaleFactor) + offsetX - Camera.dx()),
                        (int)(py  + offsetY - Camera.dy()),
                        (int)(motion_horizontal * animation[animation_state].getCurrentImage().getWidth(null) * .5),
                        (int)(animation[animation_state].getCurrentImage().getHeight(null) * .5), null);
            else if (motion_horizontal == RIGHT){
                pen.drawImage(animation[animation_state].getCurrentImage(),
                        (int)(px  + offsetX - Camera.dx()),
                        (int)(py  + offsetY - Camera.dy()),
                        (int)(motion_horizontal * animation[animation_state].getCurrentImage().getWidth(null) * .5),
                        (int)(animation[animation_state].getCurrentImage().getHeight(null) * .5), null);
            }
			
			//super.draw(pen);
		}
	}
        
        public void loadImages(Graphics pen) {
        	if(!animationsLoaded) {
            	for(int i = 0; i < animation.length; i++) {
                    pen.drawImage(animation[animation_state].getCurrentImage(),
                            (int)px + w - Camera.dx(),
                            (int)py - Camera.dy(),
                            (int)(motion_horizontal * animation[animation_state].getCurrentImage().getWidth(null)  * .25),
                            (int)(animation[animation_state].getCurrentImage().getHeight(null)  * .25), null);
            	}
            	animationsLoaded = true;
        	}
        }
	
	//-------------------------------------------------------------------------

}
