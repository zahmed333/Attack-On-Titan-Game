
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.util.Random;
import java.util.ArrayList;

/**
* @author Elodie Fourquet 
* @date March, 2021

Most of your code for Project 2 for
+ Part I has to be written in this file (read carefully comments below)
and
+ Part II in a child of this class!

For Part I, make sure your code 
- relies/make use on its abstract parent class, i.e. call the provided
features of timing and window level managment the parent is responsible of
(so skim it enough and often) -- AScrollingGame IS-A AbstractGame
and
- calls the GameGrid instance methods, which are declared in the
interface GridController

For Part II, follow a similar process where the parent class would be this
one, called from you class CreativeGame
*/

public class AScrollingGame extends AbstractGame {
	
	//---------------- Class Variables and Constants -----------------//
	
	protected static final int KEY_DEBUG = KeyEvent.VK_D;
	
	protected static final int KEY_SPEED_UP = KeyEvent.VK_9;
	protected static final int KEY_SLOW_DOWN = KeyEvent.VK_0;
	protected static final int KEY_RESET_SPEED = KeyEvent.VK_R;
	
	protected static final int KEY_MOVE_DOWN = KeyEvent.VK_DOWN;
	protected static final int KEY_MOVE_RIGHT = KeyEvent.VK_RIGHT;
	protected static final int KEY_MOVE_UP = KeyEvent.VK_UP;
	protected static final int KEY_MOVE_LEFT = KeyEvent.VK_LEFT;
	
	
	//USE THIS object! to provide you randomness
	protected static final Random DICE = new Random();   
	
	protected static final String INTRO_SCREEN = "assets/splash1.png";
	// Default player row location for game start
	protected static final int DEFAULT_PLAYER_ROW = 7;
	
	//eren is the main character
	protected static String PLAYER_IMG = "assets/eren.png";  // player image file
	protected static String PLAYER_IMG_RUN = "assets/eren-run.png";
	protected static String PLAYER_IMG_BACK = "assets/eren-back.png";
	protected static String PLAYER_IMG_UP = "assets/eren-up.png"; 
	protected static String PLAYER_IMG_DOWN= "assets/eren-down.png"; 
	
	//2 enemys at random
	protected static String AVOID_IMG = "assets/enemy1.png";
	protected static String AVOID_IMG2 = "assets/enemy2.png";
	protected static String GET_IMG = "assets/get.png";
	
	// ADD more class variables or constants for other assets
	// USE ArrayList to group different GETs or similarly AVOIDs.
	
	
	protected boolean debug = false;
	
	// store last mouse click location 
	// (use it if you want mouse interaction in your creative game)
	protected Location clickCoord;
	
	
	// make sure to update it
	protected Location playerCoord;
	protected Location updatedPlayerCoord;
	
	protected int score;
	protected int hits;
	
	/**********************     Constructors   **********************/
	
	public AScrollingGame() {
		this(GridController.DEFAULT_GRID_H, GridController.DEFAULT_GRID_W);
	}
	
	public AScrollingGame(int grid_h, int grid_w){
		this(grid_h, grid_w, DEFAULT_TIMER_DELAY);
	}
	
	public AScrollingGame(int hdim, int wdim, int init_delay_ms) {
		super(hdim, wdim, init_delay_ms);        
	}
	
	
	
	/****************** Abstract Methods of Parent ******************/
	
	//----- The three below are implemented for you
	// CALL them as you complete ScrollingGame features below
	
	protected void slowDown(int msDelay){
		//increase gameLoop delay (i.e. sleep) to slow down the game 
		currentTimerDelay += msDelay;
	}
	
	protected void speedUp(int msDelay){
		//System.out.println("Previous delay " + currentTimerDelay);
		currentTimerDelay = Math.max(currentTimerDelay - msDelay, MIN_TIMER_DELAY);
		//System.out.println("New delay " + currentTimerDelay);
	}
	
	// update the title bar of the game window 
	protected void updateTitle(String title) {
		super.setTitle(title);
	}  
	
	//----- The abstract methods below are minimally implemented (dummy to run)
	// INCOMPLETE abstract methods below: Implement them for Part I
	// Complete, change and add at your convenience
	
	
	protected void startGame() {
		updateTitle("Welcome to my game...");   
		displaySplashScreen(INTRO_SCREEN);
                                          
		resetGamePlayParam();
	}
	
	
	//Call methods that check for user input
	//   key press and mouse click
	protected int performGameUpdates() {
		clickCoord = handleMouseClick();
		if (clickCoord != null)
			System.out.println("Mouse clicked at : " + clickCoord);
		return handleKeyPress();
	}
	
	
	
	//Call methods that modify assets at each "render ticks"
	//Some assets move each "render ticks", new ones are created
	protected void performRenderUpdates(){
		handleCollisionWith(grid.getAsset(playerCoord));
		updateTitle("Scrolling Game --- SCORE " + score + " ;hits " + hits);
		scroll();
		populate();
	}
	
	// return true if when game is over, false otherwise 
	//   (called each gameLoop update iteration)
	// dummy implementation for now
	protected boolean isGameOver() {
		return (score >= 10 || hits >= 10);
	}
	
	// similar to startGame, but executed after game play
	protected void endGame() {
    	if (score >= 10)
    		updateTitle("GAME OVER ... YOU WON!");
    	else if (hits >= 10)
    		updateTitle("GAME OVER ... YOU LOST");
    displaySplashScreen(null);
	}

	
	
	/****************** ScrollingGame specific instance methods 
	to be implemented for Part I  ******************/
	
	
	
	//Update game state with new assets 
	protected void populate() {
		for (int i = 0; i < grid.getTotalRows(); i++){
			Location temp = new Location(i, grid.getTotalCols() - 1);
			int val = DICE.nextInt(10);
			if (val == 1)
				grid.setAsset(temp, AVOID_IMG);
			else if (val == 0)
				grid.setAsset(temp, GET_IMG);
		}
  	}
  	
  	//Update game state with motions
  	// such as scrolling items from left to right by one column
  	protected void scroll() {
  		for (int row = 0; row < grid.getTotalRows(); row++) {
  			for (int col = 1; col < grid.getTotalCols(); col++) {
  				Location oldScrollCoordinate = new Location(row, col);
  				Location newScrollCoordinate = new Location(row, col-1);
  				Location collision = new Location(playerCoord.getRow(), 
  					playerCoord.getCol() + 1);
  				if(newScrollCoordinate.getRow() == playerCoord.getRow() && 
  					newScrollCoordinate.getCol() == playerCoord.getCol())
  					handleCollisionWith(grid.getAsset(collision));
  				else if(playerCoord.getRow() == row && playerCoord.getCol() == col)
  					grid.setAsset(playerCoord, PLAYER_IMG);	
  					
  				else if(playerCoord.getRow() != row || 
  					playerCoord.getCol() + 1 != col){
  					grid.moveAsset(oldScrollCoordinate, newScrollCoordinate);
  				}
  			}
  		}
  	}
  	
  	//Check for collision with target
  	// could be called with USER for example
  	protected void handleCollisionWith(String target) {
  	  if (GET_IMG == target){ 
  	  	  score ++;
  	  	  updateTitle("Scrolling Game --- SCORE " + score + " ;hits " + hits);
  	  }
  	  else if (AVOID_IMG == target || AVOID_IMG2 == target){ 
  	  	  hits++;
  	  	  updateTitle("Scrolling Game --- SCORE " + score + " ;hits " + hits);
  	  }
  	}
  	
  	protected int handleKeyPress() {
  		
  		int key = super.handleKeyPress(); //call parent for window level keys
  		
  		if (key == KEY_DEBUG) {
  			debug = !debug;     
  			grid.toggleLines(debug);
  		}
  		if(!isPaused){
  			if (key == KEY_MOVE_DOWN) {
  				updatedPlayerCoord = moveRow(playerCoord, 1);
  				moveUser(updatedPlayerCoord, PLAYER_IMG_DOWN);
  			}
  			else if (key == KEY_MOVE_RIGHT) {
  				updatedPlayerCoord = moveCol(playerCoord, 1);
  				moveUser(updatedPlayerCoord, PLAYER_IMG_RUN);
  			}
  			else if (key == KEY_MOVE_UP) {
  				updatedPlayerCoord = moveRow(playerCoord, -1);
  				moveUser(updatedPlayerCoord, PLAYER_IMG_UP);
  			}
  			else if (key == KEY_MOVE_LEFT) {
  				updatedPlayerCoord = moveCol(playerCoord, -1);
  				moveUser(updatedPlayerCoord, PLAYER_IMG_BACK);
  			}
  			else if (key == KEY_SLOW_DOWN) {
  				slowDown(10);
  			}
  			else if (key == KEY_SPEED_UP) {
  				speedUp(10);
  			}
  			else if (key == KEY_RESET_SPEED) {
  				currentTimerDelay = DEFAULT_TIMER_DELAY;
  			}
  		}
  		
  		return key;
  	}
  	
  	
  	// Consider adding an handleCollisionWith giving a Location. Could be
  	// private/protected void/boolean checkForCollisionAt(Location target) {
  	//...
  	//}
  	
  	
  	
  	/****************************** Helper methods ************************/
  	
  	//moves directionally in a row -1 up/down 1 depending on direction
  	private Location moveRow(Location loc1, int ud) {
  		int newRow = loc1.getRow() + ud;
  		if (newRow >= 0 && newRow < grid.getTotalRows())
  			return new Location(newRow, loc1.getCol());
  		return loc1;
  	}
  	
  	//moves directionally in a column -1 left/right 1 depending on direction
  	private Location moveCol(Location loc1, int lr) {
  		int newCol = loc1.getCol() + lr;
  		if (newCol >= 0 && newCol < grid.getTotalCols())
  			return new Location(loc1.getRow(), newCol);
  		return loc1;
  	}
  	
  	//Code that is often used for the handling keys
  	protected void moveUser(Location updatedPlayerCoord, String whichIMG) {
  		handleCollisionWith(grid.getAsset(updatedPlayerCoord));
  		grid.moveAsset(playerCoord, updatedPlayerCoord);
  		grid.setAsset(playerCoord, null);
  		playerCoord = updatedPlayerCoord;
  		grid.setAsset(playerCoord, whichIMG);
  	}
  	
  	
  	// Feel free to add, change or overwite here or in CreativeGame version
  	
  	protected void resetGamePlayParam() {
  		score = 0; 
  		hits = 0;
  		updateTitle("Scrolling Game --- SCORE " + score + " ;hits " + hits);
        
  		// EXPERIMENT with the lines below, including the private helper
  		// feel free to CHANGE/COMMENT/DELETE
  		
  		initPlayer();
  		
  		debug = false;
  		grid.toggleLines(false);
  		System.out.println("debug mode " + debug + " grid lines shown");
  		
  	} 
  	
  	// Feel free to expand/change
  	private void initPlayer() {
  		
  		int startRow = DEFAULT_PLAYER_ROW;
  		if (startRow > grid.getTotalRows())
  			startRow = 0;
  		
  		// store and initialize user position
  		playerCoord = new Location(startRow, 0);
  		grid.setAsset(playerCoord, PLAYER_IMG);
  		
  	}
  	
}








