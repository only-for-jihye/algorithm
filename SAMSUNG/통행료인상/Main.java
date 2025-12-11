package 통행료인상;

import java.util.Scanner;

class Main {
	private final static int MAX_E = 300;
	private final static int MAX_I = 20000;
	private final static int CMD_INIT = 100;
	private final static int CMD_ADD = 200;
	private final static int CMD_CALC = 300;

	private final static UserSolution usersolution = new UserSolution();

	private static boolean run(Scanner sc) {
		int q = sc.nextInt();

		int n, m, k;
		String strTmp;
		int[] sCityArr = new int[MAX_E];
		int[] eCityArr = new int[MAX_E];
		int[] mTollArr = new int[MAX_E];
		int[] mIncrease = new int[MAX_I];
		int[] mRet = new int[MAX_I + 1];
		int sCity, eCity, mToll;
		int cmd, ans, ret = 0;
		boolean okay = false;

		for (int i = 0; i < q; ++i) {
			cmd = sc.nextInt();
			strTmp = sc.next();
			switch (cmd) {
				case CMD_INIT:
					okay = true;
					strTmp = sc.next();
					n = sc.nextInt();
					strTmp = sc.next();
					k = sc.nextInt();
					for (int j = 0; j < k; ++j) {
						strTmp = sc.next();
						sCityArr[j] = sc.nextInt();
						strTmp = sc.next();
						eCityArr[j] = sc.nextInt();
						strTmp = sc.next();
						mTollArr[j] = sc.nextInt();
					}
					usersolution.init(n, k, sCityArr, eCityArr, mTollArr);
					break;
				case CMD_ADD:
					strTmp = sc.next();
					sCity = sc.nextInt();
					strTmp = sc.next();
					eCity = sc.nextInt();
					strTmp = sc.next();
					mToll = sc.nextInt();
					usersolution.add(sCity, eCity, mToll);
					break;
				case CMD_CALC:
					strTmp = sc.next();
					sCity = sc.nextInt();
					strTmp = sc.next();
					eCity = sc.nextInt();
					strTmp = sc.next();
					m = sc.nextInt();
					for (int j = 0; j < m; ++j) {
						strTmp = sc.next();
						mIncrease[j] = sc.nextInt();
					}
					usersolution.calculate(sCity, eCity, m, mIncrease, mRet);
					strTmp = sc.next();
					for (int j = 0; j <= m; ++j) {
						ans = sc.nextInt();
						if (ans != mRet[j])
							okay =false;
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

		System.setIn(new java.io.FileInputStream("SAMSUNG/TollgateIncrease/input.txt"));

		Scanner sc = new Scanner(System.in);

		TC = sc.nextInt();
		MARK = sc.nextInt();

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(sc) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}

		sc.close();
	}
}