package 박테리아;

import java.util.*;

class UserSolution {
	
	class Bacteria implements Comparable<Bacteria> {
		int life;
		int id;
		public Bacteria(int life, int id) {
			super();
			this.life = life;
			this.id = id;
		}
		@Override
		public int compareTo(Bacteria o) {
			if (this.life != o.life) return Integer.compare(this.life, o.life);
			return Integer.compare(this.id, o.id);
		}
	}
	
	// 박테리아 수명 절반 주기
	int[] lifeHalfTime;
	// 박테리아 수명
	int[] lifeSpan;
	// 박테리아 보관소
	TreeSet<Bacteria> ts;
	// 박테리아 시간별 관리
	PriorityQueue<Bacteria> pq;
	// getCount 용도 SegmentTree
	int[] tree;
	
	int updateTree (int start, int end, int node, int idx, int val) {
		if (idx < start || idx > end) {
			return tree[node];
		}
		if (start == end) {
			return tree[node] += val;
		}
		int mid = (start + end) / 2;
		int leftVal = updateTree(start, mid, node * 2, idx, val);
		int rightVal = updateTree(mid + 1, end, node * 2 + 1, idx, val);
		return tree[node] = leftVal + rightVal;
	}
	
	int query (int start, int end, int node, int left, int right) {
		if (left > end || right < start) {
			return 0;
		}
		if (left <= start && right >= end) {
			return tree[node];
		}
		int mid = (start + end) / 2;
		int leftVal = query (start, mid, node * 2, left, right);
		int rightVal = query (mid + 1, end, node * 2 + 1, left, right);
		return leftVal + rightVal;
	}
	
	void update(int tStamp) {
		while (!pq.isEmpty() && pq.peek().life <= tStamp) {
			Bacteria now = pq.remove();
			// 박테리아 폐기
			ts.remove(new Bacteria(lifeSpan[now.id], now.id));
			// 박테리아 개수 감소
			updateTree(1, 1000000, 1, lifeSpan[now.id], -1);
			// 박테리아 수명 절반
			lifeSpan[now.id] /= 2;
			// 박테리아가 죽지 않으면 TreeSet 갱신
			if (lifeSpan[now.id] > 99) {
				pq.add(new Bacteria(now.life + lifeHalfTime[now.id], now.id));
				ts.add(new Bacteria(lifeSpan[now.id], now.id));
				// 박테리아 추가
				updateTree(1, 1000000, 1, lifeSpan[now.id], 1);
			}
		}
	}

	public void init() {
		lifeHalfTime = new int[30001];
		lifeSpan = new int[30001];
		ts = new TreeSet<>();
		pq = new PriorityQueue<>();
		tree = new int[1000001 * 4];
	}
	 
	public void addBacteria(int tStamp, int mID, int mLifeSpan, int mHalfTime) {
		update(tStamp);
		lifeSpan[mID] = mLifeSpan;
		lifeHalfTime[mID] = mHalfTime;
		ts.add(new Bacteria(lifeSpan[mID], mID));
		pq.add(new Bacteria(lifeHalfTime[mID] + tStamp, mID));
		updateTree(1, 1000000, 1, mLifeSpan, 1);
	}
	 
	public int getMinLifeSpan(int tStamp) {
		update(tStamp);
		if (ts.size() == 0) {
			return -1;
		}
	    return ts.first().id;
	}
	
	// getCount에서 TimeLimit이 되지 않으려면 .. SegmentTree를 사용해야 한다.
	public int getCount(int tStamp, int mMinSpan, int mMaxSpan) {
		update(tStamp);
		return query(1, 1000000, 1, mMinSpan, mMaxSpan);
	}
	
}
