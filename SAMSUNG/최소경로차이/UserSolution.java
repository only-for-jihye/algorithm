package 최소경로차이;

import java.io.*;
import java.util.*;

/**
 * parametric search + bfs 풀이
 */

class UserSolution {
	
	class Edge {
		int id;
		int to;
		int cost;
		public Edge(int id, int to, int cost) {
			super();
			this.id = id;
			this.to = to;
			this.cost = cost;
		}
	}
	
	ArrayList<Edge>[] al;
	HashMap<Integer, Integer> hm;
	int idCnt;
	int[] removed;
	int[] visited;
	int cc; // bfs에서 각 bfs를 돌 때마다 방문 체크하기 위함, 계속 초기화 하면 시간이 넘 오래걸림
	int N;
	
	int register(int id) {
		if (hm.get(id) == null) {
			hm.put(id, idCnt++);
		}
		return hm.get(id);
	}
	
	public void init(int N, int K, int[] mId, int[] sCity, int[] eCity, int[] mCost) {
		al = new ArrayList[N];
		for (int i = 0; i < N; i++) {
			al[i] = new ArrayList<>();
		}
		hm = new HashMap<>();
		removed = new int[2001];
		visited = new int[N];
		cc = 0;
		idCnt = 0;
		this.N = N;
		for (int i = 0; i < K; i++) {
			add(mId[i], sCity[i], eCity[i], mCost[i]);
		}
		return;
	}

	// 700
	public void add(int mId, int sCity, int eCity, int mCost) {
		int id = register(mId);
		// 단방향
		al[sCity].add(new Edge(id, eCity, mCost));
		return;
	}

	// 300
	public void remove(int mId) {
		int id = hm.get(mId);
		removed[id] = -1;
		hm.remove(mId);
		return; 
	}

	// 30
	public int cost(int sCity, int eCity) {
		// parametric search
		int left = 0;
		int right = 500; // 비용 최대 값이 500이라고 되어 있음, 1~500이므로 차이도 500
		while (left <= right) {
			int mid = (left + right) / 2; // 값의 차이 = mid
			if (validate(mid, sCity, eCity)) { // 갈 수 있어 없어
				right = mid - 1;
			} else {
				left = mid + 1;
			}
		}
		if (left > 500) {
			return -1;
		}
		return left;
	}

	private boolean validate(int limit, int sCity, int eCity) {
		// parametric search validate
		for (int i = 1; i <= 500 - limit; i++) {
			int minCost = i; // 최소 비용
			int maxCost = minCost + limit; // 최대 비용
			if (bfs(sCity, eCity, limit, minCost, maxCost)) { // 모든 값의 차이를 직접 검증
				return true;
			}
		}
		return false;
	}

	private boolean bfs (int start, int end, int limit, int minCost, int maxCost) {
		// bfs
		// 갈 수 있는지 없는지만 확인
		ArrayDeque<Edge> ad = new ArrayDeque<>();
		ad.add(new Edge(0, start, 0));
		
		cc++; // 매 bfs마다 방문을 체크, 초기화 하지 않고 쓰는 방법 ★
		visited[start] = cc;
		
		while(!ad.isEmpty()) {
			Edge now = ad.pollLast();
			
			if (now.to == end) {
				return true;
			}
			
			for (Edge next : al[now.to]) {
				// 삭제
				if (removed[next.id] == -1) {
					continue;
				}
				// 이미 방문
				if (visited[next.to] == cc) {
					continue;
				}
				// cost가 최소값을 넘거나 최대값을 넘으면 패스
				if (next.cost < minCost || next.cost > maxCost) {
					continue;
				}
				visited[next.to] = cc;
				ad.add(next);
			}
		}
		return false;
	}
}