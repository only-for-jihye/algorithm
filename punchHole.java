import java.io.*;
  
public class Solution {
    static final int DIR_V = 0;
    static final int DIR_H = 1;
      
    static int N,W,H;
    static Node punch,find;
    static Papper papper; // 현재의 종이 정보
    static ArrayList<Papper> pappers;
    static ArrayList<Node> holes;
    public static void main(String[] args) throws Exception {
        // FileInputStream fls = new FileInputStream("input.txt");
        // System.setIn(fls);
          
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
          
        int T = Integer.parseInt(br.readLine());
        for(int t=0; t<T; ++t) {
            StringTokenizer st = new StringTokenizer(br.readLine());
              
            N = Integer.parseInt(st.nextToken());
            W = Integer.parseInt(st.nextToken());
            H = Integer.parseInt(st.nextToken());
  
            pappers = new ArrayList<>();
            holes = new ArrayList<>();
            papper = new Papper(new Node(0,0),0,0);
              
            for(int i=0; i<N; ++i) {
                st = new StringTokenizer(br.readLine());
                int dir = Integer.parseInt(st.nextToken());
                int range = Integer.parseInt(st.nextToken());
                getPapper(dir,range);
            }
              
            st = new StringTokenizer(br.readLine());
            // 구멍을 뚫을 위치 정보 저장
            int punchX = Integer.parseInt(st.nextToken());
            int punchY = Integer.parseInt(st.nextToken());
            punch = new Node(punchY,punchX);
            // 순서를 출력할 구멍 정보 저장
            int findX = Integer.parseInt(st.nextToken());
            int findY = Integer.parseInt(st.nextToken());
            find = new Node(findY,findX);
  
            // 전체 구멍 구하기
            holes.add(punch); // 첫 번째 구멍은 punch 위치
            getHole();
            // 전체 구멍 정렬
            Collections.sort(holes);
              
            // 결과 출력
            System.out.println("#"+(t+1)+" "+getResult());
        }
    }
      
    public static void getPapper(int dir, int range) {
        int ny = papper.point.y;
        int nx = papper.point.x;
        if(dir==DIR_V) { // 수평방향으로 접었을 경우 시작점 y위치를 range만큼 이동
            nx += range;
        } else { // 수직방향으로 접었을 경우 시작점 x위치를 range만큼 이동
            ny += range;
        }
          
        papper = new Papper(new Node(ny,nx),dir,range);
        pappers.add(papper);
    }
      
    public static void getHole() {
        for(int i=pappers.size()-1; i>=0; --i) {
            int max = holes.size();
              
            Papper current = pappers.get(i);
            if(current.dir==DIR_V) {
                for(int j=0; j<max; ++j) {
                    Node hole = holes.get(j);
                      
                    int dis = Math.abs(current.point.x - hole.x);
                    if(dis>current.range) continue;
                      
                    int ny = hole.y;
                    int nx = current.point.x - (dis-1);
                    holes.add(new Node(ny,nx));
                }
            } else {
                for(int j=0; j<max; ++j) {
                    Node hole = holes.get(j);
                      
                    int dis = Math.abs(current.point.y - hole.y);
                    if(dis>current.range) continue;
                      
                    int ny = current.point.y - (dis-1);
                    int nx = hole.x;
                    holes.add(new Node(ny,nx));
                }
            }
        }
    }
  
    public static int getResult() {
        for(int i=0; i<holes.size(); ++i) {
            Node hole = holes.get(i);
            if(hole.y==find.y && hole.x==find.x) return i+1;
        }
        return 0;
    }
  
    static class Papper {
        Node point;
        int dir,range;
          
        Papper(Node point, int dir, int range) {
            this.point = point;
            this.dir = dir;
            this.range = range;
        }
    }
    static class Node implements Comparable<Node> {
        int y,x;
          
        Node(int y, int x) {
            this.y = y;
            this.x = x;
        }
          
        @Override
        public int compareTo(Node o) {
            return this.y==o.y ? this.x - o.x : this.y - o.y;
        }
    }
}
