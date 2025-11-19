package EnergeTransport;

//Main.java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Main {
	private static BufferedReader br;
  private static UserSolution userSolution = new UserSolution();

  private final static int MAX_K = 1000;

  private final static int CMD_INIT = 100;
  private final static int CMD_ADD = 200;
  private final static int CMD_REMOVE = 300;
  private final static int CMD_TRANSPORT = 400;
  
  private static boolean run() throws Exception {
  	StringTokenizer st = new StringTokenizer(br.readLine(), " ");
  	
      int Q = Integer.parseInt(st.nextToken());
      int N, M, K, mID, aStorage, bStorage, sStorage, eStorage, ans, ret;
		int[] mIDArr = new int[MAX_K];
		int[] aStorageArr = new int[MAX_K];
		int[] bStorageArr = new int[MAX_K];
      char[] mAttr;
      char[][] mAttrArr = new char[MAX_K][];

  	boolean okay = false;
	    for (int q = 0; q < Q; ++q)
	    {
      	st = new StringTokenizer(br.readLine(), " ");
	        int cmd = Integer.parseInt(st.nextToken());

	        if (cmd == CMD_INIT)
	        {
	            N = Integer.parseInt(st.nextToken());
              M = Integer.parseInt(st.nextToken());
              K = Integer.parseInt(st.nextToken());
	            
	            for(int i = 0; i < K; ++i)
	            {
      	        st = new StringTokenizer(br.readLine(), " ");
                  mIDArr[i] = Integer.parseInt(st.nextToken());
                  aStorageArr[i] = Integer.parseInt(st.nextToken());
                  bStorageArr[i] = Integer.parseInt(st.nextToken());
                  mAttrArr[i] = st.nextToken().toCharArray();
	            }
	            userSolution.init(N, M, K, mIDArr, aStorageArr, bStorageArr, mAttrArr);
	            okay = true;
	        }
	        else if (cmd == CMD_ADD)
	        {
              mID = Integer.parseInt(st.nextToken());
              aStorage = Integer.parseInt(st.nextToken());
              bStorage = Integer.parseInt(st.nextToken());
              mAttr = st.nextToken().toCharArray();
	            userSolution.add(mID, aStorage, bStorage, mAttr);
	        }
	        else if (cmd == CMD_REMOVE)
	        {
              mID = Integer.parseInt(st.nextToken());
	            userSolution.remove(mID);
	        	
	        }
	        else if(cmd == CMD_TRANSPORT)
	        {
              sStorage = Integer.parseInt(st.nextToken());
              eStorage = Integer.parseInt(st.nextToken());
              ans = Integer.parseInt(st.nextToken());
              mAttr = st.nextToken().toCharArray();
	        	ret = userSolution.transport(sStorage, eStorage, mAttr);	        	
	        	if(ans != ret)
	        	{
	        		okay = false;
	        	}
	        }
	    }
	    return okay;
  }

  public static void main(String[] args) throws Exception {
      int T, MARK;

      // System.setIn(new java.io.FileInputStream("./sample_input.txt"));
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