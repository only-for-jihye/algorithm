package 전기차여행;

//UserSolution.java
import java.util.*;

//전기차 여행
//@admin_deukwha
//dijkstra

class UserSolution {
   
  class Edge implements Comparable <Edge> {
      int id;
      int to;
      int cost;
      int power;
      Edge(int id, int to, int cost, int power) {
          this.id = id;
          this.to = to;
          this.cost = cost;
          this.power = power;
      }
      @Override
      public int compareTo(Edge o) {
          if(cost < o.cost) return -1;
          if(cost > o.cost) return 1;
          if(power > o.power) return -1;
          if(power < o.power) return 1;
          return 0;
      }
  }
   
  HashMap<Integer, Integer>hm;
  int idCnt;
  ArrayList<Edge>[] al;
  int[] chargers; // index : 도시#, value : 이 도시에서의 충전량
  int n; // 도시의 개수 (최대 500, 도시 번호는 0번부터)
  int[] removed; // index : 도로 id (최대 8,000개), value : 삭제되었다면 1
  int[] contaminatedTime; // index : 도시 #, value : 이 도시에 전염병이 퍼지는 최소 시각
  int[][] dist; // index1: 도시 #, index2: 차량의 충전량, value : 최소 시간
   
  int register(int id) {
      if(hm.get(id) == null) hm.put(id, idCnt++);
      return hm.get(id);
  }
   
  void contaminate(int m, int[]st, int[]time) {
      PriorityQueue<Edge>pq = new PriorityQueue<>();
      Arrays.fill(contaminatedTime, Integer.MAX_VALUE);
      for(int i = 0; i < m; i++) {
          pq.add(new Edge(-1, st[i], time[i], -1));
          contaminatedTime[st[i]] = time[i];
      }
      while(!pq.isEmpty()) {
          Edge now = pq.remove();
          if(contaminatedTime[now.to] < now.cost) continue;
          contaminatedTime[now.to] = now.cost; // now.to 도시가 감염되는 최소 시간 확정
          for(Edge next : al[now.to]) {
              if(removed[next.id] == 1) continue;
              int nc = now.cost + next.cost;
              if(contaminatedTime[next.to] <= nc) continue;
              contaminatedTime[next.to] = nc;
              pq.add(new Edge(-1, next.to, nc, -1));
          }
      }
  }
   
  int dijkstra(int st, int en, int B) {
      PriorityQueue<Edge>pq = new PriorityQueue<>();
      pq.add(new Edge(-1, st, 0, B));
      for(int i = 0; i < n; i++) Arrays.fill(dist[i], Integer.MAX_VALUE);
      dist[st][B] = 0;
       
      while(!pq.isEmpty()) {
          Edge now = pq.remove();
          if(dist[now.to][now.power] < now.cost) continue;
          if(now.to == en) {
              return now.cost;
          }
          for(Edge next : al[now.to]) {
              if(removed[next.id] == 1) continue;
              int maxChargeTime = contaminatedTime[now.to] - now.cost; // 현재 도시에서 충전할수 있는 최대 시간
              int requiredCharge = (int)(Math.ceil((double)(Math.max(0, next.power - now.power)) / (double)chargers[now.to])); // 다음 도로를 건너가기 위한 최소 충전 시간
              int flag = 0;
              for(int t = requiredCharge; t < maxChargeTime; t++) {
                  if(flag == 1) break;                                        // 만약 더 충전시 과충전이면 -> break
                  int nc = now.cost + t + next.cost;                          // 다음 도시까지의 cost
                  if(contaminatedTime[next.to] <= nc) break;                  // 이만큼 충전하고 도달했을때 도착도시가 감염되면 -> 이만큼 충전 불가능
                  int np = Math.min(now.power + (t * chargers[now.to]), B);   // 충전량 계산 (최대 B)
                  if(np == B) flag = 1;                                       // 이 이상 충전시 과충전이면 -> flag 기록
                  np -= next.power;                                           // 도로를 건너가는 전기량 감소
                  if(dist[next.to][np] <= nc) continue;               
                  for(int i = np; i >= 0; i--) {                              // ** 다음 도시로 건너올수 있는 최대 power
                      if(dist[next.to][i] < nc) break;                        // 이 power보다 더 적고, 늦게 이 도시로 들어오는 경우는 unpromising
                      dist[next.to][i] = nc;                                 
                  }
                  pq.add(new Edge(-1, next.to, nc, np));
              }
          }
      }
      return -1;
  }
   
   
  public void init(int N, int mCharge[], int K, int mId[], int sCity[], int eCity[], int mTime[], int mPower[]) {
      n = N;
      chargers = mCharge;    
      removed = new int[8001];
      hm = new HashMap<>();
      idCnt = 0;
      al = new ArrayList[N];
      contaminatedTime = new int[N];
      dist = new int[N][301];
      for(int i = 0; i < N; i++) al[i] = new ArrayList<>();
      for(int i = 0; i < K; i++) add(mId[i], sCity[i], eCity[i], mTime[i], mPower[i]);
      return;
  }

  // 3,000
  public void add(int mId, int sCity, int eCity, int mTime, int mPower) {
      int id = register(mId);
      // 단방향 연결
      al[sCity].add(new Edge(id, eCity, mTime, mPower));
      return;
  }

  // 900
  public void remove(int mId) {
      // 항상 존재하는 id의 도로가 주어짐
      removed[hm.get(mId)] = 1;
      return;
  }

  // 200
  public int cost(int B, int sCity, int eCity, int M, int mCity[], int mTime[]) {
      // #1. M개의 mCity 도시에서 전염병 발생
      // 모든 도시에 전염병이 퍼지는 가장 빠른 시각 확인
      contaminate(M, mCity, mTime);
      // #2. sCity 도시에서 B충전량으로 eCity로 도망
      int ret = dijkstra(sCity, eCity, B);
      return ret;
  }
}