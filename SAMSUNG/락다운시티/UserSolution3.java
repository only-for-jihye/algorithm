package 락다운시티;

import java.util.ArrayDeque;
import java.util.Queue;

class UserSolution3 {

	private int N, M;
	private int[][] map;
	
	// 상하좌우 방향 벡터
	private final int[] dr = {-1, 1, 0, 0};
	private final int[] dc = {0, 0, -1, 1};

	void init(int N, int M, String mGrade[][]) {
		this.N = N;
		this.M = M;
		this.map = new int[N][N];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				map[i][j] = getHashDicAsc(mGrade[i][j]);
			}
		}
	}

	void change(int mRow, int mCol, int mDir, int mLength, String mGrade) {
		int hash = getHashDicAsc(mGrade);

		if (mDir == 0) { // 행 증가 (세로 방향)
			for (int k = 0; k < mLength; k++) {
				if (mRow + k < N) {
					map[mRow + k][mCol] = hash;
				}
			}
		} else { // 열 증가 (가로 방향)
			for (int k = 0; k < mLength; k++) {
				if (mCol + k < N) {
					map[mRow][mCol + k] = hash;
				}
			}
		}
	}

	String calculate(int L, int sRow, int sCol, int eRow, int eCol) {
		// 매개 변수 탐색 (Parametric Search)
		// 해시값이 작을수록 보안등급이 높음(사전순 빠름), 해시값이 클수록 보안등급이 낮음(사전순 느림)
		// 목표: 이동 가능한 경로 중 '가장 낮은 보안등급'(가장 큰 해시값)을 찾음
		// 범위: 1 ~ ZZZ의 해시값
		
		int low = 1;
		int high = getHashDicAsc("ZZZ");
		int ans = 1;

		while (low <= high) {
			int mid = (low + high) / 2;

			// 결정 문제: 보안등급이 mid (해시값) 이상인 구역들만 밟아서 L회 안에 갈 수 있는가?
			// (즉, 기준보다 널널하거나 같은 구역들만 통과 가능)
			if (bfs(L, sRow, sCol, eRow, eCol, mid)) {
				ans = mid;
				low = mid + 1; // 가능하므로 더 높은(더 널널한) 등급도 시도
			} else {
				high = mid - 1; // 불가능하므로 기준을 낮춤
			}
		}

		return decodeHashDicAsc(ans);
	}

	// BFS 탐색
	private boolean bfs(int limit, int startIdx, int endIdx, int minGrade) {
        visitToken++;
        int head = 0;
        int tail = 0;

        visited[startIdx] = visitToken;
        queue[tail++] = (startIdx << 16); // dist는 0이므로 생략 가능

        while (head < tail) {
            int cur = queue[head++];
            int idx = cur >>> 16;
            int dist = cur & 0xFFFF;

            if (idx == endIdx) return true;
            if (dist >= limit) continue;

            int nDist = dist + 1;
            
            // 좌표 계산 없이 인덱스로만 이동
            // 상
            int nIdx = idx - N;
            if (idx >= N) { // r > 0 과 동일
                 if (visited[nIdx] != visitToken && map[nIdx] >= minGrade) {
                     visited[nIdx] = visitToken;
                     queue[tail++] = (nIdx << 16) | nDist;
                 }
            }
            // 하
            nIdx = idx + N;
            if (idx < (N - 1) * N) { // r < N-1 과 동일 (정확히는 idx / N < N - 1)
                 // 단, 1차원 배열 범위 체크로 대체 가능: nIdx < N * N
                 if (visited[nIdx] != visitToken && map[nIdx] >= minGrade) {
                     visited[nIdx] = visitToken;
                     queue[tail++] = (nIdx << 16) | nDist;
                 }
            }
            // 좌
            if (idx % N != 0) { // c > 0
                 nIdx = idx - 1;
                 if (visited[nIdx] != visitToken && map[nIdx] >= minGrade) {
                     visited[nIdx] = visitToken;
                     queue[tail++] = (nIdx << 16) | nDist;
                 }
            }
            // 우
            if ((idx + 1) % N != 0) { // c < N - 1
                 nIdx = idx + 1;
                 if (visited[nIdx] != visitToken && map[nIdx] >= minGrade) {
                     visited[nIdx] = visitToken;
                     queue[tail++] = (nIdx << 16) | nDist;
                 }
            }
        }
        return false;
    }

	// --- 제공된 해싱 함수 ---
	
	static int getHashDicAsc(String s) {
		int hash = 0;
		int p = 27 * 27 * 27; // 3자리~4자리 커버용 가중치
		for (int i = 0; i < s.length(); i++) {
			hash += (s.charAt(i) - 'A' + 1) * p;
			p /= 27;
		}
		return hash;
	}

	static String decodeHashDicAsc(int hash) {
		StringBuilder sb = new StringBuilder();
		int p = 27 * 27 * 27;

		while (p > 0) {
			int val = hash / p;
			if (val > 0) {
				sb.append((char) ('A' + val - 1));
			}
			hash %= p;
			p /= 27;
		}

		return sb.toString();
	}
}