package Bacteria;

import java.io.*;
import java.util.*;

// Time Flow + segment tree

class UserSolution_Teaching {
	
	class Bact implements Comparable <Bact>{
		int data;
		int id;
		Bact(int data, int id) {
			this.data = data;
			this.id = id;
		}
		@Override
		public int compareTo(Bact o) {
			if(data < o.data) return -1;
			if(data > o.data) return 1;
          	if(id < o.id) return -1;
          	if(id > o.id) return 1; 
			return 0;
		}
	}
	
	// index : ID (1~30,000) -> add call count
	// value : 이 id 박테리아가 절반이 되기까지 걸리는 시간
	int[] halfTime;
	// value : 이 id 박테리아의 생명 
	int[] lifeSpan;
	
	// segment tree 
	int[] tree;
	
	// TreeMap => key : Bact(data -> life순으로 관리)
	TreeSet<Bact>ts;
	// Bact(data -> time순으로 관리)
	PriorityQueue<Bact>pq; 
	
	int updateTree(int start, int end, int node, int idx, int val) {
	    if (idx < start || idx > end)
	        return tree[node];
	    if (start == end) 
	        return tree[node] += val;
	    int mid = (start + end) / 2;
	    int leftVal = updateTree(start, mid, node * 2, idx, val);
	    int rightVal = updateTree(mid + 1, end, node * 2 + 1, idx, val);
	    return tree[node] = leftVal + rightVal; 
	}
	
	int query(int start, int end, int node, int left, int right) {
	    if (left > end || right < start)
	        return 0; 
	    if (left <= start && right >= end)
	        return tree[node];
	    int mid = (start + end) / 2;
	    int leftVal = query(start, mid, node * 2, left, right);
	    int rightVal = query(mid + 1, end, node * 2 + 1, left, right);
	    return leftVal + rightVal;
	}
	
	// sim -> time flow
	void update(int t) {
		// 이 시간에 절반이 되고 멸종해야하는 박테리아 모두 처리
	    while (!pq.isEmpty() && pq.peek().data <= t) {
	        Bact now = pq.remove();
	        ts.remove(new Bact(lifeSpan[now.id], now.id));
	        // lifeSpan[now.id]의 생명을 가진 박테리아 하나 삭제
	        updateTree(1, 1000000, 1, lifeSpan[now.id], -1);
	        // life가 절반이 된다.
	        lifeSpan[now.id] /= 2; 
	        // 사라지지 않는다면 -> 갱신
	        if (lifeSpan[now.id] > 99) {
	        	pq.add(new Bact(now.data + halfTime[now.id], now.id));
	        	ts.add(new Bact(lifeSpan[now.id], now.id));
	        	// lifeSpan[now.id]의 생명을 가진 박테리아 하나 update
	            updateTree(1, 1000000, 1, lifeSpan[now.id], 1);
	        }
	    }
	}

	public void init() {
		halfTime = new int[30001];
		lifeSpan = new int[30001];
		tree = new int[1000001 * 4];  
		ts = new TreeSet<>();
		pq = new PriorityQueue<>();
	    return;
	}
	 
	// 30,000 x (log(30,000) + + log(30,000) + log(4000,000) + @update)
	public void addBacteria(int tStamp, int mID, int mLifeSpan, int mHalfTime){
		update(tStamp); 
		halfTime[mID] = mHalfTime;
		lifeSpan[mID] = mLifeSpan; 
		ts.add(new Bact(lifeSpan[mID], mID));
		pq.add(new Bact(halfTime[mID] + tStamp, mID));
	    updateTree(1, 1000000, 1, mLifeSpan, 1); 
	    return;
	}
	 
	// 1,000 x(O(30,000) + @update) 
	public int getMinLifeSpan(int tStamp) {
		update(tStamp);
		if(ts.size() == 0)
			return -1; 
	    return ts.first().id;
	}
	 
	// 15,000 x log(1,000,000)
	// subset : TLE -> 15,000 x (log(30,000) + 1,000,000)
	public int getCount(int tStamp, int mMinSpan, int mMaxSpan) {
		update(tStamp);
		// submap/subset construction : O(1) => retrieval O(logN)
		// size() => O(N) for TreeMap
		// return ts.subSet(new Bact(mMinSpan, -1), true, new Bact(mMaxSpan, -1), true).size();
		return query(1, 1000000, 1, mMinSpan, mMaxSpan);
	}
}