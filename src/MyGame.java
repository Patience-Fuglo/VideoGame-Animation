//-----------------------------------------------------------------------------
// The GamePanel class implements the game loop and asynchronous input system
//-----------------------------------------------------------------------------
// At the moment this is also where details for specific games are coded 
//-----------------------------------------------------------------------------




import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

//-----------------------------------------------------------------------------

public class MyGame extends GamePanel
{
    // CENTER POSITIONS. Change the big numbers whenever you change the window size.
    int cx = 1900 / 2;
    int cy = 1070 / 2;
    // Map size
    int mapWidth = 40, mapHeight = 10;
	int tileWidth = 100, tileHeight = 100;
        
    // Declare Sprites
	Player player;
	NPC targetDummy;
	ArrayList<Bullet> bullets;
	String[] bulletframeNames = new String[]{"YellowScale__", "OrangeScale__"};
    int[] bulletframeCount = new int[]{9, 9};

    // backgrounds usually have a position of 0,0
    ImageLayer[] backgroundLayers;
    //Image image =  Toolkit.getDefaultToolkit().getImage("./image/food/Apple.png");
    
    // Declare Tilesets
    Tileset defaultTileset;
    
    // Declare Collectibles
    Image[] foodImages;
    int[] foodScores;
    ArrayList<Collectible> collectibles;
    
    // Level Variables
    int score;
    int totalCollected;
    
    // Declare Level layouts, as Tile[] data structures
    ArrayList<Tile[]> levels;
    
    //
    int[] level1TilesLayout = new int[] {
    		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    		-1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    		-1, -1, -1, -1, -1, -1, -1,  0,  0,  0,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0,  0,  0,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    		-1, -1, -1, -1, -1, -1, -1,  0,  0,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1,  0,  0,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0,
    		 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0
    };
    int[] level1CollectiblesLayout = new int[] {
    		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    };

	//Bullet bullet  = new Bullet(-1000, 0, 0);
    
    Font uiKeyFont = new Font("Consolas", Font.LAYOUT_RIGHT_TO_LEFT,  50);
    Font uiValueFont = new Font("Consolas", Font.LAYOUT_LEFT_TO_RIGHT, 50);
    Font scoreFont = new Font("Consolas", Font.BOLD, 100);
		
    //-------------------------------------------------------------------------

    @Override
    public void initialize()
    {
    	String[] frameNames = new String[]{"Jump__", "Crouch__", "Run_Shoot__", "Run_Shoot__", "Run_Shoot__", "Run_Shoot__", "Crouch__", "Idle__", "Idle__", "Dead__"};
        int[] frameCount = new int[]{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9};
    	
    	player = new Player(0, 700, 100, 200, "Soldier2_",frameNames, frameCount, "png", 3, "Soldier2");
    	targetDummy = new NPC(1700, 700, 100, 200, "Soldier1_",frameNames,frameCount, "png", 3, "Soldier1");
    	
    	String backgroundName = "backgroundsIceland"; // Change this to change background
    	backgroundLayers = new ImageLayer[6];
    	for(int b = 1; b < backgroundLayers.length + 1; b++) {
    		backgroundLayers[b - 1] = new ImageLayer(-cx,  -cy, b, backgroundName + "/" + (backgroundLayers.length - b + 1) + ".png");
    	}
    	
    	String tilesetName = "tilesWinter"; // Change this to change tileset
    	defaultTileset = new Tileset(tilesetName + "/", new String[] {
        		"Grass", // 0
        		"GrassLeft", // 1
        		"GrassMid", // 2
        		"GrassRight", // 3
        }, "png");
    	levels = new ArrayList<>();
    	levels.add(new Tile[mapHeight * mapWidth]); // Added a placeholder for title screen
    	levels.add(new Tile[mapHeight * mapWidth]);
    	
    	foodImages = new Image[] {
    			Toolkit.getDefaultToolkit().getImage("./image/food/Cookie.png"),
    			Toolkit.getDefaultToolkit().getImage("./image/food/Apple.png"),
    			Toolkit.getDefaultToolkit().getImage("./image/food/Cherry.png"),
    			Toolkit.getDefaultToolkit().getImage("./image/food/Pineapple.png"),
    			Toolkit.getDefaultToolkit().getImage("./image/food/Fish.png"),
    			Toolkit.getDefaultToolkit().getImage("./image/food/Beer.png"),
    			Toolkit.getDefaultToolkit().getImage("./image/food/Whiskey.png"),
    	};
    	foodScores = new int[] {
    			25, 50, 75, 100, 250, 500, 1000
    			
    	};
    	collectibles = new ArrayList<>();
    	bullets = new ArrayList<>();
    	
        Camera.intialize(0, 0, -cx,  -cy);
        
        LoadLevel(1);
    }
    
    public void CreateBullet(Sprite shooter) {
    	Bullet bullet = new Bullet(shooter, 82 / 2, 37 / 2, "",bulletframeNames, bulletframeCount, "png", 3, "Bullet", 0);
    	bullets.add(bullet);
    }
    
    public void LoadLevel(int currentLevel) {
    	// initialize level layout
    	if(currentLevel == 1) {
    		Tile[] level = levels.get(currentLevel);
    		
    		for(int y = 0; y < mapHeight; y++) {
    			for (int x = 0; x < mapWidth; x++) {
    				int tileIndex = (y * mapWidth) + x;
    				int tx = x * tileWidth;
    				int ty = y * tileWidth;
    				
    				// Set Tiles
    				int layout_cell = level1TilesLayout[tileIndex];
    	    		level[tileIndex] = new Tile(tx, ty, tileWidth, tileHeight, defaultTileset, layout_cell);
    	    		
    	    		// Set Collectibles
    	    		int collectible_cell = level1CollectiblesLayout[tileIndex];
    	    		if(collectible_cell != -1) {
    	    			collectibles.add(new Collectible(tx, ty, tileWidth, tileHeight, foodImages[collectible_cell], foodScores[collectible_cell]));
    	    		}
    			}
    		}
    	}
    }
   

    
    //-------------------------------------------------------------------------
    //   The boolean variables reflecting the keyboard state are queried and
    //   user controlled entities are updated accordingly
    //-------------------------------------------------------------------------
    
    public void respond_To_User_Input()
    {	
     	//if(pressing[UP])  tank.moveForward(5);
    	//if(pressing[DN])  tank.moveBackward(3);
    	//if(pressing[LT])  player.moveLeft(3);
    	//if(pressing[RT])  player.moveRight(3);
    	
    	if(pressing[LT]) {
            player.moveLeft(3);
        }
    	else if(pressing[RT])  {
            player.moveRight(3);
        }
    	else {
    		player.stopHorizontalMovement(0);
    	}
    	
    	if(pressing[UP])  {
            player.jump(5);
        }
    	if(pressing[DN])  {
            player.moveDown(3);
        }
    	player.handlePhysics();
    	
    	if (pressing[SPACE] && player.fireBullet()) {
    		CreateBullet(player);
    	}
    	
    	//if(pressing[SPACE])  player.shoot(bullet);
    	
    	Camera.track(player);

    	
    }
    
	//-------------------------------------------------------------------------

     
    public void move_Computer_Controlled_Entities()
    { 
    	targetDummy.moveInDirectPath();

    	targetDummy.handlePhysics();

    	if (targetDummy.fireBullet()) {
    		CreateBullet(targetDummy);
    	}
    	
    	for(int i = bullets.size() - 1; i >= 0; i--) {
    		Bullet bullet = bullets.get(i);
    		bullet.fireBullet();
    	}
    }

    //-------------------------------------------------------------------------

    public void resolve_Collisions()
    {
    	player.resolveCollisions(tileWidth, tileHeight, mapWidth, mapHeight, levels.get(1));
    	targetDummy.resolveCollisions(tileWidth, tileHeight, mapWidth, mapHeight, levels.get(1));
    	
    	for(int b = 0; b < bullets.size(); b++) {
    		Bullet bullet = bullets.get(b);
    		
    		bullet.resolveCollisions(tileWidth, tileHeight, mapWidth, mapHeight, levels.get(1));
    		
    		//for(int n = 0; n < )
    		
    		if(bullet.shooter == player) {
	    		if(targetDummy.overlaps(bullet) && targetDummy.health > 0){
	    			targetDummy.hurt();
	    			bullet.hitTarget = true;
	    		}
    		}
    		else
    		{
	    		if(player.overlaps(bullet) && player.health > 0){
	    			player.hurt();
	    			bullet.hitTarget = true;
	    		}
    		}
    		
    		if(bullet.hitTarget) {
    			bullet = bullets.remove(b);
    			bullet = null;
    		}
    	}

    	
    	for(int c = collectibles.size() - 1; c >= 0; c--) {
    		if(player.overlaps(collectibles.get(c)) && !collectibles.get(c).collected) {
    			totalCollected++;
    			score += collectibles.get(c).score;
    			collectibles.get(c).collected = true;
    			//collectibles.remove(c);
    		}
    	}
    }
    
    //-------------------------------------------------------------------------
    // paint the screen when the O.S. calls according to the code below
    //-------------------------------------------------------------------------
    
	public void paint(Graphics pen)
	{    	
		pen.clearRect(0, 0,2560, 1536);

		// Draw Background stuff
        for(int i = 0; i < backgroundLayers.length; i++) {
        	backgroundLayers[i].draw(pen);
		}
                
        // Draw Tilesets
        for(int i = 0; i < levels.get(1).length; i++) {
        	// Draw Every tile from level layout
        	levels.get(1)[i].draw(pen);
        }
        
        // Draw Sprite Stuff
        //pen.drawImage(image, 0, 0, 100, 100, null);


		// Draw Player
		player.draw(pen, -50, -25, 2);
		
		targetDummy.draw(pen, -50, -25, 2);
		
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).draw(pen);
		}
		
		if(player.bottomTile != null)
			player.bottomTile.drawHighlight(pen, Color.blue);
		if(player.bottomLeftTile != null)
			player.bottomLeftTile.drawHighlight(pen, Color.green);
		if(player.bottomRightTile != null)
			player.bottomRightTile.drawHighlight(pen, Color.ORANGE);
		
        
		// Draw Collectibles
		for(int i = 0; i < collectibles.size(); i++) {
			// Draw Every collectible from level layout
			if(!collectibles.get(i).collected) {
				collectibles.get(i).draw(pen);
			}
			else {
				collectibles.get(i).drawUI(pen, (i * 25) + 25, 75, 25, 25);
			}
		}
		
		// Draw String (Scores, variables, etc.)
		pen.setFont(uiKeyFont);
		pen.setColor(Color.orange);
		
    	pen.drawString(("SCORE: "), 25, 50);
    	//pen.drawString(("COLLECTED: "), 25, 100);

		pen.setFont(uiValueFont);
    	pen.drawString(("" + score), 200, 50);
    	//pen.drawString(("COLLECTED: "), 25, 100);
	}
	
	//-------------------------------------------------------------------------
	// When the left mouse button is pressed 
	//    Get the coordinates (mx, my) where the mouse was when pressed
	//    Check if (mx, my) is inside any of the Soldiers or the BattleLord
	///   If so make put that entity under the user's control by assigning
	//    it to my_sprite

	//-------------------------------------------------------------------------

	//*  Rectangle Editor
	int mx = 0;
	int my = 0;
	
	int nx = 0;
	int ny = 0;
	
	/*
	Rect[] rect = new Rect[100];
	int count = -1;

	//*/
	
	
    public void mousePressed(MouseEvent e)
    {
    	// Get the mouse location
    	mx = e.getX();
    	my = e.getY();
    	
    /*  Rectangle Editor
    	count++;
    	
    	rect[count] = new Rect(mx, my, 0, 0, Color.BLACK);
    */
    }
    
	//-------------------------------------------------------------------------

    public void mouseDragged(MouseEvent e)
    {
    	// Get the mouse location
    	nx = e.getX();
    	ny = e.getY();
    	
    	
    /*  Rectangle Editor
    	int dx = nx - mx;
    	int dy = ny - my;
    	
    	rect[count].setSize(dx, dy);
    //*/	
    	
    }
    
	//-------------------------------------------------------------------------

    public void mouseMoved(MouseEvent e)
    {
    	// Get the mouse location
    	int mx = e.getX();
    	int my = e.getY();
    	
    	
    	//player.aimAt(mx, my, 3);
    }
    
	//-------------------------------------------------------------------------
    // Not used yet
	//-------------------------------------------------------------------------
    
    public void mouseReleased(MouseEvent e)
    {
    	mousePressed = false;
    }
    
	
}