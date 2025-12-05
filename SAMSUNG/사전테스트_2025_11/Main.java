package 사전테스트_2025_11;

import java.io.*;
import java.util.*;

public class Main {
	
    static int H, W;
    static char[][] grid;
    static int[] dr = {-1, 0, 1, 0}; // 동서남북
    static int[] dc = {0, 1, 0, -1};
    static char[] direction = {'^', '>', 'v', '<'};

    static class Edge {
        int row, col;
		public Edge(int row, int col) {
			super();
			this.row = row;
			this.col = col;
		}
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        H = Integer.parseInt(st.nextToken());
        W = Integer.parseInt(st.nextToken());
        grid = new char[H][W];
        
        int hashCnt = 0;

        for (int i = 0; i < H; i++) {
            String line = br.readLine();
            
            for (int j = 0; j < W; j++) {
                grid[i][j] = line.charAt(j);
                if (grid[i][j] == '#') {
                	hashCnt++;
                }
            }
        }

        ArrayList<Edge> search = new ArrayList<>();

        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                if (grid[i][j] == '#') {
                    int end = 0;
                    for (int d = 0; d < 4; d++) {
                        if (isMovePossible(i, j, d)) {
                        	end++;
                        }
                    }
                    if (end == 1) {
                        search.add(new Edge(i, j));
                    }
                }
            }
        }

        Collections.sort(search, (edge1, edge2) -> {
            if (edge1.row != edge2.row) return edge2.row - edge1.row;
            return edge2.col - edge1.col;
        });

        for (Edge start : search) {
            int startDir = -1;
            for (int i = 0; i < 4; i++) {
                if (isMovePossible(start.row, start.col, i)) {
                    startDir = i;
                    break;
                }
            }

            if (startDir != -1) {
                String path = getAnswer(start, startDir, hashCnt);
                if (path != null) {
                    System.out.println((start.row + 1) + " " + (start.col + 1));
                    System.out.println(direction[startDir]);
                    System.out.println(path);
                    return;
                }
            }
        }
    }

    public static String getAnswer(Edge start, int startDir, int hashCnt) {
        StringBuilder commands = new StringBuilder();
        boolean[][] visited = new boolean[H][W];
        
        int cur_r = start.row;
        int cur_c = start.col;
        int cur_direction = startDir;
        
        visited[cur_r][cur_c] = true;
        int visitCnt = 1;

        while (true) {
            int nextDir = -1;
            for (int d = 0; d < 4; d++) {
                if (isMovePossible(cur_r, cur_c, d)) {
                    int next_nr = cur_r + (dr[d] * 2);
                    int next_nc = cur_c + (dc[d] * 2);
                    if (!visited[next_nr][next_nc]) {
                        nextDir = d;
                        break;
                    }
                }
            }
            if (nextDir == -1) break;

            int diff = (nextDir - cur_direction + 4) % 4;
            if (diff == 3) {
                commands.append("L");
            } else if (diff == 1) {
                commands.append("R");
            }
            commands.append("A");
            
            int next_nr = cur_r + (dr[nextDir] * 2);
            int next_nc = cur_c + (dc[nextDir] * 2);

            visited[next_nr][next_nc] = true;
            visitCnt += 2;
            
            cur_r = next_nr;
            cur_c = next_nc;
            cur_direction = nextDir;
        }

        if (visitCnt == hashCnt) {
            return commands.toString();
        }
        return null;
    }

    public static boolean isMovePossible(int r, int c, int d) {
        int nr = r + dr[d];
        int nc = c + dc[d];
        int next_nr = r + (dr[d] * 2);
        int next_nc = c + (dc[d] * 2);

        if (nr >= 0 && nr < H && nc >= 0 && nc < W &&
        		next_nr >= 0 && next_nr < H && next_nc >= 0 && next_nc < W) {
            return grid[nr][nc] == '#' && grid[next_nr][next_nc] == '#';
        }
        return false;
    }
}