package Bulbs;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class AD_250611 {

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


// findMaxModels 함수에서 첫 번째 모델을 0개 만들어보고 바로 다음 모델을 고려하는 이유는 모든 가능한 조합을 빠짐없이 탐색하기 위함입니다. 
// 이는 백트래킹(Backtracking) 알고리즘의 핵심적인 작동 방식입니다.

// 좀 더 자세히 설명해 드릴게요.

// 모든 경우의 수를 탐색해야 하니까
// 우리의 목표는 가장 많은 장식 조명을 생산하는 방법을 찾는 겁니다. 이 최댓값을 찾으려면, 가능한 모든 시나리오를 고려해봐야 해요.

// 예를 들어, 모델 A, B, C가 있다고 해봅시다.

// 시나리오 1: 모델 A를 0개 만들고, 남은 전구로 모델 B, C를 최대로 만들어보는 경우
// 시나리오 2: 모델 A를 1개 만들고, 남은 전구로 모델 B, C를 최대로 만들어보는 경우
// 시나리오 3: 모델 A를 2개 만들고, 남은 전구로 모델 B, C를 최대로 만들어보는 경우
// 시나리오 4: 모델 A를 3개 만들고, 남은 전구로 모델 B, C를 최대로 만들어보는 경우
// 이 4가지 시나리오 중에서 어떤 것이 최종적으로 가장 많은 조명을 만들 수 있는 조합인지 우리는 미리 알 수 없습니다.

// 만약 모델 A가 전구를 너무 많이 소모해서, 모델 A를 1개라도 만들면 다른 모델들을 아예 만들 수 없게 될 수도 있어요. 이 경우, 모델 A를 0개 만드는 시나리오가 최고의 결과를 낼 수도 있겠죠.
// 반대로, 모델 A가 아주 효율적이어서 3개를 만드는 것이 다른 모델과 조합했을 때 더 많은 총 개수를 낼 수도 있습니다.
// 그래서 백트래킹은 "가장 좋은 선택일 거야"라고 섣불리 가정하지 않고, 현재 모델을 0개 만드는 것부터 시작해서 가능한 최대 개수(3개)까지 모두 시도해봅니다. 각 시도(count)마다 마치 "이번 길로 가볼까?" 하고 결정을 내린 뒤, 그 결정에 따라 전구를 소모하고 나머지 모델들에 대해서도 동일한 방식으로 탐색을 진행합니다.

// 재귀의 본질
// findMaxModels 함수는 for (int count = 0; ...) 루프를 통해 이 모든 시나리오를 반복적으로 탐색해요.

// count = 0일 때:

// 현재 모델을 0개 만든다고 가정합니다.
// 전구 소모는 없습니다.
// 재귀 호출: findMaxModels(modelIndex + 1, currentTotalModels) (다음 모델을 고려하러 갑니다.)
// 이 재귀 호출이 끝난 후에는, "아, 0개 만들었을 때의 경로는 다 탐색해봤다!" 하고 돌아와서 다음 시도(count = 1)를 준비합니다.
// count = 1일 때:

// 현재 모델을 1개 만든다고 가정합니다.
// 전구를 1개 만들 분량만큼 소모합니다.
// 재귀 호출: findMaxModels(modelIndex + 1, currentTotalModels + 1) (다음 모델을 고려하러 갑니다.)
// 이 재귀 호출이 끝난 후에는, "아, 1개 만들었을 때의 경로도 다 탐색해봤다!" 하고 돌아와서 다음 시도(count = 2)를 준비합니다.
// 이렇게 각 count 값에 대해 하나의 완전한 탐색 경로를 시작하고, 그 경로가 끝날 때(모든 모델을 다 고려했을 때) 현재까지의 maxTotalModelsProduced와 비교하는 거죠.

// 결론적으로, 0개부터 시도하는 것은 모든 경우의 수를 놓치지 않고 탐색하여 진정한 최댓값을 찾기 위한 백트래킹 알고리즘의 필수적인 부분입니다.


// findMaxModels 함수는 재귀적 백트래킹 방식으로 작동하기 때문에, 각 모델에 대해 0개부터 가능한 최대 개수(3개)까지 순차적으로 시도하고, 그 선택 후 남은 전구와 함께 다음 모델로 넘어갑니다.

// 따라서, 시나리오 1: "모델 A를 0개 만들고, B와 C를 최대로 만들어볼 때" 의 내부 탐색 과정은 다음과 같이 전개됩니다.

// findMaxModels 탐색의 트리 구조
// 시작: findMaxModels(modelIndex = 0, currentTotalModels = 0) (초기 전구 재고)

// 1. modelIndex = 0 (모델 A 고려)
// 현재 선택: 모델 A를 0개 만들기

// 전구 소모: 없음.
// 재귀 호출: findMaxModels(modelIndex = 1, currentTotalModels = 0) (이제 모델 B를 고려합니다. 현재까지 0개 만듦)
// 1.1. modelIndex = 1 (모델 B 고려 - A는 0개 만든 상태)
// 현재 선택: 모델 B를 0개 만들기

// 전구 소모: 없음.
// 재귀 호출: findMaxModels(modelIndex = 2, currentTotalModels = 0) (이제 모델 C를 고려합니다. 현재까지 0개 만듦)
// 1.1.1. modelIndex = 2 (모델 C 고려 - A는 0개, B는 0개 만든 상태)
// 현재 선택: 모델 C를 0개 만들기

// 전구 소모: 없음.
// 재귀 호출: findMaxModels(modelIndex = 3, currentTotalModels = 0)
// modelIndex == models.length (3 == 3) 이므로 기저 조건 도달!
// maxTotalModelsProduced = Math.max(현재_최댓값, 0) 업데이트.
// return; (모델 C의 count = 0 탐색 끝, 모델 B의 count = 0으로 돌아감)
// 백트래킹 (모델 C): 전구 원상 복구.

// 현재 선택: 모델 C를 1개 만들기

// 전구 소모: 모델 C 1개 분량.
// 재귀 호출: findMaxModels(modelIndex = 3, currentTotalModels = 1)
// 기저 조건 도달!
// maxTotalModelsProduced = Math.max(현재_최댓값, 1) 업데이트.
// return;
// 백트래킹 (모델 C): 전구 원상 복구.

// ... (모델 C를 2개 만들기 시도)

// ... (모델 C를 3개 만들기 시도)

// findMaxModels(modelIndex = 2, ...) 함수 종료. findMaxModels(modelIndex = 1, ...)으로 돌아감.

// 백트래킹 (모델 B): 전구 원상 복구.

// 현재 선택: 모델 B를 1개 만들기

// 전구 소모: 모델 B 1개 분량.
// 재귀 호출: findMaxModels(modelIndex = 2, currentTotalModels = 1) (이제 모델 C를 고려합니다. 현재까지 1개 만듦)
// 1.2.1. modelIndex = 2 (모델 C 고려 - A는 0개, B는 1개 만든 상태)
// 현재 선택: 모델 C를 0개 만들기

// ...
// 기저 조건 도달! (currentTotalModels = 1)
// 백트래킹 (모델 C):

// 현재 선택: 모델 C를 1개 만들기

// ...
// 기저 조건 도달! (currentTotalModels = 2)
// 백트래킹 (모델 C):

// ... (모델 C를 2개 만들기 시도)

// ... (모델 C를 3개 만들기 시도)

// findMaxModels(modelIndex = 2, ...) 함수 종료. findMaxModels(modelIndex = 1, ...)으로 돌아감.

// 백트래킹 (모델 B): 전구 원상 복구.

// ... (모델 B를 2개 만들기 시도)

// ... (모델 B를 3개 만들기 시도)

// findMaxModels(modelIndex = 1, ...) 함수 종료. findMaxModels(modelIndex = 0, ...)으로 돌아감.

// 백트래킹 (모델 A): 전구 원상 복구.

// 결론
// 예, 맞습니다!

// "모델 A를 0개 만들고, B와 C를 최대로 만들어볼 때" 라는 시나리오가 있다면, findMaxModels의 재귀 호출과 for 루프의 중첩 덕분에 내부적으로는 A=0, B=0, C=0, A=0, B=0, C=1, A=0, B=0, C=2, A=0, B=0, C=3 의 경로를 먼저 탐색한 다음, A=0, B=1, C=0, A=0, B=1, C=1, ... 이런 식으로 모든 가능한 서브 조합들을 빠짐없이 탐색하게 됩니다.

// 이러한 깊이 우선 탐색(DFS) 방식의 백트래킹을 통해, 모든 끝점(기저 조건)에 도달했을 때의 총 생산 개수를 기록하고, 그 중 최댓값을 찾는 것이 이 알고리즘의 작동 원리입니다.
