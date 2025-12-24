package 통행료인상; 

import java.util.*;

class UserSolution2 {
	
	class Node {
		int from;
		int to;
		int cost;
		public Node(int from, int to, int cost) {
			super();
			this.from = from;
			this.to = to;
			this.cost = cost;
		}
		void setCost(int cost) {
			this.cost = cost;
		}
	}
	
	class Edge implements Comparable<Edge> {
		int to;
		int cost;
		int count;
		public Edge(int to, int cost, int count) {
			super();
			this.to = to;
			this.cost = cost;
			this.count = count;
		}
		@Override
		public int compareTo(Edge o) {
			if (this.cost != o.cost) return Integer.compare(this.cost, o.cost);
			return Integer.compare(this.count, o.count);
		}
	}
	
	int N;
	ArrayList<Node>[] al; 
	// 최단 경로의 cost와 count 수를 저장하고 정렬하기 위한 pq
	PriorityQueue<Edge> path;
	// 각 간선마다 노선의 수를 저장하기 위한 배열
	int[] edgeCount;
	
	public void init(int N, int K, int sCity[], int eCity[], int mToll[]) {
		this.N = N;
		al = new ArrayList[N];
		for (int i = 0; i < N; i++) {
			al[i] = new ArrayList<>();
		}
		for (int i = 0; i < K; i++) {
			int from = sCity[i];
			int to = eCity[i];
			int cost = mToll[i];
			add(from, to, cost);
		}
		path = new PriorityQueue<>();
		edgeCount = new int[N];
		return;
	}

	// 500
	public void add(int sCity, int eCity, int mToll) {
		// 양방향
		al[sCity].add(new Node(sCity, eCity, mToll));
		al[eCity].add(new Node(eCity, sCity, mToll));
		return;
	}

	// 호출 2 X M 20,000 X N 200 X 도로수 800 (500+300)
	public void calculate(int sCity, int eCity, int M, int mIncrease[], int mRet[]) {
		// 인상 할 때, 모든 도로의 cost가 인상됨
		path = new PriorityQueue<>();
		dijkstra(sCity, eCity);
		mRet[0] = path.peek().cost;
		
		// path 재정렬
		int update = 0;
		for (int i = 0; i < M; i++) {
			update += mIncrease[i];
			
			while (path.peek().to < update) {
				Edge now = path.poll();
				now.cost += (update - now.to) * now.count;
				now.to = update; // 재정렬을 위한 트릭 변수
				path.add(now);
			}
			
			mRet[i + 1] = path.peek().cost;
		}

		// 최종 증가 값을 더해서 node의 cost 세팅
		for (int i = 0; i < N; i++) {
			for (Node next : al[i]) {
				next.setCost(next.cost + update);
			}
		}
		
		return;
	}
	
	void dijkstra(int sCity, int eCity) {
		Arrays.fill(edgeCount, Integer.MAX_VALUE);
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		pq.add(new Edge(sCity, 0, 0));
		
		while (!pq.isEmpty()) {
			Edge now = pq.poll();
			
			if (now.count >= edgeCount[now.to]) continue;
			edgeCount[now.to] = now.count;
			
			if (now.to == eCity) {
				// cost와 count만 저장하고 to는 의미 없음
				path.add(new Edge(0, now.cost, now.count));
				continue;
			}
			
			for (Node next : al[now.to]) {
				if (edgeCount[next.to] <= now.count + 1) continue;
				pq.add(new Edge(next.to, now.cost + next.cost, now.count + 1));
			}
		}
	}
	
}