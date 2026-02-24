package 배송로봇;

import java.util.*;

class UserSolution {

    static final int INF = 1000000; // Integer.MAX_VALUE로 선언하면 +하는 순간 -가 됨, mTime이 100이고 도시가 50개면, .. 대충 100만때림
    int N;
    int[][] dist; // 모든 정점 간 최단 거리 (Floyd-Warshall)

    int minTotalTime;
    int currentM;
    int[] currentSenders;
    int[] currentReceivers;
    boolean[] visited;

    public void init(int N, int E, int[] sCity, int[] eCity, int[] mTime) {
        this.N = N;
        this.dist = new int[N][N];

        for (int i = 0; i < N; i++) {
            Arrays.fill(dist[i], INF);
            dist[i][i] = 0;
        }

        for (int i = 0; i < E; i++) {
            int u = sCity[i];
            int v = eCity[i];
            int w = mTime[i];
            dist[u][v] = Math.min(dist[u][v], w);
        }

        // 플로이드 워셜 : 초기 노드 간 최단 거리 계산
        for (int k = 0; k < N; k++) {
            for (int i = 0; i < N; i++) {
            	
                if (dist[i][k] == INF) continue;
                
                for (int j = 0; j < N; j++) {
                    if (dist[i][j] > dist[i][k] + dist[k][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }
    }

    public void add(int sCity, int eCity, int mTime) {
        if (dist[sCity][eCity] <= mTime) return;
        dist[sCity][eCity] = mTime;

        // 새로운 간선(sCity -> eCity)이 추가됨으로 인해 단축되는 경로 갱신 O(N^2)
        // i -> j 로 가는 길보다 i -> sCity -> eCity -> j 가 더 짧으면 갱신
        for (int i = 0; i < N; i++) {
            if (dist[i][sCity] == INF) continue;
            for (int j = 0; j < N; j++) {
                if (dist[eCity][j] == INF) continue;
                
                int newPath = dist[i][sCity] + mTime + dist[eCity][j];
                if (dist[i][j] > newPath) {
                    dist[i][j] = newPath;
                }
            }
        }
    }

    public int deliver(int mPos, int M, int[] mSender, int[] mReceiver) {
        this.currentM = M;
        this.currentSenders = mSender;
        this.currentReceivers = mReceiver;
        this.visited = new boolean[M];
        this.minTotalTime = Integer.MAX_VALUE;

        // DFS 탐색 시작 (현재 로봇 위치, 누적 시간, 배송 완료 횟수)
        dfs(mPos, 0, 0);

        return minTotalTime;
    }

    private void dfs(int robotPos, int accTime, int count) {
        // 가지치기: 이미 찾은 최소 시간보다 현재 시간이 더 길면 탐색 중단
        if (accTime >= minTotalTime) return;

        // 기저 조건: 모든 배송 완료
        if (count == currentM) {
            minTotalTime = accTime;
            return;
        }

        // 순열 생성: 아직 처리하지 않은 배송 건을 하나씩 시도
        for (int i = 0; i < currentM; i++) {
            if (!visited[i]) {
                int pickupNode = currentSenders[i];
                int dropNode = currentReceivers[i];

                // 경로 유효성 검사 (갈 수 없는 길이면 스킵)
                if (dist[robotPos][pickupNode] == INF || dist[pickupNode][dropNode] == INF) {
                    continue;
                }

                // 이동 시간 계산
                // 1. 현재위치 -> 픽업지 이동
                // 2. 픽업지 -> 배송지 이동 (물건 적재/하차 시간은 0)
                int cost = dist[robotPos][pickupNode] + dist[pickupNode][dropNode];

                visited[i] = true;
                // 배송 후 로봇의 위치는 dropNode가 됨
                dfs(dropNode, accTime + cost, count + 1);
                visited[i] = false; // 백트래킹
            }
        }
    }
}