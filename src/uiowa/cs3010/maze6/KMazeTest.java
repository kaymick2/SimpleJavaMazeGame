package uiowa.cs3010.maze6;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

// Test class for the KMaze class
class KMazeTest {
    private KMaze KMaze;
    private final int width = 30; // Width of the KMaze
    private final int height = 30; // Height of the KMaze

    // Set up method to initialize the KMaze before each test
    @BeforeEach
    void setUp() {
        KMaze = new KMaze(width, height);
    }

    // Test to ensure that a path exists from the player spawn to the exit
    @Test
    void testPathsExist() {
        KNode playerSpawn = KMaze.getPlayerSpawn();
        KNode exit = KMaze.getExitPosition();

        List<KNode> pathToExit = KMaze.findSolutionPath(playerSpawn.getX(), playerSpawn.getY(), exit);
        assertNotNull(pathToExit, "KMaze should have a path from the player spawn to the exit.");
    }

    // Test to ensure that key positions (player spawn and exit) are paths
    @Test
    void testKeyPositionsArePaths() {
        KNode playerSpawn = KMaze.getPlayerSpawn();
        KNode exit = KMaze.getExitPosition();
        int[][] grid = KMaze.getGrid();

        assertEquals(0, grid[playerSpawn.getX()][playerSpawn.getY()], "KPlayer spawn position should be a path.");
        assertEquals(0, grid[exit.getX()][exit.getY()], "Exit position should be a path.");
    }

    // Test to ensure that the KMaze contains walls
    @Test
    void doMazeHaveWall() {
        int[][] grid = KMaze.getGrid();
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

        assertTrue(hasWalls, "KMaze should contain walls.");
    }

    // Test to ensure that valid positions are correctly identified
    @Test
    void testValidPositions() {
        assertTrue(KMaze.isValidPosition(0, 0), "Top-left corner should be a valid position.");
        assertTrue(KMaze.isValidPosition(width - 1, height - 1), "Bottom-right corner should be a valid position.");
        assertFalse(KMaze.isValidPosition(-1, -1), "Negative coordinates should be invalid.");
        assertFalse(KMaze.isValidPosition(width, height), "Coordinates outside the KMaze boundaries should be invalid.");
    }

    // Test to ensure that the exit position is correctly set
    @Test
    void testExitPosition() {
        KNode exit = KMaze.getExitPosition();
        assertEquals(width - 1, exit.getX(), "Exit X coordinate should be at the right edge.");
        assertEquals(0, exit.getY(), "Exit Y coordinate should be at the top row.");
    }

    // Test to ensure that the player spawn position is correctly set
    @Test
    void testSpawnPositions() {
        KNode playerSpawn = KMaze.getPlayerSpawn();

        assertEquals(0, playerSpawn.getX(), "KPlayer spawn X coordinate should be at the left edge.");
        assertEquals(0, playerSpawn.getY(), "KPlayer spawn Y coordinate should be at the top row.");
    }
}
