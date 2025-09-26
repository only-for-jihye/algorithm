package pro_edu.bfs.esa;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		int[] visitedCount = new int[N + 1];
		boolean[] visited = new boolean[N + 1];
		
		ArrayList<Integer>[] nodes = new ArrayList[N + 1];
		
		for (int i = 1; i <= N; i++) {
			nodes[i] = new ArrayList<>();
		}
		
		for (int i = 1; i <= M; i++) {
			st = new StringTokenizer(br.readLine());
			int start = Integer.parseInt(st.nextToken());
			int end = Integer.parseInt(st.nextToken());
			nodes[start].add(end);
			nodes[end].add(start);
		}
		
		st = new StringTokenizer(br.readLine());
		int R = Integer.parseInt(st.nextToken());
		int K = Integer.parseInt(st.nextToken());
		
		// bfs
		Queue<Integer> q = new LinkedList<>();
		q.add(R);
		visited[R] = true;
		visitedCount[R] = 0;
		
		while (!q.isEmpty()) {
			int current = q.poll();
			for (int next : nodes[current]) {
//				System.out.print(next + " ");
				if (visited[next]) continue;
				visited[next] = true;
				visitedCount[next] = visitedCount[current] + 1;
				q.add(next);
			}
		}
		int result = 0;
		for (int i = 1; i <= N; i++) {
			if (visited[i] && visitedCount[i] <= K) result++;
		}
		System.out.println(result);
	}
}
