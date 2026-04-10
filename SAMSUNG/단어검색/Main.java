package 단어검색;

import java.io.*;
import java.util.*;

class Main
{	
	private static final int CMD_INIT 		= 1;
	private static final int CMD_ADD_STR 	= 2;
	private static final int CMD_DEL_STR 	= 3;
	private static final int CMD_SEARCH_STR	= 4;
	
	private static UserSolution usersolution = new UserSolution();

	private static boolean run(BufferedReader br) throws Exception 
	{
    	String str;
    	int cmd, ans, ret;
    	boolean okay = false; 
    	
    	int Q = Integer.parseInt(br.readLine());
    	
    	for (int q = 0; q < Q; ++q)
    	{
    		StringTokenizer st = new StringTokenizer(br.readLine(), " ");			
            cmd = Integer.parseInt(st.nextToken());
            switch(cmd)
            {
            case CMD_INIT:
            	usersolution.init();
            	okay = true;
            	break;
            	
            case CMD_ADD_STR:
            	str = st.nextToken();
            	ans = Integer.parseInt(st.nextToken());
            	ret = usersolution.addStr(str); 
            	if(ans != ret) 
            		okay = false; 
            	break;
            
            case CMD_DEL_STR:
            	str = st.nextToken();
            	ans = Integer.parseInt(st.nextToken());
            	ret = usersolution.deleteStr(str); 
            	if(ans != ret) 
            		okay = false; 
            	break;
            	
            case CMD_SEARCH_STR:
            	str = st.nextToken();
            	ans = Integer.parseInt(st.nextToken());
            	ret = usersolution.searchStr(str); 
            	if(ans != ret) 
            		okay = false; 
            	break;
 
            default:
            	okay = false;
            	break;
            }
    	}
 
    	return okay;
	}
    
    public static void main(String[] args) throws Exception
    {
        //System.setIn(new java.io.FileInputStream("src/input.txt"));

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