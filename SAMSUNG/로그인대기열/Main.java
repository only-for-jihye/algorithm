package 로그인대기열;

import java.io.*;
import java.util.*;

class Main
{	
	private final static int MAX_NAME		= 10;

	private final static int CMD_INIT		= 0;
    private final static int CMD_LOGIN		= 1;
    private final static int CMD_CLOSE		= 2;
    private final static int CMD_CONNECT	= 3;
    private final static int CMD_ORDER		= 4;
    
    private static char[] nameID = new char [MAX_NAME];
    
    private static UserSolution usersolution = new UserSolution();
    
    private static void String2Char(String s, char[] b)
    {
        int n = s.length();
        for (int i = 0; i < n; ++i) b[i] = s.charAt(i);
        for (int i = n; i < MAX_NAME; ++i) b[i] = '\0';
    }  

    private static boolean run (BufferedReader br) throws Exception 
    {
		int cmd, ans, ret;

        int Q = 0;
        boolean okay = false;

        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        Q = Integer.parseInt(st.nextToken());
        for (int i = 0; i < Q; ++i)
        {
            st = new StringTokenizer(br.readLine(), " ");
            cmd = Integer.parseInt(st.nextToken());
            switch (cmd)
            {
            case CMD_INIT:
				usersolution.init();
				okay = true;
    			break;
   
    		case CMD_LOGIN:
				String2Char(st.nextToken(), nameID);
				usersolution.loginID(nameID);
    			break;
    
    		case CMD_CLOSE:
				ans = Integer.parseInt(st.nextToken());
				String2Char(st.nextToken(), nameID);
				ret = usersolution.closeIDs(nameID);
				if (ans != ret)
					okay = false;
    			break;
    
    		case CMD_CONNECT:
				ans = Integer.parseInt(st.nextToken());
				usersolution.connectCnt(ans);
				break;
	
    		case CMD_ORDER:
				ans = Integer.parseInt(st.nextToken());
				String2Char(st.nextToken(), nameID);
				ret = usersolution.waitOrder(nameID);
				if (ans != ret)
					okay = false;
    			break;
    
    		default:
    			okay = false;
    		}
    	}

    	return okay;
    }
    
    public static void main(String[] args) throws Exception
    {

        System.setIn(new java.io.FileInputStream("SAMSUNG/로그인대기열/sample_input.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer line = new StringTokenizer(br.readLine(), " ");

        int TC = Integer.parseInt(line.nextToken());
        int MARK = Integer.parseInt(line.nextToken());
        
        for (int testcase = 1; testcase <= TC; ++testcase)
        {
        	int score = run(br) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
        }
        
        br.close();
    }
}