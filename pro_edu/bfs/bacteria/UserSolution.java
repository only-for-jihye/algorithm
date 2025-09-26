package pro_edu.bfs.bacteria;

import java.io.*;
import java.util.*;

class UserSolution {
	class Node implements Comparable<Node>{
		int row;
		int col;
		int dist; // 처음 심어진 박테리아 위치로부터의 거리
		
		public Node(int row, int col) {
			super();
			this.row = row;
			this.col = col;
			this.dist = 0;
		}
		
		public Node(int row, int col, int dist) {
			super();
			this.row = row;
			this.col = col;
			this.dist = dist;
		}

		@Override
		public int compareTo(UserSolution.Node o) {
			// 거리가 짧은 순
//			if (this.dist < o.dist) return -1;
//			if (this.dist > o.dist) return 1;
			if (this.dist != o.dist) return this.dist - o.dist;
			// row가 작은 순
//			if (this.row < o.row) return -1;
//			if (this.row > o.row) return 1;
			if (this.row != o.row) return this.row - o.row;
			// col이 작은 순
//			if (this.col < o.col) return -1;
//			if (this.col > o.col) return 1;
			if (this.col != o.col) return this.col - o.col;
			return 0;
		}
	}
	// 2차원 map <- 박테리아 심기(정보? => id)
	int map[][]; // [row][col] = 해당 좌표의 박테리아 id
	// 박테리아 정보 <- 수명(언제까지 살아있는가?)
	int life[]; // [박테리아 id] = 해당 박테리아가 언제까지 살아있을 수 있는가?
	int N;
	boolean[][] visited;
	
	int[] dr = {-1, 0, 1, 0};
	int[] dc = {0, 1, 0, -1};
	
    public void init(int N) {
    	this.N = N;
    	map = new int[N + 1][N + 1];
    	life = new int[3001];
//    	visited = new boolean[N + 1][N + 1];
    	return; 
    }
    

    boolean sizePossible(int mTime, int startRow, int startCol, Main.Bacteria bac) {
    	// 실제로 심기전에
    	// Srow, Scol에서부터 원하는 크기만큼의 공간은 존재하는가?
    	Queue<Node> q = new LinkedList<>();
    	boolean[][] visited = new boolean[N + 1][N + 1];
    	q.add(new Node(startRow, startCol));
    	visited[startRow][startCol] = true;
    	
    	int count = 0;
    	while (!q.isEmpty()) {
    		Node current = q.poll();
    		count++;
    		int currentRow = current.row;
    		int currentCol = current.col;
    		if (count >= bac.size) return true;
    		for (int i = 0; i < 4; i++) {
    			int nr = currentRow + dr[i];
    			int nc = currentCol + dc[i];
    			if (nr <= 0 || nc <= 0 || nr > N || nc > N) continue;
    			if (visited[nr][nc]) continue;
    			if (life[map[nr][nc]] > mTime) continue;
    			visited[nr][nc] = true;
    			q.add(new Node(nr, nc));
    		}
    	}
    	return false;
    }
    
    Main.Result put(int mTime, int startRow, int startCol, Main.Bacteria bac) { // 실제로 박테리아 심기
    	// BFS를 통해 bacteria가 퍼져 나가도록
    	//   => 박테리아가 퍼져나가는데, 우선순위가 있음
    	
    	life[bac.id] = mTime + bac.time; // life 세팅
    	
    	PriorityQueue<Node> pq = new PriorityQueue<>();// <- 박테리아가 심어질 예정인 좌표'들'
    	boolean[][] visited = new boolean[N + 1][N + 1];
    	int count = 0;
    	pq.add(new Node(startRow, startCol, 0));
    	visited[startRow][startCol] = true;
    	
    	Main.Result rslt = new Main.Result(); 
    	
    	while (!pq.isEmpty()) {
    		Node current = pq.poll();
    		count++; // 심어질때마다 (꺼낼때마다) 카운팅
    		map[current.row][current.col] = bac.id;
    		if (count >= bac.size) {
    			rslt.row = current.row;
    			rslt.col = current.col;
    			return rslt;
    		}
    		int currentRow = current.row;
    		int currentCol = current.col;
    		for (int i = 0; i < 4; i++) {
//    			if (count >= bac.size) return true;
    			int nr = currentRow + dr[i];
    			int nc = currentCol + dc[i];
    			if (nr <= 0 || nc <= 0 || nr > N || nc > N) continue;
    			if (visited[nr][nc]) continue;
    			if (life[map[nr][nc]] > mTime) continue;
    			visited[nr][nc] = true;
    			int dist = Math.abs(nr - startRow) + Math.abs(nc - startCol);
    			pq.add(new Node(nr, nc, dist));
    		}
    	}
    	return rslt;
    }
    
    // BFS : 반복되는 횟수 == edge 개수 <- Queue사용시
    // PQ사용시? => log8,000 = 13
    // 2000*4 => 8,000 * 3,000  * 13(pq사용시)
    // 24,000,000 * 13(pq사용)
    public Main.Result putBacteria(int mTime, int mRow, int mCol, Main.Bacteria mBac) {
    	Main.Result ret = new Main.Result(); 
    	if (life[map[mRow][mCol]] > mTime) return ret;
    	if(sizePossible(mTime, mRow, mCol, mBac)) { // 심을 공간이 충분?
    		ret = put(mTime, mRow, mCol, mBac); // 실제로 심기!
    	}
    	return ret;
    }
    
    // 1 * 100
    public int killBacteria(int mTime, int mRow, int mCol) {
    	// 해당 좌표의 박테리아 수명을 0으로
    	int id = map[mRow][mCol];
    	if (life[id] <= mTime) return 0;
    	life[id] = 0;
    	return id;
    }
    // 1 * 100
    public int checkCell(int mTime, int mRow, int mCol) {
    	// 해당 좌표의 박테리아 수명이 mTime 이상인지?
    	int id = map[mRow][mCol];
    	if (life[id] <= mTime) return 0;
    	return id; 
    }
//    public int killBacteria(int mTime, int mRow, int mCol) {
//    	if(life[map[mRow][mCol]] <= mTime) return 0;
//    	// 해당 좌표의 박테리아 수명을 0으로
//    	life[map[mRow][mCol]] = 0;
//    	return map[mRow][mCol]; 
//    }
//    // 1 * 100
//    public int checkCell(int mTime, int mRow, int mCol) {
//    	if(life[map[mRow][mCol]] <= mTime) return 0;
//    	// 해당 좌표의 박테리아 수명이 mTime 이상인지?
//    	return map[mRow][mCol];
//    }
}