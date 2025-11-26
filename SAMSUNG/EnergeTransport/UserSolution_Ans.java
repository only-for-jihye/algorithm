package EnergeTransport;

import java.util.*;

public class UserSolution_Ans {
    // 무한대 값 상수
    private static final int INF = Integer.MAX_VALUE;
    
    // 제약 조건에 따른 배열 크기 설정
    // N은 최대 100, 파이프라인은 Init(500) + Add(500) = 최대 1000개
    private static final int MAX_N = 105;
    private static final int MAX_PIPES = 1005;

    // 파이프라인 정보를 저장할 클래스
    static class Pipeline {
        int u, v;           // 연결된 두 저장소 노드
        char[] attrs;       // 파이프라인의 속성 배열
        int id;             // 내부 관리용 고유 ID (배열 인덱스용)
        int selfCost;       // 이 파이프라인의 속성과 동일한 에너지가 통과할 때 비용 ('D' 개수)

        public Pipeline(int u, int v, char[] attrs, int id, int selfCost) {
            this.u = u;
            this.v = v;
            this.attrs = attrs;
            this.id = id;
            this.selfCost = selfCost;
        }
    }

    // 다익스트라 탐색을 위한 상태 클래스
    static class State implements Comparable<State> {
        int cost;   // 현재까지의 누적 비용
        int u;      // 현재 위치한 저장소 번호
        int pIdx;   // 현재 에너지의 속성 상태 (-1: 초기 상태, 0 이상: 해당 파이프라인 ID의 속성으로 변경됨)

        public State(int cost, int u, int pIdx) {
            this.cost = cost;
            this.u = u;
            this.pIdx = pIdx;
        }

        // 우선순위 큐에서 비용이 낮은 순서대로 정렬
        @Override
        public int compareTo(State o) {
            return Integer.compare(this.cost, o.cost);
        }
    }

    // 전역 변수 선언
    private int N, M;
    private int totalPipeCount; // 생성된 누적 파이프라인 수 (삭제되어도 줄어들지 않음)
    
    // 그래프 구조
    private ArrayList<Pipeline>[] adj;          // 인접 리스트 (그래프 탐색용)
    private ArrayList<Pipeline> allPipelines;   // 모든 파이프라인 정보 (인덱스 참조용)
    private Map<Integer, Pipeline> activePipes; // 활성화된 파이프라인 관리 (삭제 검색용)

    // [최적화 1] 비용 조회 테이블 (Look-up Table)
    // costTable[A][B] = A 파이프라인 속성을 가진 에너지가 B 파이프라인을 통과할 때의 비용
    private int[][] costTable;

    // 메모리 재사용을 위한 다익스트라 거리 배열
    private int[] distUnchanged;
    private int[][] distChanged;

    /**
     * 초기화 함수
     */
    public void init(int N, int M, int K, int[] mID, int[] aStorage, int[] bStorage, char[][] mAttr) {
        this.N = N;
        this.M = M;
        this.totalPipeCount = 0;

        // 자료구조 할당 및 초기화
        this.adj = new ArrayList[N + 1];
        for (int i = 0; i <= N; i++) {
            this.adj[i] = new ArrayList<>();
        }
        
        this.allPipelines = new ArrayList<>();
        this.activePipes = new HashMap<>();
        
        // 최대 크기로 미리 할당 (매번 new 하지 않음)
        this.costTable = new int[MAX_PIPES][MAX_PIPES];
        this.distUnchanged = new int[N + 1];
        this.distChanged = new int[N + 1][MAX_PIPES];

        // 초기 파이프라인 추가
        for (int i = 0; i < K; i++) {
            add(mID[i], aStorage[i], bStorage[i], mAttr[i]);
        }
    }

    /**
     * 파이프라인 추가 함수
     * - 그래프 연결 및 Cost Table 업데이트 수행
     */
    public void add(int mID, int aStorage, int bStorage, char[] mAttr) {
        // 자체 비용(Self Cost) 계산: 속성을 변경할 때 발생하는 기본 비용 ('D'의 개수)
        int selfCost = 0;
        for (int i = 0; i < M; i++) {
            if (mAttr[i] == 'D') selfCost++;
        }

        // 데이터 객체 생성
        char[] attrCopy = mAttr.clone(); // 참조 분리를 위해 복사
        Pipeline newPipe = new Pipeline(aStorage, bStorage, attrCopy, totalPipeCount, selfCost);
        
        // 1. 그래프 및 관리 리스트에 등록
        adj[aStorage].add(newPipe);
        adj[bStorage].add(newPipe);
        activePipes.put(mID, newPipe);
        allPipelines.add(newPipe);

        // 2. [최적화 1 구현] Cost Table 업데이트 (Pre-computation)
        // 새로 들어온 파이프라인(newPipe)과 기존 모든 파이프라인 간의 비용을 미리 계산
        // O(K * M) -> add 호출 횟수만큼 수행되므로 전체 수행 시간에 영향 적음
        for (int i = 0; i <= totalPipeCount; i++) {
            Pipeline existing = allPipelines.get(i);
            int cost = calcCostRaw(existing.attrs, attrCopy);
            
            // 대칭 행렬 (A->B 비용 == B->A 비용)
            costTable[i][totalPipeCount] = cost;
            costTable[totalPipeCount][i] = cost;
        }

        totalPipeCount++;
    }

    /**
     * 파이프라인 제거 함수
     */
    public void remove(int mID) {
        Pipeline p = activePipes.remove(mID);
        if (p != null) {
            // 인접 리스트에서 제거하여 그래프 탐색 경로 차단
            // ArrayList remove(Object)는 O(N)이나 Degree가 작으므로 매우 빠름
            adj[p.u].remove(p);
            adj[p.v].remove(p);
            
            // 주의: allPipelines와 costTable에서는 데이터를 지우지 않음
            // 이유: 이미 속성이 변경된 에너지가 삭제된 파이프라인의 속성 ID(pIdx)를 가지고 있을 수 있음
        }
    }

    /**
     * 에너지 운송 최소 비용 계산 (다익스트라 알고리즘)
     */
    public int transport(int sStorage, int eStorage, char[] mAttr) {
        // 1. 거리 배열 초기화 (전체를 new 하는 대신 기존 배열 값만 초기화)
        Arrays.fill(distUnchanged, INF);
        // 사용 중인 파이프라인 ID 범위까지만 초기화하여 루프 최소화
        for (int i = 1; i <= N; i++) {
            Arrays.fill(distChanged[i], 0, totalPipeCount, INF);
        }

        PriorityQueue<State> pq = new PriorityQueue<>();

        // 시작 노드 설정
        distUnchanged[sStorage] = 0;
        pq.offer(new State(0, sStorage, -1));

        while (!pq.isEmpty()) {
            State cur = pq.poll();
            int u = cur.u;
            int cost = cur.cost;
            int pIdx = cur.pIdx;

            // [최적화 2 구현] Strict Pruning (꺼낼 때 가지치기)
            // 큐에 들어있던 "더 비싼 경로"가 나중에 나왔을 때 즉시 폐기
            if (pIdx == -1) {
                if (cost > distUnchanged[u]) continue;
            } else {
                if (cost > distChanged[u][pIdx]) continue;
            }

            // 목적지 도달 시 종료
            if (u == eStorage) return cost;

            // 인접한 파이프라인 탐색
            for (Pipeline p : adj[u]) {
                int v = (p.u == u) ? p.v : p.u;
                
                // Case A: 아직 속성을 변경하지 않은 상태 (Unchanged)
                if (pIdx == -1) {
                    // 1. 변경 없이 그대로 이동
                    // (쿼리마다 mAttr이 다르므로 테이블 사용 불가, 직접 계산)
                    int keepCost = calcCostRaw(mAttr, p.attrs);
                    if (distUnchanged[u] + keepCost < distUnchanged[v]) {
                        distUnchanged[v] = distUnchanged[u] + keepCost;
                        pq.offer(new State(distUnchanged[v], v, -1));
                    }

                    // 2. 현재 파이프라인의 속성으로 변경하고 이동
                    // 변경 비용 = p.selfCost (D의 개수)
                    // 상태 변화: -1 -> p.id
                    int changeCost = p.selfCost;
                    if (distUnchanged[u] + changeCost < distChanged[v][p.id]) {
                        distChanged[v][p.id] = distUnchanged[u] + changeCost;
                        pq.offer(new State(distChanged[v][p.id], v, p.id));
                    }
                } 
                // Case B: 이미 속성이 변경된 상태 (Changed to pIdx)
                else {
                    // 3. 변경된 속성을 유지하며 이동
                    // [최적화 1 활용] 미리 계산된 테이블에서 O(1)로 비용 조회
                    int moveCost = costTable[pIdx][p.id];
                    
                    if (distChanged[u][pIdx] + moveCost < distChanged[v][pIdx]) {
                        distChanged[v][pIdx] = distChanged[u][pIdx] + moveCost;
                        pq.offer(new State(distChanged[v][pIdx], v, pIdx));
                    }
                }
            }
        }

        return -1; // 도달 불가능
    }

    /**
     * 두 속성 배열 간의 비용을 직접 계산하는 함수 O(M)
     * - add() 시점의 테이블 생성과 transport()의 초기 상태(Unchanged)에서만 사용
     */
    private int calcCostRaw(char[] energy, char[] pipe) {
        int cost = 0;
        for (int i = 0; i < M; i++) {
            // 1. 속성 값이 다르거나
            // 2. 에너지 속성이 'D' 이거나
            // 3. 파이프라인 속성이 'D' 이면 비용 발생
            if (energy[i] != pipe[i] || energy[i] == 'D' || pipe[i] == 'D') {
                cost++;
            }
        }
        return cost;
    }
}