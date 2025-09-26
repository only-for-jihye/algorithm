package pro_edu.bfs.hyperTube;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {

	/*
	 * 1. 방법 : 차수 -> 이동 횟수
	 * 	if (node 번호 > 100,000)
	 *		visited[next] = visited[now]
	 * 	else
	 * 		visited[next] = visited[now] + 1
	 * 
	 * 2. 방법 : 어차피 하이퍼튜브 -> 역, 세트로 묶어서 처리
	 * 	visited[next] = visited[now] + 1 통일
	 *  마지막 출력에서만 visited[도착지] / 2
	 * 
	 * 시작점
	 * 하이퍼튜브
	 * 경유지
	 * 하이퍼튜브
	 * 경유지
	 * 하이퍼튜브
	 * ... 반복
	 */
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int N = Integer.parseInt(st.nextToken());
		int K = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		int[] visitedCount = new int[120001];
		boolean[] visited = new boolean[120001];
		
		// 기본 배열
		ArrayList<Integer>[] station = new ArrayList[120001];
		
		for (int i = 1; i <= 120000; i++) {
			station[i] = new ArrayList<>();
		}
		
		int hyperId = 100000;
		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			hyperId++;
			for (int j = 0; j < K; j++) {
				int station1 = Integer.parseInt(st.nextToken());
				station[hyperId].add(station1);
				station[station1].add(hyperId);
			}
		}
		
		ArrayDeque<Integer> q = new ArrayDeque<>();
		q.addLast(1);
		visited[1] = true;
		visitedCount[1] = 1;
		boolean departed = false;
		while (!q.isEmpty()) {
			int current = q.pollFirst();
			
			if (current == N) {
				departed = true;
				break;
			}
			
			for (int next : station[current]) {
				if (visited[next]) continue;
				visited[next] = true;
				q.add(next);
				if (next > 100000) {
					visitedCount[next] = visitedCount[current];
				} else {
					visitedCount[next] = visitedCount[current] + 1;
				}
			}
		}
		if (departed) {
			System.out.println(visitedCount[N]);
		} else {
			System.out.println(-1);
		}
	}
}
