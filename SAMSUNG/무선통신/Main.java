package 무선통신;

import java.io.*;
import java.util.*;

class Main {
	private static final int CMD_INIT 			= 0; 
	private static final int CMD_ADDRADIO	 	= 1; 
	private static final int CMD_GETPOWER		= 2;

	private static UserSolution userSolution = new UserSolution();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;
	
	private static boolean run(BufferedReader br) throws Exception {

		int Q, N, K, Limit;
		int[] id = new int[100];
		int[] freq = new int[100];
		int[] my = new int[100];
		int[] mx = new int[100]; 
		boolean ok = false;
		int ret, ans; 

		Q = Integer.parseInt(br.readLine());
		
		for (int q = 0; q < Q; ++q) {

			st = new StringTokenizer(br.readLine(), " ");
			int cmd = Integer.parseInt(st.nextToken());

			switch (cmd) {

			case CMD_INIT:
				N = Integer.parseInt(st.nextToken());
				Limit = Integer.parseInt(st.nextToken());
				userSolution.init(N, Limit);
				ok = true; 
				break;
			case CMD_ADDRADIO:
				K = Integer.parseInt(st.nextToken());
				for(int i = 0; i < K; i++) {
					st = new StringTokenizer(br.readLine(), " ");
					id[i] = Integer.parseInt(st.nextToken());
					freq[i] = Integer.parseInt(st.nextToken());
					my[i] = Integer.parseInt(st.nextToken());
					mx[i] = Integer.parseInt(st.nextToken());
				}
				userSolution.addRadio(K, id, freq, my, mx);
				break;
			case CMD_GETPOWER:
				id[0] = Integer.parseInt(st.nextToken());
				freq[0] = Integer.parseInt(st.nextToken());
				ret = userSolution.getMinPower(id[0], freq[0]);
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