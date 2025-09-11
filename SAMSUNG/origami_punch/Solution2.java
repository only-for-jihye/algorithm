package origami_punch;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Solution2 {

    static int W;
    static int H;
    static int N;
    static int punchX;
    static int punchY;
    static int findX;
    static int findY;
    static Node punch;
    static Node find;
    static ArrayList<Paper> papers;
    static Paper paper;
    static ArrayList<Node> holes;

    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream("SAMSUNG/origami_punch/sample2.txt");
        System.setIn(fis);
        Scanner sc = new Scanner(System.in);

        int T = sc.nextInt();

        for (int t = 1; t <= T; t++) {
            N = sc.nextInt();
            W = sc.nextInt();
            H = sc.nextInt();

            // 초기화
            papers = new ArrayList<>();
            holes = new ArrayList<>();
            paper = new Paper(new Node(0, 0), 0, 0);

            for (int l = 0; l < N; l++) { // N번 접으니까 반복
                int d = sc.nextInt(); // 방향 0 또는 1
                int r = sc.nextInt(); // r번 접는다
                // 종이를 접어보자
                getFoldPaper(d, r);
            }

            // papers 디버깅용 출력해보기
//            for (Paper paper : papers) {
//                System.out.println(paper.toString());
//            }

            punchX = sc.nextInt(); // 구멍 뚫는 x위치
            punchY = sc.nextInt(); // 구멍 뚫는 y위치
            punch = new Node(punchY, punchX);
            findX = sc.nextInt(); // 찾는 x위치
            findY = sc.nextInt(); // 찾는 y위치
            find = new Node(findY, findX);

            // 전체 구멍 구하기
            holes.add(punch);
            getHole();

            // 정렬
            Collections.sort(holes);

            // 결과 출력
            System.out.println("#"+(t+1)+" "+getResult());
        }
        sc.close();
    }

    private static int getResult() {
        for(int i = 0; i < holes.size(); ++i) {
            Node hole = holes.get(i);
            if(hole.y == find.y && hole.x == find.x) {
                return i + 1;
            }
        }
        return 0;
    }

    private static void getHole() {
        for (int i = papers.size() - 1; i >= 0; i--) {
            int max = holes.size();

            Paper current = papers.get(i);
            if(current.dir == 0) {
                for(int j = 0; j < max; ++j) {
                    Node hole = holes.get(j);

                    int dis = Math.abs(current.point.x - hole.x);
                    if(dis>current.range) continue;

                    int ny = hole.y;
                    int nx = current.point.x - (dis-1);
                    holes.add(new Node(ny,nx));
                }
            } else {
                for(int j = 0; j < max; ++j) {
                    Node hole = holes.get(j);

                    int dis = Math.abs(current.point.y - hole.y);
                    if(dis>current.range) continue;

                    int ny = current.point.y - (dis-1);
                    int nx = hole.x;
                    holes.add(new Node(ny,nx));
                }
            }
        }
    }

    private static void getFoldPaper(int d, int r) {
        int ny = paper.point.y;
        int nx = paper.point.x;
        if (d == 0) { // 수평으로 접는다면 가로 W에 r값만큼 추가하여 시작점 조정
            nx += r;
        } else { // 수직으로 접는다면 세로 H에 r값만큼 추가하여 시작점 조정
            ny += r;
        }
        // 접은 종이 추가할 때, 방향과 몇칸인지도 같이 추가
        paper = new Paper(new Node(ny, nx), d, r);
        papers.add(paper);
    }

    static class Paper {
        Node point;
        int dir, range;

        public Paper(Node point, int dir, int range) {
            this.point = point;
            this.dir = dir;
            this.range = range;
        }

        @Override
        public String toString() {
            return "Paper{" +
                    "point=" + point +
                    ", dir=" + dir +
                    ", range=" + range +
                    '}';
        }
    }

    static class Node implements Comparable<Node> {
        int x, y;

        public Node(int y, int x) {
            this.y = y;
            this.x = x;
        }

        @Override
        public int compareTo(Node o) {
            return this.y == o.y ? this.x - o.x : this.y - o.y;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

}
