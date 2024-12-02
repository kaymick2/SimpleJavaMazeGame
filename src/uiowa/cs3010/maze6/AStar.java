import java.util.*;

class Node {
    public int x, y;
    public Node parent;
    public int g, h, f;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Calculate Manhattan Distance heuristic
    public void calculateHeuristic(Node target) {
        this.h = Math.abs(target.x - this.x) + Math.abs(target.y - this.y);
    }

    // Update cost
    public void updateCost(Node parent) {
        this.g = parent.g + 1;  // Assuming uniform cost
        this.f = this.g + this.h;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

public class AStar {
    private Maze maze;

    public AStar(Maze maze) {
        this.maze = maze;
    }

    // A-star search algorithm to find path
    public List<Node> findPath(int startX, int startY, int endX, int endY) {
        Node start = new Node(startX, startY);
        Node target = new Node(endX, endY);
        start.calculateHeuristic(target);

        List<Node> openList = new ArrayList<>();
        List<Node> closedList = new ArrayList<>();
        openList.add(start);

        while (!openList.isEmpty()) {
            // Sort nodes by f value
            openList.sort(Comparator.comparingInt(n -> n.f));
            Node current = openList.get(0);

            if (current.equals(target)) {
                return reconstructPath(current);
            }

            openList.remove(current);
            closedList.add(current);

            // Get neighbors
            List<Node> neighbors = getNeighbors(current);
            for (Node neighbor : neighbors) {
                if (closedList.contains(neighbor)) {
                    continue;  // Ignore already evaluated nodes
                }

                neighbor.calculateHeuristic(target);
                if (!openList.contains(neighbor)) {
                    neighbor.updateCost(current);
                    neighbor.parent = current;
                    openList.add(neighbor);
                }
            }
        }
        return new ArrayList<>();  // Return empty if no path found
    }

    // Reconstruct the path by backtracking from the target node
    private List<Node> reconstructPath(Node current) {
        List<Node> path = new ArrayList<>();
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        Collections.reverse(path);  // Path is built backwards, so reverse it
        return path;
    }

    // Get valid neighbors (up, down, left, right)
    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        for (int[] dir : directions) {
            int newX = node.x + dir[0];
            int newY = node.y + dir[1];
            if (isValidMove(newX, newY)) {
                neighbors.add(new Node(newX, newY));
            }
        }
        return neighbors;
    }

    // Check if the move is valid within the maze
    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < maze.getGrid().length && y >= 0 && y < maze.getGrid()[0].length && maze.getGrid()[x][y] == 0;
    }
}
