//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//
//public class GameController extends JFrame {
//    private static final int CELL_SIZE = 20;  // Size of each cell in pixels
//    private Maze maze;
//    private Player player;
//    private Killer killer;
//    private Bystander bystander1, bystander2;
//    private int turnLimit = 250;
//    private boolean gameRunning = true;
//    private JTextArea dialogueBox;  // Added running dialogue
//    private JButton restartButton;  // Added restart button
//
//    public GameController() {
//        maze = new Maze(20);  // Create a 20x20 maze
//        initializeGame();
//
//        // Set up the JFrame
//        setTitle("Murder Mystery Maze Game");
//        setLayout(new BorderLayout());
//        setSize(maze.getGrid().length * CELL_SIZE + 200, maze.getGrid()[0].length * CELL_SIZE + 100);  // Adjust size for dialogue box and centering
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        // Center grid in the middle
//        JPanel gamePanel = new JPanel() {
//            @Override
//            public void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                drawMaze(g);
//            }
//        };
//        gamePanel.setPreferredSize(new Dimension(maze.getGrid().length * CELL_SIZE, maze.getGrid()[0].length * CELL_SIZE));
//        add(gamePanel, BorderLayout.CENTER);
//
//        // Add a running dialogue box
//        dialogueBox = new JTextArea(5, 20);
//        dialogueBox.setEditable(false);
//        JScrollPane scrollPane = new JScrollPane(dialogueBox);
//        add(scrollPane, BorderLayout.SOUTH);
//
//        // Add a restart button
//        restartButton = new JButton("Restart Game");
//        restartButton.addActionListener(e -> restartGame());
//        add(restartButton, BorderLayout.NORTH);
//
//        // Add Key Listener to capture player movement
//        addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                if (gameRunning) {
//                    handlePlayerMove(e);
//                }
//            }
//        });
//
//        pack();
//    }
//
//    // Initialize the game objects
//    private void initializeGame() {
//        int startX = 0;
//        int startY = 0;
//
//        // Ensure player starts on a valid path
//        while (maze.getGrid()[startX][startY] == Maze.getWallValue()) {
//            startX = (int) (Math.random() * maze.getGrid().length);
//            startY = (int) (Math.random() * maze.getGrid()[0].length);
//        }
//
//        player = new Player(maze, startX, startY);
//        killer = new Killer(maze, player);
//        bystander1 = new Bystander(maze);
//        bystander2 = new Bystander(maze);
//    }
//
//    // Handle player movement
//    private void handlePlayerMove(KeyEvent e) {
//        switch (e.getKeyCode()) {
//            case KeyEvent.VK_W:  // Up
//            case KeyEvent.VK_UP:
//                player.move('W');
//                break;
//            case KeyEvent.VK_S:  // Down
//            case KeyEvent.VK_DOWN:
//                player.move('S');
//                break;
//            case KeyEvent.VK_A:  // Left
//            case KeyEvent.VK_LEFT:
//                player.move('A');
//                break;
//            case KeyEvent.VK_D:  // Right
//            case KeyEvent.VK_RIGHT:
//                player.move('D');
//                break;
//        }
//
//        // Killer moves after the player
//        killer.move();
//        bystander1.rescue(player, killer);
//        bystander2.rescue(player, killer);
//        turnLimit--;
//
//        killer.displayProximityAlert();
//        updateDialogueBox(killer.getX(), killer.getY());
//
//        checkGameStatus();
//        repaint();
//    }
//
//    // Update dialogue box with proximity alert
//    private void updateDialogueBox(int killerX, int killerY) {
//        double distance = Math.sqrt(Math.pow(killerX - player.getX(), 2) + Math.pow(killerY - player.getY(), 2));
//        if (distance <= 1) {
//            dialogueBox.append("The sound of breathing grows louder.\n");
//        } else if (distance <= 2) {
//            dialogueBox.append("A door creaks open nearby.\n");
//        } else if (distance <= 3) {
//            dialogueBox.append("You hear faint footsteps behind you.\n");
//        } else {
//            dialogueBox.append("All is quiet.\n");
//        }
//    }
//
//    // Check win/lose conditions
//    private void checkGameStatus() {
//        int exitX = maze.getGrid().length - 1;  // Exit at bottom-right
//        int exitY = maze.getGrid()[0].length - 1;
//
//        // Check win condition
//        if (player.getX() == exitX && player.getY() == exitY) {
//            JOptionPane.showMessageDialog(this, "Congratulations! You escaped the maze.");
//            gameRunning = false;
//        }
//
//        // Check lose condition
//        if (player.getX() == killer.getX() && player.getY() == killer.getY()) {
//            JOptionPane.showMessageDialog(this, "You were caught by the killer! Game Over.");
//            gameRunning = false;
//        }
//
//        // Check if time runs out
//        if (turnLimit == 0) {
//            JOptionPane.showMessageDialog(this, "Time's up! The killer caught you.");
//            gameRunning = false;
//        }
//    }
//
//    // Restart the game
//    private void restartGame() {
//        gameRunning = true;
//        dialogueBox.setText("");
//        turnLimit = 250;
//        maze = new Maze(20);  // Reset the maze
//        initializeGame();
//        repaint();
//    }
//
//    // Draw the maze, player, killer, and bystanders
//    private void drawMaze(Graphics g) {
//        int[][] grid = maze.getGrid();
//
//        for (int i = 0; i < grid.length; i++) {
//            for (int j = 0; j < grid[i].length; j++) {
//                int x = j * CELL_SIZE;
//                int y = i * CELL_SIZE;
//
//                if (grid[i][j] == Maze.getWallValue()) {
//                    g.setColor(Color.BLACK);
//                } else {
//                    g.setColor(Color.WHITE);
//                }
//
//                g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
//                g.setColor(Color.GRAY);
//                g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
//            }
//        }
//
//        // Draw player
//        g.setColor(Color.BLUE);
//        g.fillRect(player.getY() * CELL_SIZE, player.getX() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
//
//        // Draw killer
//        g.setColor(Color.RED);
//        g.fillRect(killer.getY() * CELL_SIZE, killer.getX() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
//
//        // Draw bystanders
//        g.setColor(Color.GREEN);
//        g.fillRect(bystander1.getY() * CELL_SIZE, bystander1.getX() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
//        g.fillRect(bystander2.getY() * CELL_SIZE, bystander2.getX() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
//
//        // Color-code the exit tile
//        g.setColor(Color.MAGENTA);  // Color for the exit
//        g.fillRect((grid[0].length - 2) * CELL_SIZE, (grid.length - 1) * CELL_SIZE, CELL_SIZE, CELL_SIZE);
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            GameController game = new GameController();
//            game.setVisible(true);
//        });
//    }
//}

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameController extends JFrame {
    private static final int CELL_SIZE = 20;  // Size of each cell in pixels
    private Maze maze;
    private Player player;
    private Killer killer;
    private Bystander bystander1, bystander2;
    private int turnLimit = 250;  // Max number of turns allowed
    private boolean gameRunning = true;

    public GameController() {
        maze = new Maze(20);  // Create a 20x20 maze
        int startX = 0;
        int startY = 0;

        // Ensure player starts on a valid path and is not trapped by walls
        while (!isValidStartPosition(startX, startY)) {
            startX = (int) (Math.random() * maze.getGrid().length);
            startY = (int) (Math.random() * maze.getGrid()[0].length);
        }

        player = new Player(maze, startX, startY);
        killer = new Killer(maze, player);  // Killer follows the player
        bystander1 = new Bystander(maze);
        bystander2 = new Bystander(maze);

        // Set up the JFrame
        setTitle("Murder Mystery Maze Game");
        setSize(maze.getGrid().length * CELL_SIZE, maze.getGrid()[0].length * CELL_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Add Key Listener to capture player movement
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameRunning) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_W:  // Up
                        case KeyEvent.VK_UP:
                            player.move('W');
                            break;
                        case KeyEvent.VK_S:  // Down
                        case KeyEvent.VK_DOWN:
                            player.move('S');
                            break;
                        case KeyEvent.VK_A:  // Left
                        case KeyEvent.VK_LEFT:
                            player.move('A');
                            break;
                        case KeyEvent.VK_D:  // Right
                        case KeyEvent.VK_RIGHT:
                            player.move('D');
                            break;
                    }

                    // Killer moves after the player
                    killer.move();
                    bystander1.rescue(player, killer);
                    bystander2.rescue(player, killer);
                    turnLimit--;

                    checkGameStatus();
                    repaint();  // Redraw the GUI after every move
                }
            }
        });

        setVisible(true);  // Make the JFrame visible
    }

    // Utility function to ensure player starts on a valid, movable position
    private boolean isValidStartPosition(int x, int y) {
        int[][] grid = maze.getGrid();
        if (grid[x][y] == Maze.getPathValue()) {
            // Check if there is at least one adjacent open path
            if ((x > 0 && grid[x - 1][y] == Maze.getPathValue()) || // Up
                    (x < grid.length - 1 && grid[x + 1][y] == Maze.getPathValue()) || // Down
                    (y > 0 && grid[x][y - 1] == Maze.getPathValue()) || // Left
                    (y < grid[0].length - 1 && grid[x][y + 1] == Maze.getPathValue())) { // Right
                return true;  // Valid position with an adjacent open path
            }
        }
        return false;  // Invalid position (surrounded by walls or no adjacent paths)
    }

    // Check win/lose conditions
    private void checkGameStatus() {
        int exitX = maze.getGrid().length - 1;  // Exit at bottom-right
        int exitY = maze.getGrid()[0].length - 1;

        // Check win condition
        if (player.getX() == exitX && player.getY() == exitY) {
            JOptionPane.showMessageDialog(this, "Congratulations! You escaped the maze.");
            gameRunning = false;
        }

        // Check lose condition
        if (player.getX() == killer.getX() && player.getY() == killer.getY()) {
            JOptionPane.showMessageDialog(this, "You were caught by the killer! Game Over.");
            gameRunning = false;
        }

        // Check if time runs out
        if (turnLimit == 0) {
            JOptionPane.showMessageDialog(this, "Time's up! The killer caught you.");
            gameRunning = false;
        }
    }

    // Custom paint method to draw the maze, player, killer, and bystanders
//    @Override
//    public void paint(Graphics g) {
//        super.paint(g);
//        int[][] grid = maze.getGrid();
//
//        // Draw the maze
//        for (int i = 0; i < grid.length; i++) {
//            for (int j = 0; j < grid[i].length; j++) {
//                int x = j * CELL_SIZE;
//                int y = i * CELL_SIZE;
//
//                if (grid[i][j] == Maze.getWallValue()) {
//                    g.setColor(Color.BLACK);  // Wall
//                } else {
//                    g.setColor(Color.WHITE);  // Path
//                }
//
//                g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
//
//                // Draw player
//                if (i == player.getX() && j == player.getY()) {
//                    g.setColor(Color.BLUE);
//                    g.fillOval(x, y, CELL_SIZE, CELL_SIZE);
//                }
//
//                // Draw killer
//                if (i == killer.getX() && j == killer.getY()) {
//                    g.setColor(Color.RED);
//                    g.fillOval(x, y, CELL_SIZE, CELL_SIZE);
//                }
//
//                // Draw bystanders
//                if (i == bystander1.getX() && j == bystander1.getY() && !bystander1.isRescued()) {
//                    g.setColor(Color.GREEN);
//                    g.fillOval(x, y, CELL_SIZE, CELL_SIZE);
//                }
//                if (i == bystander2.getX() && j == bystander2.getY() && !bystander2.isRescued()) {
//                    g.setColor(Color.GREEN);
//                    g.fillOval(x, y, CELL_SIZE, CELL_SIZE);
//                }
//            }
//        }
//    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int[][] grid = maze.getGrid();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                int x = j * CELL_SIZE;
                int y = i * CELL_SIZE;

                // Apply fog of war
                if (player.isInFog(i, j)) {
                    g.setColor(Color.DARK_GRAY);  // Fog
                } else if (grid[i][j] == Maze.getWallValue()) {
                    g.setColor(Color.BLACK);  // Wall
                } else {
                    g.setColor(Color.WHITE);  // Path
                }
                g.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                // Draw entities
                if (!player.isInFog(i, j)) {
                    if (i == player.getX() && j == player.getY()) {
                        g.setColor(Color.BLUE);
                        g.fillOval(x, y, CELL_SIZE, CELL_SIZE);
                    }
                    if (i == killer.getX() && j == killer.getY()) {
                        g.setColor(Color.RED);
                        g.fillOval(x, y, CELL_SIZE, CELL_SIZE);
                    }
                    if (i == bystander1.getX() && j == bystander1.getY() && !bystander1.isRescued()) {
                        g.setColor(Color.GREEN);
                        g.fillOval(x, y, CELL_SIZE, CELL_SIZE);
                    }
                    if (i == bystander2.getX() && j == bystander2.getY() && !bystander2.isRescued()) {
                        g.setColor(Color.GREEN);
                        g.fillOval(x, y, CELL_SIZE, CELL_SIZE);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new GameController();  // Start the game
    }
}
