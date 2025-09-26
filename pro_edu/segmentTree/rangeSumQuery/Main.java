package pro_edu.segmentTree.rangeSumQuery;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

	static long[] org;
	static long[] tree;
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken());
		int Q = Integer.parseInt(st.nextToken());
		
		org = new long[N];
		tree = new long[N * 4];
		
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < N; i++) {
			org[i] = Integer.parseInt(st.nextToken());
		}
		
		build(1, 0, org.length - 1);
		
		for (int i = 0; i < Q; i++) {
			st = new StringTokenizer(br.readLine());
			int cmd = Integer.parseInt(st.nextToken());
			int start = Integer.parseInt(st.nextToken());
			int end = Integer.parseInt(st.nextToken());
			if (cmd == 1) {
				update(1, 0, org.length - 1, start - 1, end);
			} else if (cmd == 2) {
				long answer = query(1, 0, org.length - 1, start - 1, end - 1);
				System.out.println(answer);
			}
		}
	}

	private static void build(int node, int start, int end) {
		// 기저 조건
		if (start == end) {
			tree[node] = org[start];
			return;
		}
		
		int mid = (start + end) / 2;
		build(node * 2, start, mid);
		build(node * 2 + 1, mid + 1, end);
		tree[node] = merge(tree[node * 2], tree[node * 2 + 1]);
	}
	
	private static long merge(long left, long right) {
//		return left + right;
		return Math.min(left, right);
	}

	private static long query(int node, int start, int end, int S, int E) {
		if (S <= start && end <= E) {
			return tree[node];
		}
		if (end < S || E < start) {
//			return 0;
			return Integer.MAX_VALUE;
		}
		int mid = (start + end) / 2;
		long left = query(node * 2, start, mid, S, E);
		long right = query(node * 2 + 1, mid + 1, end, S, E);
		return merge(left, right);
	}

	private static void update(int node, int start, int end, int idx, int diff) {
		if (end < idx || idx < start) {
			return;
		}
		if (start == idx && end == idx) {
			tree[node] = diff;
			return;
		}
		int mid = (start + end) / 2;
		update(node * 2, start, mid, idx ,diff);
		update(node * 2 + 1, mid + 1, end, idx ,diff);
		tree[node] = merge(tree[node * 2], tree[node * 2 + 1]);
	}
	
}
