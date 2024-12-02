// Maze.java - Handles maze generation
import java.util.Random;
import java.util.Stack;

public class Maze {
    private int[][] grid;
    private int size;
    private static final int WALL = 1;
    private static final int PATH = 0;
    private Random random = new Random();

    // Constructor to initialize the maze with a minimum size of 50x50
    public Maze(int size) {
        this.size = Math.max(size, 20);  // Ensure the size is at least 50x50
        this.grid = new int[this.size][this.size];
        generateMaze();
    }

    // Method to generate maze using Recursive Backtracking
//    private void generateMaze() {
//        // Fill the grid with walls
//        for (int x = 0; x < size; x++) {
//            for (int y = 0; y < size; y++) {
//                grid[x][y] = WALL;
//            }
//        }
//
//        // Start maze generation at (1,1)
//        int startX = 2;
//        int startY = 2;
//        grid[startX][startY] = PATH;  // Mark the starting cell as a path
//
//        // Create a stack for backtracking
//        Stack<int[]> stack = new Stack<>();
//        stack.push(new int[]{startX, startY});
//
//        // Direction vectors: North, South, East, West
//        final int[][] directions = {{0, -2}, {0, 2}, {2, 0}, {-2, 0}};
//
//        // Recursive Backtracking algorithm
//        while (!stack.isEmpty()) {
//            int[] current = stack.peek();
//            int x = current[0];
//            int y = current[1];
//
//            // Shuffle directions to add randomness
//            int[][] shuffledDirections = shuffleDirections(directions);
//
//            boolean carved = false;
//            for (int[] direction : shuffledDirections) {
//                int newX = x + direction[0];
//                int newY = y + direction[1];
//
//                // Check if the new position is valid and unvisited
//                if (isValid(newX, newY)) {
//                    // Remove the wall between the current and new cell
//                    grid[x + direction[0] / 2][y + direction[1] / 2] = PATH;
//
//                    // Mark the new cell as a path
//                    grid[newX][newY] = PATH;
//
//                    // Push the new position onto the stack
//                    stack.push(new int[]{newX, newY});
//                    carved = true;
//                    break;
//                }
//            }
//
//            // If no adjacent unvisited cells, backtrack
//            if (!carved) {
//                stack.pop();
//            }
//        }
//    }
    private void generateMaze() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                grid[x][y] = WALL;
            }
        }
        int startX = 0, startY = 0;
        int exitX = size - 1, exitY = size - 1;
        grid[startX][startY] = PATH;
        grid[exitX][exitY] = PATH;
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startX, startY});
        final int[][] directions = {{0, -2}, {0, 2}, {2, 0}, {-2, 0}};
        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int x = current[0];
            int y = current[1];
            int[][] shuffledDirections = shuffleDirections(directions);
            boolean carved = false;
            for (int[] direction : shuffledDirections) {
                int newX = x + direction[0];
                int newY = y + direction[1];
                if (isValid(newX, newY)) {
                    grid[x + direction[0] / 2][y + direction[1] / 2] = PATH;
                    grid[newX][newY] = PATH;
                    stack.push(new int[]{newX, newY});
                    carved = true;
                    break;
                }
            }
            if (!carved) {
                stack.pop();
            }
        }
    }

    // Utility function to shuffle directions
    private int[][] shuffleDirections(int[][] directions) {
        int[][] shuffled = directions.clone();
        for (int i = 0; i < shuffled.length; i++) {
            int randomIndex = random.nextInt(shuffled.length);
            int[] temp = shuffled[i];
            shuffled[i] = shuffled[randomIndex];
            shuffled[randomIndex] = temp;
        }
        return shuffled;
    }

    // Check if a cell is valid for carving
    private boolean isValid(int x, int y) {
        return x > 0 && y > 0 && x < size - 1 && y < size - 1 && grid[x][y] == WALL;
    }

    // Getter method for the grid
    public int[][] getGrid() {
        return grid;
    }

    // Getter methods for constants
    public static int getWallValue() {
        return WALL;
    }

    public static int getPathValue() {
        return PATH;
    }
}
