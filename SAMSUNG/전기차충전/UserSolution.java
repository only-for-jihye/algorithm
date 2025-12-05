package 전기차충전;

import java.io.*;
import java.util.*;

class UserSolution {
		
	class Road implements Comparable<Road> {
		int mId;
		int eCity;
		int cost;
		int price;
		boolean isDeleted;
		public Road(int mId, int eCity, int cost, int price) {
			super();
			this.mId = mId;
			this.eCity = eCity;
			this.cost = cost;
			this.price = price;
			this.isDeleted = false;
		}
		@Override
		public int compareTo(Road o) {
			if (this.cost!= o.cost) return Integer.compare(this.cost, o.cost);
			if (this.price != o.price) return Integer.compare(this.price, o.price);
			return Integer.compare(this.mId, o.mId);
		}
	}
	
	
	int[] charger; // seq:도시번호, value:충전단가 
	int N;
	ArrayList<Road>[] roads;
	HashMap<Integer, Road> roadMap;
	
	public void init(int N, int[] mCost, int K, int[] mId, int[] sCity, int[] eCity, int[] cost) {
		this.N = N;
		charger = mCost;
		roads = new ArrayList[N];
		roadMap = new HashMap<>();
		for (int i = 0; i < N; i++) {
			roads[i] = new ArrayList<>();
		}
		for (int i = 0; i < K; i++) {
			add(mId[i], sCity[i], eCity[i], cost[i]);
		}
    }

	// 1,400
    public void add(int mId, int sCity, int eCity, int cost) {
    	Road road = new Road(mId, eCity, cost, 0);
    	roadMap.put(mId, road);
    	roads[sCity].add(road);
    }

    // 500
    public void remove(int mId) {
    	Road road = roadMap.get(mId);
    	road.isDeleted = true;
    }

    // 100
    int cost(int sCity, int eCity) {
    	PriorityQueue<Road> pq = new PriorityQueue<>();
    	pq.add(new Road(-1, sCity, 0, charger[sCity]));
    	
    	int[][] dist = new int[N][2001]; // [도시번호][cost]
    	
    	for (int i = 0; i < N; i++) {
    		for (int j = 0; j < 2001; j++) {
    			dist[i][j] = Integer.MAX_VALUE;
    		}
    	}
    	
    	dist[sCity][0] = 0;
    	while (!pq.isEmpty()) {
    		Road now = pq.poll();
    		
    		if (dist[now.eCity][now.price] < now.cost) continue;
    		
    		if (now.eCity == eCity) return now.cost;
    		
    		for (Road next : roads[now.eCity]) {
    			if (next.isDeleted) continue;
    			
    			int nextCost = now.cost + now.price * next.cost;
    			int nextPrice = Math.min(now.price, charger[next.eCity]);
    			
    			if (dist[next.eCity][nextPrice] <= nextCost) continue;
    			dist[next.eCity][nextPrice] = nextCost;
    			pq.add(new Road(next.mId, next.eCity, nextCost, nextPrice));
    			
    		}
    	}
    	
        return -1;
    }
}




