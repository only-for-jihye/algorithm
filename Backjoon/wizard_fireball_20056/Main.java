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

package Backjoon.wizard_fireball_20056;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("unchecked")
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
    }

    static int n;
    static int m;
    static int k;
    // static int[][] grid;
    static List<Fireball>[][] grid;
    static List<Fireball> fireball;
    static int[] dr = {-1, -1, 0, 1, 1, 1, 0, -1}; // 행 방향
    static int[] dc = {0, 1, 1, 1, 0, -1, -1, -1}; // 열 방향

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt(); // 격자 N
        m = sc.nextInt(); // 파이어볼의 개수
        k = sc.nextInt(); // K번 이동

        // grid = new int[n][n]; // 격자가 N X N인 배열
        grid = new ArrayList[n][n]; // 행과 열은 1번부터 시작하므로 +1을 주고, 파이어볼의 위치를 비교하기 위해 List형으로 선언

        fireball = new ArrayList<>(); // 파이어볼 생성

        for (int i = 0; i < m; i++) {
            int r = sc.nextInt() - 1; // 행
            int c = sc.nextInt() - 1; // 열
            int m = sc.nextInt(); // 질량
            int s = sc.nextInt(); // 속도
            int d = sc.nextInt(); // 방향
            fireball.add(new Fireball(r, c, m, d, s)); // 파이어볼 개수만큼 추가
        }

        for (int i = 0; i < k; i++) {
            move(); // 이동
            combAndDevide();
        }

        int answer = 0;
        for (Fireball fb : fireball) {
            answer += fb.m;
        }
        
        System.out.println(answer);
        // 이동 끝난 뒤 일어나는 일들, 합쳐지고 나뉘어지고
        
    }

    public static void move() {
        // 초기화
        grid = new ArrayList[n][n]; 
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = new ArrayList<>();
            }
        }
        for (Fireball fb : fireball) {
            // int moveR = fb.r + (dr[fb.d] * fb.s); // r행 : 방향 d로 속력 s만큼 이동
            // int moveC = fb.c + (dc[fb.d] * fb.s); // c열 : 방향 c로 속력 s만큼 이동
            // 원형 구조라 격자 N X N을 넘어서 index out of bounds 에러가 뜸
            // 원형 구조의 격자 이동 구조 공식 : (현재 좌표 + 이동 거리 % N + N) % N
            int moveR = (fb.r + (dr[fb.d] * fb.s) % n + n) % n;
            int moveC = (fb.c + (dc[fb.d] * fb.s) % n + n) % n;

            // Gemini
            // int actualMoveR = dr[fb.d] * fb.s;
            // int nextR = (fb.r + actualMoveR) % n;
            // if (nextR < 0) {
            //     nextR += n;
            // }

            // int actualMoveC = dc[fb.d] * fb.s;
            // int nextC = (fb.c + actualMoveC) % n;
            // if (nextC < 0) {
            //     nextC += n;
            // }

            // 이동한 파이어볼의 위치를 격자에 저장함
            grid[moveR][moveC].add(new Fireball(moveR, moveC, fb.m, fb.d, fb.s));
        }
    }

    public static void combAndDevide() {
        List<Fireball> nextFireballs = new ArrayList<>(); // 다음 턴의 파이어볼들을 저장할 임시 리스트
        // 파이어볼이 같은 칸에 있는지 확인 ?
        for (int i = 0; i < n; i++) { // 격자의 배열 탐색
            for (int j = 0; j < n; j++) {
                List<Fireball> fbInGrid = grid[i][j];
                if (fbInGrid.size() == 0) { // 0
                    continue;
                } else if (fbInGrid.size() == 1) { // 1
                    nextFireballs.add(fbInGrid.get(0));
                } else { // 2개 이상
                    // 모든 파이어볼은 하나로 합쳐진다
                    // 파이어볼은 4개의 파이어볼로 나누어진다
                    int sumM = 0;
                    int sumS = 0;
                    boolean allEven = true;
                    boolean allOdd = true;
                    for (Fireball fb : fbInGrid) {
                        sumM += fb.m; // 질량 합
                        sumS += fb.s; // 속력 합
                        if (fb.d % 2 == 0) {
                            allOdd = false;
                        } else {
                            allEven = false;
                        }
                    }
                    int newM = sumM / 5;
                    int newS = sumS / fbInGrid.size();
                    if (newM == 0) {
                        continue;
                    }
                    if (allEven || allOdd) {
                        nextFireballs.add(new Fireball(i, j, newM, 0, newS));
                        nextFireballs.add(new Fireball(i, j, newM, 2, newS));
                        nextFireballs.add(new Fireball(i, j, newM, 4, newS));
                        nextFireballs.add(new Fireball(i, j, newM, 6, newS));
                    } else {
                        nextFireballs.add(new Fireball(i, j, newM, 1, newS));
                        nextFireballs.add(new Fireball(i, j, newM, 3, newS));
                        nextFireballs.add(new Fireball(i, j, newM, 5, newS));
                        nextFireballs.add(new Fireball(i, j, newM, 7, newS));
                    }
                    // 질량 = 합쳐진 파이어볼의 질량 합 / 5
                    // 속력 = 합쳐진 파이어볼의 속력 합 / 합쳐진 파이어볼의 수
                    // 방향이 모두 홀수거나 짝수면 0, 2, 4, 6으로 나누어지고 // 아니면 1, 3, 5, 7
                    // 질량이 0인 파이어볼은 소멸되어 없어짐
                }
            }
        }
        // 모든 칸의 처리가 끝나면, 다음 턴의 파이어볼 리스트로 업데이트
        fireball = nextFireballs;
    }
}

