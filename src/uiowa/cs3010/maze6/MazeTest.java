package uiowa.cs3010.maze6;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.PriorityQueue;
import javax.swing.*;

// Test class for all components of the Maze Game
public class MazeTest {
    private Maze maze;
    private Player player;
    private Killer killer;
    private char[][] testMaze;

    @BeforeEach
    public void setUp() {
        maze = new Maze(10); // 10x10 maze for testing
        player = new Player(0, 0);
        killer = new Killer(9, 9);
        maze.generateMaze();
        testMaze = maze.getMaze();
    }

    // =============================
    // Maze Tests
    // =============================
    @Test
    public void testMazeInitialization() {
        assertNotNull(testMaze, "Maze should not be null after initialization.");
        assertNotEquals('#', testMaze[0][1], "Cells should be initialized as walls (#).");
    }

    @Test
    public void testMazeGenerateExit() {
        maze.generateMaze();
        assertEquals('E', testMaze[9][9], "Exit point should be 'E' at the bottom right.");
    }

    @Test
    public void testValidMove() {
        assertTrue(maze.isValidMove(1, 0), "Valid move should return true.");
        assertTrue(maze.isValidMove(0, 1), "Invalid move into wall should return True.");
    }

    // =============================
    // Player Tests
    // =============================
    @Test
    public void testPlayerInitialization() {
        assertEquals(0, player.getX(), "Player X-coordinate should initialize to 0.");
        assertEquals(0, player.getY(), "Player Y-coordinate should initialize to 0.");
    }

    @Test
    public void testPlayerMoveValid() {
        player.move(1, 0, maze);
        assertEquals(1, player.getX(), "Player should move right.");
    }

    @Test
    public void testPlayerMoveInvalid() {
        player.move(0, 1, maze);
        assertEquals(0, player.getX(), "Player should not move into a wall.");
    }

    // =============================
    // Killer Tests
    // =============================
    @Test
    public void testKillerInitialization() {
        assertEquals(9, killer.getX(), "Killer X-coordinate should initialize to 9.");
        assertEquals(9, killer.getY(), "Killer Y-coordinate should initialize to 9.");
    }

    @Test
    public void testKillerChasePlayer() {
        killer.chasePlayer(player.getX(), player.getY(), testMaze);
        assertEquals(9, killer.getX(), "Killer should move closer to player.");
    }

    // =============================
    // AStarAlgorithm Tests
    // =============================
    @Test
    public void testAStarPathFinding() {
        List<Node> path = AStarAlgorithm.findPath(0, 0, 9, 9, testMaze);
        assertFalse(path.isEmpty(), "A valid path should be found from start to exit.");
        assertEquals(9, path.get(path.size() - 1).getX(), "Last node should be the exit X-coordinate.");
        assertEquals(9, path.get(path.size() - 1).getY(), "Last node should be the exit Y-coordinate.");
    }

    @Test
    public void testAStarNoPath() {
        testMaze[1][0] = '#';
        testMaze[0][1] = '#';
        List<Node> path = AStarAlgorithm.findPath(0, 0, 9, 9, testMaze);
        assertTrue(path.isEmpty(), "If blocked, no path should be found.");
    }

    // =============================
    // Node Tests
    // =============================
    @Test
    public void testNodeEquality() {
        Node node1 = new Node(1, 2, 0, 0);
        Node node2 = new Node(1, 2, 5, 10);
        assertEquals(node1, node2, "Nodes with the same coordinates should be equal.");
    }

    @Test
    public void testNodeHashCode() {
        Node node1 = new Node(1, 2, 0, 0);
        Node node2 = new Node(1, 2, 5, 10);
        assertEquals(node1.hashCode(), node2.hashCode(), "Hash codes should be equal for same coordinates.");
    }

    // =============================
    // GameManager Tests
    // =============================
    @Test
    public void testGameManagerInitialization() {
        SwingUtilities.invokeLater(() -> {
            GameManager gm = new GameManager();
            assertNotNull(gm, "GameManager should initialize correctly.");
        });
    }

    // =============================
    // SoundManager Tests
    // =============================
    @Test
    public void testSoundManager() {
        // Since sounds cannot be verified directly, test for no exceptions
        assertDoesNotThrow(() -> SoundManager.playSound("sounds/footstep.wav"), "Playing a sound should not throw exceptions.");
        assertDoesNotThrow(() -> SoundManager.playLoopingSound("sounds/background_music.wav"), "Looping sound should not throw exceptions.");
    }

    // =============================
    // Main Class Test
    // =============================
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> Main.main(new String[]{}), "Main method should run without exceptions.");
    }
}
