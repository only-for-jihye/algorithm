package seaTurnel;

import java.util.*;
import java.io.*;

public class Main {

	static String[] map;
	static int[][] visited;
	static int dr[] = {-1 ,1, 0, 0};
	static int dc[] = {0, 0, -1, 1};
	static int row;
	static int col;
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		row = Integer.parseInt(st.nextToken());
		col = Integer.parseInt(st.nextToken());
		
		map = new String[row];
		
		for (int i = 0; i < row; i++) {
			map[i] = br.readLine();
		}
		
		Queue<Grid> q_water = bfs();
		
		while(!q_water.isEmpty()) {
			Grid now = q_water.poll();
			for(int i = 0; i < 4; i++) {
				int nr = now.row + dr[i];
				int nc = now.col + dc[i];
				if(nr < 0 || nc < 0 || nr >= row|| nc >= col) continue;
				if(visited[nr][nc] != 0) continue;
				
				visited[nr][nc] = visited[now.row][now.col] + 1;
				
				if(map[nr].charAt(nc) == '.') {
					q_water.add(new Grid(nr, nc));
				} else {
					System.out.println(visited[now.row][now.col]);
					return;
				}
			}
		}
	
	}
	
	static Queue<Grid> bfs(){

		Queue<Grid> q_water = new LinkedList<>();
		Queue<Grid> q_land  = new LinkedList<>();
		
		visited = new int[row][col];
		
		boolean find = false;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (map[i].charAt(j) == 'X') {
					q_land.add(new Grid(i, j));
					visited[i][j] = 1;
					find = true;
					break;
				}
			}
			if (find) break;
		}

		
		while(!q_land.isEmpty()) {
			Grid current = q_land.poll();
			for(int i = 0; i < 4; i++) {
				int nr = current.row + dr[i];
				int nc = current.col + dc[i];
				if(nr < 0 || nc < 0 || nr >= row || nc >= col) continue;
				if(visited[nr][nc] != 0) continue;
				visited[nr][nc] = 1;
				if(map[nr].charAt(nc) == 'X') {
					q_land.add(new Grid(nr, nc));
				} else {
					q_water.add(new Grid(nr, nc));
				}
			}
		}
		return q_water;
	}
	
}

class Grid {
	int row, col;
	
	public Grid(int row, int col) {
		super();
		this.row = row;
		this.col = col;
	}
}