package 삼국지게임;

import java.io.*;
import java.util.*; 

// 각 API에 전달되는 문자는 모두 소문자
// 군주 이름 또한 4이상 10이하 소문자 -> Hash
// Union-Find
class UserSolution {

	int N;
	HashMap<String, Integer> hm; // id에 따라 군주 이름
	int[] soldiers; // id에 따른 병사 수
	String[] monarchs; // id에 따른 이름
	int[] parent;
	HashMap<Integer, Integer>[] enemys;
	
	int[] xdir = {-1, -1, 0, 1, 1, 1, 0, -1};
	int[] ydir = {0, 1, 1, 1, 0, -1, -1, -1};
	
	int getHash(int x, int y) {
		return x * N + y;
	}
	
	String toString(char[] name) {
//		StringBuilder sb = new StringBuilder();
//		for (char n : name) {
//			sb.append(n);
//		}
//		return sb.toString();
		return String.valueOf(name);
	}
	
	public void init(int N, int[][] soldier, char[][][] monarch) {
		this.N = N;
		soldiers = new int[25 * 25 * 2];
		monarchs = new String[25 * 25 * 2];
		hm = new HashMap<>();
		parent = new int[25 * 25 * 2]; // 대충 ..
		enemys = new HashMap[25 * 25 * 2];
		for (int i = 0; i < 25 * 25 * 2; i++) {
			enemys[i] = new HashMap<>();
		}
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				int id = getHash(i, j);
				soldiers[id] = soldier[i][j];
				monarchs[id] = toString(monarch[i][j]);
				hm.put(toString(monarch[i][j]), id);
				parent[id] = id + N * N;
				parent[id + N * N] = id + N * N; // 가장 최상위
			}
		}
		return;
	}
	
	public void destroy() {
		return; 
	}
	
	int find(int node) {
		if (node == parent[node]) {
			return node;
		}
		return parent[node] = find(parent[node]);
	}
	
	// 8,000
	public int ally(char[] monarchA, char[] monarchB) {
		// 동맹 관계
		String getAName = toString(monarchA);
		String getBName = toString(monarchB);
		
		int aId = hm.get(getAName);
		int bId = hm.get(getBName);
		
		int parentA = find(aId);
		int parentB = find(bId);
		
		// 이미 동맹 관계 
		if (parentA == parentB) return -1;
		// 이미 적대 관계
		if (enemys[parentA].get(parentB) != null) return -2;
		
		// 동맹 관계가 맺어짐
		parent[parentB] = parentA;
		
		// parentB의 적대관계가 parentA에게 상속됨
		for (Map.Entry<Integer, Integer> entry : enemys[parentB].entrySet()) {
			int enemyId = entry.getKey();
			int parentEenemy = find(enemyId);
			// 서로 적대관계 등록
			enemys[parentA].put(parentEenemy, 1);
			enemys[parentEenemy].put(parentA, 1);
		}
		return 1; 
	}
	
	// 8,000
	public int attack(char[] monarchA, char[] monarchB, char[] general) {
		// A -> B 공격, 대장은 general
		int aId = hm.get(toString(monarchA));
		int bId = hm.get(toString(monarchB));
		// 군주 monarchA 와 군주 monarchB 가 동맹관계 이면 -1을 반환
		int pa = find(aId);
		int pb = find(bId);
		if (pa == pb) return -1;
		// 군주 monarchA 의 영토 또는 동맹 영토가 군주 monarchB 의 영토와 인접하지 않다면 -2을 반환
		int x = bId / N;
		int y = bId % N;
		// 8방향 탐색
		int flag = 0;
		for (int i = 0; i < 8; i++) {
			int nx = x + xdir[i];
			int ny = y + ydir[i];
			if (nx < 0 || ny < 0 || nx >= N || ny >= N) continue;
			int id = getHash(nx, ny);
			// 인접에 우리 아군이 있다
			if (find(id) == pa) {
				flag = 1;
			}
		}
		if (flag == 0) return -2; // 인접에 아군이 없음
		
		// 공격 시작
		// 적대 관계 설정
		enemys[pa].put(pb, 1);
		enemys[pb].put(pa, 1);
		
		// 군사 절반 파견
		// monarchA와 인접한 군주들, monarchB와 인접한 군주들 병력 반반 얻음
		int soldierA = getSoldier(bId, pa);
		int soldierB = getSoldier(bId, pb) + soldiers[bId];
		
		// 공격 성공
		if (soldierA - soldierB > 0) {
			// 공격이 성공하면 군주 monarchB 는 처형되고, monarchB 가 다스렸던 영토는 멸망하여 동맹관계도 적대관계도 없는 새로운 영토가 된다.
//			새로운 영토의 군주는 general 이 되고, monarchA의 동맹에 편입되며, 적대 관계는 monarchA 의 적대 관계와 동일하다.
//			각 군주 이름은 알파벳 소문자로 이루어져 있으며, 길이는 4 이상 10 이하의 문자열이다.
//			monarchA 와 monarchB 는 현재 군주임이 보장된다. general 는 군주가 아님이 보장된다.
			hm.remove(toString(monarchB)); // 처형
			hm.put(toString(general), bId); // general의 영토가 됨
			parent[bId] = pa;
			soldiers[bId] = soldierA - soldierB;
			return 1;
		} else { // 공격 실패
			soldiers[bId] = soldierB - soldierA;
			return 0;
		}
	}
	
	private int getSoldier(int id, int parent) {
		int x = id / N;
		int y = id % N;
		int count = 0;
		for (int i = 0; i < 8; i++) {
			int nx = x + xdir[i];
			int ny = y + ydir[i];
			if (nx < 0 || ny < 0 || nx >= N || ny >= N) continue;
			int nid = getHash(nx, ny);
			// 아군이라면
			if (find(nid) == parent) {
				int soldier = soldiers[nid] / 2;
				soldiers[nid] -= soldier;
				count += soldier;
			}
		}
		return count;
	}

	// 13,000
	public int recruit(char[] monarchA, int num, int sign) {
		int id = hm.get(toString(monarchA));
		if (sign == 0) { // 하나의 군주에만 병사 모집
			soldiers[id] += num;
			return soldiers[id];
		} else { // 동맹국 모두 병사 모집
			// 싹 다 뒤져봄
			int pa = find(id);
			int sum = 0;
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					int getId = getHash(i, j);
					if (find(getId) == pa) {
						soldiers[getId] += num; // 각 동맹국마다 병사 모집
						sum += soldiers[getId];
					}
				}
			}
			return sum;
		}
	}
}




