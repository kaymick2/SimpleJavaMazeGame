package uiowa.cs3010.maze6;

import java.util.*;

public class AStarAlgorithm {

    // Finds the shortest path from start to goal using A* algorithm
    public static List<Node> findPath(int startX, int startY, int goalX, int goalY, char[][] maze) {
        // Priority queue to hold the nodes to be evaluated, ordered by f value (g + h)
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(Node::f));

        // Set to track the nodes that have already been evaluated
        Set<Node> closedList = new HashSet<>();

        // Initialize the start node with a g value of 0 and heuristic value
        Node startNode = new Node(startX, startY, 0, heuristic(startX, startY, goalX, goalY));
        openList.add(startNode); // Add the start node to the open list

        // Map to track the path, storing which node led to each current node
        Map<Node, Node> cameFrom = new HashMap<>();

        // Loop while there are nodes to evaluate in the open list
        while (!openList.isEmpty()) {
            // Get the node with the lowest f value (g + h)
            Node current = openList.poll();

            // If the current node is the goal, reconstruct and return the path
            if (current.getX() == goalX && current.getY() == goalY) {
                List<Node> path = new ArrayList<>();
                while (current != null) {
                    path.add(current); // Add the current node to the path
                    current = cameFrom.get(current); // Move to the previous node in the path
                }
                Collections.reverse(path); // Reverse the path to start from the start node
                return path;
            }

            // Add the current node to the closed list
            closedList.add(current);

            // Check the neighbors of the current node
            for (Node neighbor : getNeighbors(current, maze)) {
                // Skip if the neighbor is already in the closed list or a wall
                if (closedList.contains(neighbor) || maze[neighbor.getX()][neighbor.getY()] == '#') {
                    continue;
                }

                // Calculate the tentative g value for this neighbor
                int tentative_g = current.getG() + 1;

                // If the neighbor is not in the open list or we found a better path, update its values
                if (!openList.contains(neighbor) || tentative_g < neighbor.getG()) {
                    cameFrom.put(neighbor, current); // Track the node that led to this neighbor
                    neighbor.setG(tentative_g); // Update g value
                    neighbor.setH(heuristic(neighbor.getX(), neighbor.getY(), goalX, goalY)); // Update heuristic value

                    // Add the neighbor to the open list if not already there
                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }

        // Return an empty list if no path is found
        return new ArrayList<>();
    }

    // Gets the neighbors of the current node (adjacent cells: up, down, left, right)
    private static List<Node> getNeighbors(Node node, char[][] maze) {
        List<Node> neighbors = new ArrayList<>();
        int[] dx = {-1, 1, 0, 0}; // X direction offsets (left, right)
        int[] dy = {0, 0, -1, 1}; // Y direction offsets (up, down)

        // Loop over all 4 possible neighbors (up, down, left, right)
        for (int i = 0; i < 4; i++) {
            int newX = node.getX() + dx[i];
            int newY = node.getY() + dy[i];

            // Check if the neighbor is within the maze boundaries
            if (newX >= 0 && newX < maze.length && newY >= 0 && newY < maze[0].length) {
                neighbors.add(new Node(newX, newY, 0, 0)); // Add valid neighbor
            }
        }

        return neighbors;
    }

    // Heuristic function to estimate the distance between two points (Manhattan distance)
    private static int heuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2); // Manhattan distance
    }
}
