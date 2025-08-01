package AD_20240710;

import java.io.FileInputStream;
import java.util.*;

public class Solution2 {

    static int W;
    static int H;
    static int N;
    static int Xh;
    static int Yh;
    static int Xc;
    static int Yc;
    static int result;

    // 겹치는 x, y 좌표 그룹들을 저장할 리스트
    // 각 리스트의 요소는 접는 횟수마다 생성되는 좌표 그룹을 담는다.
    static List<List<Integer>> foldedXGroups;
    static List<List<Integer>> foldedYGroups;

    // 최종적으로 뚫리는 모든 구멍들을 저장하는 리스트
    static List<Point> allHoles;

    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream("AD_20240710/sample.txt");
        System.setIn(fis);

        Scanner sc = new Scanner(System.in);

        int T = sc.nextInt();

        for (int i = 1; i <= T; i++) {
            N = sc.nextInt();
            W = sc.nextInt();
            H = sc.nextInt();

            // 테스트 케이스마다 전역 변수 초기화
            foldedXGroups = new ArrayList<>();
            foldedYGroups = new ArrayList<>();
            allHoles = new ArrayList<>();
            result = 0;

            for (int j = 0; j < N; j++) {
                int d = sc.nextInt(); // 방향 0 또는 1
                int f = sc.nextInt(); // f번 접는다
                fold(d, f);
            }

            Yh = sc.nextInt(); // 구멍 뚫는 y위치
            Xh = sc.nextInt(); // 구멍 뚫는 x위치
            Yc = sc.nextInt(); // 찾는 y위치
            Xc = sc.nextInt(); // 찾는 x위치

            // Xh, Yh로 구멍을 뚫는다
            hole();

            search(); // Xc, Yc가 몇 번째 구멍인지 찾기

            System.out.println("#" + i + " " + result);

            // debugging
            // for (Card c : cardList) {
            //     System.out.println(c.toString());
            // }
        }

        sc.close();
    }

    // 방향 d로 f번 접어서 겹치는 y,x 좌표끼리 하나의 list를 만든다.
    static void fold(int d, int f) {

    }

    // Yh와 Xh 값으로 y, x 위치에 구멍을 뚫는다. 그리고 Yh, Xh 값이 있는 list의 모든 구멍을 뚫는다.
    static void hole() {

    }

    // 구멍을 뚫은 list를 행 우선으로 정렬하여 Yc, Xc 위치가 몇번째인지 찾는다.
    static void search() {

    }

    static class Point {
        int x;
        int y;
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

}
