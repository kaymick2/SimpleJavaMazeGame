// Bystander.java - Trigger killer's aggressive behavior when a bystander is rescued
import java.util.Random;
public class Bystander {
    private int x, y;
    private boolean rescued = false;

    public Bystander(Maze maze) {
        Random rand = new Random();
        int[][] grid = maze.getGrid();
        do {
            x = rand.nextInt(grid.length);
            y = rand.nextInt(grid[0].length);
        } while (grid[x][y] != 0);  // Ensure it's placed in an open space
    }

    public boolean isRescued() { return rescued; }
    public int getX() { return x; }
    public int getY() { return y; }

    public void rescue(Player player, Killer killer) {
        if (player.getX() == x && player.getY() == y && !rescued) {
            rescued = true;
            System.out.println("You rescued a bystander!");
            killer.increaseAggression();  // Make killer more aggressive after rescue
        }
    }
}
