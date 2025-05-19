/* ************************************************************************** */
/*                                                                            */
/*                                                      :::    :::    :::     */
/*   Problem Number: 20056                             :+:    :+:      :+:    */
/*                                                    +:+    +:+        +:+   */
/*   By: funjongsoo <boj.kr/u/funjongsoo>            +#+    +#+          +#+  */
/*                                                  +#+      +#+        +#+   */
/*   https://boj.kr/20056                          #+#        #+#      #+#    */
/*   Solved: 2025/05/18 21:07:01 by funjongsoo    ###          ###   ##.kr    */
/*                                                                            */
/* ************************************************************************** */

import java.util.*;

public class Main {

    public static class Fireball {
        int r; // 파이어볼 위치 행
        int c; // 파이어볼 위치 열
        int m; // 질량
        int d; // 방향
        int s; // 가속력

        Fireball(int r, int c, int m, int d, int s) {
            this.r = r;
            this.c = c;
            this.m = m;
            this.d = d;
            this.s = s;
        }
    }d

    static int n;
    static int m;
    static int k;
    static List<Fireball>[][] grid;
    static List<Fireball> fireball;
    static int[] dr = {-1, -1, 0, 1, 1, 1, 0, -1}; // 행 방향
    static int[] dc = {0, 1, 1, 1, 0, -1, -1, -1}; // 열 방향

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt(); // 격자가 N X N인 배열
        m = sc.nextInt(); // 파이어볼의 개수
        k = sc.nextInt(); // K번 이동

        grid = new ArrayList[n + 1][n + 1];
        fireball = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                grid[i][j] = new ArrayList<>();
            }
        }

        for (int i = 0; i < m; i++) {
            int r = sc.nextInt(); // 행
            int c = sc.nextInt(); // 열
            int m = sc.nextInt(); // 질량
            int s = sc.nextInt(); // 속도
            int d = sc.nextInt(); // 방향
            grid[r][c].add(new Fireball(r, c, m, d, s));
            fireball.add(new Fireball(r, c, m, d, s));
        }
        
        for (int l = 0; l < k; k++) {
            move();
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    if (grid[i][j].size() >= 2) {
                        combAndDevide(i, j, grid[i][j]);
                    }
                }
            }
            clean();
        }
        int answer = 0;
        for (Fireball ball : fireball) {
            answer += ball.m;
        }
        System.out.println(answer);
    }

    public static void clean() {
        fireball = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (grid[i][j].size() > 0) {
                    for (Fireball b : grid[i][j]) {
                        fireball.add(b);
                    }
                }
            }
        }
    }

    public static void move() {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                grid[i][j] = new ArrayList<>();
            }
        }

        for (Fireball fb : fireball) {
            int nr = fb.r + dr[fb.d] * (fb.s % n);
            int nc = fb.c + dc[fb.d] * (fb.s % n);
            if (nr <= 0) nr += n;
            if (nc <= 0) nc += n;
            if (nr > n) nr -= n;
            if (nc > n) nc -= n;

            fb.r = nr;
            fb.c = nc;
            grid[nr][nc].add(fb);
        }
    }

    public static void combAndDevide(int r, int c, List<Fireball> fireball) {
        int mSum = 0;
        int sSum = 0;
        boolean isEven = true;
        boolean isOdd = true;
        for (Fireball ball : fireball) {
            mSum += ball.m;
            sSum += ball.s;
            if (ball.d % 2 != 0) {
                isEven = false;
            } else {
                isOdd = false;
            }
        }
        int nm = mSum / 5;
        int ns = sSum / fireball.size();
        int[] dirs = {0, 2, 4, 6};
        if (!isOdd && !isEven) {
            dirs[0] = 1;
            dirs[1] = 3;
            dirs[2] = 5;
            dirs[3] = 7;
        }

        grid[r][c] = new ArrayList<>();
        if (nm <= 0) return;
        for (int d : dirs) {
            grid[r][c].add(new Fireball(r, c, nm, d, ns));
        }
    }
}

