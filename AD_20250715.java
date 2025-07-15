// package 250715;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Solution {

    static int N; // 홀의 수
    static int[] holeDepths; // 홀의 깊이를 저장
    static List<Pilars> pilarsList; // 기둥 정보 배열
    static boolean[] visited; // 방문 체크
    static Pilars[] currentPilars; // 현재 선택한 기둥의 배열
    static int minOverallMaxDiff; // 최소 최대 차이값
    static int maxOverallConsecutiveColor; // 최대 연속 색상 개수
    
    public static void main(String[] args) {
        FileReader file = null;
        try {
            file = new FileReader("sample.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner sc = new Scanner(file);
        // Scanner sc = new Scanner(System.in);
        
        // TC 수
        int T = sc.nextInt();

        for (int i = 0; i < T; i++) {
            // 홀의 수
            N = sc.nextInt();
            holeDepths = new int[N];
            visited = new boolean[N];
            currentPilars = new Pilars[N];
            for (int j = 0; j < N; j++) {
                // 홀의 깊이
                holeDepths[j] = sc.nextInt();
            }
            pilarsList = new ArrayList<>();
            for (int k = 0; k < N; k++) {
                // 기둥 높이                
                Pilars p = new Pilars(sc.nextInt(), 0);
                pilarsList.add(p);
            }
            // System.out.println("pilarsList.size : " + pilarsList.size());
            for (int l = 0; l < N; l++) {
                // 기둥 색깔
                pilarsList.get(l).color = sc.nextInt();
            }
            
            minOverallMaxDiff = Integer.MAX_VALUE;
            maxOverallConsecutiveColor = 0;
            permutations(0); // 순열 시작
            System.out.println("#tc" + (i + 1) + " " + maxOverallConsecutiveColor);
        }

        sc.close();
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void permutations(int k) {
        // 순열 완성
        if (k == N) {
            // System.out.println(Arrays.toString(currentPilars));
            // 1. 인접한 기둥 높이 차이의 최댓값 계산
            int maxHeightDifference = 0;
            for (int i = 0; i < N; i++) {
                int p1Height = currentPilars[i].height - holeDepths[i];
                int p2Height = currentPilars[(i + 1) % N].height - holeDepths[(i + 1) % N];; // 원형 연결
                /*
                 * i -> index    : (i + 1) % N ..원형 연결
                 * 0 -> 1
                 * 1 -> 2
                 * 2 -> 3
                 * 3 -> 4
                 * 4 -> 5
                 * 5 -> 0
                 */
                // 바로 옆에 있는 기둥과의 차이를 구하고, max 값 비교해서 업데이트
                maxHeightDifference = Math.max(maxHeightDifference, Math.abs(p1Height - p2Height)); 
            }

            // 2. 같은 기둥 색깔이 연속하는 최대 길이 계산
            int[] colorsForConsecutiveCheck = new int[N];
            for(int j = 0; j < N; j++) {
                colorsForConsecutiveCheck[j] = currentPilars[j].color;
            }
            int currentConsecutiveColor = calculateMaxConsecutive(colorsForConsecutiveCheck);

            // 3. 최적의 순열 업데이트
            // 가장 작은 최대 높이 차이를 찾음
            // 현재 보고 있는 기둥 배치의 최대 높이 차이(maxHeightDifference)가, 지금까지 우리가 찾았던 가장 작은 최대 높이 차이(minOverallMaxDiff)보다 적다면
            if (maxHeightDifference < minOverallMaxDiff) { 
                minOverallMaxDiff = maxHeightDifference;
                maxOverallConsecutiveColor = currentConsecutiveColor;
            // 현재 보고 있는 기둥 배치의 최대 높이 차이(maxHeightDifference)가, 지금까지 우리가 찾았던 가장 작은 최대 높이 차이(minOverallMaxDiff)와 같다면
            } else if (maxHeightDifference == minOverallMaxDiff) {
                maxOverallConsecutiveColor = Math.max(maxOverallConsecutiveColor, currentConsecutiveColor); // 연속색깔의 수가 더 많은걸 택한다.
            }
            return;
        }
        // 재귀호출, 탐색
        for (int i = 0; i < N; i++) { // 순열 + 백트래킹
            if (!visited[i]) {
                visited[i] = true;
                currentPilars[k] = pilarsList.get(i);
                permutations(k + 1);
                visited[i] = false; // 백트래킹
            }
        }
    }

    static int calculateMaxConsecutive(int[] colors) {
        if (N == 0) return 0;
        if (N == 1) return 1;

        // 모든 기둥의 색깔이 동일한 경우
        boolean allSameColor = true;
        for (int i = 1; i < N; i++) {
            if (colors[i] != colors[0]) {
                allSameColor = false;
                break;
            }
        }
        // 모두 동일하면 그냥 N 반환
        if (allSameColor) {
            return N;
        }

        // 원형 배열의 연속을 쉽게 처리하기 위해 배열을 확장 (하나로 이어지게 끔..)
        // colorsList의 처음 N-1개 요소를 뒤에 추가하여 모든 랩어라운드 케이스를 커버
        int[] extendedColors = new int[N + N - 1];
        // System.arraycopy(colors, 0, extendedColors, 0, N);
        // System.arraycopy(colors, 0, extendedColors, N, N - 1);
        /* 
            public static native void arraycopy(
                                    Object src,     // 1. 원본 배열 (source array)
                                    int srcPos,     // 2. 원본 배열에서 복사를 시작할 위치 (source position)
                                    Object dest,    // 3. 대상 배열 (destination array)
                                    int destPos,    // 4. 대상 배열에서 복사를 시작할 위치 (destination position)
                                    int length      // 5. 복사할 요소의 개수 (number of elements to copy)
                                );
            System.arraycopy는 C/C++ 네이티브 코드로 구현되어 있어 훨씬 빠릅니다. 특히 배열 크기가 클 때 성능 차이가 많이 납니다.
         */
        // 첫 번째 N개의 요소 복사
        for (int i = 0; i < N; i++) {
            extendedColors[i] = colors[i];
        }

        // 다음 N-1개의 요소 (앞부분을 다시 복사)
        for (int i = 0; i < N - 1; i++) {
            extendedColors[N + i] = colors[i];
        }

        int maxConsecutiveLen = 0;
        int currentConsecutiveLen = 0;

        for (int i = 0; i < extendedColors.length; i++) {
            if (i == 0 || extendedColors[i] == extendedColors[i-1]) { // index가 0이거나 바로 옆과 동일하면 count++
                currentConsecutiveLen++;
            } else {
                currentConsecutiveLen = 1; // 아니면 1로 초기화
            }
            maxConsecutiveLen = Math.max(maxConsecutiveLen, currentConsecutiveLen);
        }
        return maxConsecutiveLen;
    }

    static class Pilars {
        int height; // 기둥 높이
        int color; // 기둥 색깔

        public Pilars (int height, int color) {
            this.height = height;
            this.color = color;
        }

        @Override
        public String toString() {
            return "Pilars{" +
                    "height=" + height +
                    ", color=" + color +
                    '}';
        }
    }

}
