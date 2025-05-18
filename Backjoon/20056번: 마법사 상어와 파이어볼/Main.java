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

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(); // 격자가 N X N인 배열
        int m = sc.nextInt(); // 파이어볼의 개수
        int k = sc.nextInt(); // K번 이동

        int[][] grid = new int[n][n]; // 격자

        List<Fireball> fireballList = new ArrayList<Fireball>();

        // 파이어볼 이동
        for (int i = 0; i < m; i++) {
            Fireball fireball = new Fireball();
            fireball.setR(sc.nextInt());
            fireball.setC(sc.nextInt());
            fireball.setM(sc.nextInt());
            fireball.setS(sc.nextInt());
            fireball.setD(sc.nextInt());
            
            move(fireball.getR()
                , fireball.getC()
                , fireball.getM()
                , fireball.getS()
                , fireball.getD());
            
            fireballList.add(fireball);
        }
        // 이동 완료 후, 2개 이상의 파이어볼이 있는 칸에서 ~~
        /**
         * 마법사 상어가 모든 파이어볼에게 이동을 명령하면 다음이 일들이 일어난다.
        모든 파이어볼이 자신의 방향 di로 속력 si칸 만큼 이동한다.
        이동하는 중에는 같은 칸에 여러 개의 파이어볼이 있을 수도 있다.
        이동이 모두 끝난 뒤, 2개 이상의 파이어볼이 있는 칸에서는 다음과 같은 일이 일어난다.
        같은 칸에 있는 파이어볼은 모두 하나로 합쳐진다.
        파이어볼은 4개의 파이어볼로 나누어진다.
        나누어진 파이어볼의 질량, 속력, 방향은 다음과 같다.
        질량은 ⌊(합쳐진 파이어볼 질량의 합)/5⌋이다.
        속력은 ⌊(합쳐진 파이어볼 속력의 합)/(합쳐진 파이어볼의 개수)⌋이다.
        합쳐지는 파이어볼의 방향이 모두 홀수이거나 모두 짝수이면, 방향은 0, 2, 4, 6이 되고, 그렇지 않으면 1, 3, 5, 7이 된다.
        질량이 0인 파이어볼은 소멸되어 없어진다.
        마법사 상어가 이동을 K번 명령한 후, 남아있는 파이어볼 질량의 합을 구해보자.
         */

    }

    void move(int r, int c, int d, int s) {
        // d 방향으로 s칸 만큼 이동
        switch (d) {
            case 0:
                r = r - (1 * s);
                break;
            case 1:
                r = r - (1 * s);
                c = c + (1 * s);
                break;
            case 2:
                c = c + (1 * s);
                break;
            case 3:
                r = r + (1 * s);
                c = c + (1 * s);
                break;
            case 4:
                r = r + (1 * s);
                break;
            case 5:
                r = r + (1 * s);
                c = c - (1 * s);
                break;
            case 6:
                c = c - (1 * s);
                break;
            case 7:
                r = r - (1 * s);
                c = c - (1 * s);
                break;
            default:
                break;
        }
    }
    return r, c;
}

class Fireball () {
    int r; // 파이어볼 위치 행
    int c; // 파이어볼 위치 열
    int m; // 질량
    int d; // 방향
    int s; // 가속력
    
    fireball(int r, int c, int m, int d, int s) {
        this.r = r;
        this.c = c;
        this.m = m;
        this.d = d;
        this.s = s;
    }

    public int getR() {
        return r;
    }

    public int getC() {
        return c;
    }
    
    public int getM() {
        return m;
    }

    public int getD() {
        return d;
    }

    public int getS() {
        return s;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setC(int c) {
        this.c = c;
    }

    public void setM(int m) {
        this.m = m;
    }

    public void setD(int d) {
        this.d = d;
    }

    public void setS(int s) {
        this.s = s;
    }

}