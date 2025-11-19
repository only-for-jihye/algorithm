package coffeeBakery;

import java.util.*;

class UserSolution {
	
	// 건물 번호 : 0 ~ N-1
	int N;
	// 양방향으로 연결되어 있음
	// 도로가 추가될 때, 거리 정보까지 함께 ..
	ArrayList<Road>[] roads;
	// 커피 건물 : M개
	// 제과점 건물 : P개
	int[] cafeBakery;
	// 제한 거리 : R
	
	// -> 각 거리의 최솟값을 구해야한다.
	PriorityQueue<Edge> cafe_pq;
	PriorityQueue<Edge> bakery_pq;
	PriorityQueue<Edge> pq;
	
	// 커피점 거리
	int[] coffeeDist;
	// 제과점 거리
	int[] bakeryDist;
	
	public void init(int N, int K, int sBuilding[], int eBuilding[], int mDistance[]) {
		this.N = N;
		roads = new ArrayList[N];
		coffeeDist = new int[N];
		bakeryDist = new int[N];
		for (int i = 0; i < N; i++) {
			roads[i] = new ArrayList<>();
		}
		for (int i = 0; i < K; i++) {
			roads[sBuilding[i]].add(new Road(sBuilding[i], eBuilding[i], mDistance[i]));
			roads[eBuilding[i]].add(new Road(eBuilding[i], sBuilding[i], mDistance[i]));
		}
	}

	public void add(int sBuilding, int eBuilding, int mDistance) {
		roads[sBuilding].add(new Road(sBuilding, eBuilding, mDistance));
		roads[eBuilding].add(new Road(eBuilding, sBuilding, mDistance));
	}

	public int calculate(int M, int mCoffee[], int P, int mBakery[], int R) {
		cafe_pq = new PriorityQueue<>();
		bakery_pq = new PriorityQueue<>();
		pq = new PriorityQueue<>();
		
		cafeBakery = new int[N];
		int cafeStart = 0;
		int bakeryStart = 0;
		for (int i = 0; i < M; i++) {
			cafeStart = mCoffee[i];
			cafeBakery[mCoffee[i]] = 1; // cafe
//			cafe_pq.add(new Edge(mCoffee[i], 0));
			pq.add(new Edge(mCoffee[i], 0, 1));
		}
		coffeeDist[cafeStart] = 0;
		
		for (int i = 0; i < P; i++) {
			bakeryStart = mBakery[i];
			cafeBakery[mBakery[i]] = 2; // bakery
//			bakery_pq.add(new Edge(mBakery[i], 0));
			pq.add(new Edge(mBakery[i], 0, 2));
		}
		bakeryDist[bakeryStart] = 0;
		
		for (int i = 0; i < N; i++) {
			coffeeDist[i] = Integer.MAX_VALUE;
			bakeryDist[i] = Integer.MAX_VALUE;
		}
		
//		while (!cafe_pq.isEmpty()) {
//			Edge now = cafe_pq.poll();
//			if (now.dist > R) {
//				continue;
//			}
//			if (now.dist > coffeeDist[now.dest]) {
//				continue;
//			}
//			for (Road road : roads[now.dest]) {
//				if (now.dist + road.dist < coffeeDist[road.eCity]) {
//					coffeeDist[road.eCity] = now.dist + road.dist;
//					cafe_pq.add(new Edge(road.eCity, coffeeDist[road.eCity]));
//				}
//			}
//		}
//		while (!bakery_pq.isEmpty()) {
//			Edge now = bakery_pq.poll();
//			if (now.dist > R) {
//				continue;
//			}
//			if (now.dist > bakeryDist[now.dest]) {
//				continue;
//			}
//			for (Road road : roads[now.dest]) {
//				if (now.dist + road.dist < bakeryDist[road.eCity]) {
//					bakeryDist[road.eCity] = now.dist + road.dist;
//					bakery_pq.add(new Edge(road.eCity, bakeryDist[road.eCity]));
//				}
//			}
//		}
		int minValue = Integer.MAX_VALUE;
		while (!pq.isEmpty()) {
			Edge now = pq.poll();
			if (now.dist > R) {
				continue;
			}
			if (cafeBakery[now.dest] != 1 && cafeBakery[now.dest] != 2) {
				if (coffeeDist[now.dest] <= R && bakeryDist[now.dest] <= R) {
					minValue = Math.min(minValue, coffeeDist[now.dest] + bakeryDist[now.dest]);
				}
			}
			if (minValue != Integer.MAX_VALUE && !pq.isEmpty() && pq.peek().dist >= minValue) break;
			
			for (Road road : roads[now.dest]) {
				if (now.type == 1) {
					if (now.dist + road.dist < coffeeDist[road.eCity]) {
						coffeeDist[road.eCity] = now.dist + road.dist;
						pq.add(new Edge(road.eCity, coffeeDist[road.eCity], 1));
					}
				}
				if (now.type == 2) {
					if (now.dist + road.dist < bakeryDist[road.eCity]) {
						bakeryDist[road.eCity] = now.dist + road.dist;
						pq.add(new Edge(road.eCity, bakeryDist[road.eCity], 2));
					}
				}
			}
		}
		
//		int minValue = Integer.MAX_VALUE;
//		for (int i = 0; i < N; i++) {
//			if (cafeBakery[i] == 1 || cafeBakery[i] == 2) continue;
//			if (coffeeDist[i] > R || bakeryDist[i] > R) continue;
//			int temp = coffeeDist[i] + bakeryDist[i];
//			if (temp < minValue) {
//				minValue = coffeeDist[i] + bakeryDist[i];
//			}
//		}
		return (minValue == Integer.MAX_VALUE ? -1 : minValue);
	}
	
	//
	class Edge implements Comparable<Edge> {
		int dest;
		int dist;
		int type;
		public Edge(int dest, int dist, int type) {
			super();
			this.dest = dest;
			this.dist = dist;
			this.type = type;
		}
		@Override
		public int compareTo(Edge o) {
			return Integer.compare(this.dist, o.dist);
		}
	}
	
	class Road implements Comparable<Road> {
		int sCity;
		int eCity;
		int dist;
		public Road(int sCity, int eCity, int dist) {
			super();
			this.sCity = sCity;
			this.eCity = eCity;
			this.dist = dist;
		}
		@Override
		public int compareTo(Road o) {
			return Integer.compare(this.dist, o.dist);
		}
	}
}