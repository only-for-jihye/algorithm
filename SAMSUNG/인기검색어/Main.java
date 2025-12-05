package 인기검색어;

import java.io.*;
import java.util.*;

class Main {
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//    private static UserSolution userSolution = new UserSolution();
//    private static UserSolution3 userSolution = new UserSolution3();
    private static UserSolution4 userSolution = new UserSolution4();
    private static StringTokenizer st; 

    private final static int CMD_INIT = 100;
    private final static int CMD_ADD = 200;
    private final static int CMD_TOP = 300;
    
    private static boolean run() throws Exception {
    	boolean okay = false;
    	
	    int Q;
	    st = new StringTokenizer(br.readLine(), " "); 
	    Q = Integer.parseInt(st.nextToken()); 

	    for (int q = 0; q < Q; q++)
	    {
	    	st = new StringTokenizer(br.readLine(), " ");
	        int cmd = Integer.parseInt(st.nextToken());

	        if (cmd == CMD_INIT)
	        {
	            int N = Integer.parseInt(st.nextToken());
	            userSolution.init(N);          
	            okay = true;
	        }
	        else if (cmd == CMD_ADD)
	        {
	            String mKeyword = st.nextToken();
	            userSolution.addKeyword(mKeyword);
	        }
	        else if (cmd == CMD_TOP)
	        {
	        	String mRet[] = new String[5];
	        	int user_ans = userSolution.top5Keyword(mRet);
	        	int correct_ans = Integer.parseInt(st.nextToken());
	        	
	        	if(correct_ans != user_ans)
	        		okay = false;
	        	
	        	for(int i=0;i<correct_ans;i++)
	        	{
	        		String ans = st.nextToken();
	        		if(ans.equals(mRet[i]) == false)
	        		{
	        			okay = false;
	        		}
	        	}
	        	
	        }
	    }
	    return okay;
    }

    public static void main(String[] args) throws Exception {
        int T, MARK;

         System.setIn(new java.io.FileInputStream("SAMSUNG/인기검색어/sample_input.txt"));
        st = new StringTokenizer(br.readLine());

        T = Integer.parseInt(st.nextToken());
        MARK = Integer.parseInt(st.nextToken());

        for (int tc = 1; tc <= T; tc++) {
            int score = run() ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }
        br.close();
    }
}