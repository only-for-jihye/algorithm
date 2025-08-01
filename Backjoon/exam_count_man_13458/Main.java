package Backjoon.exam_count_man_13458;/* ************************************************************************** */
/*                                                                            */
/*                                                      :::    :::    :::     */
/*   Problem Number: 13458                             :+:    :+:      :+:    */
/*                                                    +:+    +:+        +:+   */
/*   By: funjongsoo <boj.kr/u/funjongsoo>            +#+    +#+          +#+  */
/*                                                  +#+      +#+        +#+   */
/*   https://boj.kr/13458                          #+#        #+#      #+#    */
/*   Solved: 2025/06/18 21:09:23 by funjongsoo    ###          ###   ##.kr    */
/*                                                                            */
/* ************************************************************************** */

import java.util.Scanner;

public class Main {

    static int N; // 시험장의 수
    static int[] A; // 각 시험장별 응시자의 수
    static int B; // 총 감독관이 감시할 수 있는 응시자의 수 (각 시험장에 총 감독관은 오직 1명)
    static int C; // 부감독관이 한 시험장에서 감시할 수 있는 응시자의 수
    // static int count; // 총 감독의 수
    static long count;

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);

        N = sc.nextInt();
        A = new int[N];

        for (int i = 0; i < N; i++) {
            A[i] = sc.nextInt();
        }

        B = sc.nextInt();
        C = sc.nextInt();

        // 최솟값 구하기
        count = 0;
        minimum();
        System.out.println(count);

    }

    static void minimum() {

        for (int i = 0; i < N; i++) {
            A[i] -= B; // 총 감독관이 감시할 수 있는 수만큼 각 시험장에서 인원 빼기
            count++; // 총 감독관은 시험장마다 1명이므로 시험장의 수 만큼 더하기
        }

        for (int i = 0; i < N; i++) {
            if (A[i] <= 0) continue;
            int divide = A[i] / C;
            int nanugi = A[i] % C;
            if (nanugi != 0) {
                divide++;
            }
            count += divide;
        }

    }

}
