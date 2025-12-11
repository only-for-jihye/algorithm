package 도로파괴;

import java.util.*;

class UserSolution5 {
	
	class Node {
		int id;
		int sCity;
		int eCity;
		int time;
		boolean isDeleted;
		public Node(int id, int sCity, int eCity, int time) {
			super();
			this.id = id;
			this.sCity = sCity;
			this.eCity = eCity;
			this.time = time;
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
	
	int N;
	HashMap<Integer, Node> hm;
	ArrayList<Node>[] al;
	int[] dist;
	int[] parentCity;
	int[] roadId;
	
	public void init(int N, int K, int mId[], int sCity[], int eCity[], int mTime[]) {
		this.N = N;
		this.hm = new HashMap<>();
		al = new ArrayList[N];
		for (int i = 0; i < N; i++) {
			al[i] = new ArrayList<>();
		}
		this.dist = new int[N];
		for (int i = 0; i < K; i++) {
			add(mId[i], sCity[i], eCity[i], mTime[i]);
		}
		parentCity = new int[N];
		roadId = new int[N];
		return;
	}

	// 1500
	public void add(int mId, int sCity, int eCity, int mTime) {
		Node node = new Node(mId, sCity, eCity, mTime);
		hm.put(mId, node);
		al[sCity].add(node);
		return;
	}

	// 500
	public void remove(int mId) {
		Node node = hm.get(mId);
		node.isDeleted = true;
		return;
	}

	// 200
	public int calculate(int sCity, int eCity) {
		// 최단거리 구하면서 경로 저장
		int baseCost = dijkstra(sCity, eCity, true);
//		System.out.println(baseCost);
		
		if (baseCost == -1) return -1;
		
		// 도로 파괴 시작
		// 도로 구하기
		int curr = eCity;
		ArrayList<Integer> record = new ArrayList<>();
		while (curr != sCity) {
			int mid = roadId[curr];
			record.add(mid);
			curr = parentCity[curr];
		}
//		System.out.println(record);
		
		int value = 0;
		
		for (int removeId : record) {
			Node node = hm.get(removeId);
			node.isDeleted = true;
			
			int deleteCost = dijkstra(sCity, eCity, false);
			node.isDeleted = false;

			if (deleteCost == -1) return -1;
			
			
			if (deleteCost - baseCost > value) {
				value = deleteCost - baseCost;
			}
//			System.out.println(value);
		}
		
		return value;
	}
	
	
	
	int dijkstra(int sCity, int eCity, boolean isSaved) {
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		pq.add(new Edge(sCity, 0));
		Arrays.fill(dist, Integer.MAX_VALUE);
		dist[sCity] = 0;
		
		if (isSaved) {
			Arrays.fill(parentCity, -1);
			Arrays.fill(roadId, -1);
		}
		
		while (!pq.isEmpty()) {
			Edge now = pq.poll();
			
			if (now.to == eCity) return now.cost;
			
			for (Node next : al[now.to]) {
				if (next.isDeleted) continue;
				if (now.cost + next.time < dist[next.eCity]) {
					dist[next.eCity] = now.cost + next.time;
					if (isSaved) {
						parentCity[next.eCity] = now.to;
						roadId[next.eCity] = next.id;
					}
					pq.add(new Edge(next.eCity, dist[next.eCity]));
				}
			}
		}
		
		return -1;
	}
}