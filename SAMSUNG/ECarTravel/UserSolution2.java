package ECarTravel;

import java.util.*;

class UserSolution2 {
	
	class Edge implements Comparable<Edge> {
		int eCity;
		int cost;
		int power;
		public Edge(int eCity, int cost, int power) {
			super();
			this.eCity = eCity;
			this.cost = cost;
			this.power = power;
		}
		@Override
		public int compareTo(Edge o) {
			if (this.cost != o.cost) return Integer.compare(this.cost, o.cost);
			if (this.power != o.power) return Integer.compare(o.power, this.power);
			return 0;
		}
	}

	class Road {
		int id;
		int sCity;
		int eCity;
		int time;
		int power;
		boolean isDeleted;
		public Road(int id, int sCity, int eCity, int time, int power) {
			super();
			this.id = id;
			this.sCity = sCity;
			this.eCity = eCity;
			this.time = time;
			this.power = power;
			this.isDeleted = false;
		}
	}
	
	int N;
	ArrayList<Road>[] roads;
	HashMap<Integer, Road> map;
	int[] deadline;
	int[][] dist;
	
	public void init(int N, int mCharge[], int K, int mId[], int sCity[], int eCity[], int mTime[], int mPower[]) {
		this.N = N;
		roads = new ArrayList[N];
		for (int i = 0; i < K; i++) {
			Road road = new Road(mId[i], sCity[i], eCity[i], mTime[i], mPower[i]);
			roads[sCity[i]].add(road);
			map.put(mId[i], road);
		}
		deadline = new int[N];
		dist = new int[N][301];
	}

	public void add(int mId, int sCity, int eCity, int mTime, int mPower) {
		Road road = new Road(mId, sCity, eCity, mTime, mPower);
		roads[sCity].add(road);
		map.put(mId, road);
	}

	public void remove(int mId) {
		Road road = map.get(mId);
		road.isDeleted = true;
	}

	public int cost(int B, int sCity, int eCity, int M, int mCity[], int mTime[]) {
		PriorityQueue<Edge> deadpq = new PriorityQueue<>();
		Arrays.fill(deadline, Integer.MAX_VALUE);
		for (int i = 0; i < M; i++) {
			deadpq.add(new Edge(mCity[i], mTime[i], -1));
			deadline[mCity[i]] = mTime[i];
		}
		while (!deadpq.isEmpty()) {
			Edge now = deadpq.poll();
			if (deadline[now.eCity] < now.cost) continue;
			deadline[now.eCity] = now.cost;
			for (Road next : roads[now.eCity]) {
				if (next.isDeleted) continue;
				int nc = now.cost + next.time;
				if (deadline[next.eCity] <= nc) continue;
				deadline[next.eCity] = nc;
				deadpq.add(new Edge(next.eCity, nc, -1));
			}
		}
		
		PriorityQueue<Edge> epq = new PriorityQueue<>();
		epq.add(new Edge(sCity, 0, B));
		for (int i = 0; i < N; i++) {
			Arrays.fill(dist[i], Integer.MAX_VALUE);
		}
		dist[sCity][B] = 0;
		
		while (!epq.isEmpty()) {
			Edge now = epq.poll();
			if (now.eCity == eCity) return now.cost;
			for (Road road : roads[now.eCity]) {
				if (road.isDeleted) continue;
				int maxChargeTime = deadline[now.eCity] - now.cost;
//				int requiredChargeTime = 
			}
		}
		
		return 0;
	}
}


