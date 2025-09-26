package pro_edu.unionFind.indion;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Solution {
	
	static int[] parents;
	// index : boss 번호, value : 해당 그룹의 인원 수
	static int groupPopulations[] = new int[26];
	static int teamCount = 0;
	static int personalCount = 26;
	
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
	
		int N = Integer.parseInt(st.nextToken());
		parents = new int[26];
		
		// 초기화
		for (int i = 0; i < 26; i++) {
			parents[i] = i;
			groupPopulations[i] = 1;
		}
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			// 'A' <- 정수화
			// 'A' - 'A' : 0
			// 'B' - 'A' : 1
			// ...
			// 'Z' - 'Z' : 25
			int A = st.nextToken().charAt(0) - 'A'; // 0
			int B = st.nextToken().charAt(0) - 'A'; // 0
			union(A, B);
		}

		// 1번 방법
//		int teamCount = 0;
//		int personalCount = 0;
//		
//		for (int i = 0; i < 26; i++) {
//			int boss = find(i);
//			if (boss != i) continue; // 보스가 아니면 무시
//			// 그룹별로 확인 ㄹ<- 그룹당 1번만 확인
//			if (groupPopulations[boss] == 1) {
//				personalCount++;
//			} else {
//				teamCount++;
//			}
//		}
		// 2번 방법
		// 계산할 대마다 ++ ,--
		System.out.println(teamCount);
		System.out.println(personalCount);
	}
	
	static int find(int node) {
		if (node == parents[node]) return node;
		return parents[node] = find(parents[node]);
	}
	
	static void union(int A, int B) {
		int aBoss = find(A);
		int bBoss = find(B);
		if (aBoss == bBoss) return;
		parents[bBoss] = aBoss;
		// 그룹 수 관리
		if (groupPopulations[aBoss] == 1 && groupPopulations[bBoss] == 1) teamCount++;
		else if(groupPopulations[aBoss] != 1 && groupPopulations[bBoss] != 1) {
			teamCount--;
		}
		// 개인 인원수 관리
		if (groupPopulations[aBoss] == 1) personalCount--;
		if (groupPopulations[bBoss] == 1) personalCount--;
		
		groupPopulations[aBoss] += groupPopulations[bBoss];
		groupPopulations[bBoss] = 0;
	}
}
