package telecom;

//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.PriorityQueue;
//
//class UserSolution {
//
//    /**
//     * 1. 우선순위 큐에 저장될 상태 (Comparable 구현)
//     * - totalDist: 1순위 정렬 기준 (오름차순)
//     * - maxEdge: 2순위 정렬 기준 (오름차순)
//     */
//    static class State implements Comparable<State> {
//        long totalDist;
//        int maxEdge;
//        int city;
//
//        State(long totalDist, int maxEdge, int city) {
//            this.totalDist = totalDist;
//            this.maxEdge = maxEdge;
//            this.city = city;
//        }
//
//        @Override
//        public int compareTo(State other) {
//            // 1. 총 거리가 다르면 총 거리 기준 오름차순
//            if (this.totalDist != other.totalDist) {
//                return Long.compare(this.totalDist, other.totalDist);
//            }
//            // 2. 총 거리가 같다면, maxEdge 기준 오름차순
//            return Integer.compare(this.maxEdge, other.maxEdge);
//        }
//    }
//
//    /**
//     * 2. 인접 리스트에 저장될 간선 정보
//     */
//    static class Edge {
//        int to;
//        int distance;
//
//        Edge(int to, int distance) {
//            this.to = to;
//            this.distance = distance;
//        }
//    }
//
//    /**
//     * 3. 도로 DB에 저장될 도로 정보 (삭제 플래그 포함)
//     */
//    static class Road {
//        int sCity;
//        int eCity;
//        int mDistance;
//        boolean removed;
//
//        Road(int s, int e, int d, boolean r) {
//            this.sCity = s;
//            this.eCity = e;
//            this.mDistance = d;
//            this.removed = r;
//        }
//    }
//
//    // 전역 변수 선언
//    static int N_cities;
//    static int capital_city;
//    
//    // 도로 DB (Key: mId, Value: Road)
//    static Map<Integer, Road> roadDb;
//    
//    // 다익스트라용 인접 리스트
//    static ArrayList<Edge>[] adj;
//    
//    // 다익스트라 결과 저장용 배열
//    static long[] dist;
//    static int[] maxEdgeCost;
//
//    // 상수 정의 (거리 1~100)
//    static final long INF_DIST = Long.MAX_VALUE / 2; // 오버플로우 방지
//    static final int INF_EDGE = 101; 
//
//    /**
//     * road_db를 기반으로 인접 리스트 adj를 새로 구축합니다.
//     */
//    void buildGraph() {
//        // 인접 리스트 초기화
//        adj = new ArrayList[N_cities];
//        for (int i = 0; i < N_cities; ++i) {
//            adj[i] = new ArrayList<>();
//        }
//
//        // roadDb를 순회하며 'removed == false'인 도로만 adj에 추가
//        for (Road road : roadDb.values()) {
//            if (!road.removed) {
//                adj[road.sCity].add(new Edge(road.eCity, road.mDistance));
//            }
//        }
//    }
//
//    /**
//     * API 1: 테스트 케이스 초기화
//     */
//    public void init(int N, int mCapital, int K, int mId[], int sCity[], int eCity[], int mDistance[]) {
//        N_cities = N;
//        capital_city = mCapital;
//        
//        roadDb = new HashMap<>();
//        
//        // 다익스트라 배열 미리 할당 (매번 새로 생성 방지)
//        dist = new long[N];
//        maxEdgeCost = new int[N];
//
//        // 초기 K개 도로 정보 저장
//        for (int i = 0; i < K; ++i) {
//            roadDb.put(mId[i], new Road(sCity[i], eCity[i], mDistance[i], false));
//        }
//    }
//
//    /**
//     * API 2: 도로 추가
//     */
//    public void add(int mId, int sCity, int eCity, int mDistance) {
//        roadDb.put(mId, new Road(sCity, eCity, mDistance, false));
//    }
//
//    /**
//     * API 3: 도로 제거 (삭제 플래그만 true로 변경)
//     */
//    public void remove(int mId) {
//        if (roadDb.containsKey(mId)) {
//            roadDb.get(mId).removed = true;
//        }
//    }
//
//    /**
//     * API 4: 2차원 다익스트라를 이용한 계산
//     */
//    public int calculate(int mCity) {
//        // 1. road_db를 기반으로 현재 시점의 그래프(adj)를 구축
//        buildGraph();
//
//        // 2. 다익스트라 배열 초기화
//        Arrays.fill(dist, INF_DIST);
//        Arrays.fill(maxEdgeCost, INF_EDGE);
//
//        // 3. 우선순위 큐 (Min-Heap) 초기화 (State.compareTo에 의해 자동 정렬)
//        PriorityQueue<State> pq = new PriorityQueue<>();
//
//        // 4. 시작점(수도) 설정
//        dist[capital_city] = 0;
//        maxEdgeCost[capital_city] = 0; // 수도까지의 max_edge는 0
//        pq.add(new State(0, 0, capital_city));
//
//        while (!pq.isEmpty()) {
//            State current = pq.poll();
//
//            long d = current.totalDist;
//            int m = current.maxEdge;
//            int u = current.city;
//
//            // 5. 프루닝(Pruning): 큐에서 뽑은 정보가 이미 갱신된 최신 정보보다
//            //    나쁘다면(더 길거나, 같지만 max_edge가 크다면) 스킵
//            if (d > dist[u] || (d == dist[u] && m > maxEdgeCost[u])) {
//                continue;
//            }
//
//            // 6. 인접 노드 탐색
//            for (Edge edge : adj[u]) {
//                int v = edge.to;
//                int weight = edge.distance;
//
//                long newDist = d + weight;
//                int newMaxEdge = Math.max(m, weight); // 새 경로의 max_edge 갱신
//
//                // 7. 갱신 로직
//                
//                // 7-1. 더 짧은 경로 발견
//                if (newDist < dist[v]) {
//                    dist[v] = newDist;
//                    maxEdgeCost[v] = newMaxEdge;
//                    pq.add(new State(newDist, newMaxEdge, v));
//                } 
//                // 7-2. 같은 길이의 경로인데, max_edge가 더 작은 경로 발견
//                else if (newDist == dist[v]) {
//                    if (newMaxEdge < maxEdgeCost[v]) {
//                        maxEdgeCost[v] = newMaxEdge;
//                        pq.add(new State(newDist, newMaxEdge, v));
//                    }
//                }
//            }
//        }
//
//        // 8. 결과 반환
//        if (dist[mCity] == INF_DIST) {
//            return -1; // 도달 불가
//        } else {
//            return maxEdgeCost[mCity]; // 최단 경로의 min(max_edge) 값
//        }
//    }
//}
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

class UserSolution {

    // ... State 클래스는 동일 ...
    static class State implements Comparable<State> {
        long totalDist;
        int maxEdge;
        int city;

        State(long totalDist, int maxEdge, int city) {
            this.totalDist = totalDist;
            this.maxEdge = maxEdge;
            this.city = city;
        }

        @Override
        public int compareTo(State other) {
            if (this.totalDist != other.totalDist) {
                return Long.compare(this.totalDist, other.totalDist);
            }
            return Integer.compare(this.maxEdge, other.maxEdge);
        }
    }

    /**
     * 2. 인접 리스트용 Edge: mId를 포함해야 함
     */
    static class Edge {
        int to;
        int distance;
        int mId; // 도로 ID (roadDb 조회용)

        Edge(int to, int distance, int mId) {
            this.to = to;
            this.distance = distance;
            this.mId = mId;
        }
    }

    /**
     * 3. 도로 DB용 Road: sCity가 필요 없음 (Key가 mId이므로)
     */
    static class Road {
        // int sCity; // adj에서 이미 알 수 있음
        int eCity;
        int mDistance;
        boolean removed;

        Road(int e, int d, boolean r) {
            // this.sCity = s;
            this.eCity = e;
            this.mDistance = d;
            this.removed = r;
        }
    }

    // 전역 변수 선언
    static int N_cities;
    static int capital_city;
    
    // 도로 DB (Key: mId, Value: Road)
    static Map<Integer, Road> roadDb;
    
    // 다익스트라용 인접 리스트 (전역으로 유지)
    static ArrayList<Edge>[] adj;
    
    static long[] dist;
    static int[] maxEdgeCost;

    static final long INF_DIST = Long.MAX_VALUE / 2;
    static final int INF_EDGE = 101; 

    /**
     * buildGraph() 함수는 이제 사용되지 않으므로 삭제
     */
    // void buildGraph() { ... }

    /**
     * API 1: 테스트 케이스 초기화
     */
    public void init(int N, int mCapital, int K, int mId[], int sCity[], int eCity[], int mDistance[]) {
        N_cities = N;
        capital_city = mCapital;
        
        roadDb = new HashMap<>();
        adj = new ArrayList[N];
        dist = new long[N];
        maxEdgeCost = new int[N];

        for (int i = 0; i < N_cities; ++i) {
            adj[i] = new ArrayList<>();
        }

        // 초기 K개 도로 정보 저장
        for (int i = 0; i < K; ++i) {
            // roadDb와 adj에 모두 추가
            roadDb.put(mId[i], new Road(eCity[i], mDistance[i], false));
            adj[sCity[i]].add(new Edge(eCity[i], mDistance[i], mId[i]));
        }
    }

    /**
     * API 2: 도로 추가 (roadDb와 adj에 모두 추가)
     */
    public void add(int mId, int sCity, int eCity, int mDistance) {
        roadDb.put(mId, new Road(eCity, mDistance, false));
        adj[sCity].add(new Edge(eCity, mDistance, mId));
    }

    /**
     * API 3: 도로 제거 (roadDb의 플래그만 변경)
     */
    public void remove(int mId) {
        if (roadDb.containsKey(mId)) {
            // roadDb에서 제거 플래그만 true로 설정
            // adj에서는 제거하지 않음 (calculate에서 필터링)
            roadDb.get(mId).removed = true;
        }
    }

    /**
     * API 4: 계산 (buildGraph() 호출 제거)
     */
    public int calculate(int mCity) {
        // 1. buildGraph() 호출 제거!

        // 2. 다익스트라 배열 초기화
        Arrays.fill(dist, INF_DIST);
        Arrays.fill(maxEdgeCost, INF_EDGE);

        // 3. 우선순위 큐 (Min-Heap) 초기화
        PriorityQueue<State> pq = new PriorityQueue<>();

        // 4. 시작점(수도) 설정
        dist[capital_city] = 0;
        maxEdgeCost[capital_city] = 0;
        pq.add(new State(0, 0, capital_city));

        while (!pq.isEmpty()) {
            State current = pq.poll();

            long d = current.totalDist;
            int m = current.maxEdge;
            int u = current.city;

            // 5. 프루닝 (기존과 동일)
            if (d > dist[u] || (d == dist[u] && m > maxEdgeCost[u])) {
                continue;
            }

            // 6. 인접 노드 탐색 (수정됨)
            for (Edge edge : adj[u]) {
                
                // 6-1. (최적화) roadDb에서 이 간선이 제거되었는지 확인
                if (roadDb.get(edge.mId).removed) {
                    continue; // 제거된 도로는 건너뜀
                }

                int v = edge.to;
                int weight = edge.distance;

                long newDist = d + weight;
                int newMaxEdge = Math.max(m, weight);

                // 7. 갱신 로직 (기존과 동일)
                if (newDist < dist[v]) {
                    dist[v] = newDist;
                    maxEdgeCost[v] = newMaxEdge;
                    pq.add(new State(newDist, newMaxEdge, v));
                } 
                else if (newDist == dist[v]) {
                    if (newMaxEdge < maxEdgeCost[v]) {
                        maxEdgeCost[v] = newMaxEdge;
                        pq.add(new State(newDist, newMaxEdge, v));
                    }
                }
            }
        }

        // 8. 결과 반환
        if (dist[mCity] == INF_DIST) {
            return -1;
        } else {
            return maxEdgeCost[mCity];
        }
    }
}