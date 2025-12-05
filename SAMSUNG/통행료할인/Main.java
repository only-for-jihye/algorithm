package 통행료할인;

import java.io.*;
import java.util.*;

class Main {
	private static final int CMD_INIT 		= 100;
	private static final int CMD_ADD 		= 200;
	private static final int CMD_REMOVE 	= 300;
	private static final int CMD_COST 		= 400; 

	private static UserSolution userSolution = new UserSolution();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;

	private static boolean run(BufferedReader br) throws Exception {

		int query_num;
		st = new StringTokenizer(br.readLine(), " ");
		query_num = Integer.parseInt(st.nextToken());
		
		int n, k, m;
		int[] mIdArr, sCityArr, eCityArr, mTollArr;
		int mId, sCity, eCity, mToll;
		int cmd, ans, ret;
		boolean okay = false;

		for (int q = 0; q < query_num; ++q) {

			st = new StringTokenizer(br.readLine(), " ");
			int query = Integer.parseInt(st.nextToken());

			switch (query) {

			case CMD_INIT:
				okay = true;
				n = Integer.parseInt(st.nextToken());
				k = Integer.parseInt(st.nextToken());
				mIdArr = new int[k];
				sCityArr = new int[k];
				eCityArr = new int[k];
				mTollArr = new int[k];
				for(int j = 0; j < k; ++j) {
					st = new StringTokenizer(br.readLine(), " ");
					mIdArr[j] = Integer.parseInt(st.nextToken());
					sCityArr[j] = Integer.parseInt(st.nextToken());
					eCityArr[j] = Integer.parseInt(st.nextToken());
					mTollArr[j] = Integer.parseInt(st.nextToken());
				}
				userSolution.init(n, k, mIdArr, sCityArr, eCityArr, mTollArr);
				break;
			case CMD_ADD:
				mId = Integer.parseInt(st.nextToken());
				sCity = Integer.parseInt(st.nextToken());
				eCity = Integer.parseInt(st.nextToken());
				mToll = Integer.parseInt(st.nextToken());
				userSolution.add(mId, sCity, eCity, mToll);
				break;
			case CMD_REMOVE:
				mId = Integer.parseInt(st.nextToken());
				userSolution.remove(mId);
				break;
			case CMD_COST:
				m = Integer.parseInt(st.nextToken());
				sCity = Integer.parseInt(st.nextToken());
				eCity = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = userSolution.cost(m, sCity, eCity);
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