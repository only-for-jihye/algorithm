package 일정관리;

import java.io.*;
import java.util.*;

class Main
{	
	private static final int CMD_INIT 					= 100;
	private static final int CMD_ADD_SCHEDULE 			= 200;
	private static final int CMD_GET_SCHEDULE 			= 300;
	private static final int CMD_DELETE_SCHEDULE	 	= 400;
	private static final int CMD_FIND_EMPTY_SCHEDULE 	= 500;
	
	public static final class RESULT
	{
		String mTitle;
		int mStartDay;
		int mEndDay;
		
		RESULT()
		{
			mTitle = "$";
			mStartDay = mEndDay = -1;
		}
	}
	
	private static UserSolution usersolution = new UserSolution();

	private static boolean run(BufferedReader br) throws Exception 
	{
    	int Q, N;
    	
    	String mTitle;
    	int mStartDay, mEndDay, mDay, mForced;
    	
    	int ret = -1, ans;
    	
    	RESULT result;
    	
    	Q = Integer.parseInt(br.readLine());
    	
    	boolean okay = false;
    	
    	for (int q = 0; q <= Q; ++q)
    	{
    		int cmd;
    		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
    				
            cmd = Integer.parseInt(st.nextToken());
            switch(cmd)
            {
            case CMD_INIT:
            	N = Integer.parseInt(st.nextToken());
            	usersolution.init(N);
            	okay = true;
            	break;
            case CMD_ADD_SCHEDULE:
            	mTitle = st.nextToken();
            	mStartDay = Integer.parseInt(st.nextToken());
            	mEndDay = Integer.parseInt(st.nextToken());
            	mForced = Integer.parseInt(st.nextToken());
            	ret = usersolution.addSchedule(mTitle, mStartDay, mEndDay, mForced);
            	ans = Integer.parseInt(st.nextToken());
            	if (ans != ret)
            		okay = false;
            	break;
            case CMD_GET_SCHEDULE:
            	mDay = Integer.parseInt(st.nextToken());
            	result = usersolution.getSchedule(mDay);
            	mTitle = st.nextToken();
            	if (!mTitle.equals("$"))
            	{
            		mStartDay = Integer.parseInt(st.nextToken());
            		mEndDay = Integer.parseInt(st.nextToken());
            		if (!result.mTitle.equals(mTitle)
            				|| result.mStartDay != mStartDay
            				|| result.mEndDay != mEndDay)
            			okay = false;
            	}
            	else
            	{
            		if (!result.mTitle.equals("$"))
            			okay = false;
            	}
            	break;
            case CMD_DELETE_SCHEDULE:
            	mTitle = st.nextToken();
            	ret = usersolution.deleteSchedule(mTitle);
            	ans = Integer.parseInt(st.nextToken());
            	if (ans != ret)
            		okay = false;
            	break;            	
            case CMD_FIND_EMPTY_SCHEDULE:
            	ret = usersolution.findEmptySchedule();
            	ans = Integer.parseInt(st.nextToken());
            	if (ans != ret)
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
        System.setIn(new java.io.FileInputStream("SAMSUNG/일정관리/sample_input.txt"));

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