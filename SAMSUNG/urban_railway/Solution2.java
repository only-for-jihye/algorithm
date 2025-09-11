package urban_railway;

import java.io.FileInputStream;
import java.util.*;

public class Solution2 {

    static int N;
    static int M;
    static int R;
    static boolean[][] already_connect_city;

    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream("SAMSUNG/urban_railway/org_sample.txt");
        System.setIn(fis);
        Scanner sc = new Scanner(System.in);

        int T = sc.nextInt(); // 테스트 케이스

        for (int i = 1; i <= T; i++) {
            N = sc.nextInt(); // 도시
            M = sc.nextInt(); // 이미 있는 철로
            R = sc.nextInt(); // 추가로 건설할 수 있는 철도
            already_connect_city = new boolean[N + 1][N + 1];

            for (int j = 0; j < M; j++) {
                int start = sc.nextInt();
                int end = sc.nextInt();
                already_connect_city[start][end] = true;
                already_connect_city[end][start] = true;
            }

            // 방문 여부를 추적하기 위한 배열
            boolean[] visited = new boolean[N + 1];

            // 각 연결 요소(도시 그룹)의 크기를 저장할 리스트
            List<Integer> componentSizes = new ArrayList<>();

            // 모든 도시를 순회하며 아직 방문하지 않은 도시에서 BFS 시작
            for (int k = 1; k <= N; k++) {
                if (!visited[k]) {
                    // 새로운 연결 요소를 발견하면 BFS를 통해 크기를 계산
                    int size = bfs(k, visited);
                    componentSizes.add(size);
                }
            }

            // 연결 요소의 크기를 내림차순으로 정렬
            Collections.sort(componentSizes, Collections.reverseOrder());

            int maxConnectedCities = 0;
            // R개의 철도를 이용하면 최대 R+1개의 연결 요소를 하나로 합칠 수 있음
            // 따라서 가장 큰 R+1개의 연결 요소 크기를 더함
            for (int k = 0; k < Math.min(R + 1, componentSizes.size()); k++) {
                System.out.println("componentSizes : " + componentSizes.get(k));
                maxConnectedCities += componentSizes.get(k);
            }

            System.out.printf("#%d %d\n", i, maxConnectedCities);
        }

        sc.close();
    }

    /**
     * BFS(너비 우선 탐색)를 사용하여 연결된 도시 그룹의 크기를 계산하는 메서드
     *
     * @param startNode 탐색을 시작할 도시 번호
     * @param visited   도시 방문 여부를 기록하는 배열
     * @return 현재 연결된 도시 그룹의 크기
     */
    private static int bfs(int startNode, boolean[] visited) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(startNode);
        visited[startNode] = true;
        int count = 1; // 시작 노드 포함

        while (!queue.isEmpty()) {
            int current = queue.poll();
            System.out.println("current: " + current);
            // 현재 도시와 연결된 모든 도시를 탐색
            for (int i = 1; i <= N; i++) {
                // 철로가 연결되어 있고 아직 방문하지 않은 도시인 경우
                if (already_connect_city[current][i] && !visited[i]) {
                    visited[i] = true;
                    queue.add(i);
                    count++;
                    System.out.println("connt count : " + count);
                }
            }
            System.out.println("not connect count : " + count);
        }
        return count;
    }
}
