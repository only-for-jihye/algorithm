import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.util.Arrays;

public class Road_Solution2 {
	
	static int N;
	static int M;
	static int[][] grid;
	static int[][] dist;
	static int[] n_direction = {-1, 0, 1, 0}; // 상 우 하 좌
	static int[] m_direction = {0, 1, 0, -1};
	
	public static void main(String[] args) throws Exception {
//		FileInputStream fis = new FileInputStream("src/road_sample.txt");
//		System.setIn(fis);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		grid = new int[N][M];
		dist = new int[N][M];
		
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				grid[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		int totalMiminumCost = bfs();
		System.out.println("minCost = " + totalMiminumCost);
	}
	
	static int bfs() {

		for (int[] row : dist) {
			Arrays.fill(row, Integer.MAX_VALUE);
		}
		
		PriorityQueue<Grid> pq = new PriorityQueue<>();
		
		// 시작 지점 (0,0)
		if (grid[0][0] == -1) {
			return -1; // 시작부터 침수가 되면 바로 종료
		}
		
		dist[0][0] = grid[0][0];
		pq.add(new Grid(0, 0, dist[0][0]));
		
		while(!pq.isEmpty()) {
			Grid current = pq.poll();
			int nx = current.x;
			int ny = current.y;
			int currentCost = current.value; // 현재까지의 누적 비용
			
			// 현재까지의 비용이 이미 더 큰 값이라면 스킵 (최적화)
			if (currentCost > dist[nx][ny]) {
				continue;
			}
			
			// 목적지 도착
			if (nx == N - 1 && ny == M - 1) {
				return currentCost;
			}
			
			for (int i = 0; i < 4; i++) {
				int dx = nx + n_direction[i];
				int dy = ny + m_direction[i];
				
				if (dx >= 0 && dy >= 0 && dx < N && dy < M) {
					if (grid[dx][dy] == -1) { // 침수 pass
						continue;
					}
					
					int newCost = currentCost + grid[dx][dy];
					
					// 더 짧은 경로를 찾았을 경우 dist 업데이트 하고 큐에 추가
					if (newCost < dist[dx][dy]) {
						dist[dx][dy] = newCost;
						pq.add(new Grid(dx, dy, newCost));
					}
				}
			}
		}
		
		// 목적지에 도달할 수 없는 경우
		return dist[N - 1][M - 1] == Integer.MAX_VALUE ? -1 : dist[N - 1][M - 1];
	}
}

class Grid implements Comparable<Grid> {
	int x;
	int y;
	int value; // 이 필드를 누적 비용으로 사용

	public Grid(int x, int y, int value) {
		this.x = x;
		this.y = y;
		this.value = value;
	}

	@Override
	public int compareTo(Grid o) {
		// 누적 비용을 기준으로 오름차순 정렬
		return Integer.compare(this.value, o.value);
	}
}
