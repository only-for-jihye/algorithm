package Bacteria;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.TreeSet;

class UserSolution2 {
    private HashMap<Integer, Bacteria> bacteriaMap;
    private TreeSet<Bacteria> activeBacteria;
    private PriorityQueue<Event> eventQueue;
    private int currentTime;

    // [!!] ADDED: Fenwick Tree (BIT)
    private int[] bit;
    // [!!] ADDED: 수명의 최대 범위 (문제 제약조건에 따라 변경 필요)
    private final int MAX_LIFESPAN = 1000001; 
    
    // ... Bacteria 클래스 (변경 없음) ...
    static class Bacteria implements Comparable<Bacteria> {
        int mID;
        int addTime;
        int initialLifeSpan;
        int mHalfTime;
        int currentLifeSpan; 

        public Bacteria(int mID, int addTime, int initialLifeSpan, int mHalfTime) {
            this.mID = mID;
            this.addTime = addTime;
            this.initialLifeSpan = initialLifeSpan;
            this.mHalfTime = mHalfTime;
            this.currentLifeSpan = initialLifeSpan;
        }

        @Override
        public int compareTo(Bacteria o) {
            if (this.currentLifeSpan != o.currentLifeSpan) {
                return Integer.compare(this.currentLifeSpan, o.currentLifeSpan);
            }
            return Integer.compare(this.mID, o.mID);
        }
    }
    // ... Event 클래스 (변경 없음) ...
    static class Event implements Comparable<Event> {
        int time; 
        int mID;

        public Event(int time, int mID) {
            this.time = time;
            this.mID = mID;
        }

        @Override
        public int compareTo(Event o) {
            return Integer.compare(this.time, o.time);
        }
    }


    public void init() {
        bacteriaMap = new HashMap<>();
        activeBacteria = new TreeSet<>();
        eventQueue = new PriorityQueue<>();
        currentTime = 0;
        
        // [!!] ADDED: BIT 초기화
        bit = new int[MAX_LIFESPAN]; 
    }

    // [!!] ADDED: BIT 업데이트 함수
    private void bitUpdate(int index, int value) {
        // 수명이 MAX_LIFESPAN을 초과하면 가장 큰 인덱스로 처리
        if (index >= MAX_LIFESPAN) index = MAX_LIFESPAN - 1;
        if (index <= 0) return; // 0 이하는 무시

        while (index < MAX_LIFESPAN) {
            bit[index] += value;
            index += (index & -index);
        }
    }

    // [!!] ADDED: BIT 누적 합 쿼리 함수
    private int bitQuery(int index) {
        // MAX_LIFESPAN을 초과하는 쿼리는 가장 큰 인덱스 값으로 처리
        if (index >= MAX_LIFESPAN) index = MAX_LIFESPAN - 1;
        if (index <= 0) return 0; // 0 이하 쿼리는 0

        int sum = 0;
        while (index > 0) {
            sum += bit[index];
            index -= (index & -index);
        }
        return sum;
    }

    /**
     * targetTime까지의 모든 이벤트를 처리
     */
    private void processEvents(int targetTime) {
        while (!eventQueue.isEmpty() && eventQueue.peek().time <= targetTime) {
            Event event = eventQueue.poll();
            int eventTime = event.time;
            int mID = event.mID;

            Bacteria bacteria = bacteriaMap.get(mID);
            
            if (bacteria == null || !activeBacteria.contains(bacteria)) {
                continue;
            }

            // 1. 상태 변경을 위해 TreeSet에서 잠시 제거
            activeBacteria.remove(bacteria);
            // [!!] ADDED: BIT에서도 제거 (이전 수명 기준)
            bitUpdate(bacteria.currentLifeSpan, -1);
            
            // 2. 현재 시점까지 경과된 반감기 횟수 계산
            int elapsed = eventTime - bacteria.addTime;
            int halfCount = elapsed / bacteria.mHalfTime;

            // 3. 수명 업데이트
            int newLifeSpan = bacteria.initialLifeSpan >> halfCount;
            bacteria.currentLifeSpan = newLifeSpan;

            // 4. 수명에 따라 폐기 또는 재등록 처리
            if (newLifeSpan <= 99) {
                // 폐기
                bacteriaMap.remove(mID);
                // BIT에서는 이미 위에서 (-1) 했으므로 추가 작업 불필요
            } else {
                // 살아남음: TreeSet에 다시 추가
                activeBacteria.add(bacteria);
                // [!!] ADDED: BIT에도 다시 추가 (새로운 수명 기준)
                bitUpdate(bacteria.currentLifeSpan, 1);
                
                // 다음 이벤트 예약
                int nextEventTime = bacteria.addTime + (halfCount + 1) * bacteria.mHalfTime;
                eventQueue.add(new Event(nextEventTime, mID));
            }
        }
        currentTime = targetTime;
    }

    public void addBacteria(int tStamp, int mID, int mLifeSpan, int mHalfTime) {
        processEvents(tStamp);

        Bacteria newBacteria = new Bacteria(mID, tStamp, mLifeSpan, mHalfTime);

        if (newBacteria.currentLifeSpan <= 99) {
            return;
        }

        bacteriaMap.put(mID, newBacteria);
        activeBacteria.add(newBacteria);
        // [!!] ADDED: BIT에 추가
        bitUpdate(newBacteria.currentLifeSpan, 1);

        eventQueue.add(new Event(tStamp + mHalfTime, mID));
    }

    public int getMinLifeSpan(int tStamp) {
        // [!!] MODIFIED: 로직은 같지만, processEvents가 BIT도 업데이트함
        processEvents(tStamp);

        if (activeBacteria.isEmpty()) {
            return -1;
        }
        
        // TreeSet은 이 용도로 계속 사용
        return activeBacteria.first().mID;
    }

    public int getCount(int tStamp, int mMinSpan, int mMaxSpan) {
        // [!!] MODIFIED: 로직이 완전히 변경됨
        processEvents(tStamp);
        
        // BIT를 사용하여 O(log K) 시간에 범위 쿼리
        return bitQuery(mMaxSpan) - bitQuery(mMinSpan - 1);
    }
}