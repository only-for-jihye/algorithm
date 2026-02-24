package 최소경로차이;

import java.io.*;
import java.util.*;

class Main {
	private static final int CMD_INIT 		= 100; 
	private static final int CMD_ADD	 	= 200; 
	private static final int CMD_REMOVE	 	= 300;
	private static final int CMD_COST		= 400; 
	private static final int MAX_K 			= 1000; 

	private static UserSolution userSolution = new UserSolution();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;

	private static boolean run(BufferedReader br) throws Exception {

		int q, n, k;
		int[] mIdArr = new int[MAX_K];
		int[] sCityArr = new int[MAX_K];
		int[] eCityArr = new int[MAX_K];
		int[] mCostArr = new int[MAX_K];
		int mId, sCity, eCity, mCost; 
		int cmd, ans, ret;
		boolean okay = false; 
		
		q = Integer.parseInt(br.readLine());
		for (int i = 0; i < q; ++i) {

			st = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(st.nextToken());

			switch (cmd) {

			case CMD_INIT:
				n = Integer.parseInt(st.nextToken());
				k = Integer.parseInt(st.nextToken());
				for(int j = 0; j < k; ++j) {
					st = new StringTokenizer(br.readLine(), " ");
					mIdArr[j] = Integer.parseInt(st.nextToken());
					sCityArr[j] = Integer.parseInt(st.nextToken());
					eCityArr[j] = Integer.parseInt(st.nextToken());
					mCostArr[j] = Integer.parseInt(st.nextToken());
				}
				userSolution.init(n, k, mIdArr, sCityArr, eCityArr, mCostArr); 
				okay = true;
				break;
			case CMD_ADD:
				mId = Integer.parseInt(st.nextToken());
				sCity = Integer.parseInt(st.nextToken());
				eCity = Integer.parseInt(st.nextToken());
				mCost = Integer.parseInt(st.nextToken());
				userSolution.add(mId, sCity, eCity, mCost);
				break;
			case CMD_REMOVE:
				mId = Integer.parseInt(st.nextToken());
				userSolution.remove(mId);
				break;
			case CMD_COST:
				sCity = Integer.parseInt(st.nextToken());
				eCity = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = userSolution.cost(sCity, eCity);
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
		 System.setIn(new java.io.FileInputStream("SAMSUNG/최소경로차이/input.txt"));

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