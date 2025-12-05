package 마라톤;

//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//class UserSolution2 {
//
//    /**
//     * 1. 도로 DB (ID로 빠른 조회/삭제)
//     */
//    static class Road {
//        int spotA, spotB, len;
//        boolean removed;
//
//        Road(int a, int b, int l) {
//            this.spotA = a;
//            this.spotB = b;
//            this.len = l;
//            this.removed = false;
//        }
//    }
//
//    /**
//     * 2. 인접 리스트용 간선 (DFS 탐색용)
//     */
//    static class Edge {
//        int to;
//        int id;
//        int len;
//
//        Edge(int to, int id, int len) {
//            this.to = to;
//            this.id = id;
//            this.len = len;
//        }
//    }
//
//    /**
//     * 3. 절반 코스 정보 (반환점 T에 저장됨)
//     */
//    static class HalfPath {
//        long length;
//        HashSet<Integer> roadIDs;
//
//        HalfPath(long l, HashSet<Integer> ids) {
//            this.length = l;
//            this.roadIDs = ids; // 깊은 복사 필요
//        }
//    }
//
//    static int N_spots;
//    static Map<Integer, Road> roadDb;
//    static List<Edge>[] adj;
//    
//    // getLength에서 매번 초기화
//    static List<HalfPath>[] halfPaths; 
//    static long maxTotalLen;
//
//    /**
//     * API 1: 초기화
//     */
//    public void init(int N) {
//        N_spots = N;
//        roadDb = new HashMap<>();
//        adj = new ArrayList[N + 1]; // 지점 ID는 1부터 N까지
//        for (int i = 1; i <= N; i++) {
//            adj[i] = new ArrayList<>();
//        }
//    }
//
//    /**
//     * API 2: 도로 추가 (양방향)
//     */
//    public void addRoad(int K, int mID[], int mSpotA[], int mSpotB[], int mLen[]) {
//        for (int i = 0; i < K; i++) {
//            int id = mID[i];
//            int a = mSpotA[i];
//            int b = mSpotB[i];
//            int len = mLen[i];
//
//            roadDb.put(id, new Road(a, b, len));
//            adj[a].add(new Edge(b, id, len));
//            adj[b].add(new Edge(a, id, len));
//        }
//    }
//
//    /**
//     * API 3: 도로 삭제 (플래그만 변경)
//     */
//    public void removeRoad(int mID) {
//        if (roadDb.containsKey(mID)) {
//            roadDb.get(mID).removed = true;
//        }
//    }
//
//    /**
//     * API 4: 마라톤 코스 길이 계산
//     */
//    public int getLength(int mSpot) {
//        // 1. 전역 변수 초기화
//        maxTotalLen = -1;
//        halfPaths = new ArrayList[N_spots + 1];
//        for (int i = 1; i <= N_spots; i++) {
//            halfPaths[i] = new ArrayList<>();
//        }
//
//        // 2. DFS로 mSpot에서 시작하는 모든 4-깊이 "절반 코스" 찾기
//        findHalfPaths(mSpot, mSpot, 0, 0L, new HashSet<>());
//
//        // 3. 반환점 T를 기준으로 "절반 코스" 2개 조합
//        for (int T = 1; T <= N_spots; T++) {
//            List<HalfPath> paths = halfPaths[T];
//            if (paths.size() < 2) continue; // 조합하려면 2개 이상 필요
//
//            for (int i = 0; i < paths.size(); i++) {
//                for (int j = i + 1; j < paths.size(); j++) {
//                    HalfPath p1 = paths.get(i);
//                    HalfPath p2 = paths.get(j);
//
//                    long totalLen = p1.length + p2.length;
//                    
//                    // 길이 제한 체크
//                    if (totalLen > 42195) continue;
//
//                    // 도로 겹침 체크 (8개 도로가 모두 다른지)
//                    if (areDisjoint(p1.roadIDs, p2.roadIDs)) {
//                        maxTotalLen = Math.max(maxTotalLen, totalLen);
//                    }
//                }
//            }
//        }
//
//        return (int) maxTotalLen;
//    }
//
//    /**
//     * DFS: mSpot에서 깊이 4까지의 경로 탐색
//     * @param u         현재 지점
//     * @param mSpot     시작/도착 지점 (중간 방문 금지)
//     * @param depth     현재까지 온 도로 수
//     * @param currentLen 현재까지의 총 길이
//     * @param usedRoadIDs 현재 경로가 사용한 도로 ID Set
//     */
//    private void findHalfPaths(int u, int mSpot, int depth, long currentLen, HashSet<Integer> usedRoadIDs) {
//        // 1. 깊이 4에 도달 (절반 코스 완성)
//        if (depth == 4) {
//            int T = u; // 현재 위치가 반환점
//            // roadIDs를 깊은 복사하여 저장
//            halfPaths[T].add(new HalfPath(currentLen, new HashSet<>(usedRoadIDs)));
//            return;
//        }
//
//        // 2. 다음 지점으로 탐색
//        for (Edge e : adj[u]) {
//            int v = e.to;
//            int roadID = e.id;
//            int roadLen = e.len;
//
//            Road r = roadDb.get(roadID);
//
//            // 3. 가지치기 (Pruning)
//            if (r.removed) continue; // 제거된 도로
//            if (usedRoadIDs.contains(roadID)) continue; // 이미 사용한 도로 (4개 중복 방지)
//            if (v == mSpot) continue; // 중간에 출발점 방문 금지
//
//            // 4. DFS 재귀 호출 (백트래킹)
//            usedRoadIDs.add(roadID);
//            findHalfPaths(v, mSpot, depth + 1, currentLen + roadLen, usedRoadIDs);
//            usedRoadIDs.remove(roadID); // 백트래킹
//        }
//    }
//
//    /**
//     * 두 Set이 겹치는 원소가 없는지 확인
//     */
//    private boolean areDisjoint(HashSet<Integer> set1, HashSet<Integer> set2) {
//        // 더 작은 Set을 기준으로 순회
//        if (set1.size() > set2.size()) {
//            return areDisjoint(set2, set1);
//        }
//        
//        for (int id : set1) {
//            if (set2.contains(id)) {
//                return false;
//            }
//        }
//        return true;
//    }
//}
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// HashSet은 더 이상 사용하지 않습니다.

class UserSolution2 {

    /**
     * 1. 도로 DB: roadIdx (0~10000)를 포함
     */
    static class Road {
        int spotA, spotB, len;
        boolean removed;
        int roadIdx; // 0부터 시작하는 고유 ID

        Road(int a, int b, int l, int idx) {
            this.spotA = a;
            this.spotB = b;
            this.len = l;
            this.removed = false;
            this.roadIdx = idx;
        }
    }

    /**
     * 2. 인접 리스트용 간선: mID와 roadIdx를 모두 포함
     */
    static class Edge {
        int to;
        int mID; // removeRoad를 위한 원본 ID
        int len;
        int roadIdx; // 방문 체크(boolean[])용 ID

        Edge(int to, int mID, int len, int roadIdx) {
            this.to = to;
            this.mID = mID;
            this.len = len;
            this.roadIdx = roadIdx;
        }
    }

    /**
     * 3. 절반 코스 정보 (HashSet 대신 int[4] 사용)
     */
    static class HalfPath {
        long length;
        int[] roadIndices; // 4개 도로의 roadIdx

        HalfPath(long l, int[] ids) {
            this.length = l;
            this.roadIndices = ids; // clone된 배열
        }
    }

    static int N_spots;
    static Map<Integer, Road> roadDb;
    static List<Edge>[] adj;
    
    // getLength에서 매번 초기화
    static List<HalfPath>[] halfPaths; 
    static long maxTotalLen;
    static int roadCounter; // 0부터 시작하는 roadIdx 부여용
    
    // 총 도로의 최대 개수 (1000 * 10)
    static final int MAX_ROADS = 10001; 
    static final long MARATHON_LIMIT = 42195L;

    // DFS 최적화를 위한 전역 배열
    static boolean[] visitedRoads; // DFS 백트래킹용
    static boolean[] removedRoads; // roadDb.get() 대체용
    static int[] currentPathRoads; // DFS 경로 추적용

    /**
     * API 1: 초기화
     */
    public void init(int N) {
        N_spots = N;
        roadDb = new HashMap<>();
        adj = new ArrayList[N + 1];
        for (int i = 1; i <= N; i++) {
            adj[i] = new ArrayList<>();
        }
        
        roadCounter = 0; 
        
        // 전역 배열 할당
        visitedRoads = new boolean[MAX_ROADS]; 
        removedRoads = new boolean[MAX_ROADS];
        currentPathRoads = new int[4]; // 4-깊이 DFS
        
        // (배열 초기화는 필요 시 getLength에서, 
        //  여기서는 removedRoads만 초기화)
        // Arrays.fill(removedRoads, false); // Java는 기본값 false
    }

    /**
     * API 2: 도로 추가 (양방향, roadIdx 부여)
     */
    public void addRoad(int K, int mID[], int mSpotA[], int mSpotB[], int mLen[]) {
        for (int i = 0; i < K; i++) {
            int id = mID[i];
            int a = mSpotA[i];
            int b = mSpotB[i];
            int len = mLen[i];
            
            int currentRoadIdx = roadCounter++; 

            roadDb.put(id, new Road(a, b, len, currentRoadIdx));
            adj[a].add(new Edge(b, id, len, currentRoadIdx));
            adj[b].add(new Edge(a, id, len, currentRoadIdx));
            
            // 추가되는 도로는 '제거' 상태가 아님
            removedRoads[currentRoadIdx] = false;
        }
    }

    /**
     * API 3: 도로 삭제 (최적화: removedRoads 배열 업데이트)
     */
    public void removeRoad(int mID) {
        if (roadDb.containsKey(mID)) {
            Road r = roadDb.get(mID);
            if (!r.removed) { // 중복 삭제 방지
                r.removed = true;
                // DFS가 조회할 배열에 반영
                removedRoads[r.roadIdx] = true;
            }
        }
    }

    /**
     * API 4: 마라톤 코스 길이 계산 (최적화된 O(5^4) + 조합)
     */
    public int getLength(int mSpot) {
        maxTotalLen = -1;
        halfPaths = new ArrayList[N_spots + 1];
        for (int i = 1; i <= N_spots; i++) {
            halfPaths[i] = new ArrayList<>();
        }

        // 1. DFS로 4-깊이 "절반 코스" 찾기
        // (visitedRoads는 어차피 백트래킹으로 초기화되므로 fill 불필요)
        findHalfPaths(mSpot, mSpot, 0, 0L);

        // 2. 반환점 T를 기준으로 "절반 코스" 2개 조합
        for (int T = 1; T <= N_spots; T++) {
            List<HalfPath> paths = halfPaths[T];
            if (paths.size() < 2) continue;

            for (int i = 0; i < paths.size(); i++) {
                for (int j = i + 1; j < paths.size(); j++) {
                    HalfPath p1 = paths.get(i);
                    HalfPath p2 = paths.get(j);

                    long totalLen = p1.length + p2.length;
                    
                    if (totalLen > MARATHON_LIMIT) continue;

                    // O(16) 비교
                    if (areDisjoint(p1.roadIndices, p2.roadIndices)) {
                        maxTotalLen = Math.max(maxTotalLen, totalLen);
                    }
                }
            }
        }

        return (int) maxTotalLen;
    }

    /**
     * DFS (HashMap/HashSet 오버헤드 제거)
     */
    private void findHalfPaths(int u, int mSpot, int depth, long currentLen) {
        
        if (depth == 4) {
            int T = u; // 반환점
            // currentPathRoads를 복제하여 저장 (매우 빠름)
            halfPaths[T].add(new HalfPath(currentLen, currentPathRoads.clone()));
            return;
        }

        for (Edge e : adj[u]) {
            int v = e.to;
            int roadIdx = e.roadIdx;

            // (최적화) O(1) 배열 조회로 변경
            if (removedRoads[roadIdx]) continue; 
            if (visitedRoads[roadIdx]) continue;
            if (v == mSpot) continue; 
            
            if (currentLen + e.len > MARATHON_LIMIT) continue; // 가지치기

            visitedRoads[roadIdx] = true;
            currentPathRoads[depth] = roadIdx; // 현재 경로(깊이)에 roadIdx 저장
            
            findHalfPaths(v, mSpot, depth + 1, currentLen + e.len);
            
            visitedRoads[roadIdx] = false; 
            // currentPathRoads[depth]는 덮어써지므로 되돌릴 필요 없음
        }
    }

    /**
     * O(16) 비교 (int[4] vs int[4])
     */
    private boolean areDisjoint(int[] set1, int[] set2) {
        for (int r1 : set1) {
            for (int r2 : set2) {
                if (r1 == r2) {
                    return false; // 겹침
                }
            }
        }
        return true; // 겹치지 않음
    }
}