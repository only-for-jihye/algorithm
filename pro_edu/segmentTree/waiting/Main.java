package pro_edu.segmentTree.waiting;

import java.io.*;
import java.util.*;

class Main {

	static int height[];
	static int tree[];
	
	static int merge(int left, int right) {
		return left + right;
	}
	
	static void build(int node, int st, int en) {
		if(st == en) {
			tree[node] = 1;
			return;
		}
		
		int mid = (st+en)/2;
		build(node * 2,  	st, 	mid);
		build(node * 2+1, 	mid+1,	en );
		
		tree[node] = merge(tree[node*2], tree[node*2 + 1]);
	}
	
	// value번째 사람이 어느 index에 있는가?
	static int query(int node, int st, int en,  int value) {
		if(st == en) {
			return st;
		}
		
		int leftValue = tree[node * 2]; // 왼쪽 자식쪽에는 몇 명이나 있는가?
		
		int mid = (st + en) / 2;

		// 왼쪽이 우리가 원하는 인원수보다 많다 
		//=> 왼쪽 어딘가에 내가 원하는 번째가 있다. 
		int idx = -1;
		if(value < leftValue)
			idx 	= query(node*2,		st,		mid,	value);
		else
			idx 	= query(node*2+1,	mid+1,	en,		value - leftValue);
		
		return idx;
	}
	
	static void update(int node, int st, int en,  int idx, int diff) {
		if(en < idx || idx < st) return;
		if(st == idx && idx == en) {
			tree[node] = diff;
			return;
		}
		
		int mid = (st + en) / 2;
		update(node * 2,		st,		mid,	idx, diff);//왼쪽
		update(node * 2 + 1,	mid+1,	en,		idx, diff);//오른쪽
		tree[node] = merge(tree[node*2], tree[node*2+1]);
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int N = Integer.parseInt(br.readLine());
		height = new int[N];
		tree = new int[N * 4];
		for(int i = 0; i < N; i++)
			height[i] = Integer.parseInt(br.readLine());
		int S[] = new int[N];
		StringTokenizer st = new StringTokenizer(br.readLine());
		for(int i = 0; i < N; i++)
			S[i] = Integer.parseInt(st.nextToken());
		
		// 1. 키가 작은 순서~큰 순서
		Arrays.sort(height);
		
		// 2. segment tree <- 각 킥의 사람이 아직 남았는가? <- 초기화(모든 키에 사람이 1명씩 있음)
		build(1, 0, N-1);
		
		// 맨 뒤에 사람부터 어느 키인지 찾기
		ArrayDeque<Integer> ans = new ArrayDeque<>();
		for(int i = N-1; i >= 0; i--) {
			int s = S[i];
			int idx = query(1, 0, N-1, s);
			update(1, 0, N-1, idx, 0);
			ans.addFirst(height[idx]);
		}
		
		for(int h : ans) {
			System.out.println(h);
		}
	}
}