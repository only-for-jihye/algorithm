package 전기차충전;

import java.io.*;
import java.util.*;

class UserSolution2 {
	
	class Node {
		int mId;
		int sCity;
		int eCity;
		int mDistance;
		boolean isDeleted;
		public Node(int mId, int sCity, int eCity, int mDistance) {
			super();
			this.mId = mId;
			this.sCity = sCity;
			this.eCity = eCity;
			this.mDistance = mDistance;
			this.isDeleted = false;
		}
	}
	
	class Edge implements Comparable<Edge> {
		int eCity;
		int cost;
		int price;
		public Edge(int eCity, int cost, int price) {
			super();
			this.eCity = eCity;
			this.cost = cost;
			this.price = price;
		}
		@Override
		public int compareTo(Edge o) {
			if (this.cost != o.cost) return Integer.compare(this.cost, o.cost);
			return Integer.compare(this.price, o.price);
		}
	}
	
	int N;
	HashMap<Integer, Node> hm;
	ArrayList<Node>[] al;
	int[][] dist;
	int[] prices;
	
	
	public void init(int N, int[] mCost, int K, int[] mId, int[] sCity, int[] eCity, int[] mDistance) {
		this.N = N;
		hm = new HashMap<>();
		al = new ArrayList[N];
		prices = mCost;
		for (int i = 0; i < N; i++) {
			al[i] = new ArrayList<>();
		}
		for (int i = 0; i < K; i++) {
			add(mId[i], sCity[i], eCity[i], mDistance[i]);
		}
		dist = new int[N][2001]; // cost
    }

    public void add(int mId, int sCity, int eCity, int mDistance) {
    	Node newNode = new Node(mId, sCity, eCity, mDistance);
    	hm.put(mId, newNode);
    	al[sCity].add(newNode);
    }

    public void remove(int mId) {
    	Node deleteNode = hm.get(mId);
    	deleteNode.isDeleted = true;
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
    		
//    		if (dist[now.eCity][now.price] < now.cost) continue;
    		
    		if (now.eCity == eCity) return now.cost;
    		
    		for (Node next : al[now.eCity]) {
    			if (next.isDeleted) continue;
    			
    			int nextCost = now.cost + next.mDistance * now.price;
    			int nextPrice = Math.min(now.price, prices[next.eCity]);
    			
    			if (nextCost < dist[next.eCity][nextPrice]) {
    				dist[next.eCity][nextPrice] = nextCost;
    				pq.add(new Edge(next.eCity, nextCost, nextPrice));
    			}
    		}
    	}
    	
        return -1;
    }
}




