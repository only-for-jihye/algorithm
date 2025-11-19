package SmartFarm;

import java.util.*;

class UserSolution_AI {

    int N; 
    int[] growthTimes; 
    Crop[][] farm; // N x N 농장 격자

    class Crop {
        int sowTime;     
        int category;    
        int extraGrowth; 

        Crop(int sowTime, int category) {
            this.sowTime = sowTime;
            this.category = category;
            this.extraGrowth = 0;
        }

        int getSize(int mTime) {
            // (mTime - this.sowTime)이 0 미만일 수 있으나,
            // growthTimes[i]가 1 이상이므로 timeGrowth는 0 이하가 됩니다.
            // L이 0 이상이므로, L 미만 체크(if (currentSize < L))는
            // 음수 크기(미래에 심김)에 대해서도 올바르게 동작합니다.
            // 단, 문제 제약상 mTime은 항상 증가하므로 sowTime <= mTime 입니다.
            int timeGrowth = (mTime - this.sowTime) / growthTimes[this.category];
            return timeGrowth + this.extraGrowth;
        }
    }

    public void init(int N, int mGrowthTime[]) {
        this.N = N;
        this.growthTimes = new int[3];
        System.arraycopy(mGrowthTime, 0, this.growthTimes, 0, 3);
        
        // N x N 크기의 농장 격자 생성
        this.farm = new Crop[N][N];
    }

    public int sow(int mTime, int mRow, int mCol, int mCategory) {
        if (farm[mRow][mCol] != null) {
            return 0; // 심기 실패
        }
        farm[mRow][mCol] = new Crop(mTime, mCategory);
        return 1; // 심기 성공
    }

    public int water(int mTime, int G, int mRow, int mCol, int mHeight, int mWidth) {
        int wateredCount = 0;
        int endRow = mRow + mHeight;
        int endCol = mCol + mWidth;

        for (int r = mRow; r < endRow; r++) {
            for (int c = mCol; c < endCol; c++) {
                if (farm[r][c] != null) {
                    farm[r][c].extraGrowth += G;
                    wateredCount++;
                }
            }
        }
        return wateredCount;
    }

    /**
     * [!!] TLE 해결의 핵심: GC(메모리 할당) 없는 2-Pass harvest
     */
    public int harvest(int mTime, int L, int mRow, int mCol, int mHeight, int mWidth) {
        int endRow = mRow + mHeight;
        int endCol = mCol + mWidth;
        
        int cropCount = 0; // 사각형 내 총 작물 수

        // --- 1단계: 검사 (O(H*W)) ---
        // (ArrayList 같은 추가 메모리 할당 절대 금지)
        for (int r = mRow; r < endRow; r++) {
            for (int c = mCol; c < endCol; c++) {
                Crop crop = farm[r][c];
                
                if (crop != null) {
                    cropCount++; 
                    if (crop.getSize(mTime) < L) {
                        return 0; // 수확 실패
                    }
                }
            }
        }

        // --- 2단계: 수확 (O(H*W)) ---
        
        // 수확할 작물이 없었으면 0 반환
        if (cropCount == 0) {
            return 0;
        }

        // 1단계를 통과했으므로, 다시 순회하며 실제 수확(삭제)
        for (int r = mRow; r < endRow; r++) {
            for (int c = mCol; c < endCol; c++) {
                // (crop != null) 조건문이 여기서는 필수
                if (farm[r][c] != null) {
                    farm[r][c] = null;
                }
            }
        }

        return cropCount;
    }
}