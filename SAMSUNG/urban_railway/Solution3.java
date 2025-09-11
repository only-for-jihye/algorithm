package urban_railway;

import java.io.FileInputStream;
import java.util.*;

public class Solution3 {

    static int N;
    static int M;
    static int R;
    static boolean[][] already_connect_city;

    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream("sample2.txt");
        System.setIn(fis);
        Scanner sc = new Scanner(System.in);

        int T = sc.nextInt();

        for (int t = 1; t <= T; t++) {
            N = sc.nextInt();
            M = sc.nextInt();
            R = sc.nextInt();

            already_connect_city = new boolean[N + 1][N + 1];

            for (int m = 0; m < M; m++) {
                int start = sc.nextInt();
                int end = sc.nextInt();
                already_connect_city[start][end] = true;
                already_connect_city[end][start] = true;
            }

            // 방문 여부
            boolean[] visited = new boolean[N + 1];

            // 크기 저장
            List<Integer> componentSizes = new ArrayList<>();
            
            for (int i = 1; i <= N; i++) { // 1번 도시부터 출발
                if (!visited[i]) {
                    int size = bfs(i, visited);
                    componentSizes.add(size);
                }
            }

            // for (int i = 0; i < componentSizes.size(); i++) {
            //     System.out.println(t + " componentSize : " + componentSizes.get(i));
            // }
            
            // 그리디 알고리즘 구현
            // 가장 큰 그룹을 만들기 위해서, 당연히 가장 큰 글부들부터 합치는 것이 최우선이다.
            Collections.sort(componentSizes, Collections.reverseOrder());

            // for (int i = 0; i < componentSizes.size(); i++) {
            //     System.out.println(t + " sorted componentSize : " + componentSizes.get(i));
            // }

            // 철도 1개로 그룹 2개를 연결하고, 철도2개로 그룹3개를 연결하고..
            // 따라서 철도 R개면 그룹 R + 1개를 연결할 수 있다.
            int maxConnectedCities = 0;
            for (int k = 0; k < Math.min(R + 1, componentSizes.size()); k++) {
                System.out.println("componentSizes : " + componentSizes.get(k));
                maxConnectedCities += componentSizes.get(k);
            }
            System.out.println(maxConnectedCities);
        }
    }
    
    // 이미 연결되어 있는 그룹의 수를 찾는다.
    static int bfs(int startNode, boolean[] visited) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(startNode);
        visited[startNode] = true;
        int count = 1;
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (int i = 1; i <= N; i++) {
                if (already_connect_city[current][i] && !visited[i]) {
                    visited[i] = true;
                    queue.add(i);
                    count++;
                }
            }
        }
        return count;
    }
}
