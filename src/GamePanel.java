// GamePanel.java
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.Timer;

/**
 * The GamePanel handles all the game logic, drawing, and user input.
 * It is the heart of the Snake game.
 */
public class GamePanel extends JPanel implements ActionListener, KeyListener {

    // --- 1. VARIABLES ---
    // Use the constants defined in SnakeGame for dimensions
    //private final int BOARD_UNITS; // Total number of game units/squares on the board
    private final int UNIT_SIZE;   // Size of a single game unit (e.g., 25 pixels)
    
    // Arrays/Collections to hold the snake's body segments
    // STUDENTS: You must use an ArrayList of your custom 'Point' Objects here.
    private Snake snake;
    
    // Variables for food location and properties
    private Point food; 
    
    // Game state variables
    private boolean isRunning;
    private int score;
    private boolean isPaused;
    private boolean gameEnded;
    private boolean showGrid;
    
    // Timer for the game loop
    private Timer timer; 
    private final int DELAY = 200; // Time delay (in ms) for the game speed


    // --- 2. OBJECTS AND CLASSES: Constructor ---
    public GamePanel() {
        // Initialize constants
        UNIT_SIZE = SnakeGame.UNIT_SIZE;
        //BOARD_UNITS = (SnakeGame.BOARD_WIDTH * SnakeGame.BOARD_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);

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

        //initialized snake with constructor, default starts in the middle left of screen and faces towards the right
        snake = new Snake();
        isRunning = true;
        isPaused = false;
        gameEnded = false;
        showGrid = true;
        placeFood(); //initial food placed randomly

        //timer initialization, starts the game
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
        int randomX;
        int randomY;
        boolean overlap;

        //keep generating random coordinates until we find a valid location
        do {
            randomX = rand.nextInt(20);
            randomY = rand.nextInt(20);

            //check if these random coordinates overlap with any part of the snake
            overlap = snake.checkOverlap(new Point(randomX, randomY));

        } while (overlap);

        //at this point, we have valid coordinates that don't overlap with the snake
        food = new Point(randomX, randomY);
    }


    // --- 5. USER-DEFINED METHOD: moveSnake() ---
    /** Calculates the new head position and updates the snake body. */
    public void moveSnake(boolean food) {
        // STUDENTS:
        // 1. Get the current head of the snake (the first element in the ArrayList).
        Point head = new Point(snake.get(0));
        // 2. Calculate the new x and y coordinates based on the 'direction' variable (U/D/L/R).
        if (snake.getDirection() == 'U') {head.y -= 1;}
        if (snake.getDirection() == 'D') {head.y += 1;}
        if (snake.getDirection() == 'R') {head.x += 1;}
        if (snake.getDirection() == 'L') {head.x -= 1;}
        // 3. Create a new 'Point' object for the new head.
        // 4. Add the new head to the front of the snake ArrayList.
        snake.add(0, head);
        // 5. REMOVE the tail segment (the last element) to simulate movement.
        //only removes the tail if food is not true, meaning whenever food is acquired on the movement
        //the snake will not lose its tail, effectively increasing its length by 1
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
        if (snake.checkHead(food)) { //comparing snake head to food
            score++; //score incrementation
            placeFood(); //placeFood() call
            //as score increases, slowly decrease the delay, therefore increasing the speed and difficulty
            timer.setDelay(DELAY - (score * 2));

            //by returning a boolean, this method can now be called inside of the moveSnake() method
            //which then determines if the snake should discard it's tail or not
            //true when food is acquired
            return true;
        }
        //false when food is not acquired
        return false;
    }


    // --- 7. USER-DEFINED METHOD: checkCollisions() ---
    /** Checks for wall and self-collision. */
    public void checkCollisions() {
        // STUDENTS:
        // 1. Check for WALL COLLISION: Is the head's x or y coordinate outside the board boundaries?
        Point head = new Point(snake.get(0));
        if (head.x < 0 || head.x >= 20 || head.y < 0 || head.y >= 20) {
            isRunning = false;
        }

        // 2. Check for SELF-COLLISION: Is the head's coordinate the same as any body segment? 
        //    (Requires a LOOP to iterate through the body segments).
        // 3. If any collision occurs, set isRunning to false.
        for (int i = 1; i < snake.size() - 1; i++) { //loop through each snake body segment, so start at 1 instead of 0
            if (snake.checkHead(snake.get(i))) { //if collision detected
                isRunning = false; //set isRunning to false
                break; //exit loop since no need to keep looping when game will end regardless
            }
        }

        // When not running, stop the timer.
        if (!isRunning) {
            // STUDENTS: Stop the timer object.
            gameEnded = true;
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
            if (showGrid) {
                g.setColor(Color.BLUE); //set color to blue
                for (int i = 0; i <= getWidth() / UNIT_SIZE; i++) { //loop through width at even intervals
                    g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, getHeight()); //draw lines spanning vertically
                }
                for (int i = 0; i <= getHeight() / UNIT_SIZE; i++) { //loop through length at even intervals
                    g.drawLine(0, i * UNIT_SIZE, getWidth(), i * UNIT_SIZE); //draw lines spanning horizontally
                }
            }

            // 2. Draw the Food (set color, use g.fillOval or g.fillRect).
            g.setColor(Color.RED); //set color to red

            //draw circle at the food position on the grid, scaled properly
            g.fillOval(food.x * UNIT_SIZE, food.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);

            // 3. Draw the Snake (requires a LOOP to iterate through the snake ArrayList).
            for (int i = 0; i < snake.size(); i++) { //loop through entire snake arraylist
                //if at the snake head, make color green
                if (i == 0) {
                    g.setColor(Color.GREEN); //head color
                }
                //otherwise, change to a slightly darker colored green
                else {
                    g.setColor(new Color(45, 180, 0)); //body color
                }
                Point p = new Point(snake.get(i)); //initialize new point for each snake section

                //fill a rectangle that is the same size as the grid, at the grid location of the given snake point
                g.fillRect(p.x * UNIT_SIZE, p.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }

            // 4. Draw the Score.
            g.setColor(Color.WHITE); //set color to white
            g.drawString("Score: " + score, 10, 20); //simple scoreboard in top left corner of screen
        }
        else if (isPaused) {
            g.setColor(new Color(0, 0, 0, 150)); //translucent background
            //draw large paused on the screen
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("PAUSED", getWidth() / 2 - 100, getHeight() / 2);
            //give instructions on how to resume
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Press P to resume", getWidth() / 2 - 95, getHeight() / 2 + 40);
        }
        else {
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
        g.setColor(Color.RED); //set color to red
        g.setFont(new Font("Arial", Font.BOLD, 36)); //larger font size for game over, also bolded
        g.drawString("Game Over", getWidth() / 2 - 100, getHeight() / 2 - 30); //roughly centered game over
        g.drawString("Press Space to Restart", 60, getHeight() / 2 + 20); //slightly below the game over

        g.setColor(Color.WHITE); //set color to white
        g.setFont(new Font("Arial", Font.PLAIN, 20)); //resize font, remove bolding
        g.drawString("Score: " + score, getWidth() / 2 - 40, getHeight() / 2 + 50); //display final score
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

            //combined moveSnake() and checkFood() into one statement, more intuitive code flow for myself
            //essentially checkFood() returns a boolean that determines inside of the moveSnake() method
            //whether or not to decrease the snake's length when it moves
            moveSnake(checkFood());
            checkCollisions(); //make sure snake isn't inside itself or outside the game area
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
            case KeyEvent.VK_A:
                if (snake.getDirection() != 'R') {
                    snake.setDirection('L'); //only allow left movement if not already going right
                }
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                if (snake.getDirection() != 'L') {
                    snake.setDirection('R'); //only allow right movement if not already going left
                }
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                if (snake.getDirection() != 'D') {
                    snake.setDirection('U'); //only allow up movement if not already going down
                }
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                if (snake.getDirection() != 'U') {
                    snake.setDirection('D'); //only allow down movement if not already going up
                }
                break;
            case KeyEvent.VK_SPACE: //when space is pressed, restart the game only if the game is not running
                if (!isRunning && gameEnded) {
                    timer.stop();
                    startGame();
                    score = 0;
                }
                break;
            case KeyEvent.VK_P:
                //make sure game is not ended before pausing because doesn't make sense to pause,
                //also bugs out other logic if pause is allowed to be pressed while the game is ended
                if (!gameEnded) {
                    isRunning = !isRunning; //reverse isRunning state, if game is paused, don't keep the game running
                    isPaused = !isPaused; //reverse isPaused state
                }
                repaint(); //trigger repaint early to update screen immediately
                break;
            case KeyEvent.VK_X: //when x is pressed, terminate the program
                System.exit(0);
                break; //not really needed since the program is terminated right above it, but oh well
            case KeyEvent.VK_G: //when g is pressed, toggle whether grid shows or not
                showGrid = !showGrid; //flip showGrid boolean
                repaint(); //trigger repaint early to update grid visibility on the spot
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

/**
 * Objectified version of the snake logic used for the game, allows for methods to be used in place of direct
 * manipulation of the snake and its variables.
 *
 * @author Ethan Lally
 */
class Snake {
    private ArrayList<Point> snake;
    private char direction;

    /**
     * Default constructor for the snake class, sets up the snake in the default position,
     * facing towards the right in the middle left of the screen.
     */
    public Snake() {
        snake = new ArrayList<>();
        snake.add(new Point(3,9));
        snake.add(new Point(2,9));
        snake.add(new Point(1,9));
        direction = 'R';
    }

    /**
     * Returns the direction the snake is currently facing.
     *
     * @return char representing snake direction
     */
    public char getDirection() {return direction;}

    /**
     * Sets the direction the snake will face.
     *
     * @param d char to set direction
     */
    public void setDirection(char d) {direction = d;}

    /**
     * Wrapper for the ArrayList get method.
     *
     * @param i index of the point to return
     * @return Point object stored at index i
     * @throws IndexOutOfBoundsException if index out of bounds of Snake's ArrayList
     */
    public Point get(int i) {
        if (i < 0 || i >= snake.size()) {
            throw new IndexOutOfBoundsException(); //honestly just wanted to do this for fun, it will never happen
        }
        return snake.get(i);
    }

    /**
     * Wrapper for the ArrayList add method.
     *
     * @param i index to insert the Point p
     * @param p Point to be added at index i
     */
    public void add(int i, Point p) {snake.add(i, p);}

    /**
     * Wrapper for ArrayList size method.
     *
     * @return int representing the length of the snake
     */
    public int size() {return snake.size();}

    /**
     * Wrapper for the ArrayList remove method.
     *
     * @param i index to remove from snake
     * @return Point representing point which was just removed
     */
    public Point remove(int i) {return snake.remove(i);}

    /**
     * Loops through the snake and checks if a given point overlaps any of the snake's points.
     *
     * @param p the Point to check overlap with the snake
     * @return true if the point does overlap the snake, false otherwise
     */
    public boolean checkOverlap(Point p) {
        boolean overlap = false;
        for (Point in : snake) {
            if (in.equals(p)) {
                overlap = true;
                break;
            }
        }
        return overlap;
    }

    /**
     * Checks the head of the snake is equal to the given point.
     *
     * @param p the Point to check overlap with the head of the snake
     * @return true if the point overlaps the head, false otherwise
     */
    public boolean checkHead(Point p) {return snake.get(0).equals(p);}
}