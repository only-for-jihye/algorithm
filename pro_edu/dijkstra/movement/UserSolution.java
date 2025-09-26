package pro_edu.dijkstra.movement;

import java.io.*;
import java.util.*;

class UserSolution {
  
	int N;
	ArrayList<Edge>[] adjList;
	boolean[] isRent;
	
	public void init(int N) {
		this.N = N;
		adjList = new ArrayList[N + 1];
		for (int i = 0; i <= N; i++) {
			adjList[i] = new ArrayList<>();
		}
		isRent = new boolean[N + 1];
	}

	public void addRoad(int K, int[] mSpotA, int[] mSpotB, int[] mDis) {
		for (int i = 0; i < K; i++) {
			adjList[mSpotA[i]].add(new Edge(mSpotB[i], mDis[i], 0, 0));
			adjList[mSpotB[i]].add(new Edge(mSpotA[i], mDis[i], 0, 0));
		}
	}

	public void addBikeRent(int mSpot) {
		isRent[mSpot] = true;
	}

	// 최소 비용 <- [특정위치][특정시간][교통수단]
	// 최소 비용 <- 나머지 정보들 하나하나 차원으로... 주는 정보들은 모두 중요한 정보들이다
	// 쓸데 없는 건 안줌
	public int getMinMoney(int mStartSpot, int mEndSpot, int mMaxTime) {
		
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		int[][] dist = new int[N + 1][3]; // 0 도보, 1 자전거, 2 택시
//		int[][][] dist = new int[N + 1][501][3];
		for (int i = 0; i <= N; i++) {
			for (int j = 0; j < 3; j++) {
				dist[i][j] = Integer.MAX_VALUE;
			}
		}
		pq.add(new Edge(mStartSpot, 0, 0, 0));
		for (int i = 0; i < 3; i++) {
			dist[mStartSpot][i] = 0;
		}
		
		while (!pq.isEmpty()) {
			Edge now = pq.poll();
			if (now.dest == mEndSpot) return now.cost;
			for (Edge edge : adjList[mStartSpot]) {
				// 시간 / 비용 따로따로 구해야함
				// 0. 도보
				int nextTime = now.time + edge.dist * 17;
				if (nextTime > mMaxTime) continue;
				if (nextTime < dist[edge.dest][0]) {
					dist[edge.dest][0] = nextTime;
					pq.add(new Edge(edge.dest, edge.dist, dist[edge.dest][0], 0));
				}
				// 1. 자전거
				nextTime = now.time + edge.dist * 4;
				int nextCost = now.cost + edge.dist * 4;
				if (nextTime > mMaxTime) continue;
				if (isRent[now.dest] && nextTime < dist[edge.dest][1]) {
					dist[edge.dest][1] = nextTime;
					pq.add(new Edge(edge.dest, edge.dist, dist[edge.dest][1], nextCost));
				}
				// 2. 택시
				nextTime = now.time + edge.dist + 7;
				nextCost = now.cost + edge.dist * 19;
				if (nextTime > mMaxTime) continue;
				if (nextTime < dist[edge.dest][2]) {
					dist[edge.dest][0] = nextTime;
					pq.add(new Edge(edge.dest, edge.dist, dist[edge.dest][2], nextCost));
				}
			}
		}
		
		return -1;
	}
}

//class Edge implements Comparable<Edge> {
//	int dest;
////	int dist;
//	int time;
//	int cost;
//	int method; // 수단
//
//	public Edge(int dest, int time, int cost, int method) {
//		super();
//		this.dest = dest;
//		this.time = time;
//		this.cost = cost;
//		this.method = method;
//	}
//
//	@Override
//	public int compareTo(Edge o) {
//		return Integer.compare(this.dist, o.dist);
//	}
//	
//}