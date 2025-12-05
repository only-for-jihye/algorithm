//UserSolution.java
package 커피점베이커리;

import java.util.*; 

class UserSolution_Teaching {
	
	// 2025.09.06 커피점 & 제과점
	// @admin_deukwha	
	// dijkstra
	
	class Edge implements Comparable <Edge>{
		int to;
		int cost;
		int type; 
		Edge(int to, int cost){
			this.to = to;
			this.cost = cost; 
		}
		Edge(int to, int cost, int type) {
			this.to = to;
			this.cost = cost;
			this.type = type;
		}

		@Override
		public int compareTo(Edge o) {
			return cost - o.cost;
		}
	}
	
	ArrayList<Edge>[] al; 
	int n;
	int[] coffeeDist;
	int[] bakeryDist; 
	int ret;
	
	public void init(int N, int K, int sBuilding[], int eBuilding[], int mDistance[]) {
		n = N;
		al = new ArrayList[N];
		for(int i = 0; i < N; i++) al[i] = new ArrayList<>();
		for(int i = 0; i < K; i++) add(sBuilding[i], eBuilding[i], mDistance[i]);
		coffeeDist = new int[n];
		bakeryDist = new int[n];
		return;
	}

	// 2,000 x o(1)
	public void add(int sBuilding, int eBuilding, int mDistance) {
		al[sBuilding].add(new Edge(eBuilding, mDistance)); 
		al[eBuilding].add(new Edge(sBuilding, mDistance)); 
		return;
	}

	// 100 x 
	// dijkstra 1번 : 32,000 log ( 10,000) 
	public int calculate(int M, int mCoffee[], int P, int mBakery[], int R) {
		return dijkstra(M, mCoffee, P, mBakery, R); 
	}

	int dijkstra(int m, int[] coffee, int p, int[] bakery, int r) {
		Arrays.fill(coffeeDist, Integer.MAX_VALUE);
		Arrays.fill(bakeryDist, Integer.MAX_VALUE); 
		
		PriorityQueue<Edge>pq = new PriorityQueue<>();
		
		for(int i = 0; i < m; i++) {
			pq.add(new Edge(coffee[i], 0, 0));
			coffeeDist[coffee[i]] = 0; 
		}
		for(int i = 0; i < p; i++) {
			pq.add(new Edge(bakery[i], 0, 1));
			bakeryDist[bakery[i]] = 0;
		}
		
		int ret = Integer.MAX_VALUE;
		
		while(!pq.isEmpty()) {
			Edge now = pq.remove(); 
			
			// ** 극한의 가지치기.. 
			if(now.cost >= ret) break;  // 답을 찾아냈으면 종료 --> 이후의 답은 무조건 > ret 
			
			// coffee
			if(now.type == 0) {
				if(coffeeDist[now.to] < now.cost) continue; 
				// now.cost > 0   : 시작한곳이 cafe가 아니고
				// now.cost <= r : r 이하의 거리로 cafe -> now.to 집까지 갈수 있었고
				// bakeryDist[now.to] > 0 : 여긴 bakery가 아니고
				// bakeryDist[now.to] <= r : r 이하의 거리로 bakery -> now.to 집까지 갈 수 있었다.
				if(now.cost > 0 && now.cost <= r && bakeryDist[now.to] > 0 && bakeryDist[now.to] <= r)
					ret = Math.min(ret,  now.cost + bakeryDist[now.to]); // 정답 갱신 
				for(Edge next : al[now.to]) {
					int nc = now.cost + next.cost;
					if(nc >= ret) continue;                     // ret보다 더 비싼 비용이 되면 -> 무조건 over 
					if(coffeeDist[next.to] <= nc) continue;     // 더 멀리 오는 경우 볼 필요 X 
					if(nc > r) continue;                        // r 넘어갈수 없음
					coffeeDist[next.to] = nc;
					pq.add(new Edge(next.to, nc, now.type)); 
				}
			}
			// bakery
			else {
				if(bakeryDist[now.to] < now.cost) continue; 
				if(now.cost > 0 && now.cost <= r && coffeeDist[now.to] > 0 && coffeeDist[now.to] <= r)
					ret = Math.min(ret,  now.cost + coffeeDist[now.to]);
				for(Edge next : al[now.to]) {
					int nc = now.cost + next.cost;
					if(nc >= ret) continue;
					if(nc > r) continue; 
					if(bakeryDist[next.to] <= nc) continue;
					bakeryDist[next.to] = nc;
					pq.add(new Edge(next.to, nc, now.type)); 
				}
			}
		}
		
		if(ret == Integer.MAX_VALUE) return -1;
		return ret;
	}
}