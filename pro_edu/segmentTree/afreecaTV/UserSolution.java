package pro_edu.segmentTree.afreecaTV;

import java.io.*;
import java.util.*;

class UserSolution {
	
	int N;
	int[] org;
	int[] tree;
	int[] sumTree;
	int[] maxTree;
	int[] minTree;
	
	private void build(int node, int start, int end) {
		if (start == end) {
			tree[node] = org[start];
			return;
		}
		
		int mid = (start + end) / 2;
		build(node, start, mid);
		build(node, mid + 1, end);
		tree[node] = merge(tree[node * 2], tree[node * 2 + 1]);
	}
	
	private int merge(int left, int right) {
		return left + right;
	}
	private int min(int left, int right) {
		return Math.min(left, right);
	}
	private int max(int left, int right) {
		return Math.max(left, right);
	}
	
	public void init(int N, int[] mSubscriber) {
		this.N = N;
		org = new int[N];
		tree = new int[N * 4];
		for (int i = 0; i < N; i++) {
			org[i] = mSubscriber[i];
		}
		build(1, 0, N - 1);
	}
	 
	public int subscribe(int mId, int mNum) {
		update(1, 0, N - 1, mId, mNum);
		return org[mId]; 
	}
	
	private void update(int node, int start, int end, int index, int diff) {
		if (end < index || index < start) return;
		if (start == index && end == index) {
			org[index] = diff;
			tree[node] = diff;
			return;
		}
		int mid = (start + end) / 2;
		update(node, start, mid, index, diff);
		update(node, mid + 1, end, index, diff);
		tree[node] = merge(tree[node * 2], tree[node * 2 + 1]);
	}
	
	 
	public int unsubscribe(int mId, int mNum) {
		update(1, 0, N - 1, mId, mNum);
		return org[mId]; 
	}
	 
	public int count(int sId, int eId) {
		return query(1, 0, N - 1, sId, eId, 1);
	}
	
	private int query(int node, int start, int end, int S, int E, int what) {
		if (S <= start && end <= E) {
			return tree[node];
		}
		if (end < S || E < start) {
			return 0;
		}
		int mid = (start + end) / 2;
		int left = query(node, start, mid, S, E, what);
		int right = query(node, mid + 1, end, S, E, what);
		if (what == 1) {
			return merge(left, right);
		} else if (what == 2) {
			return min(left, right);
		} else {
			return max(left, right);
		}
	}
	
	public int calculate(int sId, int eId) {
		return query(1, 0, N - 1, sId, eId, 2) - query(1, 0, N - 1, sId, eId, 3);
	}
}