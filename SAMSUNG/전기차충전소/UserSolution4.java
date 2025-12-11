package 전기차충전소;

import java.io.*;
import java.util.*;

class UserSolution4 {
	
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
		int price;
		public Edge(int to, int cost, int price) {
			super();
			this.to = to;
			this.cost = cost;
			this.price = price;
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
	int[] prices;
	
	public void init(int N, int[] mCost, int K, int[] mId, int[] sCity, int[] eCity, int[] mDistance) {
		this.N = N;
		this.prices = mCost;
		this.hm = new HashMap<>();
		this.al = new ArrayList[N];
		for (int i = 0; i < N; i++) {
			al[i] = new ArrayList<>();
		}
		for (int i = 0; i < K; i++) {
			add(mId[i], sCity[i], eCity[i], mDistance[i]);
		}
		dist = new int[N][2001];
		return;
    }

    public void add(int mId, int sCity, int eCity, int mDistance) {
    	Node node = new Node(mId, sCity, eCity, mDistance);
    	hm.put(mId, node);
    	al[sCity].add(node); // 단방향
        return;
    }

    public void remove(int mId) {
    	Node node = hm.get(mId);
    	node.isDeleted = true;
        return;
    }

    int cost(int sCity, int eCity) {
    	PriorityQueue<Edge> pq = new PriorityQueue<>();
    	pq.add(new Edge(sCity, 0, prices[sCity]));
    	
    	for (int i = 0; i < N; i++) {
    		for (int j = 0; j < 2001; j++) {
    			dist[i][j] = Integer.MAX_VALUE;
    		}
    	}
    	
    	dist[sCity][0] = 0;
    	
      	while (!pq.isEmpty()) {
     		Edge now = pq.poll();
    		if (now.cost > dist[now.to][now.price]) {
    			continue;
    		}
    		if (now.to == eCity) return now.cost;
    		
    		for (Node next : al[now.to]) {
    			if (next.isDeleted) continue;
    			int cost = now.cost + next.cost * now.price;
    			if (cost < dist[next.to][now.price]) {
    				dist[next.to][now.price] = cost;
    				int minPrice = Math.min(now.price, prices[next.to]);
    				pq.add(new Edge(next.to, cost, minPrice));
    			}
    		}
    	}
    	
    	
        return -1;
    }
}