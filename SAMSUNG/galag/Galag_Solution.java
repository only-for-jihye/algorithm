package galag;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Galag_Solution {

    static int N;
    static int[][] grid;
    static int[][][] dist;
    static int[] direction = {-1, 0, 1}; // 좌, 그대로, 우
    static final int MIN_INF = -1; // 미방문

    public static void main(String[] args) throws Exception {
//        FileInputStream fis = new FileInputStream("src/galag_sample.txt");
//        System.setIn(fis);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 1; t <= T; t++) {
            N = Integer.parseInt(br.readLine());
            grid = new int[N][5];
            dist = new int[N + 1][5][N + 2];

            for (int i = 0; i < N; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                for (int j = 0; j < 5; j++) {
                    grid[i][j] = Integer.parseInt(st.nextToken());
                }
            }
            int answer = bfs();
            System.out.println("#" + t + " " + answer);
        }
    }

    static int bfs() {
    	
        for (int i = 0; i <= N; i++) {
            for (int j = 0; j < 5; j++) {
                Arrays.fill(dist[i][j], MIN_INF);
            }
        }

        PriorityQueue<Galaxygrid> pq = new PriorityQueue<>();

        int startX = N;
        int startY = 2;
        int startCoin = 0;
        int startBombRow = -1; // 미사용

        dist[startX][startY][startBombRow + 1] = startCoin;
        pq.add(new Galaxygrid(startX, startY, startCoin, startBombRow));

        int maxCoin = 0;

        while (!pq.isEmpty()) {
            Galaxygrid current = pq.poll();
            int currentX = current.x;
            int currentY = current.y;
            int currentCoin = current.coin;
            int currentBombX = current.bombX;

            if (currentCoin < dist[currentX][currentY][currentBombX + 1]) {
                continue;
            }

            maxCoin = Math.max(maxCoin, currentCoin);

            if (currentX == 0) continue;

            int nX = currentX - 1;

            for (int i = 0; i < 3; i++) {
                int nY = currentY + direction[i];

                if (nY < 0 || nY >= 5) continue;

                boolean isDead = false;
                if (grid[nX][nY] == 2) {
                    if (currentBombX == -1) {
                        isDead = true;
                    } else { 
                        if (!(nX >= Math.max(0, currentBombX - 4) && nX <= currentBombX)) {
                            isDead = true;
                        }
                    }
                }

                if (!isDead) {
                    int getCoin = (grid[nX][nY] == 1) ? 1 : 0;
                    int totalCoin = currentCoin + getCoin;

                    if (totalCoin > dist[nX][nY][currentBombX + 1]) {
                        dist[nX][nY][currentBombX + 1] = totalCoin;
                        pq.add(new Galaxygrid(nX, nY, totalCoin, currentBombX));
                    }
                }

                if (currentBombX == -1) {
                    int newBombRow = currentX;
                    int getCoin = (grid[nX][nY] == 1) ? 1 : 0;
                    int totalCoin = currentCoin + getCoin;

                    if (totalCoin > dist[nX][nY][newBombRow + 1]) {
                        dist[nX][nY][newBombRow + 1] = totalCoin;
                        pq.add(new Galaxygrid(nX, nY, totalCoin, newBombRow));
                    }
                }
            }
        }
        return maxCoin;
    }
}

class Galaxygrid implements Comparable<Galaxygrid> {
    int x, y, coin;
    int bombX;

    public Galaxygrid(int x, int y, int coin, int bombX) {
        this.x = x;
        this.y = y;
        this.coin = coin;
        this.bombX = bombX;
    }

    @Override
    public int compareTo(Galaxygrid o) {
//    	return Integer.compare(this.coin, o.coin);
        return Integer.compare(o.coin, this.coin);
    }
}
