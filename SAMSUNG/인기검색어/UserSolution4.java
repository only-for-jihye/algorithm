package 인기검색어;

import java.io.*;
import java.util.*;

class UserSolution4 {
	
	int[] parents;
	int[] groupCount;
	int[] wordCount;
	String[] idToString;
	
	// union-find
	int find (int n) {
		if (parents[n] == n) return n;
		return parents[n] = find(parents[n]);
	}
	
	void union(int a, int b) {
		int pa = find(a);
		int pb = find(b);
		
		if (pa == pb) return;
		
		int winner = -1, loser = -1;
		
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
	
	ArrayDeque<String> dq;
	int N;
	int idCount;

	public void init(int N) {
		this.N = N;
		this.dq = new ArrayDeque<>();
		parents = new int[501];
		groupCount = new int[501];
		wordCount = new int[501];
		idToString = new String[501];
	}
  
	public void addKeyword(String mKeyword) {
		dq.addLast(mKeyword);
		if (dq.size() > N) {
			dq.removeFirst();
		}
	}
	
	public int top5Keyword(String mRet[]) {
		HashMap<String, Integer> hm = new HashMap<>();
		HashMap<String, Integer> similar = new HashMap<>();
		
		for (int i = 0; i < dq.size(); i++) {
			parents[i] = i;
			groupCount[i] = 0;
			wordCount[i] = 0;
		}
		
		idCount = 0;
		for (String word : dq) {
			if (hm.get(word) == null) {
				hm.put(word, idCount++);
			}
			int id = hm.get(word);
			groupCount[id]++;
			wordCount[id]++;
			idToString[id] = word;
		}
		
		for (String word : dq) {
			int id = hm.get(word);
			for (int i = 0; i < word.length(); i++) {
				StringBuilder sb = new StringBuilder();
				sb.append(word.substring(0, i));
				sb.append("*");
				sb.append(word.substring(i + 1));
				
				String str = sb.toString();
				if (similar.get(str) != null) {
					union(id, similar.get(str));
				} else {
					similar.put(str, id);
				}
			}
		}
		
		PriorityQueue<Node> pq = new PriorityQueue<>();
		for (int i = 0; i < idCount; i++) {
			if (parents[i] == i) { // 대표 검색어 인것만 찾앗
				pq.add(new Node(groupCount[i], idToString[i]));
			}
		}
		
		int count = 0;
		while (!pq.isEmpty() && count < 5) {
			Node now = pq.poll();
			mRet[count++] = now.str;
		}
		return count;
	}
	
	class Node implements Comparable<Node> {
		int groupCount;
		String str;
		public Node(int groupCount, String str) {
			super();
			this.groupCount = groupCount;
			this.str = str;
		}
		@Override
		public int compareTo(Node o) {
			if (this.groupCount != o.groupCount) return Integer.compare(o.groupCount, this.groupCount);
			return this.str.compareTo(o.str);
		}
	}
}