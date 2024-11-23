package uiowa.cs3010.maze6;

import java.util.*;

// Class representing the maze
public class Maze {
    private final int[][] grid; // 2D array representing the maze grid
    private final int width; // Width of the maze
    private final int height; // Height of the maze

    // Constructor to initialize the maze with specified width and height
    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new int[width][height];
        generateMaze(); // Generate the maze
    }

    // Method to generate the maze
    private void generateMaze() {
        boolean validMaze;
        do {
            // Initialize the grid with walls (0)
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    grid[x][y] = 0;
                }
            }

            Node playerSpawn = getPlayerSpawn(); // Get the player's spawn position
            Node exit = getExitPosition(); // Get the exit position

            // Carve a path from the player spawn to the exit
            Set<Node> essentialCells = new HashSet<>(carvePathBFS(playerSpawn, exit));
            Random random = new Random();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Node node = new Node(x, y);
                    // Randomly add walls while keeping essential paths clear
                    if (!essentialCells.contains(node) && !isBufferZone(x, y, playerSpawn, exit)) {
                        grid[x][y] = (random.nextInt(4) < 3) ? 0 : 1;
                    }
                }
            }

            // Ensure start and exit positions are open paths
            grid[playerSpawn.getX()][playerSpawn.getY()] = 0;
            grid[exit.getX()][exit.getY()] = 0;

            // Check if there is a valid path from the player spawn to the exit
            List<Node> pathToExit = findSolutionPath(playerSpawn.getX(), playerSpawn.getY(), exit);

            validMaze = (pathToExit != null);
        } while (!validMaze);
        printMaze(); // Print the maze for debugging
    }

    // Helper method to define a buffer zone to prevent wall-locking around key points
    private boolean isBufferZone(int x, int y, Node... nodes) {
        for (Node node : nodes) {
            if (Math.abs(node.getX() - x) <= 1 && Math.abs(node.getY() - y) <= 1) {
                return true;
            }
        }
        return false;
    }

    // Method to print the maze for debugging
    public void printMaze() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(grid[x][y] == 1 ? "#" : " ");
            }
            System.out.println();
        }
    }

    // Method to carve a path using Breadth-First Search (BFS)
    private Set<Node> carvePathBFS(Node start, Node target) {
        Queue<Node> queue = new LinkedList<>();
        Set<Node> path = new HashSet<>();
        queue.add(start);
        grid[start.getX()][start.getY()] = 0;

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            path.add(current);

            if (current.equals(target)) break;

            for (Node neighbor : getNeighbors(current)) {
                if (grid[neighbor.getX()][neighbor.getY()] == 1) {
                    grid[neighbor.getX()][neighbor.getY()] = 0;
                    queue.add(neighbor);
                    path.add(neighbor);
                }
            }
        }
        return path;
    }

    // Method to get the neighbors of a given node
    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}}; // Possible movement directions (up, down, left, right)
        for (int[] dir : directions) {
            int newX = node.getX() + dir[0];
            int newY = node.getY() + dir[1];
            if (newX >= 0 && newY >= 0 && newX < width && newY < height) {
                neighbors.add(new Node(newX, newY));
            }
        }
        return neighbors;
    }

    // Method to find the solution path from the start to the target node
    public List<Node> findSolutionPath(int startX, int startY, Node target) {
        Queue<Node> queue = new LinkedList<>();
        Map<Node, Node> cameFrom = new HashMap<>();
        Node start = new Node(startX, startY);

        queue.add(start);
        cameFrom.put(start, null);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.equals(target)) {
                return reconstructPath(cameFrom, target);
            }

            for (Node neighbor : getNeighbors(current)) {
                if (grid[neighbor.getX()][neighbor.getY()] == 0 && !cameFrom.containsKey(neighbor)) {
                    queue.add(neighbor);
                    cameFrom.put(neighbor, current);
                }
            }
        }
        return null;
    }

    // Method to reconstruct the path from the target node to the start node
    private List<Node> reconstructPath(Map<Node, Node> cameFrom, Node target) {
        List<Node> path = new ArrayList<>();
        for (Node node = target; node != null; node = cameFrom.get(node)) {
            path.add(node);
        }
        Collections.reverse(path); // Reverse the path to get the correct order from start to target
        return path;
    }

    // Method to check if a position is valid within the maze
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height && grid[x][y] == 0;
    }

    // Getter for the exit position
    public Node getExitPosition() {
        return new Node(width - 1, 0);
    }

    // Getter for the player spawn position
    public Node getPlayerSpawn() {
        return new Node(0, 0);
    }

    // Getter for the maze grid
    public int[][] getGrid() {
        return grid;
    }

    // Getter for the maze width
    public int getWidth() {
        return width;
    }

    // Getter for the maze height
    public int getHeight() {
        return height;
    }
}