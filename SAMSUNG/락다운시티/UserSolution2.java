package 락다운시티;

import java.util.ArrayDeque;
import java.util.Queue;

class UserSolution2 {

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
	private boolean bfs(int L, int sRow, int sCol, int eRow, int eCol, int limitHash) {
		Queue<int[]> q = new ArrayDeque<>();
		boolean[][] visited = new boolean[N][N];

		// {행, 열, 이동횟수}
		q.offer(new int[] {sRow, sCol, 0});
		visited[sRow][sCol] = true;

		while (!q.isEmpty()) {
			int[] curr = q.poll();
			int r = curr[0];
			int c = curr[1];
			int dist = curr[2];

			// 목적지 도착
			if (r == eRow && c == eCol) {
				return true;
			}

			// 횟수 제한 초과 시 이동 불가
			if (dist >= L) continue;

			for (int i = 0; i < 4; i++) {
				int nr = r + dr[i];
				int nc = c + dc[i];

				if (nr >= 0 && nr < N && nc >= 0 && nc < N && !visited[nr][nc]) {
					// 조건: 다음 구역의 해시값이 limitHash 이상이어야 함
					// (출발 구역은 검사에서 제외되므로 큐에 넣을 때 검사하지 않고, 다음 구역 진입 시 검사)
					if (map[nr][nc] >= limitHash) {
						visited[nr][nc] = true;
						q.offer(new int[] {nr, nc, dist + 1});
					}
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