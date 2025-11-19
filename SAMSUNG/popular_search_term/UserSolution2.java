package popular_search_term;

//UserSolution.java
import java.io.*;
import java.util.*;

//인기검색어
//@admin_deukwha
//hashing + union find

class UserSolution2 {
	
	class Node implements Comparable <Node> {
		int cnt;
		String str;
		Node(int cnt, String str) {
			this.cnt = cnt;
			this.str = str; 
		}
		@Override
		public int compareTo(Node o) {
			if(cnt > o.cnt) return -1; 
			if(cnt < o.cnt) return 1;
			return str.compareTo(o.str);
		}
	}
	
	ArrayDeque<String>dq;
	int n; 
	int[] parent;
	int[] groupCnt;
	int[] wordCnt;
	int idCnt;
	String[] idToString; 
	
	int find(int n) {
		if(parent[n] == n)
			return n;
		return parent[n] = find(parent[n]); 
	}
	
	void union(int a, int b) {
		int pa = find(a);
		int pb = find(b);
		
		if(pa == pb)
			return; 
		
		int winner = -1;
		int loser = -1;
		
		if(wordCnt[pa] > wordCnt[pb]) {
			winner = pa;
			loser = pb; 
		}
		else if(wordCnt[pa] < wordCnt[pb]) {
			winner = pb;
			loser = pa;
		}
		else {
			if(idToString[pa].compareTo(idToString[pb]) < 0) {
				winner = pa;
				loser = pb;
			}
			else {
				winner = pb;
				loser = pa; 
			}
		}
		parent[loser] = winner;
		groupCnt[winner] += groupCnt[loser]; 
	}

	public void init(int N) {
		dq = new ArrayDeque<>();
		n = N; // max. 500
		parent = new int[501];
		groupCnt = new int[501]; 
		wordCnt = new int[501];
		idToString = new String[501]; 
		return; 
	}

	// 10,000 x O(1)
	public void addKeyword(String mKeyword) {
		dq.add(mKeyword);
		if(dq.size() > n)
			dq.removeFirst(); 
		return; 
	}
	
	// 100 x 
	public int top5Keyword(String mRet[]) {
	
		HashMap<String, Integer>hm = new HashMap<>(); // for id sequencing
		HashMap<String, Integer>similars = new HashMap<>(); // key : hashed string, value : id
		
		// init O(500)
		for(int i = 0; i < dq.size(); i++) {
			parent[i] = i;
			groupCnt[i] = 0;
			wordCnt[i] = 0;
		}
		
		// id seq O(500)
		idCnt = 0; 
		for(String word : dq) {
			if(hm.get(word) == null) 
				hm.put(word, idCnt++);
			int id = hm.get(word);
			wordCnt[id]++;
			idToString[id] = word; 
			groupCnt[id]++;
		}
		
		// hash O(500)
		for(String w : dq) {
			int id = hm.get(w);
				for(int j = 0; j < w.length(); j++) {
                

                StringBuilder sb = new StringBuilder(); // 한 문자마다 censor 진행
                sb.append(w.substring(0, j)); 
                sb.append("*");
                sb.append(w.substring(j+1)); 
                
                String str = sb.toString(); 
					if(similars.get(str) != null)
						union(id, similars.get(str));
					else 
						similars.put(str, id);
				
			}
		}
		
		PriorityQueue<Node>pq = new PriorityQueue<>();
		for(int i = 0; i < idCnt; i++) 
			if(parent[i] == i)
				pq.add(new Node(groupCnt[i], idToString[i]));
		
		int cnt = 0; 
		while(!pq.isEmpty() && cnt < 5) 
			mRet[cnt++] = pq.remove().str; 
		return cnt;
	}
}