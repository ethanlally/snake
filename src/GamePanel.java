// GamePanel.java
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.*;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer; // Swing Timer for the game loop

/**
 * The GamePanel handles all the game logic, drawing, and user input.
 * It is the heart of the Snake game.
 */
public class GamePanel extends JPanel implements ActionListener, KeyListener {

    // --- 1. VARIABLES ---
    // Use the constants defined in SnakeGame for dimensions
    private final int BOARD_UNITS; // Total number of game units/squares on the board
    private final int UNIT_SIZE;   // Size of a single game unit (e.g., 25 pixels)
    
    // Arrays/Collections to hold the snake's body segments
    // STUDENTS: You must use an ArrayList of your custom 'Point' Objects here.
    private ArrayList<Point> snake;
    
    // Variables for food location and properties
    private Point food; 
    
    // Game state variables
    private boolean isRunning;
    private char direction; // 'U', 'D', 'L', 'R'
    private int score;
    
    // Timer for the game loop
    private Timer timer; 
    private final int DELAY = 200; // Time delay (in ms) for the game speed


    // --- 2. OBJECTS AND CLASSES: Constructor ---
    public GamePanel() {
        // Initialize constants
        UNIT_SIZE = SnakeGame.UNIT_SIZE;
        BOARD_UNITS = (SnakeGame.BOARD_WIDTH * SnakeGame.BOARD_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);

        // Set up the panel properties
        this.setPreferredSize(new Dimension(SnakeGame.BOARD_WIDTH, SnakeGame.BOARD_HEIGHT));
        this.setBackground(new Color(15, 50, 100));
        this.setFocusable(true);
        // Add the KeyListener interface to this panel
        this.addKeyListener(this); 
        
        // Initialize the game
        startGame();
    }


    // --- 3. USER-DEFINED METHOD: startGame() ---
    /** Initializes the snake, food, and game state. */
    public void startGame() {
        // STUDENTS: 
        // 1. Initialize the snake ArrayList and add initial 'Point' segments (e.g., 3 segments).
        // 2. Set the initial direction (e.g., 'R').
        // 3. Set isRunning to true.
        // 4. Call a method to place the first food item (placeFood()).
        // 5. Initialize and start the game timer.
        snake = new ArrayList<Point>();
        snake.add(new Point(3,10));
        snake.add(new Point(2,10));
        snake.add(new Point(1,10));
        direction = 'R';
        isRunning = true;
        placeFood();
        timer = new Timer(DELAY, this);
        timer.start();
    }


    // --- 4. USER-DEFINED METHOD: placeFood() ---
    /** Generates a new random location for the food. */
    public void placeFood() {
        // STUDENTS:
        // 1. Use the Random class to generate random x and y coordinates.
        // 2. Ensure the food location is within the board boundaries and is NOT on the snake.
        // 3. Create a new 'Point' object for the food.
        Random rand = new Random();
        int randomX = rand.nextInt(20);
        int randomY = rand.nextInt(20);
        for (Point in : snake) {
            if (new Point(randomX, randomY).equals(in)) {
                placeFood();
            }
        }
        food = new Point(randomX, randomY);
    }


    // --- 5. USER-DEFINED METHOD: moveSnake() ---
    /** Calculates the new head position and updates the snake body. */
    public void moveSnake(boolean food) {
        // STUDENTS:
        // 1. Get the current head of the snake (the first element in the ArrayList).
        // 2. Calculate the new x and y coordinates based on the 'direction' variable (U/D/L/R).
        // 3. Create a new 'Point' object for the new head.
        // 4. Add the new head to the front of the snake ArrayList.
        // 5. REMOVE the tail segment (the last element) to simulate movement.
        Point head = new Point(snake.get(0));
        if (direction == 'U') {head.y -= 1;}
        if (direction == 'D') {head.y += 1;}
        if (direction == 'R') {head.x += 1;}
        if (direction == 'L') {head.x -= 1;}
        snake.add(0, head);
        if (!food) {
            snake.remove(snake.size() - 1);
        }
    }
    
    
    // --- 6. USER-DEFINED METHOD: checkFood() ---
    /** Checks if the snake has eaten the food. */
    public boolean checkFood() {
        // STUDENTS:
        // 1. Compare the snake's head coordinates with the food's coordinates.
        // 2. If they match:
        //    a. Increment the score.
        //    b. DO NOT remove the tail (this makes the snake grow).
        //    c. Call placeFood() to generate a new food location.
        if (snake.get(0).equals(food)) {
            score++;
            placeFood();
            return true;
        }
        return false;
    }


    // --- 7. USER-DEFINED METHOD: checkCollisions() ---
    /** Checks for wall and self-collision. */
    public void checkCollisions() {
        // STUDENTS:
        // 1. Check for WALL COLLISION: Is the head's x or y coordinate outside the board boundaries?
        // 2. Check for SELF-COLLISION: Is the head's coordinate the same as any body segment? 
        //    (Requires a LOOP to iterate through the body segments).
        // 3. If any collision occurs, set isRunning to false.

        Point head = new Point(snake.get(0));
        if (head.x < 0 || head.x >= 20 || head.y < 0 || head.y >= 20) {
            isRunning = false;
        }
        for (int i = 1; i < snake.size() - 1; i++) {
            if (head.equals(snake.get(i))) {
                isRunning = false;
                break;
            }
        }
        // When not running, stop the timer.
        if (!isRunning) {
            // STUDENTS: Stop the timer object.
            timer.stop();
        }
    }


    // --- 8. USER-DEFINED METHOD: paintComponent() (Graphics) ---
    /** Swing method for drawing components. DO NOT call this directly; use repaint(). */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g); // Delegate the drawing to a custom method
    }


    // --- 9. USER-DEFINED METHOD: draw(Graphics g) (Graphics) ---
    /** Draws the game elements (grid, food, snake, score). */
    public void draw(Graphics g) {
        if (isRunning) {
            // STUDENTS: 
            // 1. Optional: Draw the grid lines (requires a LOOP).
            g.setColor(Color.BLUE);
            for (int i = 0; i < getWidth() / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, getHeight());
            }
            for (int i = 0; i < getHeight() / UNIT_SIZE; i++) {
                g.drawLine(0, i * UNIT_SIZE, getWidth(), i * UNIT_SIZE);
            }
            // 2. Draw the Food (set color, use g.fillOval or g.fillRect).
            g.setColor(Color.RED);
            g.fillOval(food.x * UNIT_SIZE, food.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            // 3. Draw the Snake (requires a LOOP to iterate through the snake ArrayList).
            for (int i = 0; i < snake.size(); i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN); // head
                }
                else {
                    g.setColor(new Color(45, 180, 0)); // body
                }
                Point p = new Point(snake.get(i));
                g.fillRect(p.x * UNIT_SIZE, p.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }
            // 4. Draw the Score.
            g.setColor(Color.WHITE);
            g.drawString("Score: " + score, 10, 20);

        } else {
            // STUDENTS: Call the gameOver() method.
            gameOver(g);
        }
    }


    // --- 10. USER-DEFINED METHOD: gameOver(Graphics g) ---
    /** Displays the Game Over screen and final score. */
    public void gameOver(Graphics g) {
        // STUDENTS:
        // 1. Display the final score.
        // 2. Display the "Game Over" text in the center of the screen.
        // 3. (Optional) Display instructions to restart.
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("Game Over", getWidth() / 2 - 100, getHeight() / 2 - 30);
        g.drawString("Press Space to Restart", 60, getHeight() / 2 + 20);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: " + score, getWidth() / 2 - 40, getHeight() / 2 + 50);
    }

    
    // --- 11. REQUIRED METHOD: actionPerformed (Game Loop) ---
    /** This method is called repeatedly by the Timer. It is the game's update loop. */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (isRunning) {
            // STUDENTS: Call the core update methods in order:
            // 1. moveSnake();
            // 2. checkFood();
            // 3. checkCollisions();
            moveSnake(checkFood());
            checkCollisions();
        }
        
        // This line triggers the drawing process after every update.
        repaint(); 
    }


    // --- 12. REQUIRED METHODS: KeyListener (Keyboard Input) ---
    @Override
    public void keyPressed(KeyEvent e) {
        // STUDENTS: Use CONDITIONALS (if/else if/switch) to check which key was pressed 
        // and update the 'direction' variable (U, D, L, R).
        // IMPORTANT: Prevent the snake from reversing direction instantly.
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT: 
                if (direction != 'R') {
                    direction = 'L';
                }
                break;
            case KeyEvent.VK_RIGHT: 
                if (direction != 'L') {
                    direction = 'R';
                }
                break;
            case KeyEvent.VK_UP: 
                if (direction != 'D') {
                    direction = 'U';
                }
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') {
                    direction = 'D';
                }
                break;
        }
    }

    // Unused but required by the KeyListener interface
    @Override
    public void keyTyped(KeyEvent e) {}

    // Unused but required by the KeyListener interface
    @Override
    public void keyReleased(KeyEvent e) {}
}
