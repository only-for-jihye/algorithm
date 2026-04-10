package 통행료할인;

import java.io.*;
import java.util.*;

class UserSolution {
	
	class Road {
		int id;
		int to;
		int cost;
		boolean isDeleted;
		public Road(int id, int to, int cost) {
			super();
			this.id = id;
			this.to = to;
			this.cost = cost;
			this.isDeleted = false;
		}
	}
	
	class Edge implements Comparable<Edge> {
		int to;
		int cost;
		int discount;
		
		public Edge(int to, int cost, int discount) {
			super();
			this.to = to;
			this.cost = cost;
			this.discount = discount;
		}
		@Override
		public int compareTo(Edge o) {
			return Integer.compare(this.cost, o.cost);
		}
		
	}
	
	int N;
	HashMap<Integer, Road> hm;
	ArrayList<Road>[] al;
	
	
	public void init(int N, int K, int[] mId, int[] sCity, int[] eCity, int mToll[]) {
		this.N = N;
		hm = new HashMap<>();
		al = new ArrayList[N];
		for (int i = 0; i < N; i++) {
			al[i] = new ArrayList<>();
		}
		for (int i = 0; i < K; i++) {
			add(mId[i], sCity[i], eCity[i], mToll[i]);
		}
		return;
	}

	public void add(int mId, int sCity, int eCity, int mToll) {
		Road road = new Road(mId, eCity, mToll);
		hm.put(mId, road);
		al[sCity].add(road);
		return;
	}

	public void remove(int mId) {
		Road road = hm.get(mId);
		road.isDeleted = true;
		return;
	}

	public int cost(int M, int sCity, int eCity) {
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		// dist 배열 선언
		int[][] dist = new int[N][M + 1];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M + 1; j++) {
				dist[i][j] = Integer.MAX_VALUE;
			}
		}
		// 초기 비용 0
		for (int i = 0; i < M + 1; i++) {
			dist[sCity][i] = 0;
		}
		pq.add(new Edge(sCity, 0, 0));
		
		while (!pq.isEmpty()) {
			Edge now = pq.remove();
			
//			if (dist[now.to][now.discount] < now.cost) continue;
			if (now.to == eCity) return now.cost;
			
			for (Road next : al[now.to]) {
				if (next.isDeleted) continue;
				// 할인권 사용
				if (now.discount < M
						&& now.cost + (next.cost / 2) < dist[next.to][now.discount + 1]) {
					dist[next.to][now.discount + 1] = now.cost + (next.cost / 2);
					pq.add(new Edge(next.to, dist[next.to][now.discount + 1], now.discount + 1));
				}
				
				// 할인권 미사용
				if (now.cost + next.cost < dist[next.to][now.discount]) {
					dist[next.to][now.discount] = now.cost + next.cost;
					pq.add(new Edge(next.to, dist[next.to][now.discount], now.discount));
				}
			}
		}
		return -1;
	}
}
