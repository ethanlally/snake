// Point.java

/**
 * A simple Object used to represent a coordinate (x, y) on the game board.
 * This class is used for the snake segments and the food location.
 */
public class Point {

    // --- 1. VARIABLES (Object Attributes) ---
    public int x;
    public int y;

    // --- 2. OBJECTS AND CLASSES: Constructor ---
    public Point(int x, int y) {
        // STUDENTS: Initialize the instance variables with the parameter values.
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a new Point with the same coordinates as the given Point. 
     * Helpful to avoid messing with an existing Point.
     *
     * @param p the Point to copy the coordinates from
     * @throws NullPointerException if {@code p} is null
     */
    public Point(Point p) {
        x = p.x;
        y = p.y;
    }

    // --- 3. USER-DEFINED METHOD: equals (Optional but highly recommended for checking collisions) ---
    /** * STUDENTS: An advanced optional method to check if two Point objects have the same coordinates. 
     * This is useful for checking if the snake head equals a body segment or the food.
     * 
     * @param obj the object to compare with
     * @return true if the coordinates are the same, false otherwise
     */

    public boolean equals(Object obj) {
        if (obj instanceof Point other) {
            return this.x == other.x && this.y == other.y;
        }
        return false;
    }
}