//package TollgateIncrease;
//
//import java.util.*;
//
//class UserSolution {
//	
//	class Edge implements Comparable<Edge> {
//		int eCity;
//		int cost;
//		public Edge(int eCity, int cost) {
//			super();
//			this.eCity = eCity;
//			this.cost = cost;
//		}
//		@Override
//		public int compareTo(Edge o) {
//			return Integer.compare(this.cost, o.cost);
//		}
//	}
//	
//	class Road implements Comparable<Road> {
//		int sCity;
//		int eCity;
//		int cost;
//		public Road(int sCity, int eCity, int cost) {
//			super();
//			this.sCity = sCity;
//			this.eCity = eCity;
//			this.cost = cost;
//		}
//		void setNewCost(int newCost) {
//			this.cost += newCost;
//		}
//		@Override
//		public int compareTo(Road o) {
//			return Integer.compare(this.cost, o.cost);
//		}
//	}
//	
//	ArrayList<Road>[] roads;
//	int N;
//	
//	public void init(int N, int K, int sCity[], int eCity[], int mToll[]) {
//		this.N = N;
//		roads = new ArrayList[N];
//		for (int i = 0; i < N; i++) {
//			roads[i] = new ArrayList<>();
//		}
//		for (int i = 0; i < K; i++) {
//			roads[sCity[i]].add(new Road(sCity[i], eCity[i], mToll[i]));
//			roads[eCity[i]].add(new Road(eCity[i], sCity[i], mToll[i]));
//		}
//	}
//
//	public void add(int sCity, int eCity, int mToll) {
//		roads[sCity].add(new Road(sCity, eCity, mToll));
//		roads[eCity].add(new Road(eCity, sCity, mToll));
//	}
//
//	public void calculate(int sCity, int eCity, int M, int mIncrease[], int mRet[]) {
////		mRet = new int[M + 1];
//		for (int i = 0; i < M + 1; i++) {
//			if (i == 0) {
//				mRet[0] = processPQ(sCity, eCity);
//			} else {
//				for (int j = 0; j < N; j++) {
//					for (int k = 0; k < roads[j].size(); k++) {
//						roads[j].get(k).setNewCost(mIncrease[i - 1]);
//					}
//					mRet[i] = processPQ(sCity, eCity);
//				}
//			}
////			System.out.println(i + " : " + mRet[i]);
//		}
//	}
//	
//	int processPQ(int sCity, int eCity) {
//		PriorityQueue<Edge> pq = new PriorityQueue<>();
//		pq.add(new Edge(sCity, 0));
//		int[] dist = new int[N];
//		for (int i = 0; i < N; i++) {
//			dist[i] = Integer.MAX_VALUE;
//		}
//		dist[0] = 0;
//		while (!pq.isEmpty()) {
//			Edge now = pq.poll();
//			
//			if (now.eCity == eCity) {
////				System.out.println(now.cost);
//				return now.cost;
//			}
//			for (Road road : roads[now.eCity]) {
//				if (now.cost + road.cost < dist[road.eCity]) {
//					dist[road.eCity] = now.cost + road.cost;
//					pq.add(new Edge(road.eCity, dist[road.eCity]));
//				}
//			}
//		}
//		return -1;
//	}
//}
package TollgateIncrease; 

import java.util.*;

class UserSolution {

    class Edge implements Comparable<Edge> {
        int eCity;
        int k;      // 도로 개수
        int cost;  // '초기 비용' 합
        public Edge(int eCity, int k, int cost) {
            this.eCity = eCity;
            this.k = k;
            this.cost = cost;
        }
        @Override
        public int compareTo(Edge o) {
            return Integer.compare(this.cost, o.cost);
        }
    }

    class Road {
        int eCity;
        int cost; // 초기 비용
        public Road(int eCity, int cost) {
            this.eCity = eCity;
            this.cost = cost;
        }
    }

    ArrayList<Road>[] roads;
    int N;
    final int INF = Integer.MAX_VALUE;
    
    int totalCumulativeIncrease; 

    public void init(int N, int K, int sCity[], int eCity[], int mToll[]) {
        this.N = N;
        roads = new ArrayList[N];
        for (int i = 0; i < N; i++) {
        	roads[i] = new ArrayList<>();
        }
        
        totalCumulativeIncrease = 0; 

        for (int i = 0; i < K; i++) {
        	roads[sCity[i]].add(new Road(eCity[i], mToll[i]));
        	roads[eCity[i]].add(new Road(sCity[i], mToll[i]));
        }
    }

    public void add(int sCity, int eCity, int mToll) {
        // [!!!] add 시, 현재 비용(mToll)에서 누적 인상액을 빼서 '초기 비용'을 계산 [!!!]
        int originalCost = mToll - totalCumulativeIncrease;
        
        roads[sCity].add(new Road(eCity, originalCost));
        roads[eCity].add(new Road(sCity, originalCost));
//        roads[sCity].add(new Road(eCity, mToll));
//        roads[eCity].add(new Road(sCity, mToll));
    }

    public void calculate(int sCity, int eCity, int M, int mIncrease[], int mRet[]) {

        int[][] dist = new int[N][N];
        for (int i = 0; i < N; i++) {
            Arrays.fill(dist[i], INF);
        }
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        dist[sCity][0] = 0;
        pq.add(new Edge(sCity, 0, 0));

        while (!pq.isEmpty()) {
        	Edge now = pq.poll();
            if (now.cost > dist[now.eCity][now.k]) continue;
            int next_k = now.k + 1;
            if (next_k >= N) continue;

            for (Road road : roads[now.eCity]) {
                int neighbor = road.eCity;
                int newCost = now.cost + road.cost; // '초기 비용' 기준
                if (newCost < dist[neighbor][next_k]) {
                    dist[neighbor][next_k] = newCost;
                    pq.add(new Edge(neighbor, next_k, newCost));
                }
            }
        }

        int minCost = INF;
        for (int k = 0; k < N; k++) {
            if (dist[eCity][k] == INF) continue;
            minCost = Math.min(minCost, dist[eCity][k] + k * totalCumulativeIncrease);
        }
        mRet[0] = (int) minCost;

        for (int i = 0; i < M; i++) {
            totalCumulativeIncrease += mIncrease[i];
            minCost = INF;

            for (int k = 0; k < N; k++) {
                int originalCost = dist[eCity][k];
                if (originalCost == INF) continue;

                int currentCost = originalCost + k * totalCumulativeIncrease;
                minCost = Math.min(minCost, currentCost);
            }
            mRet[i + 1] = (int) minCost;
        }
        
    }
}