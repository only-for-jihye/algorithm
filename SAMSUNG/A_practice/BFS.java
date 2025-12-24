package A_practice;

import java.util.ArrayDeque;
import java.util.Queue;

public class BFS {

    // 문제에 주어진 최대 크기에 맞춰 설정 (예: 100)
    static final int MAX_N = 100;
    
    static int g_n; // 맵의 실제 크기 (init 등에서 설정되었다고 가정)
    static int[][] g_map = new int[MAX_N][MAX_N];
    static int[][] g_visited = new int[MAX_N][MAX_N];
    static int g_visitedCount = 0;
    
    // 상우하좌 델타 배열
    static int[][] g_mv = { {-1, 0}, {0, 1}, {1, 0}, {0, -1} };

    // 큐에 넣을 상태 정보 클래스 (C++ struct Info 대응)
    static class Info {
        int y, x;       // 행, 열
        int grade;      // 등급 (minHash)
        int count;      // 이동 횟수

        // 시작점용 생성자
        public Info(int y, int x, int grade) {
            this(y, x, grade, 0);
        }

        // 이동 중 생성자
        public Info(int y, int x, int grade, int count) {
            this.y = y;
            this.x = x;
            this.grade = grade;
            this.count = count;
        }
    }

    public boolean isValid(int minHash, int L, int sRow, int sCol, int eRow, int eCol) {
        // [최적화 핵심]
        // 매 BFS마다 visited 배열을 0으로 초기화(memset/loop)하면 O(N^2) 비용이 듦.
        // 대신 visitedCount를 1 증가시키고, 배열에 저장된 값이 현재 count와 같은지로 방문 체크를 함.
        // 이렇게 하면 초기화 비용이 O(1)이 됨.
        ++g_visitedCount;

        Queue<Info> q = new ArrayDeque<>();
        
        // 시작점 처리
        Info startInfo = new Info(sRow, sCol, minHash);
        g_visited[sRow][sCol] = g_visitedCount; // 방문 표시
        q.add(startInfo);

        while (!q.isEmpty()) {
            Info cur = q.poll();

            // 거리 제한 L에 도달했으면 더 이상 확장하지 않음
            // (단, 아래 로직에서 목적지 체크를 먼저 하므로, 여기서는 큐에서 뺀 시점이 L이면 다음 이동 불가 처리)
            if (cur.count == L) continue;

            for (int i = 0; i < 4; ++i) {
                int nextRow = cur.y + g_mv[i][0];
                int nextCol = cur.x + g_mv[i][1];

                // 1. 범위 벗어남 체크
                if (nextRow < 0 || nextCol < 0 || nextRow >= g_n || nextCol >= g_n) continue;
                
                // 2. 방문 여부 체크 (현재 회차의 visitedCount와 같은지 확인)
                if (g_visited[nextRow][nextCol] == g_visitedCount) continue;
                
                // 3. 이동 조건 체크 (현재 등급이 다음 칸보다 크면 이동 불가)
                if (cur.grade > g_map[nextRow][nextCol]) continue;

                // 4. 목적지 도착 확인 (이동 횟수가 L 미만일 때 도착하면 성공)
                // nextRow, nextCol로 이동하는 비용은 cur.count + 1임. 
                // 따라서 조건이 cur.count < L 이어야 함.
                if (cur.count < L && nextRow == eRow && nextCol == eCol) {
                    return true;
                }

                // 5. 다음 위치 큐에 삽입
                g_visited[nextRow][nextCol] = g_visitedCount; // 방문 처리
                q.add(new Info(nextRow, nextCol, minHash, cur.count + 1));
            }
        }

        return false;
    }
}