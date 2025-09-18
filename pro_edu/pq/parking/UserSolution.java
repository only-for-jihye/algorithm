package pro_edu.pq.parking;

import java.io.*;
import java.util.*; 

/*
 * 0. 차량 정보도 관리해야 한다.
 * 		기존에 나왔던 차도 계속 관리 해야 한다.
 * 		-> id를 기준으로 데이터를 확인할 수 있도록 관리해야 한다.
 * 1. 실제 주차 차량들 관리해야 한다.
 * 		-> 값이 10억까지 가능하므로 DAT는 불가
 * 		-> HashMap 사용!
 * 		-> id를 기준으로 데이터를 확인할 수 있어야 한다.
 * 2. 대기 차량들을 관리해야 한다. <- 최우선 순위로 관리해야 한다. : PriorityQueue
 * 								총 대기 시간 - 총 주차 시간
 * 								총 대기시간 ?
 * 										1. 이전에 대기했던 시간
 * 										2. 현재 시간과 관계 없이 도착 시간만으로만 비교해도 된다.
 * 										* 우선 순위 식
 * 											총 대기시간 - 총 주차시간
 * 											previousWatingTime + curTime - arrivedTime - parkingTime
 * 											curTime은 양 변에 모두 추가 되므로
 * 											previousWatingTime - arrivedTime - parkingTime
 * leave -> id를 base로 어떤 차인지 찾을 수 있어야 한다.
 * 
 */
class UserSolution {
	
	HashMap<Integer, Car> cars; // key : carNumber, value : car
	HashMap<Integer, Car> parkingList; // 실제 주차 리스트
	PriorityQueue<Car> watingList; // 대기열, 대기중인 차량 리스트

	int baseTime, baseFee, unitTime, unitFee, capacity;
	
  
	public void init(int mBaseTime, int mBaseFee, int mUnitTime, int mUnitFee, int mCapacity) {
		cars = new HashMap<Integer, Car>();
		parkingList = new HashMap<Integer, Car>();
		watingList = new PriorityQueue<Car>();
		this.baseTime = mBaseTime;
		this.baseFee = mBaseFee;
		this.unitTime = mUnitTime;
		this.unitFee = mUnitFee;
		this.capacity = mCapacity;
	}

	public int arrive(int mTime, int mCar) {
		Car car = cars.get(mCar);
		if (car == null) {
			car = new Car(mCar, 0, 0, 0, mTime);
			cars.put(mCar, car); // 새로 생성하여 추가
		}
		car.arrivedTime = mTime;
		// 1. 주차장 용량 확인
		// 2. 용량이 비어 있으면 차량 입차
		// 2-1. 용량이 부족하면 대기열에 추가
		// 대기열을 통해서 주차 입고를 할 수 있도록 하자
		watingList.add(car);
		parking(mTime);
		return watingList.size();
	}
	
	public void parking(int mTime) {
		if (parkingList.size() >= capacity) return;
		if (watingList.isEmpty()) return;
		
		// 주차를 진행
		while (!watingList.isEmpty()) {
			Car car = watingList.poll();
			if (cars.get(car.carNumber) != car) continue;
			car.previousWaitingTime = mTime - car.arrivedTime;
			car.arrivedTime = 0;
//			car.parkingTime = ??? // 아직 모른다
			parkingList.put(car.carNumber, car);
			break;
		}
	}

	public int leave(int mTime, int mCar) {
		Car car = parkingList.get(mCar);
		if (car == null) {
			// 대기열에서 삭제될 차
			car = cars.get(mCar);
			Car newCar = new Car(car.carNumber, 0, car.parkingTime, car.previousWaitingTime + mTime - car.arrivedTime, car.parkingStartTime);
			cars.put(car.carNumber, newCar);
			return -1; // parkingList에 없으면 대기중이므로 -1을 리턴
		}
		parkingList.remove(car.carNumber); // 출차
		car.arrivedTime = 0;
		int nowParkingTime = mTime - car.parkingStartTime;
		car.parkingTime += mTime - car.parkingStartTime;
		car.parkingStartTime = 0;
		parking(mTime); // 입차
		
		int cost = baseFee;
		if (nowParkingTime > baseTime) {
			cost += Math.ceil((nowParkingTime - baseTime) / unitTime) * unitFee;
		}
		return cost;
	}
}

class Car implements Comparable<Car> {
	int carNumber; // 자동차 번호
	int arrivedTime; // 도착 시간
	int parkingTime; // 주차 시간 얼마나 주차했는지
	int previousWaitingTime; // 이전에 얼마나 대기했는지
	int parkingStartTime; // 주차 시작 시간
	public Car(int carNumber, int arrivedTime, int parkingTime, int previousWaitingTime, int parkingStartTime) {
		super();
		this.carNumber = carNumber;
		this.arrivedTime = arrivedTime;
		this.parkingTime = parkingTime;
		this.previousWaitingTime = previousWaitingTime;
		this.parkingStartTime = parkingStartTime;
	}
	
	@Override
	public int compareTo(Car o) {
		int thisPr = this.previousWaitingTime - this.arrivedTime - this.parkingTime;
		int otherPr = o.previousWaitingTime - o.arrivedTime - o.parkingTime;
		if (thisPr > otherPr) return -1;
		if (thisPr < otherPr) return 1;
		if (this.arrivedTime != o.arrivedTime) return this.arrivedTime - o.arrivedTime;
		return 0;
	}
}

