/* ************************************************************************** */
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
import java.util.List;
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
        r = sc.nextInt();
        c = sc.nextInt();
        k = sc.nextInt();
        divs = new Hot[r][c];

        for(int i = 0; i < r; i++) {
            for(int j = 0; j < c; j++) {
                int input = sc.nextInt();
                int isHot = 0, temp = 0;
                boolean result = false;
                if (input == 5) {
                    result = true;
                }
                if (input >= 1 && input <= 4) {
                    isHot = input;
                }
                divs[i][j] = new Hot(isHot, temp, result);
            }
        }

        w = sc.nextInt();
        wall = new boolean[r][c][2];

        for (int i = 0; i < w; i++) {
            int wallX = sc.nextInt() - 1;
            int wallY = sc.nextInt() - 1;
            int wallT = sc.nextInt(); // 위쪽 = 0, 오른쪽 = 1
            // divs[x][y].block = true;

            if (wallT == 0) {
                wall[wallX][wallY][0] = true; // 위쪽
            } else {
                wall[wallX][wallY][1] = true; // 오른쪽
            }
        }

        // 시작
        int choco = 0; // 초코
        while (choco <= 100) {
            for(int i = 0; i < r; i++) {
                for(int j = 0; j < c; j++) {
                    // 1. 온풍기 찾아서 바람 한번 나옴
                    int direction = divs[i][j].isHot;
                    if (direction != 0) {
                        start(i, j, direction);
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
                for(int y = 0; y < c; y++) {
                    if (divs[x][y].result == true && divs[x][y].temp < k) {
                        okay = false;
                        break;
                    }
                }
                if (!okay) break;
            }
            if (okay) {
                System.out.println(choco);
                return;
            }
        }
        // 100번 돌렸는데도 없으면;;
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
                break; // 오른쪽
            case 2:
                j--;
                break; // 왼쪽
            case 3:
                i--;
                break; // 위쪽
            case 4:
                i++;
                break; // 아래쪽
        }

        // 시작
        if (i >= 0 && i < r && j >= 0 && j < c) { 
            divs[i][j].temp += 5; // 첫칸 5도 증가
            visited[i][j] = true; // 온도 올림
            q.offer(new int[]{i, j, 1}); // 처음 넣음
        }

        while (!q.isEmpty()) {
            int[] current = q.poll();
            int currentX = current[0];
            int currentY = current[1];
            int distance = current[2];

            if (distance >= 5) {
                continue;
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
        // 상 우 하 좌
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, 1, 0, -1};
        // 동시에 발생하려면, 배열에 넣어둬야하나?
        int[][] temp = new int[r][c];

        for (int x = 0; x < r; x++) {
            for (int y = 0; y < c; y++) {
                for (int d = 0; d < 4; d++) {
                    int nx = x + dx[d];
                    int ny = y + dy[d];

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
                            int calc = divs[x][y].temp - divs[nx][ny].temp;
                            if (calc > 0) {
                                int devide = calc / 4;
                                temp[x][y] -= devide;
                                temp[nx][ny] += devide;
                            } // 다른 한쪽이 크면?.. 어차피 다음 계산에서 다음 셀이 계산함
                        }
                    }
                }
            }
        }
        for (int x = 0; x < r; x++) {
            for (int y = 0; y < c; y++) {
                divs[x][y].temp += temp[x][y];
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