import java.awt.*;
import java.awt.event.*;
import javax.swing.*;




public class GameMain extends JPanel implements MouseListener{
	//Constants for game 
	// number of ROWS by COLS cell constants 
	public static final int ROWS = 3;     
	public static final int COLS = 3;  
	public static final String TITLE = "Tic Tac Toe";

	//constants for dimensions used for drawing
	//cell width and height
	public static final int CELL_SIZE = 100;
	//drawing canvas
	public static final int CANVAS_WIDTH = CELL_SIZE * COLS;
	public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
	//Noughts and Crosses are displayed inside a cell, with padding from border
	public static final int CELL_PADDING = CELL_SIZE / 6;    
	public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;    
	public static final int SYMBOL_STROKE_WIDTH = 8;

	/*declare game object variables*/
	// the game board 
	private Board board;

	private GameState currentState; 

	// the current player
	private Player currentPlayer; 
	// for displaying game status message
	private JLabel statusBar;       


	/** Constructor to setup the UI and game components on the panel */
	public GameMain() {   

		setFocusable(true);              // Makes the panel focusable and added a mouse listener 
		addMouseListener(this);	    

		// Setup the status bar (JLabel) to display status message       
		statusBar = new JLabel("         ");       
		statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 14));       
		statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));       
		statusBar.setOpaque(true);       
		statusBar.setBackground(Color.LIGHT_GRAY);  

		//layout of the panel is in border layout
		setLayout(new BorderLayout());       
		add(statusBar, BorderLayout.SOUTH);
		// account for statusBar height in overall height
		setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT + 30));

		// Create a new instance of the game "Board"class. 
		board = new Board();
		//call initGame() to initialize the game board 
		initGame();
	}

	public static void main(String[] args) {
		// Run GUI code in Event Dispatch thread for thread safety.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//create a main window to contain the panel
				JFrame frame = new JFrame(TITLE);

				//create the new GameMain panel and add it to the frame 
				GameMain gamePanel = new GameMain(); 
				frame.add(gamePanel);		


				//the default close operation of the frame to exit_on_close
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    

				frame.pack();             
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
	/** Custom painting codes on this JPanel */
	public void paintComponent(Graphics g) {
		//fill background and set colour to white
		super.paintComponent(g);
		setBackground(Color.CYAN);
		//ask the game board to paint itself
		board.paint(g);

		//set status bar message
		if (currentState == GameState.Playing) {          
			statusBar.setForeground(Color.BLACK);          
			if (currentPlayer == Player.Cross) {   

				//the status bar to display the message "X"'s Turn 
				statusBar.setText("'X's Turn");

			} else {    

				// the status bar to display the message "O"'s Turn 
				statusBar.setText("'0's Turn");

			}       
		} else if (currentState == GameState.Draw) {          
			statusBar.setForeground(Color.RED);          
			statusBar.setText("It's a Draw! Click to play again.");       
		} else if (currentState == GameState.Cross_won) {          
			statusBar.setForeground(Color.RED);          
			statusBar.setText("'X' Won! Click to play again.");       
		} else if (currentState == GameState.Nought_won) {          
			statusBar.setForeground(Color.RED);          
			statusBar.setText("'O' Won! Click to play again.");       
		}
	}


	/** Initialise the game-board contents and the current status of GameState and Player) */
	public void initGame() {
		for (int row = 0; row < ROWS; ++row) {          
			for (int col = 0; col < COLS; ++col) {  
				// all cells empty
				board.cells[row][col].content = Player.Empty;           
			}
		}
		currentState = GameState.Playing;
		currentPlayer = Player.Cross;
	}


	/**After each turn check to see if the current player hasWon by putting their symbol in that position, 
	 * If they have the GameState is set to won for that player
	 * If no winner then isDraw is called to see if deadlock, if not GameState stays as PLAYING
	 *   
	 */
	public void updateGame(Player thePlayer, int row, int col) {
		// Check for win after play
		if (board.hasWon(thePlayer, row, col)) {
			// Set the game state to the winner's state
			if (thePlayer == Player.Cross) {
				currentState = GameState.Cross_won;  // If Cross wins
			} else {
				currentState = GameState.Nought_won; // If Nought wins
			}


		} else if (board.isDraw()) {
			// If it's a draw, update the currentState
			currentState = GameState.Draw;

		}
		// Otherwise, the game continues (no need to update currentState here, as it's already set to Playing)
	}



	/** Event handler for the mouse click on the JPanel. If selected cell is valid and Empty then current player is added to cell content.
	 *  UpdateGame is called which will call the methods to check for winner or Draw. if none then GameState remains playing.
	 *  If win or Draw then call is made to method that resets the game board.  Finally a call is made to refresh the canvas so that new symbol appears*/

	public void mouseClicked(MouseEvent e) {  
		// get the coordinates of where the click event happened            
		int mouseX = e.getX();             
		int mouseY = e.getY();             
		// Get the row and column clicked             
		int rowSelected = mouseY / CELL_SIZE;             
		int colSelected = mouseX / CELL_SIZE;               			
		if (currentState == GameState.Playing) {                
			if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0 && colSelected < COLS && board.cells[rowSelected][colSelected].content == Player.Empty) {
				// move  
				board.cells[rowSelected][colSelected].content = currentPlayer; 
				// update currentState                  
				updateGame(currentPlayer, rowSelected, colSelected); 
				// Switch player
				if (currentPlayer == Player.Cross) {
					currentPlayer =  Player.Nought;
				}
				else {
					currentPlayer = Player.Cross;
				}
			}             
		} else {        
			// game over and restart              
			initGame();            
		}   repaint();
	}


	@Override
	public void mousePressed(MouseEvent e) {
		//  Auto-generated, event not used

	}
	@Override
	public void mouseReleased(MouseEvent e) {
		//  Auto-generated, event not used

	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// Auto-generated,event not used

	}
	@Override
	public void mouseExited(MouseEvent e) {
		// Auto-generated, event not used

	}

}
