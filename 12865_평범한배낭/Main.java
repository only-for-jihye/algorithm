/* ************************************************************************** */
/*                                                                            */
/*                                                      :::    :::    :::     */
/*   Problem Number: 12865                             :+:    :+:      :+:    */
/*                                                    +:+    +:+        +:+   */
/*   By: funjongsoo <boj.kr/u/funjongsoo>            +#+    +#+          +#+  */
/*                                                  +#+      +#+        +#+   */
/*   https://boj.kr/12865                          #+#        #+#      #+#    */
/*   Solved: 2025/06/18 21:43:56 by funjongsoo    ###          ###   ##.kr    */
/*                                                                            */
/* ************************************************************************** */

import java.util.*;
import java.util.Scanner;

public class Main {

    static class goods {
        int w; // 각 물건의 무게
        int v; // 각 물건의 가치
        goods (int w, int v) {
            this.w = w;
            this.v = v;
        }
    }

    static int n; // 물건의 수
    // static int w; // 각 물건의 무게
    // static int v; // 각 물건의 가치
    static List<goods> goodsList;
    static int vSum; // 준서가 즐길 수 있는 가치의 합계
    static int k; // 준서가 가져갈 수 있는 최대 무게

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        n = sc.nextInt();
        k = sc.nextInt();
        goodsList = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int gw = sc.nextInt();
            int gv = sc.nextInt();
            goodsList.add(new goods(gw, gv));
        }
        
        // 가치의 최대값
        // 백트래킹 써야하나 ?

    }
}
