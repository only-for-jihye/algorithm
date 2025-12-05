package 고대통신망;

import java.util.*;

class UserSolution {
	
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
		int to;
		int cost;
		int maxPath;
		public Edge(int to, int cost, int maxPath) {
			super();
			this.to = to;
			this.cost = cost;
			this.maxPath = maxPath;
		}
		@Override
		public int compareTo(Edge o) {
			return Integer.compare(this.cost, o.cost);
		}
	}
	
	HashMap<Integer, Node> hm;
	ArrayList<Node>[] al;
	int N;
	int mCapital;
	
	
	public void init(int N, int mCapital, int K, int mId[], int sCity[], int eCity[], int mDistance[]) {
		this.N = N;
		this.mCapital = mCapital;
		hm = new HashMap<>();
		al = new ArrayList[N];
		for (int i = 0; i < N; i++) {
			al[i] = new ArrayList<>();
		}
		for (int i = 0; i < K; i++) {
			add(mId[i], sCity[i], eCity[i], mDistance[i]);
		}
	}

	// 14,000
	public void add(int mId, int sCity, int eCity, int mDistance) {
		Node node = new Node(mId, sCity, eCity, mDistance);
		hm.put(mId, node);
		al[sCity].add(node);
	}

	// 1,000
	public void remove(int mId) {
		Node node = hm.get(mId);
		node.isDeleted = true;
	}

	// 5,000
	public int calculate(int mCity) {
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		pq.add(new Edge(mCapital, 0, 0));
		
		int[] dist = new int[N];
		Arrays.fill(dist, Integer.MAX_VALUE);
		dist[mCapital] = 0;
		
		while (!pq.isEmpty()) {
			Edge now = pq.poll();
			
			if (now.to == mCity) {
				return now.maxPath;
			}
			
			for (Node next : al[now.to]) {
				if (next.isDeleted) continue;
				int nextCost = now.cost + next.mDistance;
				if (nextCost < dist[next.eCity]) {
					dist[next.eCity] = nextCost;
										
					pq.add(new Edge(next.eCity, nextCost, Math.max(now.maxPath, next.mDistance)));
				}
			}
		}
		return -1;
	}
}


