// package 250715;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Solution {

    static int N;
    static int[] depths;
    static List<Pilars> pilarsList;
    static boolean[] visited;
    static Pilars[] currentPilars;
    static int minOverallMaxDiff;
    static int maxOverallConsecutiveColor;
    
    public static void main(String[] args) {
        FileReader file = null;
        try {
            file = new FileReader("sample.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Scanner sc = new Scanner(System.in);
        Scanner sc = new Scanner(file);
        
        // TC 수
        int T = sc.nextInt();

        for (int i = 0; i < T; i++) {
            // 홀의 수
            N = sc.nextInt();
            depths = new int[N];
            visited = new boolean[N];
            currentPilars = new Pilars[N];
            for (int j = 0; j < N; j++) {
                // 홀의 깊이
                depths[j] = sc.nextInt();
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
            permutations(0);
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
                int p1Height = currentPilars[i].height;
                int p2Height = currentPilars[(i + 1) % N].height; // 원형 연결
                maxHeightDifference = Math.max(maxHeightDifference, Math.abs(p1Height - p2Height));
            }

            // 2. 같은 기둥 색깔이 연속하는 최대 길이 계산
            int[] colorsForConsecutiveCheck = new int[N];
            for(int j = 0; j < N; j++) {
                colorsForConsecutiveCheck[j] = currentPilars[j].color;
            }
            int currentConsecutiveColor = calculateMaxConsecutive(colorsForConsecutiveCheck, N);

            // 3. 최적의 순열 업데이트
            if (maxHeightDifference < minOverallMaxDiff) {
                minOverallMaxDiff = maxHeightDifference;
                maxOverallConsecutiveColor = currentConsecutiveColor;
            } else if (maxHeightDifference == minOverallMaxDiff) {
                maxOverallConsecutiveColor = Math.max(maxOverallConsecutiveColor, currentConsecutiveColor);
            }
            return;
        }
        // 재귀호출, 탐색
        for (int i = 0; i < N; i++) {
            if (!visited[i]) {
                visited[i] = true;
                currentPilars[k] = pilarsList.get(i);
                permutations(k + 1);
                visited[i] = false; // 백트래킹
            }
        }
    }

    static int calculateMaxConsecutive(int[] colors, int N) {
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

        if (allSameColor) {
            return N;
        }

        // 원형 배열의 연속을 쉽게 처리하기 위해 배열을 확장
        // colorsList의 처음 N-1개 요소를 뒤에 추가하여 모든 랩어라운드 케이스를 커버
        int[] extendedColors = new int[N + N - 1];
        System.arraycopy(colors, 0, extendedColors, 0, N);
        System.arraycopy(colors, 0, extendedColors, N, N - 1);
        /* 
            public static native void arraycopy(
                                    Object src,     // 1. 원본 배열 (source array)
                                    int srcPos,     // 2. 원본 배열에서 복사를 시작할 위치 (source position)
                                    Object dest,    // 3. 대상 배열 (destination array)
                                    int destPos,    // 4. 대상 배열에서 복사를 시작할 위치 (destination position)
                                    int length      // 5. 복사할 요소의 개수 (number of elements to copy)
                                );
         */
        int maxConsecutiveLen = 0;
        int currentConsecutiveLen = 0;

        for (int i = 0; i < extendedColors.length; i++) {
            if (i == 0 || extendedColors[i] == extendedColors[i-1]) {
                currentConsecutiveLen++;
            } else {
                currentConsecutiveLen = 1;
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
