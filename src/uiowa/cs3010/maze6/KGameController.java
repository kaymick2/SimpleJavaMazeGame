package uiowa.cs3010.maze6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

// KMain class to control the KMaze game
public class KGameController extends JFrame {
    private static final int DEFAULT_CELL_SIZE = 52; // Default size of each cell in the KMaze
    private static final int GAME_TIME_LIMIT = 13; // Time limit for the game in seconds
    private final int cellSize; // Size of each cell in the KMaze
    private final KMaze KMaze; // The KMaze object
    private final KPlayer KPlayer; // The KPlayer object
    private boolean gameRunning = true; // Flag to indicate if the game is running
    private final Timer gameTimer; // Timer for the game
    private int timeRemaining = GAME_TIME_LIMIT; // Time remaining in the game
    private final JLabel statusLabel; // Label to display the time remaining
    private List<KNode> solutionPath; // List to store the solution path
    private final MazePanel mazePanel; // Panel to display the KMaze

    // Constructor to initialize the game controller
    public KGameController() {
        KMaze = new KMaze(20, 20); // Create a new KMaze with specified size
        KPlayer = new KPlayer(KMaze); // Create a new KPlayer in the KMaze
        setTitle("KMaze Game"); // Set the title of the window

        // Calculate the cell size based on the screen size and KMaze dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxCellSizeWidth = (screenSize.width - 100) / KMaze.getWidth();
        int maxCellSizeHeight = (screenSize.height - 100) / KMaze.getHeight();
        cellSize = Math.min(DEFAULT_CELL_SIZE, Math.min(maxCellSizeWidth, maxCellSizeHeight));

        // Set the size of the window
        setSize(KMaze.getWidth() * cellSize, KMaze.getHeight() * cellSize + 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set the default close operation
        setLocationRelativeTo(null); // Center the window on the screen

        // Initialize and add the KMaze panel
        mazePanel = new MazePanel();
        mazePanel.setPreferredSize(new Dimension(KMaze.getWidth() * cellSize, KMaze.getHeight() * cellSize));
        add(mazePanel, BorderLayout.CENTER);

        // Add key listener for KPlayer movement and solution path display
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameRunning) {
                    if (e.getKeyCode() == KeyEvent.VK_O) {
                        solutionPath = KMaze.findSolutionPath(KPlayer.getX(), KPlayer.getY(), KMaze.getExitPosition());
                        mazePanel.repaint();
                    } else {
                        handlePlayerMovement(e);
                    }
                }
            }
        });

        // Initialize and add the status label
        statusLabel = new JLabel("time left " + timeRemaining + " S");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusLabel, BorderLayout.NORTH);

        // Initialize and start the game timer
        gameTimer = new Timer(500, e -> {
            if (gameRunning) {
                timeRemaining--;
                statusLabel.setText("time left " + timeRemaining + " S");

                if (timeRemaining <= 0) {
                    endGame("womp womp game over");
                }
            }
        });
        gameTimer.start();
    }

    // Handle KPlayer movement based on key press
    private void handlePlayerMovement(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> KPlayer.move("w"); // Move up
            case KeyEvent.VK_S -> KPlayer.move("s"); // Move down
            case KeyEvent.VK_A -> KPlayer.move("a"); // Move left
            case KeyEvent.VK_D -> KPlayer.move("d"); // Move right
        }
        checkForExit();
        mazePanel.repaint();
    }

    // Check if the KPlayer has reached the exit
    private void checkForExit() {
        KNode exitPosition = KMaze.getExitPosition();
        if (KPlayer.getX() == exitPosition.getX() && KPlayer.getY() == exitPosition.getY()) {
            endGame("you didn't lose!");
        }
    }

    // End the game and display a message
    private void endGame(String message) {
        gameRunning = false;
        gameTimer.stop();
        JOptionPane.showMessageDialog(this, message);
        dispose(); // Dispose of the JFrame
    }

    // Inner class for the KMaze panel
    private class MazePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawMaze(g);
            drawPlayer(g);
        }

        // Draw the KMaze grid
        private void drawMaze(Graphics g) {
            int playerX = KPlayer.getX();
            int playerY = KPlayer.getY();
            KNode exitPosition = KMaze.getExitPosition();

            for (int x = 0; x < KMaze.getWidth(); x++) {
                for (int y = 0; y < KMaze.getHeight(); y++) {
                    if (Math.abs(playerX - x) <= 100 && Math.abs(playerY - y) <= 100) {
                        if (x == exitPosition.getX() && y == exitPosition.getY()) {
                            g.setColor(Color.blue);
                        } else if (solutionPath != null && solutionPath.contains(new KNode(x, y))) {
                            g.setColor(Color.green);
                        } else if (KMaze.getGrid()[x][y] == 1) {
                            g.setColor(Color.pink);
                        } else {
                            g.setColor(Color.white);
                        }
                        g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                    } else {
                        g.setColor(Color.darkGray);
                        g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                    }
                }
            }
        }

        // Draw the KPlayer
        private void drawPlayer(Graphics g) {
            g.setColor(Color.blue);
            int playerX = KPlayer.getX() * cellSize + cellSize / 2;
            int playerY = KPlayer.getY() * cellSize + cellSize / 2;
            g.fillOval(playerX - cellSize / 4, playerY - cellSize / 4, cellSize / 2, cellSize / 2);
        }
    }

    // KMain method to start the game
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KGameController game = new KGameController();
            game.setVisible(true);
        });
    }
}