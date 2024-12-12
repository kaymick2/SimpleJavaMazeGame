package uiowa.cs3010.maze6;

import java.util.List;

public class Killer {
    private int x, y; // Coordinates of the killer in the maze

    // Constructor to initialize the killer's starting position
    public Killer(int startX, int startY) {
        this.x = startX; // Set the initial x-coordinate
        this.y = startY; // Set the initial y-coordinate
    }

    // Getter method for the x coordinate of the killer
    public int getX() {
        return x;
    }

    // Getter method for the y coordinate of the killer
    public int getY() {
        return y;
    }

    // Method to move the killer to a new position (newX, newY)
    public void move(int newX, int newY) {
        this.x = newX; // Update the x-coordinate
        this.y = newY; // Update the y-coordinate
    }

    // Method for the killer to chase the player
    public void chasePlayer(int playerX, int playerY, char[][] maze) {
        // Use A* algorithm to find the path from the killer to the player
        List<Node> path = AStarAlgorithm.findPath(x, y, playerX, playerY, maze);

        // If a valid path exists (size > 1 to ensure there's a next step to take)
        if (path != null && path.size() > 1) {
            Node nextMove = path.get(1); // Get the next step in the path
            move(nextMove.getX(), nextMove.getY()); // Move the killer to that position
            SoundManager.playSound("sounds/footstep.wav"); // Trigger killer move sound
        }
    }
}
