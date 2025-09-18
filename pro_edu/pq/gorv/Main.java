package pro_edu.pq.gorv;

import java.io.*;
import java.util.*;

class Main {
	private static final int CMD_INIT 		= 100;
	private static final int CMD_EXPAND 	= 200;
	private static final int CMD_CALCULATE 	= 300;
	private static final int CMD_DIVIDE		= 400;
	
	private static UserSolution userSolution = new UserSolution();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;
	
	private static boolean run(BufferedReader br) throws Exception {

		int[] population = new int[10000];
		int cmd, ans, ret;
		int Q = 0;
		int N, from, to, num; 
		boolean okay = false; 
		
		st = new StringTokenizer(br.readLine(), " ");
		Q = Integer.parseInt(st.nextToken());

		for (int q = 0; q < Q; ++q) {

			st = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(st.nextToken());

			switch (cmd) {

			case CMD_INIT:
				N = Integer.parseInt(st.nextToken());
				st = new StringTokenizer(br.readLine(), " ");
				for(int i = 0; i < N; i++)
					population[i] = Integer.parseInt(st.nextToken());
				userSolution.init(N, population);
                okay = true; 
				break;
				
			case CMD_EXPAND:
				num = Integer.parseInt(st.nextToken());
				ret = userSolution.expand(num); 
				ans = Integer.parseInt(st.nextToken());
				if(ret != ans)
					okay = false; 
				break;
				
			case CMD_CALCULATE:
				from = Integer.parseInt(st.nextToken());
				to = Integer.parseInt(st.nextToken());
				ret = userSolution.calculate(from, to);  
				ans = Integer.parseInt(st.nextToken());
				if(ret != ans)
					okay = false; 
				break;
				
			case CMD_DIVIDE:
				from = Integer.parseInt(st.nextToken());
				to = Integer.parseInt(st.nextToken());
				num = Integer.parseInt(st.nextToken());
				ret = userSolution.divide(from, to, num);  
				ans = Integer.parseInt(st.nextToken());
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
		// System.setIn(new java.io.FileInputStream("res/sample_input.txt"));

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