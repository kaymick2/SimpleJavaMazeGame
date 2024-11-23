package uiowa.cs3010.maze6;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

// Test class for the Maze class
class MazeTest {
    private Maze maze;
    private final int width = 30; // Width of the maze
    private final int height = 30; // Height of the maze

    // Set up method to initialize the maze before each test
    @BeforeEach
    void setUp() {
        maze = new Maze(width, height);
    }

    // Test to ensure that a path exists from the player spawn to the exit
    @Test
    void testPathsExist() {
        Node playerSpawn = maze.getPlayerSpawn();
        Node exit = maze.getExitPosition();

        List<Node> pathToExit = maze.findSolutionPath(playerSpawn.getX(), playerSpawn.getY(), exit);
        assertNotNull(pathToExit, "Maze should have a path from the player spawn to the exit.");
    }

    // Test to ensure that key positions (player spawn and exit) are paths
    @Test
    void testKeyPositionsArePaths() {
        Node playerSpawn = maze.getPlayerSpawn();
        Node exit = maze.getExitPosition();
        int[][] grid = maze.getGrid();

        assertEquals(0, grid[playerSpawn.getX()][playerSpawn.getY()], "Player spawn position should be a path.");
        assertEquals(0, grid[exit.getX()][exit.getY()], "Exit position should be a path.");
    }

    // Test to ensure that the maze contains walls
    @Test
    void doMazeHaveWall() {
        int[][] grid = maze.getGrid();
        boolean hasWalls = false;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (grid[x][y] == 1) {
                    hasWalls = true;
                    break;
                }
            }
            if (hasWalls) break;
        }

        assertTrue(hasWalls, "Maze should contain walls.");
    }

    // Test to ensure that valid positions are correctly identified
    @Test
    void testValidPositions() {
        assertTrue(maze.isValidPosition(0, 0), "Top-left corner should be a valid position.");
        assertTrue(maze.isValidPosition(width - 1, height - 1), "Bottom-right corner should be a valid position.");
        assertFalse(maze.isValidPosition(-1, -1), "Negative coordinates should be invalid.");
        assertFalse(maze.isValidPosition(width, height), "Coordinates outside the maze boundaries should be invalid.");
    }

    // Test to ensure that the exit position is correctly set
    @Test
    void testExitPosition() {
        Node exit = maze.getExitPosition();
        assertEquals(width - 1, exit.getX(), "Exit X coordinate should be at the right edge.");
        assertEquals(0, exit.getY(), "Exit Y coordinate should be at the top row.");
    }

    // Test to ensure that the player spawn position is correctly set
    @Test
    void testSpawnPositions() {
        Node playerSpawn = maze.getPlayerSpawn();

        assertEquals(0, playerSpawn.getX(), "Player spawn X coordinate should be at the left edge.");
        assertEquals(0, playerSpawn.getY(), "Player spawn Y coordinate should be at the top row.");
    }
}
