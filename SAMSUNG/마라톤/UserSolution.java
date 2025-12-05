package 마라톤;

import java.util.*;

class UserSolution {
	
	class Edge implements Comparable<Edge> {
		int eCity;
		int count;
		int lenSum;
		public Edge(int eCity, int count, int lenSum) {
			super();
			this.eCity = eCity;
			this.count = count;
			this.lenSum = lenSum;
		}
		@Override
		public int compareTo(Edge o) {
			return Integer.compare(this.lenSum, o.lenSum);
		}
	}
	
	class Road {
		int id;
		int sCity;
		int eCity;
		int len;
		boolean isDeleted;
		public Road(int id, int sCity, int eCity, int len) {
			super();
			this.id = id;
			this.sCity = sCity;
			this.eCity = eCity;
			this.len = len;
			this.isDeleted = false;
		}
	}
	
	int N;
	ArrayList<Road>[] roads;
	HashMap<Integer, Road[]> map;
	  
	public void init(int N) {
		this.N = N;
		roads = new ArrayList[N + 1];
		for (int i = 0; i <= N; i++) {
			roads[i] = new ArrayList<>();
		}
		map = new HashMap<>();
	}

	public void addRoad(int K, int mID[], int mSpotA[], int mSpotB[], int mLen[]) {
		for (int i = 0; i < K; i++) {
			Road road1 = new Road(mID[i], mSpotA[i], mSpotB[i], mLen[i]);
			roads[mSpotA[i]].add(road1);
			Road road2 = new Road(mID[i], mSpotB[i], mSpotA[i], mLen[i]);
			roads[mSpotB[i]].add(road2);
			Road[] rd = new Road[2];
			rd[0] = road1;
			rd[1] = road2;
			map.put(mID[i], rd);
		}
	}

	public void removeRoad(int mID) {
		if (map.containsKey(mID)) {
			Road road1 = map.get(mID)[0];
			Road road2 = map.get(mID)[1];
			road1.isDeleted = true;
			road2.isDeleted = true;
		}
	}

	public int getLength(int mSpot) {
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		pq.add(new Edge(mSpot, 0, 0));
		int dist[] = new int[N + 1];
//		Arrays.fill(dist, Integer.MAX_VALUE);
//		dist[0] = 0;
		int answer = 0;
		while (!pq.isEmpty()) {
			Edge now = pq.poll();
			if (now.count == 8 && now.lenSum <= 42195 && now.eCity == mSpot) {
				answer = Math.max(now.lenSum, answer);
			}
			for (Road next : roads[now.eCity]) {
			}
		}
		
		return -1;
	}
}

