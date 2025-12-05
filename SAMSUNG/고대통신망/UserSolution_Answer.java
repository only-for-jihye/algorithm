package 고대통신망;

//UserSolution.java
import java.util.*;

//2025.10.24 고대통신망
//@admin_deukwha
//lazy dijkstra (dynamic dijkstra) 

class UserSolution_Answer {
	
	class Edge implements Comparable <Edge> {
		int id; 
		int to;
		int cost;
		int maxDist;
		int from; 
		Edge(int id, int to, int cost) {
			this.id = id;
			this.to = to;
			this.cost = cost; 
		}
		Edge(int id, int to, int cost, int maxDist) {
			this.id = id;
			this.to = to;
			this.cost = cost;
			this.maxDist = maxDist; 
		}
		public int compareTo(Edge o) {
			if(cost == o.cost) return maxDist - o.maxDist; 
			return cost - o.cost;
		}
	}
	
	ArrayList<Edge>[] al = new ArrayList[5001];
	HashMap<Integer, Integer>hm = new HashMap<>();
	int capital; 
	int[] dist = new int[5001]; 
	int[] maxDist = new int[5001];
	int n; 
	PriorityQueue<Edge>pq = new PriorityQueue<>();
	int[] rid = new int[24001]; 
	int idCnt;
	
	int register(int id) {
		if(hm.get(id) == null) hm.put(id, idCnt++);
		return hm.get(id);
	}
	
	void update() {
		while(!pq.isEmpty()) {
			Edge now = pq.poll();
			if(dist[now.to] < now.cost) continue;
			if(maxDist[now.to] < now.maxDist) continue;
			for(Edge next : al[now.to]) {
				if(rid[next.id] == -1) continue;                           // 삭제된 도로라면 skip
				int nc = now.cost + next.cost;
				int md = Math.max(next.cost, now.maxDist);                 // 다음 노드까지의 최장 도로길이 확인
				if(dist[next.to] < nc) continue;                           // 최단거리가 아니라면 -> 필요 없음 
				if(dist[next.to] == nc && md > maxDist[next.to]) continue; // 최단거리와 동일시 -> 최장거리 비교
				dist[next.to] = nc;                                        // 최단거리 갱신 
				maxDist[next.to] = md;                                     // 최장도로 길이 갱신 
				pq.add(new Edge(-1, next.to, nc, md));
			}
		}
	}
	
	void dijkstra(int st) {
		for(int i = 0; i < n; i++) {
			dist[i] = Integer.MAX_VALUE;
			maxDist[i]  = Integer.MAX_VALUE;
		}
		dist[st] = 0;
		maxDist[st] = 0;
		pq.clear();
		pq.add(new Edge(-1, st, 0, 0)); 
		update();
	}
	
	public void init(int N, int mCapital, int K, int mId[], int sCity[], int eCity[], int mDistance[]) {
		for(int i = 0; i < N; i++) al[i] = new ArrayList<>();
		capital = mCapital;
		n = N; 
		hm.clear();
		pq.clear(); 
		Arrays.fill(rid, -1);
		idCnt = 0; 
		for(int i = 0; i < K; i++) {
			int id = register(mId[i]);
			al[sCity[i]].add(new Edge(id, eCity[i], mDistance[i]));
			rid[id] = sCity[i];
		}
		// 초기 최단 루트 구해두기
		dijkstra(capital); 
		return;
	}

	// 14,000 = 최대 24,000개의 도로
	public void add(int mId, int sCity, int eCity, int mDistance) {
		// 단방향 연결
		int id = register(mId); 
		al[sCity].add(new Edge(id, eCity, mDistance));
		rid[id] = sCity;
		// 업데이트
		if(dist[sCity] == Integer.MAX_VALUE) return; //
		// 만약 현재 sCity까지의 루트에서 eCity까지의 새로운 더 짧은 루트가 생겨났다면 -> 갱신
		if(dist[sCity] + mDistance < dist[eCity] || (dist[sCity] + mDistance == dist[eCity] && maxDist[eCity] > Math.max(mDistance, maxDist[sCity]))) { 
			dist[eCity] = dist[sCity] + mDistance; 
			maxDist[eCity] = Math.max(maxDist[sCity], mDistance);
			pq.add(new Edge(-1, eCity, dist[eCity], maxDist[eCity])); // 여기서부터 다시 update할수 있도록 준비
		}
		return;
	}

	// 1,000 
	public void remove(int mId) {
		int id = register(mId); 
		int cid = rid[id]; 
		rid[id] = -1;
		for(Edge next : al[cid]) {
			if(next.id == id) {                             // mId 도로에 대해
				if(dist[cid] + next.cost== dist[next.to]) { // 만약 이 도로를 타고 from -> to로 가는길이 최단거리에 사용되는 길이라면
					dijkstra(capital);                      // 여기서 다시 dijkstra()로 모든 최단거리 갱신 (호출수가 가장 적으니 여기서 이득보기)
					return;
				}
			}
		}
		return;
	}

	// 5,000 
	// dijkstra : 24,000log(5,000)
	// --> 매번 dijkstra를 돌리면 시간초과 
	public int calculate(int mCity) {
		update();
		if(maxDist[mCity] == Integer.MAX_VALUE) return -1;
		return maxDist[mCity];
	}
}