package 스마트팜;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Main {
	private static BufferedReader br;
//    private static UserSolution userSolution = new UserSolution();
    private static UserSolution_AI userSolution = new UserSolution_AI();

    private final static int CATEGORY_NUM = 3;

    private final static int CMD_INIT     = 100;
    private final static int CMD_SOW      = 200;
    private final static int CMD_WATER    = 300;
    private final static int CMD_HARVEST  = 400;
    
    private static boolean run() throws Exception {
    	StringTokenizer st = new StringTokenizer(br.readLine(), " ");
    	
        int Q = Integer.parseInt(st.nextToken());
        int N, mTime, mCategory, L, G, mRow, mCol, mHeight, mWidth, ans, ret;
		int[] mGrowthTime = new int[CATEGORY_NUM];

    	boolean okay = false;
	    for (int q = 0; q < Q; ++q)
	    {
        	st = new StringTokenizer(br.readLine(), " ");
	        int cmd = Integer.parseInt(st.nextToken());

	        if (cmd == CMD_INIT)
	        {
	            N = Integer.parseInt(st.nextToken());
                for(int i = 0; i < 3; ++i)
                    mGrowthTime[i] = Integer.parseInt(st.nextToken());
	            
	            userSolution.init(N, mGrowthTime);
	            okay = true;
	        }
	        else if (cmd == CMD_SOW)
	        {
                mTime = Integer.parseInt(st.nextToken());
                mRow = Integer.parseInt(st.nextToken());
                mCol = Integer.parseInt(st.nextToken());
                mCategory = Integer.parseInt(st.nextToken());
                ans = Integer.parseInt(st.nextToken());
	            ret = userSolution.sow(mTime, mRow, mCol, mCategory);
                if(ans != ret)
	        		okay = false;
	        }
	        else if (cmd == CMD_WATER)
	        {
                mTime = Integer.parseInt(st.nextToken());
                G = Integer.parseInt(st.nextToken());
                mRow = Integer.parseInt(st.nextToken());
                mCol = Integer.parseInt(st.nextToken());
                mHeight = Integer.parseInt(st.nextToken());
                mWidth = Integer.parseInt(st.nextToken());
                ans = Integer.parseInt(st.nextToken());
	            ret = userSolution.water(mTime, G, mRow, mCol, mHeight, mWidth);
                if(ans != ret)
	        		okay = false;
	        	
	        }
	        else if(cmd == CMD_HARVEST)
	        {
                mTime = Integer.parseInt(st.nextToken());
                L = Integer.parseInt(st.nextToken());
                mRow = Integer.parseInt(st.nextToken());
                mCol = Integer.parseInt(st.nextToken());
                mHeight = Integer.parseInt(st.nextToken());
                mWidth = Integer.parseInt(st.nextToken());
                ans = Integer.parseInt(st.nextToken());
	            ret = userSolution.harvest(mTime, L, mRow, mCol, mHeight, mWidth);
                if(ans != ret)
	        		okay = false;
	        }
	    }
	    return okay;
    }

    public static void main(String[] args) throws Exception {
        int T, MARK;

         System.setIn(new java.io.FileInputStream("SAMSUNG/SmartFarm/input.txt"));
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