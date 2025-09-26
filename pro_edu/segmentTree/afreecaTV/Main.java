package pro_edu.segmentTree.afreecaTV;

import java.io.*;
import java.util.*;

class Main {
	private static final int CMD_INIT 		 = 100;
	private static final int CMD_SUBSCRIBE	 = 200;
	private static final int CMD_UNSUBSCRIBE = 300;
	private static final int CMD_COUNT		 = 400; 
	private static final int CMD_CALCULATE 	 = 500; 

	private static UserSolution userSolution = new UserSolution();
	
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;

	private static boolean run(BufferedReader br) throws Exception {
		
		int n, mId, mNum, sId, eId;
		int cmd, ans, ret;
		int[] mSubscriber; 
		boolean okay = false; 
		
		st = new StringTokenizer(br.readLine(), " ");
		int Q = Integer.parseInt(st.nextToken());

		for (int q = 0; q < Q; ++q) {
			
			st = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(st.nextToken());
			
			switch(cmd) {
			
			case CMD_INIT:
				okay = true;
				n = Integer.parseInt(st.nextToken());
				mSubscriber = new int[n]; 
				for(int j = 0; j < n; j++) 
					mSubscriber[j] = Integer.parseInt(br.readLine());
                userSolution.init(n, mSubscriber); 
				break; 
				
			case CMD_SUBSCRIBE:
				mId = Integer.parseInt(st.nextToken());
				mNum = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = userSolution.subscribe(mId, mNum);
				if(ans != ret) 
					okay = false; 
				break;
				
			case CMD_UNSUBSCRIBE:
				mId = Integer.parseInt(st.nextToken());
				mNum = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = userSolution.unsubscribe(mId, mNum);
				if(ans != ret) 
					okay = false; 
				break;
				
			case CMD_COUNT:
				sId = Integer.parseInt(st.nextToken());
				eId = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = userSolution.count(sId, eId);
                if(ans != ret)
//					okay = false; 
				break;
			
			case CMD_CALCULATE:
				sId = Integer.parseInt(st.nextToken());
				eId = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = userSolution.calculate(sId, eId);
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
	
	public static void main(String[] args) throws Exception {
		//System.setIn(new java.io.FileInputStream("sample_input.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");

		int TC = Integer.parseInt(stinit.nextToken());
		int MARK = Integer.parseInt(stinit.nextToken());
		
		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		br.close();
	}
}