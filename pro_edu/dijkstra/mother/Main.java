package pro_edu.dijkstra.mother;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {

	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int x = Integer.parseInt(st.nextToken());
		int y = Integer.parseInt(st.nextToken());
	
//		st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(br.readLine());
		
		Node[][] map = new Node[N][N];
		
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				int cost = Integer.parseInt(st.nextToken());
				map[i][j] = new Node(i, j, cost);
			}			
		}
		
		PriorityQueue<Node> pq = new PriorityQueue<>();
		int[][] dist = new int[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				dist[i][j] = Integer.MAX_VALUE;
			}
		}
		
//		pq.add(new Node(x, y, 0));
		pq.add(new Node(x, y, map[x][y].cost));
//		dist[x][y] = 0;
		dist[x][y] = map[x][y].cost;
		
		int[] dr = {-1, 0, 1, 0};
		int[] dc = {0, 1, 0, -1};
		int ansCost = 0;
		while(!pq.isEmpty()) {
			Node now = pq.poll();
			if (dist[now.x][now.y] < now.cost) continue;
			ansCost = now.cost;
			for (int i = 0; i < 4; i++) {
				int nr = now.x + dr[i];
				int nc = now.y + dc[i];
				
				if (nr < 0 || nc < 0 || nr >= N || nc >= N) continue;
				
				if (map[nr][nc].cost == -1) continue; // 벽, 이동불가

				int nextCost = map[nr][nc].cost + now.cost;
				
				if (dist[nr][nc] <= nextCost) continue;
				
				dist[nr][nc] = nextCost;
				pq.add(new Node(nr, nc, nextCost));
			}
		}
//		for (int i = 0; i < N; i++) {
//			for (int j = 0; j < N; j++) {
//				System.out.print(dist[i][j] + " ");
//			}			
//			System.out.println();
//		}
		System.out.println(ansCost);
	}
}

class Node implements Comparable<Node> {
	int x;
	int y;
	int cost;
	public Node(int x, int y, int cost) {
		super();
		this.x = x;
		this.y = y;
		this.cost = cost;
	}
	@Override
	public int compareTo(Node o) {
//		return Integer.compare(o.cost, this.cost);
		if (this.cost != o.cost) return this.cost - o.cost;
		if (this.x != o.x) return this.x - o.x;
		if (this.y != o.y) return this.y - o.y;
		return 0;
	}
}