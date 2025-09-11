package coffeeBakery;

//Main.java

import java.io.*;
import java.util.*;

class Main {
	private final static int MAX_E = 30000;
	private final static int MAX_SHOP = 1000;
	private final static int CMD_INIT = 100;
	private final static int CMD_ADD = 200;
	private final static int CMD_CALC = 300;
	
	private final static UserSolution usersolution = new UserSolution();
	// private final static UserSolution_answer usersolution = new UserSolution_answer();
	private static StringTokenizer st;
	private static boolean run(BufferedReader br)throws IOException {
		st = new StringTokenizer(br.readLine());
		int q =Integer.parseInt(st.nextToken());

		int n, k, m, p, r;
		int[] sBuildingArr = new int[MAX_E];
		int[] eBuildingArr = new int[MAX_E];
		int[] mDistArr = new int[MAX_E];
		int[] mCoffee = new int[MAX_SHOP];
		int[] mBakery = new int[MAX_SHOP];
		int sBuilding, eBuilding, mDist;
		int cmd, ans, ret = 0;
		boolean okay = false;

		for (int i = 0; i < q; ++i) {
			cmd = Integer.parseInt(br.readLine());
			switch (cmd) {
				case CMD_INIT:
					okay = true;
					st = new StringTokenizer(br.readLine());
					n = Integer.parseInt(st.nextToken());
					k = Integer.parseInt(st.nextToken());
					for (int j = 0; j < k; ++j) {
						st = new StringTokenizer(br.readLine());
						sBuildingArr[j] = Integer.parseInt(st.nextToken());
						eBuildingArr[j] = Integer.parseInt(st.nextToken());
						mDistArr[j] = Integer.parseInt(st.nextToken());
					}
					usersolution.init(n, k, sBuildingArr, eBuildingArr, mDistArr);
					break;
				case CMD_ADD:
					st = new StringTokenizer(br.readLine());
					sBuilding = Integer.parseInt(st.nextToken());
					eBuilding = Integer.parseInt(st.nextToken());
					mDist = Integer.parseInt(st.nextToken());
					usersolution.add(sBuilding, eBuilding, mDist);
					break;
				case CMD_CALC:
					st = new StringTokenizer(br.readLine());
					m = Integer.parseInt(st.nextToken());
					p = Integer.parseInt(st.nextToken());
					r = Integer.parseInt(st.nextToken());
					for (int j = 0; j < m; ++j) {
						mCoffee[j] = Integer.parseInt(br.readLine());
					}
					for (int j = 0; j < p; ++j) {
						mBakery[j] = Integer.parseInt(br.readLine());
					}
					ret = usersolution.calculate(m, mCoffee, p, mBakery, r);
					ans = Integer.parseInt(br.readLine());
					
					if (ans != ret) {
						okay =false;
						// System.out.println("ret : " + ret + ", ans = " + ans);
						}
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

		System.setIn(new java.io.FileInputStream("SAMSUNG/coffeeBakery/tc.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());

		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
	}
}