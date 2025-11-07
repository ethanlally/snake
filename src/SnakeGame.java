// SnakeGame.java
import javax.swing.JFrame;

/**
 * The main driver class for the Snake Game.
 * This class sets up the JFrame (the main window) and initializes the game panel.
 * Students should focus on the GamePanel and Point classes for game logic.
 */
public class SnakeGame {

    // --- 1. VARIABLES (Constants for Game Setup) ---
    // Define the dimensions of the game window
    public static final int BOARD_WIDTH = 540;
    public static final int BOARD_HEIGHT = 540;

    // Define the size of each grid unit (to be used in GamePanel)
    public static final int UNIT_SIZE = 25;


    // --- 2. USER-DEFINED METHOD: main ---
    public static void main(String[] args) {
        // Create the main window object
        JFrame frame = new JFrame("Classic Java Snake Game");
        
        // Create the panel where the game will be drawn and logic will run
        GamePanel gamePanel = new GamePanel();

        // --- CODE TO ADD FRAME COMPONENTS ---
        // STUDENTS: Add the gamePanel object to the frame.
        frame.add(gamePanel);

        // Set the size of the window
        frame.setSize(BOARD_WIDTH, BOARD_HEIGHT); 

        // Center the window on the screen (Optional but nice)
        frame.setLocationRelativeTo(null);

        // Ensure the program exits when the window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Make the window visible
        frame.setVisible(true);

        // --- CODE TO START GAME LISTENER/THREAD ---
        // STUDENTS: If you need to focus the panel for keyboard input, 
        // you might call gamePanel.requestFocusInWindow(); here.
        gamePanel.requestFocusInWindow();
    }
}