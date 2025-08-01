package Backjoon.warm_air_fan_23289;/* ************************************************************************** */
/*                                                                            */
/*                                                      :::    :::    :::     */
/*   Problem Number: 23289                             :+:    :+:      :+:    */
/*                                                    +:+    +:+        +:+   */
/*   By: funjongsoo <boj.kr/u/funjongsoo>            +#+    +#+          +#+  */
/*                                                  +#+      +#+        +#+   */
/*   https://boj.kr/23289                          #+#        #+#      #+#    */
/*   Solved: 2025/06/06 12:51:54 by funjongsoo    ###          ###   ##.kr    */
/*                                                                            */
/* ************************************************************************** */

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {

    static class Hot {
        int isHot; // 온풍기 방향, 0이면 온풍기 없음
        int temp; // 현재 온도, 0부터 시작
        boolean result; // 마지막 조건
        Hot (int isHot, int temp, boolean result) {
            this.isHot = isHot;
            this.temp = temp;
            this.result = result;
        }

        @Override
        public String toString() {
            return "Hot{isHot=" + isHot + ", temp=" + temp + ", result=" + result + '}';
        }
    }

    static Hot[][] divs;
    static int r, c, k;
    static int w;
    static int[] dx, dy;
    // static int x, y, t;
    static boolean[][][] wall; // [x][y][t]
    

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        r = sc.nextInt(); // 행
        c = sc.nextInt(); // 열
        k = sc.nextInt(); // 마지막 검사 (모든 칸의 온도가 k이상인지 확인)
        divs = new Hot[r][c]; // 각 칸의 온풍기가 있는지, 있으면 방향이 어딘지, 그리고 온도, 마지막 조건 결과 등을 담는다.

        for(int i = 0; i < r; i++) {
            for(int j = 0; j < c; j++) {
                int input = sc.nextInt(); // 입력값 받기
                int isHot = 0, temp = 0; // 초기화
                boolean result = false; // 마지막 조건 결과
                if (input == 5) {
                    result = true; // 5를 입력받으면 마지막 조건 대상
                }
                if (input >= 1 && input <= 4) {
                    isHot = input; // 온풍기 방향 세팅
                }
                divs[i][j] = new Hot(isHot, temp, result);
            }
        }

        w = sc.nextInt(); // 벽의 개수
        wall = new boolean[r][c][2]; // 벽의 위치를 담는다.

        for (int i = 0; i < w; i++) {
            int wallX = sc.nextInt() - 1; // 0-based
            int wallY = sc.nextInt() - 1; // 0-based
            int wallT = sc.nextInt(); // 위쪽 벽 = 0, 오른쪽 벽 = 1
            // divs[x][y].block = true;

            if (wallT == 0) {
                wall[wallX][wallY][0] = true; // 위쪽
            } else {
                wall[wallX][wallY][1] = true; // 오른쪽
            }
        }

        // 시작
        int choco = 0; // 초코 개수 시작
        while (choco <= 100) { // 초코가 100개가 될 때까지만 돌림
            // 1. 온풍기 가동
            for(int i = 0; i < r; i++) {
                for(int j = 0; j < c; j++) {
                    // 1. 온풍기 찾아서 바람 한번 나옴
                    int direction = divs[i][j].isHot;
                    if (direction != 0) { // 0은 빈칸
                        start(i, j, direction); // 온풍기 틀기 시작, 모든 온풍기가 돌때까지 돌려야한다.
                    }
                }
            }

            // 2. 온도 조절
            controlTemperture();
            // 3. 바깥 온도 감소
            for(int x = 0; x < r; x++) {
                for(int y = 0; y < c; y++) {
                    if (x == 0 || y == 0 || x == r - 1 || y == c - 1) {
                        if (divs[x][y].temp > 0) {
                            divs[x][y].temp -= 1;
                        }
                    }
                }
            }
            // 4. 초코 먹방
            choco++;
            // 5. 온도 검사
            boolean okay = true;
            for(int x = 0; x < r; x++) {
                for(int y = 0; y < c; y++) { // 모든 칸 검사

                    if (divs[x][y].result == true && divs[x][y].temp < k) {
                        okay = false;
                        break; // 마지막 조건 검사 대상이지만 하나라도 온도가 k만큼 안올랐으면 상위 for문으로 이동
                    }

                }
                if (!okay) break; // for문 탈출해서 다시 while 구문 시작
            }

            if (okay) { // 초코가 100개가 안됐지만 모든 칸 만족 시...
                System.out.println(choco);
                return; // while 빠져나오기
            } 
        }
        // 그리고 초코 100개 이상 먹었지만 모든 칸이 만족이 안됐으면 ?
        System.out.println(101);
        
        

        // // 검사
        // for(int i = 0; i < r; i++) {
        //     for(int j = 0; j < c; j++) {
        //         System.out.println("(" + i + "," + j + ") : " + divs[i][j].toString());
        //     }
        //     System.out.println(" ");
        // }
    }


    public static void start(int i, int j, int direction) {
        Queue<int[]> q = new LinkedList<>();
        boolean[][] visited = new boolean[r][c];
        
        switch (direction) {
            case 1:
                j++;
                break; // 오른쪽으로 이동
            case 2:
                j--;
                break; // 왼쪽으로 이동
            case 3:
                i--;
                break; // 위쪽으로 이동
            case 4:
                i++;
                break; // 아래쪽으로 이동
        }

        // 시작
        if (i >= 0 && i < r && j >= 0 && j < c) { 
            divs[i][j].temp += 5; // 첫칸은 무조건 5도 증가함
            visited[i][j] = true; // 방문 완료 - 온도 올림
            q.offer(new int[]{i, j, 1}); // 큐에 처음 넣음 (행, 열, 거리)
        }

        while (!q.isEmpty()) { // 처음 이동한 칸(큐에 처음 넣음)을 기준으로 시작
            int[] current = q.poll();
            int currentX = current[0]; // 행
            int currentY = current[1]; // 열
            int distance = current[2]; // 거리 측정

            if (distance >= 5) {
                continue; // 거리가 5 이상이면 종료
            }

            int nextTemperture = 5 - distance; // 거리 1이 늘어날때마다 온도는 1 감소 (5 - 4 - 3 - 2 - 1)

            // 이동
            // 방향 먼저
            if (direction == 1) { // 오른쪽
                // 1. 오른쪽 직진
                int nx = currentX;
                int ny = currentY + 1;
                // 격자를 벗어나지 않고, 방문하지 않은 곳이며, 현재 기준으로 오른쪽으로 이동하는데 오른쪽에 벽이 없어야 함
                if (nx >= 0 && nx < r && ny >= 0 && ny < c && !visited[nx][ny] && !wall[currentX][currentY][1]) {
                    divs[nx][ny].temp += nextTemperture;
                    visited[nx][ny] = true;
                    q.offer(new int[]{nx, ny, distance + 1});
                }
                // 2. 대각선 위
                // 경유지
                nx = currentX - 1;
                ny = currentY;
                // 이동할 곳
                int moveX = currentX - 1;
                int moveY = currentY + 1;
                // 경유지는 격자를 벗어나지 않고, 현재 기준으로 위로 이동하는데 윗벽이 없어야 함
                if (moveX >= 0 && moveX < r && moveY >= 0 && moveY < c && 
                    nx < r && ny >= 0 && ny < c && 
                    !visited[moveX][moveY] && nx >= 0 &&
                    !wall[currentX][currentY][0] && 
                    !wall[nx][ny][1]) {
                        divs[moveX][moveY].temp += nextTemperture;
                        visited[moveX][moveY] = true;
                        q.offer(new int[]{moveX, moveY, distance + 1});
                }

                // 3. 대각선 아래
                // 경유지
                nx = currentX + 1;
                ny = currentY;
                // 이동할 곳
                moveX = currentX + 1;
                moveY = currentY + 1;
                // 경유지에서 목적지로 가는 조건, 경유지의 위쪽 벽이 없어야됌
                if (moveX >= 0 && moveX < r && moveY >= 0 && moveY < c &&
                    nx >= 0 && nx < r && ny >= 0 && ny < c &&
                    !visited[moveX][moveY] && 
                    !wall[nx][ny][0] && 
                    !wall[nx][ny][1]) {
                        divs[moveX][moveY].temp += nextTemperture;
                        visited[moveX][moveY] = true;
                        q.offer(new int[]{moveX, moveY, distance + 1});

                }
            } else if (direction == 2) { // 왼쪽
                // 1. 왼쪽 직진
                int nx = currentX;
                int ny = currentY - 1;
                // 격자를 벗어나지 않고, 방문하지 않은 곳이며, 현재 기준으로 왼쪽으로 이동하는데 목적지의 오른쪽 벽이 없어야됨
                if (nx >= 0 && nx < r && ny >= 0 && ny < c && !visited[nx][ny] && !wall[nx][ny][1]) {
                    divs[nx][ny].temp += nextTemperture;
                    visited[nx][ny] = true;
                    q.offer(new int[]{nx, ny, distance + 1});
                }
                // 2. 대각선 위
                // 경유지
                nx = currentX - 1;
                ny = currentY;
                // 이동할 곳
                int moveX = currentX - 1;
                int moveY = currentY - 1;
                // 경유지는 격자를 벗어나지 않고, 현재 기준으로 위로 이동하는데 윗벽이 없어야 함
                if (moveX >= 0 && moveX < r && moveY >= 0 && moveY < c &&
                    nx >= 0 && nx < r && ny >= 0 && ny < c &&
                    !visited[moveX][moveY] &&
                    !wall[currentX][currentY][0] &&
                    !wall[moveX][moveY][1]) {
                        divs[moveX][moveY].temp += nextTemperture;
                        visited[moveX][moveY] = true;
                        q.offer(new int[]{moveX, moveY, distance + 1});
                    }
                // 3. 대각선 아래
                // 경유지
                nx = currentX + 1;
                ny = currentY;
                // 이동할 곳
                moveX = currentX + 1;
                moveY = currentY - 1;
                // 경유지에서 목적지로 가는 조건, 경유지의 위쪽 벽이 없어야됌
                if (nx >= 0 && nx < r && ny >= 0 && ny < c && 
                    moveX >= 0 && moveX < r && moveY >= 0 && moveY < c && 
                    !visited[moveX][moveY] &&
                    !wall[nx][ny][0] &&
                    !wall[moveX][moveY][1]) {
                        divs[moveX][moveY].temp += nextTemperture;
                        visited[moveX][moveY] = true;
                        q.offer(new int[]{moveX, moveY, distance + 1});
                    }
            } else if (direction == 3) { // 위쪽
                // 1. 위쪽 직진
                int nx = currentX - 1;
                int ny = currentY;
                // 격자를 벗어나지 않고, 방문하지 않은 곳이며, 현재 기준으로 위로 이동하는데 현재의 위쪽 벽이 없어야함
                if (nx >= 0 && nx < r && ny >= 0 && ny < c && !visited[nx][ny] && !wall[currentX][currentY][0]) {
                    divs[nx][ny].temp += nextTemperture;
                    visited[nx][ny] = true;
                    q.offer(new int[]{nx, ny, distance + 1});
                }
                // 2. 대각선 왼쪽 방향
                // 경유지
                nx = currentX;
                ny = currentY - 1;
                // 이동할 곳
                int moveX = currentX - 1;
                int moveY = currentY - 1;
                // 경유지는 격자를 벗어나지 않고, 현재 기준으로 위로 이동하는데 윗벽이 없어야 함
                if (nx >= 0 && nx < r && ny >= 0 && ny < c &&
                    moveX >= 0 && moveX < r && moveY >= 0 && moveY < c &&
                    !visited[moveX][moveY] &&
                    !wall[nx][ny][0] &&
                    !wall[nx][ny][1]) {
                        divs[moveX][moveY].temp += nextTemperture;
                        visited[moveX][moveY] = true;
                        q.offer(new int[]{moveX, moveY, distance + 1});
                    }
                // 3. 대각선 오른쪽 방향
                // 경유지
                nx = currentX;
                ny = currentY + 1;
                // 이동할 곳
                moveX = currentX - 1;
                moveY = currentY + 1;
                // 경유지에서 목적지로 가는 조건, 현재의 위쪽 벽이 없어야함
                if (nx >= 0 && nx < r && ny >= 0 && ny < c && 
                    moveX >= 0 && moveX < r && moveY >= 0 && moveY < c &&    
                    !visited[moveX][moveY] && 
                    !wall[currentX][currentY][1] &&
                    !wall[nx][ny][0]) {
                        divs[moveX][moveY].temp += nextTemperture;
                        visited[moveX][moveY] = true;
                        q.offer(new int[]{moveX, moveY, distance + 1});
                }
            } else if (direction == 4) { // 아래쪽
                // 1. 아래쪽 직진
                int nx = currentX + 1;
                int ny = currentY;
                // 격자를 벗어나지 않고, 방문하지 않은 곳이며, 현재 기준으로 아래로 이동하는데, 목적지의 위쪽 벽이 없어야됌
                if (nx >= 0 && nx < r && ny >= 0 && ny < c && !visited[nx][ny] && !wall[nx][ny][0]) {
                    divs[nx][ny].temp += nextTemperture;
                    visited[nx][ny] = true;
                    q.offer(new int[]{nx, ny, distance + 1});
                }
                // 2. 대각선 왼쪽 방향
                // 경유지
                nx = currentX;
                ny = currentY - 1;
                // 이동할 곳
                int moveX = currentX + 1;
                int moveY = currentY - 1;
                // 경유지는 격자를 벗어나지 않고, 현재 기준으로 위로 이동하는데 윗벽이 없어야 함
                if (nx >= 0 && nx < r && ny >= 0 && ny < c && 
                    moveX >= 0 && moveX < r && moveY >= 0 && moveY < c && 
                    !visited[moveX][moveY] &&
                    !wall[nx][ny][1] && 
                    !wall[moveX][moveY][0]) {
                        divs[moveX][moveY].temp += nextTemperture;
                        visited[moveX][moveY] = true;
                        q.offer(new int[]{moveX, moveY, distance + 1});
                }
                // 3. 대각선 오른쪽 방향
                // 경유지
                nx = currentX;
                ny = currentY + 1;
                // 이동할 곳
                moveX = currentX + 1;
                moveY = currentY + 1;
                // 경유지에서 목적지로 가는 조건, 현재의 위쪽 벽이 없어야함
                if (nx >= 0 && nx < r && ny >= 0 && ny < c && 
                    moveX >= 0 && moveX < r && moveY >= 0 && moveY < c &&
                    !visited[moveX][moveY] && 
                    !wall[currentX][currentY][1] &&
                    !wall[moveX][moveY][0]) {
                        divs[moveX][moveY].temp += nextTemperture;
                        visited[moveX][moveY] = true;
                        q.offer(new int[]{moveX, moveY, distance + 1});
                }
            }
        }
    }

    private static void controlTemperture() {
        // 온도가 높은 칸에서 낮은 칸으로 두 칸의 온도 차이 / 4만큼 온도가 조절됨 ?
        // 상 우 하 좌, 온도 방향은 대각선 없음
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, 1, 0, -1};
        // 동시에 발생하려면, 새로운 임시 배열을 만들고, 나중에 합산
        int[][] temp = new int[r][c]; // 0으로 이루어진 임시 배열

        for (int x = 0; x < r; x++) {
            for (int y = 0; y < c; y++) {
                for (int d = 0; d < 4; d++) {
                    int nx = x + dx[d]; // 행 방향 이동
                    int ny = y + dy[d]; // 열 방향 이동

                    if (nx >= 0 && nx < r && ny >= 0 && ny < c) {
                        // 벽 확인해야되는뎅..
                        // // 기준 열에서 위쪽 이동
                        // if (wall[x][y][0]) continue;
                        // // 아래 이동
                        // if (wall[x + 1][y][1]) continue;
                        // // 좌로 이동
                        // if (wall[x - 1][y][1]) continue;
                        // // 우로 이동
                        // if (wall[x][y][1]) continue;
                        if (!isWall(x, y, nx, ny)) {
                            // 온도 차이
                            int calc = divs[x][y].temp - divs[nx][ny].temp; // 소수점 절삭
                            if (calc > 0) { // 온도는 높은 칸에서 낮은 칸으로 이동함
                                int devide = calc / 4;
                                temp[x][y] -= devide; // 0부터 시작해서 값을 계속 누적
                                temp[nx][ny] += devide; // 0부터 시작해서 값을 계속 누적
                            } // calc < 0을 계산하지 않는 이유는, 어차피 다음 칸에서 계산이 됨, 만약 이 조건을 넣게 되면, 인접한 칸까지 중복 계산이 되어버림
                        }
                    }
                }
            }
        }
        for (int x = 0; x < r; x++) {
            for (int y = 0; y < c; y++) {
                divs[x][y].temp += temp[x][y]; // 기존 온도 + 변화된 온도를 더해줌
            }
        }
    }

    public static boolean isWall(int x, int y, int nx, int ny) {
        // 위로 이동할 때
        if (nx == x - 1 && ny == y) {
            return wall[x][y][0];
        } else if (nx == x + 1 && ny == y) { // 아래 
            return wall[nx][y][0];
        } else if (nx == x && ny == y - 1) { // 좌
            return wall[x][ny][1];
        } else if (nx == x && ny == y + 1) { // 우
            return wall[x][y][1];
        }
        return false;
    }


}
