import java.io.*;
import java.util.*; 

class UserSolution_gemini {
	
	static int[] parent; 
	static int[] soldiers; 
	// key : name
	// value : id (hash)
	static HashMap<String, Integer>hm; 
	static HashMap<Integer,Integer>[]enemies; 
	
	static int[]ydir = {-1, 1, 0, 0, -1, 1, -1, 1};
	static int[]xdir = {0, 0, -1, 1, -1, -1, 1, 1}; 
	static int n; // size
	
	static int Find(int node) {
		if(node == parent[node])
			return node;
		return parent[node] = Find(parent[node]); 
	}
	
	static int getHash(int y, int x) {
		// 겹치지 않는 범위로 설정
		return y * n + x; 
	}
	
	static int getSoldier(int y, int x, int p) {
		int cnt = 0;
		for(int i = 0; i < 8; i++) {
			int ny = y + ydir[i];
			int nx = x + xdir[i];
			if(ny < 0 || nx < 0 || ny >= n || nx >= n)
				continue;
			int id = getHash(ny, nx); 
			// 같은 동맹이라면 절반을 보내준다.
			if(Find(id) == p) {
				int temp = soldiers[id] / 2; 
				cnt += temp;
				soldiers[id] -= temp;  
			}
		}
		return cnt; 
	}
	
	// 50 x O(NxN) = 50 x 625 = 31,1250
	public void init(int N, int[][] soldier, char[][][] monarch) {
		parent = new int[N*N*2];
		soldiers = new int[N*N*2];
		enemies = new HashMap[N*N*2]; 
		n = N; 
		
		hm = new HashMap<>(); 
		
		for(int i = 0; i < N*N*2; i++)
			enemies[i] = new HashMap<>();
		
		// init
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				int id = getHash(i, j); 
				String name = String.valueOf(monarch[i][j]);
				soldiers[id] = soldier[i][j]; 
				hm.put(name, id); 
				// 추후 id번 땅의 동맹 변환을 위해 "변하지 않는" 번호로 parent 설정
				parent[id] = id + N*N;
				// 최상위 parent
				parent[id+N*N] = id + N*N; 
			}
		}
		return;
	}
	
	public void destroy() {
		return; 
	}
	
	// 8,000 x O(624) + @(overhead) => 모두가 적이라고 가정시 (이런 경우 없음)
	// 약 5,000,000 + @ 
	public int ally(char[] monarchA, char[] monarchB) {
		int A = hm.get(String.valueOf(monarchA));
		int B = hm.get(String.valueOf(monarchB));
		
		int pa = Find(A);
		int pb = Find(B); 
		
		// 이미 동맹관계이면 -1
		if(pa == pb) 
			return -1; 
		
		// 적대관계라면 -2
		if(enemies[pa].get(pb) != null)
			return -2; 
		
		// 동맹
		parent[pb] = pa; 
		// pb의 적들을 모두 pa의 적들로 설정
		for(Map.Entry<Integer, Integer>ent : enemies[pb].entrySet()) {
			int enemyID = ent.getKey();
			int pe = Find(enemyID);
			enemies[pa].put(pe, 1);
			enemies[pe].put(pa, 1);
		}
		return 1; 
	}
	
	// 8,000 x 3 x O(8) (세번 주변 확인)
	// 8,000 x 24 = 192,000
	public int attack(char[] monarchA, char[] monarchB, char[] general) {
		int A = hm.get(String.valueOf(monarchA));
		int B = hm.get(String.valueOf(monarchB));
		
		int pa = Find(A);
		int pb = Find(B);
		
		// 이미 동맹관계이면 -1
		if(pa == pb)
			return -1;
		
		// B영토 주변에 동맹이 주변에 없으면 -2
		int y = B / n;
		int x = B % n; 
		int flag = 0; 
		for(int i = 0; i < 8; i++) {
			int ny = y + ydir[i];
			int nx = x + xdir[i];
			if(ny < 0 || nx < 0 || ny >= n || nx >= n)
				continue;
			int id = getHash(ny, nx); 
			// 주변에 동맹을 찾았다!
			if(Find(id) == pa)
				flag = 1; 
		}
		// 동맹이 없으면 -2
		if(flag == 0)
			return -2; 
		
		// 서로 적대 관계가 된다. 
		enemies[pa].put(pb, 1);
		enemies[pb].put(pa, 1);
		
		// B 지역을 기반으로 주변에 있는 동맹의 절반의 병사를 보낸다.
		int soldierCntA = getSoldier(y, x, pa);
		// B 지역은 해당 지역의 병사 추가
		int soldierCntB = getSoldier(y, x, pb) + soldiers[B]; 
		
		int res = 0; // 결과
		// 공격 성공시
		if(soldierCntA > soldierCntB) {
			// monarchB 군주는 처형
			hm.remove(String.valueOf(monarchB)); 
			// 새로운 군주 등록
			hm.put(String.valueOf(general), B);
			soldiers[B] = soldierCntA - soldierCntB; 
			// A군의 동맹 지역으로 설정
			parent[B] = pa; 
			res = 1; 
		}
		else {
			// 방어 성공 -> 병사 소모
			soldiers[B] = soldierCntB - soldierCntA; 
		}
		return res; 
	}
	
	// O(N*N) = 25 x 25 = 625 x 13,000 = 8,125,000
	public int recruit(char[] monarchA, int num, int sign) {
		int A = hm.get(String.valueOf(monarchA));
		// sign == 0 : A의 병사 증가
		if(sign == 0) {
			soldiers[A] += num;
			return soldiers[A]; 
		}
		// sign == 1 : 모든 동맹의 병사 증가
		else {
			int sum = 0; 
			int pa = Find(A); 
			// 모든 동맹의 병사 수 업데이트
			for(int i = 0; i < n; i++) {
				for(int j = 0; j < n; j++) {
					int id = getHash(i, j);
					if(Find(id) == pa) {
						soldiers[id] += num;
						sum += soldiers[id];
					}
				}
			}
			return sum; 
		}
	}
}