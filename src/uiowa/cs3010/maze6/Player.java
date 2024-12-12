package uiowa.cs3010.maze6;

public class Player {
    private int x, y;

    // Constructor to initialize player's starting position
    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    // Getter for x-coordinate
    public int getX() {
        return x;
    }

    // Getter for y-coordinate
    public int getY() {
        return y;
    }

    // Move method to update the player's position
    // dx and dy represent changes in x and y directions
    // The Maze object checks if the move is valid
    public void move(int dx, int dy, Maze maze) {
        int newX = x + dx;
        int newY = y + dy;

        // Only update position if the new coordinates are valid
        if (maze.isValidMove(newX, newY)) {
            this.x = newX;
            this.y = newY;
            SoundManager.playSound("sounds/footstep.wav"); // Trigger footstep sound
            maze.updateFogOfWar(getX(), getY());
        } else {
            SoundManager.playSound("sounds/wall_hit.wav"); // Trigger wall hit sound
        }
    }
}
