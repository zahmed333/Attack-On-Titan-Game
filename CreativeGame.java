import java.awt.event.KeyEvent;
import java.awt.Color;
import java.util.Random;
import java.util.ArrayList;

public class CreativeGame extends AScrollingGame {
	
	protected static final String INTRO_SCREEN = "assets/Video Game Start Screen.jpg";
	protected static final String INTRO_SCREEN2 = "assets/instructions.png";
	protected static final String WIN_SCREEN = "assets/Video Game End Screen Win.png";
	protected static final String LOSE_SCREEN = "assets/Video Game End Screen.png";
	
	protected static final String ATTACKTWO = "assets/transformation2.png";
	
	protected static final String DESTROYDISPLAY = "assets/destroy.png";
	
	protected static final int KEY_ATTACK = KeyEvent.VK_SPACE;
	protected static final int KEY_HELP = KeyEvent.VK_H;
	
	protected static int tAttacks = 0;
	protected static int ultAttack = 0;
	
	public CreativeGame() {
		this(GridController.DEFAULT_GRID_H, GridController.DEFAULT_GRID_W);
	}
	
	public CreativeGame(int grid_h, int grid_w){
		super(grid_h, grid_w, DEFAULT_TIMER_DELAY);
	}
	
	public CreativeGame(int hdim, int wdim, int init_delay_ms) {
		super(hdim, wdim, init_delay_ms);        
	}
	
	//adds on instructions
	protected void startGame() {
		updateTitle("Welcome to my game...");   
		displaySplashScreen(INTRO_SCREEN);
		displaySplashScreen(INTRO_SCREEN2);
		resetGamePlayParam();
	}
	
	//displays new splashscreens
	protected void endGame() {
    		if (score >= 10) {
    			updateTitle("GAME OVER ... YOU WON!");
    			displaySplashScreen(WIN_SCREEN);
    			displaySplashScreen(null);
    		}
    		else if (hits >= 10) {
    			updateTitle("GAME OVER ... YOU LOST");
    			displaySplashScreen(LOSE_SCREEN);
    			displaySplashScreen(null);
    		}
    }
    		
  	private void initPlayer() {
  		
  		int startRow = DEFAULT_PLAYER_ROW;
  		if (startRow > grid.getTotalRows())
  			startRow = 0;
  		
  		// store and initialize user position
  		playerCoord = new Location(startRow, 0);
  		grid.setAsset(playerCoord, PLAYER_IMG);
  		
  		
  	}
  	
  	protected void moveUser(Location updatedPlayerCoord, String whichImg) {
  		handleCollisionWith(grid.getAsset(updatedPlayerCoord));
  		grid.moveAsset(playerCoord, updatedPlayerCoord);
  		grid.setAsset(playerCoord, null);
  		playerCoord = updatedPlayerCoord;
  		grid.setAsset(playerCoord, whichImg);
  	}
  	
  	//randomly decides between the 2 enemies
  	protected void populate() {
		for (int i = 0; i < grid.getTotalRows(); i++){
			Location temp = new Location(i, grid.getTotalCols() - 1);
			int val = DICE.nextInt(110);
			if (val > 90){
				if (DICE.nextInt(2) == 1)
					grid.setAsset(temp, AVOID_IMG);
				else
					grid.setAsset(temp, AVOID_IMG2);
			}
			else if (val == 5)
				grid.setAsset(temp, GET_IMG);
		}
  	}
  	
  	//attacks the asset in front by setting it to null
  	protected void attack() {
  		if (tAttacks < 5) {
  			grid.setAsset(playerCoord, ATTACKTWO);
  			
  			Location temp = new Location(playerCoord.getRow(), playerCoord.getCol() + 1);
  			grid.setAsset(temp, null);
  			tAttacks++;
  		}
  	}
  	
  	//sets every enemy to a null as part of the one time ultimate attack feature
  	protected void helpDestroy() {
  		if (ultAttack < 1) {
  			displaySplashScreen(DESTROYDISPLAY);
  			for (int row = 0; row < grid.getTotalRows(); row++) {
  				for (int col = 1; col < grid.getTotalCols(); col++) {
  					Location temp = new Location(row, col);
  					if (grid.getAsset(temp) == AVOID_IMG || 
  					grid.getAsset(temp) == AVOID_IMG2) 
  						grid.setAsset(temp, null);
  				}
  			}
  		ultAttack++;
  		}
  	}
  	
	protected int handleKeyPress() {
  		int key = super.handleKeyPress(); //call parent for window level keys
  		
  		if(!isPaused){
  			if (key == KEY_ATTACK) 
  				attack();
  			
  			else if (key == KEY_HELP) 
  				helpDestroy();
  		}
  		return key;
  	}
  	
}