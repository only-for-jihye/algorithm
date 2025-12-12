package 경유지운송;

import java.io.*;
import java.util.*;

class UserSolution {
	
	class Edge {
		int to;
		int cost;
		Edge(int to, int cost) {
			this.to = to;
			this.cost = cost; 
		}
	}
	
	ArrayList<Edge>[] al; 
	int[] visited;
	int cc; 
	
	void pushEdge(int st, int en, int cost) {
		al[st].add(new Edge(en, cost));
		al[en].add(new Edge(st, cost)); 
	}
	
	public void init(int N, int K, int[] sCity, int[] eCity, int[] mLimit) {
		al = new ArrayList[N];
		for(int i = 0; i < N; i++) {
			al[i] = new ArrayList<>();
		}
		visited = new int[N];
		cc = 0; 
		
		// init
		for(int i = 0; i < K; i++) {
			pushEdge(sCity[i], eCity[i], mLimit[i]); 
		}
		return;
	}

	// 1,400
	public void add(int sCity, int eCity, int mLimit) {
		pushEdge(sCity, eCity, mLimit); 
		return; 
	}

	// 100
	// 100 x BFS x parameteric 
	// 100 x (V+E) x log(30000)
	// 100 x (1000+3400) x 15
	// 6,600,000
	public int calculate(int sCity, int eCity, int M, int[] mStopover) {
		int left = 0;
		int right = 30000; 
		
		while(left <= right) {
			int mid = (left + right) / 2;
			if(isPossible(mid, sCity, eCity, M, mStopover)) 
				left = mid + 1; 
			else
				right = mid - 1; 
		}
		return right; 
	}
	
	boolean isPossible(int limit, int st, int en, int m, int[] stops) {
		ArrayDeque<Integer>q = new ArrayDeque<>();
		q.add(st); 
		
		cc++;
		visited[st] = cc;
		int cnt = 0; 
		
		while(!q.isEmpty()) {
			int now = q.remove(); 
			
			// 가지치기 -> 경로 내에 이미 조건이 충족되었다면 더 확인 필요 X
			if(now == en && cnt == m)
				return true;
			
			// 만약 경유지를 하나 확인했다면 -> 경유지 개수 count 
			for(int i = 0; i < m; i++) {
				if(now == stops[i]) {
					cnt++;
					break; 
				}
			}
			
			for(Edge next : al[now]) {
				if(visited[next.to] == cc)
					continue;
				if(next.cost < limit)
					continue; 
				visited[next.to] = cc;
				q.add(next.to);
			}
		}
		
		// 마지막 체크
		// #1. en에 도달할 수 있었고, m개의 경유지를 다 거칠수 있었는지
		if(visited[en] == cc && m == cnt)
			return true;
		return false;
	}
}