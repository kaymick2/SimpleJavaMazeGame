// Player.java - Add fog of war implementation
public class Player {
    private int x, y;
    private Maze maze;
    private int visionRadius = 10;  // Limit vision to 1 square in each direction

    public Player(Maze maze, int startX, int startY) {
        this.maze = maze;
        this.x = startX;  // Start position
        this.y = startY;  // Start position
    }

    public void move(char direction) {
        // Store the potential new position
        int newX = x;
        int newY = y;

        // Move player based on input direction
        switch (direction) {
//            case 'W': newY++; break;  // Up
//            case 'S': newY--; break;  // Down
//            case 'A': newX--; break;  // Left
//            case 'D': newX++; break;  // Right
            case 'W': newX--; break;  // Up
            case 'S': newX++; break;  // Down
            case 'A': newY--; break;  // Left
            case 'D': newY++; break;  // Right
        }

        // Check boundaries and collisions
        if (newX >= 0 && newX < maze.getGrid().length && newY >= 0 && newY < maze.getGrid()[0].length) {
            if (maze.getGrid()[newX][newY] == 0) {  // Ensure the new position is a path
                x = newX;  // Update player's position
                y = newY;
            }
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }

    // Check if a cell is within the player's vision radius
    public boolean isInFog(int cellX, int cellY) {
        return Math.abs(x - cellX) > visionRadius || Math.abs(y - cellY) > visionRadius;
    }
}
