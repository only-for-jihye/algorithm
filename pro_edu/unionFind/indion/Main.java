package pro_edu.unionFind.indion;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

	static String[] basic;
	static int[] parents;
	static int[] child;
	static boolean[] isGroup;
	static int groupCount;
	
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
	
		basic = new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"
				, "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
		}; // ASCII A 65 -> -65로 다빼주면 0부터 index
		
		int N = Integer.parseInt(st.nextToken());
		parents = new int[26];
		child = new int[26];
		isGroup = new boolean[26];
		for (int i = 0; i < 26; i++) {
			parents[i] = i;
			child[i] = i;
		}
		groupCount = 0;
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			String aInput = st.nextToken();
			String bInput = st.nextToken();
			union(aInput, bInput);
		}
		System.out.println(groupCount);
		int noGroup = 0;
		for (int i = 0; i < isGroup.length; i++) {
			if (!isGroup[i]) noGroup++;
		}
		System.out.println(noGroup);
	}
	
	public static void union(String aChild, String bChild) {
		int aBoss = find(aChild);
		int bBoss = find(bChild);
		
		if (aBoss == bBoss) return;
		parents[aBoss] = bBoss;
		groupCount++;
		isGroup[aBoss] = true;
		isGroup[bBoss] = true;
	}
	
	public static int find(String value) {
		int childIdx = getIndex(value);
		if (childIdx == parents[childIdx]) return childIdx;
		
		int boss = find(value);
		return boss;
	}
	
	public static int getIndex(String node) {
		for (int i = 0; i < basic.length; i++) {
			if (basic[i].equals(node)) return i;
		}
		return -1;
	}
	
}
