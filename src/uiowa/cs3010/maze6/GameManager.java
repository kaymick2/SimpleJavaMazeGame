package uiowa.cs3010.maze6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GameManager extends JFrame implements ActionListener {
    private static final int MAZE_SIZE = 20; // Maze dimensions (20x20 grid)
    private Maze maze; // The maze itself, responsible for maze generation and management
    private Player player; // The player, who navigates the maze
    private Killer killer; // The killer, who chases the player
    private JPanel mazePanel; // Panel to display the maze
    private JLabel[][] mazeLabels; // 2D array of labels representing each cell in the maze
    private JTextArea consoleTextArea; // Text area for commentary (game events)
    private Random random; // Random object for generating random events

    public GameManager() {
        // Set up the main frame for the game
        setTitle("Murder Mystery Maze"); // Set the window title
        setSize(900, 900); // Set the window size, taller to fit the console at the bottom
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the application when the window is closed
        setLayout(new BorderLayout()); // Use BorderLayout to arrange components

        random = new Random(); // Initialize the random number generator

        // Create a menu bar with options
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Game");
        JMenuItem newGame = new JMenuItem("New Game"); // Option to start a new game
        JMenuItem exit = new JMenuItem("Exit"); // Option to exit the game
        newGame.addActionListener(this); // Listen for new game action
        exit.addActionListener(this); // Listen for exit action
        menu.add(newGame); // Add new game option to the menu
        menu.add(exit); // Add exit option to the menu
        menuBar.add(menu); // Add the menu to the menu bar
        setJMenuBar(menuBar); // Set the menu bar for the JFrame

        // Create the maze panel to display the maze
        mazePanel = new JPanel(new GridLayout(MAZE_SIZE, MAZE_SIZE)); // Grid layout for the maze
        mazeLabels = new JLabel[MAZE_SIZE][MAZE_SIZE]; // 2D array to hold labels for each cell in the maze
        for (int i = 0; i < MAZE_SIZE; i++) {
            for (int j = 0; j < MAZE_SIZE; j++) {
                mazeLabels[i][j] = new JLabel(); // Create a label for each cell
                mazeLabels[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Set borders for the labels
                mazeLabels[i][j].setOpaque(true); // Make the labels opaque for background color
                mazePanel.add(mazeLabels[i][j]); // Add each label to the maze panel
            }
        }
        add(mazePanel, BorderLayout.CENTER); // Add the maze panel to the center of the window

        // Create the text console for tracking game events
        consoleTextArea = new JTextArea(5, 20); // Create a non-editable text area (5 rows, 20 columns)
        consoleTextArea.setEditable(false); // Set text area to be non-editable
        JScrollPane scrollPane = new JScrollPane(consoleTextArea); // Make the text area scrollable
        add(scrollPane, BorderLayout.SOUTH); // Add the scrollable text area to the bottom of the window

        // Initialize key bindings for player movement
        mazePanel.setFocusable(true); // Make the maze panel focusable to detect key events
        initializeKeyBindings(); // Set up key bindings for player controls

        // Initialize the game
        initializeGame(); // Create the maze and game components
        startNewGame(); // Start a new game when the application launches

        setVisible(true); // Make the window visible
    }

    private void initializeKeyBindings() {
        // Set up key bindings to handle player movement using keys
        InputMap inputMap = mazePanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW); // Capture key presses when the panel is focused
        ActionMap actionMap = mazePanel.getActionMap(); // Map the actions to key presses

        // Bind movement keys
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "moveUp");   // 'A' and Left -> Up
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "moveDown"); // 'D' and Right -> Down
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "moveLeft"); // 'W' and Up -> Left
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "moveRight"); // 'S' and Down -> Right
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveRight");

        // Map the keys to their respective actions (move directions)
        actionMap.put("moveUp", new MoveAction(0, -1));    // Move up
        actionMap.put("moveDown", new MoveAction(0, 1));   // Move down
        actionMap.put("moveLeft", new MoveAction(-1, 0));  // Move left
        actionMap.put("moveRight", new MoveAction(1, 0));  // Move right
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Respond to menu actions (New Game or Exit)
        String command = e.getActionCommand();
        if (command.equals("New Game")) {
            startNewGame(); // Start a new game
        } else if (command.equals("Exit")) {
            System.exit(0); // Exit the application
        }
    }

    private void initializeGame() {
        // Create the maze object, responsible for generating and updating the maze
        maze = new Maze(MAZE_SIZE);
    }

    private void startNewGame() {
        // Reset the player and killer positions for each new game
        player = new Player(0, 0); // Start the player at the top-left corner
        killer = new Killer(MAZE_SIZE - 1, 0); // Start the killer at the bottom-left corner
        SoundManager.playLoopingSound("sounds/background_music.wav"); // Loop background music
        // Clear the console text area for new game messages
        consoleTextArea.setText("");

        // Generate a new maze and update the visibility (fog of war)
        maze.generateMaze(); // Generate the maze structure
        maze.updateFogOfWar(player.getX(), player.getY()); // Update the fog of war based on player position
        printMaze(); // Update the maze display
    }

    private void printMaze() {
        // Print the maze to the GUI based on the maze structure and visibility (fog of war)
        char[][] mazeArray = maze.getMaze(); // Get the maze structure
        boolean[][] fogOfWar = maze.getFogOfWar(); // Get the fog of war status

        for (int i = 0; i < MAZE_SIZE; i++) {
            for (int j = 0; j < MAZE_SIZE; j++) {
                mazeLabels[i][j].setText(""); // Clear the label text
                if (fogOfWar[i][j]) { // If the cell is visible (not in fog of war)
                    switch (mazeArray[i][j]) {
                        case '#':
                            mazeLabels[i][j].setBackground(Color.BLACK); // Wall
                            break;
                        case 'E':
                            mazeLabels[i][j].setBackground(Color.GREEN); // Exit
                            break;
                        default:
                            mazeLabels[i][j].setBackground(Color.WHITE); // Path
                            break;
                    }
                } else {
                    mazeLabels[i][j].setBackground(Color.GRAY); // Fog of war (hidden cell)
                }
            }
        }

        // Update the player and killer positions on the maze grid
        mazeLabels[player.getX()][player.getY()].setBackground(Color.BLUE); // Player is blue
        mazeLabels[killer.getX()][killer.getY()].setBackground(Color.RED); // Killer is red
    }

    private boolean movePlayer(int dx, int dy) {
        // Move the player and the killer, then update the maze
        player.move(dx, dy, maze); // Move the player based on the direction
        killer.chasePlayer(player.getX(), player.getY(), maze.getMaze()); // Move the killer towards the player

        maze.updateFogOfWar(player.getX(), player.getY()); // Update the fog of war
        printMaze(); // Refresh the maze display

        // Add commentary based on the player's position
        addCommentary();

        // Check if the game is over (player caught by killer or reaches exit)
        if (player.getX() == killer.getX() && player.getY() == killer.getY()) {
            JOptionPane.showMessageDialog(this, "Game Over! The killer caught you!"); // Display game over message
            SoundManager.playSound("sounds/killer_laugh.wav"); // Trigger killer laugh
            startNewGame(); // Restart the game
            return false;
        }

        // Check if the player reaches the exit
        if (maze.getMaze()[player.getX()][player.getY()] == 'E') {
            JOptionPane.showMessageDialog(this, "Congratulations! You escaped the maze!"); // Player wins
            startNewGame(); // Restart the game
            return false;
        }

        return true; // Continue the game
    }

    private void addCommentary() {
        // Add commentary based on the relative position of the killer to the player
        int playerX = player.getX();
        int playerY = player.getY();
        int killerX = killer.getX();
        int killerY = killer.getY();

        String comment;

        // Killer is above the player
        if (killerX < playerX) {
            String[] aboveComments = {
                    "You feel a gaze boring into you from above.",
                    "The sound of creaking floorboards echoes from above.",
                    "A shadow flickers on the ceiling above you.",
                    "You hear heavy breathing somewhere above.",
                    "The air feels colder from above, sending chills down your spine."
            };
            comment = aboveComments[random.nextInt(aboveComments.length)];
        }
        // Killer is below the player
        else if (killerX > playerX) {
            String[] belowComments = {
                    "The floor seems to vibrate faintly from below.",
                    "A faint clinking sound resonates from below.",
                    "You smell a faint trace of blood coming from below.",
                    "A low growl echoes up from below.",
                    "The shadows from below flicker and seem to climb toward you."
            };
            comment = belowComments[random.nextInt(belowComments.length)];
        }
        // Killer is to the left of the player
        else if (killerY < playerY) {
            String[] leftComments = {
                    "A faint scraping sound comes from the left wall.",
                    "You hear a faint whisper to your left, barely audible.",
                    "The left side of the maze feels unnaturally dark.",
                    "A faint metallic scent wafts in from the left.",
                    "The hair on your left arm stands on end."
            };
            comment = leftComments[random.nextInt(leftComments.length)];
        }
        // Killer is to the right of the player
        else if (killerY > playerY) {
            String[] rightComments = {
                    "The sound of hurried footsteps echoes from the right.",
                    "You catch a glimpse of movement to your right.",
                    "The right-hand path seems to pulse with a dark energy.",
                    "A faint laugh echoes from the right.",
                    "You hear the sound of something sharp dragging along the walls to your right."
            };
            comment = rightComments[random.nextInt(rightComments.length)];
        }
        // Killer is in the same spot as the player (this should never happen)
        else {
            comment = "You feel an icy breath on your neck.";
        }

        // Append the commentary to the text console
        consoleTextArea.append(comment + "\n");
    }

    // Inner class to handle player movement actions
    class MoveAction extends AbstractAction {
        int dx, dy;

        MoveAction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            movePlayer(dx, dy); // Perform the movement when an action is triggered
        }
    }

    // Main method to launch the game
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameManager::new); // Create a new GameManager instance and run it
    }
}
