import java.util.*;
public class MazeSolver {

    static final int[][] mainMaze = {
        {0, 0, 0, 0, 0},
        {1, 1, 1, 0, 0},
        {0, 0, 1, 0, 0},
        {0, 0, 1, 1, 1},
        {0, 0, 0, 0, 0}
    };

    public static void main(String[] args) {
        (new MazeSolver(MazeSolver.mainMaze)).findPath();
    }

    //private member variables
    private int[][] maze;
    private int rows;
    private int cols;
    private ArrayList<String> path;
    private boolean[][] visited;
    private String startWall; // To remember which wall we started from
    
    //member functions
    public MazeSolver(int[][] maze) {
        this.maze = maze;
        this.rows = maze.length;
        this.cols = maze[0].length;
        this.path = new ArrayList<>();
        this.visited = new boolean[rows][cols];
        this.startWall = ""; // Initialize empty
    }
    
    public ArrayList<String> findPath() {
        int[] start = getStart();
        if (start == null) {
            System.out.println("No valid path found! Must start from top or left wall.");
            this.printTestResult();
            return new ArrayList<>();
        }
        
        // Initialize visited array
        visited = new boolean[rows][cols];
        
        // Start DFS from the found starting position
        if (findPathDFS(start[0], start[1], new ArrayList<>(), startWall, getValidEndWalls(startWall, start[0], start[1])) != null) {
            this.printTestResult();
            return path;
        }

        this.printTestResult();
        return new ArrayList<>();
    }
    
    private String[] getValidEndWalls(String startWall, int startX, int startY) {
        // Check if starting in a corner
        boolean isCorner = (startX == 0 && startY == 0) || // top-left
                          (startX == 0 && startY == cols-1) || // top-right
                          (startX == rows-1 && startY == 0) || // bottom-left
                          (startX == rows-1 && startY == cols-1); // bottom-right
        
        if (isCorner) {
            // If in corner, can end on any adjacent wall
            if (startX == 0) { // top corner
                return new String[]{"LEFT", "RIGHT", "BOTTOM"};
            } else if (startX == rows-1) { // bottom corner
                return new String[]{"LEFT", "RIGHT", "TOP"};
            } else if (startY == 0) { // left corner
                return new String[]{"TOP", "BOTTOM", "RIGHT"};
            } else { // right corner
                return new String[]{"TOP", "BOTTOM", "LEFT"};
            }
        }
        
        // If not in corner, must end on opposite wall
        if (startWall.equals("TOP")) {
            return new String[]{"BOTTOM"};
        } else if (startWall.equals("LEFT")) {
            return new String[]{"RIGHT"};
        }
        return new String[]{};
    }
    
    private boolean hasAtLeastOneTurn(ArrayList<String> path) {
        if (path.size() < 3) return false;
        
        int prevX = -1, prevY = -1;
        int currentDir = -1;  // 0: North, 1: East, 2: South, 3: West
        boolean hasTurn = false;
        
        for (String coord : path) {
            int x = Integer.parseInt(coord.split("\\[")[1].split("\\]")[0]);
            int y = Integer.parseInt(coord.split("\\[")[2].split("\\]")[0]);
            
            if (prevX != -1) {
                int newDir;
                if (x < prevX) newDir = 0;      // Moving North
                else if (x > prevX) newDir = 2; // Moving South
                else if (y < prevY) newDir = 3; // Moving West
                else newDir = 1;                // Moving East
                
                if (currentDir != -1 && newDir != currentDir) {
                    hasTurn = true;
                }
                currentDir = newDir;
            }
            
            prevX = x;
            prevY = y;
        }
        
        return hasTurn;
    }
    
    public void printMazeWithPath() {
        // Create a copy of the maze for display
        String[][] display = new String[rows][cols];
        
        // Fill with original maze values (1s are open spots)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                display[i][j] = (maze[i][j] == 1) ? "1" : " ";
            }
        }
        
        // Mark our walking path with 'P'
        for (String coord : path) {
            int x = Integer.parseInt(coord.split("\\[")[1].split("\\]")[0]);
            int y = Integer.parseInt(coord.split("\\[")[2].split("\\]")[0]);
            display[x][y] = "P";
        }
        
        // Print the maze with path
        System.out.println("Maze with path (P marks the walking path):");
        for (int i = 0; i < rows; i++) {
            System.out.print("[ ");
            for (int j = 0; j < cols; j++) {
                System.out.print(display[i][j] + " , ");
            }
            System.out.println("]");
        }
    }
    
    private int[] getStart() {
        // Check left wall (column 0)
        for (int i = 0; i < rows; i++) {
            if (maze[i][0] == 1) {
                startWall = "LEFT";
                return new int[]{i, 0};
            }
        }
        
        // Check top wall (row 0)
        for (int j = 0; j < cols; j++) {
            if (maze[0][j] == 1) {
                startWall = "TOP";
                return new int[]{0, j};
            }
        }
        
        // If no valid start found, return null
        return null;
    }

    public static void testMain() {
        // Test Case 1: Original L-shaped path from right to bottom
        int[][] testMaze1 = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        // Test Case 2: Zigzag path from top to bottom
        int[][] testMaze2 = {
            {0, 0, 0, 1, 0},
            {0, 0, 0, 1, 0},
            {0, 1, 1, 1, 0},
            {0, 1, 0, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 1, 0},
            {0, 0, 0, 1, 0}
        };

        // Test Case 3: U-shaped path from left to right
        int[][] testMaze3 = {
            {0, 0, 0, 0, 0, 0, 0},
            {1, 1, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0},
            {1, 1, 1, 1, 1, 1, 1},
            {0, 0, 0, 0, 0, 0, 0}
        };

        // Test Case 4: Simple turn from top to right (should not work - not adjacent walls)
        int[][] testMaze4 = {
            {0, 0, 0, 1, 0},
            {0, 0, 0, 1, 0},
            {0, 0, 0, 1, 1},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0}
        };

        // Test Case 5: Multiple turns from left to top
        int[][] testMaze5 = {
            {0, 0, 0, 0, 0},
            {1, 1, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 1, 1},
            {0, 0, 0, 0, 0}
        };

        // Test Case 6: Corner start (top-left) with path to bottom
        int[][] testMaze6 = {
            {1, 1, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 1, 1, 0, 0},
            {0, 1, 0, 0, 0}
        };

        // Test Case 7: Corner start (top-right) with path to left
        int[][] testMaze7 = {
            {0, 0, 0, 0, 1},
            {0, 0, 0, 1, 1},
            {0, 0, 1, 1, 0},
            {0, 1, 1, 0, 0},
            {1, 1, 0, 0, 0}
        };

        // Test Case 8: No valid path (all walls blocked)
        int[][] testMaze8 = {
            {1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0}
        };

        // Test Case 9: Spiral path from corner
        int[][] testMaze9 = {
            {1, 1, 1, 1, 1},
            {0, 0, 0, 0, 1},
            {1, 1, 1, 0, 1},
            {1, 0, 0, 0, 1},
            {1, 1, 1, 1, 1}
        };

        // Test Case 10: Multiple forks with dead ends
        int[][] testMaze10 = {
            {1, 1, 1, 0, 1},
            {0, 0, 1, 0, 1},
            {1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0},
            {1, 0, 1, 1, 1}
        };

        System.out.println("Test Case 1: L-shaped path from right to bottom");
        MazeSolver solver1 = new MazeSolver(testMaze1);
        solver1.findPath();

        System.out.println("\nTest Case 2: Zigzag path from top to bottom");
        MazeSolver solver2 = new MazeSolver(testMaze2);
        solver2.findPath();

        System.out.println("\nTest Case 3: U-shaped path from left to right");
        MazeSolver solver3 = new MazeSolver(testMaze3);
        solver3.findPath();

        System.out.println("\nTest Case 4: Simple turn from top to right (should not work)");
        MazeSolver solver4 = new MazeSolver(testMaze4);
        solver4.findPath();

        System.out.println("\nTest Case 5: Multiple turns from left to top");
        MazeSolver solver5 = new MazeSolver(testMaze5);
        solver5.findPath();

        System.out.println("\nTest Case 6: Corner start (top-left) with path to bottom");
        MazeSolver solver6 = new MazeSolver(testMaze6);
        solver6.findPath();

        System.out.println("\nTest Case 7: Corner start (top-right) with path to left");
        MazeSolver solver7 = new MazeSolver(testMaze7);
        solver7.findPath();

        System.out.println("\nTest Case 8: No valid path (all walls blocked)");
        MazeSolver solver8 = new MazeSolver(testMaze8);
        solver8.findPath();

        System.out.println("\nTest Case 9: Spiral path from corner");
        MazeSolver solver9 = new MazeSolver(testMaze9);
        solver9.findPath();

        System.out.println("\nTest Case 10: Multiple forks with dead ends");
        MazeSolver solver10 = new MazeSolver(testMaze10);
        solver10.findPath();
    }

    private void printTestResult() {
        if (!path.isEmpty()) {
            System.out.println("Path found:");
            System.out.println(path);
            System.out.println("\nVisual representation of the path:");
            printMazeWithPath();
        } else {
            System.out.println("No valid path found!");
        }
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }

    private boolean isEdge(int x, int y) {
        return x == 0 || x == rows - 1 || y == 0 || y == cols - 1;
    }

    private boolean isValidEndWall(String currentWall, String[] validEndWalls) {
        for (String validWall : validEndWalls) {
            if (currentWall.equals(validWall)) {
                return true;
            }
        }
        return false;
    }

    private String getCurrentWall(int x, int y) {
        if (x == 0) return "TOP";
        if (x == rows - 1) return "BOTTOM";
        if (y == 0) return "LEFT";
        if (y == cols - 1) return "RIGHT";
        return "NONE";
    }

    private int[] findPathDFS(int x, int y, ArrayList<String> currentPath, String startWall, String[] validEndWalls) {
        final int[] DX = {-1, 0, 1, 0}; // Up, Right, Down, Left
        final int[] DY = {0, 1, 0, -1}; 
        // Check if current position is valid and not visited
        if (!isValid(x, y) || maze[x][y] != 1 || visited[x][y]) {
            return null;
        }

        // Mark current position as visited and add to path
        visited[x][y] = true;
        currentPath.add(String.format("A[%d][%d]", x, y));

        // Check available directions (excluding the direction we came from)
        int[] nextX = new int[3];  // Only 3 possible directions (can't go back)
        int[] nextY = new int[3];
        int availableDirections = 0;
        
        // Get the direction we came from (if any)
        int cameFromDir = -1;
        if (currentPath.size() > 1) {
            String prevCoord = currentPath.get(currentPath.size() - 2);
            int prevX = Integer.parseInt(prevCoord.split("\\[")[1].split("\\]")[0]);
            int prevY = Integer.parseInt(prevCoord.split("\\[")[2].split("\\]")[0]);
            
            // Determine direction we came from
            if (prevX < x) cameFromDir = 0;      // Came from North
            else if (prevX > x) cameFromDir = 2; // Came from South
            else if (prevY < y) cameFromDir = 3; // Came from West
            else if (prevY > y) cameFromDir = 1; // Came from East
        }
        
        // Check all directions except the one we came from
        for (int dir = 0; dir < 4; dir++) {
            if (dir == cameFromDir) continue;  // Skip the direction we came from
            
            int newX = x + DX[dir];
            int newY = y + DY[dir];
            
            if (isValid(newX, newY) && maze[newX][newY] == 1 && !visited[newX][newY]) {
                nextX[availableDirections] = newX;
                nextY[availableDirections] = newY;
                availableDirections++;
            }
        }

        // If this is a fork (more than one direction available)
        if (availableDirections > 1) {
            // Save current position as a fork
            String forkPosition = String.format("A[%d][%d]", x, y);
            
            // Try each direction from the fork
            for (int i = 0; i < availableDirections; i++) {
                int[] next = findPathDFS(nextX[i], nextY[i], currentPath, startWall, validEndWalls);
                if (next != null) {
                    return next;
                }
            }
            
            // If no path found from any direction, backtrack
            visited[x][y] = false;
            currentPath.remove(currentPath.size() - 1);
            return null;
        }
        // If only one direction available, continue that way
        else if (availableDirections == 1) {
            // Check if this leads to a valid end wall
            if (isEdge(nextX[0], nextY[0])) {
                String currentWall = getCurrentWall(nextX[0], nextY[0]);
                if (isValidEndWall(currentWall, validEndWalls) && !currentWall.equals(startWall)) {
                    if (hasAtLeastOneTurn(currentPath)) {
                        // Add the final position to the path
                        currentPath.add(String.format("A[%d][%d]", nextX[0], nextY[0]));
                        path = new ArrayList<>(currentPath);
                        return new int[]{nextX[0], nextY[0]};
                    }
                }
            }
            
            int[] next = findPathDFS(nextX[0], nextY[0], currentPath, startWall, validEndWalls);
            if (next != null) {
                return next;
            }
            
            // If no path found, backtrack
            visited[x][y] = false;
            currentPath.remove(currentPath.size() - 1);
            return null;
        }
        // If no directions available, backtrack
        else {
            visited[x][y] = false;
            currentPath.remove(currentPath.size() - 1);
            return null;
        }
    }
} 
