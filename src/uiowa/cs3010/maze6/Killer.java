import java.util.List;

// Killer.java - Add increaseAggression method for dynamic difficulty
public class Killer {
    private int x, y;
    private Maze maze;
    private Player player;
    private AStar aStar;
    private boolean aggressiveMode = false;  // Killer becomes more aggressive after rescuing bystanders

    public Killer(Maze maze, Player player) {
        this.maze = maze;
        this.player = player;
        this.aStar = new AStar(maze);  // Pass only maze to the AStar constructor
        this.x = maze.getGrid().length - 1;  // Start position
        this.y = maze.getGrid()[0].length - 1;  // Start position
    }

    public void move() {
        List<Node> path = aStar.findPath(x, y, player.getX(), player.getY());
        if (!path.isEmpty()) {
            Node nextMove = path.get(1);  // Move to the next step in the path
            this.x = nextMove.x;
            this.y = nextMove.y;
        }

        // If aggressive mode is enabled, the killer moves faster (move two steps instead of one)
        if (aggressiveMode && path.size() > 2) {
            Node nextMove = path.get(2);  // Move an extra step to increase difficulty
            this.x = nextMove.x;
            this.y = nextMove.y;
        }
    }
//    public void move() {
//        List<Node> path = aStar.findPath(x, y, player.getX(), player.getY());
//        if (path.size() > 1) {
//            Node nextMove = path.get(1);
//            this.x = nextMove.x;
//            this.y = nextMove.y;
//        }
//        if (aggressiveMode && path.size() > 2) {
//            Node nextMove = path.get(2);
//            this.x = nextMove.x;
//            this.y = nextMove.y;
//        }
//    }

    // This method will be called to increase killer's aggression when a bystander is rescued
    public void increaseAggression() {
        aggressiveMode = true;  // Enable faster movement after a bystander is rescued
        System.out.println("The killer becomes more aggressive!");
    }

    public void displayProximityAlert() {
        double distance = Math.sqrt(Math.pow(x - player.getX(), 2) + Math.pow(y - player.getY(), 2));
        if (distance <= 1) {
            System.out.println("The sound of breathing grows louder.");
        } else if (distance <= 2) {
            System.out.println("A door creaks open nearby.");
        } else if (distance <= 3) {
            System.out.println("You hear faint footsteps behind you.");
        } else {
            System.out.println("All is quiet.");
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
