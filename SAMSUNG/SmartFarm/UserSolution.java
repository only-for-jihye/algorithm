package SmartFarm;

/*
 * [문제 요약]
 * N x N 농장에서 작물을 관리하는 4개의 API를 구현합니다.
 *
 * 1. init(N, mGrowthTime): N x N 농장을 초기화하고, 3가지 작물 품종별 성장 시간을 저장합니다.
 * 2. sow(mTime, r, c, category): mTime 시각에 (r, c) 위치에 작물을 심습니다.
 * - 이미 작물이 있으면 0을 반환, 성공하면 1을 반환합니다.
 * - 초기 크기는 0입니다.
 * 3. water(mTime, G, r, c, h, w): mTime 시각에 (r, c)부터 h x w 크기 사각형 구역에 물을 줍니다.
 * - 해당 구역의 모든 작물 크기가 G만큼 증가합니다.
 * - 물을 맞은 작물의 수를 반환합니다.
 * 4. harvest(mTime, L, r, c, h, w): mTime 시각에 (r, c)부터 h x w 크기 사각형 구역을 수확합니다.
 * - 구역 내 "모든" 작물의 크기가 L 이상이어야 합니다.
 * - 하나라도 L 미만이면 수확에 실패하고 0을 반환합니다.
 * - 성공하면 해당 구역의 모든 작물을 제거(수확)하고, 수확한 작물 수를 반환합니다.
 *
 * [핵심 로직]
 * 작물의 크기 계산:
 * mTime 시각의 작물 크기 = (시간에 따른 성장) + (물주기에 따른 추가 성장)
 * - 시간에 따른 성장 = (mTime - sowTime) / growthTime[category]
 * - 물주기에 따른 추가 성장 = water() 호출로 누적된 G의 합
 *
 * [자료 구조]
 * N x N 크기의 2D 배열을 사용하여 농장 격자를 표현합니다.
 * 각 셀에는 `Crop` 객체를 저장하거나, 작물이 없으면 `null`을 저장합니다.
 *
 * Crop 클래스:
 * - int sowTime: 작물이 심어진 시간
 * - int category: 작물 품종 (0, 1, 2)
 * - int extraGrowth: water()로 인해 추가된 총 성장량
 *
 * [함수별 구현]
 * 1. init(): N과 growthTimes를 멤버 변수에 저장하고, `farm = new Crop[N][N]`으로 2D 배열을 생성합니다.
 *
 * 2. sow(): `farm[mRow][mCol]`이 `null`인지 확인합니다.
 * - `null`이면, `new Crop(mTime, mCategory)` 객체를 생성하여 `farm[mRow][mCol]`에 할당하고 1을 반환합니다.
 * - `null`이 아니면 (이미 작물이 있으면) 0을 반환합니다.
 *
 * 3. water(): (mRow, mCol)부터 (mRow + mHeight - 1, mCol + mWidth - 1)까지 2중 for 루프를 돕니다.
 * - 각 (r, c) 위치에 대해 `farm[r][c] != null`인지 확인합니다.
 * - `null`이 아니면(작물이 있으면), `farm[r][c].extraGrowth += G`를 수행하고 카운트를 1 증가시킵니다.
 * - 루프 종료 후 총 카운트를 반환합니다.
 *
 * 4. harvest(): 2단계로 진행합니다.
 * - [1단계: 검사]
 * - (mRow, mCol)부터 (mRow + mHeight - 1, mCol + mWidth - 1)까지 2중 for 루프를 돕니다.
 * - `cropCount` 변수로 사각형 내 총 작물 수를 셉니다.
 * - 각 (r, c) 위치에 작물이 존재하면(`farm[r][c] != null`), 현재 크기를 계산합니다.
 * - `currentSize = (mTime - crop.sowTime) / growthTimes[crop.category] + crop.extraGrowth`
 * - `currentSize < L` 이면, 수확 조건(모든 작물이 L 이상)을 만족하지 못하므로 즉시 0을 반환합니다.
 * - [2단계: 수확]
 * - 1단계 루프가 0을 반환하지 않고 완료되었다면, 해당 구역의 모든 작물이 L 이상이라는 의미입니다.
 * - `cropCount`가 0이면 (수확할 작물이 없음) 0을 반환합니다.
 * - `cropCount`가 0보다 크면, 다시 2중 for 루프를 돌면서 `farm[r][c]`가 `null`이 아닌 모든 셀을 `null`로 설정하여 작물을 제거(수확)합니다.
 * - `cropCount`를 반환합니다.
 *
 * [성능]
 * - N이 최대 1,000이므로 N x N은 1,000,000입니다.
 * - `water`와 `harvest`는 사각형 영역(H x W)을 순회하며, 최악의 경우 O(N^2) (약 1,000,000) 연산이 걸립니다.
 * - `water`/`harvest` 호출 횟수가 10,000회이므로, 총 O(Calls * N^2) = 10,000 * 1,000,000 = 10^10이 되어 시간 초과가 발생할 수 있습니다.
 * - 하지만 `sow` 호출이 100,000회로 제한되므로, 격자(farm)는 최대 100,000개의 작물만 가집니다 (10% 밀도).
 * - O(H * W) 루프는 대부분 `null`을 확인하고 지나갑니다.
 * - 출제 의도는 2D 세그먼트 트리 등이 아닌, 이 O(H * W) 브루트포스 풀이로 통과되도록 테스트 케이스의 총합 $\sum (H \times W)$가 관리된다고 가정하는 것이 합리적입니다.
 */

import java.util.ArrayList;

class UserSolution {

    int N; // 농장 크기
    int[] growthTimes; // 작물별 성장 시간
    Crop[][] farm; // N x N 농장 격자

    /**
     * 작물 정보를 저장하는 내부 클래스
     */
    class Crop {
        int sowTime;     // 작물이 심어진 시간
        int category;    // 작물 품종 (0, 1, 2)
        int extraGrowth; // water()로 인해 추가된 총 성장량

        Crop(int sowTime, int category) {
            this.sowTime = sowTime;
            this.category = category;
            this.extraGrowth = 0;
        }

        /**
         * 현재 시간(mTime) 기준으로 작물의 총 크기를 계산
         */
        int getSize(int mTime) {
            // 시간에 따른 성장 = (현재 시간 - 심은 시간) / 품종별 성장 시간
            int timeGrowth = (mTime - this.sowTime) / growthTimes[this.category];
            // 총 크기 = 시간에 따른 성장 + 물주기로 인한 추가 성장
            return timeGrowth + this.extraGrowth;
        }
    }

    /**
     * 테스트 케이스 시작 시 농장 초기화
     * @param N 농장의 한 변의 길이 (10 <= N <= 1,000)
     * @param mGrowthTime 3개 품종의 성장 시간 (1 <= mGrowthTime[i] <= 30)
     */
    public void init(int N, int mGrowthTime[]) {
        this.N = N;
        this.growthTimes = new int[3];
        // mGrowthTime 배열 복사
        System.arraycopy(mGrowthTime, 0, this.growthTimes, 0, 3);
        
        // N x N 크기의 농장 격자 생성 (모든 셀은 null로 초기화됨)
        this.farm = new Crop[N][N];
    }

    /**
     * 특정 위치에 작물을 심음
     * @param mTime 현재 시간 (1 <= mTime <= 1,000,000)
     * @param mRow 행 위치
     * @param mCol 열 위치
     * @param mCategory 작물 품종
     * @return 성공 시 1, 실패(이미 작물이 있음) 시 0
     */
    public int sow(int mTime, int mRow, int mCol, int mCategory) {
        // 해당 위치에 이미 작물이 있는지 확인
        if (farm[mRow][mCol] != null) {
            return 0; // 심기 실패
        }

        // 새 작물을 생성하여 격자에 배치
        farm[mRow][mCol] = new Crop(mTime, mCategory);
        return 1; // 심기 성공
    }

    /**
     * 사각형 구역에 물을 주어 작물을 성장시킴
     * @param mTime 현재 시간
     * @param G 추가 성장 크기 (1 <= G <= 10)
     * @param mRow 좌측 상단 행
     * @param mCol 좌측 상단 열
     * @param mHeight 세로 길이
     * @param mWidth 가로 길이
     * @return 물을 맞은 작물의 수
     */
    public int water(int mTime, int G, int mRow, int mCol, int mHeight, int mWidth) {
        int wateredCount = 0;
        int endRow = mRow + mHeight;
        int endCol = mCol + mWidth;

        // 지정된 사각형 구역을 순회
        for (int r = mRow; r < endRow; r++) {
            for (int c = mCol; c < endCol; c++) {
                // 해당 위치에 작물이 존재하면
                if (farm[r][c] != null) {
                    farm[r][c].extraGrowth += G; // 추가 성장량 누적
                    wateredCount++;
                }
            }
        }
        return wateredCount;
    }

    /**
     * 사각형 구역의 작물을 수확
     * @param mTime 현재 시간
     * @param L 수확 최소 크기 (0 <= L <= 100)
     * @param mRow 좌측 상단 행
     * @param mCol 좌측 상단 열
     * @param mHeight 세로 길이
     * @param mWidth 가로 길이
     * @return 수확한 작물의 수 (실패 시 0)
     */
//    public int harvest(int mTime, int L, int mRow, int mCol, int mHeight, int mWidth) {
//        int endRow = mRow + mHeight;
//        int endCol = mCol + mWidth;
//        
//        // 수확 대상이 될 작물들의 위치를 임시 저장
//        ArrayList<int[]> cropsToHarvest = new ArrayList<>();
//        int cropCount = 0; // 사각형 내 총 작물 수
//
//        // --- 1단계: 수확 가능 여부 검사 ---
//        for (int r = mRow; r < endRow; r++) {
//            for (int c = mCol; c < endCol; c++) {
//                Crop crop = farm[r][c];
//                
//                // 작물이 존재하는 경우
//                if (crop != null) {
//                    cropCount++; // 총 작물 수 증가
//                    
//                    // 현재 크기 계산
//                    int currentSize = crop.getSize(mTime);
//                    
//                    // 크기가 L 미만인 작물이 하나라도 있으면 수확 실패
//                    if (currentSize < L) {
//                        return 0; 
//                    }
//                    
//                    // 수확 대상 목록에 추가 (Java 1.8+에서는 람다/스트림 사용 가능)
//                    // 여기서는 간단히 int[2] 배열로 위치 저장
//                    cropsToHarvest.add(new int[]{r, c});
//                }
//            }
//        }
//
//        // --- 2단계: 수확 진행 ---
//        // 1단계를 통과했다면, 구역 내 모든 작물이 L 이상이거나 작물이 없음
//        
//        // 사각형 내에 작물이 없었으면 0 반환
//        if (cropCount == 0) {
//            return 0;
//        }
//
//        // 수확 대상 작물들을 격자에서 제거
//        for (int[] coord : cropsToHarvest) {
//            farm[coord[0]][coord[1]] = null;
//        }
//
//        // 수확한 작물의 수 반환
//        return cropCount; // 또는 cropsToHarvest.size()
//    }
    public int harvest(int mTime, int L, int mRow, int mCol, int mHeight, int mWidth) {
        int endRow = mRow + mHeight;
        int endCol = mCol + mWidth;
        
        int cropCount = 0; // 사각형 내 총 작물 수

        // --- 1단계: 수확 가능 여부 검사 (O(H*W)) ---
        for (int r = mRow; r < endRow; r++) {
            for (int c = mCol; c < endCol; c++) {
                Crop crop = farm[r][c];
                
                // 작물이 존재하는 경우
                if (crop != null) {
                    cropCount++; // 총 작물 수 증가
                    
                    // 현재 크기 계산
                    int currentSize = crop.getSize(mTime);
                    
                    // 크기가 L 미만인 작물이 하나라도 있으면 수확 실패
                    if (currentSize < L) {
                        return 0; // [!] 즉시 실패 반환
                    }
                    
                    // [!] ArrayList에 추가하는 로직 (메모리 할당) 삭제
                }
            }
        }

        // --- 2단계: 수확 진행 ---
        
        // 사각형 내에 수확할 작물이 없었으면 0 반환
        if (cropCount == 0) {
            return 0;
        }

        // [!] O(H*W) 영역을 다시 순회하며 실제 수확(삭제) 진행
        for (int r = mRow; r < endRow; r++) {
            for (int c = mCol; c < endCol; c++) {
                // 작물이 있는 칸만 null로 변경
                if (farm[r][c] != null) {
                    farm[r][c] = null;
                }
            }
        }

        // 수확한 작물의 수 반환
        return cropCount;
    }
}