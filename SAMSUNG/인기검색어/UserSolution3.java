package 인기검색어;

import java.io.*;
import java.util.*;

class UserSolution3 {
	
	class Node implements Comparable<Node> {
		int count;
		String str;
		public Node(int count, String str) {
			super();
			this.count = count;
			this.str = str;
		}
		@Override
		public int compareTo(Node o) {
			if (this.count != o.count) return Integer.compare(o.count, this.count);
			return this.str.compareTo(o.str);
		}
	}
	
	ArrayDeque<String> dq;
	int N;
	int[] parents;
	int[] groupCount;
	int[] wordCount;
	int idCount;
	String[] idToString;
	
	// union-find
	int find (int n) {
		if (parents[n] == n) return n;
		return parents[n] = find(parents[n]);
	}
	
	void union (int a, int b) {
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
	
	public void init(int N) {
		dq = new ArrayDeque<>();
		this.N = N;
		parents = new int[501];
		wordCount = new int[501];
		groupCount = new int[501];
		idToString = new String[501];
	}
  
	// 최대 10,000번
	public void addKeyword(String mKeyword) {
		dq.addLast(mKeyword);
		if (dq.size() > N) {
			dq.removeFirst(); // dq 사이즈가 다 차면 처음 것을 삭제
		}
	}
	
	// 최대 100번
	public int top5Keyword(String mRet[]) {
		
		HashMap<String, Integer> hm = new HashMap<>();
		HashMap<String, Integer> similar = new HashMap<>();
		
		// 초기화하는 이유, add 할때마다 새로 계산해야되기 때문
		for (int i = 0; i < dq.size(); i++) {
			parents[i] = i;
			groupCount[i] = 0;
			wordCount[i] = 0;
		}
		
		// id seq 처리
		idCount = 0;
		for (String word : dq) {
			if (hm.get(word) == null) {
				hm.put(word, idCount++);
			}
			int id = hm.get(word);
			wordCount[id]++;
			idToString[id] = word;
			groupCount[id]++;
		}
		
		// hash 처리
		for (String word : dq) {
			int id = hm.get(word);
			for (int i = 0; i < word.length(); i++) {
				StringBuilder sb = new StringBuilder();
				// 한 문자씩 *로 바꿈
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
			if (parents[i] == i) {
				pq.add(new Node(groupCount[i], idToString[i]));
			}
		}
		
		int count = 0;
		while (!pq.isEmpty() && count < 5) {
			Node current = pq.poll();
			mRet[count++] = current.str;
		}
		
		return count;
	}
	
}