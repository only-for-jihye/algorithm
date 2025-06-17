import java.io.FileReader; // FileReader 추가
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    // 전구 종류 상수
    private static final int RED = 0;
    private static final int BLUE = 1;
    private static final int YELLOW = 2;
    
    // 모든 장식 조명 모델에 공통적으로 적용되는 최대 생산 가능 개수
    private static final int MAX_PRODUCTION_PER_MODEL_GLOBAL = 3; 

    // 장식 조명 모델을 표현하는 클래스
    static class DecorativeLightModel {
        int[] costs; // [빨강, 파랑, 노랑] 전구 소모량

        public DecorativeLightModel(int red, int blue, int yellow) {
            this.costs = new int[]{red, blue, yellow};
        }
    }

    // 전역 변수 (백트래킹에서 상태 공유)
    static int[] currentAvailableBulbs; // 현재 남은 전구 개수 [빨강, 파랑, 노랑]
    static DecorativeLightModel[] models; // 현재 테스트 케이스의 모든 장식 조명 모델 정보
    static int maxTotalModelsProduced; // 현재 테스트 케이스에서 최대로 만들 수 있는 장식 조명의 총 개수

    public static void main(String[] args) throws IOException {
        // BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader br = new BufferedReader(new FileReader("sample.txt")); 
        StringTokenizer st;

        int T = Integer.parseInt(br.readLine()); // 첫 줄: 테스트 케이스 갯수

        for (int t = 1; t <= T; t++) { // 각 테스트 케이스 처리
            st = new StringTokenizer(br.readLine());

            int numModels = Integer.parseInt(st.nextToken()); // 현재 테스트 케이스의 장식 조명 모델 종류 수
            currentAvailableBulbs = new int[3]; // 현재 테스트 케이스의 초기 전구 개수
            currentAvailableBulbs[RED] = Integer.parseInt(st.nextToken());
            currentAvailableBulbs[BLUE] = Integer.parseInt(st.nextToken());
            currentAvailableBulbs[YELLOW] = Integer.parseInt(st.nextToken());

            models = new DecorativeLightModel[numModels];
            // 각 장식 조명 모델별 필요한 전구 갯수 정보 읽기
            for (int i = 0; i < numModels; i++) {
                st = new StringTokenizer(br.readLine());
                int redCost = Integer.parseInt(st.nextToken());
                int blueCost = Integer.parseInt(st.nextToken());
                int yellowCost = Integer.parseInt(st.nextToken());
                models[i] = new DecorativeLightModel(redCost, blueCost, yellowCost);
            }

            maxTotalModelsProduced = 0; // 각 테스트 케이스 시작 시, 최대 생산 개수 초기화
            findMaxModels(0, 0); // 백트래킹 시작: 첫 번째 모델(인덱스 0)부터, 현재까지 0개 생산

            // 결과 출력
            System.out.println("#" + t + " " + maxTotalModelsProduced);
        }

        br.close(); // BufferedReader 닫기
    }

    /**
     * 백트래킹을 통해 만들 수 있는 최대 장식 조명 개수를 찾는 함수
     * @param modelIndex 현재 고려하고 있는 장식 조명 모델의 인덱스
     * @param currentTotalModels 현재까지 만든 장식 조명의 총 개수
     */
    public static void findMaxModels(int modelIndex, int currentTotalModels) {
        // 1. 기저 조건: 모든 모델을 다 고려했으면 (재귀 종료)
        if (modelIndex == models.length) {
            maxTotalModelsProduced = Math.max(maxTotalModelsProduced, currentTotalModels);
            return;
        }

        DecorativeLightModel currentModel = models[modelIndex];

        // 2. 현재 모델을 0개부터 'MAX_PRODUCTION_PER_MODEL_GLOBAL' 개수까지 만들어보기
        // 이 루프가 각 모델에 대해 0, 1, 2, 3개를 만들어보는 경우를 탐색합니다.
        for (int count = 0; count <= MAX_PRODUCTION_PER_MODEL_GLOBAL; count++) { 
            // 3. 현재 'count' 만큼 모델을 만들 수 있는지 전구 재고 확인
            boolean canMake = true;
            for (int i = 0; i < currentAvailableBulbs.length; i++) {
                // 필요한 전구 개수 = currentModel.costs[i] * count
                if (currentAvailableBulbs[i] < currentModel.costs[i] * count) {
                    canMake = false;
                    break;
                }
            }

            // 4. 만들 수 있다면
            if (canMake) {
                // 4-1. 전구 소모 (상태 변경): 재고에서 사용한 전구만큼 차감
                for (int i = 0; i < currentAvailableBulbs.length; i++) {
                    currentAvailableBulbs[i] -= currentModel.costs[i] * count;
                }

                // 4-2. 다음 모델로 재귀 호출: 다음 모델을 고려하고, 현재까지 생산된 총 개수를 업데이트
                findMaxModels(modelIndex + 1, currentTotalModels + count);

                // 4-3. 백트래킹 (상태 원상 복구): 다음 'count'를 시도하기 위해 소모했던 전구를 다시 추가
                for (int i = 0; i < currentAvailableBulbs.length; i++) {
                    currentAvailableBulbs[i] += currentModel.costs[i] * count;
                }
            }
            // 만약 'canMake'가 false이면, 현재 'count'로는 더 만들 수 없으므로, for 루프가 다음 'count'로 자동으로 넘어갑니다.
        }
    }
}

// 출력값
// #1 3
// #2 5
// #3 0
// #4 4
// #5 7
