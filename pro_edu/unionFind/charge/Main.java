package pro_edu.unionFind.charge;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	
	static int[] parents;

	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
	
		int T = Integer.parseInt(st.nextToken());
		parents = new int[T + 1];
		for (int i = 1; i <= T; i++) {
			parents[i] = i;
		}
		
		st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken());
		int answer = 0;
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			int version = Integer.parseInt(st.nextToken());
			if (version > T) version = T;
			int guessBoss = find(version);
			
			// 더이상 충전소 사용이 불가함
			if (guessBoss == 0) break;
			// guessBoss를 사용하면 그 다음부턴 해당 Boss를 못 쓰고 그 앞으로 가야함
			union(guessBoss - 1, guessBoss);
			answer++;
		}
		System.out.println(answer);
	}
	
	static int find(int node) {
		if (node == parents[node]) return node;
		return parents[node] = find(parents[node]);
	}
	
	static void union(int A, int B) {
		int aBoss = find(A);
		int bBoss = find(B);
		if (aBoss == bBoss) return;
		if (aBoss < bBoss) parents[bBoss] = aBoss;
		else parents[aBoss] = bBoss;
	}
}
