package 커피점베이커리;

import java.util.*;

class UserSolution3 {
	
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
	}
	
	class Edge implements Comparable<Edge> {
		int to;
		int cost;
		int type;
		public Edge(int to, int cost, int type) {
			super();
			this.to = to;
			this.cost = cost;
			this.type = type;
		}
		@Override
		public int compareTo(Edge o) {
			return Integer.compare(this.cost, o.cost);
		}
	}
	
	int N;
	ArrayList<Node>[] al;
	int[] coffeeOrBakery;
	int[] coffeeDist;
	int[] bakeryDist;
	
	public void init(int N, int K, int sBuilding[], int eBuilding[], int mDistance[]) {
		this.N = N;
		this.al = new ArrayList[N];
		for (int i = 0; i < N; i++) {
			al[i] = new ArrayList<>();
		}
		for (int i = 0; i < K; i++) {
			add(sBuilding[i], eBuilding[i], mDistance[i]);
		}
		
		coffeeDist = new int[N];
		bakeryDist = new int[N];
		return;
	}

	public void add(int sBuilding, int eBuilding, int mDistance) {
		Node node = new Node(sBuilding, eBuilding, mDistance);
		al[sBuilding].add(node);
		Node node2 = new Node(eBuilding, sBuilding, mDistance);
		al[eBuilding].add(node2);
		return;
	}

	public int calculate(int M, int mCoffee[], int P, int mBakery[], int R) {
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		Arrays.fill(coffeeDist, Integer.MAX_VALUE);
		Arrays.fill(bakeryDist, Integer.MAX_VALUE);
		coffeeOrBakery = new int[N];
		// 커피 넣고
		for (int i = 0; i < M; i++) {
			coffeeOrBakery[mCoffee[i]] = 1;
			pq.add(new Edge(mCoffee[i], 0, 1));
			coffeeDist[mCoffee[i]] = 0;
		}
		
		// 베이커리 넣고
		for (int i = 0; i < P; i++) {
			coffeeOrBakery[mBakery[i]] = 2;
			pq.add(new Edge(mBakery[i], 0, 2));
			bakeryDist[mBakery[i]] = 0;
		}
		
		int minValue = Integer.MAX_VALUE;
		
		while (!pq.isEmpty()) {
			Edge now = pq.poll();
			if (now.cost == Integer.MAX_VALUE) continue;
			if (now.cost > R) continue;
			
			if (coffeeOrBakery[now.to] == 0) { // 빌딩 이면서 ...
				if (coffeeDist[now.to] <= R && bakeryDist[now.to] <= R) {
					int answer = coffeeDist[now.to] + bakeryDist[now.to];
					minValue = Math.min(answer, minValue);
				}
			}
			
			if (minValue != Integer.MAX_VALUE && !pq.isEmpty() && pq.peek().cost >= minValue) continue;

			for (Node next : al[now.to]) {
				if (now.type == 1) { // 커피
					if (now.cost + next.cost < coffeeDist[next.to]) {
						coffeeDist[next.to] = now.cost + next.cost;
						pq.add(new Edge(next.to, coffeeDist[next.to], 1));
					}
				}
				if (now.type == 2) { // 베이커리
					if (now.cost + next.cost < bakeryDist[next.to]) {
						bakeryDist[next.to] = now.cost + next.cost;
						pq.add(new Edge(next.to, bakeryDist[next.to], 2));
					}
				}
			}
		}
		
		
		return minValue == Integer.MAX_VALUE ? -1 : minValue;
	}
	
}