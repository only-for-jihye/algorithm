package 도로파괴;

import java.util.*;

class UserSolution3 {
	
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
		int eCity;
		int cost;
		public Edge(int eCity, int cost) {
			super();
			this.eCity = eCity;
			this.cost = cost;
		}
		@Override
		public int compareTo(Edge o) {
			return Integer.compare(this.cost, o.cost);
		}
	}
	
	int N;
	HashMap<Integer, Node> hm;
	ArrayList<Node>[] al;
	
	// 최단 경로를 기록해야 한다.
	int[] parentNode;
	int[] parentEdge;
	
	public void init(int N, int K, int mId[], int sCity[], int eCity[], int mTime[]) {
		this.N = N;
		hm = new HashMap<>();
		al = new ArrayList[N];
		for (int i = 0; i < N; i++) {
			al[i] = new ArrayList<>();
		}
		for (int i = 0; i < K; i++) {
			add(mId[i], sCity[i], eCity[i], mTime[i]);
		}
		parentNode = new int[N];
		parentEdge = new int[N];
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
		
		int curr = eCity;
		ArrayList<Integer> paths = new ArrayList<>();
		while (curr != sCity) {
			int nodeId = parentEdge[curr];
			paths.add(nodeId);
			curr = parentNode[curr];
		}
		
		int maxValue = 0;
		for (int path : paths) {
			Node node = hm.get(path);
			node.isDeleted = true;
			
			int newCost = dijkstra(sCity, eCity, false);
			node.isDeleted = false;
			if (newCost == -1) return -1;
			int cost = newCost - baseCost;
			if (cost > maxValue) {
				maxValue = cost;
			}
		}
		
		return maxValue;
	}
	
	private int dijkstra(int sCity, int eCity, boolean isSaved) {
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		pq.add(new Edge(sCity, 0));
		
		int dist[] = new int[N];
		Arrays.fill(dist, Integer.MAX_VALUE);
		dist[sCity] = 0;
		
		if (isSaved) {
			Arrays.fill(parentNode, -1);
			Arrays.fill(parentEdge, -1);
		}
		
		while (!pq.isEmpty()) {
			Edge now = pq.poll();
			
			if (now.eCity == eCity) return now.cost;
			
			for (Node next : al[now.eCity]) {
				if (next.isDeleted) continue;
				int nextCost = now.cost + next.mTime;
				if (nextCost < dist[next.eCity]) {
					dist[next.eCity] = nextCost;
					if (isSaved) {
						parentNode[next.eCity] = now.eCity;
						parentEdge[next.eCity] = next.mId;
					}
					pq.add(new Edge(next.eCity, nextCost));
				}
			}
		}
		return -1;
	}
}


