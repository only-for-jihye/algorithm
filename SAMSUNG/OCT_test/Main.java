package OCT_test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
	
	static int[][] arr;
	static int N;
	static boolean[] visited;
    static int minCost;
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
	
		int T = Integer.parseInt(st.nextToken());
		
		for (int t = 1; t <= T; t++) {
			st = new StringTokenizer(br.readLine());
			N = Integer.parseInt(st.nextToken());
			arr = new int[N + 1][N + 1];
			for (int i = 1; i <= N; i++) {
				st = new StringTokenizer(br.readLine());
				for (int j = 1; j <= N; j++) {
					arr[i][j] = Integer.parseInt(st.nextToken());
				}
			}
			int answer = calc();
			System.out.println("#" + t + " " + answer);
		}
	}

    private static int calc() {
        if (N == 1) return 0;

        minCost = Integer.MAX_VALUE;
        visited = new boolean[N + 1];

        visited[1] = true;
        dfs(1, 0, 1);

        return (minCost != Integer.MAX_VALUE) ? minCost : -1;
    }

    private static void dfs(int now, int cost, int count) {
        if (cost >= minCost) return;

        if (count == N) {
            if (arr[now][1] != 0) {
                minCost = Math.min(minCost, cost + arr[now][1]);
            }
            return;
        }

        for (int i = 1; i <= N; i++) {
            if (!visited[i] && arr[now][i] != 0) {
                visited[i] = true;
                dfs(i, cost + arr[now][i], count + 1);
                visited[i] = false;
            }
        }
    }
	
//	private static int calc() {
//		PriorityQueue<Edge> pq = new PriorityQueue<>();
//		pq.add(new Edge(1, 0));
//		int[] dist = new int[N + 1];
//		Arrays.fill(dist, Integer.MAX_VALUE);
//		dist[1] = 0;
//
//		while (!pq.isEmpty()) {
//			Edge now = pq.poll();
//			if (now.dest == N) return now.cost;
//			
//			for (int i = 1; i <= N; i++) {
//				if (now.cost + arr[now.dest][i] < dist[i]) {
//					dist[i] = now.cost + arr[now.dest][i];
//					pq.add(new Edge(i, dist[i]));
//				}
//			}
//		}
//		
//		return dist[N];
//	}
	
}

//class Edge implements Comparable<Edge> {
//	int dest;
//	int cost;
//	public Edge(int dest, int cost) {
//		super();
//		this.dest = dest;
//		this.cost = cost;
//	}
//	@Override
//	public int compareTo(Edge o) {
//		return Integer.compare(this.cost, o.cost);
//	}
//}
