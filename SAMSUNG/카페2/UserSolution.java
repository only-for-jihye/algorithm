package 카페2;

//UserSolution.java
import java.util.*;

class UserSolution {
	
	Map<Integer, Order> orderMap; // 전체 주문
	Map<Integer, ArrayDeque<Integer>> waitingDrinks; // 음료별 대기 주문 
	TreeSet<Order> orderSet;
	int arrivalTime;
  
	public void init(int N) {
		orderMap = new HashMap<>(); 
		waitingDrinks = new HashMap<>(); 
		for (int i = 1; i <= N; i++) {
            waitingDrinks.put(i, new ArrayDeque<>());
        }
		orderSet = new TreeSet<>();
		arrivalTime = 0;
	}

	public int order(int mID, int M, int mBeverages[]) {
		// 1. 주문에 포함된 음료 종류별 개수를 센다.
		Map<Integer, Integer> remainingDrinksMap = new HashMap<>();
//		for (int beverage : mBeverages) {
		for (int i = 0; i < M; i++) {
			remainingDrinksMap.put(mBeverages[i], remainingDrinksMap.getOrDefault(remainingDrinksMap, 0) + 1);
			// 2. 해당 음료를 기다리는 큐에 주문 ID를 추가한다.
			waitingDrinks.get(mBeverages[i]).add(mID);
		}
		// 3. Order 객체를 생성하고 자료 구조에 추가한다.
		Order newOrder = new Order(mID, arrivalTime++, M, remainingDrinksMap);
		orderMap.put(mID, newOrder); // 전체 주문 관리 내역 추가
		orderSet.add(newOrder);
		return orderSet.size();
	}

	public int supply(int mBeverage) {
		ArrayDeque<Integer> waitingQueue = waitingDrinks.get(mBeverage);
		while (!waitingQueue.isEmpty()) {
			int orderId = waitingQueue.pollFirst();
			Order order = orderMap.get(orderId);
			
			// 이미 완료되었거나 취소된 주문은 건너 뜀
			if (order == null || order.isCanceled || order.isCompleted) continue;
			
			// TreeSet은 정렬 기준이 되는 값이면 업데이트
			// 먼저 제거하고 값 바꾸고 다시 add
			orderSet.remove(order);
			
			order.remainingCount--;
			order.remainingDrinks.put(mBeverage, order.remainingDrinks.get(mBeverage) - 1);
			order.allocatedDrinks.add(mBeverage); // 처리한 음료 기록
			
			if (order.remainingCount == 0) {
				order.isCompleted = true; // 완료
			} else {
				orderSet.add(order);
			}
			return orderId;
		}
		return -1; // 유효한 주문이 없음, 음료 버림
	}

	public int cancel(int mID) {
		
		Order cancelOrder = orderMap.get(mID);
		
		if (cancelOrder.isCompleted) return 0;
		if (cancelOrder.isCanceled) return -1;
		
		// 1. 주문을 유효 주문 목록에서 제거하고 취소 상태로 변경
		int remainingCountBeforeCancel = cancelOrder.remainingCount;
		orderSet.remove(cancelOrder);
		cancelOrder.isCanceled = true;
		
		// 2. 이 주문에 공급되었던 음료를 다른 주문에 재배치
	    for (Integer beverage : cancelOrder.allocatedDrinks) {
//	    	waitingDrinks.get(beverage).addFirst(mID); // 임시로 넣기
	        supply(beverage);
//	        waitingDrinks.get(beverage).remove(mID); // 다시 제거
	    }
	    
	    return remainingCountBeforeCancel;
	}
	
	public int getStatus(int mID) {
		Order order = orderMap.get(mID);
		if (order.isCompleted) return 0;
		if (order.isCanceled) return -1;
		
		return order.remainingCount;
	}

	Main.RESULT hurry() {
		Main.RESULT res = new Main.RESULT();
		Iterator<Order> it = orderSet.iterator();
		res.IDs = new int[5];
		
		int range = 0;
		while (it.hasNext() && range < 5) {
			res.IDs[range++] = it.next().orderId; 
		}
		res.cnt = range;
		
		return res;
	}
	
	class Order implements Comparable<Order> {
		int orderId;
		int arrivalTime;
		int remainingCount;
		Map<Integer, Integer> remainingDrinks;
		List<Integer> allocatedDrinks;
		boolean isCanceled;
		boolean isCompleted;
		public Order(int orderId, int arrivalTime, int M, Map<Integer, Integer> remainingDrinks) {
			super();
			this.orderId = orderId;
			this.arrivalTime = arrivalTime;
			this.remainingCount = M;
			this.remainingDrinks = remainingDrinks;
			this.allocatedDrinks = new ArrayList<>();
			this.isCanceled = false;
			this.isCompleted = false;
		}
		@Override
		public int compareTo(Order o) {
			// 1순위: 남은 음료 개수 (내림차순)
            if (this.remainingCount != o.remainingCount) {
                return Integer.compare(o.remainingCount, this.remainingCount);
            }
            // 2순위: 도착 시간 (오름차순)
            if (this.arrivalTime != o.arrivalTime) {
                return Integer.compare(this.arrivalTime, o.arrivalTime);
            }
            // 고유성을 보장하기 위한 3순위
            return Integer.compare(this.orderId, o.orderId);
		}
	}
}