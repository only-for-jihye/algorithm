package 광물운송;

//Main.java

import java.io.*; 
import java.util.*;

class Main {

  private final static int CMD_INIT = 1;
  private final static int CMD_ADD = 2;
  private final static int CMD_DROP = 3;

  private final static UserSolution usersolution = new UserSolution();

  private static boolean run(BufferedReader br) throws Exception {

  	StringTokenizer st = new StringTokenizer(br.readLine(), " ");
      int query_num = Integer.parseInt(st.nextToken());
      boolean ok = false;

      for (int q = 0; q < query_num; q++) {
      	st = new StringTokenizer(br.readLine(), " ");
          int query = Integer.parseInt(st.nextToken());

          if (query == CMD_INIT) {
              int L = Integer.parseInt(st.nextToken());
              int N = Integer.parseInt(st.nextToken());
              usersolution.init(L, N);
              ok = true;
          } else if (query == CMD_ADD) {
              int mID = Integer.parseInt(st.nextToken());
              int mRow = Integer.parseInt(st.nextToken());
              int mCol = Integer.parseInt(st.nextToken());
              int mQuantity = Integer.parseInt(st.nextToken());
              int ret = usersolution.addBaseCamp(mID, mRow, mCol, mQuantity);
              int ans = Integer.parseInt(st.nextToken());
              if (ans != ret) {
                  ok = false;
              }
          } else if (query == CMD_DROP) {
              int K = Integer.parseInt(st.nextToken());
              int ret = usersolution.findBaseCampForDropping(K);
              int ans = Integer.parseInt(st.nextToken());
              if (ans != ret) {
                  ok = false;
              }
          }
      }
      return ok;
  }

  public static void main(String[] args) throws Exception {
      int T, MARK;
      System.setIn(new java.io.FileInputStream("SAMSUNG/광물운송/input.txt"));

      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));    
      StringTokenizer st = new StringTokenizer(br.readLine(), " ");

      T = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());	

      for (int tc = 1; tc <= T; tc++) {
          int score = run(br) ? MARK : 0;
          System.out.println("#" + tc + " " + score);
      }
      br.close();
  }
}