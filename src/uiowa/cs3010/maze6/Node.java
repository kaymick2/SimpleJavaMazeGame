package uiowa.cs3010.maze6;

import java.util.Objects;

public class Node {
    private int x, y, g, h; // x and y are the coordinates of the node, g is the cost to reach this node, h is the heuristic value

    // Constructor to initialize the node with coordinates, g, and h values
    public Node(int x, int y, int g, int h) {
        this.x = x;
        this.y = y;
        this.g = g;
        this.h = h;
    }

    // Getter method for the x coordinate
    public int getX() {
        return x;
    }

    // Getter method for the y coordinate
    public int getY() {
        return y;
    }

    // Getter method for the g value (cost to reach this node)
    public int getG() {
        return g;
    }

    // Getter method for the h value (heuristic distance to goal)
    public int getH() {
        return h;
    }

    // Setter method for the g value (cost to reach this node)
    public void setG(int g) {
        this.g = g;
    }

    // Setter method for the h value (heuristic distance to goal)
    public void setH(int h) {
        this.h = h;
    }

    // Calculates the f value (total cost) of the node, which is g + h
    public int f() {
        return g + h;
    }

    // Overrides the equals method to compare nodes based on their coordinates
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // If the two nodes are the same object in memory, they are equal
        if (obj == null || getClass() != obj.getClass()) return false; // If the object is null or not a Node, return false
        Node node = (Node) obj; // Cast the object to a Node
        return x == node.x && y == node.y; // Nodes are equal if their coordinates are the same
    }

    // Overrides the hashCode method to generate a unique hash for the node based on its coordinates
    @Override
    public int hashCode() {
        return Objects.hash(x, y); // Hash code is based on the x and y coordinates
    }
}
