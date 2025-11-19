package pro_edu.dijkstra.movement;

import java.io.*;
import java.util.*;

class Main {
	private static final int CMD_INIT 		= 0;
	private static final int CMD_ROAD 		= 1;
	private static final int CMD_BIKE	 	= 2;
	private static final int CMD_MONEY 		= 3; 

	private static UserSolution2 userSolution = new UserSolution2();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;

	private static boolean run(BufferedReader br) throws Exception {

		int Q, N, K;
		int maxTime;
		int ret, ans;
		boolean ok = false;
		int[] spotA = new int[30];
		int[] spotB = new int[30];
		int[] dis = new int[30];
		
		st = new StringTokenizer(br.readLine(), " ");
		Q = Integer.parseInt(st.nextToken()); 

		for (int q = 0; q < Q; ++q) {

			st = new StringTokenizer(br.readLine(), " ");
			int query = Integer.parseInt(st.nextToken());

			switch (query) {

			case CMD_INIT:
				N = Integer.parseInt(st.nextToken());
				userSolution.init(N); 
				ok = true;
				break;
			case CMD_ROAD:
				K = Integer.parseInt(st.nextToken());
				for(int i = 0; i < K; i++) {
					st = new StringTokenizer(br.readLine(), " "); 
					spotA[i] = Integer.parseInt(st.nextToken());
					spotB[i] = Integer.parseInt(st.nextToken());
					dis[i] = Integer.parseInt(st.nextToken());
				}
				userSolution.addRoad(K, spotA, spotB, dis); 
				break;
			case CMD_BIKE:
				spotA[0] = Integer.parseInt(st.nextToken());
				userSolution.addBikeRent(spotA[0]);
				break;
			case CMD_MONEY:
				spotA[0] = Integer.parseInt(st.nextToken());
				spotB[0] = Integer.parseInt(st.nextToken());
				maxTime = Integer.parseInt(st.nextToken());
				ret = userSolution.getMinMoney(spotA[0], spotB[0], maxTime);
				ans = Integer.parseInt(st.nextToken());
				if(ans != ret)
					ok = false; 
				break;
			default:
				ok = false;
				break;
			}
		}
		return ok;
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