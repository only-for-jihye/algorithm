package 통행료할인;

import java.io.*;
import java.util.*;

class UserSolution3 {
	
	class Node {
		int id;
		int from;
		int to;
		int cost;
		boolean isDeleted;
		public Node(int id, int from, int to, int cost) {
			super();
			this.id = id;
			this.from = from;
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
	HashMap<Integer, Node> hm;
	ArrayList<Node>[] al;
	int[][] dist;
	
	
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
		Node node = new Node(mId, sCity, eCity, mToll);
		hm.put(mId, node);
		al[sCity].add(node);
		return;
	}

	public void remove(int mId) {
		Node node = hm.get(mId);
		node.isDeleted = true;
		return;
	}

	public int cost(int M, int sCity, int eCity) {
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		pq.add(new Edge(sCity, 0, 0));
		dist = new int[N][M + 1];
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M + 1; j++) {
				dist[i][j] = Integer.MAX_VALUE;
			}
		}
		
		for (int i = 0; i < M + 1; i++) {
			dist[sCity][i] = 0;
		}
		
		while (!pq.isEmpty()) {
			Edge now = pq.poll();
			
			if (now.cost > dist[now.to][now.discount]) continue;
			
			if (now.to == eCity) return now.cost;
			
			for (Node next : al[now.to]) {
				if (next.isDeleted) continue;
				// 할인권 사용
				int costDiscount = now.cost + (next.cost / 2);
				if (now.discount < M && costDiscount < dist[next.to][now.discount + 1]) {
					dist[next.to][now.discount + 1] = costDiscount;
					pq.add(new Edge(next.to, costDiscount, now.discount + 1));
				}
				
				// 할인권 미사용
				int cost = now.cost + next.cost;
				if (cost < dist[next.to][now.discount]) {
					dist[next.to][now.discount] = cost;
					pq.add(new Edge(next.to, cost, now.discount));
				}
			}
		}
		
		return -1;
	}
}