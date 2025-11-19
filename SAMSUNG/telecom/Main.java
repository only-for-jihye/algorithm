package telecom;

//Main.java

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Main {
	private final static int MAX_K = 10000;
	private final static int CMD_INIT = 100;
	private final static int CMD_ADD = 200;
	private final static int CMD_REMOVE = 300;
	private final static int CMD_CALC = 400;

	private final static UserSolution usersolution = new UserSolution();

	private static boolean run(BufferedReader br) throws Exception {
		int q = Integer.parseInt(br.readLine());

		int n, m, k;
		int[] mIdArr = new int[MAX_K];
		int[] sCityArr = new int[MAX_K];
		int[] eCityArr = new int[MAX_K];
		int[] mDistArr = new int[MAX_K];
		int mId, mCity, sCity, eCity, mDist;
		int cmd, ans, ret = 0;
		boolean okay = false;

		for (int i = 0; i < q; ++i) {
			StringTokenizer st = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(st.nextToken());
			switch (cmd) {
				case CMD_INIT:
					okay = true;
					n = Integer.parseInt(st.nextToken());
					m = Integer.parseInt(st.nextToken());
					k = Integer.parseInt(st.nextToken());
					for (int j = 0; j < k; ++j) {
						StringTokenizer road = new StringTokenizer(br.readLine(), " ");
						mIdArr[j] = Integer.parseInt(road.nextToken());
						sCityArr[j] = Integer.parseInt(road.nextToken());
						eCityArr[j] = Integer.parseInt(road.nextToken());
						mDistArr[j] = Integer.parseInt(road.nextToken());
					}
					usersolution.init(n, m, k, mIdArr, sCityArr, eCityArr, mDistArr);
					break;
				case CMD_ADD:
					mId = Integer.parseInt(st.nextToken());
					sCity = Integer.parseInt(st.nextToken());
					eCity = Integer.parseInt(st.nextToken());
					mDist = Integer.parseInt(st.nextToken());
					usersolution.add(mId, sCity, eCity, mDist);
					break;
				case CMD_REMOVE:
					mId = Integer.parseInt(st.nextToken());
					usersolution.remove(mId);
					break;
				case CMD_CALC:
					mCity = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					ret = usersolution.calculate(mCity);
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

		System.setIn(new java.io.FileInputStream("SAMSUNG/telecom/sample_input.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");

		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}

		br.close();
	}
}