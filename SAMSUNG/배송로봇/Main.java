package 배송로봇;

import java.io.*;
import java.util.*;

class Main {
    private static BufferedReader br;
    private static UserSolution userSolution = new UserSolution();

    private static final int MAX_E 					= 500;
    private final static int CMD_INIT 			= 100;
    private final static int CMD_ADD			= 200;
    private final static int CMD_DELIVER		= 300;

    private static boolean run() throws Exception {

        StringTokenizer stdin;

        int Q;
        int n, e, m, pos;
        String strTmp; 
        int[] sIdArr = new int[MAX_E];
        int[] eIdArr = new int[MAX_E];
        int[] mTimeArr = new int[MAX_E];
        int sId, eId, mTime;
        int cmd, ret, ans; 
        boolean okay = false;
        
        stdin = new StringTokenizer(br.readLine(), " ");
        Q = Integer.parseInt(stdin.nextToken());

        for (int q = 0; q < Q; q++) {
            stdin = new StringTokenizer(br.readLine(), " ");
            cmd = Integer.parseInt(stdin.nextToken());
            strTmp = stdin.nextToken();

            switch(cmd) {
			
			case CMD_INIT:
				okay = true; 
				stdin = new StringTokenizer(br.readLine(), " "); 
				strTmp = stdin.nextToken();
				n = Integer.parseInt(stdin.nextToken());
				strTmp = stdin.nextToken();
				e = Integer.parseInt(stdin.nextToken());
				for(int j = 0; j < e; ++j) {
					stdin = new StringTokenizer(br.readLine(), " "); 
					strTmp = stdin.nextToken();
					sIdArr[j] = Integer.parseInt(stdin.nextToken());
					strTmp = stdin.nextToken();
					eIdArr[j] = Integer.parseInt(stdin.nextToken());
					strTmp = stdin.nextToken();
					mTimeArr[j] = Integer.parseInt(stdin.nextToken());
				}
				userSolution.init(n, e, sIdArr, eIdArr, mTimeArr);
				break; 
				
			case CMD_ADD:
				stdin = new StringTokenizer(br.readLine(), " "); 
				strTmp = stdin.nextToken();
				sId = Integer.parseInt(stdin.nextToken());
				strTmp = stdin.nextToken();
				eId = Integer.parseInt(stdin.nextToken());
				strTmp = stdin.nextToken();
				mTime = Integer.parseInt(stdin.nextToken());
				userSolution.add(sId, eId, mTime);
				break;
				
			case CMD_DELIVER:
				stdin = new StringTokenizer(br.readLine(), " "); 
				strTmp = stdin.nextToken();
				pos = Integer.parseInt(stdin.nextToken());
				strTmp = stdin.nextToken();
				m = Integer.parseInt(stdin.nextToken()); 
				for(int j = 0; j < m; ++j) {
					stdin = new StringTokenizer(br.readLine(), " ");
					strTmp = stdin.nextToken();
					sIdArr[j] = Integer.parseInt(stdin.nextToken());
					strTmp = stdin.nextToken();
					eIdArr[j] = Integer.parseInt(stdin.nextToken());
				}
				stdin = new StringTokenizer(br.readLine(), " ");
				strTmp = stdin.nextToken();
				ans = Integer.parseInt(stdin.nextToken());
				ret = userSolution.deliver(pos, m, sIdArr, eIdArr);
				if(ret != ans) 
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
        int T, MARK;

         System.setIn(new java.io.FileInputStream("SAMSUNG/배송로봇/sample_input.txt"));
        br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");
        T = Integer.parseInt(stinit.nextToken());
        MARK = Integer.parseInt(stinit.nextToken());

        for (int tc = 1; tc <= T; tc++) {
            int score = run() ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }
        br.close();
    }
}