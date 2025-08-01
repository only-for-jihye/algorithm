package SAMSUNG.clean_robot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Solution {

    static int N;
    static int currentFace; // 1:앞, 2:뒤, 3:오른쪽, 4:왼쪽, 5:윗면
    static int currentY, currentX; // 0-indexed coordinates on the current face
    static int currentDir; // 0:상(Up), 1:우(Right), 2:하(Down), 3:좌(Left)

    // dy, dx for movement based on currentDir
    static int[] dy = {-1, 0, 1, 0}; // Up, Right, Down, Left
    static int[] dx = {0, 1, 0, -1}; // Up, Right, Down, Left

    // Transition table: [current_face][exit_dir] -> {next_face, y_map_type, x_map_type, dir_rotation}
    // y_map_type, x_map_type:
    //   0: new_coord = old_coord
    //   1: new_coord = N-1 - old_coord
    //   2: new_coord = old_other_coord (e.g., new_y = old_x)
    //   3: new_coord = N-1 - old_other_coord (e.g., new_y = N-1 - old_x)
    // dir_rotation: how much the direction rotates when transitioning.
    //   0: no change
    //   1: 90 deg right turn (current_dir + 1)
    //   2: 180 deg (current_dir + 2)
    //   3: 90 deg left turn (current_dir + 3)
    static int[][][] transitions = new int[6][4][4]; // Max face 5, 4 directions, 4 values

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder commandsBuilder = new StringBuilder();
        while (st.hasMoreTokens()) {
            commandsBuilder.append(st.nextToken());
        }
        String commands = commandsBuilder.toString();

        // 초기 상태 (0-indexed)
        // 문제 예시: N=4일 때 세로 3, 가로 2 -> 0-indexed로 (y=2, x=1)
        currentFace = 1; // 항상 1번 유리면
        currentY = N - 2; // N=4일 때 2 (세로 3)
        currentX = N / 2 - 1; // N=4일 때 1 (가로 2)
        currentDir = 0; // 진행 방향은 위쪽

        // 전환 테이블 초기화
        initTransitions();

        // 명령어 수행
        for (char cmdChar : commands.toCharArray()) {
            int command = Character.getNumericValue(cmdChar);
            executeCommand(command);
        }

        // 최종 유리면 번호 출력
        System.out.println(currentFace);
    }

    private static void executeCommand(int command) {
        if (command == 1) { // 전진 (Forward)
            // 특수 규칙: 1,2,3,4번 면의 바닥(Y=N-1)에 위치하고 진행 방향이 아래(2)일 경우 전진 무시
            // 즉, (현재 면이 1~4이고, 방향이 아래이며, 현재 Y좌표가 면의 가장 아래(N-1)일 때)
            if ((currentFace >= 1 && currentFace <= 4) && currentDir == 2 && currentY == N - 1) {
                return; // 전진 명령 무시
            }

            int nextY = currentY + dy[currentDir];
            int nextX = currentX + dx[currentDir];

            // 현재 면을 벗어나는지 확인
            if (nextY < 0 || nextY >= N || nextX < 0 || nextX >= N) {
                // 면 전환 발생
                int exitDir = currentDir; // 현재 방향이 넘어가려는 가장자리 방향

                int[] transitionInfo = transitions[currentFace][exitDir];
                int nextFace = transitionInfo[0];
                int yMapType = transitionInfo[1];
                int xMapType = transitionInfo[2];
                int dirRotation = transitionInfo[3];

                // 새로운 면에서의 상대적 좌표 계산 (mappedY, mappedX는 임시 변환된 좌표)
                int mappedY = currentY;
                int mappedX = currentX;

                if (yMapType == 1) mappedY = N - 1 - mappedY;
                else if (yMapType == 2) mappedY = currentX; // old_x가 new_y가 됨
                else if (yMapType == 3) mappedY = N - 1 - currentX; // N-1 - old_x가 new_y가 됨

                if (xMapType == 1) mappedX = N - 1 - mappedX;
                else if (xMapType == 2) mappedX = currentY; // old_y가 new_x가 됨
                else if (xMapType == 3) mappedX = N - 1 - currentY; // N-1 - old_y가 new_x가 됨


                // 새로운 면의 가장자리에 착지하는 최종 좌표 결정
                int finalNewY = mappedY;
                int finalNewX = mappedX;

                // exitDir에 따라 새로운 면의 어느 가장자리로 진입하는지 결정
                if (exitDir == 0) { // 위로 넘어갔다면, 새 면의 맨 아래 줄(N-1)에 착지
                    finalNewY = N - 1;
                } else if (exitDir == 1) { // 오른쪽으로 넘어갔다면, 새 면의 맨 왼쪽 줄(0)에 착지
                    finalNewX = 0;
                } else if (exitDir == 2) { // 아래로 넘어갔다면, 새 면의 맨 위 줄(0)에 착지
                    finalNewY = 0;
                } else if (exitDir == 3) { // 왼쪽으로 넘어갔다면, 새 면의 맨 오른쪽 줄(N-1)에 착지
                    finalNewX = N - 1;
                }

                currentFace = nextFace;
                currentY = finalNewY;
                currentX = finalNewX;
                currentDir = (currentDir + dirRotation) % 4;

            } else { // 같은 면에 머무름
                currentY = nextY;
                currentX = nextX;
            }

        } else if (command == 2) { // 좌로 회전 (Turn Left)
            currentDir = (currentDir + 3) % 4;
        } else if (command == 3) { // 우로 회전 (Turn Right)
            currentDir = (currentDir + 1) % 4;
        }
    }

    // 면 전환 테이블 초기화 (큐브 전개도 및 방향 매핑에 따라 정의)
    private static void initTransitions() {
        // Face 1 (앞면)
        // 위 (0): (0,x) -> 윗면(5) (N-1,x). 방향 유지.
        transitions[1][0] = new int[]{5, 0, 0, 0};
        // 오른쪽 (1): (y,N-1) -> 오른쪽면(3) (y,0). 방향 유지.
        transitions[1][1] = new int[]{3, 0, 0, 0};
        // 왼쪽 (3): (y,0) -> 왼쪽면(4) (y,N-1). 방향 유지.
        transitions[1][3] = new int[]{4, 0, 0, 0};

        // Face 2 (뒷면)
        // 위 (0): (0,x) -> 윗면(5) (0, N-1-x). 방향 180도 회전.
        transitions[2][0] = new int[]{5, 0, 1, 2}; // 수정됨
        // 오른쪽 (1): (y,N-1) -> 왼쪽면(4) (N-1-y, 0). 방향 좌회전.
        transitions[2][1] = new int[]{4, 1, 0, 3};
        // 왼쪽 (3): (y,0) -> 오른쪽면(3) (N-1-y, N-1). 방향 우회전.
        transitions[2][3] = new int[]{3, 1, 0, 1};

        // Face 3 (오른쪽면)
        // 위 (0): (0,x) -> 윗면(5) (N-1-x, N-1). 방향 좌회전.
        transitions[3][0] = new int[]{5, 1, 3, 3}; // 수정됨
        // 오른쪽 (1): (y,N-1) -> 뒷면(2) (y,0). 방향 유지.
        transitions[3][1] = new int[]{2, 0, 0, 0};
        // 왼쪽 (3): (y,0) -> 앞면(1) (y,N-1). 방향 유지.
        transitions[3][3] = new int[]{1, 0, 0, 0};

        // Face 4 (왼쪽면)
        // 위 (0): (0,x) -> 윗면(5) (x, 0). 방향 우회전.
        transitions[4][0] = new int[]{5, 2, 0, 1}; // 수정됨
        // 오른쪽 (1): (y,N-1) -> 앞면(1) (y,0). 방향 유지.
        transitions[4][1] = new int[]{1, 0, 0, 0};
        // 왼쪽 (3): (y,0) -> 뒷면(2) (y,N-1). 방향 유지.
        transitions[4][3] = new int[]{2, 0, 0, 0};

        // Face 5 (윗면)
        // 위 (0): (0,x) -> 뒷면(2) (0, N-1-x). 방향 180도 회전.
        transitions[5][0] = new int[]{2, 0, 1, 2}; // 수정됨
        // 오른쪽 (1): (y,N-1) -> 오른쪽면(3) (N-1-y, 0). 방향 좌회전.
        transitions[5][1] = new int[]{3, 1, 0, 3}; // 수정됨
        // 아래 (2): (N-1,x) -> 앞면(1) (0, x). 방향 유지.
        transitions[5][2] = new int[]{1, 0, 0, 0};
        // 왼쪽 (3): (y,0) -> 왼쪽면(4) (y, N-1). 방향 우회전.
        transitions[5][3] = new int[]{4, 0, 0, 1}; // 수정됨
    }
}
