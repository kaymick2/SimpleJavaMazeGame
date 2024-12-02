package uiowa.cs3010.maze6;

import java.util.*;

// Class representing the maze
public class KMaze {
    private final int[][] grid; // 2D array representing the maze grid
    private final int width; // Width of the maze
    private final int height; // Height of the maze

    // Constructor to initialize the maze with specified width and height
    public KMaze(int width, int height) {
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

            KNode playerSpawn = getPlayerSpawn(); // Get the player's spawn position
            KNode exit = getExitPosition(); // Get the exit position

            // Carve a path from the player spawn to the exit
            Set<KNode> essentialCells = new HashSet<>(carvePathBFS(playerSpawn, exit));
            Random random = new Random();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    KNode KNode = new KNode(x, y);
                    // Randomly add walls while keeping essential paths clear
                    if (!essentialCells.contains(KNode) && !isBufferZone(x, y, playerSpawn, exit)) {
                        grid[x][y] = (random.nextInt(4) < 3) ? 0 : 1;
                    }
                }
            }

            // Ensure start and exit positions are open paths
            grid[playerSpawn.getX()][playerSpawn.getY()] = 0;
            grid[exit.getX()][exit.getY()] = 0;

            // Check if there is a valid path from the player spawn to the exit
            List<KNode> pathToExit = findSolutionPath(playerSpawn.getX(), playerSpawn.getY(), exit);

            validMaze = (pathToExit != null);
        } while (!validMaze);}

    // Helper method to define a buffer zone to prevent wall-locking around key points
    private boolean isBufferZone(int x, int y, KNode... KNodes) {
        for (KNode KNode : KNodes) {
            if (Math.abs(KNode.getX() - x) <= 1 && Math.abs(KNode.getY() - y) <= 1) {
                return true;
            }
        }
        return false;
    }



    // Method to carve a path using Breadth-First Search (BFS)
    private Set<KNode> carvePathBFS(KNode start, KNode target) {
        Queue<KNode> queue = new LinkedList<>();
        Set<KNode> path = new HashSet<>();
        queue.add(start);
        grid[start.getX()][start.getY()] = 0;

        while (!queue.isEmpty()) {
            KNode current = queue.poll();
            path.add(current);

            if (current.equals(target)) break;

            for (KNode neighbor : getNeighbors(current)) {
                if (grid[neighbor.getX()][neighbor.getY()] == 1) {
                    grid[neighbor.getX()][neighbor.getY()] = 0;
                    queue.add(neighbor);
                    path.add(neighbor);
                }
            }
        }
        return path;
    }

    // Method to get the neighbors of a given KNode
    private List<KNode> getNeighbors(KNode KNode) {
        List<KNode> neighbors = new ArrayList<>();
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}}; // Possible movement directions (up, down, left, right)
        for (int[] dir : directions) {
            int newX = KNode.getX() + dir[0];
            int newY = KNode.getY() + dir[1];
            if (newX >= 0 && newY >= 0 && newX < width && newY < height) {
                neighbors.add(new KNode(newX, newY));
            }
        }
        return neighbors;
    }

    // Method to find the solution path from the start to the target node
    public List<KNode> findSolutionPath(int startX, int startY, KNode target) {
        Queue<KNode> queue = new LinkedList<>();
        Map<KNode, KNode> cameFrom = new HashMap<>();
        KNode start = new KNode(startX, startY);

        queue.add(start);
        cameFrom.put(start, null);

        while (!queue.isEmpty()) {
            KNode current = queue.poll();

            if (current.equals(target)) {
                return reconstructPath(cameFrom, target);
            }

            for (KNode neighbor : getNeighbors(current)) {
                if (grid[neighbor.getX()][neighbor.getY()] == 0 && !cameFrom.containsKey(neighbor)) {
                    queue.add(neighbor);
                    cameFrom.put(neighbor, current);
                }
            }
        }
        return null;
    }

    // Method to reconstruct the path from the target node to the start node
    private List<KNode> reconstructPath(Map<KNode, KNode> cameFrom, KNode target) {
        List<KNode> path = new ArrayList<>();
        for (KNode KNode = target; KNode != null; KNode = cameFrom.get(KNode)) {
            path.add(KNode);
        }
        Collections.reverse(path); // Reverse the path to get the correct order from start to target
        return path;
    }

    // Method to check if a position is valid within the maze
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height && grid[x][y] == 0;
    }

    // Getter for the exit position
    public KNode getExitPosition() {
        return new KNode(width - 1, 0);
    }

    // Getter for the player spawn position
    public KNode getPlayerSpawn() {
        return new KNode(0, 0);
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