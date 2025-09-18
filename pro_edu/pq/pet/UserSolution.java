package pro_edu.pq.pet;

import java.io.*;
import java.util.*;

class UserSolution {
	
	// mTime <- time flow
	// 2가지
	// 1. 진짜 해당 시간의 전체적인 state를 관리 <- 시간이 흐르는 과정을 구현
	// 2. 특정 object만 해당 시간의 state를 관리

	// 변동이 생기는 시점 : life 가 감소한다.
	// life가 작은 순서의 동물을 PriorityQueue를 이용
	
	Pet pets[]; // ID가 100만까지 이므로 DAT로 활용
	PriorityQueue<Pet> pq;
	int alivedPetCount;
	
	public void init() {
		pets = new Pet[1000001];
		pq = new PriorityQueue<Pet>();
		alivedPetCount = 0;
	}
	
	void decreaseLife(int mTime) { // mTime까지 시간이 흐른 상태
		while(!pq.isEmpty()) {
			Pet nowPet = pq.peek();
			
			if (nowPet != pets[nowPet.id]) { // 갱신되기 전 정보가 있을 경우 삭제 처리
				pq.poll(); // 삭제
				continue;
			}
			if (nowPet.life > mTime) break; // 현재 시간 이후에 대한 정보, 이미 충분히 시간이 흐름
			// now.life <- 해당 동물의 수명이 mTime보다 작다. 수명이 다했다.
			pets[nowPet.id] = null;
			pq.poll(); // 삭제
			alivedPetCount--;
		}
	}
	
	public int adopt(int mTime, int id, int life) {
		decreaseLife(mTime);
		// 이미 있으면 동물 생성 X
		if (pets[id] != null) return alivedPetCount;
		// 없으면 생성하고 반환
		Pet pet = new Pet(id, mTime + life); // 지금으로부터 몇시간 살 수 있는지.
		pets[id] = pet;
		pq.add(pet);
		alivedPetCount++;
		return alivedPetCount;
	}

	public int getLife(int mTime, int id) {
		decreaseLife(mTime);
//		while(!pq.isEmpty()) {
//			Pet p = pq.peek();
//			if (p.id == id) {
//				return p.life;
//			}
//		}
		Pet pet = pets[id];
		if (pet == null) return -1;
		return pet.life - mTime;
	}
  
  	public int vaccinate(int mTime, int id, int life) {
  		decreaseLife(mTime);
//  		while(!pq.isEmpty()) {
//  			Pet p = pq.peek();
//  			if (p.id == id) {
//  				p.life += life;
//  				return p.life;
//  			}
//  		}
  		Pet pet = pets[id];
  		if (pet == null) return -1;
  		
  		Pet newPet = new Pet(pet.id, pet.life + life);
  		pq.add(newPet);
  		pets[id] = newPet; // 직접 수정하면 안되고 위와 같이 새로운 정보로 갱신한다.
  		
  		return newPet.life - mTime;
    }
}

class Pet implements Comparable<Pet> {
//	int mTime;
	int id;
	int life;
//	boolean isDead;
	public Pet (int id, int life) {
//		this.mTime = mTime;
		this.id = id;
		this.life = life;
//		this.isDead = false;
	}
	@Override
	public int compareTo(Pet o) {
//		return Integer.compare(this.mTime, o.mTime);
		return Integer.compare(this.life, o.life);
	}
}