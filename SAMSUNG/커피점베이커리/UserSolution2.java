package 커피점베이커리;

import java.util.*;

class UserSolution2 {
	
	class Edge implements Comparable<Edge> {
		int eCity;
		int distance;
		int type;
		public Edge(int eCity, int distance, int type) {
			super();
			this.eCity = eCity;
			this.distance = distance;
			this.type = type; // 1: coffee, 2: bakery
		}
		@Override
		public int compareTo(Edge o) {
			return Integer.compare(this.distance, o.distance);
		}
	}
	
	// 건물은 0 ~ N-1 까지의 ID 값을 가짐
	int N;
	// 양방향 도로
	List<Edge>[] edges;
	// 커피점
	int[] isCoffee;
	// 베이커리
	int[] isBakery;
	int[] cafeOrbakery;

	public void init(int N, int K, int sBuilding[], int eBuilding[], int mDistance[]) {
		edges = new ArrayList[N];
		isCoffee = new int[N];
		isBakery = new int[N];
		this.N = N;
		for (int i = 0; i < N; i++) {
			edges[i] = new ArrayList<>();
		}
		for (int i = 0; i < K; i++) {
			int sCity = sBuilding[i];
			int eCity = eBuilding[i];
			int distance = mDistance[i];
			edges[sCity].add(new Edge(eCity, distance, 0));
			edges[eCity].add(new Edge(sCity, distance, 0));
		}
	}

	// 2000 이하
	public void add(int sBuilding, int eBuilding, int mDistance) {
		edges[sBuilding].add(new Edge(eBuilding, mDistance, 0));
		edges[eBuilding].add(new Edge(sBuilding, mDistance, 0));
	}

	// 100 이하
	public int calculate(int M, int mCoffee[], int P, int mBakery[], int R) {
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		Arrays.fill(isCoffee, Integer.MAX_VALUE);
		Arrays.fill(isBakery, Integer.MAX_VALUE);
		cafeOrbakery = new int[N];
		for (int i = 0; i < M; i++) { // coffee
			pq.add(new Edge(mCoffee[i], 0, 1));
			isCoffee[mCoffee[i]] = 0;
			cafeOrbakery[mCoffee[i]] = 1;
		}
		
		for (int i = 0; i < P; i++) { // bakery
			pq.add(new Edge(mBakery[i], 0, 2));
			isBakery[mBakery[i]] = 0;
			cafeOrbakery[mBakery[i]] = 2;
		}
		
		int minValue = Integer.MAX_VALUE;
		
		while (!pq.isEmpty()) {
			Edge now = pq.poll();
			if (now.distance >= minValue) continue;
			if (now.distance > R) continue;
			
			if (cafeOrbakery[now.eCity] == 0) { // 커피점 제과점 모두 아닌 일반 빌딩
				if (isCoffee[now.eCity] <= R && isBakery[now.eCity] <= R) {
					minValue = Math.min(minValue, isCoffee[now.eCity] + isBakery[now.eCity]);
				}
			}
			
			if (minValue != Integer.MAX_VALUE && !pq.isEmpty() && pq.peek().distance >= minValue) break;
			
			for (Edge next : edges[now.eCity]) {
				int nextCost = now.distance+ next.distance;
				if (now.type == 1) { // coffee
					if (nextCost < isCoffee[next.eCity]) {
						isCoffee[next.eCity] = nextCost;
						pq.add(new Edge(next.eCity, isCoffee[next.eCity], 1));
					}
				}
				if (now.type == 2) { // bakery
					if (nextCost < isBakery[next.eCity]) {
						isBakery[next.eCity] = nextCost;
						pq.add(new Edge(next.eCity, isBakery[next.eCity], 2));
					}
				}
			}
			
		}
		return (minValue != Integer.MAX_VALUE) ? minValue : -1;
	}
}