package SAMSUNG.urban_railway;

/*
    Union-Find와 Backtracking 조합으로 풀이
    1. Union-Find: 두 도시를 연결했을 때, 그 도시들이 속한 두 그룹을 하나의 큰 그룹으로 즉시 병합하고, 그 그룹의 크기를 바로 업데이트할 수 있습니다. 
       이는 BFS나 DFS를 매번 돌려야 하는 부담 없이 연결 상태와 크기 계산을 매우 효율적으로 처리합니다. 특정 시점의 그래프 상태에서 가장 큰 연결 요소의 크기를 얻는 것이 간단합니다.
    
    2. BFS/DFS: 두 도시를 연결할 때마다 새로운 그래프를 만들고, 그 그래프에 대해 처음부터 BFS/DFS를 다시 수행하여 모든 연결 요소를 찾아야 합니다. 
       이는 그래프 복사 비용과 매번 전체 탐색을 해야 하는 오버헤드가 발생합니다. 특히 그래프 복사(깊은 복사) 과정에서 실수가 발생하기 쉽습니다.
 */

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Scanner;

public class Solution {

    static int T; // 테스트 케이스의 수
    static int N; // 도시 (Node)
    static int M; // 철로의 수
    static int R; // 추가로 건설하는 철도의 수
    static int[] parent; // 각 도시가 속한 연결 요소의 루트(대표)
    static int[] sizes; // 각 연결 요소에 속한 도시의 개수
    static int u; // M개의 철로로 이어진 2개의 도시 중 첫번째
    static int v; // M개의 철로로 이어진 2개의 도시 중 두번째
    static int maxOverallConnected; // 최대 연결된 도시의 수

    public static void main(String[] args) throws Exception {
        // FileInputStream fls = new FileInputStream("sample.txt");
//        FileInputStream fls = new FileInputStream("D:\\repository\\algorithm\\AD_20240821_기출\\sample.txt");
        FileInputStream fls = new FileInputStream("D:\\repository\\algorithm\\AD_20240821_기출\\org_sample.txt");
        System.setIn(fls);

        Scanner sc = new Scanner(System.in);

        // 테스트 케이스의 수
        T = sc.nextInt();
        for (int i = 0; i < T; i++) {
            N = sc.nextInt();
            M = sc.nextInt();
            R = sc.nextInt();

            parent = new int[N + 1];
            sizes = new int[N + 1];

            // Union-Find 초기화: 각 도시의 부모를 자기 자신으로, 크기를 1로 설정
            // 초기화, 각각 독립적인 집합으로 만든다.
            for (int j = 1; j <= N; j++) {
                parent[j] = j;
                sizes[j] = 1; // 각각 하나씩만 속하므로 사이즈는 1로 초기화
            }

            for (int k = 0; k < M; k++) {
                u = sc.nextInt();
                v = sc.nextInt();
                // union 호출, 연결된 도시를 하나의 더 큰 집합으로 합친다.
                union(u, v);
            }

            maxOverallConnected = 0;
            // R개의 철도 건설 시뮬레이션을 위한 백트래킹 시작
            // 초기 상태의 parent와 sizes 배열을 복사하여 전달
            backtrack(0, Arrays.copyOf(parent, parent.length), Arrays.copyOf(sizes, sizes.length), 1);
            
            // N=0인 경우를 처리 (테스트 케이스에 N=0은 없지만 일반적인 경우)
            if (N == 0) {
                System.out.println(0);
            } else {
                System.out.println(maxOverallConnected);
            }
        }
        sc.close();
    }

    // 백트래킹을 사용하여 R개의 철도 건설 시뮬레이션
    // currentR: 현재까지 건설한 철도 개수
    // currentParent: 현재 시점의 parent 배열 복사본
    // currentSizes: 현재 시점의 sizes 배열 복사본
    // startNode: 중복 탐색을 피하기 위한 시작 노드 인덱스
    public static void backtrack(int currentR, int[] currentParent, int[] currentSizes, int startNode) {
        // 현재 상태에서 가장 큰 연결 요소의 크기를 계산
        int currentMax = 0;
        for (int i = 1; i <= N; i++) {
            if (currentParent[i] == i) { // i가 루트 노드인 경우
                currentMax = Math.max(currentMax, currentSizes[i]);
            }
        }
        // 초기 연결된 도시가 전혀 없을 경우, 각 도시를 1개로 본다.
        if (N > 0 && currentMax == 0 && currentR == 0) { // 아무 철도도 건설하지 않은 초기 상태
             currentMax = 1; // 각 도시가 개별적으로 1개씩 연결된 것으로 간주
        }
        
        maxOverallConnected = Math.max(maxOverallConnected, currentMax);

        if (currentR == R) {
            return; // R개의 철도를 모두 건설했으므로 종료
        }

        // 새로운 철도를 건설할 두 도시 선택
        // startNode를 사용하여 (u, v) 쌍의 중복 탐색을 방지
        for (int u = startNode; u <= N; u++) {
            for (int v = u + 1; v <= N; v++) {
                // 현재 배열 상태를 복사하여 다음 단계로 전달
                int[] nextParent = Arrays.copyOf(currentParent, currentParent.length);
                int[] nextSizes = Arrays.copyOf(currentSizes, currentSizes.length);
                
                // Union-Find 연산에 사용할 임시 parent, sizes 배열 설정 (클래스 멤버 변수 사용)
                parent = nextParent;
                sizes = nextSizes;

                // 두 도시가 아직 같은 집합에 속해 있지 않다면 연결 시도
                if (find(u) != find(v)) {
                    union(u, v);
                    // 재귀 호출, 다음 철도 건설
                    // startNode를 u로 설정하여, 이미 고려한 도시 쌍은 다시 고려하지 않음 (순서가 바뀌는 것 방지)
                    backtrack(currentR + 1, parent, sizes, u); // u부터 다시 시작
                }
            }
        }
    }

    // Find 연산: 경로 압축 적용
    // 도시 i가 속한 집합의 최종 대표(루트)를 찾음
    public static int find(int i) {
        // 자기 자신이 부모인 경우, 그대로 반환 (루트 노드임)
        if (parent[i] == i) {
            return i;
        }
        // 부모를 재귀적으로 찾음
        return parent[i] = find(parent[i]);
    }

    // Union 연산: 크기 기반 합치기
    public static boolean union(int i, int j) {
        int root_i = find(i);
        int root_j = find(j);

        if (root_i != root_j) {
            // 두 집합을 합칠 때는, 더 작은 집합을 더 큰 집합 아래로 합침침
            // 더 작은 트리를 큰 트리에 붙여서 트리의 높이를 최소화
            if (sizes[root_i] < sizes[root_j]) {
                int temp = root_i;
                root_i = root_j;
                root_j = temp;
            }

            // 작은 집합의 대표의 부모를 큰 집합의 대표로 설정
            parent[root_j] = root_i; 
            // 큰 집합의 크기에 작은 집합의 크기를 더해줍니다.
            sizes[root_i] += sizes[root_j];
            return true; // 병합
        }
        return false; // 이미 같은 집합
    }

}
