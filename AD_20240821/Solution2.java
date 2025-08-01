package AD_20240821;

import java.io.FileInputStream;
import java.util.Scanner;

public class Solution2 {

    static int N;
    static int M;
    static int R;
    static boolean[][] connect_city;

    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream("AD_20240821/sample.txt");
        System.setIn(fis);
        Scanner sc = new Scanner(System.in);

        int T = sc.nextInt(); // 테스트 케이스

        for (int i = 1; i <= T; i++) {
            N = sc.nextInt(); // 도시
            M = sc.nextInt(); // 이미 있는 철로
            R = sc.nextInt(); // 추가로 건설할 수 있는 철도
            connect_city = new boolean[N + 1][N + 1];
            // 초기화
            for (int k = 1; k <= N; k++) {
                connect_city[k][k] = false;
            }

            for (int j = 0; j < M; j++) {
                int start = sc.nextInt();
                int end = sc.nextInt();
                connect_city[start][end] = true;
            }

            for (int k = 1; k <= N; k++) {
                System.out.println(connect_city[k][k]);
            }
        }
    }
}
