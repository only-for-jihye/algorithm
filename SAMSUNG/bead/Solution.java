// package 20250728;
/**
 * TC 값이 틀림 ...
 *  #1 4
    #2 3
    #3 4
    #4 5
    #5 5
 */

import java.io.FileInputStream;
import java.util.Scanner;

public class Solution {

    static int N;
    static int M;
    static int[] g;
    static int[] hole;
    static boolean[] visited;
    static int[] comb;
    static long f;

    static int answer;

    public static void main(String[] args) throws Exception {

        FileInputStream fis = new FileInputStream("sample.txt");
        System.setIn(fis);

        Scanner sc = new Scanner(System.in);

        int T = sc.nextInt();

        for (int i = 0; i < T; i++) {
            answer = 0;
            N = sc.nextInt();
            M = sc.nextInt();

            g = new int[N];
            visited = new boolean[N];
            for (int j = 0; j < N; j++) {
                g[j] = sc.nextInt();
                visited[j] = false;
            }

            hole = new int[M];
            for (int k = 0; k < M; k++) {
                hole[k] = sc.nextInt();
            }
            
            // comb = new int[M];
            // permutation(0);
            for (int m = Math.min(M, N); m >= 1; m--) {
                comb = new int[m];
                permutation(0, m);
                if (answer > 0) break;
            }

            System.out.println("#" + (i + 1) + " " + answer);

        }
        sc.close();
    }

    static void permutation(int k, int m) {
        if (k == m) {
            // 생성된 순열 출력
            // for (int num : comb) {
            //     System.out.print(num + " ");
            // }
            // System.out.println();

            // 여기서 계산, comb에 순열 조합이 담겨있음..
            if (calculate(comb)) {
                answer = m;
            }
            return;
        }

        for (int i = 0; i < N; i++) { 
            if (!visited[i]) {
                visited[i] = true;
                comb[k] = g[i];
                permutation(k + 1, m);
                // 이미 더 큰 개수로 해를 찾았거나, 현재 m개로 해를 찾았다면 더 이상 탐색할 필요 없음 (가지치기)
                if (answer == m) { // 또는 answer > 0 (현재 반복의 m보다 큰 answer가 이미 발견된 경우)
                    return; 
                }
                visited[i] = false;
            }
        }
    }

    static boolean calculate(int[] comb) {
        int distance = 0;
        f = 0;
        for (int i = 0; i < comb.length; i++) {
            for (int j = 0; j < comb.length; j++) {
                if (i == j) continue;
                // 구슬이 다음 구슬 뿐만 아니라, 모든 구슬끼리 탐색하도록
                // distance 확인
                // if (hole[i] > hole[j]) {
                //     distance = Math.abs(hole[i] - hole[j]);
                // } else if (hole[i] < hole[j]) {
                //     distance = Math.abs(hole[j] - hole[i]);
                // }
                distance = Math.abs(hole[i] - hole[j]);

                if (distance == 0) return false;
                
                if (hole[i] > hole[j]) {
                    if (comb[i] * comb[j] > 0) { // 척력
                        f += (comb[i] * comb[j]) / distance;
                    } else { // 인력
                        f -= (comb[i] * comb[j]) / distance;
                    }
                } else {
                    if (comb[i] * comb[j] > 0) { // 척력
                        f -= (comb[i] * comb[j]) / distance;
                    } else { // 인력
                        f += (comb[i] * comb[j]) / distance;
                    }
                }

            }
            // 힘을 계산
            // f += (comb[i + 1] * comb[i]) / distance;
            // System.out.println("f : " + f);
        }
        return f == 0;
    }



}
