package pro_edu.pq.gorv;

import java.io.*;
import java.util.*;

/*
 * 1. 각 City 정보 관리
 * 
 * 2. 도로 정보 관리
 * 
 * 우선 순위
 * 1. 이동시간 가장 큰
 * 2. 고유번호 작은
 * 
 * 고유번호로 도로를 찾아 이동 시간을 찾을 수 있어야함
 * 고유번호로 도시를 찾아 인구 수를 찾을 수 있어야함
 */
class UserSolution {

	// 도로 정보
//	Road[] roads;
	// 도시 정보
	City[] citys;
 
	/*
	 * ID를 base로 인구수 <- DAT[id] : 해당 id의 도시 인구수
	 * 
	 */
	public void init(int N, int[] mPopulation) {
//		roads = new Road[N];
		citys = new City[N];
		for (int i = 0; i < N; i++) {
			citys[i].id = i;
//			citys[i].population = mPopulation[i];
//			citys[i].time = citys[i].population;
//			if (i < N - 1) {
//				citys[i].time += citys[i + 1].population;
//			}
		}
//		for (int i = 0; i < N; i++) {
//			roads[i].id = i;
//			int time = citys[i].population;
//			if (i < N - 1) {
//				time += citys[i + 1].population;
//			}
//			roads[i].time = time;
//			roads[i].count = 1;
//		}
	}
	 
	// 도로 : 시간이 가장 오래 걸리는 도로 찾기
	// 호출 : 5000번
	// PriorityQueue로 관리하면 된다.
	// 우선 순위 = 도로를 이동하는 시간
	public int expand(int M) {
	    return 0;
	}
	
	/*
	 *  from에서 to까지 얼마나 걸리는지. 시간 확인 필요, id : 도로 정보
	 *  호출 2500번
	 *  from ~ to : 10,000개 ??
	 *  2500만번 ?
	 *  
	 */
	public int calculate(int mFrom, int mTo) {
		
	    return 0;
	}
	
	// from에서 to까지 선거구 결성
	// parametric search  ->  id를 base로 인구수를 확인
	// K개의 선거구 -> 가장 많은 선거구의 인구수 반환
	// K개 이상의 선거구 -> 가장 많은 선거구의 인구수 반환
	// -> 선거구 인구수를 가정 -> 검증 -> K개 이사으이 선거구가 가능한지 검증
	// 답 가능 구간 1~1000만 => log1000만 => 약 20번
	// 가정 횟수 * 검증 횟수 * 호출 = 6000만 반복
	public int divide(int mFrom, int mTo, int K) {
	    return 0;
	}
}

class City implements Comparable<City> {
	int id;
	int population;
	int k; // 선거구
	int time;
	int roadCnt;
	public City(int id, int population, int k, int time) {
		super();
		this.id = id;
		this.population = population;
		this.k = k;
		this.time = time;
		this.roadCnt = 1;
	}
	@Override
	public int compareTo(City o) {
		if (this.time < o.time) return -1;
		if (this.time > o.time) return 1;
		if (this.id < o.id) return -1;
		if (this.id > o.id) return 1;
		return 0;
	}
}

class Road implements Comparable<Road> {
	int id;
	int time;
	int count;
	public Road(int id, int time, int count) {
		super();
		this.id = id;
		this.time = time;
		this.count = count;
	}
	@Override
	public int compareTo(Road o) {
		if (this.time < o.time) return -1;
		if (this.time > o.time) return 1;
		return 0;
	}
}