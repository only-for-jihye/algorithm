package 도로파괴;

import java.util.*;

class UserSolution {
	
	class Edge implements Comparable<Edge> {
		int id;
		int to;
		int cost;
		boolean isDeleted;
		
		public Edge(int id, int to, int cost) {
			super();
			this.id = id;
			this.to = to;
			this.cost = cost;
			this.isDeleted = false;
		}

		@Override
		public int compareTo(Edge o) {
			return Integer.compare(this.cost, o.cost);
		}
	}
	
	int N;
	ArrayList<Edge>[] al;
	HashMap<Integer, Edge> hm;
	
	// 경로 복원을 위한 배열들 (calculate 함수 내에서 사용되지만 멤버로 선언해 둠)
    int[] parentCity;   // 이 도시에 오기 직전 도시
    int[] parentEdgeId; // 이 도시에 오기 위해 사용한 도로 ID
    int[] dist;         // 최단 거리 테이블
	
	public void init(int N, int K, int mId[], int sCity[], int eCity[], int mTime[]) {
		this.N = N;
		al = new ArrayList[N];
		hm = new HashMap<>();
		for (int i = 0; i < N; i++) {
			al[i] = new ArrayList<>();
		}
		for (int i = 0; i < K; i++) {
			add(mId[i], sCity[i], eCity[i], mTime[i]);
		}
		
		// 재사용을 위해 미리 할당
        parentCity = new int[N];
        parentEdgeId = new int[N];
        dist = new int[N];
	}

	// 1,500
	public void add(int mId, int sCity, int eCity, int mTime) {
		Edge edge = new Edge(mId, eCity, mTime);
		hm.put(mId, edge);
		al[sCity].add(edge);
	}

	// 500
	public void remove(int mId) {
		Edge edge = hm.get(mId);
		edge.isDeleted = true;
	}

	// 200
	public int calculate(int sCity, int eCity) {
		int baseCost = dijkstra(sCity, eCity, true);
		
		if (baseCost == -1) return -1;
		
		ArrayList<Integer> pathEdges = new ArrayList<>();
		int curr = eCity;
		while (curr != sCity) {
			int edgeId = parentEdgeId[curr];
			pathEdges.add(edgeId);
			curr = parentCity[curr];
		}
		
		int maxDelay = 0;
		for (int edgeId : pathEdges) {
			Edge targetEdge = hm.get(edgeId);
			
			targetEdge.isDeleted = true;
			
			int newCost = dijkstra(sCity, eCity, false);
			
			targetEdge.isDeleted = false;
			
			if (newCost == -1) return -1;
			
			int delay = newCost - baseCost;
			if (delay > maxDelay) {
				maxDelay = delay;
			}
		}
		return maxDelay;
	}
	
	private int dijkstra(int sCity, int eCity, boolean savePath) {
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		pq.add(new Edge(-1, sCity, 0));
		int[] dist = new int[N];
		Arrays.fill(dist, Integer.MAX_VALUE);
		if (savePath) {
			Arrays.fill(parentCity, -1);
			Arrays.fill(parentEdgeId, -1);
		}
		dist[sCity] = 0;
		
		// 최단거리 일단 구함
		while (!pq.isEmpty()) {
			Edge now = pq.poll();
			
			if (now.to == eCity) return now.cost;
			
			for (Edge next : al[now.to]) {
				if (next.isDeleted) continue;
				
				if (now.cost + next.cost < dist[next.to]) {
					dist[next.to] = now.cost + next.cost;
					// path 기록
					if (savePath) {
						parentCity[next.to] = now.to;
						parentEdgeId[next.to] = next.id;
					}
					pq.add(new Edge(next.id, next.to, dist[next.to]));
				}
			}
		}
		return -1;
	}
}


