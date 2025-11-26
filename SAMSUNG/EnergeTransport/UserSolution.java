package EnergeTransport;

import java.util.*;

class UserSolution {
	
	// 양방향 파이프라인
	// 저장소는 1~N
	
	// 각 파이프라인은 M개의 속성을 가짐
	// 속성의 종류는 P, W, D 3가지로 이루어짐
	// 그리고 D는 딱 하나 무조건 들어가야함
	
	class Pipe {
		int id;
		int from;
		int to;
		ArrayDeque<Integer> attr;
		boolean isDeleted;
		public Pipe(int id, int from, int to) {
			super();
			this.id = id;
			this.from = from;
			this.to = to;
			this.attr = new ArrayDeque<>();
			this.isDeleted = false;
		}
	}
	
	class Edge implements Comparable<Edge> {
		int to;
		int cost;
		int attr;
		public Edge(int to, int cost, int attr) {
			super();
			this.to = to;
			this.cost = cost;
			this.attr = attr;
		}
		@Override
		public int compareTo(Edge o) {
			return Integer.compare(this.cost, o.cost);
		}
	}
	
	int N;
	int M;
	HashMap<Integer, Pipe> hm;
	ArrayList<Pipe>[] pipeline;

	void init(int N, int M, int K, int mID[], int aStorage[], int bStorage[], char mAttr[][]) {
		this.N = N;
		this.M = M;
		this.hm = new HashMap<>();
		this.pipeline = new ArrayList[N + 1];
		for (int i = 1; i <= N; i++) {
			this.pipeline[i] = new ArrayList<>();
		}
		for (int i = 1; i <= N; i++) {
			Pipe pl = new Pipe(mID[i], aStorage[i], bStorage[i]);
			for (int j = 0; j < M; j++) {
				pl.attr.add(mAttr[i][j] - 96);
			}
			this.hm.put(mID[i], pl);
			this.pipeline[aStorage[i]].add(pl);
			this.pipeline[bStorage[i]].add(pl);
		}
	}

	void add(int mID, int aStorage, int bStorage, char mAttr[]) {
		Pipe pl = new Pipe(mID, aStorage, bStorage);
		for (int i = 0; i < M; i++) {
			pl.attr.add(mAttr[i] - 96);
		}
		this.hm.put(mID, pl);
		this.pipeline[aStorage].add(pl);
		this.pipeline[bStorage].add(pl);
	}

	void remove(int mID) {
		Pipe removed = hm.get(mID);
		removed.isDeleted = true;
	}

	int transport(int sStorage, int eStorage, char mAttr[]) {
		
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		int[][] dist = new int[N + 1][M + 1];
		for (int i = 1; i <= N; i++) {
			for (int j = 1; j <= N; j++) {
				dist[i][j] = Integer.MAX_VALUE;
			}
		}
		for (int i = 1; i <= M; i++) {
			dist[sStorage][i] = 0;
		}
		pq.add(new Edge(sStorage, 0, -1));
		while (!pq.isEmpty()) {
			Edge now = pq.poll();
			
			if (now.to == eStorage) return now.cost;
			
			for (Pipe next : pipeline[now.to]) {
				// 속성 값이 같을 때
				if (now.attr == next.attr.peek()) {
					
				}
				// 속성 값이 다를 때
					// 속성값 변경 O
					
					// 속성값 변경 X
			}
		}
		
		return -1;
	}
}


