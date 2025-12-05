package 광물운송;

//UserSolution.java
import java.util.*;
import java.io.*;

//2025.05.09. 광물운송
//@admin_deukwha
//union find + spatial partioning

class UserSolution {
	
	class BaseCamp implements Comparable <BaseCamp>{
		int id; // 실제 id
		int y;
		int x; 
		int q;  // 채굴량
		BaseCamp(int id, int y, int x, int q) {
			this.id = id;
			this.y = y;
			this.x = x;
			this.q = q; 
		}
		@Override
		public int compareTo(BaseCamp o) {
			if(q < o.q) return -1;
			if(q > o.q) return 1;
			if(y < o.y) return -1;
			if(y > o.y) return 1; 
			if(x < o.x) return -1;
			if(x > o.x) return 1; 
			return 0;
		}
	}
	
	// index : N / L 크기로 나눠진 영역 번호
	// value : 이제 이 영역에 사는 데이터 = 베이스캠프의 ID 
	ArrayList<Integer>[][]map;
	
	// id.seq.
	HashMap<Integer, Integer>hm;
	int idCnt;
	
	int mod; // 얘 크기로 영역을 나눈다! -> 영역 찾기 위해선 얘로 나눠서 사용
	
	// union find용
	int[] parent; 
	int[] quantities; // index : 그룹/개인 번호, value : 이 그룹의 채굴량
	
	// index : seq.된 베이스캠프 id
	// value : 이 basecamp 정보
	BaseCamp[] basecamps; 
	
	int[] dy = {0, -1, 1, 0, 0, -1, 1, -1, 1};
	int[] dx = {0, 0, 0, -1, 1, -1, -1, 1, 1};
	
	int register(int id) {
		if(hm.get(id) == null) hm.put(id, idCnt++);
		return hm.get(id);
	}
	
	int find(int node) {
		if(node == parent[node]) return node;
		return parent[node] = find(parent[node]); 
	}
	
	void union(int a, int b) {
		int pa = find(a);
		int pb = find(b);
		if(pa == pb) return; 

		// #1. quantity 작은순 우선, #2. y 작은순 우선, #3. x작은 우선
		if(basecamps[pa].compareTo(basecamps[pb]) < 0) {
			// 대표를 pa로 만들어준다.
			parent[pb] = pa;
			quantities[pa] += quantities[pb];
		}
		else {
			// 대표를 pb로 만들어준다.
			parent[pa] = pb;
			quantities[pb] += quantities[pa];
		}
	}

	void init(int L, int N){
		mod = L; 
		map = new ArrayList[31][31]; // L*30 => 31번째 영역에 들어갈거니까
		for(int i = 0; i <= 30; i++) 
			for(int j = 0; j <= 30; j++) 
				map[i][j] = new ArrayList<>();
		hm = new HashMap<>();
		idCnt = 0; 
		// 베이스캠프의 최대 수 = 20,000
		parent = new int[20001];
		quantities = new int[20001];
		basecamps = new BaseCamp[20001];
	}
	
	// 호출횟수 : 20,000	
	int addBaseCamp(int mID, int mRow, int mCol, int mQuantity){
		// #1. mID -> id sequencing
		int id = register(mID); 
		// 이 베이스캠프에 대한 정보를 정리.
		basecamps[id] = new BaseCamp(mID, mRow, mCol, mQuantity);
		parent[id] = id; 
		quantities[id] = mQuantity; // 이 그룹의 총 채굴량 = 일단 mQuantity로 시작
		
		// #2. 이 ID의 베이스캠프가 닿을수 있는 영역들 확인하면서
		int y = mRow / mod;
		int x = mCol / mod; 

		// -> 상하좌우 + 대각선방향 + 내 위치까지 9개를 확인 (y,x)영역
		for(int i = 0; i < 9; i++) {
			int ny = y + dy[i];
			int nx = x + dx[i];
			// 방향배열 = 무조건 범위 체크
			if(ny < 0 || nx < 0 || ny > 30 || nx > 30) continue; 
			for(int nid : map[ny][nx]) {
				int dist = Math.abs(mRow - basecamps[nid].y) + Math.abs(mCol - basecamps[nid].x);
				if(dist > mod) continue;
				union(nid, id); 
			}
		}
		map[y][x].add(id); 
		return quantities[find(id)];
	}
	
	// 호출횟수 : 200
	int findBaseCampForDropping(int K){

		BaseCamp ret = new BaseCamp(-1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE); 
		for(int i = 0; i < idCnt; i++) {
			if(i != find(i)) continue; 
			if(quantities[i] < K) continue;
			if(ret.compareTo(basecamps[i]) > 0) ret = basecamps[i];
		}
		return ret.id;
	}
}