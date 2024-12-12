package uiowa.cs3010.maze6;

import java.util.*;

public class Maze {
    private char[][] maze; // 2D array representing the maze layout
    private boolean[][] fogOfWar; // 2D array for fog of war visibility tracking
    private int mazeSize; // Size of the maze (assuming a square maze)
    private boolean isFogOfWarEnabled; // Flag to control the visibility of fog of war

    // Constructor to initialize the maze with a given size
    public Maze(int size) {
        this.mazeSize = size;
        this.maze = new char[mazeSize][mazeSize]; // Initialize maze array
        this.fogOfWar = new boolean[mazeSize][mazeSize]; // Initialize fog of war array
        this.isFogOfWarEnabled = true; // Fog of war is enabled by default
        initializeMaze(); // Initialize the maze structure with walls
    }

    // Method to initialize the maze with walls and clear the fog of war
    private void initializeMaze() {
        for (int i = 0; i < mazeSize; i++) {
            for (int j = 0; j < mazeSize; j++) {
                maze[i][j] = '#'; // Initialize maze with walls
                fogOfWar[i][j] = false; // Initially, no fog of war (everything is visible)
            }
        }
    }

    // Method to generate the maze using a randomized depth-first search (DFS) algorithm
    public void generateMaze() {
        Random random = new Random(); // Random object for shuffling neighbors
        Stack<int[]> stack = new Stack<>(); // Stack to store the current path during DFS
        int startX = 0, startY = 0; // Starting point of the maze
        maze[startX][startY] = ' '; // Make the starting point open (empty space)
        stack.push(new int[] {startX, startY}); // Push the start position onto the stack

        // Depth-first search loop to carve the maze
        while (!stack.isEmpty()) {
            int[] current = stack.pop(); // Pop the current position from the stack
            int x = current[0], y = current[1];

            // Get shuffled neighboring cells to explore
            List<int[]> neighbors = getNeighbors(x, y);
            Collections.shuffle(neighbors, random);

            // Loop through each neighbor and carve paths
            for (int[] neighbor : neighbors) {
                int nx = neighbor[0], ny = neighbor[1];
                int mx = (x + nx) / 2, my = (y + ny) / 2; // Middle point to carve a path

                // If the neighbor is a wall, carve a path to it and push it onto the stack
                if (maze[nx][ny] == '#') {
                    maze[nx][ny] = ' '; // Open up the neighbor cell
                    maze[mx][my] = ' '; // Open up the middle cell to connect the two
                    stack.push(new int[] {nx, ny});
                }
            }
        }

        // Ensure both the start and end points are open and visible
        maze[0][0] = ' '; // Ensure start point is open
        maze[mazeSize - 1][mazeSize - 1] = 'E'; // Ensure end point is open and visible

        // Ensure that both the player and killer can reach the exit
        ensureReachability(0, 0, mazeSize - 1, mazeSize - 1);
        ensureReachability(mazeSize - 1, 0, mazeSize - 1, mazeSize - 1);
    }

    // Method to get valid neighboring cells for maze generation
    private List<int[]> getNeighbors(int x, int y) {
        List<int[]> neighbors = new ArrayList<>();

        // Add neighboring cells in all four directions if they are valid
        if (x > 1) neighbors.add(new int[] {x - 2, y});
        if (x < mazeSize - 2) neighbors.add(new int[] {x + 2, y});
        if (y > 1) neighbors.add(new int[] {x, y - 2});
        if (y < mazeSize - 2) neighbors.add(new int[] {x, y + 2});

        return neighbors;
    }

    // Method to ensure a valid path exists between two points in the maze
    private void ensureReachability(int startX, int startY, int goalX, int goalY) {
        if (AStarAlgorithm.findPath(startX, startY, goalX, goalY, maze).isEmpty()) {
            // If no path exists, create a path between the two points
            createPath(startX, startY, goalX, goalY);
        }
    }

    // Method to manually create a path between two points in the maze
    private void createPath(int startX, int startY, int goalX, int goalY) {
        int x = startX, y = startY;
        // Keep moving towards the goal until it is reached
        while (x != goalX || y != goalY) {
            if (x < goalX) x++;
            else if (x > goalX) x--;
            else if (y < goalY) y++;
            else if (y > goalY) y--;
            maze[x][y] = ' '; // Make the current position part of the path
        }
    }

    // Method to get the current maze layout
    public char[][] getMaze() {
        return maze;
    }

    // Method to get the current fog of war layout
    public boolean[][] getFogOfWar() {
        return fogOfWar;
    }

    // Method to update the fog of war based on the player's position
    public void updateFogOfWar(int playerX, int playerY) {
        if (!isFogOfWarEnabled) {
            // If fog of war is disabled, clear the fog
            for (int i = 0; i < mazeSize; i++) {
                for (int j = 0; j < mazeSize; j++) {
                    fogOfWar[i][j] = false;
                }
            }
            return; // Skip further processing
        }

        // If fog of war is enabled, apply fog to neighboring cells
        for (int i = 0; i < mazeSize; i++) {
            for (int j = 0; j < mazeSize; j++) {
                fogOfWar[i][j] = false; // Initially, everything is covered by fog
            }
        }

        // Reveal surrounding cells based on the player's position
        fogOfWar[playerX][playerY] = true;
        if (playerX > 0) fogOfWar[playerX - 1][playerY] = true;
        if (playerX < mazeSize - 1) fogOfWar[playerX + 1][playerY] = true;
        if (playerY > 0) fogOfWar[playerX][playerY - 1] = true;
        if (playerY < mazeSize - 1) fogOfWar[playerX][playerY + 1] = true;
//        fogOfWar[mazeSize - 1][mazeSize - 1] = true; // Ensure exit is always visible
    }

    // Method to check if a move is valid (i.e., not a wall)
    public boolean isValidMove(int x, int y) {
        return x >= 0 && x < mazeSize && y >= 0 && y < mazeSize && maze[x][y] != '#';
    }


    //In-progress method, intended for debugging purposes. Essentially cheat ability to see maze without fog
    // Method to toggle the fog of war (enable/disable it)
    public void toggleFogOfWar() {
        isFogOfWarEnabled = !isFogOfWarEnabled;
    }
}
