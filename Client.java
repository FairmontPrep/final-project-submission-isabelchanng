import java.util.*;

public class MazeSolver {
    private static final int[] DX = {-1, 0, 1, 0}; // Up, Right, Down, Left
    private static final int[] DY = {0, 1, 0, -1};
    
    private int[][] maze;
    private int rows;
    private int cols;
    private ArrayList<String> path;
    private boolean[][] visited;
    
    public MazeSolver(int[][] maze) {
        this.maze = maze;
        this.rows = maze.length;
        this.cols = maze[0].length;
        this.path = new ArrayList<>();
        this.visited = new boolean[rows][cols];
    }
    
    public ArrayList<String> findPath() {
        // Try starting from each edge cell that contains 1
        // For each starting position, we'll try to reach a valid adjacent wall
        // Try from left wall
        for (int i = 0; i < rows; i++) {
            if (maze[i][0] == 1) {
                visited = new boolean[rows][cols];
                if (findPathDFS(i, 0, new ArrayList<>(), "LEFT", new String[]{"TOP", "BOTTOM"})) {
                    return path;
                }
            }
        }
        
        // Try from right wall
        for (int i = 0; i < rows; i++) {
            if (maze[i][cols-1] == 1) {
                visited = new boolean[rows][cols];
                if (findPathDFS(i, cols-1, new ArrayList<>(), "RIGHT", new String[]{"TOP", "BOTTOM"})) {
                    return path;
                }
            }
        }
        
        // Try from top wall
        for (int j = 0; j < cols; j++) {
            if (maze[0][j] == 1) {
                visited = new boolean[rows][cols];
                if (findPathDFS(0, j, new ArrayList<>(), "TOP", new String[]{"LEFT", "RIGHT"})) {
                    return path;
                }
            }
        }
        
        // Try from bottom wall
        for (int j = 0; j < cols; j++) {
            if (maze[rows-1][j] == 1) {
                visited = new boolean[rows][cols];
                if (findPathDFS(rows-1, j, new ArrayList<>(), "BOTTOM", new String[]{"LEFT", "RIGHT"})) {
                    return path;
                }
            }
        }
        
        return new ArrayList<>(); // No valid path found
    }
    
    private boolean findPathDFS(int x, int y, ArrayList<String> currentPath, String startWall, String[] validEndWalls) {
        //still working 
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
        return "NONE"; // Not on a wall
    }
    
    private boolean hasAtLeastOneTurn(ArrayList<String> path) {
        if (path.size() < 3) return false;
        
        int prevX = -1, prevY = -1;
        int directionChanges = 0;
        boolean horizontalMove = false;
        boolean verticalMove = false;
        
        for (String coord : path) {
            int x = Integer.parseInt(coord.split("\\[")[1].split("\\]")[0]);
            int y = Integer.parseInt(coord.split("\\[")[2].split("\\]")[0]);
            
            if (prevX != -1) {
                if (x == prevX && y != prevY) { // Horizontal movement
                    if (verticalMove) {
                        directionChanges++;
                    }
                    horizontalMove = true;
                    verticalMove = false;
                }
                if (x != prevX && y == prevY) { // Vertical movement
                    if (horizontalMove) {
                        directionChanges++;
                    }
                    verticalMove = true;
                    horizontalMove = false;
                }
            }
            
            prevX = x;
            prevY = y;
        }
        
        return directionChanges > 0;
    }
    
    private boolean isValid(int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }
    
    private boolean isEdge(int x, int y) {
        return x == 0 || x == rows - 1 || y == 0 || y == cols - 1;
    }
    
    private boolean isAdjacentWall(String wall1, String wall2) {
        // Define adjacent walls (walls that meet at corners)
        return;
        //still in progress 
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
    
    public static void main(String[] args) {
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

        System.out.println("Test Case 1: L-shaped path from right to bottom");
        MazeSolver solver1 = new MazeSolver(testMaze1);
        ArrayList<String> solution1 = solver1.findPath();
        printTestResult(solver1, solution1);

        System.out.println("\nTest Case 2: Zigzag path from top to bottom");
        MazeSolver solver2 = new MazeSolver(testMaze2);
        ArrayList<String> solution2 = solver2.findPath();
        printTestResult(solver2, solution2);

        System.out.println("\nTest Case 3: U-shaped path from left to right");
        MazeSolver solver3 = new MazeSolver(testMaze3);
        ArrayList<String> solution3 = solver3.findPath();
        printTestResult(solver3, solution3);

        System.out.println("\nTest Case 4: Simple turn from top to right (should not work)");
        MazeSolver solver4 = new MazeSolver(testMaze4);
        ArrayList<String> solution4 = solver4.findPath();
        printTestResult(solver4, solution4);

        System.out.println("\nTest Case 5: Multiple turns from left to top");
        MazeSolver solver5 = new MazeSolver(testMaze5);
        ArrayList<String> solution5 = solver5.findPath();
        printTestResult(solver5, solution5);
    }

    private static void printTestResult(MazeSolver solver, ArrayList<String> solution) {
        if (!solution.isEmpty()) {
            System.out.println("Path found:");
            System.out.println(solution);
            System.out.println("\nVisual representation of the path:");
            solver.printMazeWithPath();
        } else {
            System.out.println("No valid path found!");
        }
    }
} 
