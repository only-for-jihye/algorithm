package 락다운시티;

//Main.java

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Main {
	private static BufferedReader br;
//  private static UserSolution userSolution = new UserSolution();
//  private static UserSolution2 userSolution = new UserSolution2();
  private static UserSolution3 userSolution = new UserSolution3();

  private final static int MAX_N          = 100;
  private final static int MAX_M          = 4;
  private final static int CMD_INIT       = 100;
  private final static int CMD_CHANGE     = 200;
  private final static int CMD_CALCULATE  = 300;
  
  private static boolean run() throws Exception {
  	StringTokenizer st = new StringTokenizer(br.readLine(), " ");
      int N, M, L, mRow, mCol, mDir, mLength, sRow, sCol, eRow, eCol;
      String mGrade[][] = new String[MAX_N][MAX_N];

      int Q = Integer.parseInt(st.nextToken());
  	boolean okay = false;

	    for (int q = 0; q < Q; ++q)
	    {
      	st = new StringTokenizer(br.readLine(), " ");
	        int cmd = Integer.parseInt(st.nextToken());

	        if (cmd == CMD_INIT)
	        {
	            N = Integer.parseInt(st.nextToken());
	            M = Integer.parseInt(st.nextToken());
              for(int i = 0; i < N; ++i) {
      	        st = new StringTokenizer(br.readLine(), " ");
                  for(int j = 0; j < N; ++j)
                      mGrade[i][j] = st.nextToken();
              }
	            userSolution.init(N, M, mGrade);
	            okay = true;
	        }
	        else if (cmd == CMD_CHANGE)
	        {
              mRow = Integer.parseInt(st.nextToken());
              mCol = Integer.parseInt(st.nextToken());
              mDir = Integer.parseInt(st.nextToken());
              mLength = Integer.parseInt(st.nextToken());
              String mChgGrade = st.nextToken();
	            userSolution.change(mRow, mCol, mDir, mLength, mChgGrade);
	        }
	        else if (cmd == CMD_CALCULATE)
	        {
              L = Integer.parseInt(st.nextToken());
              sRow = Integer.parseInt(st.nextToken());
              sCol = Integer.parseInt(st.nextToken());
              eRow = Integer.parseInt(st.nextToken());
              eCol = Integer.parseInt(st.nextToken());
              String ansGrade = st.nextToken();
	            String ret = userSolution.calculate(L, sRow, sCol, eRow, eCol);

              if(ansGrade.compareTo(ret)!= 0) {
                  okay = false;
              }
	        }
	    }
	    return okay;
  }

  public static void main(String[] args) throws Exception {
      int T, MARK;

       System.setIn(new java.io.FileInputStream("SAMSUNG/락다운시티/input.txt"));
      br = new BufferedReader(new InputStreamReader(System.in));        
      StringTokenizer st = new StringTokenizer(br.readLine(), " ");
      
      T = Integer.parseInt(st.nextToken());
      MARK = Integer.parseInt(st.nextToken());

      for (int tc = 1; tc <= T; tc++) {
          int score = run() ? MARK : 0;
          System.out.println("#" + tc + " " + score);
      }

      br.close();
  }
}