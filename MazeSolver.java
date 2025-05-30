import java.util.*;

public class MazeSolver {
    static ArrayList<ArrayList<Integer>> A = new ArrayList<>(Arrays.asList(
        //INPUT MAZE HERE
        new ArrayList<>(Arrays.asList(1, 1, 0, 1, 0, 0, 0, 0 )),
        new ArrayList<>(Arrays.asList(0, 0, 0, 1, 0, 0, 1, 0 )),
        new ArrayList<>(Arrays.asList(0, 0, 0, 1, 0, 0, 0, 0 )),
        new ArrayList<>(Arrays.asList(0, 0, 1, 1, 0, 0, 0, 0 )),
        new ArrayList<>(Arrays.asList(0, 0, 1, 0, 0, 0, 0, 0 )),
        new ArrayList<>(Arrays.asList(0, 0, 1, 1, 1, 1, 0, 0 )),
        new ArrayList<>(Arrays.asList(0, 0, 0, 1, 2, 0, 0, 0 )),
        new ArrayList<>(Arrays.asList(0, 0, 7, 1, 1, 1, 1, 1 ))
    ));

    public static void main(String[] args) {
        ArrayList<String> path = findPath(A);
        System.out.println("Path Coordinates:");
        System.out.println(path);
        System.out.println("\nVisualization of path:");
        printPathOnly(path, A);
    }

    static ArrayList<String> findPath(ArrayList<ArrayList<Integer>> map) {
        int R = map.size(), C = map.get(0).size();
        boolean[][] visited = new boolean[R][C];
        ArrayList<String> path = new ArrayList<>();

        // Try all top wall entries
        for (int j = 0; j < C; j++) {
            if (map.get(0).get(j) == 1) {
                if (dfs(0, j, map, visited, path, -1, false, 0, j)) {
                    return path;
                }
            }
        }
        // Try all left wall entries except (0,0)
        for (int i = 1; i < R; i++) {
            if (map.get(i).get(0) == 1) {
                if (dfs(i, 0, map, visited, path, -1, false, i, 0)) {
                    return path;
                }
            }
        }
        return new ArrayList<>();
    }

    static boolean dfs(int i, int j, ArrayList<ArrayList<Integer>> map, boolean[][] visited,
                      ArrayList<String> path, int prevDir, boolean hasTurn, int startI, int startJ) {
        int R = map.size(), C = map.get(0).size();
        if (i < 0 || i >= R || j < 0 || j >= C) return false;
        if (visited[i][j] || map.get(i).get(j) != 1) return false;

        visited[i][j] = true;
        path.add("A[" + i + "][" + j + "]");

        if (isExit(i, j, R, C, startI, startJ) && hasTurn) {
            return true;
        }

        int[][] dirs = {{-1,0}, {1,0}, {0,-1}, {0,1}};
        for (int d = 0; d < 4; d++) {
            int ni = i + dirs[d][0], nj = j + dirs[d][1];
            boolean turned = hasTurn || (prevDir != -1 && d != prevDir);
            if (dfs(ni, nj, map, visited, path, d, turned, startI, startJ)) {
                return true;
            }
        }

        visited[i][j] = false;
        path.remove(path.size() - 1);
        return false;
    }

    static boolean isExit(int i, int j, int R, int C, int startI, int startJ) {
        // Must be on a wall
        boolean onEdge = i == 0 || j == 0 || i == R-1 || j == C-1;
        if (!onEdge) return false;

        // Started on top wall - must end on left or right wall
        if (startI == 0) {
            return j == 0 || j == C-1;
        }
        // Started on left wall - must end on top or bottom wall
        if (startJ == 0) {
            return i == 0 || i == R-1;
        }
        return false;
    }

    static void printPathOnly(ArrayList<String> path, ArrayList<ArrayList<Integer>> map) {
        int R = map.size(), C = map.get(0).size();
        Set<String> onPath = new HashSet<>(path);
        for (int i = 0; i < R; i++) {
            System.out.print("[ ");
            for (int j = 0; j < C; j++) {
                String key = "A[" + i + "][" + j + "]";
                System.out.print(onPath.contains(key) ? "1" : " ");
                if (j < C-1) System.out.print(" , ");
            }
            System.out.println(" ]");
        }
    }
}
