package uiowa.cs3010.maze6;

import java.util.*;

// Class representing a node in the KMaze
class KNode {
    private final int x; // X-coordinate of the node
    private final int y; // Y-coordinate of the node
    private double gCost; // Cost from the start node to this node
    private double hCost; // Heuristic cost from this node to the target node
    private KNode parent; // Parent node to trace the path

    // Constructor to initialize the node with its coordinates
    public KNode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getter for the X-coordinate
    public int getX() { return x; }

    // Getter for the Y-coordinate
    public int getY() { return y; }

    // Method to calculate the total cost (F-cost) of the node
    public double getFCost() {
        return gCost + hCost;
    }

    // Getter for the G-cost
    public double getGCost() { return gCost; }

    // Setter for the G-cost
    public void setGCost(double gCost) { this.gCost = gCost; }

    // Setter for the H-cost
    public void setHCost(double hCost) { this.hCost = hCost; }

    // Getter for the parent node
    public KNode getParent() { return parent; }

    // Setter for the parent node
    public void setParent(KNode parent) { this.parent = parent; }

    // Override equals method to compare nodes based on their coordinates
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KNode other)) return false;
        return this.x == other.x && this.y == other.y;
    }

    // Override hashCode method to generate a hash code based on the coordinates
    @Override
    public int hashCode() {
        return x * 31 + y;
    }
}

        // Class implementing the A* pathfinding algorithm
        @SuppressWarnings("ALL")
        public class KAStar {
            private final KMaze KMaze; // Reference to the KMaze

            // Constructor to initialize the KAStar with the KMaze
            public KAStar(KMaze KMaze) {
                this.KMaze = KMaze;
            }

            // Method to find the path from the start node to the target node
            public List<KNode> findPath(KNode startKNode, KNode targetKNode) {
                PriorityQueue<KNode> openSet = new PriorityQueue<>(Comparator.comparingDouble(KNode::getFCost)); // Open set for nodes to be evaluated
                Set<KNode> closedSet = new HashSet<>(); // Closed set for nodes already evaluated
                openSet.add(startKNode); // Add the start node to the open set

                while (!openSet.isEmpty()) {
                    KNode currentKNode = openSet.poll(); // Get the node with the lowest F-cost

                    // If the target node is reached, retrace the path
                    if (currentKNode.equals(targetKNode)) {
                        return retracePath(startKNode, currentKNode);
                    }

                    closedSet.add(currentKNode); // Add the current node to the closed set

                    // Evaluate each neighbor of the current node
                    for (KNode neighbor : getNeighbors(currentKNode)) {
                        if (closedSet.contains(neighbor)) continue; // Skip if the neighbor is in the closed set

                        double newGCost = currentKNode.getGCost() + getDistance(currentKNode, neighbor); // Calculate the G-cost for the neighbor
                        if (newGCost < neighbor.getGCost() || !openSet.contains(neighbor)) {
                            neighbor.setGCost(newGCost); // Update the G-cost
                            neighbor.setHCost(getDistance(neighbor, targetKNode)); // Update the H-cost
                            neighbor.setParent(currentKNode); // Set the parent to the current node

                            if (!openSet.contains(neighbor)) {
                                openSet.add(neighbor); // Add the neighbor to the open set if not already present
                            }
                        }
                    }
                }
                return Collections.emptyList(); // Return an empty list if no path is found
            }

            // Method to retrace the path from the target node to the start node
            private List<KNode> retracePath(KNode startKNode, KNode targetKNode) {
                List<KNode> path = new ArrayList<>();
                KNode currentKNode = targetKNode;

                // Trace back from the target node to the start node using the parent references
                while (!currentKNode.equals(startKNode)) {
                    path.add(currentKNode);
                    currentKNode = currentKNode.getParent();
                }
                Collections.reverse(path); // Reverse the path to get the correct order from start to target
                return path;
            }

            // Method to get the neighbors of a given KNode
            private List<KNode> getNeighbors(KNode KNode) {
                List<KNode> neighbors = new ArrayList<>();
                int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}}; // Possible movement directions (up, down, left, right)
                for (int[] dir : directions) {
                    int newX = KNode.getX() + dir[0];
                    int newY = KNode.getY() + dir[1];
                    if (KMaze.isValidPosition(newX, newY)) {
                        neighbors.add(new KNode(newX, newY)); // Add the neighbor if the position is valid in the KMaze
                    }
                }
                return neighbors;
            }

            // Method to calculate the Manhattan distance between two nodes
            private double getDistance(KNode a, KNode b) {
                return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
            }
        }
