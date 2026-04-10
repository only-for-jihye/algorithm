package 무선통신;

import java.io.*;
import java.util.*;

import java.util.*;

class UserSolution {
    private int N, mLimit;
    private static final int CELL_SIZE = 300;
    private int GRID_DIM;

    // 객체 생성을 최소화하기 위한 1차원 배열 및 Linked List 기반 Grid
    private int[][] head;
    private int[] next;
    private int[] rY, rX, rFreq;

    public void init(int N, int mLimit) {
        this.N = N;
        this.mLimit = mLimit;
        this.GRID_DIM = (N / CELL_SIZE) + 1;

        this.head = new int[GRID_DIM][GRID_DIM];
        this.next = new int[50005]; 
        this.rY = new int[50005];
        this.rX = new int[50005];
        this.rFreq = new int[50005];
    }

    public void addRadio(int K, int[] mID, int[] mFreq, int[] mY, int[] mX) {
        for (int i = 0; i < K; i++) {
            int id = mID[i];
            rY[id] = mY[i];
            rX[id] = mX[i];
            rFreq[id] = mFreq[i];

            int cy = mY[i] / CELL_SIZE;
            int cx = mX[i] / CELL_SIZE;

            next[id] = head[cy][cx];
            head[cy][cx] = id;
        }
    }

    // 주어진 파워(targetPower) 이하로 연결 가능한 통신기의 개수를 반환
    private int countRadios(int mID, int targetPower) {
        int cY = rY[mID], cX = rX[mID], cFreq = rFreq[mID];
        
        // targetPower에 따라 탐색해야 할 최대 격자 거리가 동적으로 축소됨
        int maxDist = targetPower / 10;
        int minCY = Math.max(0, cY - maxDist) / CELL_SIZE;
        int maxCY = Math.min(N - 1, cY + maxDist) / CELL_SIZE;
        int minCX = Math.max(0, cX - maxDist) / CELL_SIZE;
        int maxCX = Math.min(N - 1, cX + maxDist) / CELL_SIZE;

        int count = 0;
        for (int i = minCY; i <= maxCY; i++) {
            for (int j = minCX; j <= maxCX; j++) {
                int currId = head[i][j];
                
                while (currId != 0) {
                    if (currId != mID) {
                        int dist = Math.abs(cY - rY[currId]) + Math.abs(cX - rX[currId]);
                        int power = dist * 10;
                        if (cFreq != rFreq[currId]) power += 1000;
                        
                        if (power <= targetPower) {
                            count++;
                        }
                    }
                    currId = next[currId];
                }
            }
        }
        return count;
    }

    public int getMinPower(int mID, int mCount) {
        // [Phase 1] 파라메트릭 서치: 조건을 만족하는 "최소 임계 파워(optPower)" 찾기
        int low = 0;
        int high = mLimit;
        int optPower = mLimit;

        while (low <= high) {
            int mid = (low + high) / 2;
            
            // mid 파워만으로 mCount개 이상 연결할 수 있다면, 파워를 더 줄여본다
            if (countRadios(mID, mid) >= mCount) {
                optPower = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        // [Phase 2] 확정된 임계 파워(optPower)를 바탕으로 실제 파워 합산 및 ID 정렬(동점자 처리)
        int cY = rY[mID], cX = rX[mID], cFreq = rFreq[mID];
        int maxDist = optPower / 10;
        int minCY = Math.max(0, cY - maxDist) / CELL_SIZE;
        int maxCY = Math.min(N - 1, cY + maxDist) / CELL_SIZE;
        int minCX = Math.max(0, cX - maxDist) / CELL_SIZE;
        int maxCX = Math.min(N - 1, cX + maxDist) / CELL_SIZE;

        long totalPower = 0;
        int selectedCount = 0;
        List<Integer> tieBreakers = new ArrayList<>(); // 파워가 완전히 동일한 후보군

        for (int i = minCY; i <= maxCY; i++) {
            for (int j = minCX; j <= maxCX; j++) {
                int currId = head[i][j];
                while (currId != 0) {
                    if (currId != mID) {
                        int dist = Math.abs(cY - rY[currId]) + Math.abs(cX - rX[currId]);
                        int power = dist * 10;
                        if (cFreq != rFreq[currId]) power += 1000;

                        // 임계 파워보다 작으면 무조건 포함
                        if (power < optPower) {
                            totalPower += power;
                            selectedCount++;
                        } 
                        // 임계 파워와 같으면 동점자 리스트에 보관하여 나중에 ID 순으로 선별
                        else if (power == optPower) {
                            tieBreakers.add(currId);
                        }
                    }
                    currId = next[currId];
                }
            }
        }

        // 동점자(파워가 같은 통신기)들 중 ID가 작은 순서대로 필요한 만큼만 추가
        Collections.sort(tieBreakers);
        int needed = mCount - selectedCount;
        for (int i = 0; i < needed; i++) {
            totalPower += optPower; // 이들은 모두 파워가 optPower임
        }

        return (int) totalPower;
    }
}