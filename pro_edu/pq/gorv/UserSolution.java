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
	Road[] roads;
	// 시간이 가장 오래 걸리는 도로 
	PriorityQueue<Road> pq;
	int[] populations;
 
	/*
	 * ID를 base로 인구수 <- DAT[id] : 해당 id의 도시 인구수
	 * 
	 */
	public void init(int N, int[] mPopulation) {
		roads = new Road[N];
		pq = new PriorityQueue<Road>();
		populations = new int[N];
		for (int i = 0; i < N; i++) {
			populations[i] = mPopulation[i];
		}
		for (int i = 0; i < N; i++) {
			roads[i] = new Road(i);
			pq.add(roads[i]);
		}
	}
	 
	// 도로 : 시간이 가장 오래 걸리는 도로 찾기
	// 호출 : 5000번
	// PriorityQueue로 관리하면 된다.
	// 우선 순위 = 도로를 이동하는 시간
	public int expand(int M) {
		int ret = 0;
		for (int i = 0; i < M; i++) {
			Road now = pq.poll();
			now.expandRoad();
			pq.add(now);
		}
	    return ret;
	}
	
	/*
	 *  from에서 to까지 얼마나 걸리는지. 시간 확인 필요, id : 도로 정보
	 *  호출 2500번
	 *  from ~ to : 10,000개 ??
	 *  2500만번 ?
	 *  
	 */
	public int calculate(int mFrom, int mTo) {
		int ret = 0;
		for (int i = Math.min(mTo, mFrom); i < Math.max(mTo, mFrom); i++) {
			ret += roads[i].time;
		}
	    return ret;
	}
	
	// from에서 to까지 선거구 결성
	// parametric search  ->  id를 base로 인구수를 확인
	// K개의 선거구 -> 가장 많은 선거구의 인구수 반환
	// K개 이상의 선거구 -> 가장 많은 선거구의 인구수 반환
	// -> 선거구 인구수를 가정 -> 검증 -> K개 이사으이 선거구가 가능한지 검증
	// 답 가능 구간 1~1000만 => log1000만 => 약 20번
	// 가정 횟수 * 검증 횟수 * 호출 = 6000만 반복
	public int divide(int mFrom, int mTo, int K) {
		
		int left = 1;
		int right = 1000*10000;
		int ret = 0;
		while (left < right) {
			int mid = (left + right) / 2;
			if (isValid(mid, mFrom, mTo, K)	) {
				left = mid - 1;
				ret = mid; // 검증이 완료된 최신 값
			} else {
				right = mid + 1;
			}
		}
	    return ret;
	}
	
	// value에 대한 검증
	// mFrom ~ mTo까지 K 개의 선거구 이상이 가능 ? <- 한 선거구는 무조건 value 이하.
	boolean isValid(int value, int mFrom, int mTo, int K) {
		int cnt = 0; // 선거구의 수
		int nowPopulation = 0; // 지금 만들어가는 선거구의 인원 수
		for (int i = Math.min(mTo, mFrom); i < Math.max(mTo, mFrom); i++) {
			nowPopulation += populations[i];
			if (nowPopulation > value) {
				cnt++;
				nowPopulation = populations[i];
			}
		}
		if (K >= cnt) return true;
		else return false;
	}
	
	class Road implements Comparable<Road> {
		int id;
		int time;
		int count;
		public Road(int id) {
			super();
			this.id = id;
			this.count = 1;
			this.time = calcTime();
		}
		@Override
		public int compareTo(Road o) {
			if (this.time > o.time) return -1; // 내 time이 더 크면 나 먼저
			if (this.time < o.time) return 1;
			if (id < o.id) return -1; // 내 id가 작으면 내가 먼저
			if (id > o.id) return 1;
			return 0;
		}
		private int calcTime() {
			time = (populations[id] + populations[id + 1]) / count;
			return time;
		}
		public int expandRoad() {
			count++;
			return count;
		}
	}
}