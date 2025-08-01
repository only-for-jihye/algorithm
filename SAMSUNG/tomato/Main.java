package SAMSUNG.tomato;/* ************************************************************************** */
/*                                                                            */
/*                                                      :::    :::    :::     */
/*   Problem Number: 7569                              :+:    :+:      :+:    */
/*                                                    +:+    +:+        +:+   */
/*   By: funjongsoo <boj.kr/u/funjongsoo>            +#+    +#+          +#+  */
/*                                                  +#+      +#+        +#+   */
/*   https://boj.kr/7569                           #+#        #+#      #+#    */
/*   Solved: 2025/04/20 22:08:45 by funjongsoo    ###          ###   ##.kr    */
/*                                                                            */
/* ************************************************************************** */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    static int[][][] boxes;
    static ArrayList<Tomato> iktomato = new ArrayList<>();
    static int m, n, h;
    
    public static void main(String[] args) throws IOException {
        // Scanner sc = new Scanner(System.in);
        // int m = sc.nextInt();
        // int n = sc.nextInt();
        // int h = sc.nextInt();
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(input.readLine(), " ");
        m = Integer.parseInt(st.nextToken());
        n = Integer.parseInt(st.nextToken());
        h = Integer.parseInt(st.nextToken());

        // h 개의 n x m 배열을 생성한다.
        boxes = new int[h][n][m];

        // 배열에 값 넣기
        for(int i = 0; i < h; i++) {
            for(int j = 0; j < n; j++) {
                st = new StringTokenizer(input.readLine(), " ");
                for(int k = 0; k < m; k++) {
                    // 토마토의 상태 배열 저장
                    boxes[i][j][k] = Integer.parseInt(st.nextToken());
                    // 익은 토마토 위치 저장
                    if(boxes[i][j][k] == 1) {
                        iktomato.add(new Tomato(i, j, k));
                    }
                }
            }
        }
        
        // int result = bfs() - 1;
        // for(int i = 0; i < h; i++) {
        //     for(int j = 0; j < n; j++) {
        //         for(int k = 0; k < m; k++) {
        //             if(boxes[i][j][k] == 0) {
        //                 result = -1;
        //                 break;
        //             }
        //         }
        //     }
        // }
        // System.out.println(result);
        System.out.println(bfs());
    }

    static int bfs() {
        // 초기화
        int z = 0, y = 0, x = 0;
        // 위,아래,좌,앞,우,뒤 순서로 움직이는 좌표 (6개 방향))
        int[] mz = {1, -1, 0, 0, 0, 0};
        int[] my = {0, 0, 0, 1, 0, -1};
        int[] mx = {0, 0, -1, 0, 1, 0};

        // 익은 토마토의 좌표를 Queue에 넣는다.
        Queue<Tomato> que = new LinkedList<>();
        for(int i = 0; i < iktomato.size(); i++) {
            que.offer(iktomato.get(i));
        }

        int day = 0;
        
        // bfs 탐색
        while(!que.isEmpty()) {
            // 익은 토마토 위치 꺼내기
            Tomato tot = que.poll();
            z = tot.getZ();
            y = tot.getY();
            x = tot.getX();

            // 6개 방향 탐색
            for(int i = 0; i < 6; i++) {
                // 좌표 이동
                int dz = z + mz[i];
                int dy = y + my[i];
                int dx = x + mx[i];

                // 배열을 벗어나지 않게 처리
                if(dz >= h || dy >= n || dx >= m ||
                    dz < 0 || dy < 0 || dx < 0) {
                        continue;
                    }
                
                // 안익은 토마토가 있으면 익혀야하므로 큐에 넣기
                if(boxes[dz][dy][dx] == 0) {
                    que.offer(new Tomato(dz, dy, dx));
                    // +1씩 누적하여 마지막에 익은 토마토의 일 수를 구한다
                    boxes[dz][dy][dx] = boxes[z][y][x] + 1;
                }
            }
        }
        
        // 마지막에 익은 토마토의 일 수에서 -1을 뺀다. 익기 시작한 날짜가 1일부터니까.. 걸린 날짜 수는 -1일이 되어야함
        day = boxes[z][y][x] - 1;

        // 아직 안익은 토마토가 있는지 확인 -> 토마토가 모두 익지 못하는 상황이면 -1을 출력
        for(int i = 0; i < h; i++) {
            for(int j = 0; j < n; j++) {
                for(int k = 0; k < m; k++) {
                    if(boxes[i][j][k] == 0) {
                        day = -1;
                        break;
                    }
                }
            }
        }
        return day;
    }
}
    
class Tomato {
    private int z, y, x;

    public Tomato(int z, int y, int x) {
        this.z = z;
        this.y = y;
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
}
