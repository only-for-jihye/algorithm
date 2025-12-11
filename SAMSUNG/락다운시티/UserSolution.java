package 락다운시티;

//UserSolution.java

class UserSolution {
	
	// 2025.11.21. 락다운시티
	// @admin_deukwha
	// parametric search + bfs
	
	int n; 
	int[][] map = new int[101][101]; 
	int m; // 보안등급 최대길이 (1~3)
	int[][] visited = new int[101][101]; 
	int cc;
	int[] dy = {-1, 1, 0, 0};
	int[] dx = {0, 0, -1, 1};
	int MIN;
	int MAX;
	
	// ** GC의 동작 최적화를 위해 queue를 배열로 사용 
	int[] qy = new int[10001]; // queue 
	int[] qx = new int[10001];
	int[] qd = new int[10001];
	int fr, re; 
	
	int hashFunction(String grade) {
		int hash = 0;
		for(int i = 0; i < m; i++) {
			if(i >= grade.length()) hash = hash * 27;
			else hash = hash * 27 + (grade.charAt(i) - 'A' + 1);
		}
		return hash;
	}
	
	boolean test(int limit, int y, int x, int ey, int ex, int cnt) {
		fr = re = 0;
		qy[re] = y;
		qx[re] = x;
		qd[re++] = 0; 
		
		visited[y][x] = ++cc;
		while(fr < re) {
			int nowY = qy[fr];
			int nowX = qx[fr];
			int nowD = qd[fr++]; 
			if(nowY == ey && nowX == ex) return true;
			int manhattan = Math.abs(nowY - ey) + Math.abs(nowX - ex);
            if (manhattan > cnt - nowD) continue;
			for(int i = 0; i < 4; i++) {
				int ny = nowY + dy[i];
				int nx = nowX + dx[i];
				if(ny < 0 || nx < 0 || ny >= n || nx >= n) continue;
				if(visited[ny][nx] == cc) continue;
				if(map[ny][nx] < limit) continue;
				if(nowD + 1 > cnt) continue;
				visited[ny][nx] = cc;
				qy[re] = ny;
				qx[re] = nx;
				qd[re++] = nowD + 1; 
			}
		}
		return false; 
	}
	
	String ps(int cnt, int y, int x, int ey, int ex) {
		int left = MIN; 
		int right = MAX; 
		while(left <= right) {
			int mid = (left + right) / 2; 
			if(test(mid, y, x, ey, ex, cnt)) left = mid + 1; 
			else right = mid - 1; 
		}
		StringBuilder sb = new StringBuilder();
        while (right > 0) {
            int mod = right % 27;
            if (mod > 0) sb.append((char) ('A' + mod - 1));
            right /= 27;
        }
        return sb.reverse().toString();
	}
	
	void init(int N, int M, String mGrade[][]) {
		n = N;
		m = M;
		MIN = Integer.MAX_VALUE;
		MAX = Integer.MIN_VALUE;
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) { 
				map[i][j] = hashFunction(mGrade[i][j]);
				MIN = Math.min(map[i][j], MIN);
				MAX = Math.max(map[i][j], MAX); 
				visited[i][j] = 0; 
			}
		}
		cc = 0; 
	}

	// 10,000 
	void change(int mRow, int mCol, int mDir, int mLength, String mGrade) {
		int v = hashFunction(mGrade); 
		MIN = Math.min(v, MIN);
		MAX = Math.max(v, MAX); 
		if(mDir == 0) for(int i = 0; i < mLength; i++) map[mRow+i][mCol] = v;
		else for(int i = 0; i < mLength; i++) map[mRow][mCol+i] = v; 
	}

	// 200
	// BFS : 100 x 100
	// parametric : log(262626) 
	String calculate(int L, int sRow, int sCol, int eRow, int eCol) {
		return ps(L, sRow, sCol, eRow, eCol); 
	}
}