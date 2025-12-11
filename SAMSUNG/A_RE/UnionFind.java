package A_RE;

import java.util.HashMap;

public class UnionFind {

	public static void main(String[] args) {
		
		idToString = new String[10];
		// idToString
		HashMap<String, Integer> hm = new HashMap<>();
		String str = "A";
		int idCount = 0;
		if (hm.get(str) == null) {
			hm.put(str, idCount++);
		}
		int id = hm.get(str);
		idToString[id] = str;
		
		System.out.println(idToString[0]);
		System.out.println(hm.toString());
	}
	
	static int[] parents;
	static int[] wordCount;
	static int[] groupCount;
	static String[] idToString;
	
	int find(int n) {
		if (parents[n] == n) return n;
		return parents[n] = find(parents[n]);
	}
	
	void union(int a, int b) {
		int pa = find(a);
		int pb = find(b);
		
		if (pa == pb) return;
		
		int winner = -1;
		int loser = -1;
		
		if (wordCount[pa] > wordCount[pb]) {
			winner = pa;
			loser = pb;
		} else if (wordCount[pa] < wordCount[pb]) {
			winner = pb;
			loser = pa;
		} else {
			if (idToString[pa].compareTo(idToString[pb]) < 0) {
				winner = pa;
				loser = pb;
			} else {
				winner = pb;
				loser = pa;
			}
		}
		
		parents[loser] = winner;
		groupCount[winner] += groupCount[loser];
	}
	
}
