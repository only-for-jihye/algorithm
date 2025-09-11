package coffeeBakery;

import java.util.*;

public class UserSolution_answer {
	int N;
	ArrayList<ArrayList<Node>> g = new ArrayList<>();
	int index = 0;

	private static final int INF = Integer.MAX_VALUE;
	private int[] costC, costB; // 커피/제과 최단거리
	private boolean[] isCoffee, isBakery; //
	private PriorityQueue<Node> pq = new PriorityQueue<>();

	public void init(int N, int K, int sBuilding[], int eBuilding[], int mDistance[]) {
		this.N = N;
		index = 0;

		g.clear();
		for (int i = 0; i < N; i++) {
			g.add(new ArrayList<>());
		}

		for (int i = 0; i < K; i++) {
			g.get(sBuilding[i]).add(new Node(eBuilding[i], mDistance[i]));
			g.get(eBuilding[i]).add(new Node(sBuilding[i], mDistance[i]));
			index++;
		}

		costC = new int[N];
		costB = new int[N];
		isCoffee = new boolean[N];
		isBakery = new boolean[N];
	}

	public void add(int sBuilding, int eBuilding, int mDistance) {
		g.get(sBuilding).add(new Node(eBuilding, mDistance));
		g.get(eBuilding).add(new Node(sBuilding, mDistance));
		index++;
	}

	public int calculate(int M, int[] mCoffee, int P, int[] mBakery, int R) {
		int result = INF;
		Arrays.fill(costC, INF);
		Arrays.fill(costB, INF);
		Arrays.fill(isCoffee, false);
		Arrays.fill(isBakery, false);
		pq.clear();

		// 커피점
		for (int i = 0; i < M; i++) {
			int start = mCoffee[i];
			isCoffee[start] = true;
			costC[start] = 0;
			pq.add(new Node(start, 0, 0)); // type 0 = 커피
		}
		// 제과점
		for (int i = 0; i < P; i++) {
			int start = mBakery[i];
			isBakery[start] = true;
			costB[start] = 0;
			pq.add(new Node(start, 0, 1)); // type 1 = 제과
		}

		while (!pq.isEmpty()) {
			Node cur = pq.poll();
			int cost = cur.cost;
			int type = cur.type;
			
			
			if (cost > R) {
				continue;
			}

			if (!isCoffee[cur.to] && !isBakery[cur.to]) { // 주택만
				if (costC[cur.to] <= R && costB[cur.to] <= R) {
					result = Math.min(result, costC[cur.to] + costB[cur.to]);
				}
			}

			// PQ 최소값이 이미 result 이상이면 break(Kick!!)
			if (result != INF && !pq.isEmpty() && pq.peek().cost >= result) {
				break;
			}

			for (Node next : g.get(cur.to)) {
				int newCost = cost + next.cost;
				if (newCost > R) {
					continue;
				}
				if (type == 0) { // 커피
					if (newCost < costC[next.to]) {
						costC[next.to] = newCost;
						pq.add(new Node(next.to, newCost, 0));
					}
				} else { // 제과
					if (newCost < costB[next.to]) {
						costB[next.to] = newCost;
						pq.add(new Node(next.to, newCost, 1));
					}
				}
			}
		}

		return (result == INF) ? -1 : result;
	}

	static class Node implements Comparable<Node> {
		int to;
		int cost;
		int type; // type 0=커피, 1=제과

		Node(int to, int cost) {
			this.to = to;
			this.cost = cost;
		}

		Node(int to, int cost, int type) {
			this.to = to;
			this.cost = cost;
			this.type = type;
		}

		@Override
		public int compareTo(Node o) {
			return this.cost - o.cost;
		}
	}
}