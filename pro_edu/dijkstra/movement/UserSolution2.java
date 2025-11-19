package pro_edu.dijkstra.movement;

import java.io.*;
import java.util.*;

class Edge implements Comparable<Edge>{
	int to;
	int cost;
	int time;
	int method;// 0 : 걷기, 1 : 자전거, 2 : 택시
	public Edge(int to, int cost, int time, int method) {
		super();
		this.to = to;
		this.cost = cost;
		this.time = time;
		this.method = method;
	}
	@Override
	public int compareTo(Edge o) {
		if(cost != o.cost) return cost - o.cost;
		if(time != o.time) return time - o.time;
		if(method != o.method) return method - o.method;
		if(to != o.to) return to - o.to;
		return 0;
	}
	@Override
	public String toString() {
		return "Edge [to=" + to + ", cost=" + cost + ", time=" + time + ", method=" + method + "]";
	}
	
}

class UserSolution2 {

	ArrayList<Edge> adjList[];
	boolean isStation[];
	int dist[][][];
	int N;
	
	public void init(int N) {
		this.N = N;
		adjList = new ArrayList[N + 1];
		isStation = new boolean[N + 1];
		dist = new int[N + 1][3][501];
		
		for(int i = 0; i < N + 1; i++)
			adjList[i] = new ArrayList<>();
		
		return;
	}
	// 그래프 구성 <-
	// ArrayList <- 인접리스트
	public void addRoad(int K, int[] mSpotA, int[] mSpotB, int[] mDis) {
		for(int i = 0; i < K; i++) {
			adjList[mSpotA[i]].add(new Edge(mSpotB[i], mDis[i], 0, 0));
			adjList[mSpotB[i]].add(new Edge(mSpotA[i], mDis[i], 0, 0));
		}
		return;
	}
	// 도시 번호 -> station 여부  [도시 번호]=station여부
	public void addBikeRent(int mSpot) {
		isStation[mSpot] = true;
		return;
	}
	
	
	public int getMinMoney(int mStartSpot, int mEndSpot, int mMaxTime) {
		for(int i = 0; i < N + 1; i++)
			for(int j = 0; j < 3; j++)
				for(int k = 0; k <= mMaxTime; k++)
					dist[i][j][k] = Integer.MAX_VALUE;
		
//		if(mStartSpot == 5 && mEndSpot == 9 && mMaxTime == 57) {
//			int de = 1;
//		}
		PriorityQueue<Edge> pq = new PriorityQueue(); 
		// 최소 비용 <- mMaxTime내에서 가능한 최대한 좋은 case들(최대한 빨리 가는 것들)
		
		pq.add(new Edge(mStartSpot,0,0,0));
		dist[mStartSpot][0][0] = 0;
		
		while(!pq.isEmpty()) {
			// now.to까지 now.method의 교통 수단으로 now.time동안 now.cost의 비용으로 도착
			Edge now = pq.poll();
			
			if(now.cost > dist[now.to][now.method][now.time]) continue;
			
			if(now.to == mEndSpot) {
				if(now.method == 1 && isStation[now.to])
					return now.cost;
				if(now.method == 2 || now.method == 0)
					return now.cost;
			}
			
			for(Edge element : adjList[now.to]) {
				if(now.method == 1) 
					useBike(now, element, mMaxTime, pq);
				else
					unUseBike(now, element, mMaxTime, pq);
			}
			
		}
		
		return -1;
	}
	
	void unUseBike(Edge now, Edge next, int timeLimit, PriorityQueue<Edge> pq) {
		//걷기, 택시
		addEdge(now, next, timeLimit, pq, 0);
		addEdge(now, next, timeLimit, pq, 2);
		if(isStation[now.to])
			//자전거
			addEdge(now, next, timeLimit, pq, 1);
	}
	
	void useBike(Edge now, Edge next, int timeLimit, PriorityQueue<Edge> pq) {
		//자전거
		addEdge(now, next, timeLimit, pq, 1);
		if(isStation[now.to]) {
			//걷기, 택시
			addEdge(now, next, timeLimit, pq, 0);
			addEdge(now, next, timeLimit, pq, 2);
		}
	}
	
	void addEdge(Edge now, Edge next, int timeLimit, PriorityQueue<Edge> pq, int method) {
		int costList[] = {0, 4, 19};
		int timeList[] = {17, 4, 1};
		
		int nextCost = now.cost + next.cost * costList[method];
		int nextTime = now.time + next.cost * timeList[method];
		
		if(now.method != 2 && method == 2)
			nextTime += 7;
		
		if(nextTime > timeLimit || dist[next.to][method][nextTime] <= nextCost)
			return;
		
		dist[next.to][method][nextTime] = nextCost;
		pq.add(new Edge(next.to, nextCost, nextTime, method));
	}
}