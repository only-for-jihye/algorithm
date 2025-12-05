package 전기차여행;

import java.io.*;
import java.util.*;

class Main {
	private final static int MAX_N = 500;
	private final static int MAX_M = 5;
	private final static int MAX_K = 4000;
	private final static int CMD_INIT = 100;
	private final static int CMD_ADD = 200;
	private final static int CMD_REMOVE = 300;
	private final static int CMD_COST = 400;

//	private final static UserSolution usersolution = new UserSolution();
	private final static UserSolution3 usersolution = new UserSolution3();

	private static boolean run(BufferedReader br) throws Exception {
		int q = Integer.parseInt(br.readLine());

		int n, m, k, b;
		int[] mChargeArr = new int[MAX_N];
		int[] mIdArr = new int[MAX_K];
		int[] sCityArr = new int[MAX_K];
		int[] eCityArr = new int[MAX_K];
		int[] mTimeArr = new int[MAX_K];
		int[] mPowerArr = new int[MAX_K];
		int[] mCityArr = new int[MAX_M];
		int mId, sCity, eCity, mTime, mPower;
		int cmd, ans, ret = 0;
		boolean okay = false;
		StringTokenizer st;

		for (int i = 0; i < q; ++i) {
			st = new StringTokenizer(br.readLine());
			cmd = Integer.parseInt(st.nextToken());
			switch (cmd) {
				case CMD_INIT:
					okay = true;
					n = Integer.parseInt(st.nextToken());
					k = Integer.parseInt(st.nextToken());
					
					st = new StringTokenizer(br.readLine());
					for (int j = 0; j < n; ++j) {
						mChargeArr[j] = Integer.parseInt(st.nextToken());
					}
					for (int j = 0; j < k; ++j) {
						st = new StringTokenizer(br.readLine());
						mIdArr[j] = Integer.parseInt(st.nextToken());
						sCityArr[j] = Integer.parseInt(st.nextToken());
						eCityArr[j] = Integer.parseInt(st.nextToken());
						mTimeArr[j] = Integer.parseInt(st.nextToken());
						mPowerArr[j] = Integer.parseInt(st.nextToken());
					}
					usersolution.init(n, mChargeArr, k, mIdArr, sCityArr, eCityArr, mTimeArr, mPowerArr);
					break;
				case CMD_ADD:
					mId = Integer.parseInt(st.nextToken());
					sCity = Integer.parseInt(st.nextToken());
					eCity = Integer.parseInt(st.nextToken());
					mTime = Integer.parseInt(st.nextToken());
					mPower = Integer.parseInt(st.nextToken());
					usersolution.add(mId, sCity, eCity, mTime, mPower);
					break;
				case CMD_REMOVE:
					mId = Integer.parseInt(st.nextToken());
					usersolution.remove(mId);
					break;
				case CMD_COST:
					b = Integer.parseInt(st.nextToken());
					sCity = Integer.parseInt(st.nextToken());
					eCity = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					m = Integer.parseInt(st.nextToken());
					for (int j = 0; j < m; ++j) {
                        st = new StringTokenizer(br.readLine());
						mCityArr[j] = Integer.parseInt(st.nextToken());
						mTimeArr[j] = Integer.parseInt(st.nextToken());
					}
					ret = usersolution.cost(b, sCity, eCity, m, mCityArr, mTimeArr);
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

	public static void main(String[] args) throws Exception {
		int TC, MARK;

		//System.setIn(new java.io.FileInputStream("res/sample_input.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());  

		TC = Integer.parseInt(st.nextToken()); 
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}

		br.close();
	}
}