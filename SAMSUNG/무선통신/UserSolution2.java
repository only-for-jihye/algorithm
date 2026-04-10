package 무선통신;

import java.io.*;
import java.util.*;

// 2024.12.07 무선 통신
// @admin_deukwha
// Spatial Partitioning + Priority Queue

class UserSolution2 {
	
	class Transmitter implements Comparable <Transmitter>{
		int id;
		int y;
		int x;
		int freq; 
		int pow;
		Transmitter(int id, int pow) {
			this.id = id;
			this.pow = pow;
		}
		
		Transmitter(int id, int y, int x, int freq, int pow) {
			this.id = id;
			this.y = y;
			this.x = x;
			this.freq = freq;
			this.pow = pow; 
		}
		@Override
		public int compareTo(Transmitter o) {
			// 반대로
			if(pow > o.pow) return -1;
			if(pow < o.pow) return 1;
			if(id > o.id) return -1;
			if(id < o.id) return 1;
			return 0;
		}
	}
	Transmitter[] transmitters = new Transmitter[50001];
	int mod = 1500;
	ArrayList<Integer>[][]board = new ArrayList[10][10]; 
	int n; 
	int limit;
	
	int[]ydir = {0, -1, 1, 0, 0, -1, 1, -1, 1};
	int[]xdir = {0, 0, 0, -1, 1, -1, -1, 1, 1}; 
	
	public void init(int N, int mLimit) {
		limit = mLimit;
		for(int i = 0; i < 10; i++) 
			for(int j = 0; j < 10; j++)
				board[i][j] = new ArrayList<>();
	  	return;
	}

	// 500 
	// K=10, 최대 50,000개의 라디오가 존재할 수 있다.
	public void addRadio(int K, int[] mID, int[] mFreq, int[] mY, int[] mX) {
		for(int i = 0; i < K; ++i) {
			int id = mID[i];
			int freq = mFreq[i];
			int y = mY[i]; 
			int x = mX[i]; 
			board[y/mod][x/mod].add(id);
			transmitters[id] = new Transmitter(id, y, x, freq, 0); 
		}
	  	return; 
	}
	
	// N이 500 이상일 경우 N개 이상을 보장한다.
	// -> 10,000일 경우 10,000개가 존재함을 보장
	// 1,000
	public int getMinPower(int mID, int mCount) {
		Transmitter t = transmitters[mID]; 
		PriorityQueue<Transmitter>pq = new PriorityQueue<>(); 
		
		int y = t.y / mod;
		int x = t.x / mod; 
		
		for(int i = 0; i < 9; i++) {
			int ny = y + ydir[i];
			int nx = x + xdir[i];
			if(ny < 0 || nx < 0 || ny >= 10 || nx >= 10) 
				continue;
			for(int id : board[ny][nx]) {
				if(id == t.id) continue;
				int dist = Math.abs(t.y - transmitters[id].y) + Math.abs(t.x - transmitters[id].x);
				int pow = dist * 10;
				if(t.freq != transmitters[id].freq) pow += 1000; 
				if(pow > limit) continue;
				Transmitter tr = new Transmitter(id, pow);
				if(pq.size() < mCount) pq.add(tr);
				else if(tr.compareTo(pq.peek()) == 1) {
					pq.remove();
					pq.add(tr); 
				}
			}
		}
		int ret = 0;
		while(!pq.isEmpty()) ret += pq.remove().pow; 
		return ret;
	}
}