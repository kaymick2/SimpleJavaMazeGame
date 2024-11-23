
package uiowa.cs3010.maze6;

// Class representing the player in the maze
public class Player {
    private int x, y; // Player's current position
    private final Maze maze; // Reference to the maze

    // Constructor to initialize the player with the maze and set the spawn position
    public Player(Maze maze) {
        this.maze = maze;
        Node spawn = maze.getPlayerSpawn(); // Get the player's spawn position from the maze
        this.x = spawn.getX(); // Set the player's initial x-coordinate
        this.y = spawn.getY(); // Set the player's initial y-coordinate
    }

    // Method to move the player in a specified direction
    public void move(String direction) {
        int newX = x, newY = y; // Initialize new coordinates with the current position
        switch (direction.toLowerCase()) {
            case "w": newY--; break; // Move up
            case "s": newY++; break; // Move down
            case "a": newX--; break; // Move left
            case "d": newX++; break; // Move right
        }

        // Check if the new position is valid within the maze
        if (maze.isValidPosition(newX, newY)) {
            this.x = newX; // Update the player's x-coordinate
            this.y = newY; // Update the player's y-coordinate
        }
    }

    // Getter for the player's x-coordinate
    public int getX() { return x; }

    // Getter for the player's y-coordinate
    public int getY() { return y; }
}