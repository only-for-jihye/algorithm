package A_practice;

import java.util.Arrays;

public class FloydWarshall {

	// C++의 #define INF 1e9 대응
    // Integer.MAX_VALUE를 사용하면 더하기 연산 시 오버플로우가 발생해 음수가 될 수 있으므로,
    // 적당히 큰 값(10억)을 사용합니다.
    static final int INF = 1_000_000_000;
    
    // 정점 개수(V)에 맞춰 배열 선언 (300 이하 가정)
    // 1-based indexing을 위해 301로 설정
    static int[][] dist = new int[301][301];

    public static void floydWarshall(int n) {
        // 1. 초기화 로직은 보통 main이나 init 함수에서 수행됩니다.
        // (알고리즘 핵심 로직 시작)
        
        // k: 거쳐가는 노드 (가장 바깥쪽 루프여야 함이 핵심!)
        for (int k = 1; k <= n; k++) {
            // i: 출발 노드
            for (int i = 1; i <= n; i++) {
                // j: 도착 노드
                for (int j = 1; j <= n; j++) {
                    
                    // i -> k -> j 로 가는 길이 끊겨있지 않은지 확인 (INF 체크)
                    // 기존 i -> j 보다 거쳐가는 길(i -> k -> j)이 더 빠르면 갱신
                    if (dist[i][k] != INF && dist[k][j] != INF) {
                        dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                    }
                }
            }
        }
    }

    // 테스트를 위한 메인 함수 (초기화 예시 포함)
    public static void main(String[] args) {
        int n = 5; // 정점의 개수 예시

        // 배열 초기화 단계 (중요)
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (i == j) {
                    dist[i][j] = 0; // 자기 자신으로 가는 비용은 0
                } else {
                    dist[i][j] = INF; // 나머지는 무한대로 초기화
                }
            }
        }

        // 여기에 간선 정보 입력 (예: 1에서 2로 가는 비용이 4)
        // dist[1][2] = 4;
        // dist[2][1] = 4; // 양방향일 경우

        // 알고리즘 실행
        floydWarshall(n);
    }
	
}
