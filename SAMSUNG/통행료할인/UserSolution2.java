package 통행료할인;

import java.io.*;
import java.util.*;

class UserSolution2 {
	
	Map<Integer, Road> roadMap;
	List<Road> roadList[];
	int N;
	
	public void init(int N, int K, int[] mId, int[] sCity, int[] eCity, int mToll[]) {
		roadMap = new HashMap<>();
		roadList = new ArrayList[N];
		this.N = N;
		for (int i = 0; i < N; i++) {
			roadList[i] = new ArrayList<>();
		}
		for (int i = 0; i < K; i++) {
			add(mId[i], sCity[i], eCity[i], mToll[i]);
		}
	}

	public void add(int mId, int sCity, int eCity, int mToll) {
		Road road = new Road(mId, sCity, eCity, mToll);
		roadList[sCity].add(road);
		roadMap.put(mId, road);
	}

	public void remove(int mId) {
		Road road = roadMap.get(mId);
		roadMap.remove(mId);
		road.isDeleted = true; // 객체 상태로 저장하기 때문에, 여기서 바꿔도 roadList에 있는 동일한 road 객체의 isDelete가 변경됨
	}

	public int cost(int M, int sCity, int eCity) {
		int dist[][] = new int[N][M + 1];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M + 1; j++) {
				dist[i][j] = Integer.MAX_VALUE;
			}
		}
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		for (int i = 0; i < M + 1; i++) {
			dist[sCity][i] = 0; // 비용 0으로 초기 세팅
		}
		pq.add(new Edge(sCity, 0, 0)); // 시작점 세팅
		while (!pq.isEmpty()) {
			Edge now = pq.poll();
			if (dist[now.dest][now.discount] < now.cost) continue;
			if (now.dest == eCity) return now.cost;
			
			for (Road road : roadList[now.dest]) {
				if (road.isDeleted) continue;
				// 할인권 사용
				if (now.discount < M &&
						now.cost + (road.cost / 2) < dist[road.eCity][now.discount + 1]) {
					dist[road.eCity][now.discount + 1] = now.cost + (road.cost / 2);
					pq.add(new Edge(road.eCity, dist[road.eCity][now.discount + 1], now.discount + 1));
				}
				
				// 할인권 미사용
				if (now.cost + road.cost < dist[road.eCity][now.discount]) {
					dist[road.eCity][now.discount] = now.cost + road.cost;
					pq.add(new Edge(road.eCity, dist[road.eCity][now.discount], now.discount));
				}
			}
			
		}
		
		return -1;
	}
	
	class Edge implements Comparable<Edge> {
		int dest;
		int cost;
		int discount;
		public Edge(int dest, int cost, int discount) {
			super();
			this.dest = dest;
			this.cost = cost;
			this.discount = discount;
		}
		@Override
		public int compareTo(Edge o) {
			return Integer.compare(this.cost, o.cost);
		}
	}
	
	class Road {
		int id;
		int sCity;
		int eCity;
		int cost;
		boolean isDeleted;
		public Road(int id, int sCity, int eCity, int cost) {
			super();
			this.id = id;
			this.sCity = sCity;
			this.eCity = eCity;
			this.cost = cost;
			this.isDeleted = false;
		}
	}
}