package 도로파괴;

import java.util.*;

class UserSolution4 {
	
	class Node {
		int id;
		int sCity;
		int eCity;
		int mTime;
		boolean isDeleted;
		public Node(int id, int sCity, int eCity, int mTime) {
			super();
			this.id = id;
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
	int N;
	ArrayList<Node>[] al;
	// 경로 저장용
	int[] parentCity;
	int[] parentmId;
	
	public void init(int N, int K, int mId[], int sCity[], int eCity[], int mTime[]) {
		this.hm = new HashMap<>();
		this.N = N;
		this.al = new ArrayList[N];
		for (int i = 0; i < N; i++) {
			this.al[i] = new ArrayList<>();
		}
		for (int i = 0; i < K; i++) {
			add(mId[i], sCity[i], eCity[i], mTime[i]);
		}
		parentCity = new int[N];
		parentmId = new int[N];
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
//		System.out.println(baseCost);
		if (baseCost == -1) return -1;
			
		// 경로 구하기 -> 역순으로 이동하자
		int curr = eCity;
		ArrayList<Integer> paths = new ArrayList<>();
		while (curr != sCity) {
			int mId = parentmId[curr];
			paths.add(mId);
			curr = parentCity[curr];
		}
		
		int maxCost = 0;
		for (int path : paths) {
			Node node = hm.get(path);
			node.isDeleted = true;
			
			int newCost = dijkstra(sCity, eCity, false);
//			System.out.println(newCost);
			node.isDeleted = false;
			if (newCost == -1) return -1;
			
			int cost = newCost - baseCost;
			if (cost > maxCost) {
				maxCost = cost;
			}
		}
		
		return maxCost;
	}
	
	private int dijkstra(int sCity, int eCity, boolean pathSave) {
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		pq.add(new Edge(sCity, 0));
		int[] dist = new int[N];
		Arrays.fill(dist, Integer.MAX_VALUE);
		dist[sCity] = 0;
		
		// 경로 저장
		if (pathSave) {
			Arrays.fill(parentCity, -1);
			Arrays.fill(parentmId, -1);
		}
	
		while (!pq.isEmpty()) {
			Edge now = pq.poll();
			if (now.cost < dist[now.to]) continue;
			if (now.to == eCity) return now.cost;
			
			for (Node next : al[now.to]) {
				if (next.isDeleted) continue;
				int nextCost = now.cost + next.mTime;
				if (nextCost < dist[next.eCity]) {
					dist[next.eCity] = nextCost;
					if (pathSave) {
						parentCity[next.eCity] = now.to;
						parentmId[next.eCity] = next.id;
					}
					pq.add(new Edge(next.eCity, nextCost));
				}
			}
		}
		
		return -1;
	}
	
}


