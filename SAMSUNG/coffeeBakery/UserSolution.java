package coffeeBakery;

import java.util.*;

//UserSolution.java

class UserSolution {
	
	private ArrayList<Node>[] graph;
	private int[] dist_from_coffee;
	private int[] dist_from_bakery;
	private boolean[] isSpecialBuilding;
	private final int INF = Integer.MAX_VALUE;
	
	public void init(int N, int K, int sBuilding[], int eBuilding[], int mDistance[]) {
		// 노드
		graph = new ArrayList[N];
		dist_from_coffee = new int[N];
		dist_from_bakery = new int[N];
		isSpecialBuilding = new boolean[N];
		for (int i = 0; i < N; i++) {
			graph[i] = new ArrayList<>();
		}
		// 도로
		for (int i = 0; i < K; i++) {
			// 양방향
			graph[sBuilding[i]].add(new Node(eBuilding[i], mDistance[i]));
			graph[eBuilding[i]].add(new Node(sBuilding[i], mDistance[i]));
		}
//		System.out.println("init completed");
//		return;
	}

	public void add(int sBuilding, int eBuilding, int mDistance) {
		graph[sBuilding].add(new Node(eBuilding, mDistance));
		graph[eBuilding].add(new Node(sBuilding, mDistance));
//		System.out.println("add completed");
//		return;
	}

	/**
	 * 최소값을 반환한다.
	 * @param M 커피점 수
	 * @param mCoffee 커피점 ID 배열
	 * @param P 제과점 수
	 * @param mBakery 제과점 ID 배열
	 * @param R 거리
	 * @return R 거리 이하 중 최소거리
	 */
	public int calculate(int M, int mCoffee[], int P, int mBakery[], int R) {
		
		Arrays.fill(dist_from_coffee, INF);
		Arrays.fill(dist_from_bakery, INF);
		Arrays.fill(isSpecialBuilding, false);
		
		for (int coffeeId : mCoffee) {
		    isSpecialBuilding[coffeeId] = true;
		}
		for (int bakeryId : mBakery) {
		    isSpecialBuilding[bakeryId] = true;
		}
		// int minSum = -1; // 또는 Integer.MAX_VALUE;
		int minSum = INF;
		
		PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
		
		for (int i = 0; i < M; i++) {
			// AI
			dist_from_coffee[mCoffee[i]] = 0;
			pq.add(new int[] {0, mCoffee[i]});
		}
		while(!pq.isEmpty()) {
			int[] current = pq.poll();
			int current_dist = current[0];
			int current_city = current[1];
			
			// ★★★ 핵심 최적화 ★★★
		    // 큐에서 꺼낸 경로가 이미 알려진 최단 경로보다 길다면 무시
		    if (current_dist > dist_from_coffee[current_city]) {
		        continue;
		    }
			
			for (Node n : graph[current_city]) {
				int new_dist = current_dist + n.distance;
		        
		        // 더 짧은 경로를 발견했다면 거리 갱신 및 큐에 추가
		        if (new_dist < dist_from_coffee[n.city]) {
		            dist_from_coffee[n.city] = new_dist;
		            pq.add(new int[]{new_dist, n.city});
//		            System.out.println("Coffee - startCity : " + current_city + ", endCity : " + n.city + ", new_dist : " + new_dist);
		        }
			}
		}
		for (int i = 0; i < P; i++) {
			// AI
			dist_from_bakery[mBakery[i]] = 0;
			pq.add(new int[] {0, mBakery[i]});
		}
		while(!pq.isEmpty()) {
			int[] current = pq.poll();
			int current_dist = current[0];
			int current_city = current[1];

			// 추가할 코드 1: 현재 경로가 R을 넘으면 더 볼 필요 없음
			if (current_dist > R) {
				continue;
			}
			// ★★★ 핵심 최적화 ★★★
		    // 큐에서 꺼낸 경로가 이미 알려진 최단 경로보다 길다면 무시
		    if (current_dist > dist_from_bakery[current_city]) {
		        continue;
		    }
			
			for (Node n : graph[current_city]) {
				int new_dist = current_dist + n.distance;

				// 추가할 코드 2: 새로 만들 경로가 R을 넘으면 큐에 넣을 필요 없음
				if (new_dist > R) {
					continue;
				}
		        
		        // 더 짧은 경로를 발견했다면 거리 갱신 및 큐에 추가
		        if (new_dist < dist_from_bakery[n.city]) {
		        	dist_from_bakery[n.city] = new_dist;
		            pq.add(new int[]{new_dist, n.city});
//		            System.out.println("Bakery - startCity : " + current_city + ", endCity : " + n.city + ", new_dist : " + new_dist);
		        }
			}
		}
		
		for (int i = 0; i < isSpecialBuilding.length; i++) {
	        // 조건: 주택이어야 하고, 두 곳 모두 R 거리 이내여야 함
	        if (!isSpecialBuilding[i] && dist_from_coffee[i] <= R && dist_from_bakery[i] <= R) {
	            
	            // 두 거리가 모두 MAX_VALUE가 아니어야 유효한 경로임 (경로가 없는 경우 방지)
	            if (dist_from_coffee[i] != Integer.MAX_VALUE && dist_from_bakery[i] != Integer.MAX_VALUE) {
	                int currentSum = dist_from_coffee[i] + dist_from_bakery[i];
	                // if (minSum == -1 || currentSum < minSum) {
					// // if (currentSum < minSum) {
	                //     minSum = currentSum;
	                // }
					minSum = Math.min(currentSum, minSum);
	            }
	        }
	    }
		return (minSum == INF) ? -1 : minSum;
	}
}

class Node implements Comparable<Node> {
	int city;
	int distance;
	
	public Node (int city, int distance) {
		this.city = city;
		this.distance = distance;
	}

	@Override
	public int compareTo(Node o) {
		return Integer.compare(this.distance, o.distance);
	}
}
