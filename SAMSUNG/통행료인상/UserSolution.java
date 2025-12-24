package 통행료인상; 

import java.util.*;

//통행료인상
//@admin_deukwha
//dijkstra

class UserSolution {
	
	class Edge implements Comparable <Edge> {
		int to;
		int cost; 
		int cnt; 
		Edge(int to, int cost, int cnt){
			this.to = to;
			this.cost = cost;
			this.cnt = cnt;
		}
		@Override
		public int compareTo(Edge o) {
			if(cost < o.cost) return -1;   // 가장 비용 싼것 우선
			if(cost > o.cost) return 1;
			if(cnt < o.cnt) return -1;     // 비용이 동일하다면 가장 적은 간선의 수로 이동한 것 우선
			if(cnt > o.cnt) return 1; 
			return 0;
		}
		public void setCost(int cost) {
			this.cost = cost;
		}
	}
	
	ArrayList<Edge>[] al;
	int n;
	PriorityQueue<Edge>path; 
	int[] edgeCnt; //index : node#, value: 이 노드까지 거쳐온 간선의 수

	void addEdge(int from, int to, int cost) {
		al[from].add(new Edge(to, cost, 0));
		al[to].add(new Edge(from, cost, 0)); 
	}
	
	public void init(int N, int K, int sCity[], int eCity[], int mToll[]) {
		n = N;
		al = new ArrayList[N];
		edgeCnt = new int[n];
		for(int i = 0; i < N; i++) al[i] = new ArrayList<>();
		for(int i = 0; i < K; i++) 
			addEdge(sCity[i], eCity[i], mToll[i]); 
		path = new PriorityQueue<>();
		return;
	}

	public void add(int sCity, int eCity, int mToll) {
		addEdge(sCity, eCity, mToll); 
		return;
	}

	// 2 x 20,000 x 800(log800) = 3.2억
	public void calculate(int sCity, int eCity, int M, int mIncrease[], int mRet[]) {
		path = new PriorityQueue<>();
		dijkstra(sCity, eCity, 0);
		mRet[0] = path.peek().cost;  // 처음 경우는 무조건 root
		int update = 0; 
		for(int i = 0; i < M; i++) {
			update += mIncrease[i];  // mIncrease만큼 인상
			while(path.peek().to < update) { // 필요없는 now.to를 현재까지 업데이트 된 비용으로 사용
				Edge now = path.remove();
				now.cost += (update - now.to) * now.cnt; // 새로운 cost 준비
				now.to = update;                         // 현재까지 업데이트 된 비용 처리
				path.add(now);
			}
			mRet[i+1] = path.peek().cost;
		}
		for(int i = 0; i < n; i++) 
			for(Edge next : al[i]) next.setCost(next.cost + update); // 모든 간선 비용 최종 업데이트
		return;
	}
	
	void dijkstra(int st, int en, int inc) {
		Arrays.fill(edgeCnt, Integer.MAX_VALUE);       
		PriorityQueue<Edge>pq = new PriorityQueue<>();
		pq.add(new Edge(st, 0, 0));
		
		while(!pq.isEmpty()) {
			Edge now = pq.remove();                        // comparator 정의에 의해 now는 가장 비용이 적으면서, 이동한 간선이 가장 적은 것 우선
			if(now.cnt >= edgeCnt[now.to]) continue;       // 비용은 이미 최저 확보, 그러니 더 많은 간선으로 왔었다면 의미 없음
			edgeCnt[now.to] = now.cnt;                     // ** 최소 비용과 간선수가 확정되는 시점
			if(now.to == en) {
				path.add(new Edge(0, now.cost, now.cnt));  // 목적지라면, 여기까지 오기 위해 필요했던 간선 수와 최소 비용 경우의 수 기록
				continue;
			}
			for(Edge next : al[now.to]) {
				if(edgeCnt[next.to] <= now.cnt + 1) continue; 
				// 같은 간선의 개수로 왔을때 edgeCnt가 갱신되는 것을 방지하기 위해 edgeCnt[] 기록하지 않음 
				pq.add(new Edge(next.to, now.cost + next.cost, now.cnt + 1));
			}
		}
	}
}