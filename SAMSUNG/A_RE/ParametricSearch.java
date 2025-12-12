package A_RE;

public class ParametricSearch {

	// mid 값이 조건을 만족하는지 판별하는 로직 (문제에 따라 구현 필요)
    public boolean isValid(int mid) {
        // ... 로직 구현 ...
        // 예: if (calculation(mid) >= target) return true;
        return true; 
    }

    /*
     * Case 1: 조건을 만족하는 [최소값] 찾기 (Lower Bound)
     * 예: "모두에게 나눠줄 수 있는 최소 예산", "조건을 만족하는 가장 작은 크기"
     */
    public int parametricSearchMin(int low, int high) {
        int answer = -1; // 답을 찾지 못했을 경우의 기본값

        while (low <= high) {
            // (low + high)가 int 범위를 넘을 수 있다면 long을 쓰거나
            // low + (high - low) / 2 방식을 권장합니다.
            int mid = (low + high) / 2;

            if (isValid(mid)) {
                answer = mid;    // 일단 현재 값은 정답 후보임 (기록)
                high = mid - 1;  // "최소값"을 찾아야 하므로 더 작은 범위를 탐색
            } else {
                low = mid + 1;   // 조건 불만족 -> 값을 키워야 함
            }
        }
        return answer;
    }

    /*
     * Case 2: 조건을 만족하는 [최대값] 찾기 (Upper Bound 변형)
     * 예: "공유기 설치 거리의 최대값", "가져갈 수 있는 보석의 최대 개수"
     * (주석에 언급하신 반대 로직입니다)
     */
    public int parametricSearchMax(int low, int high) {
        int answer = -1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (isValid(mid)) {
                answer = mid;   // 일단 현재 값은 정답 후보임 (기록)
                low = mid + 1;  // "최대값"을 찾아야 하므로 더 큰 범위를 탐색
            } else {
                high = mid - 1; // 조건 불만족 -> 값을 줄여야 함
            }
        }
        return answer;
    }
    
}
