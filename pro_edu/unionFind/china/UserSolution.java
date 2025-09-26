package pro_edu.unionFind.china;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class UserSolution
{
	
	static int[] parents;
	static int[] populations;
	static int nationCnt;
	
	public void init(int N, int[] countries)
	{
		nationCnt = N;
		parents = new int[N];
		populations = new int[N];
		for (int i = 0; i < N; i++) {
			parents[i] = i;
			populations[i] = countries[i];
		}
	}
	
	public int alliance(char A, char B)
	{
		int a = A - 'A';
		int b = B - 'A';

		if (find(a) == find(b)) return -1;
		if (populations[find(a)] == 0 || populations[find(b)] == 0) {
			return -1;
		}
		union(a, b, 1);
		
		return 1; 
	}
    
	public int war(char A, char B)
	{
		int a = A - 'A';
		int b = B - 'A';
		if (find(a) == find(b)) return -1;
		if (populations[find(a)] == 0 || populations[find(b)] == 0) {
			return -1;
		}
		// 전쟁을 치룬다
//		int aPopulation = populations[find(a)];
//		int bPopulation = populations[find(b)];
//		if (aPopulation > bPopulation) {
//			aPopulation -= bPopulation;
//			bPopulation = 0;
//			parents[find(b)] = find(a);
//		} else if (aPopulation < bPopulation) {
//			bPopulation -= aPopulation;
//			aPopulation = 0;
//			parents[find(a)] = find(b);
//		} else {
//			aPopulation = 0;
//			bPopulation = 0;
//		}
//		populations[find(a)] = aPopulation;
//		populations[find(b)] = bPopulation;
		union(a, b, -1);
		return 1;
	}

	public int getCnt()
	{
//		int cnt = 0;
//		for (int i = 0; i < nationCnt; i++) {
//			if (populations[i] > 0) {
//				cnt++;
//			}
//		}
//		return cnt; 
		return nationCnt;
	}
	
	public int getPopCnt(char A)
	{
		int a = A - 'A';
		int par = find(a);
		return populations[par];
	}
	
	public int find(int value) {
		if (value == parents[value]) return value;
		return parents[value] = find(parents[value]);
	}
	
	public void union(int A, int B, int sign) {
		int aBoss = find(A);
		int bBoss = find(B);
		if (aBoss == bBoss) return;
		if (populations[aBoss] < populations[bBoss]) {
			int temp = aBoss;
			aBoss = bBoss;
			bBoss = temp;
		} // 인구 수가 A쪽이 많도록
		parents[bBoss] = aBoss; // A 밑에 B를 넣음
		populations[aBoss] += populations[bBoss] * sign;
		populations[bBoss] = 0;
		if (populations[aBoss] == 0) nationCnt --;
		if (populations[bBoss] == 0) nationCnt --;
	}
}