package pro_edu.bfs.yesoo;

import java.io.*;
import java.util.*;

class Main {
	
	public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	public static StringTokenizer st;
	
	
	static class Coord{
		int row,col;

		public Coord(int row, int col) {
			super();
			this.row = row;
			this.col = col;
		}
		
	}

	static String MAP[] = new String[8];
	static int visited[][] = new int[8][9];
	static int dr[] = {-1,1,0,0};
	static int dc[] = {0,0,-1,1};
	static Queue<Coord> getStartQueue(){

		Queue<Coord> q_sea = new LinkedList<>(); // 다른 섬을 찾을 때 활용할 queue
		
		Queue<Coord> q_land = new LinkedList<>();
		q_land.add(new Coord(7, 0));
		visited[7][0] = 1;
		
		while(!q_land.isEmpty()) {
			Coord now = q_land.poll();
			for(int i = 0; i < 4; i++) {
				int nr = now.row + dr[i];
				int nc = now.col + dc[i];
				if(nr < 0 || nc < 0 || nr >= 8 || nc >= 9)
					continue;
				if(visited[nr][nc] != 0) 
					continue;
				visited[nr][nc] = 1;
				if(MAP[nr].charAt(nc) == '#')
					// 땅
					q_land.add(new Coord(nr, nc));
				else
					// 바다
					q_sea.add(new Coord(nr, nc));
			}
		}
		return q_sea;
	}
	

	public static void main(String[]args) throws Exception {
		for(int i = 0; i < 8; i++)
			MAP[i] = br.readLine();
		
		Queue<Coord> q_sea = getStartQueue();
		
		while(!q_sea.isEmpty()) {
			Coord now = q_sea.poll();
			for(int i = 0; i < 4; i++) {
				int nr = now.row + dr[i];
				int nc = now.col + dc[i];
				if(nr < 0 || nc < 0 || nr >= 8 || nc >= 9) continue;
				if(visited[nr][nc] != 0) continue;
				
				visited[nr][nc] = visited[now.row][now.col] + 1;
				
				if(MAP[nr].charAt(nc) == '_')
					q_sea.add(new Coord(nr, nc));
				else {
					System.out.println(visited[now.row][now.col]);
					return;
				}
			}
		}
	}
}

class Picture {
	int x;
	int y;
	int value;
	public Picture(int x, int y, int value) {
		super();
		this.x = x;
		this.y = y;
		this.value = value;
	}
}
