package 도로파괴;

import java.util.*;

class UserSolution2 {
	
	class Node {
		int mId;
		int sCity;
		int eCity;
		int mTime;
		boolean isDeleted;
		public Node(int mId, int sCity, int eCity, int mTime) {
			super();
			this.mId = mId;
			this.sCity = sCity;
			this.eCity = eCity;
			this.mTime = mTime;
			this.isDeleted = false;
		}
	}
	
	class Edge implements Comparable<Edge> {
		int to;
		int cost;
		public Edge(int to, int cost) {
			super();
			this.to = to;
			this.cost = cost;
		}
		@Override
		public int compareTo(Edge o) {
			return Integer.compare(this.cost, o.cost);
		}
	}
	
	HashMap<Integer, Node> hm;
	ArrayList<Node>[] al;
	int[] dist;
	int[] parentCity;
	int[] parentEdgeId;
	
	public void init(int N, int K, int mId[], int sCity[], int eCity[], int mTime[]) {
		hm = new HashMap<>();
		al = new ArrayList[N];
		for (int i = 0; i < N; i++) {
			al[i] = new ArrayList<>();
		}
		dist = new int[N];
		for (int i = 0; i < K; i++) {
			add(mId[i], sCity[i], eCity[i], mTime[i]);
		}
		parentCity = new int[N];
		parentEdgeId = new int[N];
	}

	public void add(int mId, int sCity, int eCity, int mTime) {
		Node node = new Node(mId, sCity, eCity, mTime);
		hm.put(mId, node);
		al[sCity].add(node);
	}

	public void remove(int mId) {
		Node node = hm.get(mId);
		node.isDeleted = true;
	}

	public int calculate(int sCity, int eCity) {
		int baseCost = dijkstra(sCity, eCity, true);
		if (baseCost == -1) return -1;
		
		ArrayList<Integer> paths = new ArrayList<>();
		int curr = eCity;
		while (curr != sCity) {
			int edgeId = parentEdgeId[curr];
			paths.add(edgeId);
			curr = parentCity[curr];
		}
		
		int maxDelay = 0;
		for (int path : paths) {
			Node node = hm.get(path);
			node.isDeleted = true;
			int newCost = dijkstra(sCity, eCity, false);
			node.isDeleted = false;
			
			if (newCost == -1) return -1;
			
			int delay = newCost - baseCost;
			
			if (delay > maxDelay) {
				maxDelay = delay;
			}
		}
		
		return maxDelay;
	}
	
	private int dijkstra(int sCity, int eCity, boolean isSaved) {
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		pq.add(new Edge(sCity, 0));
		
		Arrays.fill(dist, Integer.MAX_VALUE);
		dist[sCity] = 0;
		
		if (isSaved) {
			Arrays.fill(parentCity, -1);
			Arrays.fill(parentEdgeId, -1);
		}
		
		while (!pq.isEmpty()) {
			Edge now = pq.poll();
			
			if (now.to == eCity) return now.cost;
			
			for (Node next : al[now.to]) {
				if (next.isDeleted) continue;
				if (now.cost + next.mTime < dist[next.eCity]) {
					dist[next.eCity] = now.cost + next.mTime;
					if (isSaved) {
						parentCity[next.eCity] = now.to;
						parentEdgeId[next.eCity] = next.mId;
					}
					pq.add(new Edge(next.eCity, dist[next.eCity]));
				}
			}
		}
		
		return -1;
	}
}


