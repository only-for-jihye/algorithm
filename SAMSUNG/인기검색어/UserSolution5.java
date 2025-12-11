package 인기검색어;

import java.io.*;
import java.util.*;

class UserSolution5 {
	
	int[] parents;
	int[] wordCount;
	int[] groupCount;
	String[] idToString;
	
	int find(int n) {
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
	
	int N;
	ArrayDeque<String> dq;

	public void init(int N) {
		this.N = N;
		this.dq = new ArrayDeque<>();
		this.parents = new int[N];
		this.wordCount = new int[N];
		this.groupCount = new int[N];
		this.idToString = new String[N];
		return; 
	}
  
	public void addKeyword(String mKeyword) {
		dq.add(mKeyword);
		if (dq.size() > N) {
			dq.removeFirst();
		}
		return; 
	}
	
	public int top5Keyword(String mRet[]) {
		// 대표 검색어 선정 위한 변수 선언
		Map<String, Integer> hm = new HashMap<>();
		Map<String, Integer> similar = new HashMap<>();
		
		for (int i = 0; i < dq.size(); i++) {
			parents[i] = i;
			wordCount[i] = 0;
			groupCount[i] = 0;
		}
		int idCount = 0;
		for (String word : dq) {
			if (hm.get(word) == null) {
				hm.put(word, idCount++);
			}
			int id = hm.get(word);
			wordCount[id]++;
			groupCount[id]++;
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
				if (similar.get(str) == null) {
					similar.put(str, id);
				} else {
					union(id, similar.get(str));
				}
			}
		}
		
		// 인기검색어
		PriorityQueue<Node> pq = new PriorityQueue<>();
		for (int i = 0; i < idCount; i++) {
			if (parents[i] == i) {
				pq.add(new Node(groupCount[i], idToString[i]));
			}
		}
		int count = 0;
		while (!pq.isEmpty() && count < 5) {
			mRet[count++] = pq.poll().str;
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
