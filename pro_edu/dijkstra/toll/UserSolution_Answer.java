package pro_edu.dijkstra.toll;

import java.io.*;
import java.util.*;

/*
 * mId로 도로의 정보를 조회할 수 있어야 한다.
 * 다만 mId는 1~10억까지 가능하다 -> HashMap
 * 
 * 출발 도시와 도착 도시로 이어져야 한다.
 * 이어지는 도로의 정보 id도 관리해야 한다.
 */
class UserSolution_Answer {
	
	HashMap<Integer, Road> map;
	ArrayList<Road> arr[];
	int N;
	
	public void init(int N, int K, int[] mId, int[] sCity, int[] eCity, int mToll[]) {
		this.N = N;
		map = new HashMap<>();
		arr = new ArrayList[N];
		for (int i = 0; i < N; i++) {
			arr[i] = new ArrayList<>();
		}
//		for (int i = 0; i < K; i++) {
//			int id = mId[i];
//			int start = sCity[i];
//			int end = eCity[i];
//			int cost = mToll[i];
//			Road road = new Road(id, start, end, cost);
//			map.put(id, road);
//			arr[start].add(road);
//		}
		for (int i = 0; i < K; i++) {
			add(mId[i], sCity[i], eCity[i], mToll[i]);
		}
	}

	public void add(int mId, int sCity, int eCity, int mToll) {
		Road road = new Road(mId, sCity, eCity, mToll);
		arr[sCity].add(road);
		map.put(mId, road);
	}

	public void remove(int mId) {
//		int sCity = map.get(mId).sCity;
//		int eCity = map.get(mId).eCity;
//
//		map.remove(mId);
//		for (int i = 0; i < arr[sCity].size(); i++) {
//			if (arr[sCity].get(i).eCity == eCity) {
////				arr[sCity].remove(i);
//				arr[sCity].get(i).isDeleted = true;
//			}
//			
//		}
		Road road = map.get(mId);
		map.remove(mId);
		road.isDeleted = true;
	}

	public int cost(int M, int sCity, int eCity) {
		
//		PriorityQueue<Element> pq = new PriorityQueue<>();
//		int cost = map[sCity][];
//		pq.add(new Element(eCity, 0, 0));
//		
//		int[][] dist = new int[N][M + 1];
//		for (int i = 0; i < N; i++) {
//			for (int j = 0; j < M + 1; j++) {
//				dist[i][j] = Integer.MAX_VALUE;
//			}
//		}
//		while (!pq.isEmpty()) {
//			Element now = pq.poll();
//			if (now.dest == eCity) break;
//			for (Road road : arr[now.dest]) {
//				int nextCity = road.eCity;
//				int nextCost = road.cost + now.cost;
//				if (dist[nextCity][0] <= nextCost) continue;
//				dist[nextCity][0] = nextCost;
//				pq.add(new Element());
//			}
//		}
//		
		int[][] dist = new int[N][M + 1];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M + 1; j++) {
				dist[i][j] = Integer.MAX_VALUE;
			}
		}
		PriorityQueue<Element> pq = new PriorityQueue<>();
		for (int i = 0; i < M + 1; i++) {
			dist[sCity][i] = 0; // 초기 비용 세팅
		}
		pq.add(new Element(sCity, 0, 0));
		while (!pq.isEmpty()) {
			Element now = pq.poll();
			// 성능 높이기
			if (dist[now.dest][now.count] < now.cost) continue;
			if (now.dest == eCity) return now.cost; 
			
			for (Road road : arr[now.dest]) {
				if (road.isDeleted) continue;
				// 1. 할인권 사용
				if (now.count < M &&
						now.cost + (road.cost / 2) < dist[road.eCity][now.count + 1]) {
					dist[road.eCity][now.count + 1] = now.cost + (road.cost / 2);
					pq.add(new Element(road.eCity, dist[road.eCity][now.count + 1], now.count + 1));
				}
				
				// 2. 할인권 미사용
				if (now.cost + road.cost < dist[road.eCity][now.count]) {
					dist[road.eCity][now.count] = now.cost + road.cost;
					pq.add(new Element(road.eCity, dist[road.eCity][now.count], now.count));
				}
			}
		}
		
		return -1;
	}
}

class Element implements Comparable<Element> {
	int dest;
	int cost;
	int count;
	public Element(int dest, int cost, int count) {
		super();
		this.dest = dest;
		this.cost = cost;
		this.count = count;
	}
	@Override
	public int compareTo(Element o) {
		return Integer.compare(this.cost, o.cost);
	}
}

class Road implements Comparable<Road> {
	int id;
	int sCity;
	int eCity;
	int cost;
	boolean isDeleted;
	
	public Road(int id, int sCity, int eCity, int cost) {
		this.id = id;
		this.sCity = sCity;
		this.eCity = eCity;
		this.cost = cost;
		isDeleted = false;
	}
	@Override
	public int compareTo(Road o) {
		return Integer.compare(this.cost, o.cost);
	}
}